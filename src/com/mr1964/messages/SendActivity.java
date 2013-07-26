package com.mr1964.messages;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.mr1964.myapplr.R;

public class SendActivity extends Activity implements OnTouchListener, OnGestureListener{
	private EditText et_send;
	private Button bt_send;
	private String user_id;
	private String user_name;
	private String message_content;
	private View mSendStatusView;
	private View mSendFormView;
	
	private GestureDetector mGestureDetector;  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.activity_send);
		init();	
		mGestureDetector = new GestureDetector((OnGestureListener) this);    
        ScrollView viewSnsLayout = (ScrollView)findViewById(R.id.send_form);    
        viewSnsLayout.setOnTouchListener(this);    
        viewSnsLayout.setLongClickable(true); 
        
        ImageView back_send = (ImageView) findViewById(R.id.back_send);
        back_send.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		
		
	}
	
	public void init(){
		SharedPreferences sharedPreferences = getSharedPreferences(
				"currentuser", Activity.MODE_PRIVATE);
		String userinfo =sharedPreferences.getString("userinfo", null);
		try {
			JSONArray json = new JSONArray(userinfo);
			JSONObject obj = json.getJSONObject(0);
			user_id = obj.getString("user_id");
			user_name = obj.getString("user_name");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mSendStatusView = findViewById(R.id.send_status);
		mSendFormView = findViewById(R.id.send_form);
		et_send = (EditText) findViewById(R.id.et_send);
		bt_send = (Button) findViewById(R.id.bt_send);
		bt_send.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view){
				message_content = et_send.getText().toString().trim();
				if(message_content.isEmpty()){
					et_send.setError("发送内容不能为空");
					et_send.requestFocus();
				} else{
					showProgress(true);
					SendAsync sendAsync = new SendAsync(user_id, user_name,message_content);
					sendAsync.execute();
				}
			}
		});
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mSendStatusView.setVisibility(View.VISIBLE);
			mSendStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mSendStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mSendFormView.setVisibility(View.VISIBLE);
			mSendFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mSendFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mSendStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mSendFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
	
	
	public class SendAsync extends AsyncTask<Void,Void,Void>{
		private String id;
		private String content;
		private String name;
		
		public SendAsync(String id,String name,String content){
			this.id = id;
			this.name = name;
			this.content = content;
		}
		
		@Override
		public Void doInBackground(Void...voids){
			Send send = new Send();
			send.sendAction(id, name,content);
			return null;			
		}
		
		@Override
		public void onPostExecute(Void n){
			showProgress(false);
			finish();
			Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
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
