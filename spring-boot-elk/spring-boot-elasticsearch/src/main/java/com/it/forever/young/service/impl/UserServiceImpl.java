package com.it.forever.young.service.impl;

import com.it.forever.young.dao.UserDao;
import com.it.forever.young.entity.User;
import com.it.forever.young.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jikzhang
 * @version 1.0
 * @date 2019/8/5 16:40
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao dao;

    @Override
    public List<User> findList() {
        return dao.findList();
    }
}
