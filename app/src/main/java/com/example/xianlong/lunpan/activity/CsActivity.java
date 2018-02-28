package com.example.xianlong.lunpan.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.xianlong.lunpan.R;
import com.example.xianlong.lunpan.adapter.GgAdapter;
import com.example.xianlong.lunpan.bean.BeanResult;
import com.example.xianlong.lunpan.bean.Ggitem;
import com.example.xianlong.lunpan.bean.JsonGenericsSerializator;
import com.example.xianlong.lunpan.context.Constants;
import com.example.xianlong.lunpan.util.ToastUtil;
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

public class CsActivity extends BaseActivity {

    ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gg);

        intview();
        intdata();
    }
    List<Ggitem> list=new ArrayList<>();
    protected void intview(){

        listview = (ListView)findViewById(R.id.listview);

    }
    GgAdapter ggAdapter;
    protected  void intdata(){

        Map<String, String> params = new HashMap<>();
        String url= "https://lf.snssdk.com/api/ad/union/show_event/?req_id=4f8879e00019449c383adbbad086ee96u1356&extra=OPH3jglvrqkjOwlZXDkEwKMdvynQ4XsWIRT9gbENAGOgngKZkmJJMPE0I1zrSMtAT2PPp3FUTfLxsazRfRcQdZw6MS8NXIM4GiImF4FVgGjqKbSRysudA0ZiJWuCqF6glF%2B\n" +
                "FoKIr46BsGlkntOxr%2BgKlJetZewGuWNIaTEv35%2B6dyM26iGzQSD1JvUoJokagDelDK9u1Nfo4kkkvNb6SUjdPuYIjcRr%2BFeTsnuSRBEn%2FhblXDYoRUWJfDdtuqvNxyjQ9tJDqk%2By7vvZsTiLCZustuRD07Q9wvxwPK3o4nfJfqnPKVhWLLDu2Vb1p43gip47sQQ05P9HIHyzmS%2B5b13vCbJByep2\n" +
                "Sx2re3ywjtKyKpBww5%2Fx%2Fbi2%2BYx0IptZdkpBc13rvALkeW57Fa6sWRdk7n2G%2BsGuzE865jOfRbupSvbpDptrsNhEWDQgCtqf2yQVwggSdDaRSt5qXZgzJajgfKqY%2FvqjdSzxe4WEj3nF0PeSIBwwEsz6sK3q371EhC5gUf5NHSxTOr15Y%2F3EjQvUqCSXogTVh%2BO4yyond7rADkTvUkPcXUQ6%2F\n" +
                "Sddu8lzzV29ntW%2BCd962YIEk6D8qC4AxYJfcRUyI5i5zhPMmQh%2F1gtkRbanfcG8myHxiPgIQbVynvRXe%2BwUm1xY2hsWNPBtbQMOSU6aiLFLODa01H88rwz30Qa24xYrRE5m1c6YKm2kBDY3OiJGdOLLVzl2nJIxgBOOP5Alk9MgW93U0koY3xckLVOcb90eaZgBAAHNK3CiXW1guvbQgURVewX%2Foew1aFE\n" +
                "fxDiueZOHZ1LIJFYIFRbqTV1FXaLRn%2BqavtV8x5a9STzGBIo4wIes2NQCTGgDjeS6Cbys9t5Q1xhducX6f862bBF249aI%2Fhs56U4byZWgITqlj%2BTm6rRJQd6%2F3v5%2F30HDEhthB9JFf6Lu5OPkHWzXK9vDREVfT8cQ8Cv8uLn0Ze9pMb%2FUQCChHsqvgCKPkgOcC9JSiovI0zDAmEWxEeNqy7r08SBp8Q\n" +
                "%2Fnb2LSSo45L807M7DTbK7y9BeLR1sIQg3GRIfmZhAy0JnfzoucMyzXbovdtz7iEYeZ6pHaItVlJAQxG%2FKAhnuxNebbzZXTxm5AV42C%2BrzltAC9fHedsfSF9X7SQqOHGHyUmL9Ni&source_type=1&pack_time=1516868131.39&tm=1516868166743";
        showLoadingDialog("请求中....");
        OkHttpUtils.get()
                .params(params)
                .url(url)
                .build()
                .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator())
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        dismissLoadingDialog();
                        ToastUtil.show(CsActivity.this,"网络异常");
                    }

                    @Override
                    public void onResponse(final BeanResult response, int id)
                    {
                        dismissLoadingDialog();
                        ToastUtil.show(CsActivity.this,"成功");
                    }
                });
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
