package com.mr1964.comments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mr1964.comments.display.SingleMessageAsync;
import com.mr1964.comments.send.CommentDisplayAsync;
import com.mr1964.comments.send.CommentSend;
import com.mr1964.myapplr.R;
import com.mr1964.utils.JsonParser;

public class CommentActivity extends Activity  implements OnTouchListener, OnGestureListener {

	private GestureDetector mGestureDetector;  
	private TextView detail_username;
	private TextView detail_content;
	private TextView detail_date;
		
	private EditText et_comment;
	private Button bt_comment;
	private ImageView iv_back_comment;
	
	private String feedback = null;
	private String username = null;
//	private String user_id = null;
	private String message = null;
	private String date = null;
	private String message_id = null;
	
	private JSONArray comment_json = null;
	private ListView comment_listview;		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		
        mGestureDetector = new GestureDetector((OnGestureListener) this);    
        LinearLayout viewSnsLayout = (LinearLayout)findViewById(R.id.comment_send);    
        viewSnsLayout.setOnTouchListener(this);    
        viewSnsLayout.setLongClickable(true);  
		
		comment_listview = (ListView) findViewById(R.id.comnent_display);

		//发评论--------开始
		detail_username = (TextView) findViewById(R.id.detail_username);
		detail_content = (TextView) findViewById(R.id.detail_content);
		detail_date = (TextView) findViewById(R.id.detail_date);
		message_id = getIntent().getStringExtra("message_id");
		
		SingleMessageAsync commentAsync = new SingleMessageAsync(message_id);
		commentAsync.execute();
		try {
			feedback = commentAsync.get();
			System.out.println("feedback= " + feedback);
		} catch (InterruptedException e) {} 
		catch (ExecutionException e) {}		
		
		try {
			JsonParser jsonParser = new JsonParser(feedback);
			username = jsonParser.getString("user_name", 0);
//			user_id = jsonParser.getString("user_id", 0);
			message = jsonParser.getString("message_content", 0);
			date = jsonParser.getString("message_date", 0);
		} catch (JSONException e) {}
		
		//显示message评论页时的message内容
		detail_username.setText(username);
		detail_content.setText(message);
		detail_date.setText(date);
				
		et_comment = (EditText) findViewById(R.id.et_comment);
		bt_comment = (Button) findViewById(R.id.bt_comment);
		bt_comment.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view){
				String comment_content = et_comment.getText().toString().trim();
				if(comment_content.isEmpty()){
					et_comment.setError("发送内容不能为空");
					et_comment.setFocusable(true);
				} else{
					SharedPreferences sharedPreferences = getSharedPreferences("currentuser", 
							Activity.MODE_PRIVATE);
						
					String currentusername = sharedPreferences.getString("user_name", "");
					String currentuser_id = sharedPreferences.getString("user_id", "");
					
					CommentSendAsync commentSendAsync = 
							new CommentSendAsync(comment_content,currentuser_id,currentusername,message_id);
					commentSendAsync.execute();
					System.out.println("comment_content= " + comment_content);
					System.out.println("currentuser_id= " + currentuser_id);
					System.out.println("message_id= " + message_id);
				}
			}
		});
		//发评论---------结束
		
		iv_back_comment = (ImageView) findViewById(R.id.back_comment);
		iv_back_comment.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				
			}});
		
		//更新listview
		freshListView(message_id);
				
		
	}
	
	//把commentlistview放onCreate的外面
	public void freshListView(String id){
		//评论listview代码---开始
		
		CommentDisplayAsync commentDisplayAsync = new CommentDisplayAsync(id);
		commentDisplayAsync.execute();
			try {			
				String jsonstring = commentDisplayAsync.get();

				comment_json = new JSONArray(jsonstring);
				System.out.println("CommentActivity中的 jsonstring = " + jsonstring);
				} catch (Exception e) {}
		
		ArrayList<Map<String ,Object>> list = new ArrayList<Map<String,Object>>();
		init(list);		
		comment_listview.setAdapter(new SimpleAdapter(this,list,R.layout.comment,
				new String[] {"commentator_name","comment_content","comment_date"},
				new int[] {R.id.comment_user_name,R.id.comment_content,R.id.comment_date}));
		
		comment_listview.setOnItemClickListener(new OnItemClickListener(){
			 @Override  
			 public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {        

				 
			    }
		});
		//评论listview代码---结束
	}
	
	public class CommentSendAsync extends AsyncTask<Void,Void,String>{
		private String comment_content;
		private String commentator_id;
		private String message_id;
		private String commentator_name;
		public CommentSendAsync(String comment_content,String commentator_id,
				String commentator_name,String message_id){
			this.comment_content = comment_content;
			this.commentator_id = commentator_id;
			this.message_id =  message_id;
			this.commentator_name = commentator_name;
		}
		
		@Override
		public String doInBackground(Void...voids){
			CommentSend commentSend = new CommentSend();
			String result = commentSend.DataAction(comment_content, commentator_id, commentator_name,message_id);
			return result;
		}
		
		@Override
		public void onPostExecute(String result){
			System.out.println("评论返回值result = " + result);
			if(!result.isEmpty()){
				System.out.println("评论返回值 if result = " + result);
				//评论成功就更新评论的listview
				//此处代码
				freshListView(message_id);
				et_comment.setText(null);
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
			}
		}		
	}
	
	//为listview填充数据
	private void init(ArrayList<Map<String,Object>>list){
		
		try{	//从服务器返回的数据，加入到map中，然后把map装入list
			//int i = 0; i < json.length(); i++
			for(int i = 0; i < comment_json.length(); i++){
				Map<String,Object> map = new HashMap<String,Object>();
				JSONObject obj = comment_json.getJSONObject(i);

				String commentator_name = obj.getString("commentator_name");
				String comment_content = obj.getString("comment_content");
				String comment_date = obj.getString("comment_date");
				
				map.put("commentator_name", commentator_name);
				map.put("comment_content", comment_content);
				map.put("comment_date", comment_date);
				list.add(map);				
			}
		} catch(Exception e){}
				
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
