
package com.mr1964.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.mr1964.utils.CheckNetwork;

public class TestActivity extends Activity implements OnScrollListener{
	
	private JSONArray json = null;
	private ListView mListView;	
	private int k = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
				
		getData();		
		initListView();
		
		
	}
	
	//从服务器下载数据
	public void getData(){
		CheckNetwork check = new CheckNetwork();

		if(check.isConnect(this)){
			TestAsync viewerAsync = new TestAsync(k);
			viewerAsync.execute();
			try {
				String jsonstring = viewerAsync.get();
				json = new JSONArray(jsonstring);

				} catch (Exception e) {}
			
		} else{
//			Toast.makeText(getApplicationContext(),
//					"无网络可用", Toast.LENGTH_SHORT).show();
			}
	}
		
	public void initListView(){
		mListView = (ListView) findViewById(R.id.myListView);

		
		ArrayList<Map<String ,Object>> list = new ArrayList<Map<String,Object>>();
		init(list);
		System.out.println("list = " + list);
		mListView.setAdapter(new MyAdapter(this,list));
		
		mListView.setOnItemClickListener(new OnItemClickListener(){
			 @Override  
			 public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {        
//				 Toast.makeText(getApplication(), "第  " + position + " 项", Toast.LENGTH_SHORT).show();	 
				 
				 try {
					JSONObject obj_id = json.getJSONObject(position);
					String message_id = String.valueOf(obj_id.getInt("message_id"));
					System.out.println("message_id= "+message_id);
					
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(), CommentActivity.class);
					intent.putExtra("message_id", message_id);
					startActivity(intent);
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				 
			    }
		});
	}
	
	
	
	
	private void init(ArrayList<Map<String,Object>>list){
				
		try{	//从服务器返回的数据，加入到map中，然后把map装入list
			//int i = 0; i < json.length(); i++
			for(int i = 0; i < json.length(); i++){
				Map<String,Object> map = new HashMap<String,Object>();
				JSONObject obj = json.getJSONObject(i);

				String username = obj.getString("user_name");
				String content = obj.getString("message_content");
				String date = obj.getString("message_date");
				
				map.put("user_name", username);
				map.put("message_content", content);
				map.put("message_date", date);
				list.add(map);				
			}
		} catch(Exception e){}
				
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
	
	}
	
	private class MyAdapter extends BaseAdapter{
        int count = 0;
        private ArrayList<Map<String, Object>> mItemList;
        public MyAdapter(Context context, List<Map<String, Object>> data) {
            mItemList = (ArrayList<Map<String, Object>>) data;
            if(data == null){
                count = 0;
            }else{
                count = data.size();
            }
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
            //观察convertView随ListView滚动情况             
            if (convertView == null) {
                     convertView = LayoutInflater.from(TestActivity.this).inflate(R.layout.message, null);
                     holder = new ViewHolder();
                    /**得到各个控件的对象*/                     
                    holder.user_name = (TextView) convertView.findViewById(R.id.user_name);
                    holder.message_content = (TextView) convertView.findViewById(R.id.message_content);
                    holder.message_date = (TextView) convertView.findViewById(R.id.message_date);
                    convertView.setTag(holder);//绑定ViewHolder对象                    
            }
            else{
                    holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象                   
            }
            /**设置TextView显示的内容，即我们存放在动态数组中的数据*/ 
            if(position>=0 && position<mItemList.size()){
            System.out.println("position= " + position);
            holder.user_name.setText(mItemList.get(position).get("user_name").toString());
            holder.message_content.setText(mItemList.get(position).get("message_content").toString());
            holder.message_date.setText(mItemList.get(position).get("message_date").toString());
            }
           
            return convertView;
			
		}
	}
	
	public final class ViewHolder{
	    public TextView user_name;
	    public TextView message_content;
	    public TextView   message_date;
    }
		

}
