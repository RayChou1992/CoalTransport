package com.zhiren.wuljg.entities;

public class User {
	public UserInfo userinfo;
	
	public static class UserInfo{
		public String id;	//用户ID
		public String name;	//用户名
	//	public String headerimg;	//用户头像
		public String session;	//Token 
	}
}
