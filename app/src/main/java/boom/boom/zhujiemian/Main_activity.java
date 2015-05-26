package boom.boom.zhujiemian;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.LoadingDialog;
import boom.boom.api.Static;
import boom.boom.api.SysApplication;
import boom.boom.api.User;
import boom.boom.api.UserData;
import boom.boom.api.Utils;
import boom.boom.denglu.dengluzhuce_activity;
import boom.boom.mimazhaohui.Mimazhaohui_activity;
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
    private TextView mmzh;
    User userlogin;
    LoadingDialog dialog;
    android.os.Handler myMessageHandler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (userlogin.ifLoggedIn()) {
                File file = new File(getCacheDir(), "loginToken.dat");
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    file.delete();
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                    try {
                        FileOutputStream out = new FileOutputStream(file);
                        try {
                            out.write(userlogin.getSessionId().getBytes());
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
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
                Static.identifyDigit = data.QueryData("identifyDigit");
                Static.avatar = data.QueryData("avatar");
                /*HttpIO io = new HttpIO("http://172.24.10.118/api/getimage.php?token=" + Static.avatar);
                io.SessionID=Static.session_id;
                Static.avatarImage = io.getImage();*/

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap=null;
                        String resultData = "";
                        InputStream in = null;
                        HttpURLConnection urlConn = null;
                        BufferedReader buffer = null;
                        try {
                            Utils.GetBuilder get = new Utils.GetBuilder(Utils.serveraddr + "/api/getimage.php");
                            get.addItem("token", Static.avatar);
                            URL url = new URL(get.toString());
                            if (url != null) {
                                urlConn = (HttpURLConnection) url.openConnection();
                                urlConn.setConnectTimeout(5000);// 设置超时时间
                                urlConn.setRequestProperty("Cookie","PHPSESSID=" + Static.session_id);
                                try {
                                    in = urlConn.getInputStream();
                                } catch (ConnectException e) {
                                    e.printStackTrace();
                                }
                            }
                            //解析得到图片
                            bitmap = BitmapFactory.decodeStream(in);
                            //关闭数据流
                            in.close();
                            urlConn.disconnect();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        Static.avatarImage=bitmap;
                    }
                });
                thread.start();

                if (String.valueOf(data.QueryData("coins")) == "null"){
                    Static.coins = 0;
                }else {
                    Static.coins = Integer.parseInt(String.valueOf(data.QueryData("coins")));
                }
                intent.setClass(Main_activity.this, Tiaozhan_activity.class);
                startActivity(intent);
            }else{
                dialog.cancel();
                Toast.makeText(getApplicationContext(), "登陆失败：" + userlogin.getServerErr(), Toast.LENGTH_SHORT).show();

            }
        }};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhujiemian);
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this),this);//字体
        if (!isNetworkAvailable(Main_activity.this)){
            Toast.makeText(getApplicationContext(), "当前没有可用网络！", Toast.LENGTH_LONG).show();
        }
        Intent intent = getIntent();
        int state = intent.getIntExtra("State", 3);
        switch (state){
            case 2:
                Toast.makeText(getApplicationContext(), "您上次的登陆信息已过期，请重新登陆。",Toast.LENGTH_SHORT);
                break;
            default:
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
                final String user_Str = user.getText().toString();
                final String pass_Str = pass.getText().toString();
                dialog = new LoadingDialog(Main_activity.this);
                dialog.show();
                dialog.setCancelable(false);

                userlogin= new User(user_Str, pass_Str);
            /*    try {
                    userlogin.AttemptLogin();
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            userlogin.AttemptLogin();
                            Message m = new Message();
                            Main_activity.this.myMessageHandler.sendMessage(m);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                /*if (userlogin.ifLoggedIn()) {
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

                }*/
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
