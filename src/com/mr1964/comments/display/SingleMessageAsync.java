package com.mr1964.comments.display;

import android.os.AsyncTask;

public class SingleMessageAsync extends AsyncTask<Void,Void,String>{
	private String message_id;
	//无参数的构造函数
	public SingleMessageAsync(String message_id){
		this.message_id = message_id;
	}
	
	public String json = null;
	
	@Override
	public String doInBackground(Void...voids){
		SingleMessageData comment = new SingleMessageData();
		String result = comment.DataAction(message_id);
		return result;			
	}
	
	@Override
	public void onPostExecute(String result){
		json =  result;
	}
	
}
