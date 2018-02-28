package com.example.xianlong.lunpan.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.xianlong.lunpan.R;
import com.example.xianlong.lunpan.util.WebViewUtils;

public class AdDetailActivity extends BaseActivity {

	private ProgressBar progressBar;
	private WebView webView;
	private Intent intent;
	private LinearLayout _layout;
	private boolean is_forword;
     TextView titles;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ad_detail);
		_layout = (LinearLayout) findViewById(R.id.root_ll);

		webView = (WebView) findViewById(R.id.webView_info);
		titles = (TextView) findViewById(R.id.title);
		progressBar = (ProgressBar) findViewById(R.id.web_progressBar);
		intent = getIntent();
		Bundle bun = intent.getExtras();
		String url = bun.getString("url");
		String title = bun.getString("title");
		titles.setText(title);
		WebViewUtils.initWebViewUtils(webView, progressBar, AdDetailActivity.this).setWebView(url);
	}
	public static void startAdDetailActivity(Context context, String title, String url){
		Intent intent = new Intent(context,AdDetailActivity.class);
		intent.putExtra("url",url);
		intent.putExtra("title",title);
		context.startActivity(intent);
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(webView != null){
			_layout.removeView(webView);  
			webView.removeAllViews(); 
			webView.destroy();
			webView = null;
		}
		super.onDestroy();
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode  == KeyEvent.KEYCODE_BACK){
			if(webView != null){
				if(webView.canGoBack() && !is_forword){
					Intent intent = new Intent();
					setResult(RESULT_OK, intent);
					webView.goBack();
					return true;
				}
			}
			else{
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
				return true;
			}
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	protected void onPause() {
		super.onPause();
		//TCAgent.onPageEnd(this,"AdDetailActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		//TCAgent.onPageStart(this,"AdDetailActivity");
	}
}
