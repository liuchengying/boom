package boom.boom.zhujiemian;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import boom.boom.R;

import boom.boom.api.Static;
import boom.boom.api.SysApplication;
import boom.boom.api.User;
import boom.boom.api.UserData;
import boom.boom.mimaxiugai.Mimaxiugai_activity;
import boom.boom.mimazhaohui.Mimazhaohui_activity;
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
    private TextView mmzh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhujiemian);
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this),this);//字体
        if (isNetworkAvailable(Main_activity.this))
        {
            Toast.makeText(getApplicationContext(), "当前有可用网络！", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "当前没有可用网络！", Toast.LENGTH_LONG).show();



        }

        denglu = (Button) findViewById(R.id.denglu);
        zhucezhanghao =(TextView) findViewById(R.id.zhucezhanghao);
        user = (EditText)findViewById(R.id.yonghuming);
        pass = (EditText) findViewById(R.id.mima);



        mmzh = (TextView)findViewById(R.id.mmzh);
        mmzh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Main_activity.this,Mimazhaohui_activity.class);
                startActivity(intent);
            }
        });


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

    public boolean isNetworkAvailable(Activity activity)
    {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null)
        {
            return false;
        }
        else
        {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    private static Boolean isExit = false;
    private void exitBy2Click() {
        Timer tExit = null;

        if (isExit == false) {
            isExit = true;
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出

                }
            }, 2000);

        } else {
            finish();
            SysApplication.getInstance().exit();
        }
    }
}
