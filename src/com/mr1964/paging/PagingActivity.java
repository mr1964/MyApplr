/*
 * 说明：int k 用于递进地取回数据，k 应该 每次取完数据后自动加10
 * */

package com.mr1964.paging;

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
//import android.widget.Toast;

import com.mr1964.comments.CommentActivity;
import com.mr1964.myapplr.R;
import com.mr1964.utils.BaseActivity;

public class PagingActivity extends BaseActivity implements OnScrollListener{
	
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
			
		getData(list);	
		initListView();
				
	}
	
		
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
        	getData(list);
            adapter.notifyDataSetChanged();
            
            System.out.println("isLoading = "+isLoading);
            System.out.println("mFirstVisibleItem = "+mFirstVisibleItem);
            System.out.println("mVisibleItemCount = "+mVisibleItemCount);
        	System.out.println("mTotalItemCount = "+mTotalItemCount);
           // isLoading = true;
        }
        
        
    }
    public void onScrollStateChanged(AbsListView view, int mScrollState) {
        
        /**
         * 当ListView滑动到最后一条记录时这时，我们会看到已经被添加到ListView的"加载项"布局， 这时应该加载剩余数据。
         */
        
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
                		(PagingActivity.this).inflate(R.layout.message, null);
                
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
		
		//无参数的构造函数
		int i ;
//		public String json = null;
		
		public TestAsync(int i){
			this.i = i;
		}				
		
		@Override
		public String doInBackground(Void...voids){
			
			PagingData data = new PagingData();
			String result = data.DataAction(i);

			
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
		
}
