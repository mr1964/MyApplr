/*
 * ˵����int k ���ڵݽ���ȡ�����ݣ�k Ӧ�� ÿ��ȡ�����ݺ��Զ���10
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
	 * �������д�ͻᱨ��ָ��
	 * private ArrayList<Map<String ,Object>> list��
	 * Ϊʲô�أ�
	 */	
	//��������д�򲻻ᱨ��ָ��
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
						 "��  " + position + " ��", Toast.LENGTH_SHORT).show();	*/ 
			
				String message_id = mItemList.get(position).get("message_id").toString();
				
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), CommentActivity.class);
				intent.putExtra("message_id", message_id);
				startActivity(intent);

//				 System.out.println("OnItemClickListenertotallist = " + totallist);
				 
				 
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

        
		/*mFirstVisibleItem + mVisibleItemCount == mTotalItemCountΪtrueʱ
		 * ��ʾ���������һ����Լ��ظ���������
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
         * ��ListView���������һ����¼ʱ��ʱ�����ǻῴ���Ѿ�����ӵ�ListView��"������"���֣� ��ʱӦ�ü���ʣ�����ݡ�
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
            //�۲�convertView��ListView�������             
            if (convertView == null) {
                convertView = LayoutInflater.from
                		(PagingActivity.this).inflate(R.layout.message, null);
                
                holder = new ViewHolder();
                /**�õ������ؼ��Ķ���*/                     
                holder.user_name = (TextView) 
                		convertView.findViewById(R.id.user_name);
                holder.message_content = (TextView) 
                		
                		convertView.findViewById(R.id.message_content);
                holder.message_date = (TextView) 
                		
                		convertView.findViewById(R.id.message_date);
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
		
		//�޲����Ĺ��캯��
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

			//��������ȡ��ʱ�����������ص�ֵ��ʵ�ǣ�[],resultҲ�͵���[]����������=2
			if(result.length() == 2){
				isLoading = true;
			} else{isLoading = false;}			
		}		
	}
		
}
