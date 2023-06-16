package com.hl.mapper;

import com.hl.po.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserInfoMapper {
    /**
     * get userInfo based on primary key 'id'.
     * Method name matches the name in UserInfoMapper.xml
     */
    UserInfo selectUserInfoById(Long id);

    /**
     * view all userInfo
     */
    List<UserInfo> selectAllUsers();

    /**
     * Update userInfo
     */
    Integer insertUserInfo(UserInfo userInfo);

    /**
     * update username based on primary key 'id'
     * @Param 注解绑定name和id. Note that 'name1' matches 'name1' in UserInfoMapper
     * Use @Param when there are 2+ parameters
     */
    Integer updateUsernameById(@Param("name1") String name, @Param("id") Long id);

    /**
     * delete user by primary key 'id'
     */
    Integer deleteUserById(Long id);
}
