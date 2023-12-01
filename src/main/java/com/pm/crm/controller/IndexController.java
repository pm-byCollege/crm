package com.pm.crm.controller;

import com.pm.crm.base.BaseController;
import com.pm.crm.service.UserService;
import com.pm.crm.utils.LoginUserUtil;
import com.pm.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
//@RestController
public class IndexController extends BaseController {

    @Resource
    private UserService userService;

    /**
     * 系统登录页
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "index";
    }

    /*
     * 系统界面欢迎页
     * */
    @RequestMapping("welcome")
    public String welcome() {
        return "welcome";
    }

    @RequestMapping("main")
    public String main(HttpServletRequest httpServletRequest){
        // session
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(httpServletRequest);
        User user = userService.selectByPrimaryKey(userId);
        httpServletRequest.getSession().setAttribute("user", user);

        return "main";
    }
}
