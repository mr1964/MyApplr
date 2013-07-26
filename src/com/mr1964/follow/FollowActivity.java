package com.mr1964.follow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.mr1964.myapplr.R;

public class FollowActivity extends Activity implements OnScrollListener ,OnTouchListener, OnGestureListener {

	private GestureDetector mGestureDetector; 
	private ListView listView;
	private List<Map<String,String>> list = new ArrayList<Map<String,String>>();
	private int k = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_follow);
		
        mGestureDetector = new GestureDetector((OnGestureListener) this);    
        LinearLayout viewSnsLayout = (LinearLayout)findViewById(R.id.ll_follow);    
        viewSnsLayout.setOnTouchListener(this);    
        viewSnsLayout.setLongClickable(true);  
        
        ImageView back_follow = (ImageView) findViewById(R.id.back_follow);
        back_follow.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		
		try {
			getData();
		} catch (Exception e) {	}
		
		initUsreList();

	}
	
	public void getData() throws Exception{
		UserAsync userAsync = new UserAsync(k);
		userAsync.execute();
		JSONArray json =  new JSONArray(userAsync.get());
		for(int i = 0; i < json.length(); i++){
			Map<String,String> map = new HashMap<String, String>();
			JSONObject obj = json.getJSONObject(i);
			map.put("user_id", obj.getString("user_id"));
			map.put("user_name", obj.getString("user_name"));
			list.add(map);
		}
		System.out.println("list = " + list);
	}

	public void initUsreList(){
		listView = (ListView) findViewById(R.id.lv_follow);
		
        SimpleAdapter adapter = new SimpleAdapter(this,list,R.layout.single_user,
                new String[]{"user_name"},
                new int[]{R.id.tv_single_user});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String user_id = list.get(arg2).get("user_id").toString();
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), ToFollowActivity.class);
				intent.putExtra("user_id", user_id);
				startActivity(intent);
				
			}});


	}
	

	
	public class UserAsync extends AsyncTask<Void,Void,String>{
		private int i;
		private String users;
		public UserAsync(int i){
			this.i = i;
		}
		@Override
		public String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			FollowGetData data = new FollowGetData();
			
			try {
				users = data.FollowGetAction(i);
			} catch (Exception e) {}
			
			return users;
		}
		@Override
		public void onPostExecute(String users){
			
		}
		
	}
	
	
	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}


	private int verticalMinDistance = 150;  
	private int minVelocity         = 0;  
	
	@Override  	
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {  
	  
		if (e1.getX() - e2.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {  
	  
			// 切换Activity  
			// Intent intent = new Intent(ViewSnsActivity.this, UpdateStatusActivity.class);  
			// startActivity(intent);  
			//Toast.makeText(this, "向左手势", Toast.LENGTH_SHORT).show();  
		} else if (e2.getX() - e1.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {  
	  
			// 切换Activity  
			// Intent intent = new Intent(ViewSnsActivity.this, UpdateStatusActivity.class);  
			// startActivity(intent);  
			//Toast.makeText(this, "向右手势", Toast.LENGTH_SHORT).show();  
			finish();
		}  
	  
		return false;  
	}


	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override  
	public boolean onTouch(View v, MotionEvent event) {  
    return mGestureDetector.onTouchEvent(event);  
	}  
	
	//当布局文件不是LinearLayout时，即可能是RelativeLayout
	//或LinearLayout中包含可上下滑动的控件(ListView,ScrollView)时需要添加下面的复写代码
	@Override  
   public boolean dispatchTouchEvent(MotionEvent ev) {  
	   mGestureDetector.onTouchEvent(ev);  
	   // scroll.onTouchEvent(ev);  
	   return super.dispatchTouchEvent(ev);  
   }
	


}
