package com.sojson.common.ztree;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LayuiTreeNode implements Comparable<LayuiTreeNode>{

	private Long id;
	
	private Long pid;
	
	private String name;
	
	private String alias;
	
	private Boolean spread;
	
	private String href;
	
	private String url;
	
	private boolean isParent;//当前节点是否是父级节点（即是否有孩子节点）	
	
	private Integer openLevel;//设定自动展开前面几级，1表示展开第一级，2表示展开前面两级
	
	private Integer orderBy;
	
	private Set<LayuiTreeNode> children = new HashSet<LayuiTreeNode>();

	public int compareTo(LayuiTreeNode o) {
		if(null == getOrderBy()){//当前orderby为null，认为当前比较大
			return 1;
		}else if(null == getOrderBy()){//被比较对象的orderby为null，认为被比较对象比较大
			return -1;
		}else{
			int m1 = getOrderBy().intValue();
			int m2 = getOrderBy().intValue();
			if(m1 > m2){
				return 1;
			}else if(m1 < m2){
				return -1;
			}else{//当orderby相等时，取id比较
				int v1 = this.getId().intValue();
				int v2 = o.getId().intValue();
				return (v1 > v2) ? 1 : ((v1 == v2 ? 0 : -1));
			}
		}
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Boolean getSpread() {
		return spread;
	}

	public void setSpread(Boolean spread) {
		this.spread = spread;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isParent() {
		return isParent;
	}

	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}

	public Integer getOpenLevel() {
		return openLevel;
	}

	public void setOpenLevel(Integer openLevel) {
		this.openLevel = openLevel;
	}

	public Integer getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}

	public Set<LayuiTreeNode> getChildren() {
		return children;
	}

	public void setChildren(Set<LayuiTreeNode> children) {
		this.children = children;
	}

	
	
	
}
