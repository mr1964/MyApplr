package com.mr1964.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/*用于检查网络是否可以用的java类*/

public class CheckNetwork {
	
	 //检查网络，不可用则kill
	public void noNetworkKillProcess(Context context){		
        if (isConnect(context)==false) {   
            new AlertDialog.Builder(context) 
            .setTitle("网络错误") 
            .setMessage("网络连接失败，请确认网络连接") 
            .setPositiveButton("确定", new DialogInterface.OnClickListener() { 
            @Override 
 			public void onClick(DialogInterface arg0, int arg1) { 
 			//TODO Auto-generated method stub 
 			android.os.Process.killProcess(android.os.Process.myPid()); 
 			                System.exit(0); 
 			} 
 			}).show(); 
 		}
	}
	
	
	
    public boolean isConnect(Context context) { 
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理） 
	    try { 
	        ConnectivityManager connectivity = (ConnectivityManager) context 
	                .getSystemService(Context.CONNECTIVITY_SERVICE); 
	        if (connectivity != null) { 
	            // 获取网络连接管理的对象 
	            NetworkInfo info = connectivity.getActiveNetworkInfo(); 
	            if (info != null&& info.isConnected()) { 
	                // 判断当前网络是否已经连接 
	                if (info.getState() == NetworkInfo.State.CONNECTED) { 
	                    return true; 
	                } 
	            } 
	        } 
	    } catch (Exception e) { 
	    	// TODO: handle exception 
	    Log.v("error",e.toString()); 
	    	} 
        return false; 
    } 
}
