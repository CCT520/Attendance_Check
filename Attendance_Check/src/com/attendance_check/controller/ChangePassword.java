package com.attendance_check.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.attendance_check.networkrequest.ChangePasswordRequest;
import com.attendence_check.controller.R;



import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ChangePassword extends Activity {

	private EditText initialpawdedit;
	private EditText newpawdedit;
	private EditText reinputpawdedit;
	private Button submitbt;
	private String url = "http://192.168.1.144:8080/FingerprintAtt/user/modify";
	
	private String userId="0";
	private String initialpassword;
	private String newpassword;
	private String newpassword1;
	private ChangePasswordRequest changepassword;
	private DemoApplication DemoApp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DemoApp = (DemoApplication) getApplication(); 
		DemoApp.preferences  = getSharedPreferences("crazyit",MODE_WORLD_READABLE);
		DemoApp.editor = DemoApp.preferences.edit();
		userId=DemoApp.preferences.getString("UserId", "0");
		setContentView(R.layout.activity_changepassword);
		initial();
		operate();
	}
	
	public void initial(){
		initialpawdedit = (EditText)findViewById(R.id.initialpsd);
		newpawdedit = (EditText)findViewById(R.id.newpsd);
		reinputpawdedit = (EditText)findViewById(R.id.repsd);
		submitbt = (Button)findViewById(R.id.submitbt);
	}

	public void operate(){
		submitbt.setOnClickListener(new OnClickListener(){
     
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				initialpassword = initialpawdedit.getText().toString();
				newpassword = newpawdedit.getText().toString();
				newpassword1 = reinputpawdedit.getText().toString();
				System.out.println("显示一下参数！！！： "+initialpassword+" "+newpassword+" "+newpassword1);
				if(newpassword.equals(newpassword1)){
					changepassword = new ChangePasswordRequest(userId,initialpassword,newpassword,ChangePassword.this);
					changepassword.execute(url);
				}else{
					AlertDialog.Builder builder = new AlertDialog.Builder(
							ChangePassword.this).setTitle("提示")
							.setMessage("两次密码不一致！");
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
			
		});
	}

}
