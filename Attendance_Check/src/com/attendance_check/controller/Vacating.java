package com.attendance_check.controller;

import java.util.ArrayList;
import java.util.Calendar;
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

import com.attendence_check.controller.R;


import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class Vacating extends Activity {

	private TextView showdatetext;
	private TextView textdate;
	private EditText editreason;
	private Button submitbt;
	final Calendar c = Calendar.getInstance();
	String url;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vacating);
		initial();
		action();
	    
	}

    public void	initial(){
    	showdatetext = (TextView)findViewById(R.id.textdate);
		textdate = (TextView)findViewById(R.id.date);
		editreason = (EditText)findViewById(R.id.editreason);
		submitbt = (Button)findViewById(R.id.submit);
    }
    
    public void action(){
    	textdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(Vacating.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year, monthOfYear, dayOfMonth);
                        showdatetext.setText(DateFormat.format("yyy-MM-dd", c));
                    }

					
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });
	 
	 submitbt.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			new Thread(){
				public void run(){
					String date = textdate.getText().toString();
					String reason = editreason.getText().toString();
					HttpClient httpClient = new DefaultHttpClient();
				    List<NameValuePair> requestParams = new ArrayList<NameValuePair>();
			        requestParams.add(new BasicNameValuePair("date", date));
			        requestParams.add(new BasicNameValuePair("reason", reason));
			        try {   
				    	        //第二步：创建代表请求的对象,参数是访问的服务器地址
				    		    HttpPost request = new HttpPost(url);     
				    			HttpResponse httpResponse; 
				    		    HttpEntity entity = new UrlEncodedFormEntity(requestParams, "UTF-8");     
				    		    request.setEntity(entity);  
				    	        httpResponse = httpClient.execute(request);
				            if (httpResponse.getStatusLine().getStatusCode() == 200) {
				                //第五步：从相应对象当中取出数据，放到entity当中
				            	HttpEntity httpentity = httpResponse.getEntity();

				                String response = EntityUtils.toString(httpentity, "utf-8");
				            }
				    		}catch(Exception e){
								e.printStackTrace();
							}
								//}
								super.run();
				}
				
			}.start();
		}
		 
	 });
    }
}
