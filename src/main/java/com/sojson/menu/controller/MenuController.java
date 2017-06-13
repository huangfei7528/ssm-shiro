package com.sojson.menu.controller;

import java.util.ArrayList;
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
import com.alibaba.fastjson.JSONObject;
import com.sojson.common.controller.BaseController;
import com.sojson.common.utils.BeanUtils;
import com.sojson.common.utils.Constants;
import com.sojson.common.utils.SpringRedisUtils;
import com.sojson.common.ztree.LayuiTreeNode;
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
	@RequestMapping(value="loadMenu")
	public Map<String, Object> loadMenu(Long parentId){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<Long, UMenuBo> menuMap = new HashMap<Long, UMenuBo>();
		try {
			menuMap = SpringRedisUtils.getMap(Constants.ALL_MENU, Long.class, UMenuBo.class);
			boolean nextLevel = false;
			if(parentId == null){
				parentId = Constants.TREE_ROOT_ID;
				nextLevel = true;
				
			}
			Set<UMenuBo> menuList = menuMap.get(parentId.toString()).getChildrenList();
//			List<MenuZTreeNode> menuNodeList = new ArrayList<MenuZTreeNode>();
			List<LayuiTreeNode> menuNodeList = new ArrayList<LayuiTreeNode>();
			if(!menuList.isEmpty()){
				for(UMenuBo bean: menuList){
					LayuiTreeNode tree = new LayuiTreeNode();
					tree.setId(bean.getId());
					tree.setName(bean.getName());
					tree.setAlias(bean.getRemark());
					tree.setUrl(bean.getUrl());
					tree.setHref(bean.getUrl());
					tree.setPid(bean.getPid() == null ? Constants.TREE_ROOT_ID : bean.getPid());
					tree.setSpread(true);
					tree.setNextLevel(nextLevel);
					if(bean.getChildrenList() != null && !bean.getChildrenList().isEmpty()){//是否有孩子节点
						tree.setIsParent(true);
					}
					menuNodeList.add(tree);
					/*MenuZTreeNode menuNode = makeMenuTreeNode(bean, StringUtils.isEmpty(parentId));////首级节点，自动展开
					menuNode.setOpenLevel(2);//自动展开前面2级
					menuNodeList.add(menuNode);*/
				}
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
		BeanUtils.copyProperties(bean, node.getMenu());//自动拷贝，菜单树固有属性
		node.getMenu().setId(bean.getId().intValue());
		node.setId(String.valueOf(bean.getId()));
		node.setpId(String.valueOf(bean.getParentMenuBo().getId()));
		node.setName(bean.getName());
		node.setOpen(open);
		if(bean.getChildrenList() != null && bean.getChildrenList().isEmpty()){//是否有孩子节点
			node.setIsParent(true);
		}
		return node;
	}
}
