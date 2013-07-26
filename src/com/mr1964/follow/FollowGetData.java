package com.mr1964.follow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
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

public class FollowGetData {
	
	private String result = "00";
	
	public String FollowGetAction(int i) throws 
			ClientProtocolException, IOException{
//		String sql = "";
		MyURL myURL = new MyURL();
		String url = myURL.port + "Applr0504/userlist/UserAction";
		String sql = "select * from userinfo order by user_id desc";
//		String sql = "select * from userinfo order by user_id desc limit "+ i +",10";
		
		HttpPost httpRequest =  new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("sql",sql));
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
		
		if(httpResponse.getStatusLine().getStatusCode()==200){
			result = EntityUtils.toString(httpResponse.getEntity());
			if(!result.isEmpty()){
				return result;
			}
		}
		return result;
		
		
	}

}
