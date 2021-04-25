package com.wc.workbench.domain;

import com.wc.utils.DateTimeUtil;
import lombok.Data;
@Data
public class ClueRemark implements Comparable<ClueRemark>{
	
	private String id;
	private String noteContent;
	private String createBy;
	private String createTime;
	private String editBy;
	private String editTime;
	private String editFlag;
	private String clueId;

	/**
	 * 无法映射  id
	 * createTime
	 * createBy
	 * editFlag
	 * customerId
	 */
	public CustomerRemark toCustomerRemark(String id, String createBy, String editFlag, String customerId) {
		CustomerRemark customerRemark = new CustomerRemark();
		customerRemark.setId(id);
		customerRemark.setCustomerId(customerId);
		customerRemark.setEditFlag(editFlag);
		customerRemark.setCreateTime(DateTimeUtil.getSysTime());
		customerRemark.setCreateBy(createBy);
		customerRemark.setNoteContent(noteContent);
		return customerRemark;
	}

	/**
	 * 无法映射  id
	 * createTime
	 * createBy
	 * editFlag
	 * contactsId
	 */
	public ContactsRemark toContactsRemark(String id, String createBy, String editFlag, String contactsId){
		ContactsRemark contactsRemark = new ContactsRemark();
		contactsRemark.setId(id);
		contactsRemark.setContactsId(contactsId);
		contactsRemark.setEditFlag(editFlag);
		contactsRemark.setCreateTime(DateTimeUtil.getSysTime());
		contactsRemark.setCreateBy(createBy);
		contactsRemark.setNoteContent(noteContent);
		return contactsRemark;
	}


	public int compareTo(ClueRemark o) {
		String time1 = null;
		String time2 = null;
		if("0".equals(editFlag)) {
			time1 = createTime;
		} else {
			time1 = editTime;
		}
		if("0".equals(o.editFlag)) {
			time2 = o.createTime;
		} else {
			time2 = o.editTime;
		}

		return time1.compareTo(time2);
	}
}
