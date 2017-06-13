package com.sojson.common.ztree;

import java.util.HashSet;
import java.util.Set;

public class LayuiTreeNode implements Comparable<LayuiTreeNode>{

	private Long id;
	
	private Long pid;
	
	private String name;
	
	private String alias;
	
	private Boolean spread;
	
	private String href;
	
	private String url;
	
	private Boolean isParent;//当前节点是否是父级节点（即是否有孩子节点）	
	
	private Boolean nextLevel;//是否自动展开下一级
	
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

	public Boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}

	public Boolean getNextLevel() {
		return nextLevel;
	}

	public void setNextLevel(Boolean nextLevel) {
		this.nextLevel = nextLevel;
	}

	
	
	
}
