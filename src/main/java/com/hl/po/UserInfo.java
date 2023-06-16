package com.hl.po;

import lombok.Data;

import java.util.Date;

/**
 * Creates Entity （实体化） through mapping
 */
// lombok @Data adds following functions: getters and setters, toString, hashCode, equals
@Data
public class UserInfo {
    private Long userId;
    private String username;
    private String userPassword;
    private Date createTime;
    private Date updateTime;
}
