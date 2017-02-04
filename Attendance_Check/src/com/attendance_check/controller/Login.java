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

import com.attendance_check.networkrequest.LoginRequest;
import com.attendence_check.controller.R;


import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends Activity {

	private EditText userid_text;
	private EditText password_text;
	private Button loginbt;
	private String userId;
	private String password;
	private String name;
	private String identity;
	private String userid;
	private String url = "http://192.168.1.144:8080/FingerprintAtt/user/login";
	private LoginRequest login_request;
	private DemoApplication DemoApp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DemoApp = (DemoApplication) getApplication(); 
		DemoApp.preferences  = getSharedPreferences("crazyit",MODE_WORLD_READABLE);
		DemoApp.editor = DemoApp.preferences.edit();
		setContentView(R.layout.activity_login);
		initial();
		operate();
	}

	public void initial(){
		userid_text = (EditText)findViewById(R.id.username);
		password_text = (EditText)findViewById(R.id.password);
		loginbt = (Button)findViewById(R.id.login);
	}
	
	public void operate(){
		loginbt.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Intent  intent = new Intent(Login.this, HomePage.class);
//				startActivity(intent);
				userid = userid_text.getText().toString();
				password = password_text.getText().toString();
				login_request = new LoginRequest(userid, password, Login.this, DemoApp);
				login_request.execute(url);
			}   
		});
		
	}
}
