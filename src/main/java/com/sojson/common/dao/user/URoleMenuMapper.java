package com.sojson.common.dao.user;

import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.sojson.common.model.URoleMenu;

public interface URoleMenuMapper {
    int insert(URoleMenu record);

    int insertSelective(URoleMenu record);
    
    int deleteByRoleIdAndMenuId(Long roleId, Long menuId);
    
    Set<Long> findMenuByRoleId(@Param(value="roleId")Long roleId);
}