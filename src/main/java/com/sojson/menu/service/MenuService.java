package com.sojson.menu.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sojson.menu.bo.UMenuBo;

public interface MenuService {
	
	int deleteByPrimaryKey(Long id);

    int insert(UMenuBo record);

    int insertSelective(UMenuBo record);

    UMenuBo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UMenuBo record);

    int updateByPrimaryKey(UMenuBo record);
    /**
     * 查询所有菜单
     * @return
     */
    Map<Long, UMenuBo> findMenuAll();
	/**
	 * 查询所有子级菜单 (根据父级ID)
	 * @return
	 */
    Set<UMenuBo> findMenuByParentId(Long parentId);
    
	//查询角色所拥有菜单
	Set<Long> findMenuByRoleId(Long roleId);
    
}
