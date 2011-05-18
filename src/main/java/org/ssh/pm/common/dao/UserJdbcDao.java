package org.ssh.pm.common.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springside.modules.utils.DBUtils;
import org.springside.modules.utils.UtilDateTime;
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

    private SimpleJdbcTemplate simpleJdbcTemplate;

    private JdbcTemplate jdbcTemplate;

    private NamedParameterJdbcTemplate njdbcTemplate;

    private TransactionTemplate transactionTemplate;

    private String searchUserSql;

    private UserMapper userMapper = new UserMapper();

    private class UserMapper implements RowMapper<User> {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setLoginName(rs.getString("login_name"));
            return user;
        }
    }

    //@Resource
     @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource) {
        simpleJdbcTemplate = new SimpleJdbcTemplate(dataSource);
        jdbcTemplate = new JdbcTemplate(dataSource);
        njdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
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
        return simpleJdbcTemplate.queryForObject(QUERY_USER_BY_ID, userMapper, id);
    }

    /**
     * 查询对象列表.
     */
    public List<User> queryObjectList() {
        return simpleJdbcTemplate.query(QUERY_USER, userMapper);
    }

    /**
     * 查询单个结果Map.
     */
    public Map<String, Object> queryMap(Long id) {
        return simpleJdbcTemplate.queryForMap(QUERY_USER_BY_ID, id);
    }

    /**
     * 查询结果Map列表.
     */
    public List<Map<String, Object>> queryMapList() {
        return simpleJdbcTemplate.queryForList(QUERY_USER);
    }

    /**
     * 使用Map形式的命名参数.
     */
    public User queryByNamedParameter(String loginName) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("login_name", loginName);

        return simpleJdbcTemplate.queryForObject(QUERY_USER_BY_LOGINNAME, userMapper, map);
    }

    /**
     * 使用Map形式的命名参数.
     */
    public List<User> queryByNamedParameterWithInClause(Long... ids) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("ids", Arrays.asList(ids));

        return simpleJdbcTemplate.query(QUERY_USER_BY_IDS, userMapper, map);
    }

    /**
     * 使用Bean形式的命名参数, Bean的属性名称应与命名参数一致.
     */
    public void createObject(User user) {
        //使用BeanPropertySqlParameterSource将User的属性映射为命名参数.
        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(user);
        simpleJdbcTemplate.update(INSERT_USER, source);
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

        simpleJdbcTemplate.batchUpdate(INSERT_USER, sourceArray);
    }

    /**
     * 使用freemarker创建动态SQL.
     */
    public List<User> searchUserByFreemarkerSqlTemplate(Map<String, ?> conditions) {
        String sql = VelocityUtils.render(searchUserSql, conditions);
        logger.info(sql);
        return simpleJdbcTemplate.query(sql, userMapper, conditions);
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
                simpleJdbcTemplate.update(INSERT_USER, source);
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
                    simpleJdbcTemplate.update(INSERT_USER, source);
                    return true;
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    status.setRollbackOnly();
                    return false;
                }
            }
        });
    }

    public SimpleJdbcCall createSimpleJdbcCall() {
            return new SimpleJdbcCall(this.jdbcTemplate);
    }

    //
    public List<User> queryBySp(String p) {
        SimpleJdbcCall jdbcCall = createSimpleJdbcCall();
        jdbcCall.getJdbcTemplate().setResultsMapCaseInsensitive(true);
        jdbcCall.withProcedureName("sp_get_all_user").returningResultSet("P_CURSOR", BeanPropertyRowMapper.newInstance(User.class));
         SqlParameterSource in = new MapSqlParameterSource().addValue("cdate", p);
         Map out = jdbcCall.execute(in);

         // oracle 下只能用这个名称需与后台定义的cursor名一致
         return (List) out.get("P_CURSOR");
    }

    /**
     * 获取服务器端的指定格式的当前时间字符串,oracle数据库的时间格式为"yyyy.MM.dd HH24:mi:ss"
     */
    public String getNowString(String format) {
        String sdate = UtilDateTime.nowDateString("yyyy-MM-dd HH:mm:ss");
        DataSource dataSource = jdbcTemplate.getDataSource();
        Connection con = null;
        try {
            con = DataSourceUtils.getConnection(jdbcTemplate.getDataSource());
            String sql = "";
            //2011.05.02
            //连接泄露
            //(DBUtils.isOracle(jdbcTemplate2.getDataSource().getConnection()))
            //if (DBUtils.isOracle(con)) {
            if (DBUtils.isOracle(con)) {
                sql = "select to_char(sysdate,'yyyy-MM-dd HH24:mi:ss') as sys_date from dual";
            } else if (DBUtils.isMSSqlServer(con)) {
                sql = "Select CONVERT(varchar(100), GETDATE(), 120)";
            } else {
                sql = ""; // 其他数据库，则采用应用服务器系统时间
            }

            if (StringUtils.isNotEmpty(sql)) {
                Object result = this.jdbcTemplate.queryForObject(sql, null, String.class);
                if (result != null) {
                    sdate = result.toString();
                }
            }

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = formatter.parse(sdate);
            sdate = new SimpleDateFormat(format).format(date);

        } catch (Exception se) {
            logger.error("getNowString:", se);
        } finally{
            //安全释放
            DataSourceUtils.releaseConnection(con, dataSource);
        }

        return sdate;
    }

    /**
     * 获取服务器端的当前时间字符串
     */
    public String getNowString() {
        return getNowString("yyyy.MM.dd HH:mm:ss");
    }

    //spring jdbc
    public void createUser(User u){
        String sql = "INSERT INTO t_users(loginName, userName) values(?,?)";

        Object[] paramValues = {u.getLoginName(),u.getUserName()};
        this.jdbcTemplate.update(sql, paramValues);
    }

    public void delUser(User u){
        String sql = "delete from  t_users where loginName=?";

        Object[] paramValues = {u.getLoginName()};
        this.jdbcTemplate.update(sql, paramValues);
    }

    public List<User> finaAll(){
        String sql = "SELECT * FROM T_users";
        return this.jdbcTemplate.query(sql, new UserMapper());
    }

    public User findByLoginName(String loginName){
        String sql = "SELECT * FROM T_UESRS WHERE loginName = ?";
        Object[] paramValues = {loginName};
        return (User)this.jdbcTemplate.queryForObject(sql, paramValues, new UserMapper());
    }

    // NamedParameterJdbcTemplate
    public void create(User acc) {
        String sql = "INSERT INTO account(loginname,password,email,"
                + "cellphone,registed_time) "
                + "VALUES(:loginname,:password,:email,:cellphone, NOW())";
        //使用一个Bean对象的属性值作为命名参数的值
        SqlParameterSource namedParameters =
                new BeanPropertySqlParameterSource(acc);

        this.njdbcTemplate.update(sql,namedParameters);
    }


    public void delete(User acc) {
        String sql = "DELETE FROM account WHERE id=:id";
        //使用指定的值来代替命名参数
        SqlParameterSource namedParameters =
                new MapSqlParameterSource("id", acc.getId());

        this.njdbcTemplate.update(sql, namedParameters);
    }

    public void update(User acc) {
        String sql = "UPDATE account SET loginname=:loginname,"
                + "password=:password,email=:email,"
                + "cellphone=:cellphone WHERE id=:id";
        //使用Map对象中的键/值对来代替多个命名参数的实际值
        Map<String, Object> namedParameters = new HashMap<String, Object>();
        namedParameters.put("loginname", acc.getLoginName());
        namedParameters.put("password", acc.getPassword());
        namedParameters.put("email", acc.getEmail());
        namedParameters.put("id", acc.getId());

        this.njdbcTemplate.update(sql,namedParameters);
    }

    @SuppressWarnings("unchecked")
    public List<User> findAll() {
        String sql = "SELECT * FROM account";
        //通过getJdbcOperations()来访问只有在JdbcTemplate中拥有的功能
        return this.njdbcTemplate
                   .getJdbcOperations()
                   .query(sql, new UserMapper());
    }

    public User findById(Long id) {
        String sql = "SELECT * FROM account WHERE id=?";
        //使用指定的值来代替命名参数
        SqlParameterSource namedParameters =
                new MapSqlParameterSource("id", id);
        return (User)njdbcTemplate
                    .query(sql, namedParameters, new UserMapper());
    }

}
