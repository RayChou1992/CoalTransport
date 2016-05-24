package com.zhiren.wuljg.entities;

import java.util.List;

public class MenuList {

	public  Childmenu childmenu ;
	
	public static class Childmenu {

		public String addressname; // 场地名称
		public String userphoto;
		public String username;
		public List<String> menulist;
	}

}
