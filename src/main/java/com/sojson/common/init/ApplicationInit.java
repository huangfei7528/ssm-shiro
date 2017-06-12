package com.sojson.common.init;

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.sojson.common.utils.Constants;
import com.sojson.common.utils.SpringRedisUtils;
import com.sojson.menu.bo.UMenuBo;
import com.sojson.menu.service.MenuService;

/*@Repository*/
public class ApplicationInit implements InitializingBean {

	@Autowired
	private MenuService menuService;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("容器初始化后加载数据..........");
		//缓存所有菜单
		Map<Long, UMenuBo> menuMap = menuService.findMenuAll();
		SpringRedisUtils.setMap(Constants.ALL_MENU, menuMap);
	}

}
