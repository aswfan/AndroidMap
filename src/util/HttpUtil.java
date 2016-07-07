package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;  
import java.util.List;  
import java.util.Map;  
  
import org.apache.http.HttpResponse;  
import org.apache.http.NameValuePair;  
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;  
import org.apache.http.client.entity.UrlEncodedFormEntity;  
import org.apache.http.client.methods.HttpGet;  
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;  
import org.apache.http.message.BasicNameValuePair;  

import org.apache.http.util.EntityUtils;  
  
import android.content.Context;
 
  
public class HttpUtil {  

	public static final String BASE_URL = "http:/127.0.0.1:8080/androidweb/servlet/";
	static InputStream inputStream;
	public static HttpClient httpClient = new DefaultHttpClient();
	/**
	 * 
	 * @param context TODO
	 * @param url 发送请求的URL
	 * @param //params 请求参数
	 * @return 服务器响应字符串
	 * @throws Exception
	 */
	public static String postRequest(Context context
			, String url, Map<String ,String> rawParams)throws Exception
 {
		url = BASE_URL + url;
		// URL 指的想请求的服务器端的所对应的Servlet
		// ，例如http://192.168.21.41:8080/androidweb/LoginServlet
		HttpPost request = HttpUtil.getHttpPost(url);
		// 遍历Map中的中的参数，全部转化成NameValuePair类型，并放在List集合中
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (String key : rawParams.keySet()) {
			params.add(new BasicNameValuePair(key, rawParams.get(key)));
		}
		// 把List<NameValuePair>所存储的参数放在HTTPPost 对象中
		request.setEntity(new UrlEncodedFormEntity(params, "utf_8"));
		// 创建DefaultHttpClient 的对象，
		DefaultHttpClient client = new DefaultHttpClient();
		// 在request对象中存储了URL以及要传送到后台去的can
		HttpResponse httpResponse = client.execute(request);
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(httpResponse.getEntity());
			return result;
		}
		return null;
	}

	public static HttpPost getHttpPost(String url){
		 HttpPost request = new HttpPost(url);
		 return request;
	}
	
	public static String getRequest(String url) throws Exception {
		HttpGet get = new HttpGet(url);
		HttpResponse httpResponse = httpClient.execute(get);
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(httpResponse.getEntity());
			return result;
		}
		return null;
	}
	
	public static HttpResponse getHttpResponse(HttpPost request) throws ClientProtocolException, IOException{
		HttpResponse response = new DefaultHttpClient().execute(request);
		return response;
	}
	
	public static String convertResponseToString(HttpResponse response) throws IllegalStateException, IOException{
		  
        String res = "";
        StringBuffer buffer = new StringBuffer();
        inputStream = response.getEntity().getContent();
        int contentLength = (int) response.getEntity().getContentLength(); //getting content length
        if (contentLength < 0){
        }
        else{
           byte[] data = new byte[512];
           int len = 0;
           try
           {
               while (-1 != (len = inputStream.read(data)) )
               {
                   buffer.append(new String(data, 0, len)); 
               }
           }
           catch (IOException e)
           {
               e.printStackTrace();
           }
           try
           {
               inputStream.close(); 
           }
           catch (IOException e)
           {
               e.printStackTrace();
           }
           res = buffer.toString();     
           //System.out.println("Response => " +  EntityUtils.toString(response.getEntity()));
        }
        return res;
   }

}  
