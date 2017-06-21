package com.sojson.menu.bo;

import java.util.List;

public class URoleMenuBo {
    private Long rId;

    private Long mId;

    private Long roleId;
    
    private List<Long> addMenus;
    
    public Long getrId() {
        return rId;
    }

    public void setrId(Long rId) {
        this.rId = rId;
    }

    public Long getmId() {
        return mId;
    }

    public void setmId(Long mId) {
        this.mId = mId;
    }

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public List<Long> getAddMenus() {
		return addMenus;
	}

	public void setAddMenus(List<Long> addMenus) {
		this.addMenus = addMenus;
	}
    
}