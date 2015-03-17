package boom.boom.zhujiemian;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Typeface;
import org.json.JSONObject;

import boom.boom.R;
import boom.boom.api.Static;
import boom.boom.api.User;
import boom.boom.api.UserData;
import boom.boom.tianzhan.Tiaozhan_activity;
import boom.boom.denglu.*;
import boom.boom.FontManager.FontManager;
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
        FontManager.changeFonts(FontManager.getContentView(this),this);//字体

        denglu = (Button) findViewById(R.id.denglu);
        zhucezhanghao =(TextView) findViewById(R.id.zhucezhanghao);
        user = (EditText)findViewById(R.id.yonghuming);
        pass = (EditText) findViewById(R.id.mima);

        denglu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_Str = user.getText().toString();
                String pass_Str = pass.getText().toString();
                User userlogin = new User(user_Str, pass_Str);
                if (userlogin.ifLoggedIn()) {
                    UserData data = new UserData(userlogin.getSessionId());
                    Intent intent = new Intent();
//                    intent.putExtra("session_id", userlogin.getSessionId());
//                    intent.putExtra("name", data.QueryData("name"));
//                    intent.putExtra("nickname", data.QueryData("nickname"));
//                    intent.putExtra("uniquesign", data.QueryData("uniquesign"));
//                    intent.putExtra("coins", data.QueryData("coins"));
                    Static.session_id = userlogin.getSessionId();
                    Static.username = data.QueryData("name");
                    Static.nickname = data.QueryData("nickname");
                    Static.uniqueSign = data.QueryData("uniquesign");
                    if (String.valueOf(data.QueryData("coins")) == "null"){
                        Static.coins = 0;
                    }else {
                        Static.coins = Integer.parseInt(String.valueOf(data.QueryData("coins")));
                    }
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
                intent.setClass(Main_activity.this,dengluzhuce_activity.class);
                startActivity(intent);

            }
        });

    }
}
