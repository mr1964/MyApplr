/*
 * 用于查询数据的类，int i 表示 从第i个记录开始取10个记录
 * 服务器传回一个json形式的String结果，你可以在logcat里看到结果
*/



package com.mr1964.paging;

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

public class PagingData {
//	private String ERROR = "0";
	public String result = "00";
	public String DataAction(int i){		
		try{
			
			
			String sql = "select * from messages order by " +
					"message_id desc limit "+ i +",10";
			
			MyURL myURL = new MyURL();			
			String url = myURL.port + "Applr0504/paging/PagingAction";
			

			
			HttpPost httpRequest  = new HttpPost(url);
			List<NameValuePair> params=new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("sql",sql));
			
			httpRequest.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8)); 
			//HttpResponse httpResponse=new DefaultHttpClient().execute(httpRequest);			

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
			}
		}catch(Exception e){
			
			}
		
		//其实服务器无数据返回时，返回的的值为：[],result其实等于[]，表示空的JSON
		//因为服务器以json型的数据返回，json型为[.....中间数据......]，数据为空时，则等于[]
	return result.trim();
	}
	
}
