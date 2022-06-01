package com.atguigu.gmall.gateway.filter;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.common.util.IpUtil;
import com.atguigu.gmall.gateway.properties.UserAuthProperties;
import com.atguigu.gmall.model.user.UserInfo;
import com.atguigu.gmall.starter.constants.RedisConst;
import com.atguigu.gmall.starter.util.JSONs;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;


/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/31 19:49
 */
@Slf4j
@Component
public class UserAuthFilter implements GlobalFilter {
    @Autowired
    UserAuthProperties authProperties;

    //ant风格路径匹配器
    AntPathMatcher matcher = new AntPathMatcher();

    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * 所有请求到达目标服务之前，都得先过这个方法
     *
     * @param exchange 代表原来的请求和响应
     * @param chain    代表原来的过滤器链
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //非响应式：HttpServletRequest、HttpServletResponse；Servlet-API
        //响应式： ServerHttpRequest、ServerHttpResponse
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        log.info("UserAuthFilter 开始拦截；请求路径：{}", path);
        //==========1.进行认证,鉴权=======================

        List<String> anyoneurls = authProperties.getAnyoneurls();
        //1.判断静态文件路径
        for (String anyoneurl : anyoneurls) {
            boolean match = matcher.match(anyoneurl, path);
            if (match) {
                return chain.filter(exchange);
            }
        }
        //2.判断任何情况下都不能通过浏览器直接访问
        List<String> denyurls = authProperties.getDenyurls();
        for (String denyurl : denyurls) {
            boolean match = matcher.match(denyurl, path);
            if (match) {
                //1.构造响应
                Result result = Result.build("", ResultCodeEnum.FORBIDDEN);
                //2.转成Json
                String str = JSONs.toStr(result);
                //3.得到DataBuffer
                DataBuffer wrap = exchange.getResponse().bufferFactory().wrap(str.getBytes(StandardCharsets.UTF_8));
                //4.将DataBuffer发布出去 Publisher<? extends DataBuffer>
                Mono<DataBuffer> body = Mono.just(wrap);
                //5.防止中文乱码 在响应头告诉浏览器文本编码格式
                exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
                //6.Response 订阅数据
                return exchange.getResponse().writeWith(body);
            }
        }

        //3.判断 必选登录才能访问
        for (String authurl : authProperties.getAuthurls()) {
            boolean match = matcher.match(authurl, path);
            if (match) {
                //判断是否登录 登录即可访问
                boolean check = validateToken(request);
                if (!check) {
                    //token 有问题 或者 没带token 重定向到登录页面
                    return SendRedirectLoginPage(exchange);
                }
            }
        }

        //4.正常请求
        String token = getToken(request);
        if (StringUtils.isEmpty(token)) {
            //没带token,直接响应
            return chain.filter(exchange);
        } else
        {
            //带了token 检验token是否和redis里的是否一致
            boolean validate = validateToken(request);
            if (!validate) {
                //token错误
                return SendRedirectLoginPage(exchange);
            } else {
                //token正确 用户id穿透服务
                ServerHttpRequest originRequest = exchange.getRequest();
                UserInfo userInfo = getTokenRedisValue(token, IpUtil.getGatwayIpAddress(originRequest));
                //从原请求克隆一个新的请求
                ServerHttpRequest cloneRequest = exchange.getRequest().mutate()
                        .header("UserId", userInfo.getId().toString())
                        .build();

                //从原exchange克隆一个新的exchange放克隆的请求
                ServerWebExchange cloneExchange = exchange.mutate()
                        .request(cloneRequest)
                        .response(exchange.getResponse())
                        .build();

                return chain.filter(cloneExchange);
            }
        }
    }

    /**
     * 重定向到登录页面
     *
     * @param exchange
     * @return
     */
    private Mono<Void> SendRedirectLoginPage(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        //1.设置响应码 httpcode :302
        response.setStatusCode(HttpStatus.FOUND);
        //2.响应头 Location: 新位置
        String originUrl = request.getURI().toString();
        URI uri = URI.create(authProperties.getLoginPage() + "?originUrl=" + originUrl);
        response.getHeaders().setLocation(uri);

        //3.响应结束
        //4.命令浏览器删除之前的假令牌(删除cookie,服务器只需要给浏览器发一个同名的cookie)
        ResponseCookie cookie = ResponseCookie
                .from("token", "8383279")
                .maxAge(0L)
                .domain(".gmall.com")
                .build();
        response.addCookie(cookie);

        return response.setComplete();

    }


    /**
     * 获取前端的token
     *
     * @param request
     * @return
     */
    private String getToken(ServerHttpRequest request) {
        String token = "";
        HttpCookie cookie = request.getCookies().getFirst("token");
        //有这个cookie，说明前端把token放到的cookie位置，给我们带来了
        if(cookie!=null){
            token = cookie.getValue();
        }else {
            //前端没有放在cookie位置
            String headerToken = request.getHeaders().getFirst("token");
            token = headerToken;
        }
        return token;
    }


    /**
     * 验证令牌 1、获取前端带来的token 2、如果有token就去redis查一下
     * 判断是否登录
     *
     * @param request
     * @return
     */

    private boolean validateToken(ServerHttpRequest request) {
        //1、获取到token；【Cookie:token=xxxx】【直接在头中有个token=xxx】
        String token = "";

        HttpCookie cookie = request.getCookies().getFirst("token");
        //有这个cookie，说明前端把token放到的cookie位置，给我们带来了
        if(cookie!=null){
            token = cookie.getValue();
        }else {
            //前端没有放在cookie位置
            String headerToken = request.getHeaders().getFirst("token");
            token = headerToken;
        }

        if(StringUtils.isEmpty(token)){
            //前端没有带token
            return false;
        }else {
            //前端带了 token；校验一下
            //  user:login:token 查询下redis中真正的值
            String ipAddress = IpUtil.getGatwayIpAddress(request);
            UserInfo loginUser = getTokenRedisValue(token, ipAddress);
            if(loginUser == null){
                //用户没登录或者假登录
                return false;
            }
            return true;
        }
    }


    /**
     * 查询Redis中这个token对应的值
     *
     * @param token
     * @param ipAddress
     * @return
     */

    public UserInfo getTokenRedisValue(String token, String ipAddress) {
        String json = redisTemplate.opsForValue().get(RedisConst.USER_LOGIN_PREFIX + token);
        if (StringUtils.isEmpty(json)) {
            return null;
        } else {
            //redis有数据
            UserInfo userInfo = JSONs.strToObject(json, new TypeReference<UserInfo>() {
            });
            //判断查询用户的当前ip是否相等,不相等则可能发生盗用
            if (!userInfo.getIpAddr().equals(ipAddress)) {
                //ip不相等
                return null;
            }
            return userInfo;
        }
    }
}
