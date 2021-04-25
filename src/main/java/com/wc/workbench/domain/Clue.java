package com.wc.workbench.domain;

import com.wc.utils.DateTimeUtil;
import com.wc.utils.UUIDUtil;
import lombok.Data;

@Data
public class Clue {
	
	private String id;	//主键
	private String fullname;	//全名（人的名字）
	private String appellation;	//称呼
	private String owner;	//所有者
	private String company;	//公司名称
	private String job;	//职业
	private String email;	//邮箱
	private String phone;	//公司电话
	private String website;	//公司网站
	private String mphone;	//手机
	private String state;	//状态
	private String source;	//来源
	private String createBy;	//创建人
	private String createTime;	//创建时间
	private String editBy;	//修改人
	private String editTime;	//修改时间
	private String description;	//描述
	private String contactSummary;	//联系纪要
	private String nextContactTime;	//下次联系时间
	private String address;	//地址

	public Clue() {
	}

	public Clue(String id, String fullname, String appellation, String owner, String company, String job, String email, String phone, String website, String mphone, String state, String source, String createBy, String createTime, String editBy, String editTime, String description, String contactSummary, String nextContactTime, String address) {
		this.id = id;
		this.fullname = fullname;
		this.appellation = appellation;
		this.owner = owner;
		this.company = company;
		this.job = job;
		this.email = email;
		this.phone = phone;
		this.website = website;
		this.mphone = mphone;
		this.state = state;
		this.source = source;
		this.createBy = createBy;
		this.createTime = createTime;
		this.editBy = editBy;
		this.editTime = editTime;
		this.description = description;
		this.contactSummary = contactSummary;
		this.nextContactTime = nextContactTime;
		this.address = address;
	}

// 无法映射  id
//          createBy
//          createTime
//			customerId
	public Contacts toContacts(String id, String customerId, String createBy) {
		Contacts contacts = new Contacts();
		contacts .setOwner(owner);
		contacts .setSource(source);
		contacts .setFullname(fullname);
		contacts .setAppellation(appellation);
		contacts .setEmail(email);
		contacts .setMphone(mphone);
		contacts .setJob(job);
		contacts .setDescription(description);
		contacts .setContactSummary(contactSummary);
		contacts .setNextContactTime(nextContactTime);
		contacts .setAddress(address);
		contacts.setId(UUIDUtil.getUUID());
		contacts.setCustomerId(customerId);
		contacts.setCreateTime(DateTimeUtil.getSysTime());
		contacts.setCreateBy(createBy);
		return contacts;
	}

// 无法映射  id
//          createBy
//          createTime
	public Customer toCustomer(String id, String createBy) {
		Customer customer = new Customer();
		customer .setOwner(owner);
		customer.setPhone(phone);
		customer.setName(company);
		customer.setWebsite(website);
		customer .setDescription(description);
		customer .setContactSummary(contactSummary);
		customer .setNextContactTime(nextContactTime);
		customer .setAddress(address);
		customer.setId(UUIDUtil.getUUID());
		customer.setCreateTime(DateTimeUtil.getSysTime());
		customer.setCreateBy(createBy);
		return customer;
	}
}
