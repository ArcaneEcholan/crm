package com.wc.workbench.domain;

import lombok.Data;

@Data
public class ContactsRemark {
	
	private String id;
	private String noteContent;
	private String createTime;
	private String createBy;
	private String editTime;
	private String editBy;
	private String editFlag;
	private String contactsId;

}
