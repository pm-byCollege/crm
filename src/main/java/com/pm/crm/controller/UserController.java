package com.pm.crm.controller;

import com.pm.crm.base.BaseController;
import com.pm.crm.base.ResultInfo;
import com.pm.crm.exceptions.ParamsException;
import com.pm.crm.model.UserModel;
import com.pm.crm.service.UserService;
import com.pm.crm.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @PostMapping("login")
    @ResponseBody
    public ResultInfo login(String userName, String userPwd){

        ResultInfo resultInfo = new ResultInfo();
        UserModel userModel = userService.userLogin(userName, userPwd);
        resultInfo.setResult(userModel);
//        try {
//            UserModel userModel = userService.userLogin(userName, userPwd);
//            resultInfo.setResult(userModel);
//        } catch (ParamsException paramsException) {
//            resultInfo.setCode(paramsException.getCode());
//            resultInfo.setMsg(paramsException.getMsg());
//            paramsException.printStackTrace();
//        } catch (Exception e) {
//            resultInfo.setCode(500);
//            resultInfo.setMsg("登录失败");
//        }

        return resultInfo;
    }

    @PostMapping("user/updatePwd")
    @ResponseBody
    public ResultInfo updateUserPassword(HttpServletRequest request, String oldPassword, String newPassword, String repearPassword) {
        ResultInfo resultInfo = new ResultInfo();
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        userService.updatePwd(userId, oldPassword, newPassword, repearPassword);
//        try {
//            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
//            userService.updatePwd(userId, oldPassword, newPassword, repearPassword);
//        }catch (ParamsException paramsException) {
//            resultInfo.setCode(paramsException.getCode());
//            resultInfo.setMsg(paramsException.getMsg());
//            paramsException.printStackTrace();
//        } catch (Exception e) {
//            resultInfo.setCode(500);
//            resultInfo.setMsg("修改密码失败");
//            e.printStackTrace();
//        }
        return resultInfo;
    }

    /*
    * 进入修改密码的页面
    * */
    @RequestMapping("toPasswordPage")
    public String toPasswordPage() {
        return "user/password";
    }
}
