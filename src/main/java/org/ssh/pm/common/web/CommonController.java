package org.ssh.pm.common.web;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springside.modules.utils.JsonViewUtil;
import org.ssh.pm.common.entity.User;
import org.ssh.pm.common.service.AccountManager;
import org.ssh.pm.common.service.CategoryService;
import org.ssh.pm.common.service.HzService;
import org.ssh.pm.common.service.ResourcesService;
import org.ssh.pm.log.LogAction;


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

        try {
            this.hzService.initDataByBatch();
            data.add(new Bean(true, "hzk初始数据成功!", ""));
        } catch (Exception se) {
            logger.error("cuco ...");
            data.add(new Bean(false, "hzk初始数据失败!" + se.toString(), ""));
        }

        try {
            this.resourcesService.initData();
            data.add(new Bean(true, "资源初始数据成功!", this.resourcesService.toString()));
        } catch (Exception se) {
            logger.error("cuco ...");
            data.add(new Bean(false, "资源初始数据失败!" + se.toString(), ""));
        }
        try {
            this.categoryService.initData();
            data.add(new Bean(true, "lz初始数据成功!", ""));
        } catch (Exception se) {
            logger.error("cuco ...");
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

    @RequestMapping(value = "/getAllUser")
    public ModelAndView showAllUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long start = System.currentTimeMillis();

        List<User> u = accountManager.getAllUserBySp();

        logger.info(" exec sp 执行共计:" + (System.currentTimeMillis() - start) + " ms");
        return JsonViewUtil.getModelMap(u);
    }
}
