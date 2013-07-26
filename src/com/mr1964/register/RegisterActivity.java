package com.mr1964.register;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mr1964.myapplr.R;

public class RegisterActivity extends Activity {
	private EditText et_username;
	private EditText et_password;
	private EditText et_check_passaword;
	private Button bt_register;
	private View mRegisterStatusView;
	private View mRegisterFormView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		//使用自定义标题栏
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_register);
		//使用布局文件来定义标题栏
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.bar_title);	
						
		
		mRegisterStatusView = findViewById(R.id.register_status);
		mRegisterFormView = findViewById(R.id.register_form);
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		et_check_passaword =(EditText) findViewById(R.id.et_check_password);
		bt_register = (Button) findViewById(R.id.bt_register);
		bt_register.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view){
				
				String username = et_username.getText().toString().trim();
				String password = et_password.getText().toString().trim();
				String check = et_check_passaword.getText().toString().trim();
				if(username.isEmpty()){
					et_username.setError("用户名不能为空");
					et_username.requestFocus();
				} else if(password.isEmpty()){
					et_password.setError("密码不能为空");
					et_password.requestFocus();
				} else if(check.isEmpty()){
					et_check_passaword.setError("密码不能为空");
					et_check_passaword.requestFocus();
				} else if(!password.equals(check)){
					
					System.out.println("password=" + password);
					System.out.println("check=" + check);
					System.out.println("password != check =" + (password != check));
					
					et_password.setError("两次密码不一致");
					et_password.requestFocus();
				} else{
					showProgress(true);
					RegisterAsync registerAsync = new RegisterAsync(username, password);
					registerAsync.execute();
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

			mRegisterStatusView.setVisibility(View.VISIBLE);
			mRegisterStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mRegisterStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mRegisterFormView.setVisibility(View.VISIBLE);
			mRegisterFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mRegisterFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mRegisterStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
	
	
	
	

	public class RegisterAsync extends AsyncTask<Void,Void,Boolean>{
		private String username;
		private String password;
		public RegisterAsync(String username,String password){
			this.username = username;
			this.password = password;
		}
		
		@Override
		public Boolean doInBackground(Void...params){
			Register register = new Register();
			boolean success = register.registerAction(username, password);			
			return success;
			

		}
		
		@Override
		protected void onPostExecute(final Boolean success) {
			showProgress(false);
			if (success) {
				Toast.makeText(getApplicationContext(), "注册成功！ ^_^", Toast.LENGTH_SHORT).show();
				System.out.println("success = " + success);
				finish();
			} else {
				et_username.setError("用户名已存在");
				et_username.requestFocus();
			}
		}
	}













}


