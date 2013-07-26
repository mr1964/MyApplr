package com.mr1964.mymessages;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.mr1964.utils.MyURL;

public class MyData {
	public String result = "[]";
	public String DataAction(int i ,String user_id){		
		try{
						
			//String url = "http://192.168.1.103:8080/Applr0504/listview/ListAction";
			//String url = "http://mr1964.eicp.net:8080/Applr0504/listview/ListAction";
			MyURL myURL = new MyURL();			
			String url = myURL.port + "Applr0504/listview/ListAction";
			
			String sql = "select * from messages where user_id = ?" +
					" order by message_id desc limit "+ i +",10";
			
			HttpPost httpRequest  = new HttpPost(url);
			List<NameValuePair> params=new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("sql",sql));
			params.add(new BasicNameValuePair("user_id",user_id));
			httpRequest.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8)); 
			//超时设置的关键部分
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 3000;
			HttpConnectionParams.setConnectionTimeout(
					httpParameters, timeoutConnection);
			int timeoutSocket = 5000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

			DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			
			if (httpResponse.getStatusLine().getStatusCode()==200) {  
				result = EntityUtils.toString(httpResponse
						.getEntity());
				System.out.println("ListView中的 result = " + result);
			}
		}catch(Exception e){e.printStackTrace();}
		
	return result;
	}
	
}
