package org.ssh.pm.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springside.modules.utils.JsonViewUtil;

@Controller
@RequestMapping("/book")
public class BookController {
    private static Logger logger = LoggerFactory.getLogger(BookController.class);

    @RequestMapping(value = "/getBooks")
    public void showBooks(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        logger.info("test...");

        long start = System.currentTimeMillis();

        logger.info(" method cache 执行共计:"
            + (System.currentTimeMillis() - start) + " ms");

        JsonViewUtil.buildJSONDataResponse(response, "test", (long)1);
    }
}
