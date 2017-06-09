package com.sojson.common.ztree;


public class MenuZTreeNode extends ZTreeNode implements Comparable<MenuZTreeNode>{
	private MenuZTreeNode.MenuInfo menu = new MenuZTreeNode.MenuInfo();//扩展的菜单信息
	
	public MenuZTreeNode.MenuInfo getMenu() {
		return menu;
	}
	
	public int compareTo(MenuZTreeNode o) {
		if(null == this.getMenu().getOrderBy()){//当前orderby为null，认为当前比较大
			return 1;
		}else if(null == o.getMenu().getOrderBy()){//被比较对象的orderby为null，认为被比较对象比较大
			return -1;
		}else{
			int m1 = this.getMenu().getOrderBy().intValue();
			int m2 = o.getMenu().getOrderBy().intValue();
			if(m1 > m2){
				return 1;
			}else if(m1 < m2){
				return -1;
			}else{//当orderby相等时，取id比较
				int v1 = this.getMenu().getId().intValue();
				int v2 = o.getMenu().getId().intValue();
				return (v1 > v2) ? 1 : ((v1 == v2 ? 0 : -1));
			}
		}
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return this.getMenu().getId().hashCode();
	}

	/**
	 * 内部类，菜单展示用到的属性；区别于menubizbean重新创建，保证传输的数据更少
	 * @author yuzhou
	 *
	 */
	public class MenuInfo{
		private Integer id;//菜单ID
		private String name;//菜单编码
		private String title;//菜单名称
		private String description;//菜单描述
		private String url;//菜单访问url
		private String target;//菜单访问展示页面
		private String type;//菜单类型
		private Long orderBy;//菜单排序 
		private String icon;//图标
		
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getTarget() {
			return target;
		}
		public void setTarget(String target) {
			this.target = target;
		}
		public Long getOrderBy() {
			return orderBy;
		}
		public void setOrderBy(Long orderBy) {
			this.orderBy = orderBy;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getIcon() {
			return icon;
		}
		public void setIcon(String icon) {
			this.icon = icon;
		}
	}
}
