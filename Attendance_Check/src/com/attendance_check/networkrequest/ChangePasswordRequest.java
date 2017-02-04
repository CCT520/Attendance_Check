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

import com.attendance_check.controller.ChangePassword;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.SimpleAdapter;

public class ChangePasswordRequest extends AsyncTask<String, String, String>{

	private String account;
	private String initialpassword;
	private String newpassword;
	private Activity ChangePassword;
	
	public ChangePasswordRequest(String account, String initialpassword, 
			String newpassword, Activity ChangePassword){
		this.account = account;
		this.initialpassword = initialpassword;
		this.newpassword = newpassword;
		this.ChangePassword = ChangePassword;
	}
	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
				arg0[0] = arg0[0]+"?userId="+account+"&oldPwd="+initialpassword+"&newPwd="+newpassword;
		        try {   
		        	System.out.println("userId"+account+"oldPwd"+initialpassword+"newPwd"+newpassword);
		        	HttpGet request = new HttpGet(arg0[0]);     
				    HttpResponse httpResponse=new DefaultHttpClient().execute(request);  
	            if (httpResponse.getStatusLine().getStatusCode() == 200) {
	                //第五步：从相应对象当中取出数据，放到entity当中
	            	HttpEntity httpentity = httpResponse.getEntity();

	                String response = EntityUtils.toString(httpentity, "utf-8");//将entity当中的数据转换为字符串
	                //创建一个JSON对象
	                //JSONObject responseObjec = new JSONObject(response.toString());
	                System.out.println("显示返回值："+response.toString());
	                try {
						JSONObject jo = new JSONObject(response.toString());
						if(jo.getString("code").equals("200")){
							publishProgress("Update_UI: Change Password Successfully");
						}else{
							publishProgress("Update_UI: Old Password Wrong");
						}
	                }catch(JSONException e) {
	                	Log.e("TAG", e.getMessage());  
	                }
	            }else{
	            	//password wrong
	            }
		        }catch (Exception e) {  
		            Log.e("TAG", e.getMessage());  
		        }  
				
		return null;
	}

	protected void onProgressUpdate(String... progresses) {  
        /*
         * *Begin to draw chart and realize listview 
         */
    	if(progresses[0].equals("Update_UI: Change Password Successfully"))
    	{
    		AlertDialog.Builder builder = new AlertDialog.Builder(
					ChangePassword).setTitle("提示")
					.setMessage("更改密码成功！");
			builder.setPositiveButton("确定",
					new Dialog.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0,
								int arg1) {
							// TODO Auto-generated method stub
							ChangePassword.finish();
						}

					});
			builder.show();		
    	}
    	if(progresses[0].equals("Update_UI: Old Password Wrong")){
    		AlertDialog.Builder builder = new AlertDialog.Builder(
					ChangePassword).setTitle("提示")
					.setMessage("原密码错误！");
			builder.setPositiveButton("确定",
					new Dialog.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0,
								int arg1) {
							// TODO Auto-generated method stub
							
						}

					});
			builder.show();
    	}
    }  
}
