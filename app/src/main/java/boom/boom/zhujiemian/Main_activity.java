package boom.boom.zhujiemian;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import boom.boom.R;
import boom.boom.api.User;
import boom.boom.api.Utils;
import boom.boom.denglu.dengluzhuce_activity;
import boom.boom.tianzhan.Tiaozhan_activity;

/**
 * Created by 刘成英 on 2015/1/13.
 */
public class Main_activity extends Activity {
    private Button denglu;
    private TextView zhucezhanghao;
    private EditText user;
    private EditText pass;
    protected String passhash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhujiemian);
        denglu = (Button) findViewById(R.id.denglu);
        zhucezhanghao =(TextView) findViewById(R.id.zhucezhanghao);
        user = (EditText)findViewById(R.id.yonghuming);
        pass = (EditText) findViewById(R.id.mima);
        denglu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User userlogin = (User)getApplication();
                userlogin.loginUser(user.toString(), pass.toString());
                if (userlogin.ifLoggedIn()) {
                    Intent intent = new Intent();
                    intent.setClass(Main_activity.this, Tiaozhan_activity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "无法登陆到服务器！错误信息：" + userlogin.getServerErr(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        zhucezhanghao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Main_activity.this,Tiaozhan_activity.class);
                startActivity(intent);

            }
        });

    }
}
