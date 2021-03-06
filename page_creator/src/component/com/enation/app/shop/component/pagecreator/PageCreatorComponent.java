/**
 * 
 */
package com.enation.app.shop.component.pagecreator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.service.auth.IAuthActionManager;
import com.enation.app.base.core.service.auth.impl.PermissionConfig;
import com.enation.eop.SystemSetting;
import com.enation.eop.resource.IMenuManager;
import com.enation.eop.resource.model.Menu;
import com.enation.framework.component.IComponent;
import com.enation.framework.context.webcontext.ThreadContextHolder;

/**
 * @author kingapex
 *2015-3-25
 */
@Component
public class PageCreatorComponent implements IComponent {
	
	@Autowired
	private IMenuManager menuManager;
	
	@Autowired
	private IAuthActionManager authActionManager;
	/* (non-Javadoc)
	 * @see com.enation.framework.component.IComponent#install()
	 */
	@Override
	public void install() {
		this.addMenu();
	}

	/* (non-Javadoc)
	 * @see com.enation.framework.component.IComponent#unInstall()
	 */
	@Override
	public void unInstall() {
		this.deleteMenu();
	}
	
	
	private static final String parentMenuName="网店设置";
	
	
	/**
	 * 为组件添加菜单
	 */
	private void addMenu(){
		int superAdminAuthId = PermissionConfig.getAuthId("super_admin"); // 超级管理员权限id

		Menu parentMenu = menuManager.get(parentMenuName);
		Menu menu = new Menu();
		menu.setTitle("生成静态页");
		menu.setPid(parentMenu.getId());
		menu.setUrl("/shop/admin/page-create/input.do");
		menu.setSorder(205);
		menu.setMenutype(Menu.MENU_TYPE_APP);
		this.menuManager.add(menu);
		//清空菜单列表
		ThreadContextHolder.getSession().removeAttribute(SystemSetting.menuListKey.toString());
		
	}
	
	
	/**
	 * 删除组件相应的菜单
	 */
	private void deleteMenu(){
		int superAdminAuthId = PermissionConfig.getAuthId("super_admin"); // 超级管理员权限id
		Menu menu =  menuManager.get("生成静态页");
		if(menu!=null){
			int addmenuid =menu.getId();
			this.authActionManager.deleteMenu(superAdminAuthId, new Integer[] {addmenuid });
			this.menuManager.delete("生成静态页");
			ThreadContextHolder.getSession().removeAttribute(SystemSetting.menuListKey.toString());
		}
	}

	

}
