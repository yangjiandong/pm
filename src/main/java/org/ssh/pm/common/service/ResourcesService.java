package org.ssh.pm.common.service;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
     * 获取当前用户授权的模块
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

            if (user.getLoginName().equalsIgnoreCase("Admin")) {
                hql = "from Resource where resourceType = :type and parentId = 0 order by orderNo";
                values.put("type", 1);
            } else {
                hql = "select a from Resource a,RoleResource b,UserRoles c where a.parentId =0 and c.userId = :userId and c.roleId = b.roleId and b.resourceId = a.oid and a.resourceType.typeName = :typeName order by a.orderNo";
                values.put("type", 1);
                values.put("userId", userId);
            }

            result = resourceDao.find(hql, values);
        } catch (Exception e) {

            logger.error("获取用户授权的模块：", e);
            throw new ServiceException(e.getMessage());
        }

        return result;

    }


    /**
     * 指定模块下,获取当前用户授权的菜单
     */
    @Transactional(readOnly = true)
    public List<Resource> loadGrantedMenus(Long parentId, Long moduleId, User u) throws ServiceException {
        List<Resource> result = new ArrayList<Resource>();
        String hql="";
        if (parentId.longValue() == 0){
            parentId=moduleId;
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

            if (u.getLoginName().equals("Admin")){
                hql = "from Resource where parentId = :parentId order by orderNo";
                values.put("parentId", parentId);
            }else{
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

            if (user.getLoginName().equals("admin")) {
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
        return resourceDao.find("from Resources where parentId = 0 and resourceType.typeName = ? order by orderNo", 1);
    }

    /**
     * 获取资源
     */
    @Transactional(readOnly = true)
    public List<Resource> loadResources(Long parentId) throws ServiceException {
        return resourceDao.find("from Resources where parentId = ? order by orderNo", parentId);

    }

    /**
     * 获取类型为子系统的系统资源
     */
    @Transactional(readOnly = true)
    public List<Resource> loadSubSystems(Long parentId) throws ServiceException {
        return resourceDao.find("from Resources where parentId = ? order by orderNo", parentId);

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
                    if (resourceDao.findBy("parentId", entity.getResourceId()).size() > 0)
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
            logger.error("保存资源信息失败：", e);
            throw new ServiceException();
        }

        return result;
    }

    public void initData() throws ServiceException {
        if ((Long) this.resourceDao.findUnique("select count(h) from " + Resource.class.getName() + " h") != 0)
            return;

        logger.debug("开始装载资源初始数据");

        File resourcetxt = new File(this.getClass().getResource("/data/resource.txt").getFile());
        try {
            FileInputStream fis = new FileInputStream(resourcetxt);
            String thisLine;

            DataInputStream myInput = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(myInput, "UTF-8"));

            Resource re;
            int line = 1;
            while ((thisLine = br.readLine()) != null) {
                //第一行是标题
                if (line == 1) {
                    line++;
                    continue;
                }
                String star[] = thisLine.split(",");
                if (star[0].trim().equals(""))
                    continue;

                re = new Resource();
                re.setResourceId(Long.valueOf(star[0]));
                re.setText(star[1]);
                re.setUrl(star[2]);
                re.setLeaf(star[3].equals("1") ? true : false);
                re.setParentId(Long.valueOf(star[4]));
                re.setResourceType(star[5]);
                re.setOrderNo(Long.valueOf(star[6]));
                re.setState(star[7]);
                re.setIconCls(star[8]);

                this.resourceDao.save(re);
            }
        } catch (Exception e) {
            logger.error("装载资源数据出错:", e);
            throw new ServiceException("导入资源时，服务器发生异常");
        } finally {
            //br.close();
        }
    }

    /**
     * 指定模块下,获取当前用户授权的菜单,注意,此处强制为第二级,?多余
     */
    @Transactional(readOnly = true)
    public List<Resource> loadGrantedSubSystems(Long moduleId, User u) throws ServiceException {
        List<Resource> result = new ArrayList<Resource>();
        String hql = "";
        try {
            Map<String, Object> values = new HashMap<String, Object>();
            if (u.getLoginName().equals("Admin")) {
                hql = "from Resource where resourceType = :type and parentId = :parentId order by orderNo";
                values.put("parentId", moduleId);
                values.put("type", "2");
            } else {
                hql = "from Resource where resourceType = :type and parentId = :parentId order by orderNo";
                values.put("parentId", moduleId);
                values.put("type", "2");
            }
            //            if (user.getLoginName().equals("admin")) {
            //                hql = "from Resource where resourceType = :type and parentId = 0 order by orderNo";
            //                values.put("type", 1);
            //            } else {
            //                hql = "select a from Resource a,RoleResource b,UserRoles c where a.parentId =0 and c.userId = :userId and c.roleId = b.roleId and b.resourceId = a.oid and a.resourceType.typeName = :typeName order by a.orderNo";
            //                values.put("type", 1);
            //                values.put("userId", userId);
            //            }

            result = resourceDao.find(hql, values);
        } catch (Exception e) {
            logger.error("指定模块下,获取当前用户授权的菜单,出错：" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return result;
    }

    /**
     * 获取全部资源
     */
    @Transactional(readOnly = true)
    public List<Resource> loadAllResources() throws ServiceException {
        return resourceDao.find("from Resource order by orderNo");

    }
}
