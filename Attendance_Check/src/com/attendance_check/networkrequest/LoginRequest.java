package com.attendance_check.networkrequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.attendance_check.controller.DemoApplication;
import com.attendance_check.controller.HomePage;
import com.attendance_check.controller.Login;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.SimpleAdapter;

public class LoginRequest extends AsyncTask<String, String, String>{

	private String userid;
	private String password;
	private Activity Login;
	private String name;
	private String userId;
	private String identity;
	private DemoApplication DemoApp;
	
	public LoginRequest(String userid, String password, Activity Login, DemoApplication DemoApp){
		this.userid = userid;
		this.password = password;
		this.Login = Login;
		this.DemoApp = DemoApp;
	}
	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		HttpClient httpClient = new DefaultHttpClient();
        List<NameValuePair> requestParams = new ArrayList<NameValuePair>();
        System.out.println("显示一下参数！！！： "+userid+" "+password+" ");
        requestParams.add(new BasicNameValuePair("userId", userid));
        requestParams.add(new BasicNameValuePair("password", password));
        
        try {   
        	arg0[0] = arg0[0]+"?userId="+userid+"&password="+password;
		    HttpGet request = new HttpGet(arg0[0]);     
		    HttpResponse httpResponse=new DefaultHttpClient().execute(request); 
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
        	HttpEntity httpentity = httpResponse.getEntity();
            String response = EntityUtils.toString(httpentity, "utf-8");//将entity当中的数据转换为字符串
            try {
				JSONObject jo = new JSONObject(response.toString());
				if(jo.getString("code").equals("200")){
					name = jo.getString("name");
					userId = jo.getString("userId");
					identity = jo.getString("identity");
					DemoApp.editor.putString("LogState", "in");
					DemoApp.editor.putString("Name", name);
					DemoApp.editor.putString("UserId", userId);
					DemoApp.editor.putString("Identity", identity);
					DemoApp.editor.commit();
					Intent intent = new Intent();
					Bundle bundle= new Bundle();
					bundle.putString("identity", identity);
					bundle.putString("userId", userId);
					intent.putExtras(bundle);
					intent.setClass(Login ,HomePage.class);
				    Login.startActivity(intent);
					Login.finish();
				}else{
					//password wrong
					publishProgress("Update_UI: Password Wrong！");
				}
				
            }catch(JSONException e) {
            	Log.e("TAG", e.getMessage());  
            }
        }else{
        	//time-out
        	publishProgress("Update_UI: Time-Out！");
		}
        
        }catch (Exception e) {  
            Log.e("TAG", e.getMessage());  
        }
		
		return null;
	}
	
	protected void onProgressUpdate(String... progresses) {  
		if(progresses[0].equals("Update_UI: Password Wrong！"))
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(
	                   Login).setTitle("提示")
					.setMessage("用户名或密码错误");
			builder.setPositiveButton("确定",
					new Dialog.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0,
								int arg1) {
							// TODO Auto-generated method stub
							
						}

					});
			builder.create().show();
		}
		if(progresses[0].equals("Update_UI: Time-Out！"))
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(
					Login).setTitle("提示")
					.setMessage("网络超时");
			builder.setPositiveButton("确定",
					new Dialog.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0,
								int arg1) {
							// TODO Auto-generated method stub
							
						}

					});
			builder.create().show();
		}
	}

}
