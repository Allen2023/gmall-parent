package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.list.SearchFeignClient;
import com.atguigu.gmall.model.list.SearchParam;
import com.atguigu.gmall.model.vo.GoodsSearchResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/30 1:05
 */
@Controller
public class ListController {
    @Autowired
    SearchFeignClient searchFeignClient;

    @GetMapping("/list.html")
    public String searchPage(SearchParam param, Model model) {
        //order不要; category3Id=61&props=23:4G:运行内存&props=106:安卓手机:手机一级&order=xx

        //TODO 远程调用检索服务去检索
        Result<GoodsSearchResultVo> searchGoods = searchFeignClient.searchGoods(param);

        if(searchGoods.isOk()){
            GoodsSearchResultVo data = searchGoods.getData();
            //展示数据到页面
            //1、原来参数原封不动给页面
            model.addAttribute("searchParam",data.getSearchParam());

            //2、品牌面包屑；  例如： 品牌：VIVO
            model.addAttribute("trademarkParam",data.getTrademarkParam());

            //3、url参数
            model.addAttribute("urlParam",data.getUrlParam());

            //4、平台属性面包屑：propsParamList；  集合里面每个元素（attrName/attrValue）
            model.addAttribute("propsParamList", data.getPropsParamList());

            //5、检索条件区：品牌列表 ； 集合里面的每个元素（tmId、tmLogoUrl、tmName）
            model.addAttribute("trademarkList",data.getTrademarkList());

            //6、检索条件区：平台属性列表： attrsList每个元素（attrId、attrName、attrValueList）
            model.addAttribute("attrsList",data.getAttrsList());

            //7、排序信息： Bean（type、sort）
            model.addAttribute("orderMap",data.getOrderMap());

            //8、查到的商品列表： goodsList 集合中每个元素（id、defaultImg、price、title、）
            model.addAttribute("goodsList",data.getGoodsList());

            //9、分页信息：当前页、总页数
            model.addAttribute("pageNo",data.getPageNo());
            model.addAttribute("totalPages",data.getTotalPages());
        }


        //来到检索页
        return "list/index";
    }
}
