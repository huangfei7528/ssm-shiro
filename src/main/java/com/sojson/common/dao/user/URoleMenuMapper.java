package com.sojson.common.dao.user;

import com.sojson.common.model.URoleMenu;

public interface URoleMenuMapper {
    int insert(URoleMenu record);

    int insertSelective(URoleMenu record);
}