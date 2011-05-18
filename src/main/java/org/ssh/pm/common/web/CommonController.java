package org.ssh.pm.common.web;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import net.sf.json.JSONArray;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springside.modules.utils.JsonViewUtil;
import org.springside.modules.utils.spring.SpringContextHolder;
import org.ssh.pm.common.entity.User;
import org.ssh.pm.common.service.AccountManager;
import org.ssh.pm.common.service.CategoryService;
import org.ssh.pm.common.service.HzService;
import org.ssh.pm.common.service.ResourcesService;
import org.ssh.pm.log.LogAction;

import com.mchange.v2.c3p0.ComboPooledDataSource;

//import org.ssh.pm.orm.hibernate.CustomerContextHolder;

//公开访问
@Controller
@RequestMapping("/common")
public class CommonController {
    private static Logger logger = LoggerFactory.getLogger(CommonController.class);
    @Autowired
    private HzService hzService;
    @Autowired
    private AccountManager accountManager;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ResourcesService resourcesService;

    @RequestMapping("/init")
    public void initData(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        List<Bean> data = new ArrayList<Bean>();

        Logger dblogger = LoggerFactory.getLogger(LogAction.DB_LOGGER_NAME);

        logger.info("开始初始化系统基础数据...");
        dblogger.info("开始初始化系统基础数据...");

        long start = System.currentTimeMillis();

        this.accountManager.initData();
        data.add(new Bean(true, "用户初始数据成功!", this.hzService.getClass().getName()));

        //指定数据源
        //CustomerContextHolder.setCustomerType("Yxh");
        try {
            this.hzService.initDataByBatch();
            data.add(new Bean(true, "hzk初始数据成功!", ""));
        } catch (Exception se) {
            logger.error("cuco ...", se);
            data.add(new Bean(false, "hzk初始数据失败!" + se.toString(), ""));
        }

        //
        //CustomerContextHolder.setCustomerType("admin");
        try {
            this.resourcesService.initData();
            data.add(new Bean(true, "资源初始数据成功!", this.resourcesService.toString()));
        } catch (Exception se) {
            logger.error("cuco ...",se);
            data.add(new Bean(false, "资源初始数据失败!" + se.toString(), this.resourcesService.toString()));
        }
        try {
            this.categoryService.initData();
            data.add(new Bean(true, "lz初始数据成功!", ""));
        } catch (Exception se) {
            logger.error("cuco ...",se);
            data.add(new Bean(false, "lz初始数据失败!" + se.toString(), ""));
        }

        logger.info(" 执行共计:" + (System.currentTimeMillis() - start) + " ms");
        dblogger.info(" 执行共计:" + (System.currentTimeMillis() - start) + " ms");

        JSONArray jsonArray = JSONArray.fromObject(data);
        StringBuffer sb = new StringBuffer();
        sb.append("{\"totalCount\":" + data.size() + ",\"info\":");
        sb.append(jsonArray.toString());
        sb.append("}");
        response.setContentType("text/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write(sb.toString());
    }

    public static class Bean {
        private boolean success;
        private String msg;
        private String name;

        public Bean(boolean success, String msg, String info) {
            this.success = success;
            this.msg = msg;
            this.name = info;
        }

        public boolean getSuccess() {
            return success;
        }

        public String getMsg() {
            return msg;
        }

        public String getName() {
            return name;
        }
    }

    @RequestMapping(value = "/get_all_user")
    public ModelAndView showAllUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long start = System.currentTimeMillis();

        List<User> u = accountManager.getAllUserBySp();

        logger.info(" exec sp 执行共计:" + (System.currentTimeMillis() - start) + " ms");
        return JsonViewUtil.getModelMap(u);
    }

    /**
     * 获取系统版本等信息
     */
    @RequestMapping(value = "/get_sys_config")
    public void getSystemConfig(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> map = PageUtils.getApplicationInfos();
        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }


    @RequestMapping("/get_datasource_info")
    public @ResponseBody Map<String, Object> getDatabaseInfo(HttpServletRequest request, HttpServletResponse response) {
        DataSource ds = (DataSource) SpringContextHolder.getBean("dataSource");

        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> dsmap = new HashMap<String, Object>();
        map.put("sysDataSource", dsmap);
        try{
            BasicDataSource basicDataSource = (BasicDataSource) ds;
            dsmap.put("最大连接数" , basicDataSource.getMaxActive());
            dsmap.put("当前连接数" , basicDataSource.getNumActive());
            dsmap.put("空闲", basicDataSource.getNumIdle());

        }catch(java.lang.ClassCastException e){
            //logger.error("sysDataSource 不采用 " + BasicDataSource.class.toString(),e);
        }
        try{
            ComboPooledDataSource cpDataSource = (ComboPooledDataSource) ds;
            dsmap.put("最大连接数" , cpDataSource.getNumConnections());
            dsmap.put("当前连接数" , cpDataSource.getNumBusyConnections());
            dsmap.put("空闲", cpDataSource.getNumIdleConnections());

        }catch(java.lang.ClassCastException e){
            //logger.error("sysDataSource 不采用 " + ComboPooledDataSource.class.toString(),e);
        }catch(SQLException se){

        }
//
//        //DynamicDataSource
//        DataSource ds2 = (DataSource) SpringContextHolder.getBean("dataSource");
//        Map<String, Object> dsmap2 = new HashMap<String, Object>();
//        map.put("dataSource", dsmap2);
//        try{
//            DynamicDataSource dynamicDataSource = (DynamicDataSource) ds2;
//
//            DataSource target = (DataSource) dynamicDataSource.returnTargetDataSource();
//
//            BasicDataSource basicDataSource = (BasicDataSource) target;
//            dsmap2.put("最大连接数" , basicDataSource.getMaxActive());
//            dsmap2.put("当前连接数" , basicDataSource.getNumActive());
//            dsmap2.put("空闲", basicDataSource.getNumIdle());
//
//            ComboPooledDataSource cpDataSource = (ComboPooledDataSource) target;
//            dsmap.put("最大连接数" , cpDataSource.getNumConnections());
//            dsmap.put("当前连接数" , cpDataSource.getNumBusyConnections());
//            dsmap.put("空闲", cpDataSource.getNumIdleConnections());
//
//        }catch(java.lang.ClassCastException e){
//            //logger.error("sysDataSource 不采用 " + BasicDataSource.class.toString(),e);
//        }catch(SQLException se){
//
//        }
        return map;

//        try {
//            JsonViewUtil.buildCustomJSONDataResponse(response, map);
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
    }

    @RequestMapping("/login")
    public ModelAndView index(ModelMap map, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        session.removeAttribute("userSession");

        return new ModelAndView("login");
    }

    /**
     * 检查用户登录信息的合法性
     */
    @RequestMapping("/logon")
    public void checkUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = accountManager.checkUserLegality(request);
        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }

    /**
     * 用户注销
     */
    @RequestMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = accountManager.logout(request);
        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }
}
