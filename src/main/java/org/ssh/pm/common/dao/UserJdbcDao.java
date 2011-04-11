package org.ssh.pm.common.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springside.modules.utils.VelocityUtils;
import org.ssh.pm.common.entity.User;

import com.google.common.collect.Maps;

/**
 * User对象的Jdbc Dao, 演示Spring JdbcTemplate的使用.
 *
 * @author calvin
 */
@Component
public class UserJdbcDao {

    private static final String QUERY_USER_BY_ID = "select id, name, login_name from T_USERS where id=?";
    private static final String QUERY_USER_BY_IDS = "select id, name, login_name from T_USERS where id in(:ids)";
    private static final String QUERY_USER = "select id, name, login_name from SS_USER order by id";
    private static final String QUERY_USER_BY_LOGINNAME = "select id,name,login_name from T_USERS where login_name=:login_name";
    private static final String INSERT_USER = "insert into T_USERS(id, login_name, name) values(:id, :loginName, :name)";

    private static Logger logger = LoggerFactory.getLogger(UserJdbcDao.class);

    private SimpleJdbcTemplate jdbcTemplate;

    private TransactionTemplate transactionTemplate;

    private String searchUserSql;

    private UserMapper userMapper = new UserMapper();

    private class UserMapper implements RowMapper<User> {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setLoginName(rs.getString("login_name"));
            return user;
        }
    }

    //@Resource
     @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource) {
        jdbcTemplate = new SimpleJdbcTemplate(dataSource);
    }

    //@Resource
     @Autowired
    public void setDefaultTransactionManager(@Qualifier("defaultTransactionManager") PlatformTransactionManager defaultTransactionManager) {
        transactionTemplate = new TransactionTemplate(defaultTransactionManager);
    }

    public void setSearchUserSql(String searchUserSql) {
        this.searchUserSql = searchUserSql;
    }

    /**
     * 查询单个对象.
     */
    public User queryObject(String id) {
        return jdbcTemplate.queryForObject(QUERY_USER_BY_ID, userMapper, id);
    }

    /**
     * 查询对象列表.
     */
    public List<User> queryObjectList() {
        return jdbcTemplate.query(QUERY_USER, userMapper);
    }

    /**
     * 查询单个结果Map.
     */
    public Map<String, Object> queryMap(Long id) {
        return jdbcTemplate.queryForMap(QUERY_USER_BY_ID, id);
    }

    /**
     * 查询结果Map列表.
     */
    public List<Map<String, Object>> queryMapList() {
        return jdbcTemplate.queryForList(QUERY_USER);
    }

    /**
     * 使用Map形式的命名参数.
     */
    public User queryByNamedParameter(String loginName) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("login_name", loginName);

        return jdbcTemplate.queryForObject(QUERY_USER_BY_LOGINNAME, userMapper, map);
    }

    /**
     * 使用Map形式的命名参数.
     */
    public List<User> queryByNamedParameterWithInClause(Long... ids) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("ids", Arrays.asList(ids));

        return jdbcTemplate.query(QUERY_USER_BY_IDS, userMapper, map);
    }

    /**
     * 使用Bean形式的命名参数, Bean的属性名称应与命名参数一致.
     */
    public void createObject(User user) {
        //使用BeanPropertySqlParameterSource将User的属性映射为命名参数.
        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(user);
        jdbcTemplate.update(INSERT_USER, source);
    }

    /**
     * 批量创建/更新对象,使用Bean形式的命名参数.
     */
    public void batchCreateObject(List<User> userList) {
        int i = 0;
        BeanPropertySqlParameterSource[] sourceArray = new BeanPropertySqlParameterSource[userList.size()];

        for (User user : userList) {
            sourceArray[i++] = new BeanPropertySqlParameterSource(user);
        }

        jdbcTemplate.batchUpdate(INSERT_USER, sourceArray);
    }

    /**
     * 使用freemarker创建动态SQL.
     */
    public List<User> searchUserByFreemarkerSqlTemplate(Map<String, ?> conditions) {
        String sql = VelocityUtils.render(searchUserSql, conditions);
        logger.info(sql);
        return jdbcTemplate.query(sql, userMapper, conditions);
    }

    /**
     * 使用TransactionTemplate编程控制事务,一般在Manager/Service层
     * 无返回值的情形.
     */
    public void createUserInTransaction(User user) {

        final BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(user);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                jdbcTemplate.update(INSERT_USER, source);
            }
        });
    }

    /**
     * 使用TransactionTemplate编程控制事务,一般在Manager/Service层
     * 有返回值的情形,并捕获异常进行处理不再抛出的情形.
     */
    public boolean createUserInTransaction2(User user) {

        final BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(user);
        return transactionTemplate.execute(new TransactionCallback<Boolean>() {
            public Boolean doInTransaction(TransactionStatus status) {
                try {
                    jdbcTemplate.update(INSERT_USER, source);
                    return true;
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    status.setRollbackOnly();
                    return false;
                }
            }
        });
    }
}
