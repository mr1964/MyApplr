package com.mr1964.comments.send;

import android.os.AsyncTask;

public class CommentDisplayAsync extends AsyncTask<Void,Void,String>{
	
	private String message_id;
	public CommentDisplayAsync(String message_id){
		this.message_id = message_id;
	}
	
	public String json = null;
	
	@Override
	public String doInBackground(Void...voids){
		CommentDisplayData data = new CommentDisplayData();
		String result = data.DataAction(message_id);
		System.out.println("CommentDisplayAsyncÖÐµÄ result = " + result);
		return result;			
	}
	
	@Override
	public void onPostExecute(String result){
		json =  result;
	}
	
}
