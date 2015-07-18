package boom.boom.mimazhaohui_phone;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.HttpIO;
import boom.boom.api.SysApplication;
import boom.boom.api.Utils;
import boom.boom.myview.SildingFinishLayout;
import boom.boom.shezhimima.Shezhimima_activity;
import boom.boom.zhujiemian.Main_activity;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static boom.boom.R.id.zhaohuimima_commit;

/**
 * Created by 刘成英 on 2015/3/12.
 */
public class Mimazhaohui_phone_activity extends Activity {
    private RelativeLayout mmzh_phone_fh;
    private EditText phonenumber;
    private EditText phone_yanzheng;
    private Button yanzheng_btn;
    private EditText newpassword;
    private Button commit;
    int second = 60;
Handler commitHandler = new Handler(new Handler.Callback() {
    @Override
    public boolean handleMessage(Message msg) {
        if(msg.what == 1){
            String Data = msg.getData().getString("data");
            Toast.makeText(Mimazhaohui_phone_activity.this, Data, Toast.LENGTH_SHORT).show();
            try {
                JSONObject obj = new JSONObject(Data);
                String state = obj.getString("state");
                if(state.equals("SUCCESS")){
                    AlertDialog alertDialog=new AlertDialog.Builder(Mimazhaohui_phone_activity.this).create();
                    alertDialog.show();
                    alertDialog.setCancelable(false);
                    Window window=alertDialog.getWindow();
                    window.setContentView(R.layout.mbox_ok);
                    TextView ok_title=(TextView)window.findViewById(R.id.ok_title);
                    TextView ok_text=(TextView)window.findViewById(R.id.ok_text);
                    //ok_title.setText("修改密码成功！");
                    ok_text.setText("修改密码成功！");
                    Button ok=(Button)window.findViewById(R.id.ok_button);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           /* Intent i = getBaseContext().getPackageManager()
                                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            //重启应用程序*/
                            Intent intent = new Intent();
                            intent.setClass(Mimazhaohui_phone_activity.this, Main_activity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }else {
                    String reason = obj.getString("reason");
                    if (reason == " NOT_VALID_SMS_CODE"){
                        Toast.makeText(Mimazhaohui_phone_activity.this,"验证码不正确!",Toast.LENGTH_SHORT).show();
                    }else if (reason == "REQUEST_TOO_FREQUENTLY"){
                        Toast.makeText(Mimazhaohui_phone_activity.this,"请求过于频繁!",Toast.LENGTH_SHORT).show();
                    }else if (reason == "MOBILE_NO_NOT_VALID"){
                        Toast.makeText(Mimazhaohui_phone_activity.this,"手机号码不对!",Toast.LENGTH_SHORT).show();
                    }

                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            Toast.makeText(Mimazhaohui_phone_activity.this,"网络连接错误，请稍后再试",Toast.LENGTH_SHORT).show();
        }
        return true;
    }
});
    Handler TimerHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            yanzheng_btn.setText(second + "秒");
            second--;
            TimerHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    yanzheng_btn.setText(second + "秒");
                    if(second == 0) {
                        yanzheng_btn.setText("获取验证码");
                        yanzheng_btn.setEnabled(true);
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
        setContentView(R.layout.mimazhaohui_shouji);
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        phonenumber = (EditText) findViewById(R.id.zhuceyonghuming);
        phone_yanzheng = (EditText) findViewById(R.id.zhmm_yanzhengma);
        yanzheng_btn = (Button) findViewById(R.id.yanzheng_btn);
        newpassword = (EditText) findViewById(R.id.newpass);
        commit = (Button) findViewById(zhaohuimima_commit);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!phonenumber.getText().equals("") && !phone_yanzheng.getText().equals("")) {
                    String mobile = phonenumber.getText().toString();
                    String smsCode = phone_yanzheng.getText().toString();
                    String passhash = newpassword.getText().toString();

                    HttpIO.GetHttpEX(Mimazhaohui_phone_activity.this, commitHandler, Utils.serveraddr + "/api/userRegister.php?action=forgotPass&mobile=" + mobile + "&smsCode=" + smsCode + "&passhash=" + Utils.StrToMD5(passhash));
                }
            }
        });
        mmzh_phone_fh = (RelativeLayout) findViewById(R.id.mmzh_phone_fh);
        mmzh_phone_fh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.base_slide_remain, R.anim.base_slide_right_out);
            }
        });
        yanzheng_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = phonenumber.getText().toString();
                EventHandler eh = new EventHandler() {
                    @Override
                    public void afterEvent(int event, int result, Object data) {
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            //回调完成
                            if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                                //提交验证码成功
                            } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                                //获取验证码成功
                            } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                                //返回支持发送验证码的国家列表
                            }
                        } else {
                            ((Throwable) data).printStackTrace();
                        }
                    }
                };
                SMSSDK.registerEventHandler(eh); //注册短信回调
                SMSSDK.getVerificationCode("86", phone);
                TimerHandler.sendEmptyMessage(0);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        yanzheng_btn.setEnabled(false);
                    }

                });
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.base_slide_remain, R.anim.base_slide_right_out);
    }
}
