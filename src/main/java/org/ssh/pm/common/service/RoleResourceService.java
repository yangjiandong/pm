package org.ssh.pm.common.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.utils.ServiceException;
import org.ssh.pm.common.dao.ResourceDao;
import org.ssh.pm.common.dao.RoleResourceDao;
import org.ssh.pm.common.entity.Resource;
import org.ssh.pm.common.entity.RoleResource;
import org.ssh.pm.common.entity.User;

@Service
@Transactional
public class RoleResourceService {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ResourceDao resourceDao;

    @Autowired
    private RoleResourceDao roleResourceDao;

    /**
     * 获取指定角色授权的系统资源信息
     */
    @Transactional(readOnly = true)
    public List<RoleResource> getAllRoleResources(Long roleId) throws ServiceException {
        List<RoleResource> result = new ArrayList<RoleResource>();
        result = roleResourceDao.find("from RoleResource where roleId = ?", roleId);
        return result;
    }

    /**
     * 保存指定角色的资源权限
     */
    public void saveRoleResource(Long roleId, String resourceIds) throws ServiceException {
        Session session = this.roleResourceDao.getSession();
        try {

            session.beginTransaction();
            // 当前的授权
            String[] menuIds = StringUtils.split(resourceIds, ",");
            HashSet<Long> granted = new HashSet<Long>();
            for (String menuId : menuIds) {
                granted.add(Long.valueOf(menuId));
            }

            // 获取本次修改之前的授权记录
            List<RoleResource> list = roleResourceDao.findBy("roleId", roleId);
            for (RoleResource roleResource : list) {
                Long roleResourceId = roleResource.getResourceId();
                if (granted.contains(roleResourceId)) {
                    granted.remove(roleResourceId);
                } else {
                    roleResourceDao.delete(roleResource); // 删除不在本次授权范围之内的授权记录
                }
            }

            // 保存本次新增的授权信息
            RoleResource roleResource = null;
            for (Long resourceId : granted) {
                roleResource = new RoleResource();
                roleResource.setRoleId(roleId);
                roleResource.setResourceId(resourceId);
                roleResourceDao.save(roleResource);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("保存指定角色的资源权限失败:", e);
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 指定模块下,获取当前用户授权的菜单
     */
    @Transactional(readOnly = true)
    public List<Resource> loadGrantedMenus(Long parentId, Long moduleId, User u) throws ServiceException {
        List<Resource> result = new ArrayList<Resource>();
        String hql = "";
        if (parentId.longValue() == 0) {
            parentId = moduleId;
        }
        try {
            Map<String, Object> values = new HashMap<String, Object>();
            //            if (user.getLoginName().equals("admin")) {
            //                hql = "from Resource where resourceType = :type and parentId = 0 order by orderNo";
            //                values.put("type", 1);
            //            } else {
            //                hql = "select a from Resource a,RoleResource b,UserRoles c where a.parentId =0 and c.userId = :userId and c.roleId = b.roleId and b.resourceId = a.oid and a.resourceType.typeName = :typeName order by a.orderNo";
            //                values.put("type", 1);
            //                values.put("userId", userId);
            //            }

            if (u.getLoginName().equals("Admin")) {
                hql = "from Resource where parentId = :parentId order by orderNo";
                values.put("parentId", parentId);
            } else {
                hql = "from Resource where resourceType = :type and parentId := :parentId order by orderNo";
                values.put("parentId", parentId);
            }

            result = resourceDao.find(hql, values);
        } catch (Exception e) {
            logger.error("获取用户授权的模块：" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return result;
    }

}
