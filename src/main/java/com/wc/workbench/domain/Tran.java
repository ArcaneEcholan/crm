package com.wc.workbench.domain;

import com.wc.utils.DateTimeUtil;
import com.wc.utils.UUIDUtil;
import lombok.Data;

@Data
public class Tran {
	
	private String id;
	private String owner;
	private String money;	//交易金额
	private String name;	//交易名称
	private String expectedDate;	//预计成交日期
	private String customerId;
	private String stage;	//交易阶段
	private String type;	//交易类型
	private String source;	//交易来源
	private String activityId;
	private String contactsId;
	private String createBy;
	private String createTime;
	private String editBy;
	private String editTime;
	private String description;
	private String contactSummary;	//联系纪要
	private String nextContactTime;	//下次联系时间

	private String possibility;

	//不能转换：createBy,createTime,id
	public TranHistory toTranHistory(String createBy) {
		TranHistory tranHistory = new TranHistory();
		tranHistory.setId(UUIDUtil.getUUID());
		tranHistory.setExpectedDate(expectedDate);
		tranHistory.setMoney(money);
		tranHistory.setStage(stage);
		tranHistory.setCreateBy(createBy);
		tranHistory.setCreateTime(DateTimeUtil.getSysTime());
		tranHistory.setTranId(id);
		return tranHistory;
	}
}
