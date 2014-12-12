package com.vegansoft.vegemap.webview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.vegansoft.vegemap.R;

public class WebViewActivity extends Activity {
	private static final String TAG = WebViewActivity.class.getName();

	private WebView mWebView;
	private String mUrl;
	private String mTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);
		
		getActionBar().setDisplayHomeAsUpEnabled(true); //상단 ActionBar 왼쪽에 Home 으로 가는 back 버튼 활성화
		
		initView();
		initData();
	}
	
	private void initView() {
		mWebView = (WebView)findViewById(R.id.webview);
	}
	
	private void initData() {
		Intent intent = getIntent();
		mUrl = intent.getStringExtra("url");
		mTitle = intent.getStringExtra("title");
		getActionBar().setTitle(mTitle);
		
		Log.d(TAG, "Loading URL: " + mUrl);
		
//		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
	    
	    mWebView.loadUrl(mUrl);
	    // WebViewClient 지정
	    mWebView.setWebViewClient(new WebViewClientClass());  
	    
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) { 
            mWebView.goBack(); 
            return true; 
        } 
        return super.onKeyDown(keyCode, event);
    }
    
    private class WebViewClientClass extends WebViewClient { 
        @Override 
        public boolean shouldOverrideUrlLoading(WebView view, String url) { 
            view.loadUrl(url); 
            return true; 
        } 
    }

}
