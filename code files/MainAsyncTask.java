package com.jsonfiles;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.InputStream;
import java.util.List;

public class MainAsyncTask extends AsyncTask<String, Void, String> {

	public static ProgressDialog mDialog;
	InputStream is = null;
	
	MainAsynListener<String> listener;
	int receivedId;
	int errorCode;
	boolean isDialogDisplay, isSuccess = false;
	Context context;
	public CommonFunctions sSetconnection;
	InputStream mInputStreamis = null;
	int checkResponse = 0;
	String url, dialog_text;
	List<NameValuePair> nameValuePairs;
	MultipartEntityBuilder reqEntitity;

	/**
	 * @param context
	 * @param url
	 * @param receivedId
	 * @param listener
	 * @param isDialogDisplay
	 * @param nameValuePairs
	 * @param reqEntity
	 * @param dialog_text
	 */
	public MainAsyncTask(Context context, String url, int receivedId,
			MainAsynListener<String> listener,boolean isDialogDisplay,List<NameValuePair> nameValuePairs,MultipartEntityBuilder reqEntity,String dialog_text) {
		
		this.context = context;
		this.url = url;
		this.receivedId = receivedId;
		this.listener = listener;
		this.isDialogDisplay = isDialogDisplay;
		this.nameValuePairs=nameValuePairs;
		this.dialog_text=dialog_text;
		this.reqEntitity=reqEntity;
	}

	@Override
	protected void onPreExecute() {
		sSetconnection = new CommonFunctions();
		if(isDialogDisplay){
			if(mDialog!=null){
			if (mDialog.isShowing()) {
				cancelDialog();
			
			}
			}
		showCommonDialog(context);
		}
	}

	@Override
	protected String doInBackground(String... arg0) {
		String mResult = null;

//		JSONObject json = new JSONObject();

		
		try {
			mInputStreamis = sSetconnection.connectionEstablished(url,nameValuePairs,reqEntitity);
			Log.e("onclick", " " +url );
			Log.e("input stream", " " + mInputStreamis);
			if (mInputStreamis != null) {
				mResult = sSetconnection.converResponseToString(mInputStreamis);
//				Log.e("Result for Activites is:", "" + mResult);

				isSuccess = true;
			} else {
				checkResponse = 2;
				Handler handler = new Handler(Looper.getMainLooper());

				handler.post(new Runnable() {

				        @Override
				        public void run() {
//				        	CommonUtilities.showMessage("Please Try Again..", context,Toast.LENGTH_SHORT);
				            Toast.makeText(context, "Please Try Again..", Toast.LENGTH_SHORT).show();
				        }
				    });     
           
			}

		} catch (Exception e) {
			checkResponse = 6;
		}
		return mResult;
	}

	/**
	 * activateDriverCheckResponse 1 = flag(Email does not Exist) 2 = Error with
	 * HTTP connection 3 = Error while convert into string 4 = Failure 5 = Email
	 * Already Exist
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	protected void onPostExecute(String _result) {
		try {
			if(isDialogDisplay){
				if (mDialog.isShowing()) {
					cancelDialog();
				}
				}
				if (isSuccess) {
					if(_result==null){                
					Handler handler = new Handler(Looper.getMainLooper());

					handler.post(new Runnable() {

					        @Override
					        public void run() {
//					        	CommonUtilities.showMessage("Please Try Again..", context,Toast.LENGTH_SHORT);
					            Toast.makeText(context, "Please Try Again..", Toast.LENGTH_SHORT).show();
					        }
					    }); 
					}
					Log.e("result><><><>",""+_result);
				listener.onPostSuccess(_result, receivedId, isSuccess);
				} else {
					listener.onPostError(receivedId, errorCode);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}


	// Common Progress bar
    public void showCommonDialog(Context mContext) {

    	mDialog = new ProgressDialog(mContext);
    	mDialog.setMessage(dialog_text);
    	mDialog.setIndeterminate(false);
    	mDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
    	mDialog.setCancelable(false);
    	mDialog.show();

    }

    @Override
    protected void onCancelled() {
    	// TODO Auto-generated method stub
    	super.onCancelled();
    	cancelDialog();
    }
    // cancel progress dialog

	public void cancelDialog() {
		try {
			if (mDialog != null) {
				if(mDialog.isShowing()){
				mDialog.cancel();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

