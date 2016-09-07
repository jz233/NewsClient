package zjj.app.newsclient.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import zjj.app.newsclient.BuildConfig;
import zjj.app.newsclient.R;
import zjj.app.newsclient.base.AppController;
import zjj.app.newsclient.base.BaseActivity;
import zjj.app.newsclient.domain.Channels;
import zjj.app.newsclient.utils.SharedPreferencesUtils;

public class SplashActivity extends BaseActivity {

    private static final int ENTER_MAIN = 0;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == ENTER_MAIN){
                finish();
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            }
        }
    };
    private long startTime;

    @Override
    public void initView() {
        startTime = System.currentTimeMillis();
        //通过设置主题实现splash界面，效率高，没有必要通过布局实现界面
//        setContentView(R.layout.activity_splash);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        AppController.getInstance().doUpdateChannelsRequest(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "网络出错，无法更新栏目列表", Toast.LENGTH_LONG).show();
                    }
                });
                enterMainUI();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String responseBody = response.body().string();
                if (BuildConfig.DEBUG)
                    Log.d("AppController", "response:" + responseBody);

                Channels channels = new Gson().fromJson(responseBody, Channels.class);
                List<Channels.ShowapiResBodyBean.ChannelListBean> list = channels.getShowapi_res_body().getChannelList();
                SharedPreferences.Editor editor = SharedPreferencesUtils.getEditor(getApplicationContext());
                for(Channels.ShowapiResBodyBean.ChannelListBean item : list){
                    editor = editor.putString(item.getName(), item.getChannelId());
                }
                editor.apply();

                enterMainUI();
            }
        });
    }

    private void enterMainUI() {
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        if(duration < 3000){
            try {
                Thread.sleep(3000-duration);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        handler.sendEmptyMessage(ENTER_MAIN);
    }
}
