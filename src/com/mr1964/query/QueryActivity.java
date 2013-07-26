/*
 * ˵����int k ���ڵݽ���ȡ�����ݣ�k Ӧ�� ÿ��ȡ�����ݺ��Զ���10
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
	 * �������д�ͻᱨ��ָ��
	 * private ArrayList<Map<String ,Object>> list��
	 * Ϊʲô�أ�
	 */	
	//��������д�򲻻ᱨ��ָ��
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
					et_query.setError("��������Ҫ����������");
					et_query.requestFocus();
				}else{
					ArrayList<Map<String,Object>> temp = 
							new ArrayList<Map<String,Object>>();
					k = 0;//�ٰ�������ʱ�������µ�һ���ʣ����´ӵ�0λ��ʼ
					list = temp;//ͬ��list�ÿգ�tempΪ�����飩������ʹlist=null�����ָ��
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

        
		/*mFirstVisibleItem + mVisibleItemCount == mTotalItemCountΪtrueʱ
		 * ��ʾ���������һ����Լ��ظ���������
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
            //�۲�convertView��ListView�������             
            if (convertView == null) {
                convertView = LayoutInflater.from
                		(QueryActivity.this).inflate(R.layout.message, null);
                
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

			//��������ȡ��ʱ�����������ص�ֵ��ʵ�ǣ�[],resultҲ�͵���[]����������=2
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
	  
			// �л�Activity  
			// Intent intent = new Intent(ViewSnsActivity.this, UpdateStatusActivity.class);  
			// startActivity(intent);  
			//Toast.makeText(this, "��������", Toast.LENGTH_SHORT).show();  
		} else if (e2.getX() - e1.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {  
	  
			// �л�Activity  
			// Intent intent = new Intent(ViewSnsActivity.this, UpdateStatusActivity.class);  
			// startActivity(intent);  
			//Toast.makeText(this, "��������", Toast.LENGTH_SHORT).show();  
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
	
	//�������ļ�����LinearLayoutʱ����������RelativeLayout
	//��LinearLayout�а��������»����Ŀؼ�(ListView,ScrollView)ʱ��Ҫ�������ĸ�д����
	@Override  
   public boolean dispatchTouchEvent(MotionEvent ev) {  
	   mGestureDetector.onTouchEvent(ev);  
	   // scroll.onTouchEvent(ev);  
	   return super.dispatchTouchEvent(ev);  
   }	
}

