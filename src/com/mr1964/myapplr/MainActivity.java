package com.mr1964.myapplr;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mr1964.home.HomeActivity;
import com.mr1964.login.LoginActivity;
import com.mr1964.messages.SendActivity;
import com.mr1964.mymessages.MyActivity;
import com.mr1964.paging.PagingActivity;
import com.mr1964.test.TestActivity;
import com.mr1964.utils.BaseActivity;

public class MainActivity extends BaseActivity {

	private TextView currentuser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		SharedPreferences sharedPreferences = getSharedPreferences("currentuser", 
				Activity.MODE_PRIVATE);
				
		boolean isLogin = sharedPreferences.getBoolean("isLogin", false); 	
		if(!isLogin){
			Intent intent = new Intent(this, LoginActivity.class);;
			startActivity(intent);
		} else{
			String currentusername = sharedPreferences.getString("user_name", "");
			currentuser.setText(currentusername + " ^_^ 欢迎回来");
		}
	}
	
	protected void myExit() {  
        Intent intent = new Intent();  
        intent.setAction("ExitApp");  
        this.sendBroadcast(intent);  
        super.finish();  
    }
	
	private long exitTime = 0;  
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if(keyCode == KeyEvent.KEYCODE_BACK){ 
            if((System.currentTimeMillis()-exitTime) > 2000){  
                Toast.makeText(getApplicationContext(),
                		"再按一次退出程序", Toast.LENGTH_SHORT).show();  
                exitTime = System.currentTimeMillis();  
            } else {  
                finish();  
            }  
            return true;  
        }  
        return super.onKeyDown(keyCode, event);  
    }
    
    public void test(View view){
    	
    	
    	Intent intent = new Intent(this,TestActivity.class);

    	startActivity(intent);
    }
 
    public void sample(View view){
    	
    	
    	Intent intent = new Intent(this,PagingActivity.class);

    	startActivity(intent);
    }
    
    public void send(View view){
    	Intent intent = new Intent(this,SendActivity.class);
    	startActivity(intent);
    }
	
	public void login(View view){
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}

	public void more(View view){
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
	}
	
	public void listview(View view){
		Intent intent = new Intent(this, MyActivity.class);
		startActivity(intent);
	}	

	public void quit(View view){
		SharedPreferences mySharedPreferences = getSharedPreferences("currentuser", 
				Activity.MODE_PRIVATE); 
		SharedPreferences.Editor editor = mySharedPreferences.edit(); 
		//注销使userinfo = "0" => userinfo.length = 1，即不在登陆状态
		editor.putBoolean("isLogin", false);
		editor.commit(); 
		finish();
	}
	
	
	

	
}
