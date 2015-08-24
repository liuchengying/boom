package boom.boom.shezhimima;

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

import boom.boom.R;
import boom.boom.api.HttpIO;
import boom.boom.api.Utils;
import boom.boom.myview.ShaderHelper;
import boom.boom.wanshanxinxi.Wanshanxinxi_activity;
import boom.boom.zhucechenggong.Zhucechenggong_activity;

/**
 * Created by Administrator on 2015/7/16.
 */
public class Shezhimima_activity extends Activity {
    private RelativeLayout shezhimimafanhui;
    private EditText password_one;
    private EditText password_two;
    private String password_1;
    private String password_2;
    private String identifyDigit;
    private Button commit;
    Handler commitHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == 1){
                String Data = msg.getData().getString("data");
                Toast.makeText(Shezhimima_activity.this,Data,Toast.LENGTH_SHORT).show();
                try{
                    JSONObject obj = new JSONObject(Data);
                    if(obj.getString("state").equals("SUCCESS")){
                        Intent intent = new Intent();
                        intent.setClass(Shezhimima_activity.this, Zhucechenggong_activity.class);
                        intent.putExtra("identifyDigit", identifyDigit);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(Shezhimima_activity.this,"服务器内部错误！",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }else {
                Toast.makeText(Shezhimima_activity.this,"网络连接超时，请稍后再试！",Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shezhimima);
        shezhimimafanhui = (RelativeLayout) findViewById(R.id.shezhifanhui);
        password_one = (EditText) findViewById(R.id.password_one);
        password_two = (EditText) findViewById(R.id.password_two);
        commit = (Button) findViewById(R.id.commit);
        shezhimimafanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        identifyDigit = getIntent().getStringExtra("identifyDigit");
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password_1 = password_one.getText().toString();
                password_2 = password_two.getText().toString();
                if(password_1.equals("")||password_2.equals("")){
                    Toast.makeText(Shezhimima_activity.this,"密码不能为空！",Toast.LENGTH_SHORT).show();
                }else {
                    if (password_1.equals(password_2)) {
                        HttpIO.GetHttpEX(Shezhimima_activity.this, commitHandler, Utils.serveraddr + "/api/userRegister.php?action=newuser&passhash=" + Utils.StrToMD5(password_1) + "&identify=" + identifyDigit);
                    } else {
                        Toast.makeText(Shezhimima_activity.this, "两次输入的密码不一致！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
