package com.sojson.common.dao.user;

import java.util.List;
import java.util.Set;

import com.sojson.common.model.UMenu;

public interface UMenuMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UMenu record);

    int insertSelective(UMenu record);

    UMenu selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UMenu record);

    int updateByPrimaryKey(UMenu record);
	/**
	 * 查询所有子级菜单 (根据父级ID)
	 * @return
	 */
    Set<UMenu> findMenuByParentId(Long pid);
    
    /**
	 * 根据角色查询所有的菜单
	 * @param roleId
	 * @return
	 */
	List<Long> findMenuIdByRole(Long roleId);
}