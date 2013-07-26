package com.mr1964.slidingmenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mr1964.comments.CommentActivity;
import com.mr1964.follow.FollowActivity;
import com.mr1964.home.HomeActivity;
import com.mr1964.login.LoginActivity;
import com.mr1964.messages.SendActivity;
import com.mr1964.myapplr.R;
import com.mr1964.mymessages.MyActivity;
import com.mr1964.paging.PagingActivity;
import com.mr1964.query.QueryActivity;
import com.slidingmenu.lib.SlidingMenu;

public class SlidingActivity extends Activity implements OnScrollListener {
	

	
	//ListView
	private JSONArray json = null;
	private ListView mListView;	
	private MyAdapter adapter;
	/*�������д�ͻᱨ��ָ��
	 * private ArrayList<Map<String ,Object>> list��
	 * Ϊʲô�أ�
	 *��������д�򲻻ᱨ��ָ��*/		
	private ArrayList<Map<String ,Object>> list = new ArrayList<Map<String,Object>>();
	private ArrayList<Map<String, Object>> mItemList = new ArrayList<Map<String,Object>>();	
	private int k = 0;
	private boolean isLoading = false;
	private String user_id;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        getData(list);	
        System.out.println(list);
		initListView();

        
    }
    
/*    public void setContentView(){
    	setTitle("cuc");
        // set the content view
        setContentView(R.layout.activity_test);
        // configure the SlidingMenu
        SlidingMenu menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.activity_main);
    }*/
    
  //����ListView��ʼ
  	public void initListView(){
  		
  		mListView = (ListView) findViewById(R.id.myListView);		
  		adapter = new MyAdapter(this,list);
  		mListView.setAdapter(adapter);
  		mListView.setOnScrollListener(this);	
  		mListView.setOnItemClickListener(new OnItemClickListener(){
  			 @Override  
  			 public void onItemClick
  			 (AdapterView<?> arg0, View view, int position, long id) {   
  				 
  /*				 Toast.makeText(getApplication(), 
  						 "��  " + position + " ��", Toast.LENGTH_SHORT).show();	*/ 
  			
  				String message_id = mItemList.get(position).get("message_id").toString();
  				
  				Intent intent = new Intent();
  				intent.setClass(getApplicationContext(), CommentActivity.class);
  				intent.putExtra("message_id", message_id);
  				startActivity(intent);			 				 
  			    }
  		});
  	}
  	
  	//�ӷ�������������
  	public void getData(ArrayList<Map<String,Object>>list){

  		TestAsync viewerAsync = new TestAsync(k);
  		k = k+10;
  		viewerAsync.execute();
  		try {
  			String jsonstring = viewerAsync.get();
  			json = new JSONArray(jsonstring);
  			for(int i = 0; i < json.length(); i++){
  				Map<String,Object> map = new HashMap<String,Object>();
  				JSONObject obj = json.getJSONObject(i);
  				
  				map.put("user_name", obj.getString("user_name"));
  				map.put("message_content", obj.getString("message_content"));
  				map.put("message_date", obj.getString("message_date"));
  				map.put("message_id",obj.getString("message_id"));
  				map.put("comment_count",obj.getString("comment_count"));
  				list.add(map);				
  			}

  			} catch (Exception e) {}						
  	}		
  	
  	public void onScroll(AbsListView view, int mFirstVisibleItem,
              int mVisibleItemCount, int mTotalItemCount) {
        
  		/*mFirstVisibleItem + mVisibleItemCount == mTotalItemCountΪtrueʱ
  		 * ��ʾ���������һ����Լ��ظ���������* */
  	    if(mFirstVisibleItem + mVisibleItemCount == mTotalItemCount&& !isLoading ){
  	    	getData(list);
  	        adapter.notifyDataSetChanged();
  	    }      
      }
  	
      public void onScrollStateChanged(AbsListView view, int mScrollState) {     
      }
  	
  	private class MyAdapter extends BaseAdapter{
  		
          public MyAdapter(Context context, List<Map<String, Object>> data) {
              mItemList = (ArrayList<Map<String, Object>>) data;          
          }
          
          public int getCount() {
          	return mItemList.size();           
          }

          public Object getItem(int pos) {
              return pos;
          }

          public long getItemId(int pos) {
              return pos;
          }
          
          @Override
  		public View getView(int position, View convertView, ViewGroup parent) {

  			ViewHolder holder;        
              if (convertView == null) {
                  convertView = LayoutInflater.from
                  		(SlidingActivity.this).inflate(R.layout.activity_home_message, null);
                  
                  holder = new ViewHolder();                  
                  holder.user_name = (TextView) 
                  		convertView.findViewById(R.id.home_user_name);
                  
                  holder.message_content = (TextView)                		
                  		convertView.findViewById(R.id.home_message_content);
                  
                  holder.message_date = (TextView)                		
                  		convertView.findViewById(R.id.home_message_date);
                  
                  holder.comment_count = (TextView)                		
                  		convertView.findViewById(R.id.home_message_count);
                  
                  convertView.setTag(holder);//��ViewHolder����                    
              }
              else{
                  holder = (ViewHolder)convertView.getTag();//ȡ��ViewHolder����                   
              }
              /**����TextView��ʾ�����ݣ������Ǵ���ڶ�̬�����е�����*/ 
              if(position>=0 && position<mItemList.size()){
              System.out.println("position= " + position);
              
              holder.user_name.setText(mItemList.get(position).get
              		("user_name").toString());
              
              holder.message_content.setText(mItemList.get(position).get
              		("message_content").toString());
              
              holder.message_date.setText(mItemList.get(position).get
              		("message_date").toString());
              
              holder.comment_count.setText(mItemList.get(position).get
              		("comment_count").toString());
              }          
              return convertView;			
  		}
  	}
  	
  	public final class ViewHolder{
  	    public TextView user_name;
  	    public TextView message_content;
  	    public TextView   message_date;
  	    public TextView comment_count;
      }
  	
  	public class TestAsync extends AsyncTask<Void,Void,String>{

  		int i ;	
  		public TestAsync(int i){
  			this.i = i;
  		}				
  		
  		@Override
  		public String doInBackground(Void...voids){
  			
  			HomeData data = new HomeData();
  			String result = data.DataAction(i,user_id);			
  			isLoading = true;
  			return result;			
  		}
  		
  		@Override
  		public void onPostExecute(String result){
  			
  			//��������ȡ��ʱ�����������ص�ֵ��ʵ�ǣ�[],resultҲ�͵���[]����������=2
  			if(result.length() == 2){
  				isLoading = true;
  				
  				if(result == "[]"){
  					Toast.makeText(getApplicationContext(),
  							"�Ѽ���ȫ������", Toast.LENGTH_SHORT).show();
  				}
  				if(result == "00"){
  					Toast.makeText(getApplicationContext(),
  							"�����������쳣", Toast.LENGTH_SHORT).show();
  				}
  				
  			} else{isLoading = false;}			
  		}		
  	}
  	//����ListView����
  	
/*  //����HomePage��ʼ
  	public void initHomePage(){
  		setContentView(R.layout.activity_main);
  		

  		
  		SharedPreferences sharedPreferences = getSharedPreferences("currentuser", 
  				Activity.MODE_PRIVATE);
  				
  		boolean isLogin = sharedPreferences.getBoolean("isLogin", false); 	
  		if(!isLogin){
  			Intent intent = new Intent(this, LoginActivity.class);;
  			startActivity(intent);
  		} else{
  			String currentusername = sharedPreferences.getString("user_name", "");

  		}
  	}
  	
  	protected void myExit() {  
          Intent intent = new Intent();  
          intent.setAction("ExitApp");  
          this.sendBroadcast(intent);  
          super.finish();  
      }
  	
  	private long exitTime = 0;  
      @Override  
      public boolean onKeyDown(int keyCode, KeyEvent event) {  
          if(keyCode == KeyEvent.KEYCODE_BACK){ 
              if((System.currentTimeMillis()-exitTime) > 2000){  
                  Toast.makeText(getApplicationContext(),
                  		"�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();  
                  exitTime = System.currentTimeMillis();  
              } else {  
                  //finish(); 
                  myExit();
              }  
              return true;  
          }  
          return super.onKeyDown(keyCode, event);  
      }
      
      public void test(View view){    	
      	Intent intent = new Intent(this,QueryActivity.class);
      	startActivity(intent);
      }
   
      public void sample(View view){   	
      	Intent intent = new Intent(this,PagingActivity.class);
      	startActivity(intent);
      }
      
      public void send(View view){
      	Intent intent = new Intent(this,SendActivity.class);
      	startActivity(intent);
      }
      
      public void fresh(View view){
      	ArrayList<Map<String, Object>> temp = new ArrayList<Map<String,Object>>();
      	list = temp;
      	k=0;
      	getData(list);
      	System.out.println("list = " + list);
      	initListView();
      }
      
  	public void login(View view){
  		Intent intent = new Intent(this, LoginActivity.class);
  		//
  		intent.putExtra("HomeActivity", false);
  		startActivity(intent);
  	}

  	public void more(View view){
  		Intent intent = new Intent(this, HomeActivity.class);
  		startActivity(intent);
  	}
  	
  	public void listview(View view){
  		Intent intent = new Intent(this, MyActivity.class);
  		startActivity(intent);
  	}	

  	public void userlist(View view){
  		Intent intent = new Intent(this, FollowActivity.class);
  		startActivity(intent);
  	}
  	
  	public void quit(View view){
  		SharedPreferences mySharedPreferences = getSharedPreferences("currentuser", 
  				Activity.MODE_PRIVATE); 
  		SharedPreferences.Editor editor = mySharedPreferences.edit(); 
  		//ע��ʹuserinfo = "0" => userinfo.length = 1�������ڵ�½״̬
  		editor.putBoolean("isLogin", false);
  		editor.commit(); 
  		finish();
  	}	
  	//����HomePage����
*/    



}
