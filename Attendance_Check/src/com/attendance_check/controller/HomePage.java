package com.attendance_check.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.attendance_check.networkrequest.LogoutRequest;
import com.attendance_check.networkrequest.MyTaskDayOffShow;
import com.attendance_check.networkrequest.StudentCheckRequest;
import com.attendance_check.networkrequest.TeacherCheckCourseRequest;
import com.attendence_check.controller.R;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class HomePage extends Activity {
    
	//private static Lock lock = new ReentrantLock();
	private String loginidentity;
	private String username;
	private String user_id;
	private String password;
	private String  result="";
	private ViewPager viewPager; 
    private ArrayList<View> pageViews; 
    private ViewGroup buttonsLine;  
    private Button button01; 
    private Button button02; 
    private Button button03; 
    private Button button04;
    private Button[] buttons;
	private int loadcount=0;
	private ListView checkrecordlist;
	private ListView dayofflist;
	private TextView newsType;
	private Button dayoffbt;
	HashMap<Integer, Double> map;
    private List<Map<String,Object>>listitems;
	private SimpleAdapter simpleadapter;
	private StudentCheckRequest studentcheck;
	private TeacherCheckCourseRequest teachercheckcourse;
	private LogoutRequest logout;
	private MyTaskDayOffShow mytaskdayoffshow;
	private String url = null;
	private DemoApplication DemoApp;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DemoApp = (DemoApplication) getApplication(); 
		DemoApp.preferences  = getSharedPreferences("crazyit",MODE_WORLD_READABLE);
		DemoApp.editor = DemoApp.preferences.edit();
		loginidentity=DemoApp.preferences.getString("Identity", "0");
		user_id=DemoApp.preferences.getString("UserId", "0");
        
		LayoutInflater inflater = getLayoutInflater(); 
        pageViews = new ArrayList<View>(); 
        //每页de界面
        View page01=inflater.inflate(R.layout.activity_check, null);
        View page02=inflater.inflate(R.layout.activity_day__off, null);
        View page03=inflater.inflate(R.layout.activity_setting, null);
       
        pageViews.add(page01);  //lee
        pageViews.add(page02);  //lee
        pageViews.add(page03);  //lee
        
       
        //按钮栏
        buttons = new Button[4]; 
        buttonsLine = (ViewGroup)inflater.inflate(R.layout.activity_home_page, null); 
        button01 = (Button) buttonsLine.findViewById(R.id.check);
        button02 = (Button) buttonsLine.findViewById(R.id.dayoff);
        button03 = (Button) buttonsLine.findViewById(R.id.set);

        buttons[0] = button01;
        buttons[1] = button02;
        buttons[2] = button03;

        
        button01.setOnClickListener(new GuideButtonClickListener(0));
        button02.setOnClickListener(new GuideButtonClickListener(1));
        button03.setOnClickListener(new GuideButtonClickListener(2));
        
       
        viewPager = (ViewPager)buttonsLine.findViewById(R.id.guidePages); 
        setContentView(buttonsLine);
 
        viewPager.setAdapter(new GuidePageAdapter()); 
        viewPager.setOnPageChangeListener(new GuidePageChangeListener()); 
    }

   
    class GuidePageAdapter extends PagerAdapter { 
      
        @Override 
        public int getCount() { 
            return pageViews.size(); 
        } 
 
        @Override 
        public boolean isViewFromObject(View arg0, Object arg1) { 
            return arg0 == arg1; 
        } 
 
        @Override 
        public int getItemPosition(Object object) { 
            // TODO Auto-generated method stub 
            return super.getItemPosition(object); 
        } 
 
        @Override 
        public void destroyItem(View arg0, int arg1, Object arg2) { 
            // TODO Auto-generated method stub 
            ((ViewPager) arg0).removeView(pageViews.get(arg1)); 
        } 
 
        @Override 
        public Object instantiateItem(View arg0, int page) { 
            // TODO Auto-generated method stub 
            ((ViewPager) arg0).addView(pageViews.get(page)); 
            if(page==0)checkpage();
            if(page==1)dayoffpage();
            if(page==2)setting();
            
            return pageViews.get(page); 
        } 
    	
        //显示新闻消息内容的函数
    	void checkpage(){
    		loginidentity="1";
    		newsType=(TextView)findViewById(R.id.attendancerecord);
    		checkrecordlist=(ListView)findViewById(R.id.checkrecordlist);
    		if(loginidentity.equals("0")){
    			studentcheck = new StudentCheckRequest(checkrecordlist, newsType, user_id,HomePage.this);
    			studentcheck.execute("http://192.168.1.144:8080/FingerprintAtt/student/course");
    			
    			checkrecordlist.setOnItemClickListener(new OnItemClickListener(){
 				@Override
 				public void onItemClick(AdapterView<?> arg0, View view,
 						int position, long arg3) {
 					// TODO Auto-generated method stub
 					//TextView tv = (TextView)view.findViewById(R.id.subject);
 					TextView courseidtext=(TextView)findViewById(R.id.ID);
 					String courseid = courseidtext.getText().toString();
 					System.out.println("courseid is "+courseid);
 					Intent intent = new Intent();
					Bundle bundle= new Bundle();
					bundle.putString("courseid", courseid);
					bundle.putString("userId", user_id);
					bundle.putString("identity", "Student");
					intent.putExtras(bundle);
					intent.setClass(HomePage.this, Details.class);
				    startActivity(intent);
 				}
     			
     		});
    		}else{
    			teachercheckcourse = new TeacherCheckCourseRequest(checkrecordlist, newsType, HomePage.this,user_id);
    			teachercheckcourse.execute("http://192.168.1.144:8080/FingerprintAtt/teacher/course");
       	     newsType.setText("全部记录");
     		checkrecordlist.setOnItemClickListener(new OnItemClickListener(){

 				@Override
 				public void onItemClick(AdapterView<?> arg0, View view,
 						int position, long arg3) {
 					// TODO Auto-generated method stub
 					TextView tv = (TextView)view.findViewById(R.id.ID);
 					String courseid = tv.getText().toString();
 					System.out.println("courseId is "+courseid);
 					Intent intent = new Intent();
					Bundle bundle= new Bundle();
					bundle.putString("courseid", courseid);
					bundle.putString("userId", user_id);
					intent.putExtras(bundle);
 					intent.setClass(HomePage.this, TeacherCheckStudent.class);
 					startActivity(intent);
 				}
     			
     		});
    		}
   	        
    		
    		
        }
        //显示关注人列表的函数
    	void dayoffpage(){
    		dayofflist=(ListView)findViewById(R.id.dayoffslist);
    		mytaskdayoffshow = new MyTaskDayOffShow(dayofflist, HomePage.this);
    		mytaskdayoffshow.execute(url);
    		dayoffbt = (Button)findViewById(R.id.applydayoff);
    		dayoffbt.setOnClickListener(new OnClickListener(){

    			@Override
    			public void onClick(View arg0) {
    				// TODO Auto-generated method stub
    				Intent intent = new Intent(HomePage.this, Vacating.class);
    				startActivity(intent);
    			}
    			
    		});
    	}
        
    	//显示个人主页的函数
    	void setting(){
        	ListView list=(ListView)findViewById(R.id.lvotherinfor);
    		list.setOnItemClickListener(new OnItemClickListener(){
    			@Override
    			public void onItemClick(AdapterView<?> parent, View view, int position,
    					long id) {
    				// TODO Auto-generated method stub
    				if(position==0){
    					Intent intent0=new Intent(HomePage.this,ChangePassword.class);
    			        startActivity(intent0);
    				}
    				if(position==1){
    					logout = new LogoutRequest(user_id, HomePage.this);
    					logout.execute("http://192.168.1.144:8080/FingerprintAtt/user/logout");
    					DemoApp.editor.putString("LogState", "out");
    					DemoApp.editor.commit();
    				}
    				/*if(position==2){
    					Intent intent1=new Intent(HomePage.this,DonationRecords.class);
    		            startActivity(intent1);
    		            }
    				if(position==3){
    					Intent intent2=new Intent(HomePage.this,RescueTips.class);
    		            startActivity(intent2);
    				}
    				if(position==4){
    					Intent intent3=new Intent(HomePage.this,Setup.class);
    		            startActivity(intent3);
    				}
    				if(position==5){
    					Intent intent=new Intent(HomePage.this,Firstpage.class);
    				    startActivity(intent);finish();
    					
    					
    				}*/
    			}
    		});
        }
       
        @Override 
        public void restoreState(Parcelable arg0, ClassLoader arg1) { 
            // TODO Auto-generated method stub 
 
        } 
 
        @Override 
        public Parcelable saveState() { 
            // TODO Auto-generated method stub 
            return null; 
        } 
 
        @Override 
        public void startUpdate(View arg0) { 
            // TODO Auto-generated method stub 
 
        } 
 
        @Override 
        public void finishUpdate(View arg0) { 
            // TODO Auto-generated method stub 
 
        } 
    }
   
   
    class GuidePageChangeListener implements OnPageChangeListener { 
 
        @Override 
        public void onPageScrollStateChanged(int arg0) { 
            // TODO Auto-generated method stub 
        } 
 
        @Override 
        public void onPageScrolled(int arg0, float arg1, int arg2) { 
            // TODO Auto-generated method stub 
        } 
 
        @Override 
        public void onPageSelected(int arg0) { 
            
        } 
    }
   
   
    class GuideButtonClickListener implements OnClickListener { 
      private int index = 0;
     
         public GuideButtonClickListener(int i) {
             index = i;
         }

         @Override
         public void onClick(View v) {
          viewPager.setCurrentItem(index, true);
         }
    }
  
 }
 
