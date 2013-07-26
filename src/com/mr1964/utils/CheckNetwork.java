package com.mr1964.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/*���ڼ�������Ƿ�����õ�java��*/

public class CheckNetwork {
	
	 //������磬��������kill
	public void noNetworkKillProcess(Context context){		
        if (isConnect(context)==false) {   
            new AlertDialog.Builder(context) 
            .setTitle("�������") 
            .setMessage("��������ʧ�ܣ���ȷ����������") 
            .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() { 
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
        // ��ȡ�ֻ��������ӹ�����󣨰�����wi-fi,net�����ӵĹ��� 
	    try { 
	        ConnectivityManager connectivity = (ConnectivityManager) context 
	                .getSystemService(Context.CONNECTIVITY_SERVICE); 
	        if (connectivity != null) { 
	            // ��ȡ�������ӹ���Ķ��� 
	            NetworkInfo info = connectivity.getActiveNetworkInfo(); 
	            if (info != null&& info.isConnected()) { 
	                // �жϵ�ǰ�����Ƿ��Ѿ����� 
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
