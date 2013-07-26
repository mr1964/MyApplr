package com.mr1964.comments.send;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.mr1964.utils.MyURL;

public class CommentDisplayData {
	public String result = "WRONG";

	public String DataAction(String message_id){		
		try{
			
			int i = 0;
			String begain = String.valueOf(i);
			String sql = "select * from comments where message_id =? order by comment_id desc limit "+ begain +",5";

			
			
			MyURL myURL = new MyURL();			
			String url = myURL.port + "Applr0504/comment/display/CommentListViewAction";
						

			
			
			HttpPost httpRequest  = new HttpPost(url);
			List<NameValuePair> params=new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("sql",sql));
			params.add(new BasicNameValuePair("message_id",message_id));
			
			System.out.println("CommentDisplayData�е� SQL = " + sql);
			
			httpRequest.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8)); 
			HttpResponse httpResponse=new DefaultHttpClient().execute(httpRequest);
			
			if (httpResponse.getStatusLine().getStatusCode()==200) {  
				result = EntityUtils.toString(httpResponse
						.getEntity());
				System.out.println("CommentDisplayData�е� result = " + result);
			}
		}catch(Exception e){e.printStackTrace();}
	//�������䷵����һ��null�����¿�ָ����������	
	return result.trim();
	
		//�˾�Ϊ������䣬�Ƿ���ֵ��Ϊnull������bug��
		//return "0123456789";
	}
	
}
