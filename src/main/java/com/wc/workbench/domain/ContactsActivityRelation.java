package com.wc.workbench.domain;

import lombok.Data;

@Data
public class ContactsActivityRelation {
	private String id;
	private String contactsId;
	private String activityId;

	public ContactsActivityRelation(String id, String contactsId, String activityId) {
		this.id = id;
		this.contactsId = contactsId;
		this.activityId = activityId;
	}
}
