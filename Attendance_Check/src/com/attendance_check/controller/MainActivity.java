package com.attendance_check.controller;



import java.util.Timer;
import java.util.TimerTask;

import com.attendence_check.controller.R;



import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private DemoApplication DemoApp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		DemoApp = (DemoApplication) getApplication(); 
		DemoApp.preferences  = getSharedPreferences("crazyit",MODE_WORLD_READABLE);
		DemoApp.editor = DemoApp.preferences.edit();
		if(DemoApp.preferences.getString("LogState", "out").equals("out")){
			Timer timer = new Timer();
	        timer.schedule(new TimerTask() {                  
	            @Override
	            public void run() {
	                Intent intent = new Intent(MainActivity.this, Login.class);
	                startActivity(intent);
	                finish();
	            }
	        }, 1000);  
		}else{
			Intent intent = new Intent(MainActivity.this, HomePage.class);
            startActivity(intent);
            finish();
		}
		
	}



}
