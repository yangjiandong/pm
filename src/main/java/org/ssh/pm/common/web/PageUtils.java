package org.ssh.pm.common.web;

import java.util.Map;

import org.springside.modules.utils.spring.SpringContextHolder;
import org.ssh.pm.common.service.CommonService;

public class PageUtils {

    public static Map<String, String> getApplicationInfos() {
        CommonService manager = (CommonService) SpringContextHolder.getBean("commonService");
        return manager.getApplicationInfos();
    }
}
