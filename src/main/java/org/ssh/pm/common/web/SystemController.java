package org.ssh.pm.common.web;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.PropertyFilter;
import org.springside.modules.utils.JsonViewUtil;
import org.springside.modules.web.ServletUtils;
import org.ssh.pm.common.entity.Resource;
import org.ssh.pm.common.entity.Role;
import org.ssh.pm.common.entity.RoleResource;
import org.ssh.pm.common.entity.User;
import org.ssh.pm.common.service.AccountManager;
import org.ssh.pm.common.service.ResourcesService;
import org.ssh.pm.common.service.RoleResourceService;
import org.ssh.pm.common.service.RoleService;
import org.ssh.pm.common.service.UserService;

//系统管理模块访问
@Controller
@RequestMapping("/system")
public class SystemController {

    @Autowired
    private AccountManager accountManager;
    @Autowired
    private ResourcesService resourcesService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleResourceService roleResourceService;
    @Autowired
    private UserService userService;

    /**
     * 保存修改后的密码
     */
    @RequestMapping("/savepassword")
    public void savePassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userName = request.getParameter("userName");
        String newPassword = request.getParameter("newPassword");
        Map<String, Object> map = accountManager.savePassword(userName, newPassword);
        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }

    /**
     * 加载子系统
     */
    @RequestMapping("/loadSubSystem")
    public void loadSubSystem(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Resource> subSystem = new ArrayList<Resource>();
        Map<String, Object> map = new HashMap<String, Object>();

        UserSession u = (UserSession) request.getSession().getAttribute("userSession");
        if (u == null)
            return;

        try {
            subSystem = resourcesService.loadGrantedSubSystems(1L, u.getAccount());
            map.put("success", true);
            map.put("subSystems", subSystem);
        } catch (Exception e) {
            map.put("success", false);
            map.put("subSystems", subSystem);
        }
        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }

    /**
     * 加载子系统的菜单项
     */
    @RequestMapping("/loadMenu")
    public void loadMenu(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Resource> menus = new ArrayList<Resource>();
        String resourceId = request.getParameter("resourceId");

        UserSession u = (UserSession) request.getSession().getAttribute("userSession");
        if (u == null)
            return;

        menus = resourcesService.loadGrantedMenus(Long.valueOf(resourceId), u.getModuleId(), u.getAccount());
        JsonViewUtil.buildJSONResponse(response, menus);
    }

    /**
     * 获取全部的系统资源信息
     */
    @RequestMapping("/get_all_sys_resources")
    public void getAllResources(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            List<Resource> data = resourcesService.loadAllResources();
            map.put("success", true);
            map.put("data", data);
        } catch (Exception e) {
            map.put("success", false);
            map.put("data", null);
        }
        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }

    /**
     * 获取指定角色授权的系统资源信息
     */
    @RequestMapping("/get_all_role_resources")
    public void getAllRoleResources(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            List<RoleResource> data = new ArrayList<RoleResource>();
            String roleId = request.getParameter("roleId");
            if (StringUtils.isNotBlank(roleId)) {
                data = roleResourceService.getAllRoleResources(Long.valueOf(roleId));
            }
            map.put("success", true);
            map.put("data", data);
        } catch (Exception e) {
            map.put("success", false);
            map.put("data", null);
        }
        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }

    /**
     * 保存指定角色的资源权限
     */
    @RequestMapping("/save_role_resources")
    public void saveRoleResources(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String roleId = request.getParameter("roleId");
            String menuIds = request.getParameter("menuIds");
            if (StringUtils.isNotBlank(roleId) && StringUtils.isNotBlank(menuIds)) {
                roleResourceService.saveRoleResource(Long.valueOf(roleId), menuIds);
            }
            map.put("success", true);
            map.put("message", "保存成功");
        } catch (Exception e) {
            map.put("success", false);
            map.put("message", "保存失败");
        }
        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }

    /**
     * 获取全部角色
     */
    @RequestMapping("/get_Sys_Roles")
    public void getAllRoles(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Page<Role> page = new Page<Role>(request);
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        String name = request.getParameter("name");
        if (StringUtils.isNotEmpty(name)) {
            PropertyFilter filter = new PropertyFilter("LIKES_name", name);
            filters.add(filter);
        }
        Page<Role> data = roleService.getAllRoles(page, filters);
        JsonViewUtil.buildJSONDataResponse(response, data.getResult(), (long) data.getTotalCount());
    }

    /**
     * 保存角色
     */
    @RequestMapping("/save_Sys_Role")
    public void saveRole(HttpServletRequest request, HttpServletResponse response, Role entity) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            roleService.saveRole(entity);
            map.put("success", true);
            map.put("message", "保存成功");
        } catch (Exception e) {
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }

    /**
     * 删除角色
     */
    @RequestMapping("/delete_Sys_Role")
    public void deleteRole(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String id = request.getParameter("id");
            roleService.delete(Long.valueOf(id));
            map.put("success", true);
            map.put("message", "");
        } catch (Exception e) {
            map.put("success", false);
            map.put("message", "删除时服务器端发生异常，删除失败！");
        }
        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }

    /**
     * 获取全部用户
     */
    @RequestMapping("/get_Sys_Users")
    public void getAllUsers(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Page<User> page = new Page<User>(request);
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        String name = request.getParameter("name");
        if (StringUtils.isNotEmpty(name)) {
            PropertyFilter filter = new PropertyFilter("LIKES_name", name);
            filters.add(filter);
        }
        Page<User> data = userService.getAllUsers(page, filters);
//		Bean bean = new Bean(true, "ok", data);
//		JsonUtils.write(bean, response.getWriter(), new String[] { "hibernateLazyInitializer", "handler", "roleList",
//				"partDBList" }, "yyyy.MM.dd");
        buildJSONDataResponse(response, data.getResult(), (long) data.getTotalCount());
    }

    private static void buildJSONDataResponse(HttpServletResponse response, List<? extends Object> data, Long total)
            throws Exception {
        JsonConfig cfg = new JsonConfig();
        cfg.setExcludes(new String[] { "handler", "hibernateLazyInitializer", "roleList", "partDBList", "RoleNames",
                "DBNames" });
        JSONArray jsonArray = JSONArray.fromObject(data, cfg);
        StringBuffer sb = new StringBuffer();
        sb.append("{\"totalCount\":" + total + ",\"rows\":");
        sb.append(jsonArray.toString());
        sb.append("}");
        String encoding = "UTF-8";
        boolean noCache = true;
        //response.setContentType("text/json; charset=UTF-8");
        response.setContentType(ServletUtils.JSON_TYPE + ";charset=" + encoding);
        if (noCache) {
            ServletUtils.setNoCacheHeader(response);
        }
        PrintWriter out = response.getWriter();
        out.write(sb.toString());
    }

    /**
     * 保存用户
     */
    @RequestMapping("/save_Sys_User")
    public void saveUser(HttpServletRequest request, HttpServletResponse response, User entity) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            userService.saveUser(entity);
            map.put("success", true);
            map.put("message", "保存成功");
        } catch (Exception e) {
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }

    /**
     * 删除用户
     */
    @RequestMapping("/delete_Sys_User")
    public void deleteUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String id = request.getParameter("id");
            userService.delete(Long.valueOf(id));
            map.put("success", true);
            map.put("message", "");
        } catch (Exception e) {
            map.put("success", false);
            map.put("message", "删除时服务器端发生异常，删除失败！");
        }
        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }
}
