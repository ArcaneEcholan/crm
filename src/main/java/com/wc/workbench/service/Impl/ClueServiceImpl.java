package com.wc.workbench.service.Impl;

import com.wc.settings.dao.UserDao;
import com.wc.settings.domain.User;
import com.wc.utils.DateTimeUtil;
import com.wc.utils.SqlSessionUtil;
import com.wc.utils.UUIDUtil;
import com.wc.vo.PageVo;
import com.wc.workbench.dao.*;
import com.wc.workbench.domain.*;
import com.wc.workbench.service.ClueService;

import javax.print.attribute.HashAttributeSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {
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
//
//    public List<Clue> showClueList(Clue clue) {
//
//        return clueDao.queryClueListByClue(clue);
//    }

    public Clue getClueById(String id) {

        return clueDao.queryClueById(id);
    }

    public List<Activity> getRelatedActivityByClueId(String id) {
        return clueDao.queryRelatedActivityByClueId(id);
    }

    public boolean delRelationByClueIdAndActivityId(Map<String, String> map) {
        return clueDao.delRelationByClueIdAndActivityId(map);
    }

    public List<Activity> getAllActivities() {
        return clueDao.queryAllActivities();
    }

    public List<Activity> getAllActivitiesByNameAndNotRelatedWithClue(Map<String, String> map) {
        return clueDao.queryAllActivitiesByNameAndNotRelatedWithClue(map);
    }

    public boolean relateClueAndActivities(String clueId, String[] actIds) {

        int count = 0;
        //循环插入所有关系
        for(int i = 0; i < actIds.length; i++) {
            ClueActivityRelation clueActivityRelation = new ClueActivityRelation();

            clueActivityRelation.setId(UUIDUtil.getUUID());
            clueActivityRelation.setActivityId(actIds[i]);
            clueActivityRelation.setClueId(clueId);

            if(clueDao.insertRelation(clueActivityRelation)) {
                count ++;
            }
        }
        //检查是否真的插入了所有的关系
        if(count == actIds.length) {
            return true;
        }
        return false;
    }

    public List<Activity> searchActivityByName(String aname) {


        return clueDao.queryActivityByName(aname);
    }

    /**
     * 完成线索表，备注表，关系表的转换以及交易表的插入
     * @param clueId
     * @param transaction
     * @param createBy
     * @return
     */
    public boolean convert(String clueId, Tran transaction, String createBy) {
        //标记转换是否成功
        boolean flag = true;

        //以下创建的记录共用一个创建时间
        String createTime = DateTimeUtil.getSysTime();

        //查询待转换的线索
        Clue targetClue = clueDao.queryClueById2(clueId);

        //转换公司
        String companyName = targetClue.getCompany();
        Customer company = customerDao.queryCustomerByName(companyName);    //查询公司（客户）是否已经存在
        if(company == null) {               //如果公司不存在，就将其写入数据库

            company = new Customer();
            company.setId(UUIDUtil.getUUID());
            company.setName(companyName);
            company.setPhone(targetClue.getPhone());
            company.setWebsite(targetClue.getWebsite());
            company.setAddress(targetClue.getAddress());
            company.setDescription(targetClue.getDescription());
            company.setOwner(targetClue.getOwner());
            company.setCreateBy(createBy);
            company.setCreateTime(createTime);
            company.setNextContactTime(targetClue.getNextContactTime());
            company.setContactSummary(targetClue.getContactSummary());

            if(!customerDao.save(company)) {            //将客户写入数据库
                flag = false;
            }
        }

        System.out.println("====================================客户转换成功========================================");

        //转换联系人
        Contacts contacts = new Contacts();

        contacts.setSource(targetClue.getSource());
        contacts.setFullname(targetClue.getFullname());
        contacts.setAppellation(targetClue.getAppellation());
        contacts.setEmail(targetClue.getEmail());
        contacts.setMphone(targetClue.getMphone());
        contacts.setJob(targetClue.getJob());
        contacts.setContactSummary(targetClue.getContactSummary());
        contacts.setNextContactTime(targetClue.getNextContactTime());
        contacts.setOwner(targetClue.getOwner());
        contacts.setDescription(targetClue.getDescription());
        contacts.setAddress(targetClue.getAddress());

        contacts.setId(UUIDUtil.getUUID());
        contacts.setCustomerId(company.getId());
        contacts.setCreateBy(createBy);
        contacts.setCreateTime(createTime);

        if(!contactsDao.save(contacts)) {        //将联系人存入数据库
            flag = false;
        }

        System.out.println("====================================联系人转换成功========================================");


        //转换备注表
        List<ClueRemark> targetClueRemarks = clueRemarkDao.queryRemarkByClueId(clueId);
        for(ClueRemark clueRemark:targetClueRemarks) {
            String noteContent = clueRemark.getNoteContent();


            ContactsRemark contactsRemark = new ContactsRemark();//转换联系人备注
            contactsRemark.setNoteContent(noteContent);

            contactsRemark.setNoteContent(noteContent);
            contactsRemark.setContactsId(contacts.getId());
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setEditFlag("0");

            if(!contactsRemarkDao.save(contactsRemark)) {
                flag = false;
            }


            CustomerRemark customerRemark = new CustomerRemark();//转换公司备注
            customerRemark.setNoteContent(noteContent);

            customerRemark.setNoteContent(noteContent);
            customerRemark.setCustomerId(company.getId());
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCreateTime(createTime);
            customerRemark.setCreateBy(createBy);
            customerRemark.setEditFlag("0");

            if(!customerRemarkDao.save(customerRemark)) {
                flag = false;
            }
        }

        System.out.println("====================================备注表转换成功========================================");


        //转换线索与市场活动关系表
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.queryRelationByClueId(clueId);
        for(ClueActivityRelation clueActivityRelation:clueActivityRelationList) {
            String activityId = clueActivityRelation.getActivityId();

            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();

            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setContactsId(contacts.getId());

            if(!contactsActivityRelationDao.save(contactsActivityRelation)) {
                flag = false;
            }
        }

        System.out.println("====================================活动关系转换成功========================================");


        //如果有交易，创建一条交易
        if(transaction != null) {
            System.out.println("====================================开始创建交易========================================");

            transaction.setSource(targetClue.getSource());
            transaction.setOwner(targetClue.getOwner());
            transaction.setNextContactTime(targetClue.getNextContactTime());
            transaction.setDescription(targetClue.getDescription());
            transaction.setCustomerId(company.getId());
            transaction.setContactsId(contacts.getId());
            transaction.setContactSummary(contacts.getContactSummary());

            if(!tranDao.createTran(transaction)) {
                flag = false;
            }


            TranHistory tranHistory = new TranHistory();    //为交易创建一条历史记录

            tranHistory.setCreateBy(transaction.getCreateBy());
            tranHistory.setCreateTime(transaction.getCreateTime());
            tranHistory.setTranId(transaction.getId());
            tranHistory.setExpectedDate(transaction.getExpectedDate());
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setMoney(transaction.getMoney());
            tranHistory.setStage(transaction.getStage());

            if(!tranHistoryDao.save(tranHistory)) {
                flag = false;
            }
        }

        System.out.println("====================================交易创建成功========================================");



        //删除线索备注
        if(!clueRemarkDao.delByClueId(clueId)) {
            flag = false;
            System.out.println("fail");

        }
        System.out.println("====================================线索备注删除成功========================================");

        //删除线索和市场活动关联关系
        if(!clueActivityRelationDao.delByClueId(clueId)) {
            flag = false;
            System.out.println("fail");

        }
        System.out.println("====================================活动关系删除成功========================================");

        //删除线索
        if(!clueDao.delById(clueId)) {
            flag = false;
            System.out.println("fail");
        }
        System.out.println("====================================线索删除成功========================================");


        return flag;
    }

    public PageVo<Clue> page(Map<String, Object> map) {
        //获取总记录数和每页的数据
        List<Clue> clueList = clueDao.queryPageCluesByConditions(map);
        int totalCount = clueDao.queryTotalClueCountByConditions(map);
        
        //计算总页数
        Integer pageSize = (Integer)map.get("pageSize");
        Integer totalPages = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;

        return new PageVo<Clue>(totalCount, totalPages, clueList);
    }

    public boolean removeCluesByIds(String[] ids) {
        boolean flag = true;

        for (int i = 0; i < ids.length; i++) {
            if(!clueDao.removeClueById(ids[i])) {
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

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("clue", clue);
        map.put("clueRemarkList", clueRemarkList);
        return map;
    }

    public boolean editClueRemarkContentByRemarkId(ClueRemark clueRemark) {

        return clueRemarkDao.updateClueRemarkContentByRemarkId(clueRemark);
    }
}
