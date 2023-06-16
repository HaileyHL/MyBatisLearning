package com.hl;

import com.hl.mapper.UserInfoMapper;
import com.hl.po.UserInfo;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Application {
    public static void main(String[] args) throws Exception {

//        // Method 1.0, using SqlSession directly to run Sql
          // gets one Mysql instance
//        getMysqlSimple();

//        // Method 2.0 onwards, using Mapper get
          // gets one Mysql instance
//        getMysqlInstanceThroughMapper();

//        // Method 2.1 gets all Mysql instances from table
//        getAllMysqlInstance();

//        // Method 2.2 updates username by id
//        updateMysqlUsername();

//        // Method 2.3 insert user given name and password
//        insertMysqlUserInfo();

        // Method 2.4 delete
        SqlSession sqlSession = getSqlSession();
        UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
        mapper.deleteUserById(3L);
        sqlSession.commit();
        sqlSession.close();
    }

    /**
     * Construct SqlSessionFactory with XML.
     * This part is normally achieved with Spring in workplace
     */
    private static SqlSession getSqlSession() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        return sqlSession;
    }

    /**
     * Method 1.0
     * Selects one instance based on id
     */
    private static void getMysqlSimple() throws IOException {
        SqlSession sqlSession = getSqlSession();

        // SqlSession achieves communication with database
        // the parameter 传参 matches with our query for a value in 'where user_id = #{id}'
        UserInfo user = sqlSession.selectOne("selectUserInfoById", 3);
        System.out.println(user);
        sqlSession.close(); // releases resources
    }

    /**
     * Method 2.0
     * Selects one instance based on id
     */
    private static void getMysqlInstanceThroughMapper() throws IOException {
        SqlSession sqlSession = getSqlSession();
        UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
        UserInfo userInfo = mapper.selectUserInfoById(1L); // 1L means Long type 1
        System.out.println(userInfo);
        sqlSession.close();
    }

    /**
     * Method 2.1
     * Selects all instances of table
     */
    private static void getAllMysqlInstance() throws IOException {
        SqlSession sqlSession = getSqlSession();
        UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
        List<UserInfo> userInfos = mapper.selectAllUsers();
        userInfos.forEach(e -> System.out.println("userInfo" + e));
        sqlSession.close();
    }

    /**
     * Method 2.2
     */
    private static void updateMysqlUsername() throws IOException {
        SqlSession sqlSession = getSqlSession();
        UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
        mapper.updateUsernameById("test100", 3L); // 执行
        sqlSession.commit(); // commit 更新到数据库
        sqlSession.close();
    }

    /**
     * Method 2.3
     */
    private static void insertMysqlUserInfo() throws IOException {
        SqlSession sqlSession = getSqlSession();
        UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("test000");
        userInfo.setUserPassword("xxx");
        mapper.insertUserInfo(userInfo);
        sqlSession.commit();
        sqlSession.close();
    }
}
