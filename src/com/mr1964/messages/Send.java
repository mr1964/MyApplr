package com.mr1964.messages;

import java.text.SimpleDateFormat;
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

public class Send {

	//private String url = "http://192.168.1.103:8080/Applr0504/message/MessageAction";
	//private String url = "http://mr1964.eicp.net:8080/Applr0504/message/MessageAction";
	
	MyURL myURL = new MyURL();			
	String url = myURL.port + "Applr0504/message/MessageAction";
	
	public Boolean sendAction(String user_id, String user_name, String message_content){		
		try{			
			HttpPost httpRequest  = new HttpPost(url);
			List<NameValuePair> params=new ArrayList<NameValuePair>();
									
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String message_date = date.format(new java.util.Date());
			
			params.add(new BasicNameValuePair("message_date", message_date));
			params.add(new BasicNameValuePair("user_id",user_id));
			params.add(new BasicNameValuePair("user_name", user_name));
			params.add(new BasicNameValuePair("message_content",message_content));
			System.out.println("message_content: " + message_content);
			
			
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
				String flag = EntityUtils.toString(httpResponse
						.getEntity());	
				if(!flag.isEmpty()){
					return true;
				}

				System.out.println("!flag.isEmpty() = " + !flag.isEmpty());
				//textView.setText(strResult);
			}
		}catch(Exception e){
			return false;} 
		return false;
	}
	
}
