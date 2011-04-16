package org.ssh.pm.common.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.springside.modules.utils.ServiceException;
import org.ssh.pm.common.dao.ResourceDao;
import org.ssh.pm.common.dao.UserDao;
import org.ssh.pm.common.entity.Resource;
import org.ssh.pm.common.entity.User;
import org.ssh.pm.orm.hibernate.EntityService;

@Service("resourcesService")
@Transactional
public class ResourcesService extends EntityService<Resource, Long> {

    @Autowired
    private ResourceDao resourceDao;

    @Autowired
    private UserDao userDao;

    @Override
    protected ResourceDao getEntityDao() {
        return this.resourceDao;
    }

    /**
     * 获取当前用户授权的指定模块子系统资源
     */
    @Transactional(readOnly = true)
    public List<Resource> loadGrantedSubSystems(Long userId) throws ServiceException {
        List<Resource> result = new ArrayList<Resource>();

        try {
            User user = this.userDao.findUnique("id", userId);
            if (user == null) {
                throw new Exception("当前用户不存在，请重新登录");
            }

            String hql = "";
            Map<String, Object> values = new HashMap<String, Object>();

            if (user.getLoginName().equals("Admin")) {
                hql = "from Resource where resourceType = :type and parentId is null order by orderNo";
                values.put("type", 2);
            } else {
                hql = "select a from Resource a,RoleResource b,UserRoles c where a.parentId is null and c.userId = :userId and c.roleId = b.roleId and b.resourceId = a.oid and a.resourceType.typeName = :typeName order by a.orderNo";
                values.put("type", 2);
                values.put("userId", userId);
            }

            result = resourceDao.find(hql, values);
        } catch (Exception e) {

            logger.error("获取用户授权的子系统失败：" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }

        return result;

    }

    /**
     * 获取当前用户授权的菜单系统资源
     */
    @Transactional(readOnly = true)
    public List<Resource> loadGrantedMenus(Long parentId, Long userId) throws ServiceException {

        List<Resource> result = new ArrayList<Resource>();
        String hql = "";

        try {

            User user = this.userDao.findUnique("id", userId);
            if (user == null) {
                throw new Exception("当前用户不存在，请重新登录");
            }

            Map<String, Object> values = new HashMap<String, Object>();

            if (user.getLoginName().equals("Admin")) {
                hql = "from Resource where parentId = :parentId order by orderNo";
                values.put("parentId", parentId);

            } else {

                hql = "select a from Resources a,RoleResource b,UserRoles c where c.userId = :userId and c.roleId = b.roleId and b.resourceId = a.oid and a.parentId = :parentId order by a.orderNo";
                values.put("parentId", parentId);
                values.put("userId", userId);
            }

            result = resourceDao.find(hql, values);

        } catch (Exception e) {
            logger.error("获取授权菜单失败：" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }

        return result;
    }

    /**
     * 获取全部模块
     */
    @Transactional(readOnly = true)
    public List<Resource> loadModules() throws ServiceException {
        return resourceDao
                .find(
                        "from Resources where parentId = 0 and resourceType.typeName = ? order by orderNo",
                        1);
    }


    /**
     * 获取资源
     */
    @Transactional(readOnly = true)
    public List<Resource> loadResources(Long parentId) throws ServiceException {
        return resourceDao
                .find(
                        "from Resources where parentId = ? order by orderNo",
                        parentId);

    }

    /**
     * 获取类型为子系统的系统资源
     */
    @Transactional(readOnly = true)
    public List<Resource> loadSubSystems(Long parentId) throws ServiceException {
        return resourceDao
                .find(
                        "from Resources where parentId = ? order by orderNo",
                        parentId);

    }

    /**
     * 获取指定oid的子资源信息
     */
    @Transactional(readOnly = true)
    public List<Resource> getChildrenResource(Long parentId) throws ServiceException {
        return resourceDao.find("from Resource where parentId = ? order by orderNo", parentId);
    }

    /**
     * 保存系统资源
     */
    public Resource save(String jsonEntity) throws ServiceException {
        Resource result = null;

        try {
            if (StringUtils.isNotEmpty(jsonEntity)) {
                JSONObject json = JSONObject.fromObject(jsonEntity);
                Resource entity = (Resource) JSONObject.toBean(json, Resource.class);

                if (entity.isTransient()) {
                    entity.setLeaf(true);
                } else {
                    if (resourceDao.findBy("parentId", entity.getId()).size() > 0)
                        entity.setLeaf(false);
                    else
                        entity.setLeaf(true);
                }

                Long parentId = entity.getParentId();
                if (parentId != null) {
                    Resource parentEntity = resourceDao.findUnique("id", parentId);
                    parentEntity.setLeaf(false);
                    resourceDao.save(parentEntity);
                }

                //result = resourceDao.saveNew(entity);
            } else {
                throw new Exception();
            }

        } catch (Exception e) {
            result = null;
            logger.error("保存资源信息失败：" + e.getMessage());
            throw new ServiceException();
        }

        return result;
    }

    public void initData() throws ServiceException {
        logger.debug("开始装载资源初始数据");

//        File resourcetxt = new File(this.getClass().getResource("data/resource.txt").getFile());
//
//        DataFile read = DataFile.createReader("UTF-8");
//        read.setDataFormat(new SimpleDelimiterFormat(",", null));
//        // first line is column header
//        read.containsHeader(true);
//
//        try {
//            read.open(resourcetxt);
//
//            String s0;
//            Resources u;
//            String id, parentId;
//
//            for (DataRow row = read.next(); row != null; row = read.next()) {
//                s0 = row.getString("text").trim();
//                parentId = row.getString(4).trim();// "parentId"
//
//                if (s0.equals(""))
//                    continue;
//
//                if (parentId.equals("")) {
//                    // TODO parentId设为0
//                    u = this.resourcesDao.findUnique("from " + Resources.class.getName()
//                            + " where text=? and (parentId='' or parentId is null) ", s0);
//
//                } else {
//                    u = this.resourcesDao.findUnique("from " + Resources.class.getName()
//                            + " where text=? and parentId =? ", s0, Long.valueOf(parentId));
//                }
//
//                if (u == null) {
//                    u = new Resources();
//                    u.setText(s0);
//                    id = row.getString(0).trim();// "resourceId"
//                    parentId = row.getString(4).trim();// "parentId"
//
//                    if (!id.equals("")) {
//                        u.setOid(new Long(id));
//                    }
//                    u.setUrl(row.getString("url"));
//                    u.setOrderNo(new Long(row.getString("orderNo")));
//                    u.setLeaf(!row.getString("leaf").equals("0"));
//
//                    ResourceType type = resourceTypeDao.get(new Long(row
//                            .getString("resourceTypeId")));
//                    u.setResourceType(type);
//
//                    if (!parentId.equals("")) {
//                        u.setParentId(new Long(parentId));
//                    }
//
//                    // u.setNote(row.getString("note"));
//                    resourcesDao.save(u);
//                } else {
//                    u.setUrl(row.getString("url"));
//                    u.setOrderNo(new Long(row.getString("orderNo")));
//                    resourcesDao.save(u);
//                }
//            }
//        } catch (Exception e) {
//            logger.error("初始资源数据出错:" + e.getMessage());
//            throw new ServiceException(e.getMessage());
//        } finally {
//            read.close();
//        }
    }



}
