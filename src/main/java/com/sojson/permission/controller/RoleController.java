package com.sojson.permission.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.sojson.common.controller.BaseController;
import com.sojson.common.model.URole;
import com.sojson.common.utils.BeanUtils;
import com.sojson.common.utils.Constants;
import com.sojson.common.utils.LoggerUtils;
import com.sojson.common.utils.SpringRedisUtils;
import com.sojson.common.ztree.MenuZTreeNode;
import com.sojson.core.mybatis.page.Pagination;
import com.sojson.menu.bo.UMenuBo;
import com.sojson.menu.service.MenuService;
import com.sojson.permission.service.RoleService;
import com.sojson.user.manager.UserManager;
/**
 * 
 * 开发公司：itboy.net<br/>
 * 版权：itboy.net<br/>
 * <p>
 * 
 * 用户角色管理
 * 
 * <p>
 * 
 * 区分　责任人　日期　　　　说明<br/>
 * 创建　周柏成　2016年5月26日 　<br/>
 * <p>
 * *******
 * <p>
 * @author zhou-baicheng
 * @email  i@itboy.net
 * @version 1.0,2016年5月26日 <br/>
 * 
 */
@Controller
@Scope(value="prototype")
@RequestMapping("role")
public class RoleController extends BaseController {
	@Autowired
	private RoleService roleService;
	@Autowired
	private MenuService menuService;
	
	@RequestMapping(value="tragetMTree")
	public ModelAndView tragetMTree(Long roleId){
		return new ModelAndView("role/tragetMtree","roleId",roleId);
	}
	
	@ResponseBody
	@RequestMapping(value="loadRoleMenu", method=RequestMethod.POST)
	public Map<String, Object> loadRoleMenu(Long roleId,Long parentId){
		try {
			//取出全部菜单信息
			Map<Long, UMenuBo> menuMap = SpringRedisUtils.getMap(Constants.ALL_MENU, Long.class, UMenuBo.class);
			Set<Long> roleMenuList = menuService.findMenuByRoleId(roleId);
			
			List<MenuZTreeNode> menuNodeList = new ArrayList<MenuZTreeNode>();
			for(Object key: menuMap.keySet()){
				Long menuId = Long.valueOf(key.toString());
				if(menuId.equals(Constants.TREE_ROOT_ID)){//跳过虚拟节点
					continue;
				}
				MenuZTreeNode menuNode = makeMenuTreeNode(menuMap.get(key), true);
				if(roleMenuList != null && roleMenuList.contains(menuId)){
					menuNode.setChecked(true);//选中已经赋予的菜单
				}
				menuNodeList.add(menuNode);
				Collections.sort(menuNodeList);
			}
			resultMap.put("status", 200);
			resultMap.put("message", "加载角色菜单成功");
			resultMap.put("treeList", JSON.toJSONString(menuNodeList));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			resultMap.put("status", 500);
			resultMap.put("message", "加载角色菜单失败");
		}
		return resultMap;
	}
	
	/**
	 * 角色列表
	 * @return
	 */
	@RequestMapping(value="index")
	public ModelAndView index(String findContent,ModelMap modelMap){
		modelMap.put("findContent", findContent);
		Pagination<URole> role = roleService.findPage(modelMap,pageNo,pageSize);
		return new ModelAndView("role/index","page",role);
	}
	/**
	 * 角色添加
	 * @param role
	 * @return
	 */
	@RequestMapping(value="addRole",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addRole(URole role){
		try {
			int count = roleService.insertSelective(role);
			resultMap.put("status", 200);
			resultMap.put("successCount", count);
		} catch (Exception e) {
			resultMap.put("status", 500);
			resultMap.put("message", "添加失败，请刷新后再试！");
			LoggerUtils.fmtError(getClass(), e, "添加角色报错。source[%s]",role.toString());
		}
		return resultMap;
	}
	/**
	 * 删除角色，根据ID，但是删除角色的时候，需要查询是否有赋予给用户，如果有用户在使用，那么就不能删除。
	 * @param id
	 * @return
	 */
	@RequestMapping(value="deleteRoleById",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteRoleById(String ids){
		return roleService.deleteRoleById(ids);
	}
	/**
	 * 我的权限页面
	 * @return
	 */
	@RequestMapping(value="mypermission",method=RequestMethod.GET)
	public ModelAndView mypermission(){
		return new ModelAndView("permission/mypermission");
	}
	/**
	 * 我的权限 bootstrap tree data
	 * @return
	 */
	@RequestMapping(value="getPermissionTree",method=RequestMethod.POST)
	@ResponseBody
	public List<Map<String, Object>> getPermissionTree(){
		//查询我所有的角色 ---> 权限
		List<URole> roles = roleService.findNowAllPermission();
		//把查询出来的roles 转换成bootstarp 的 tree数据
		List<Map<String, Object>> data = UserManager.toTreeData(roles);
		return data;
	}
	
	/**
	 * 构造前台展示树的数据格式
	 * @param bean原始数据格式
	 * @param open是否需要展开
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private MenuZTreeNode makeMenuTreeNode(UMenuBo bean, boolean open) {
		MenuZTreeNode node = new MenuZTreeNode();
		node.setId(bean.getId());
		node.setPId(bean.getParentMenuBo().getId());
		node.setName(bean.getName());
		node.setOpen(open);
		node.setUrl(bean.getUrl());
		node.setOrderBy(bean.getOrderBy());
		node.setLogoUrl(bean.getLogoUrl());
		
		UMenuBo menu = (UMenuBo) SpringRedisUtils.getHashKey(Constants.ALL_MENU, bean.getId()+"");
		if(menu.getChildrenList() != null && !menu.getChildrenList().isEmpty()){//是否有孩子节点
			node.setIsParent(true);
		}else{
			node.setIsParent(false);
		}
		return node;
	}
}
