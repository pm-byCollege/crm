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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;

@Service
public class UserService extends BaseService<User, Integer> {

    @Resource
    private UserMapper userMapper;

    /*
    * 修改密码
    * */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePwd(Integer userId, String oldPwd, String newPwd, String repeatPwd) {
        User user = userMapper.selectByPrimaryKey(userId);
        AssertUtil.isTrue(null == user, "用户不存在");
        checkPasswordParams(user, oldPwd, newPwd, repeatPwd);
        // 设置新密码
        user.setUserPwd(Md5Util.encode(newPwd));

        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1, "修改密码失败");
    }

    private void checkPasswordParams(User user, String oldPwd, String newPwd, String repeatPwd) {
        AssertUtil.isTrue(StringUtils.isEmpty(oldPwd), "旧密码不能为空");
        AssertUtil.isTrue(StringUtils.isEmpty(newPwd), "新密码不能为空");
        AssertUtil.isTrue(StringUtils.isEmpty(repeatPwd), "确认密码不能为空");
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(oldPwd)) ,"原始密码不正确");
        AssertUtil.isTrue(oldPwd.equals(newPwd), "新密码不能与原始密码相同");
        AssertUtil.isTrue(!newPwd.equals(repeatPwd), "确认密码与新密码不一致");
    }

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
