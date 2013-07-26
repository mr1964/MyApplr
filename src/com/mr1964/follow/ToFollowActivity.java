package com.mr1964.follow;

import java.io.IOException;

import org.apache.http.ParseException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.mr1964.myapplr.R;

public class ToFollowActivity extends Activity {
	private Button bt_follow;
	private Button bt_notfollow;
	private String user1_id;
	private String user2_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_to_follow);
		
		initButton();
		
			
	}
	
	public void initButton(){
		bt_follow = (Button) findViewById(R.id.bt_follow);
		bt_notfollow = (Button) findViewById(R.id.bt_notfollow);
		
		bt_follow.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				SharedPreferences sharedPreferences = getSharedPreferences
						("currentuser",Activity.MODE_PRIVATE);
				user1_id = sharedPreferences.getString("user_id", "");		
				user2_id = getIntent().getStringExtra("user_id");
				System.out.println(user1_id + user2_id );
				FollowAsync follow = new FollowAsync(user1_id,user2_id);
				follow.execute();				
				
				
			}});
		
		bt_notfollow.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				finish();
				
			}});
		
	}
	
	public class FollowAsync extends AsyncTask<Void,Void,Boolean>{
		private String user1;
		private String user2;
		private boolean flag = false;
		
		public FollowAsync(String user1,String user2){
			this.user1 = user1;
			this.user2 = user2;
		}
		
		@Override
		protected Boolean doInBackground(Void... arg0) {

			FollowSendData data = new FollowSendData();
			
			try {
				flag = data.FollowAction(user1, user2);
			} catch (ParseException e) {
			} catch (IOException e) {
			}
			
			return flag;
		}
		
		@Override
		public void onPostExecute(Boolean flag){
			if(flag){
				Toast.makeText(getApplicationContext(), 
						"Follow ³É¹¦", Toast.LENGTH_SHORT).show();
			}
			finish();
		}
		
	}



}
