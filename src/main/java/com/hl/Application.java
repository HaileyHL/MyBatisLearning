package com.hl;

import com.hl.mapper.UserInfoMapper;
import com.hl.po.UserInfo;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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

//        // Method 2.4 delete
//        deleteSqlUserInfo();

//        // Method 3 example that demonstrates difference between # and $
//        querySqlUserInfo();

//        // Method 4 Dynamic Sql. This example makes password optional
//        queryUserInfoByUsernameOrPwd();

//        // Method 4.1 Another Example of Dynamic Sql
//        queryUserInfoByParam();

//        // Method 5.0 Select by page basic
//        selectUserInfoByPages(1, 3);

//        // Method 5.1 Select by pageHelper add-on (could do conditionals
//        // see pageHelper API


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

    /**
     * Method 2.4
     */
    private static void deleteSqlUserInfo() throws IOException {
        SqlSession sqlSession = getSqlSession();
        UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
        mapper.deleteUserById(3L);
        sqlSession.commit();
        sqlSession.close();
    }

    /**
     * Method 3 little practice
     * '#' logic: use a placeholder when preparing the sql line;
     *   pass in the entries as parameters
     * '$' logic: enter the parameters at sql line.
     *   stings such as "name1" need to be made "'name1'" in order to pass
     *
     * recommended using '#' in workplace. '$' at risk of sql injection attack
     */
    private static void querySqlUserInfo() throws IOException {
        SqlSession sqlSession = getSqlSession();
        UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);

        // Don't use:
        // UserInfo userInfo0 = mapper.queryUserInfoByUsernameAndPwd("'test' or 1 = 1", "123456"); // '$'
        // Instead, use:
        UserInfo userInfo = mapper.queryUserInfoByUsernameAndPwd("test", "123456"); // '#'

        System.out.println(userInfo);
        sqlSession.close();
    }

    /**
     * Method 4.0 Dynamic SQL
     * See https://mybatis.org/mybatis-dynamic-sql/ for detailed APIs.
     */
    private static void queryUserInfoByUsernameOrPwd() throws IOException {
        SqlSession sqlSession = getSqlSession();
        UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
        UserInfo userInfo = new UserInfo();

        // stream() supports the following sql-like data processing operations
        List<UserInfo> userInfos = mapper.queryUserInfoByUsernameOrPwd(userInfo).stream()
                .filter(e-> !Objects.equals(e.getUserPassword(),"xxx"))
                .sorted(Comparator.comparing(UserInfo::getUpdateTime))
                .collect(Collectors.toList());
        userInfos.forEach(e -> System.out.println("userInfo" + e));
        sqlSession.close();
    }


    /**
     * Method 4.1 Dynamic SQL Continued
     */
    private static void queryUserInfoByParam() throws IOException {
        SqlSession sqlSession = getSqlSession();
        UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);

        UserInfo userInfo = new UserInfo();

        Map<String, List<UserInfo>> map = mapper.queryUserInfoByParam(userInfo)
                .stream()
                .collect(Collectors.groupingBy(UserInfo::getUsername));
        map.forEach((k,v)->{
            System.out.println("name: " + k + ", value: " + v);
        });

        sqlSession.close();
    }

    /**
     * Method 5 Select by Page 分页查询
     */
    private static void selectUserInfoByPages(Integer pageIndex, Integer pageSize) throws IOException {
        SqlSession sqlSession = getSqlSession();
        UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
        List<UserInfo> userInfos = mapper.selectUserInfoByPages(pageIndex, pageSize);

        userInfos.forEach(e -> System.out.println("userInfo" + e));
        sqlSession.close();
    }


}
