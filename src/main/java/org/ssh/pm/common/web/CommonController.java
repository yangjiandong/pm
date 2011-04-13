package org.ssh.pm.common.web;

import java.util.Map;

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
import org.ssh.pm.common.service.AccountManager;
import org.ssh.pm.common.service.HzService;

//公开访问
@Controller
@RequestMapping("/common")
public class CommonController {
    private static Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private HzService hzService;

    @Autowired
    private AccountManager accountManager;

    @RequestMapping("/init")
    public void initData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("开始初始化系统基础数据...");
        long start = System.currentTimeMillis();

        this.accountManager.initData();
        this.hzService.initData();

        logger.info(" 执行共计:" + (System.currentTimeMillis() - start) + " ms");

        JsonViewUtil.buildJSONDataResponse(response, " 初始数据成功", (long) 1);

    }
}
