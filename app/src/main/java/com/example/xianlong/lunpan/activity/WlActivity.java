package com.example.xianlong.lunpan.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xianlong.lunpan.MainActivity;
import com.example.xianlong.lunpan.R;
import com.example.xianlong.lunpan.adapter.WlAdapter;
import com.example.xianlong.lunpan.adapter.WlAdapter2;
import com.example.xianlong.lunpan.bean.BeanResult;
import com.example.xianlong.lunpan.bean.JsonGenericsSerializator;
import com.example.xianlong.lunpan.bean.Kditem;
import com.example.xianlong.lunpan.bean.Kuaidi;
import com.example.xianlong.lunpan.bean.Neirong;
import com.example.xianlong.lunpan.bean.Wuliu;
import com.example.xianlong.lunpan.context.Constants;
import com.example.xianlong.lunpan.util.ToastUtil;
import com.example.xianlong.lunpan.util.ValueStorage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.GenericsCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class WlActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wl);

        intview();
        //indatass();
    }
    TextView wlname;
    EditText danhao;
    ListView listView;
    Button chaxun;
    protected void intview(){
        wlname=(TextView)findViewById(R.id.wuliu);
        danhao=(EditText)findViewById(R.id.danhao);
        listView=(ListView)findViewById(R.id.listview);
        chaxun=(Button)findViewById(R.id.chaxun);
        wlname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popwindow();
            }
        });
        chaxun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indatass();
            }
        });
        for(int i=0;i<name.length;i++){
            Wuliu wuliu=new Wuliu();
            wuliu.name=name[i];
            wuliu.type=type[i];
            lists.add(wuliu);
        }
    }
    WlAdapter wlAdapter;
    WlAdapter2 wlAdapter2;
    List<Kditem> list=new ArrayList<>();
    List<Wuliu> lists=new ArrayList<>();
    String types="";
    protected void indatass(){
        Map<String, String> params = new HashMap<>();
        String url= "http://kdwlcxf.market.alicloudapi.com/kdwlcx?"+"no="+danhao.getText().toString()+"&type="+types;
        //String url= "http://kdwlcxf.market.alicloudapi.com/kdwlcx?no=887736614584492661&type=YTO";
        params.put("no", "887736614584492661");
        params.put("type", "YTO");
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "APPCODE 2131d7e45b0c4bf582bbacd604a014db");
        showLoadingDialog("请求中....");
        OkHttpUtils.get()
                .headers(header)
                .url(url)
                .build()
                .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator())
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        dismissLoadingDialog();
                        ToastUtil.show(WlActivity.this,"网络异常");
                    }

                    @Override
                    public void onResponse(final BeanResult response, int id)
                    {
                        if (response.status.equals("0")) {
                            dismissLoadingDialog();
                            try {
                                ToastUtil.show(WlActivity.this, response.msg);
                                String object = new Gson().toJson(response);
                                JSONObject jsonObject = new JSONObject(object);
                                String dataJson = jsonObject.optString("result");
                                Type type = new TypeToken<Kuaidi>(){}.getType();
                                Kuaidi neirongs = new Gson().fromJson(dataJson, type);
                                list=neirongs.list;
                                wlAdapter=new WlAdapter(WlActivity.this,list);
                                listView.setAdapter(wlAdapter);
                                wlAdapter.notifyDataSetChanged();
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else{
                            dismissLoadingDialog();
                            ToastUtil.show(WlActivity.this,response.msg);
                        }
                    }
                });
    }

    protected void popwindow(){
        final PopupWindow popupWindow=new PopupWindow();
        View view = LayoutInflater.from(WlActivity.this).inflate(
                R.layout.pop_pays, null);
        ListView namelist=(ListView) view.findViewById(R.id.lists);
        wlAdapter2=new WlAdapter2(WlActivity.this,lists);
        namelist.setAdapter(wlAdapter2);
        wlAdapter2.notifyDataSetChanged();
        namelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                wlname.setText(lists.get(position).name);
                types=lists.get(position).type;
                popupWindow.dismiss();
            }
        });
        popupWindow.setContentView(view);
        // 设置视图
        // 设置弹出窗体的宽和高
        popupWindow.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        // 设置弹出窗体可点击
        popupWindow.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        popupWindow.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从底部向上弹出
        popupWindow.setAnimationStyle(R.style.ActionSheetDialogStyle);
        popupWindow.showAtLocation(listView, Gravity.BOTTOM, 0, 0);

    }
    private String[] name ={ "邮政包裹","顺丰","申通","圆通","韵达","德邦","宅急送","中通","中通快运","中邮","如风达","顺达快递","苏宁","运通","京东","百世快递","百世快运","天天","易通达","易达通","万象","速尔","中铁快运","中铁物流","中国东方"};
    private String[] type ={ "CHINAPOST","SFEXPRESS","STO","YTO","YUNDA","DEPPON","ZJS","ZTO","ZTO56","CNPL","RFD","SDEX","SUNING","YTEXPRESS","JD","HTKY","BSKY","TTKDEX","ETD","QEXPRESS","EWINSHINE","SURE","CRE","ZTKY","COE"};
}
