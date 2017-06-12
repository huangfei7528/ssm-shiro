package com.sojson.menu.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.sojson.common.utils.Constants;

public class UMenuBo implements Serializable{
	
	private static final long serialVersionUID = -4133445002662497448L;

	private Long id;

    private Long pid;

    private String name;

    private String url;

    private String logoUrl;

    private String remark;

    private Long orderBy;

    private Date createTime;

    private Date modifeTime;

    private UMenuBo parentMenuBo;
    
    private Set<UMenuBo> childrenList = new HashSet<UMenuBo>();
    /**
	 * 树形结构展示时，构造的顶级目录
	 * @return
	 */
	public static UMenuBo getRootBean(){
		UMenuBo rootBean = new UMenuBo();
		rootBean.setId(Constants.TREE_ROOT_ID);
		return rootBean;
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
        this.name = name == null ? null : name.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl == null ? null : logoUrl.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Long getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Long orderBy) {
        this.orderBy = orderBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifeTime() {
        return modifeTime;
    }

    public void setModifeTime(Date modifeTime) {
        this.modifeTime = modifeTime;
    }

	public UMenuBo getParentMenuBo() {
		return parentMenuBo;
	}

	public void setParentMenuBo(UMenuBo parentMenuBo) {
		this.parentMenuBo = parentMenuBo;
	}

	public Set<UMenuBo> getChildrenList() {
		return childrenList;
	}

	public void setChildrenList(Set<UMenuBo> childrenList) {
		this.childrenList = childrenList;
	}
    
    
}