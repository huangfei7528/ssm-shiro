package com.hf.dao;

import com.hf.model.UMenu;

public interface UMenuMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UMenu record);

    int insertSelective(UMenu record);

    UMenu selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UMenu record);

    int updateByPrimaryKey(UMenu record);
}