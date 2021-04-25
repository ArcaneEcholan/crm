package com.wc.workbench.domain;


import lombok.Data;

@Data
public class Customer {
	private String id;
	private String owner;
	private String name;
	private String website;
	private String phone;
	private String createBy;
	private String createTime;
	private String editBy;
	private String editTime;
	private String contactSummary;
	private String nextContactTime;
	private String description;
	private String address;
}
