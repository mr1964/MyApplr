
package com.mr1964.home;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.laohuai.appdemo.customui.ui.MyListView;
import com.laohuai.appdemo.customui.ui.MyListView.OnMoreListener;
import com.laohuai.appdemo.customui.ui.MyListView.OnRefreshListener;
import com.laohuai.appdemo.customui.ui.PaddingListView;
import com.mr1964.comments.CommentActivity;
import com.mr1964.follow.FollowActivity;
import com.mr1964.login.LoginActivity;
import com.mr1964.messages.SendActivity;
import com.mr1964.myapplr.R;
import com.mr1964.mymessages.MyActivity;
import com.mr1964.query.QueryActivity;
import com.mr1964.test.TestActivity;
import com.mr1964.utils.BaseActivity;
import com.slidingmenu.lib.SlidingMenu;

public class HomeActivity extends BaseActivity implements OnClickListener{
	
	//ListView
	private JSONArray json = null;
	private MyListView mListView;	
	private MyAdapter adapter;
	/*�������д�ͻᱨ��ָ��
	 * private ArrayList<Map<String ,Object>> list��
	 * Ϊʲô�أ�
	 *��������д�򲻻ᱨ��ָ��*/		
	private ArrayList<Map<String ,Object>> list = new ArrayList<Map<String,Object>>();
	private ArrayList<Map<String, Object>> mItemList = new ArrayList<Map<String,Object>>();	
	private int k = 0;
	//private boolean isLoading = false;
	private String user_id;
	private ImageView menu_button;
	private SlidingMenu menu;
	
	//HomePage
	private TextView currentuser;
	
	
	//2013-06-07
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	private boolean firstLoading = true;
	private boolean freshError = false;
	private AlertDialog ad ;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView();
		
		sharedPreferences = getSharedPreferences("currentuser", 
				Activity.MODE_PRIVATE);
		editor = sharedPreferences.edit();
				
		user_id = sharedPreferences.getString("user_id", "");
		

        
        
        
        //��װһ��ʱ��û�������ݣ��жϼ��ػ����ˢ��
        String cache = sharedPreferences.getString("list_cache", "");  
        if(cache.isEmpty()){
    		getData(list,k);        	
        } else{
        	getListFromString(list,cache);   
        }
          
        //
	
		initListView();
		//��֪��Ϊʲô��ֻ�м����������ܼ��ػ���󣬼������ظ���
		mListView.onMoreComplete();	
		initHomePage();
		initMenu();
	}
	
	
    public void setContentView(){
	setTitle("ONE");
    // set the content view
    setContentView(R.layout.activity_test);
    // configure the SlidingMenu
    menu = new SlidingMenu(this);
    menu.setMode(SlidingMenu.LEFT);
    menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
    menu.setShadowWidthRes(R.dimen.shadow_width);
    menu.setShadowDrawable(R.drawable.shadow);
    menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
    menu.setFadeDegree(0.35f);
    menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
    menu.setMenu(R.layout.activity_main);
    
    //������ ����
    menu_button = (ImageView) findViewById(R.id.title_menu_button);
    menu_button.setOnClickListener(this);
    
    }
    
    protected void saveToTxt(String str ){
    	if(str == null){}//if str = null��do nothing
        try {
        	String path = Environment.getExternalStorageDirectory() 
        			+ "/123/backup.txt";
            FileWriter fw = new FileWriter(path);
            fw.flush();
            fw.write(str);
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	
	
	//����ListView��ʼ
	public void initListView(){
		
		mListView = (MyListView) findViewById(R.id.myListView);		
		adapter = new MyAdapter(this,list);
		mListView.setAdapter(adapter);
//		mListView.setOnScrollListener(this);
		
		mListView.setonMoreListener(new OnMoreListener(){

			@Override
			public void onMore() {
				Toast.makeText(getApplicationContext(),
						"������...^_^", Toast.LENGTH_SHORT).show();
				if(firstLoading){
					k = k + 10;
					firstLoading = false;
				}
				getData(list,k);
	            adapter.notifyDataSetChanged();
			}});
		
		mListView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				ArrayList<Map<String, Object>> temp = new ArrayList<Map<String,Object>>();
		    	list = temp;
		    	k=0;
		    	getData(list,k);
		    	System.out.println("list fresh = " + list);
		    	if(!freshError){
		    		initListView();
		    		//adapter.notifyDataSetChanged();
		    	}
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener(){
			 @Override  
			 public void onItemClick
			 (AdapterView<?> arg0, View view, int position, long id) {   
						
				String message_id = mItemList.get(position-1).get("message_id").toString();				
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), CommentActivity.class);
				intent.putExtra("message_id", message_id);
				startActivity(intent);	

			    }
		});
		
		//����
		Animation right_in = (Animation)AnimationUtils.loadAnimation(this, R.anim.right_in);
		LayoutAnimationController lac = new LayoutAnimationController(right_in);
		lac.setDelay(0.1f);
		lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
		mListView.setLayoutAnimation(lac);
	}
	
	//�ӷ�������������
	public void getData(ArrayList<Map<String,Object>>list, int index){
		
		
		index = k;
		TestAsync viewerAsync = new TestAsync(index,list);
		viewerAsync.execute();
		k = k+10;


	}
	
	public void getListFromString(ArrayList<Map<String,Object>>list,String str){
		try {			
			
			JSONArray mJson = new JSONArray(str);
			for(int i = 0; i < mJson.length(); i++){
				Map<String,Object> map = new HashMap<String,Object>();
				JSONObject obj = mJson.getJSONObject(i);
				
				map.put("user_name", obj.getString("user_name"));
				map.put("message_content", obj.getString("message_content"));
				map.put("message_date", obj.getString("message_date"));
				map.put("message_id",obj.getString("message_id"));
				map.put("comment_count",obj.getString("comment_count"));
				list.add(map);				
			}

			} catch (Exception e) {
				e.printStackTrace();
			}
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
                		(HomeActivity.this).inflate(R.layout.activity_home_message, null);
                
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
//            System.out.println("position= " + position);
            
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

		ArrayList<Map<String,Object>> list;
		int index;
		public TestAsync( int index,ArrayList<Map<String,Object>>list){
			this.list = list;
			this.index = index;
		}				
		
		@Override
		public String doInBackground(Void...voids){
			
			HomeData data = new HomeData();
			String result = data.DataAction(index,user_id);	
			//
			
			try {
				String jsonstring = result;
				if(jsonstring.length() == 2){
					//�˴���ʾ�������쳣��ִ���κβ���
				}
				json = new JSONArray(jsonstring);
				if(firstLoading){
					editor.putString("list_cache", jsonstring);
					firstLoading = false;
				}
				
				
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

				} catch (Exception e) {
					e.printStackTrace();
				}
			
			//
			
			
			editor.putInt("k", k);
			editor.commit();
			//
			

			return result;			
		}
		
		@Override
		public void onPostExecute(String result){
			
			mListView.onRefreshComplete();
			
			//��������ȡ��ʱ�����������ص�ֵ��ʵ�ǣ�[],resultҲ�͵���[]����������=2
			if(result.length() == 2){

				
				if(result == "[]"){
					Toast.makeText(getApplicationContext(),
							"�Ѽ���ȫ������", Toast.LENGTH_SHORT).show();
				}
				if(result == "00"){
					freshError = true;
					Toast.makeText(getApplicationContext(),
							"�����������쳣", Toast.LENGTH_SHORT).show();
				}
				
			} else{
				mListView.onMoreComplete();
				freshError = false;}
			
			adapter.notifyDataSetChanged();
		}		
	}
	//����ListView����
	
	//����HomePage��ʼ
	public void initHomePage(){
	
		SharedPreferences sharedPreferences = getSharedPreferences("currentuser", 
				Activity.MODE_PRIVATE);
				
		boolean isLogin = sharedPreferences.getBoolean("isLogin", false); 	
		if(!isLogin){
			Intent intent = new Intent(this, LoginActivity.class);;
			startActivity(intent);
		} else{
			currentuser = (TextView) findViewById(R.id.menu_user);
			String currentusername = sharedPreferences.getString("user_name", "");
			currentuser.setText(currentusername + " , ^_^ ��ӭ����");
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
    
    private PaddingListView lv_menu_button;
    public void initMenu(){
    	lv_menu_button = (PaddingListView) findViewById(R.id.lv_menu_button);
    	final MenuAdapter menuAdapter = new MenuAdapter();
    	lv_menu_button.setAdapter(menuAdapter);
    	lv_menu_button.setonRefreshListener(new com.laohuai.appdemo.customui.ui.PaddingListView.OnRefreshListener(){

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				menuAdapter.notifyDataSetChanged();
				lv_menu_button.onRefreshComplete();
			}});
    	lv_menu_button.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch(arg2){
				//PaddingListView����һ��header�����в���0��ʼ������1��ʼ
					case 1 :
						Intent intent = new Intent();
						intent.setClass(getApplicationContext(), SendActivity.class);
				    	startActivity(intent);
						break;
					case 2 :
						Intent intent1 = new Intent();
						intent1.setClass(getApplicationContext(), MyActivity.class);
						startActivity(intent1);
						break;
					case 3 :
						ArrayList<Map<String, Object>> temp = new ArrayList<Map<String,Object>>();
				    	list = temp;
				    	k=0;
				    	getData(list,k);
				    	System.out.println("list = " + list);
				    	if(!freshError){
				    		initListView();
				    	}
						break;
					case 4 :
						Intent intent6 = new Intent();
						intent6.setClass(getApplicationContext(), FollowActivity.class);
						startActivity(intent6);
						break;
					case 5 :
						Intent intent4 = new Intent();
						intent4.setClass(getApplicationContext(), QueryActivity.class);
				    	startActivity(intent4);
						break;
					case 6 :
						Intent intent5 = new Intent();
						intent5.setClass(getApplicationContext(), TestActivity.class);
				    	startActivity(intent5);
						break;
					case 7 :
						Intent intent3 = new Intent();
						intent3.setClass(getApplicationContext(), LoginActivity.class);
						intent3.putExtra("HomeActivity", false);
						startActivity(intent3);
						
						break;
					case 8 :

						LayoutInflater inflater = HomeActivity.this.getLayoutInflater();
						View view = inflater.inflate(R.layout.quit, null);
						ad = new AlertDialog.Builder(HomeActivity.this).create();
						ad.setView(view, 0, 0, 0, 0);
						ad.show();
						Button bt_quit_ok = (Button) view.findViewById(R.id.bt_quit_ok);
						Button bt_quit_no = (Button) view.findViewById(R.id.bt_quit_no);
						bt_quit_ok.setOnClickListener(HomeActivity.this);
						bt_quit_no.setOnClickListener(HomeActivity.this);
						

						break;
				}
				
			}});
    	
    }
    
    private List<Object> getMenuData() {
        
        List<Object> mList = new  ArrayList< Object>();
        mList.add(R.drawable.menu_send);
        mList.add(R.drawable.menu_main);
        mList.add(R.drawable.menu_fresh);
        mList.add(R.drawable.menu_follow);
        mList.add(R.drawable.menu_search);
        mList.add(R.drawable.menu_test);
        mList.add(R.drawable.menu_acount);
        mList.add(R.drawable.menu_quit);
              
        return mList;
    }
    
    private List<Map<String,Object>> menuData() {
        
        List<Map<String,Object>> mList = new  ArrayList<Map<String,Object>>();
        Map<String,Object> map1 = new HashMap<String, Object>();
        map1.put("title", "��  ��");
        map1.put("img", R.drawable.menu_send);       
        mList.add(map1);
        
        Map<String,Object> map2 = new HashMap<String, Object>();
        map2.put("title", "��  ҳ");
        map2.put("img", R.drawable.menu_main);       
        mList.add(map2);
        
        Map<String,Object> map3 = new HashMap<String, Object>();
        map3.put("title", "ˢ  ��");
        map3.put("img", R.drawable.menu_fresh);       
        mList.add(map3);
        
        Map<String,Object> map4 = new HashMap<String, Object>();
        map4.put("title", "��  ע");
        map4.put("img", R.drawable.menu_follow);       
        mList.add(map4);
        
        Map<String,Object> map5 = new HashMap<String, Object>();
        map5.put("title", "��  ��");
        map5.put("img", R.drawable.menu_search);       
        mList.add(map5);
        
        Map<String,Object> map6 = new HashMap<String, Object>();
        map6.put("title", "��  ��");
        map6.put("img", R.drawable.menu_test);       
        mList.add(map6);
        
        Map<String,Object> map7 = new HashMap<String, Object>();
        map7.put("title", "��  ��");
        map7.put("img", R.drawable.menu_acount);       
        mList.add(map7);
        
        Map<String,Object> map8 = new HashMap<String, Object>();
        map8.put("title", "ע  ��");
        map8.put("img", R.drawable.menu_quit);       
        mList.add(map8);
              
        return mList;
    }
    
    protected class MenuAdapter extends BaseAdapter{
    	List<Map<String,Object>> mList = menuData();
    	
    	List<Object> menu_list = getMenuData();
    	String[] itemList = 
    		{"��  ��","��  ҳ","ˢ  ��","��  ע","��  ��","��  ��" ,"��  ��","ע  ��"};
    	
    	int count = 8;
		@Override
		public int getCount() {
			return 8;
			//return menu_list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			if(arg1 == null){
				arg1 = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.menu_button, null);
				
				TextView item = (TextView) arg1.findViewById(R.id.menu_button_item);
//				item.setText(itemList[arg0]);
				
				ImageView menu_pic = (ImageView) arg1.findViewById(R.id.menu_pic);
//				menu_pic.setBackgroundResource((Integer)menu_list.get(arg0));
				
				item.setText(mList.get(arg0).get("title").toString());
				menu_pic.setBackgroundResource((Integer)mList.get(arg0).get("img"));
				
			}
			return arg1;
		}}	
	//����HomePage����


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.title_menu_button:
				menu.showMenu();
				break;
			case R.id.bt_quit_ok:
				SharedPreferences mySharedPreferences = 
				getSharedPreferences("currentuser", Activity.MODE_PRIVATE); 
				SharedPreferences.Editor editor = mySharedPreferences.edit(); 
				//ע��ʹuserinfo = "0" => userinfo.length = 1�������ڵ�½״̬
				editor.putBoolean("isLogin", false);
				editor.commit(); 
				finish();
				break;
			case R.id.bt_quit_no:
				ad.dismiss();
				break;

		}
		
	}

}
