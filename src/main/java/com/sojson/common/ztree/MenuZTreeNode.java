package com.sojson.common.ztree;


public class MenuZTreeNode extends ZTreeNode implements Comparable<MenuZTreeNode>{
	
	public int compareTo(MenuZTreeNode o) {
		if(null == this.getOrderBy()){//当前orderby为null，认为当前比较大
			return 1;
		}else if(null == o.getOrderBy()){//被比较对象的orderby为null，认为被比较对象比较大
			return -1;
		}else{
			int m1 = this.getOrderBy().intValue();
			int m2 = o.getOrderBy().intValue();
			if(m1 > m2){
				return 1;
			}else if(m1 < m2){
				return -1;
			}else{//当orderby相等时，取id比较
				int v1 = this.getId().intValue();
				int v2 = o.getId().intValue();
				return (v1 > v2) ? 1 : ((v1 == v2 ? 0 : -1));
			}
		}
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return this.getId().hashCode();
	}
	
}
