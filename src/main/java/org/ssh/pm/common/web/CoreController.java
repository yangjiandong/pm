package org.ssh.pm.common.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springside.modules.orm.grid.GridColumnMeta;
import org.springside.modules.orm.grid.GridMetaUtil;
import org.springside.modules.utils.JsonViewUtil;

@Controller
public class CoreController {
    private static Logger logger = LoggerFactory.getLogger(CoreController.class);

    /**
     * 获取实体类Grid元数据
     */
    @RequestMapping("/get_grid_meta")
    public void getGridMeta(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String flag = "0";

        try {
            String entityName = request.getParameter("entityName");

            if (StringUtils.isBlank(entityName)) {
                throw new Exception("实体类名称不能为空");
            }

            String str = request.getParameter("packageName");
            String c = "org.ssh." + str + ".entity." + entityName;

            Object a = Class.forName(c).newInstance();

            List<GridColumnMeta> meta = GridMetaUtil.getGridMeta(c);

            map.put("success", true);
            map.put("data", meta);

        } catch (Exception e) {
            logger.error("getGridMeta", e);
            map.put("success", false);
            map.put("data", null);
        }

        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }
}
