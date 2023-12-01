package com.pm.crm;

import com.alibaba.fastjson.JSON;
import com.pm.crm.base.ResultInfo;
import com.pm.crm.exceptions.NoLoginException;
import com.pm.crm.exceptions.ParamsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/*
* 全局异常统一处理
* */
@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {

    /*
    * 异常处理方法
    *   方法返回值 1、 返回视图  2、返回数据 json
    * */

    /**
     *
     * @param httpServletRequest request请求对象
     * @param httpServletResponse 响应对象
     * @param o 方法对象
     * @param e 异常对象
     * @return
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {

        /*
        * 非法请求拦截
        *   未登录异常
        * */
        if (e instanceof NoLoginException) {
            ModelAndView mv = new ModelAndView("redirect:/index");
            return mv;
        }

        ModelAndView modalAndView = new ModelAndView("error");
        modalAndView.addObject("code", 500);
        modalAndView.addObject("msg", "异常，请重试...");

        // 通过反射判断方法对象是否声明responseBody注解
        if (o instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) o;
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);

            if (responseBody == null) {
                // 返回视图
                if (e instanceof ParamsException) {
                    ParamsException paramsException = (ParamsException) e;
                    modalAndView.addObject("code", paramsException.getCode());
                    modalAndView.addObject("msg", paramsException.getMsg());
                }
                return modalAndView;
            } else {
                // 返回数据
                // 默认的异常处理
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(500);
                resultInfo.setMsg("网络异常，请重试");

                // 判断是否为自定义异常
                if (e instanceof ParamsException) {
                    ParamsException paramsException = (ParamsException) e;
                    modalAndView.addObject("code", paramsException.getCode());
                    modalAndView.addObject("msg", paramsException.getMsg());
                }
                httpServletResponse.setContentType("application/json;charset=utf-8");
                PrintWriter out = null;
                try {
                    out = httpServletResponse.getWriter();
                    String json = JSON.toJSONString(resultInfo);
                    out.write(json);
                } catch (IOException exception) {
                    exception.printStackTrace();
                } finally {
                    if (out != null) {
                        out.close();
                    }
                }
                return null;
            }
        }
        return modalAndView;
    }
}
