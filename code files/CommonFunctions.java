package com.jsonfiles;

import android.annotation.SuppressLint;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

@SuppressLint("SimpleDateFormat")
public class CommonFunctions{
	   Boolean isInternetpresent=false;
	   int timeout=20000;
	   int timeout1=40000;
	public InputStream connectionEstablished(String mUrl,List<NameValuePair> nameValuePairs,MultipartEntityBuilder reqEntity) {
		InputStream mInputStreamis = null;
		
		HttpClient client = new DefaultHttpClient();
		
		HttpConnectionParams.setConnectionTimeout(client.getParams(), timeout); // Timeout Limit
		HttpConnectionParams.setSoTimeout(client.getParams(), timeout1);
		HttpResponse response;
		HttpEntity entity;
		try {
	
			HttpPost post = new HttpPost(mUrl);
//			HttpGet post=new HttpGet(mUrl);

		if(nameValuePairs!=null){
			try {
				post.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
				for (int i = 0; i < nameValuePairs.size(); i++) {
					Log.e("NameValuePairs", "PArams "+nameValuePairs.get(i).toString());

				}
				
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}else
		if(reqEntity!=null){
			entity=reqEntity.build();
			post.setEntity(entity);
			java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream((int)entity.getContentLength());
			entity.writeTo(out);
		   
		     // or convert to string
		     String entityContentAsString = new String(out.toByteArray());
		     Log.e("value are---->", " " + entityContentAsString);
		}
		
			response = client.execute(post);
			/* Checking response */
			if (response != null) {
				mInputStreamis = response.getEntity().getContent(); // Get the entity.

			}

		}

		catch (Exception e) {
			
			Log.e("Caught in exception", "Error  ===== " + e.toString());
			
		}

		return mInputStreamis;
	}

	public String converResponseToString(InputStream InputStream) {
		String mResult = "";
		StringBuilder mStringBuilder;

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					InputStream, "UTF-8"), 8);
			mStringBuilder = new StringBuilder();
			mStringBuilder.append(reader.readLine() + "\n");
			String line = "0";
			while ((line = reader.readLine()) != null) {
				mStringBuilder.append(line + "\n");
			}
			InputStream.close();
			mResult = mStringBuilder.toString();

			return mResult;
		} catch (Exception e) {
		
			return mResult;
		}
	}
}
