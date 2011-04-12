package org.ssh.pm.common.web;

import java.util.List;
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
import org.ssh.pm.common.entity.Hz;
import org.ssh.pm.common.service.HzService;

@Controller
@RequestMapping("/book")
public class BookController {
    private static Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private HzService hzService;

    @RequestMapping(value = "/getBooks")
    public void showBooks(HttpServletRequest request, HttpServletResponse response) throws Exception {

        JsonViewUtil.buildJSONDataResponse(response, "test", (long) 1);
    }

    @RequestMapping(value = "/getHz")
    public void showHz(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("test memcahced...");
        long start = System.currentTimeMillis();

        Map<String, String> allBooks = this.hzService.getMemo("测试");
        JSONArray jsonArray = JSONArray.fromObject(allBooks);

        logger.info(" cache 执行共计:" + (System.currentTimeMillis() - start) + " ms");

        JsonViewUtil.buildJSONDataResponse(response, jsonArray.toString(), (long) 1);
    }

    @RequestMapping(value = "/getAllHz")
    public ModelAndView showAllHz(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("test method memcahced...");
        long start = System.currentTimeMillis();

        List<Hz> books = hzService.getHzsOnMethodCache();

        logger.info(" method cache 执行共计:" + (System.currentTimeMillis() - start) + " ms");

        return JsonViewUtil.getModelMap(books);
    }
}
