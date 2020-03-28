package com.it.forever.young.dao;

import com.it.forever.young.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author jikzhang
 * @version 1.0
 * @className UserDao
 * @date 2019/8/5 16:31
 * @description TODO
 */
@Mapper
public interface UserDao {

    List<User> findList();

    User get(@Param("id") Long id);

}
