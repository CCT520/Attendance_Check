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

public class LogoutRequest extends AsyncTask<String, String, String>{

	private String userid;
	private String password;
	private Activity HomePage;
	private String name;
	private String userId;
	private String identity;
	
	public LogoutRequest(String userid,  Activity HomePage){
		this.userid = userid;
		this.HomePage = HomePage;
	}
	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		HttpClient httpClient = new DefaultHttpClient();
        List<NameValuePair> requestParams = new ArrayList<NameValuePair>();
        System.out.println("显示一下参数！！！： "+userid+" "+password+" ");
        requestParams.add(new BasicNameValuePair("userId", userid));
        
        try {   
        	arg0[0] = arg0[0]+"?userId="+userid;
		    HttpGet request = new HttpGet(arg0[0]);     
		    HttpResponse httpResponse=new DefaultHttpClient().execute(request); 
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
        	HttpEntity httpentity = httpResponse.getEntity();
            String response = EntityUtils.toString(httpentity, "utf-8");
            try {
				JSONObject jo = new JSONObject(response.toString());
				if(jo.getString("code").equals("200")){
					Intent intent = new Intent();
					Bundle bundle= new Bundle();
					bundle.putString("identity", identity);
					bundle.putString("userId", userId);
					intent.putExtras(bundle);
					intent.setClass(HomePage ,Login.class);
					HomePage.startActivity(intent);
					HomePage.finish();
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
		
	}

}
