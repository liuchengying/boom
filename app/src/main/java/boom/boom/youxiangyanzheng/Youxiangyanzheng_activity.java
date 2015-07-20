package boom.boom.youxiangyanzheng;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashSet;

import boom.boom.R;
import boom.boom.api.HttpIO;
import boom.boom.api.Utils;

/**
 * Created by Administrator on 2015/7/18.
 */
public class Youxiangyanzheng_activity extends Activity {
    EditText email;
    EditText vcode;
    Button get_vcode;
    Button commit;
    String email_addr;
    RelativeLayout fh;
    Handler getvcodeHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == 1){
                String Data = msg.getData().getString("data");
                try{
                    JSONObject obj = new JSONObject(Data);
                    if(obj.getString("state").equals("SUCCESS")){
                        Toast.makeText(Youxiangyanzheng_activity.this,"获取验证码成功！请前往邮箱查看",Toast.LENGTH_SHORT).show();
                    }else {
                        if(obj.getString("reason").equals("MAIL_POST_ERROR")) {
                            Toast.makeText(Youxiangyanzheng_activity.this, "获取验证码失败！请输入正确的邮箱！", Toast.LENGTH_SHORT).show();
                        }else if (obj.getString("reason").equals("MAIL_ADDR_ALREADY_USED")){
                            Toast.makeText(Youxiangyanzheng_activity.this,"邮箱地址已被使用！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(Youxiangyanzheng_activity.this,"网络连接错误！",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Youxiangyanzheng_activity.this,"保存成功！", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra("data",email_addr);
                        setResult(500, intent);
                        finish();
                    }else {
                        if (obj.getString("reason").equals("VCODE_NOT_EQUAL")){
                            Toast.makeText(Youxiangyanzheng_activity.this,"验证码错误！",Toast.LENGTH_SHORT).show();
                        }else if (obj.getString("reason").equals("CURRENT_MAIL_DID_NOT_APPLY")){
                            Toast.makeText(Youxiangyanzheng_activity.this,"请先获取验证码！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(Youxiangyanzheng_activity.this,"网络连接错误！",Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youxiang_yanzheng);
        fh = (RelativeLayout) findViewById(R.id.mmzh_emill_fh);
        fh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        email = (EditText) findViewById(R.id.yxyz_addr);
        vcode = (EditText) findViewById(R.id.zhmm_emill_yanzhengma);
        get_vcode = (Button) findViewById(R.id.get_vcode);
        commit = (Button) findViewById(R.id.commit);
        get_vcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_addr = email.getText().toString();
                if(!email_addr.equals("")) {
                    HttpIO.GetHttpEX(Youxiangyanzheng_activity.this, getvcodeHandler, Utils.serveraddr + "/api/mail.php?action=mail_order&mail_addr=" + email_addr);
                }else {
                    Toast.makeText(Youxiangyanzheng_activity.this,"请输入邮箱！",Toast.LENGTH_SHORT).show();
                }
            }
        });
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strVcode = vcode.getText().toString();
                if(!strVcode.equals("")){
                    HttpIO.GetHttpEX(Youxiangyanzheng_activity.this,commitHandler,Utils.serveraddr + "/api/mail.php?action=mail_verify&mail_addr=" + email_addr + "&vcode=" + strVcode);
                }else {
                    Toast.makeText(Youxiangyanzheng_activity.this,"请输入验证码！",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
