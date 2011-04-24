package org.ssh.pm.common.web;

import java.util.HashMap;
import java.util.Map;

import org.springside.modules.utils.spring.SpringContextHolder;
import org.ssh.pm.common.service.HzService;

public class PageUtils {

    public static Map<String, String> getApplicationInfos(){
        Map<String, String>alls = new HashMap<String, String>();

        HzService manager = (HzService) SpringContextHolder.getBean(
        "hzService");

        alls.put("version", "1.2366");
        alls.put("application_name", "成本核算111");
        alls.put("run_mode", "DEV");
        alls.put("copyright", "常州鑫亿" + manager.getMemo("常州鑫亿").get("py"));
        alls.put("year", "2005");

        return alls;
    }
}
