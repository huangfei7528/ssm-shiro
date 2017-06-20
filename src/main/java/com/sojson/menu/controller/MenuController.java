package com.sojson.menu.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.sojson.common.controller.BaseController;
import com.sojson.common.utils.Constants;
import com.sojson.common.utils.SpringRedisUtils;
import com.sojson.common.ztree.MenuZTreeNode;
import com.sojson.menu.bo.UMenuBo;
import com.sojson.menu.service.MenuService;
@Controller
@Scope(value="prototype")
@RequestMapping("menu")
public class MenuController extends BaseController{

	@Autowired
	private MenuService menuService;
	
	@RequestMapping(value="menu",method=RequestMethod.GET)
	public ModelAndView menu(){
		return new ModelAndView("menu/menu");
	}
	
	@ResponseBody
	@RequestMapping(value="addMenu",method=RequestMethod.POST)
	public Map<String, Object> addMenu(UMenuBo menu){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try{
			if(menu == null){
				resultMap.put("status", 500);
				resultMap.put("message", "请输入菜单信息。");
			}else{
				if(menu.getPid() == null){
					resultMap.put("status", 500);
					resultMap.put("message", "请选择父级菜单。");
				}else if(menu.getName() == null){
					resultMap.put("status", 500);
					resultMap.put("message", "请选择菜单名称。");
				}else{
					int id = menuService.insertSelective(menu);
					menu = menuService.selectByPrimaryKey(Long.valueOf(id));
					//取出父菜单
					UMenuBo pMenu = (UMenuBo) SpringRedisUtils.getHashKey(Constants.ALL_MENU, menu.getPid()+"");
					menu.setParentMenuBo(pMenu);
					pMenu.getChildrenList().add(menu);
//					makeMenuTreeNode(pMenu, StringUtils.isEmpty(pMenu.getPid()));
					//更新父级菜单缓存
					SpringRedisUtils.setHashKey(Constants.ALL_MENU, menu.getPid()+"", pMenu);
					//新增添加的菜单
					SpringRedisUtils.setHashKey(Constants.ALL_MENU, menu.getId()+"", menu);
					MenuZTreeNode menuNode = makeMenuTreeNode(menu, StringUtils.isEmpty(menu.getPid()));
					resultMap.put("status", 200);
					resultMap.put("message", "添加成功。");
					resultMap.put("node", JSON.toJSONString(menuNode));
				}
			}
		}catch(Exception e){
			logger.error(e);
			resultMap.put("status", 500);
			resultMap.put("message", "添加菜单失败,请稍后重试!");
		}
		return resultMap;
	}
	@ResponseBody
	@RequestMapping(value="targetEdit",method=RequestMethod.POST)
	public Map<String, Object> targetEdit(Long id){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(id == null){
			resultMap.put("status", 500);
			resultMap.put("message", "请选择需要删除的节点。");
		}else{
			try{
				UMenuBo menu = menuService.selectByPrimaryKey(id);
				resultMap.put("node", JSON.toJSONString(menu));
				resultMap.put("status", 200);
				resultMap.put("message", "查询成功。");
			}catch(Exception e){
				logger.error(e);
				resultMap.put("status", 500);
				resultMap.put("message", "查询失败。");
			}
		}
		return resultMap;
	}
	
	@ResponseBody
	@RequestMapping(value="editMenu",method=RequestMethod.POST)
	public Map<String, Object> editMenu(UMenuBo menu){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(menu == null){
			resultMap.put("status", 500);
			resultMap.put("message", "请选择需要修改的菜单。");
		}else{
			try{
				menuService.updateByPrimaryKeySelective(menu);
				resultMap.put("status", 200);
				resultMap.put("message", "修改成功!");
			}catch(Exception e){
				logger.error(e);
				resultMap.put("status", 200);
				resultMap.put("message", "修改失败!");
			}
		}
		return resultMap;
	}
	
	@ResponseBody
	@RequestMapping(value="deleteMenu",method=RequestMethod.POST)
	public Map<String, Object> deleteMenu(Long id){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(id == null){
			resultMap.put("status", 500);
			resultMap.put("message", "请选择需要删除的节点。");
		}else{
			try{
				Set<UMenuBo> boList = menuService.findMenuByParentId(id);
				if(boList != null && boList.size()>0 ){
					resultMap.put("status", 500);
					resultMap.put("message", "删除失败,请先删除该节点下子节点!");
				}else{
					menuService.deleteByPrimaryKey(id);
					resultMap.put("status", 200);
					resultMap.put("message", "删除成功!");
				}
			}catch(Exception e){
				logger.error(e);
				resultMap.put("status", 500);
				resultMap.put("message", "删除失败,请稍后重试!");
			}
		}
		return resultMap;
	}
	
	@ResponseBody
	@RequestMapping(value="loadMenu",method=RequestMethod.POST)
	public Map<String, Object> loadMenu(Long parentId){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<Long, UMenuBo> menuMap = new HashMap<Long, UMenuBo>();
		try {
			menuMap = SpringRedisUtils.getMap(Constants.ALL_MENU, Long.class, UMenuBo.class);
			if(parentId == null){
				parentId = Constants.TREE_ROOT_ID;
			}
			Set<UMenuBo> menuList = menuMap.get(parentId.toString()).getChildrenList();
			List<MenuZTreeNode> menuNodeList = new ArrayList<MenuZTreeNode>();
			if(!menuList.isEmpty()){
				for(UMenuBo bean: menuList){
					MenuZTreeNode menuNode = makeMenuTreeNode(bean, StringUtils.isEmpty(parentId));////首级节点，自动展开
					menuNode.setOpenLevel(2);//自动展开前面2级
					menuNodeList.add(menuNode);
				}
				Collections.sort(menuNodeList);
			}
			
			String json = JSON.toJSONString(menuNodeList);
			resultMap.put("status", 200);
			resultMap.put("message", "查询成功");
			resultMap.put("treeList", json);
		}catch(Exception e){
			logger.error(e);
			resultMap.put("status", 500);
			resultMap.put("message", "系统错误,请稍后重试!");
		}
		return resultMap;
	}
	

	/**
	 * 构造前台展示树的数据格式
	 * @param bean原始数据格式
	 * @param open是否需要展开
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private MenuZTreeNode makeMenuTreeNode(UMenuBo bean, boolean open) 
			throws InstantiationException, IllegalAccessException{
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
