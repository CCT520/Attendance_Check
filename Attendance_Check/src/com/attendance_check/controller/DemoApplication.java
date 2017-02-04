package com.attendance_check.controller;


import android.app.Application;
import android.content.SharedPreferences;



public class DemoApplication extends Application {
	    public final String Server_IP="10.206.17.174:8080";     
		public String content="";
		public String UserName;
		public String UserPawd;
		public String identity;
		public int logstate;
	
		public SharedPreferences preferences;
		public SharedPreferences.Editor editor;
		
	    public String getServer_IP(){   
	        return Server_IP;   
	    }      
	    
	    
	    public String getidentity(){
	    	return identity;
	    }
	    
	    public void setidentity(String s){
	    	this.identity=s;
	    }
	    
	    public String getcontent(){
	    	return content;
	    }
	    
	    public void setcontent(String c){
	    	this.content=c;
	    }
	    
	  
	    
	    public String getUserName(){
	    	return UserName;
	    }
	    
	    public void setUserName(String u){
	    	this.UserName=u;
	    }
	    
	    public String getUserPawd(){
	    	return UserPawd;
	    }
	    
	    public void setUserPawd(String u){
	    	this.UserPawd=u;
	    }
	    
	    public SharedPreferences getpreferences()
		{
			return preferences;
		}
		
		public void setpreferences(SharedPreferences preferences)
		{
			this.preferences=preferences;
		}
		
		public SharedPreferences.Editor geteditor()
		{
			return editor;
		}
		
		public void seteditor(SharedPreferences.Editor editor)
		{
			this.editor=editor;
		}
		
	
	@Override
	public void onCreate() {
		super.onCreate();
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		
	}

}