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
import com.sojson.common.dao.user.URoleMenuMapper;
import com.sojson.common.model.UMenu;
import com.sojson.common.model.URoleMenu;
import com.sojson.common.utils.BeanUtils;
import com.sojson.common.utils.Constants;
import com.sojson.core.mybatis.BaseMybatisDao;
import com.sojson.menu.bo.UMenuBo;
import com.sojson.menu.bo.URoleMenuBo;
import com.sojson.menu.service.MenuService;

@Service
public class MenuServiceImpl extends BaseMybatisDao<URoleMapper> implements MenuService {

	@Autowired
	private UMenuMapper menuMapper;
	@Autowired
	private URoleMenuMapper roleMenuMapper;
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return menuMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(UMenuBo record) {
		UMenu u = new UMenu();
		BeanUtils.copyNotNullProperties(record, u);
		return menuMapper.insert(u);
	}

	@Override
	public int insertSelective(UMenuBo record) {
		UMenu u = new UMenu();
		BeanUtils.copyNotNullProperties(record, u);
		menuMapper.insertSelective(u);
		return u.getId().intValue();
	}

	@Override
	public UMenuBo selectByPrimaryKey(Long id) {
		UMenuBo bo = new UMenuBo();
		UMenu m = menuMapper.selectByPrimaryKey(id);
		BeanUtils.copyNotNullProperties(m, bo);
		return bo;
	}

	@Override
	public int updateByPrimaryKeySelective(UMenuBo record) {
		UMenu u = new UMenu();
		BeanUtils.copyNotNullProperties(record, u);
		return menuMapper.updateByPrimaryKeySelective(u);
	}

	@Override
	public int updateByPrimaryKey(UMenuBo record) {
		UMenu u = new UMenu();
		BeanUtils.copyNotNullProperties(record, u);
		return menuMapper.updateByPrimaryKey(u);
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
	public Set<Long> findMenuByRoleId(Long roleId) {
		Set<Long> menuSet = new HashSet<Long>();
		if(roleId != null){
			menuSet = roleMenuMapper.findMenuByRoleId(roleId);
		}
		return menuSet;
	}

	@Override
	public Boolean updateByRoleAndMenu(Long roleId, List<Long> addMenus) {
		//现有的菜单信息
		Set<Long> menuSet = this.findMenuByRoleId(roleId);
		if(addMenus == null || addMenus.size()<=0){
			return true;
		}
		//需要添加的的菜单
		Set<Long> addMenuSet = new HashSet<Long>();
		//数据库已有的菜单 不需要在删除
		Set<Long> hasMenuSet = new HashSet<Long>();
		//若查询数据库中菜单信息为存在 则添加的全为需要添加的菜单信息
		if(menuSet == null || menuSet.size() <= 0){
			addMenuSet.addAll(addMenus);
		}else{
			for(Long menuId : addMenus){
				//若已有此菜单 则添加至数据库已有菜单hasMenuSet集合 若无此菜单 则添加至需要添加的菜单addMenuSet集合
				if(menuSet.contains(menuId)){
					hasMenuSet.add(menuId);
				}else{
					addMenuSet.add(menuId);
				}
			}
			//删除menuSet中在addMenus已含有的菜单 剩下的则为需要删除的菜单
			menuSet.removeAll(hasMenuSet);
			//移除addMenus新增菜单中已有的hasMenuSet菜单信息
			addMenus.removeAll(hasMenuSet);
		}
		//删除菜单
		if(menuSet != null && menuSet.size() > 0){
			for(Long menuId : menuSet){
				roleMenuMapper.deleteByRoleIdAndMenuId(roleId, menuId);
			}
		}
		//添加新增的菜单
		if(addMenus != null && addMenus.size() > 0){
			for(Long menuId : addMenus){
				URoleMenu rm = new URoleMenu();
				rm.setmId(menuId);
				rm.setrId(roleId);
				roleMenuMapper.insert(rm);
			}
		}
		return true;
	}

}
