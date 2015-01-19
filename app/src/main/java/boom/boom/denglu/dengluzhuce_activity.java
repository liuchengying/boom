package boom.boom.denglu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import boom.boom.R;
import boom.boom.api.LoginUser;
import boom.boom.zhujiemian.Main_activity;

/**
 * Created by 刘成英 on 2015/1/13.
 */
public class dengluzhuce_activity extends Activity {
    private Button fanhui;
    private Button zhucetijiao;
    private EditText text_username;
    private EditText text_password;
    private EditText text_password2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhuce);
        zhucetijiao = (Button) findViewById(R.id.zhucetijiao);
        fanhui = (Button)findViewById(R.id.zhucufanhui);
        text_username = (EditText)findViewById(R.id.zhuceyonghuming);
        text_password = (EditText)findViewById(R.id.zhucemima);
        text_password2 = (EditText)findViewById(R.id.zhucemima);
        zhucetijiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text_password.toString() == text_password2.toString()) {
                    LoginUser user = new LoginUser(text_username.toString(), text_password.toString());
                    if (user.AttemptToRegisted()) {
                        Intent intent = new Intent();
                        intent.setClass(dengluzhuce_activity.this, Main_activity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "注册失败！服务器返回错误：" + user.GetLastError(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "两次用户名密码不相符。", Toast.LENGTH_SHORT).show();
                }
            }
        });
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent();
                 intent.setClass(dengluzhuce_activity.this,Main_activity.class);
                startActivity(intent);

            }
        });
    }
}
