package boom.boom.zhuceyanzheng;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.HttpIO;
import boom.boom.api.Static;
import boom.boom.api.SysApplication;
import boom.boom.api.Utils;
import boom.boom.myview.SildingFinishLayout;
import boom.boom.shezhimima.Shezhimima_activity;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by 刘成英 on 2015/3/12.
 */
public class Zhuceyanzheng_activity extends Activity {
    EditText phonenumber;
    EditText verifiedcode;
    Button getcode;
    Button commit;
    int second = 60;
    Handler commitHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == 1) {
                String Data = msg.getData().getString("data");
                Toast.makeText(Zhuceyanzheng_activity.this, Data, Toast.LENGTH_SHORT).show();
                try{
                    JSONObject obj = new JSONObject(Data);
                    String state = obj.getString("state");
                    if(state.equals("SUCCESS")){
                        String identifyDigit = obj.getString("identifyDigit");
                        Intent intent = new Intent();
                        intent.setClass(Zhuceyanzheng_activity.this, Shezhimima_activity.class);
                        intent.putExtra("identifyDigit", identifyDigit);
                        startActivity(intent);
                    }else {
                        String reson = obj.getString("reson");
                        if(reson.equals("NOT_VALID_SMS_CODE")){
                            Toast.makeText(Zhuceyanzheng_activity.this,"验证码不正确!",Toast.LENGTH_SHORT).show();
                        }else if (reson.equals("REQUEST_TOO_FREQUENTLY")){
                            Toast.makeText(Zhuceyanzheng_activity.this,"请求过于频繁!",Toast.LENGTH_SHORT).show();
                        }else if (reson.equals("MOBILE_NO_NOT_VALID")){
                            Toast.makeText(Zhuceyanzheng_activity.this,"手机号不正确!",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(Zhuceyanzheng_activity.this,"未知错误!",Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else {

            }
            return true;
        }
    });
    Handler TimerHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            getcode.setText(second + "秒");
            second--;
                TimerHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getcode.setText(second + "秒");
                        if(second == 0) {
                            getcode.setText("获取验证码");
                            getcode.setEnabled(true);
                            second = 60;
                        }else{
                            second--;
                            TimerHandler.postDelayed(this,1000);
                        }
                    }
                }, 1000);

            return true;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.shoujizhuce);
        RelativeLayout zhucefanhui=(RelativeLayout)findViewById(R.id.shezhifanhui);
        zhucefanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, R.anim.base_slide_right_out);
            }
        });
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        phonenumber = (EditText) findViewById(R.id.sjzc_phonenumber);
        verifiedcode = (EditText) findViewById(R.id.sjzc_verifiedcode);
        getcode = (Button) findViewById(R.id.sjzc_getcode);
        commit = (Button) findViewById(R.id.sjzc_commit);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!phonenumber.getText().equals("")&&!verifiedcode.getText().equals("")){
                    String phone = phonenumber.getText().toString();
                    String code = verifiedcode.getText().toString();
                    HttpIO.GetHttpEX(Zhuceyanzheng_activity.this,commitHandler, Utils.serveraddr+"/api/userRegister.php?action=smsVerify&mobile="+phone+"&smsCode="+code);
                }
            }
        });
        getcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = phonenumber.getText().toString();
                EventHandler eh=new EventHandler(){
                    @Override
                    public void afterEvent(int event, int result, Object data) {
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            //回调完成
                            if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                                //提交验证码成功
                            }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                                //获取验证码成功
                            }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                                //返回支持发送验证码的国家列表
                            }
                        }else{
                            ((Throwable)data).printStackTrace();
                        }
                    }
                };
                SMSSDK.registerEventHandler(eh); //注册短信回调
                SMSSDK.getVerificationCode("86", phone);
                TimerHandler.sendEmptyMessage(0);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getcode.setEnabled(false);
                    }
                });



            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }
}
