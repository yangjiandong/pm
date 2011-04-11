package org.ssh.pm.orm.hibernate;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.event.PostDeleteEvent;
import org.hibernate.event.PostDeleteEventListener;
import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.event.PostUpdateEvent;
import org.hibernate.event.PostUpdateEventListener;
import org.ssh.pm.orm.hibernate.entity.HistoryEntry;

/**
 * @version 2008-1-25
 * @author jeffrey
 */
public class HistoryListener implements PostInsertEventListener,
        PostUpdateEventListener, PostDeleteEventListener {

    private static final long serialVersionUID = 1L;

    @Override
    public void onPostInsert(PostInsertEvent event) {
        if (event.getEntity() instanceof Historizable) {
            Historizable entity = (Historizable) event.getEntity();
            HistoryEntry entry = new HistoryEntry();
            entry.setOperationType(OperationType.CREATE);
            entry.setTimestamp(new Date());
            entry.setHistorizableEntity(entity);
            recordHistory(event.getSession(), entry);
        }
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        if (event.getEntity() instanceof Historizable) {
            for (int i = 0; i < event.getState().length; i++) {
                // 更新前的值
                Object oldValue = event.getOldState()[i];
                // 更新后的新值
                Object newValue = event.getState()[i];
                // 跳过集合属性
                if (newValue instanceof PersistentCollection) {
                    continue;
                }
                if (oldValue != null && !oldValue.equals(newValue)) {
                    HistoryEntry entry = new HistoryEntry();
                    // 取得属性名称
                    entry.setProperty(event.getPersister().getPropertyNames()[i]);
                    entry.setOperationType(OperationType.UPDATE);
                    // 如果更改的属性是关联对象，则存储其id
                    if (oldValue instanceof AbstractEntity) {
                        entry.setPreviousValue(((AbstractEntity) oldValue).getId().toString());
                    } else {
                        entry.setPreviousValue(oldValue != null ? oldValue.toString() : null);
                    }
                    if (newValue instanceof AbstractEntity) {
                        entry.setNewValue(((AbstractEntity) newValue).getId().toString());
                    } else {
                        entry.setNewValue(newValue != null ? newValue.toString() : null);
                    }
                    entry.setTimestamp(new Date());
                    entry.setHistorizableEntity((Historizable) event.getEntity());
                    recordHistory(event.getSession(), entry);
                }
            }
        }
    }

    @Override
    public void onPostDelete(PostDeleteEvent event) {
        if (event.getEntity() instanceof Historizable) {
            HistoryEntry entry = new HistoryEntry();
            entry.setOperationType(OperationType.DELETE);
            entry.setTimestamp(new Date());
            entry.setHistorizableEntity((Historizable) event.getEntity());
            recordHistory(event.getSession(), entry);
        }
    }

    // 另外启动一个session处理审核日志的保存
    private void recordHistory(Session session, HistoryEntry entry) {
        Session tempSession = session.getSessionFactory().openSession();
        Transaction tx = tempSession.beginTransaction();
        try {
            tx.begin();
            tempSession.save(entry);
            tempSession.flush();
            tx.commit();
        } catch (Exception ex) {
            tx.rollback();
        }
        tempSession.close();
    }
}