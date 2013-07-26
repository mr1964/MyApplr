package com.mr1964.login;

import org.json.JSONException;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mr1964.home.HomeActivity;
import com.mr1964.myapplr.R;
import com.mr1964.register.RegisterActivity;
import com.mr1964.utils.BaseActivity;
import com.mr1964.utils.JsonParser;

public class LoginActivity extends BaseActivity {
	private EditText login_username;
	private EditText login_password;
	private Button bt_login;
	private View mLoginFormView;
	private View mLoginStatusView;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		boolean from = true;
		from = getIntent().getBooleanExtra("HomeActivity", true);
		
		SharedPreferences sharedPreferences = getSharedPreferences("currentuser", 
				Activity.MODE_PRIVATE);				
		boolean isLogin = sharedPreferences.getBoolean("isLogin", false); 	
		if(isLogin && from ){
			
			Intent intent = new Intent(this, HomeActivity.class);;
			startActivity(intent);
			
		}
				
		setContentView(R.layout.activity_login);
								
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginFormView = findViewById(R.id.login_form);
		login_username = (EditText) findViewById(R.id.login_username);
		login_password = (EditText) findViewById(R.id.login_password);
		bt_login = (Button) findViewById(R.id.bt_login);		
		bt_login.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view){
				String username = login_username.getText().toString().trim();
				String password = login_password.getText().toString().trim();
				if(username.isEmpty()){
					login_username.setError("�û�������Ϊ��");
					login_username.requestFocus();
				} else if(password.isEmpty()){
					login_password.setError("���벻��Ϊ��");
					login_password.requestFocus();
				} else{
					//��ʼ������
					showProgress(true);					
					LoginAsync loginAsync = new LoginAsync(username, password);
					loginAsync.execute();
					//finish();	
					
				}
			}
		});
		
	}
	//�˳��ķ�������Ҫ�˳�ʱ����myExit()����
	protected void myExit() {  
        Intent intent = new Intent();  
        intent.setAction("ExitApp");  
        this.sendBroadcast(intent);  
        super.finish();  
    }
	
	//��back����ֱ���˳�
	private long exitTime = 0;  
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	SharedPreferences sharedPreferences = getSharedPreferences("currentuser", 
				Activity.MODE_PRIVATE);
    	boolean isLogin = sharedPreferences.getBoolean("isLogin", false);
    	//����½״̬isLogin��true�򷵻���ҳ��false��ֱ���˳�app

	        if(keyCode == KeyEvent.KEYCODE_BACK){
	        	if(isLogin){
	        		finish();
	        	} else{
		        	if((System.currentTimeMillis()-exitTime) > 2000){  
		                Toast.makeText(getApplicationContext(),
		                		"�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();  
		                exitTime = System.currentTimeMillis();  
		            } else {  
		            	myExit();  
		            }  
		        	return true;
	        	}
	        } 
        
        
        return super.onKeyDown(keyCode, event);  
    }
	
	public void toregister(View v){
    	Intent intent = new Intent(this, RegisterActivity.class);
    	startActivity(intent);
    }
	
	//��ʾ����
	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	public class LoginAsync extends AsyncTask<Void,Void,String>{
		private String username;
		private String password;
		public LoginAsync(String username, String password){
			this.username = username;
			this.password = password;
		}
		@Override
		public String doInBackground(Void...params){
			Login login = new Login();
			String RESULT = login.loginAction(username, password);
			return RESULT;
			
		}
		
		@Override
		public void onPostExecute(String RESULT){
			
			String user_name = null;
			String user_id = null;
			//����������
			showProgress(false);

			SharedPreferences mySharedPreferences = getSharedPreferences("currentuser", 
					Activity.MODE_PRIVATE); 
			SharedPreferences.Editor editor = mySharedPreferences.edit(); 
			editor.putString("userinfo", RESULT); 
			

			JsonParser jsonParser = new JsonParser(RESULT);
			try {
				user_name = jsonParser.getString("user_name", 0);
				user_id = jsonParser.getString("user_id", 0);				
			} catch (JSONException e) {}
			
			editor.putString("user_name", user_name);
			editor.putString("user_id", user_id); 
			editor.commit(); 
			System.out.println("RESULT.length() = " + RESULT.length());
						
//.........................................	
			
/*			 * ����������3��ֵ��0,1,��½�ɹ�
			 * ǰ���־�Ϊ��½ʧ�ܣ�����String�ͣ�0 �� 1 ����ʱRESULT.length() == 1
			 * �ɹ�ʱ��RESULT.length() ��= 1
			 * �û���Ϣ(json��)
			 * 0��ʾ�û��������ڣ�1��ʾ�������*/
			
			if(RESULT.length() == 1){
				int i = Integer.parseInt(RESULT);
				switch(i){
					case 0 :login_username.setError("�û���������");
							login_username.requestFocus();
						break;
					case 1 :login_password.setError("���벻��ȷ");
							login_password.requestFocus();
						break;
					case 2 :
						login_username.setError("����������Ӳ���������");

						break;
						}
				
			} else{
				
				Intent intent = new Intent();
				intent.putExtra("username", username);
				intent.setClass(getApplicationContext(),HomeActivity.class);
				
				startActivity(intent);
				
				//��½�ɹ�����true����mySharedPreferences
				editor.putBoolean("isLogin", true);
				editor.commit();
				Toast.makeText(getApplicationContext(), 
						"��½�ɹ��� ^_^", Toast.LENGTH_SHORT).show();
				//finish();				
			}
//..........................................			
		}
	}
}
