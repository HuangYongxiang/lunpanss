package com.example.xianlong.lunpan.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.example.xianlong.lunpan.R;
import com.example.xianlong.lunpan.context.Constants;
import com.example.xianlong.lunpan.util.ToastUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{
	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entry);
		// 通过WXAPIFactory工厂，获取IWXAPI的实例
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
		api.registerApp(Constants.APP_ID);
		//注意：
		//第三方开发者如果使用透明界面来实现WXEntryActivity，需要判断handleIntent的返回值，如果返回值为false，则说明入参不合法未被SDK处理，应finish当前透明界面，避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
		try {
			api.handleIntent(getIntent(), this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	// 微信发送请求到第三方应用时，会回调到该方法
	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
			case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
				finish();
				break;
			case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
				finish();
				break;
			default:
				break;
		}
	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp resp) {
		String result = "";
		switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				result = "分享成功";
				ToastUtil.show(this,result);
				finish();
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				result ="分享取消";
				ToastUtil.show(this,result);
				finish();
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				result = "分享被拒绝";
				ToastUtil.show(this,result);
				finish();
				break;
			case BaseResp.ErrCode.ERR_UNSUPPORT:
				result = "不支持错误";
				ToastUtil.show(this,result);
				finish();
				break;
			default:
				result = "分享返回";
				ToastUtil.show(this,result);
				finish();
				break;
		}
	}
}