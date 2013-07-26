package com.mr1964.login;

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

public class Login {

	//private String url = "http://192.168.1.103:8080/Applr0504/login/LoginAction";
	
	//private String url = "http://mr1964.eicp.net:8080/Applr0504/login/LoginAction";
	
	MyURL myURL = new MyURL();			
	String url = myURL.port + "Applr0504/login/LoginAction";
	
	public String loginAction(String username, String password){
		String SERVER_ERROR = "2";
		String feedback = SERVER_ERROR;
		try{			
			HttpPost httpRequest  = new HttpPost(url);
			List<NameValuePair> params=new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username",username));//�û���
			params.add(new BasicNameValuePair("password",password));//����
			
			System.out.println("ACTION--username" + username);
			System.out.println("ACTION--password" + password);
					
			httpRequest.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8)); 
			//��ʱ���õĹؼ�����
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 3000;
			HttpConnectionParams.setConnectionTimeout(
					httpParameters, timeoutConnection);
			int timeoutSocket = 5000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

			DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			
			if (httpResponse.getStatusLine().getStatusCode()==200) {  
				feedback = EntityUtils.toString(httpResponse
						.getEntity());
				System.out.println("feedback = " + feedback);
				System.out.println("feedback�ĳ��� = " + feedback.length());
				
			}
			
		}catch(Exception e){} 
		
		//���������ص�ֵ���������ո񣬲�֪��Ϊʲô����trim()����ȥ���ո�
		return feedback.trim();
	}
	
}
