package org.ssh.pm.common.dao;

import java.util.List;
import java.util.Map;

import javax.servlet.jsp.jstl.sql.Result;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

//http://joknm.iteye.com/blog/777732
@Repository
public class SPBaseDao {
    // 存储过程前缀
    protected static String PROCEDURE_NAME_PREFIX = "st_";

    /**
     * 存储过程对应的表名，为空时，从操作对象类名中获取
     * 存储过程命名方式：前缀_表名_后缀
     * 操作对象如：ProductModel, ProductSellTableModel
     * 对应获取表名为：productTable, ProductSellTable
     */
    protected static String TABLE_NAME = null;

    // 存储过程后缀
    protected static String PROCEDURE_NAME_ADD_SUFFIX = "Add";
    protected static String PROCEDURE_NAME_UPDATE_SUFFIX = "Update";
    protected static String PROCEDURE_NAME_DELETE_SUFFIX = "Delete";
    protected static String PROCEDURE_NAME_QUERY_BY_PRIMARY_KEY_SUFFIX = "QueryByPrimaryKey";

    public SPBaseDao(){}

    private SimpleJdbcTemplate simpleJdbcTemplate ;

    private JdbcTemplate jdbcTemplate ;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource) {
        simpleJdbcTemplate = new SimpleJdbcTemplate(dataSource);
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public SimpleJdbcTemplate getSimpleJdbcTemplate() {
        return simpleJdbcTemplate;
    }

    public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
        this.simpleJdbcTemplate = simpleJdbcTemplate;
    }

    public SimpleJdbcCall createSimpleJdbcCall() {
        return new SimpleJdbcCall(this.jdbcTemplate);
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     *
     * @功能模块: executeProcedure
     * @方法说明: 执行存储过程 将对象t转换成存储过程参数，并执行存储过程，执行存储过程后返回此对象操作后的信息
     * @version: 1.0
     * @param <T> 泛类型
     * @param t 要操作的对象信息
     * @param returnClass 返回的类型，如果为null 返回类型与查询类型一致
     * @param procedureName 存储过程名称
     * @return Map<"ReturningResultSet", Object>
     * @throws
     */
    @SuppressWarnings("unchecked")
    protected <T> Map executeProcedure(Class returnClass, T t, String procedureName){
        SimpleJdbcCall sjc = this.createSimpleJdbcCall();
        sjc.getJdbcTemplate().setResultsMapCaseInsensitive(true);
        sjc.withProcedureName(procedureName).returningResultSet("ReturningResultSet", BeanPropertyRowMapper.newInstance(returnClass==null?t.getClass():returnClass));

        SqlParameterSource param = new BeanPropertySqlParameterSource(t);
        return sjc.execute(param);
    }

    /**
     *
     * @功能模块: querySelf
     * @方法说明: 执行存储过程 将对象t转换成存储过程参数，并执行存储过程，执行存储过程后返回此对象操作后的信息
     * @version: 1.0
     * @param <T> 泛类型
     * @param t 要操作的对象信息
     * @param procedureName 存储过程名称
     * @return T 执行存储过程后返回的对象,与传入对象类型一致
     * @throws
     */
    @SuppressWarnings("unchecked")
    protected <T> T querySelf(T t, String procedureName){
        Map returnResultSet = executeProcedure(null, t, procedureName);
        List list = (List)returnResultSet.get("ReturningResultSet");
        if(list.size()>0){
            return (T)list.get(0);
        }else{
            return null;
        }
    }



    /**
     *
     * @功能模块: add
     * @方法说明: 添加一个对象 将对象t转换成存储过程参数，并执行存储过程，执行存储过程后返回此对象操作后的信息
     * @version: 1.0
     * @param <T> 泛类型
     * @param t 要添加的对象信息
     * @param procedureName 存储过程名称
     * @return T 泛类型（返回添加后的对象）
     * @throws
     */
    protected <T> T add(T t, String procedureName){
        return this.querySelf(t, procedureName);
    }

    /**
     *
     * @功能模块: add
     * @方法说明: 添加一个对象 将对象t转换成存储过程参数，并执行存储过程，执行存储过程后返回此对象操作后的信息
     * @version: 1.0
     * @param <T> 泛类型
     * @param t 要添加的对象信息
     * @return T 泛类型（返回添加后的对象）
     * @throws
     */
    public <T> T add(T t){
        return add(t, getProcedureName(t.getClass(), PROCEDURE_NAME_ADD_SUFFIX));
    }

    /**
     *
     * @功能模块: update
     * @方法说明: 按主键修改一个对象 将对象t转换成存储过程参数，并执行存储过程，执行存储过程后返回此对象操作后的信息
     * @version: 1.0
     * @param <T> 泛类型
     * @param t 要修改的对象信息
     * @param procedureName 存储过程名称
     * @return T 泛类型（返回修改后的对象信息）
     * @throws
     */
    protected <T> T update(T t, String procedureName){
        return this.querySelf(t, procedureName);
    }

    /**
     *
     * @功能模块: update
     * @方法说明: 按主键修改一个对象 将对象t转换成存储过程参数，并执行存储过程，执行存储过程后返回此对象操作后的信息
     * @version: 1.0
     * @param <T> 泛类型
     * @param t 要修改的对象信息
     * @return T 泛类型（返回修改后的对象信息）
     * @throws
     */
    public <T> T update(T t){
        return update(t, getProcedureName(t.getClass(), PROCEDURE_NAME_UPDATE_SUFFIX));
    }

    /**
     *
     * @功能模块: delete
     * @方法说明: 按主键删除一个对象 将对象t转换成存储过程参数，并执行存储过程，执行存储过程后返回执行结果信息
     * @version: 1.0
     * @param <T> 泛型
     * @param t 要删除的对象
     * @param procedureName 存储过程名称
     * @return Result 执行结果信息类
     * @throws
     */
    protected <T> Result delete(T t, String procedureName){
        Map returnResultSet = executeProcedure(Result.class, t, procedureName);
        List list = (List)returnResultSet.get("ReturningResultSet");
        if(list!=null && list.size()>0){
            return (Result)list.get(0);
        }else{
            return null;
        }
    }

    /**
     *
     * @功能模块: delete
     * @方法说明: 按主键删除一个对象 将对象t转换成存储过程参数，并执行存储过程，执行存储过程后返回执行结果信息
     * @version: 1.0
     * @param <T> 泛型
     * @param t 要删除的对象
     * @return Result 执行结果信息类
     * @throws
     */
    public <T> Result delete(T t){
        return delete(t, getProcedureName(t.getClass(), PROCEDURE_NAME_DELETE_SUFFIX));
    }

    /**
     *
     * @功能模块: delete
     * @方法说明: 按主键字符串删除多个对象 将对象t转换成存储过程参数，并执行存储过程，执行存储过程后返回执行结果信息
     * @version: 1.0
     * @param ids 要删除的对象ID，多个ID以逗号分割，如：1,2,3
     * @return Result 执行结果信息类
     * @throws
     */
    protected Result delete(String ids, String procedureName){
        SimpleJdbcCall sjc = this.createSimpleJdbcCall();
        sjc.getJdbcTemplate().setResultsMapCaseInsensitive(true);
        sjc.withProcedureName(procedureName).returningResultSet("ReturningResultSet", BeanPropertyRowMapper.newInstance(Result.class));

        List list = (List)sjc.execute(ids).get("ReturningResultSet");
        if(list!=null && list.size()>0){
            return (Result)list.get(0);
        }else{
            return null;
        }
    }

    /**
     *
     * @功能模块: query
     * @方法说明: 按条件，排序，分页信息查询内容
     * @version: 1.0
     * @param <T> 泛型，查询的对象类型
     * @param t 对象实例
     * @param condition 条件
     * @param order 排序
     * @param pagination 分页信息
     * @param procedureName 存储过程名称
     * @return Map<"Pagination", PaginationModel>
     * 		   Map<"List", List<T>>
     * @throws
     */
//	@SuppressWarnings("unchecked")
//	protected Map query(Class clazz, Map<String, Object> condition, String order, PaginationModel pagination, String procedureName){
//		SimpleJdbcCall sjc = this.createSimpleJdbcCall();
//		sjc.getJdbcTemplate().setResultsMapCaseInsensitive(true);
//		sjc.withProcedureName(procedureName).
//			returningResultSet("Pagination", BeanPropertyRowMapper.newInstance(PaginationModel.class)).
//			returningResultSet("List", BeanPropertyRowMapper.newInstance(clazz));
//
//		Map<String, Object> param = new HashMap<String, Object>();
//		param.putAll(condition==null?new HashMap():condition);
//		param.put("orderby", order==null?"":order);
//		param.put("pageSize", pagination.getPageSize());
//		param.put("currPage", pagination.getCurrPage());
//		Map returnResultSet = sjc.execute(param);
//
//		Map<String, Object> newResult = new HashMap<String, Object>();
//		newResult.put("List", returnResultSet.get("List"));
//
//		List list = (List)returnResultSet.get("Pagination");
//		if(list.size()>0){
//			newResult.put("Pagination", list.get(0));
//		}
//
//		return newResult;
//	}

    /**
     *
     * @功能模块: query
     * @方法说明: 按条件查询对象列表
     * @version: 1.0
     * @param <T> 泛型
     * @param t 要查询的对象
     * @param condition 条件
     * @param procedureName 存储过程名称
     * @return List 对象列表
     * @throws
     */
    @SuppressWarnings("unchecked")
    protected List query(Class clazz, Map<String, Object> condition, String procedureName){
        SimpleJdbcCall sjc = this.createSimpleJdbcCall();
        sjc.getJdbcTemplate().setResultsMapCaseInsensitive(true);
        sjc.withProcedureName(procedureName).
            returningResultSet("List", BeanPropertyRowMapper.newInstance(clazz));
        Map resultSet = sjc.execute(condition);
        return (List)resultSet.get("List");
    }


    /**
     *
     * @功能模块: query
     * @方法说明: 按对象主键进行查询 将对象t转换成存储过程参数，并执行存储过程进行查询，返回查询后的对象信息
     * @version: 1.0
     * @param <T> 泛类型
     * @param t 要查询的对象信息
     * @param procedureName 存储过程名称
     * @return T 执行查询存储过程后返回的对象,与传入对象类型一致
     * @throws
     */
    protected <T> T query(T t, String procedureName){
        return this.querySelf(t, procedureName);
    }

    /**
     *
     * @功能模块: query
     * @方法说明: 按对象主键进行查询 将对象t转换成存储过程参数，并执行存储过程进行查询，返回查询后的对象信息
     * @version: 1.0
     * @param <T> 泛类型
     * @param t 要查询的对象信息
     * @return T 执行查询存储过程后返回的对象,与传入对象类型一致
     * @throws
     */
    public <T> T query(T t){
        return query(t, getProcedureName(t.getClass(), PROCEDURE_NAME_QUERY_BY_PRIMARY_KEY_SUFFIX));
    }

    /**
     *
     * @功能模块: getProcedureName
     * @方法说明: 根据查询的对象类，获取此操作此对象存储过程的名称
     * @version: 1.0
     * @param clazz 查询的对象类
     * @param suffix 存储过程后缀
     * @return String 存储过程名称
     * @throws
     */
    private String getProcedureName(Class clazz, String suffix){
        String tableName = TABLE_NAME==null?getTableName(clazz.getSimpleName()):TABLE_NAME;
        return PROCEDURE_NAME_PREFIX + tableName + (suffix==null?"":("".equals(suffix)?"":"_"+suffix));
    }

    /**
     *
     * @功能模块: getTableName
     * @方法说明: 根据查询的对象类名，获取此对象类对应的表
     * @version: 1.0
     * @param className
     * @return String
     * @throws
     */
    private String getTableName(String className){
        if(className.endsWith("Model")){
            String name = className.substring(0, className.length()-"Model".length());
            return name.endsWith("Table")?name:name+"Table";
        }else{
            return className.endsWith("Table")?className:className+"Table";
        }
    }
}
