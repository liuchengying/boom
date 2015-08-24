package boom.boom.Welcome;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.loopj.android.http.PersistentCookieStore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;


import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.HttpIO;
import boom.boom.api.Static;
import boom.boom.api.SysApplication;
import boom.boom.api.User;
import boom.boom.api.UserData;
import boom.boom.api.Utils;
import boom.boom.tianzhan.Tiaozhan_activity;
import boom.boom.zhujiemian.Main_activity;
import cn.sharesdk.framework.ShareSDK;
import cn.smssdk.SMSSDK;

/**
 * Created by 刘成英 on 2015/1/13.
 */
public class Welcome_activity extends Activity {
    public String session;
    User user;
    public boolean msg_delivered = false;
    Handler loginHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == 1){
                if (user.ifLoggedIn()){
                    UserData data  = new UserData(session);
                    //Static.session_id = session;
                    Log.e("Test", "Test3");
                    try {
                        Static.username = data.QueryData("name");
                        Static.nickname = data.QueryData("nickname");
                        Static.uniqueSign = data.QueryData("uniquesign");
                        Static.identifyDigit = data.QueryData("identifyDigit");
                        Static.avatar = data.QueryData("avatar");
                        Static.coins = Integer.parseInt(data.QueryData("coins"));
                       /* Static.province = Utils.GetSubJSONObject(data.toJSONObject(), "location").getString("province");
                        Static.city = Utils.GetSubJSONObject(data.toJSONObject(), "location").getString("city");
                        Static.area = Utils.GetSubJSONObject(data.toJSONObject(), "location").getString("area");*/
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap bitmap=null;
                            String resultData = "";
                            InputStream in = null;
                            HttpURLConnection urlConn = null;
                            BufferedReader buffer = null;
                            if(!Static.avatar.equals("null")){
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
                            }
                            else {
                                Resources res = getResources();
                                bitmap = BitmapFactory.decodeResource(res,R.drawable.android_114);
                            }
                            Static.avatarImage = bitmap;
                        }
                    });
                    thread.start();
                    while (Static.avatarImage == null);
                    Intent intent = new Intent();
                    intent.setClass(Welcome_activity.this, Tiaozhan_activity.class);
                    startActivity(intent);
                    Welcome_activity.this.finish();
                }else{
                    Intent intent = new Intent(Welcome_activity.this,Main_activity.class);
                    intent.putExtra("State", 3);
                    startActivity(intent);
                    msg_delivered = true;
                    Welcome_activity.this.finish();
                }
            }else {
                Toast.makeText(Welcome_activity.this,"网络连接错误！请检查网络连接",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Welcome_activity.this,Main_activity.class);
                intent.putExtra("State", 3);
                startActivity(intent);
                msg_delivered = true;
                Welcome_activity.this.finish();
            }
            return true;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.huanyingjiemian);
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        SMSSDK.initSDK(this, "8d930e70baed", "421e26e130df780c049c7c22f07fb18c");
        try {
            ShareSDK.initSDK(Welcome_activity.this, "8dfb7359498a");
        }catch (Exception e){
            e.printStackTrace();
        }
        Static.cookieStore = new PersistentCookieStore(Welcome_activity.this);
        if(HttpIO.getCookies().size() == 0){
            Timer timer = new Timer();
            TimerTask task = new TimerTask(){
                @Override
                public void run() {
                    Intent intent = new Intent(Welcome_activity.this,Main_activity.class);
                    intent.putExtra("State", 3);
                    startActivity(intent);
                    msg_delivered = true;
                    Welcome_activity.this.finish();
                }
            };
            timer.schedule(task, 1000);
        }else {
            user = new User(session,Welcome_activity.this,loginHandler);
        }
        /*File file = new File(Utils.getWorkPath());
        if (!file.exists() && !file.isDirectory()){
            Log.e("BASE_FILE", "Unable to find the base data directory. Generate new one.");
            Log.e("BASE_FILE", "The folder will be created at " + file.getAbsolutePath());
//            file.mkdir();
            file.mkdirs();
        }
        file = new File(Utils.getVideoPath());
        if (!file.exists() && !file.isDirectory())  file.mkdirs();
        file = new File(Utils.getImagePath());
        if (!file.exists() && !file.isDirectory())  file.mkdirs();
        file = new File(getCacheDir(), "loginToken.dat");
        if (!file.exists()){
            Timer timer = new Timer();
            TimerTask task = new TimerTask(){
                @Override
                public void run() {
                    Intent intent = new Intent(Welcome_activity.this,Main_activity.class);
                    intent.putExtra("State", 3);
                    startActivity(intent);
                    msg_delivered = true;
                    Welcome_activity.this.finish();
                }
            };
            timer.schedule(task, 1000);
        }
        byte [] buffer = new byte[255];
        try {
            FileInputStream in = new FileInputStream(file);
            try {
                in.read(buffer);
//                this.session = buffer.toString();
                this.session = new String(buffer, "UTF-8");
                int counter;
                Log.e("Test", "Test1");
                for (counter = 0; buffer[counter] != '\u0000'; counter++);
                Log.e("Test", "Test2");
                this.session = this.session.substring(0, counter);
                Static.session_id = this.session;
                user = new User(session,Welcome_activity.this,loginHandler);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
    }
}
