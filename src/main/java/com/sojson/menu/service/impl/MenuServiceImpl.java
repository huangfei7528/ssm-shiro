package com.sojson.menu.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sojson.common.dao.user.UMenuMapper;
import com.sojson.common.dao.user.URoleMapper;
import com.sojson.common.model.UMenu;
import com.sojson.common.utils.BeanUtils;
import com.sojson.common.utils.Constants;
import com.sojson.core.mybatis.BaseMybatisDao;
import com.sojson.menu.bo.UMenuBo;
import com.sojson.menu.service.MenuService;

@Service
public class MenuServiceImpl extends BaseMybatisDao<URoleMapper> implements MenuService {

	@Autowired
	private UMenuMapper menuMapper;
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return menuMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(UMenu record) {
		return menuMapper.insert(record);
	}

	@Override
	public int insertSelective(UMenu record) {
		return menuMapper.insertSelective(record);
	}

	@Override
	public UMenuBo selectByPrimaryKey(Long id) {
		UMenuBo bo = new UMenuBo();
		UMenu m = menuMapper.selectByPrimaryKey(id);
		BeanUtils.copyNotNullProperties(bo, m);
		return bo;
	}

	@Override
	public int updateByPrimaryKeySelective(UMenu record) {
		return menuMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(UMenu record) {
		return menuMapper.updateByPrimaryKey(record);
	}

	@Override
	public Map<Long, UMenuBo> findMenuAll() {
		Set<UMenu> menuSet = menuMapper.findMenuByParentId(null);
		Map<Long, UMenuBo> menuMap = new HashMap<Long, UMenuBo>();
		menuMap.put(Constants.TREE_ROOT_ID, UMenuBo.getRootBean());
		if(!menuSet.isEmpty()){
			for(UMenu menu : menuSet){
				UMenuBo menuBo = new UMenuBo();
				BeanUtils.copyNotNullProperties(menu, menuBo);
				menuMap.put(menuBo.getId(), menuBo);
			}
			for(UMenu m : menuSet){
				do{
					UMenu pMenu = menuMapper.selectByPrimaryKey(m.getPid());
					if(pMenu == null){//父节点为空，则设置为顶级目录
						menuMap.get(m.getId()).setParentMenuBo(menuMap.get(Constants.TREE_ROOT_ID));
					}else{
						if(!menuMap.containsKey(pMenu.getId())){
							UMenuBo parentMenu = new UMenuBo();
							BeanUtils.copyNotNullProperties(pMenu, parentMenu);
							menuMap.put(pMenu.getId(), parentMenu);
						}
						menuMap.get(m.getId()).setParentMenuBo(menuMap.get(pMenu.getId()));
					}
					if(pMenu != null && pMenu.getPid() != null)
						m = menuMapper.selectByPrimaryKey(pMenu.getPid());
					else
						m = null;
				}while(m != null);
			}
			
			for(UMenuBo bo: menuMap.values()){//遍历计算孩子信息
				if(bo.getParentMenuBo() != null){
					bo.getParentMenuBo().getChildrenList().add(bo);
				}
			}
		}
		return menuMap;
	}

	@Override
	public Set<UMenuBo> findMenuByParentId(Long pid) {
		Set<UMenu> menuSet = menuMapper.findMenuByParentId(pid);
		Set<UMenuBo> boSet = new HashSet<UMenuBo>();
		if(!menuSet.isEmpty()){
			for(UMenu m : menuSet ){
				UMenuBo bo = new UMenuBo();
				BeanUtils.copyNotNullProperties(m, bo);
				boSet.add(bo);
			}
		}
		return boSet;
	}

	@Override
	public List<Long> findMenuIdByRole(Long roleId) {
		return menuMapper.findMenuIdByRole(roleId);
	}

}
