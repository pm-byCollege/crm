package com.pm.crm.dao;

import com.pm.crm.base.BaseMapper;
import com.pm.crm.vo.User;

public interface UserMapper extends BaseMapper<User, Integer> {
    User queryUserByName(String userName);
}