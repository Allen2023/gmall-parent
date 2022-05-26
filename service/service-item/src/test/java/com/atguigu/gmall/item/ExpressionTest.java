package com.atguigu.gmall.item;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/26 18:22
 */

import org.junit.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Arrays;

/**
 * 自定义表达式功能
 * 文档：https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#expressions
 */
public class ExpressionTest {

   // @Test
   public void testExpression() {
        //1、准备一个表达式解析器
        SpelExpressionParser parser = new SpelExpressionParser();

        String spelStr = "#{new Integer(#args[1])}";
        //2、解析表达式
        TemplateParserContext parserContext = new TemplateParserContext();
        //3、表达式
        Expression expression = parser.parseExpression(spelStr, parserContext);

        StandardEvaluationContext context = new StandardEvaluationContext();
        //从上下文所有变量中找
        context.setVariable("args", Arrays.asList("22","44","55"));
        context.setVariable("nowTime", System.currentTimeMillis());
        Object value = expression.getValue(context, Object.class);
        System.out.println( value+":"+value.getClass());
    }
}
