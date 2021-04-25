package com.wc.workbench.service.Impl;

import com.wc.settings.dao.UserDao;
import com.wc.settings.domain.User;
import com.wc.utils.SqlSessionUtil;
import com.wc.utils.UUIDUtil;
import com.wc.vo.PageVo;
import com.wc.workbench.dao.*;
import com.wc.workbench.domain.*;
import com.wc.workbench.service.ClueService;

import java.util.*;

public class ClueServiceImpl implements ClueService {
    ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);
    ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
    ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);
    TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    public boolean saveClue(Clue clue) {
        System.out.println(clue.getId());
        return clueDao.saveClue(clue);

    }

    public Clue getClueById(String id) {

        return clueDao.queryClueById(id);
    }

    public List<Activity> getRelatedActivityByClueId(String id) {
        return clueDao.queryRelatedActivityByClueId(id);
    }

    public boolean delRelationByClueIdAndActivityId(Map<String, String> map) {
        return clueDao.delRelationByClueIdAndActivityId(map);
    }

    public List<Activity> getAllNotBundedActivities() {
        return clueDao.queryAllNotBundedActivities();
    }

    public List<Activity> getAllActivitiesByNameAndNotRelatedWithClue(Map<String, String> map) {
        return clueDao.queryAllActivitiesByNameAndNotRelatedWithClue(map);
    }

    public boolean relateClueAndActivities(String clueId, String[] actIds) {

        int count = 0;
        //循环插入所有关系
        for (int i = 0; i < actIds.length; i++) {
            ClueActivityRelation clueActivityRelation = new ClueActivityRelation();

            clueActivityRelation.setId(UUIDUtil.getUUID());
            clueActivityRelation.setActivityId(actIds[i]);
            clueActivityRelation.setClueId(clueId);

            if (clueDao.insertRelation(clueActivityRelation)) {
                count++;
            }
        }
        //检查是否真的插入了所有的关系
        if (count == actIds.length) {
            return true;
        }
        return false;
    }

    public List<Activity> searchActivityByName(String aname) {


        return clueDao.queryActivityByName(aname);
    }



    public PageVo<Clue> page(Map<String, Object> map) {
        //获取总记录数和每页的数据
        List<Clue> clueList = clueDao.queryPageCluesByConditions(map);
        int totalCount = clueDao.queryTotalClueCountByConditions(map);

        //计算总页数
        Integer pageSize = (Integer) map.get("pageSize");
        Integer totalPages = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;

        return new PageVo<Clue>(totalCount, totalPages, clueList);
    }

    public boolean removeCluesByIds(String[] ids) {
        boolean flag = true;

        for (int i = 0; i < ids.length; i++) {
            if (!clueDao.removeClueById(ids[i])) {
                flag = false;
            }
        }

        return flag;
    }

    public Map<String, Object> getUserListAndClue(String id) {
        Clue clue = clueDao.queryClueById(id);
        List<User> userList = userDao.getUserList();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("clue", clue);
        map.put("userList", userList);
        return map;
    }

    public boolean updateClue(Clue clue) {
        return clueDao.updateClueByClueId(clue);
    }

    public boolean saveRemark(ClueRemark clueRemark) {
        return clueRemarkDao.saveClueRemark(clueRemark);
    }

    public Map<String, Object> showClueRemarkList(String clueId) {
        //获取线索公司
        Clue clue = clueDao.queryClueById(clueId);
        //获取备注列表
        List<ClueRemark> clueRemarkList = clueRemarkDao.queryRemarkByClueId(clueId);

        //将备注列表按修改时间或创建时间从降序排列
        Collections.sort(clueRemarkList, Collections.reverseOrder());   //降序排列

        //将数据返回web端
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("clue", clue);
        map.put("clueRemarkList", clueRemarkList);
        return map;
    }

    public boolean editClueRemarkContentByRemarkId(ClueRemark clueRemark) {

        return clueRemarkDao.updateClueRemarkContentByRemarkId(clueRemark);
    }

    public boolean removeClueRemarkByRemarkId(String id) {
        return clueRemarkDao.delByClueRemarkId(id);
    }

    public List<Activity> getNotBundedActivitiesByName(String aname) {
        return activityDao.queryNotBundedActivityByName(aname);
    }

    public boolean bundActs(List<ClueActivityRelation> list) {
        return clueActivityRelationDao.insertRelations(list);
    }

    public List<Activity> getAllRelatedActsByClueId(String clueId) {
        return activityDao.getAllRelatedActsByClueId(clueId);
    }

    public boolean unbund(Map<String, Object> map) {
        return clueActivityRelationDao.delRelationByClueIdAndActId(map);
    }


    /**
     * 完成线索的转换
     */
    public boolean convertClue(Tran tran, String createBy, String id) {
        boolean flag = true;
        Clue clue = clueDao.queryClueById2(id);

        //转换客户
        Customer customer = clue.toCustomer(UUIDUtil.getUUID(), createBy);
        if(!customerDao.save(customer)){
            flag=false;
        }

        //转换联系人
        Contacts contacts = clue.toContacts(UUIDUtil.getUUID(), customer.getId(), createBy);
        if(!contactsDao.save(contacts)){
            flag=false;
        }

        //转换备注
        List<ClueRemark> clueRemarks = clueRemarkDao.queryRemarkByClueId(id);
        List<CustomerRemark> customerRemarks = new ArrayList<>();
        CustomerRemark customerRemark = null;
        for (ClueRemark clueRemark : clueRemarks) {//逐条转换备注
            customerRemark = clueRemark.toCustomerRemark(UUIDUtil.getUUID(), createBy, "0", customer.getId());
            customerRemarks.add(customerRemark);
        }
        if(!customerRemarkDao.saveCustomerRemarks(customerRemarks)) {
            flag=false;
        }

        List<ContactsRemark> contactsRemarks = new ArrayList<>();
        ContactsRemark contactsRemark = null;
        for (ClueRemark clueRemark : clueRemarks) {//逐条转换备注
            contactsRemark = clueRemark.toContactsRemark(UUIDUtil.getUUID(), createBy, "0", contacts.getId());
            contactsRemarks.add(contactsRemark);
        }
        if(!contactsRemarkDao.saveContactsRemarks(contactsRemarks)) {
            flag=false;
        }
        System.out.println(contactsRemarks);

        //关系转换
        List<ClueActivityRelation> clueActivityRelations = clueActivityRelationDao.queryRelationByClueId(id);
        List<ContactsActivityRelation> contactsActivityRelations = new ArrayList<>();
        for(int i = 0; i < clueActivityRelations.size(); i++) {
            ContactsActivityRelation contactsActivityRelation = clueActivityRelations.get(i).toContactsActivityRelation(UUIDUtil.getUUID());
            contactsActivityRelations.add(contactsActivityRelation);
        }
        if(!contactsActivityRelationDao.saveContactsActivityRelations(contactsActivityRelations)) {
            flag=false;
        }

        //添加交易
        if(tran != null) {
            //owner\customerId\contactsId\createTime\createBy
            tran.setContactsId(contacts.getId());
            tran.setCustomerId(customer.getId());
            if(!tranDao.createTran(tran)) {
                flag=false;
            }

            TranHistory tranHistory = tran.toTranHistory(createBy);
            if(!tranHistoryDao.save(tranHistory)) {
                flag=false;
            }
        }

        //删除线索和备注以及关系
        if(!clueDao.delById(id)) {
            flag=false;
        }
        if(!clueRemarkDao.delByClueId(id)) {
            flag=false;
        }
        if(!clueActivityRelationDao.delByClueId(id)) {
            flag=false;
        }

        return flag;
    }
}