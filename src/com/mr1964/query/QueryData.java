/*
 * ���ڲ�ѯ���ݵ��࣬int i ��ʾ �ӵ�i����¼��ʼȡ10����¼
 * ����������һ��json��ʽ��String������������logcat�￴�����
*/



package com.mr1964.query;

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

public class QueryData {
//	private String ERROR = "0";
	public String result = "00";
	public String QueryDataAction(int i,String keyword){		
		try{
			
			
			String sql = "select * from messages where message_content" +
					" like '%"+ keyword + "%' " +
							"order by message_id desc limit "+ i +",10";
			
			MyURL myURL = new MyURL();			
			String url = myURL.port + "Applr0504/paging/PagingAction";
			//String url = "http://192.168.1.103:8080/Applr0504/paging/PagingAction";
			//String url = "http://mr1964.eicp.net:8080/Applr0504/paging/PagingAction";

			
			HttpPost httpRequest  = new HttpPost(url);
			List<NameValuePair> params=new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("sql",sql));
			
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
				result = EntityUtils.toString(httpResponse
						.getEntity());
			}
		}catch(Exception e){e.printStackTrace();}
		//��ʵ�����������ݷ���ʱ�����صĵ�ֵΪ��[],result��ʵ����[]����ʾ�յ�JSON
		//��Ϊ��������json�͵����ݷ��أ�json��Ϊ[.....�м�����......]������Ϊ��ʱ�������[]
	return result.trim();
	}
	
}
