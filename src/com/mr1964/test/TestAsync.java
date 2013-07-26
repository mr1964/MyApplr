package com.mr1964.test;

import android.os.AsyncTask;

public class TestAsync extends AsyncTask<Void,Void,String>{
	
	//无参数的构造函数
	int i ;
	public TestAsync(int i){
		this.i = i;
	}
	
	public String json = null;
	
	@Override
	public String doInBackground(Void...voids){
		TestData data = new TestData();
		String result = data.DataAction(i);
		return result;			
	}
	
	@Override
	public void onPostExecute(String result){
		json =  result;
	}
	
}
