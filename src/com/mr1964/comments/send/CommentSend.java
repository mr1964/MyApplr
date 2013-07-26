package com.mr1964.comments.send;

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

public class CommentSend {
	public String result;
	public String DataAction(String comment_content,String commentator_id,
			String commentator_name,String message_id){		
		try{
			//String sql = "select * from messages where message_id =" + message_id ;		
			//String url = "http://mr1964.eicp.net:8080/Applr0504/comment/send/CommentSendAction";
			
			MyURL myURL = new MyURL();			
			String url = myURL.port + "Applr0504/comment/send/CommentSendAction";
						
			//String url = "http://192.168.1.103:8080/Applr0504/comment/send/CommentSendAction";
			HttpPost httpRequest  = new HttpPost(url);
			
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String comment_date = date.format(new java.util.Date());
			
			List<NameValuePair> params=new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("comment_content",comment_content));
			params.add(new BasicNameValuePair("comment_date",comment_date));
			params.add(new BasicNameValuePair("commentator_id",commentator_id));
			params.add(new BasicNameValuePair("message_id",message_id));
			params.add(new BasicNameValuePair("commentator_name",commentator_name));
			
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
				System.out.println(" result = " + result);
			}
		}catch(Exception e){e.printStackTrace();}
		
	return result.trim();
	}
	
}
