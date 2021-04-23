package com.wc.workbench.domain;

public class ClueRemark implements Comparable<ClueRemark>{
	
	private String id;
	private String noteContent;
	private String createBy;
	private String createTime;
	private String editBy;
	private String editTime;
	private String editFlag;
	private String clueId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNoteContent() {
		return noteContent;
	}
	public void setNoteContent(String noteContent) {
		this.noteContent = noteContent;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getEditBy() {
		return editBy;
	}
	public void setEditBy(String editBy) {
		this.editBy = editBy;
	}
	public String getEditTime() {
		return editTime;
	}
	public void setEditTime(String editTime) {
		this.editTime = editTime;
	}
	public String getEditFlag() {
		return editFlag;
	}
	public void setEditFlag(String editFlag) {
		this.editFlag = editFlag;
	}
	public String getClueId() {
		return clueId;
	}
	public void setClueId(String clueId) {
		this.clueId = clueId;
	}

	@Override
	public String toString() {
		return "ClueRemark{" +
				"id='" + id + '\'' +
				", noteContent='" + noteContent + '\'' +
				", createBy='" + createBy + '\'' +
				", createTime='" + createTime + '\'' +
				", editBy='" + editBy + '\'' +
				", editTime='" + editTime + '\'' +
				", editFlag='" + editFlag + '\'' +
				", clueId='" + clueId + '\'' +
				'}';
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
