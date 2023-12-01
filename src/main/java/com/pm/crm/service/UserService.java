package com.pm.crm.service;

import com.pm.crm.base.BaseService;
import com.pm.crm.dao.UserMapper;
import com.pm.crm.model.UserModel;
import com.pm.crm.utils.AssertUtil;
import com.pm.crm.utils.Md5Util;
import com.pm.crm.utils.UserIDBase64;
import com.pm.crm.vo.User;
import freemarker.template.utility.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
public class UserService extends BaseService<User, Integer> {

    @Resource
    private UserMapper userMapper;

    /*
    * 用户登录
    * */
    public UserModel userLogin(String username, String pwd) {
        checkLoginParams(username, pwd);
        User user = userMapper.queryUserByName(username);
        AssertUtil.isTrue(user == null, "用户名不存在");
        checkPwd(user.getUserPwd(), pwd);
        return buildUserInfo(user);
    }

    private UserModel buildUserInfo(User user) {
        UserModel userModel = new UserModel();
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    private void checkPwd(String userPwd, String pwd) {
        pwd = Md5Util.encode(pwd);
        AssertUtil.isTrue(!userPwd.equals(pwd), "用户密码不正确");
    }

    private void checkLoginParams(String username, String pwd) {
        AssertUtil.isTrue(StringUtils.isEmpty(username), "用户名不能为空");
        AssertUtil.isTrue(StringUtils.isEmpty(pwd), "密码不能为空");
    }

}
