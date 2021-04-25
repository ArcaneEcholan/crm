package com.wc.workbench.domain;

import lombok.Data;

@Data
public class ClueActivityRelation {
	private String id;
	private String clueId;
	private String activityId;

	public ContactsActivityRelation toContactsActivityRelation(String id) {
		return new ContactsActivityRelation(id, clueId, activityId);
	}
}
