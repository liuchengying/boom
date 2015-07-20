package boom.boom.mimazhaohui_emall;

import android.app.Activity;
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
import android.widget.Toast;

import org.json.JSONObject;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.HttpIO;
import boom.boom.api.SysApplication;
import boom.boom.api.Utils;
import boom.boom.myview.SildingFinishLayout;

/**
 * Created by 刘成英 on 2015/3/12.
 */
public class Mimazhaohui_emall_activity extends Activity {
    private RelativeLayout mmzh_emall_fh;
    EditText et_email;
    EditText et_vcode;
    Button bt_getvode;
    EditText et_newpass;
    Button bt_commit;
    String email_addr;
    Handler getvcodeHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == 1){
                String Data = msg.getData().getString("data");
                try{
                    JSONObject obj = new JSONObject(Data);
                    if(obj.getString("state").equals("SUCCESS")){
                        Toast.makeText(Mimazhaohui_emall_activity.this, "获取验证码成功！请前往邮箱查看", Toast.LENGTH_SHORT).show();
                    }else {
                        if(obj.getString("reason").equals("MAIL_POST_ERROR")) {
                            Toast.makeText(Mimazhaohui_emall_activity.this, "获取验证码失败！请输入正确的邮箱！", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(Mimazhaohui_emall_activity.this,"获取验证码失败！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(Mimazhaohui_emall_activity.this,"网络连接错误！",Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    });
    Handler commitHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == 1){
                String Data = msg.getData().getString("data");
                try{
                    JSONObject obj = new JSONObject(Data);
                    if(obj.getString("state").equals("SUCCESS")){
                        Toast.makeText(Mimazhaohui_emall_activity.this, "保存成功！",Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        if (obj.getString("reason").equals("VCODE_NOT_EQUAL")){
                            Toast.makeText(Mimazhaohui_emall_activity.this,"验证码错误！",Toast.LENGTH_SHORT).show();
                        }else if (obj.getString("reason").equals("CURRENT_MAIL_DID          _NOT_APPLY")){
                            Toast.makeText(Mimazhaohui_emall_activity.this,"请先获取验证码！",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(Mimazhaohui_emall_activity.this, "提交失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(Mimazhaohui_emall_activity.this,"网络连接错误！",Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mimazhaohui_youxiang);
        SildingFinishLayout mSildingFinishLayout = (SildingFinishLayout) findViewById(R.id.sildingFinishLayout);
        mSildingFinishLayout
                .setOnSildingFinishListener(new SildingFinishLayout.OnSildingFinishListener() {

                    @Override
                    public void onSildingFinish() {
                        Mimazhaohui_emall_activity.this.finish();
                    }
                });

        mSildingFinishLayout.setTouchView(mSildingFinishLayout);
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        mmzh_emall_fh = (RelativeLayout)findViewById(R.id.mmzh_emill_fh);
        mmzh_emall_fh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, R.anim.base_slide_right_out);
            }
        });
        et_email = (EditText) findViewById(R.id.mail_addr);
        et_vcode = (EditText) findViewById(R.id.zhmm_emill_yanzhengma);
        bt_getvode = (Button) findViewById(R.id.getvcode);
        bt_getvode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_addr = et_email.getText().toString();
                HttpIO.GetHttpEX(Mimazhaohui_emall_activity.this,getvcodeHandler, Utils.serveraddr + "/api/mail.php?action=mail_forgot_password&mail_addr=" + email_addr);
            }
        });
        et_newpass = (EditText) findViewById(R.id.zhucexinmima);
        bt_commit = (Button) findViewById(R.id.commit);
        bt_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vcode = et_vcode.getText().toString();
                String newpass = et_newpass.getText().toString();
                HttpIO.GetHttpEX(Mimazhaohui_emall_activity.this,commitHandler,Utils.serveraddr + "/api/mail.php?action=mail_verify_forgot_password&mail_addr=" + email_addr + "&vcode=" + vcode + "&newpass=" +Utils.StrToMD5(newpass));
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }
}
