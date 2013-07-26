/*
 * 说明：int k 用于递进地取回数据，k 应该 每次取完数据后自动加10
 * */

package com.mr1964.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
//import android.widget.Toast;

import com.mr1964.comments.CommentActivity;
import com.mr1964.myapplr.R;
import com.mr1964.utils.BaseActivity;

public class QueryActivity extends BaseActivity implements OnScrollListener,OnTouchListener, OnGestureListener {

	private GestureDetector mGestureDetector;  
	
	private JSONArray json = null;
	private ListView mListView;	
	private MyAdapter adapter;
	/*
	 * 如果这样写就会报空指针
	 * private ArrayList<Map<String ,Object>> list；
	 * 为什么呢？
	 */	
	//下面这样写则不会报空指针
	private ArrayList<Map<String ,Object>> list = new ArrayList<Map<String,Object>>();
	private ArrayList<Map<String, Object>> mItemList = new ArrayList<Map<String,Object>>();
	
	private int k = 0;
	private boolean isLoading = false;
	
	private Button bt_query;
	private EditText et_query;
	private ImageView back_query;
	private String key;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_query);
		
		et_query = (EditText) findViewById(R.id.et_query);
		bt_query = (Button) findViewById(R.id.bt_query);
		bt_query.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view){
				key = et_query.getText().toString().trim();
				if(key.isEmpty()){
					et_query.setError("请输入您要搜索的内容");
					et_query.requestFocus();
				}else{
					ArrayList<Map<String,Object>> temp = 
							new ArrayList<Map<String,Object>>();
					k = 0;//再按搜索键时是搜索新的一个词，重新从第0位开始
					list = temp;//同理，list置空（temp为空数组）。不能使list=null，会空指针
					getData(list,key);				
					initListView();
				}
			}
		});
		
		back_query = (ImageView) findViewById(R.id.back_query);
		back_query.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		
			
		mGestureDetector = new GestureDetector((OnGestureListener) this);    
        TableLayout viewSnsLayout = (TableLayout)findViewById(R.id.tl_query);    
        viewSnsLayout.setOnTouchListener(this);    
        viewSnsLayout.setLongClickable(true);  
				
	}
	
		
	public void initListView(){
		
		mListView = (ListView) findViewById(R.id.query_listview);		
		adapter = new MyAdapter(this,list);
		mListView.setAdapter(adapter);
		mListView.setOnScrollListener(this);
	
		mListView.setOnItemClickListener(new OnItemClickListener(){
			 @Override  
			 public void onItemClick
			 (AdapterView<?> arg0, View view, int position, long id) {   
				 
/*				 Toast.makeText(getApplication(), 
						 "第  " + position + " 项", Toast.LENGTH_SHORT).show();	*/ 
			
				String message_id = mItemList.get(position).get("message_id").toString();
				
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), CommentActivity.class);
				intent.putExtra("message_id", message_id);
				startActivity(intent);

//				 System.out.println("OnItemClickListenertotallist = " + totallist);
				 
				 
			    }
		});
	}
	
	//从服务器下载数据
		public void getData(ArrayList<Map<String,Object>>list,String query){

			TestAsync viewerAsync = new TestAsync(k,query);
			System.out.println("      query = " + query);
			k = k+10;
			viewerAsync.execute();
			try {
				String jsonstring = viewerAsync.get();
				json = new JSONArray(jsonstring);
				
				System.out.println("json = " + json);
				
				for(int i = 0; i < json.length(); i++){
					Map<String,Object> map = new HashMap<String,Object>();
					JSONObject obj = json.getJSONObject(i);

					String username = obj.getString("user_name");
					String content = obj.getString("message_content");
					String date = obj.getString("message_date");
					String id = obj.getString("message_id");
					
					map.put("user_name", username);
					map.put("message_content", content);
					map.put("message_date", date);
					map.put("message_id",id);
					list.add(map);				
				}

				} catch (Exception e) {}
							
		}		
	


	public void onScroll(AbsListView view, int mFirstVisibleItem,
            int mVisibleItemCount, int mTotalItemCount) {

        
		/*mFirstVisibleItem + mVisibleItemCount == mTotalItemCount为true时
		 * 表示滑到到最后一项，可以加载更多内容了
		 * 
		 * */
        if(mFirstVisibleItem + mVisibleItemCount == mTotalItemCount&& !isLoading ){
        	getData(list,key);
            adapter.notifyDataSetChanged();
        }
        
        
    }
    public void onScrollStateChanged(AbsListView view, int mScrollState) {
        
    }
	
	private class MyAdapter extends BaseAdapter{

       
//        private ArrayList<Map<String, Object>> mItemList;
        public MyAdapter(Context context, List<Map<String, Object>> data) {
            mItemList = (ArrayList<Map<String, Object>>) data;

           
        }
        public int getCount() {
        	//return count;
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
            //观察convertView随ListView滚动情况             
            if (convertView == null) {
                convertView = LayoutInflater.from
                		(QueryActivity.this).inflate(R.layout.message, null);
                
                holder = new ViewHolder();
                /**得到各个控件的对象*/                     
                holder.user_name = (TextView) 
                		convertView.findViewById(R.id.user_name);
                holder.message_content = (TextView) 
                		
                		convertView.findViewById(R.id.message_content);
                holder.message_date = (TextView) 
                		
                		convertView.findViewById(R.id.message_date);
                convertView.setTag(holder);//绑定ViewHolder对象                    
            }
            else{
                holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象                   
            }
            /**设置TextView显示的内容，即我们存放在动态数组中的数据*/ 
            if(position>=0 && position<mItemList.size()){
            System.out.println("position= " + position);
            
            holder.user_name.setText(mItemList.get(position).get
            		("user_name").toString());
            
            holder.message_content.setText(mItemList.get(position).get
            		("message_content").toString());
            
            holder.message_date.setText(mItemList.get(position).get
            		("message_date").toString());
            }
           
            return convertView;
			
		}
	}
	
	public final class ViewHolder{
	    public TextView user_name;
	    public TextView message_content;
	    public TextView   message_date;
    }
	
	public class TestAsync extends AsyncTask<Void,Void,String>{

		private int i ;
		private String keyword;
		
		public TestAsync(int i,String keyword){
			this.i = i;
			this.keyword = keyword;
		}				
		
		@Override
		public String doInBackground(Void...voids){
			
			QueryData data = new QueryData();
			String result = data.QueryDataAction(i, keyword);			
			isLoading = true;
			return result;			
		}
		
		@Override
		public void onPostExecute(String result){

			//已无数据取回时，服务器返回的值其实是：[],result也就等于[]，长度正好=2
			if(result.length() == 2){
				isLoading = true;
				
			} else{isLoading = false;}			
		}		
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

