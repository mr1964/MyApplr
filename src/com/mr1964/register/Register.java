package com.mr1964.register;

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

public class Register {
	
	MyURL myURL = new MyURL();			
	String url = myURL.port + "Applr0504/register/RegisterAction";

	//private String url = "http://192.168.1.103:8080/Applr0504/register/RegisterAction";
	//private String url = "http://mr1964.eicp.net:8080/Applr0504/register/RegisterAction";
	
	public Boolean registerAction(String username, String password){		
		try{			
			HttpPost httpRequest  = new HttpPost(url);
			List<NameValuePair> params=new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_name",username));//用户名
			params.add(new BasicNameValuePair("user_password",password));//密码
			
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateTime = date.format(new java.util.Date());
			
			params.add(new BasicNameValuePair("register_date", dateTime));
			
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
