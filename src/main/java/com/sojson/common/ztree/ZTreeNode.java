package com.sojson.common.ztree;

/**
 * 前台树形结构展示用到的实体对象
 * @author yuzhou
 *
 */
public class ZTreeNode {
		
	private String id;//当前节点ID
	private String pId;//父节点ID
	private String name;//节点名称
	private boolean open;//是否展开
	private boolean isParent;//当前节点是否是父级节点（即是否有孩子节点）
	private boolean checked;//是否 默认选中
	private Integer openLevel;//设定自动展开前面几级，1表示展开第一级，2表示展开前面两级
	private String type;//菜单类型
	
	public ZTreeNode() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ZTreeNode(String id, String pId, String name, boolean open,
			boolean isParent) {
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.open = open;
		this.isParent = isParent;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}
	
	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Integer getOpenLevel() {
		return openLevel;
	}

	public void setOpenLevel(Integer openLevel) {
		this.openLevel = openLevel;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String toString() {
		// TODO Auto-generated method stub
		return "id=" + this.id
				+ ",pId=" + this.pId
				+ ",name=" + this.name
				+ ",open=" + this.open
				+ ",isParent=" + this.isParent;
	}
}
