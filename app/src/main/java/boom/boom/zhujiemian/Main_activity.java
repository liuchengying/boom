package boom.boom.zhujiemian;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.pushservice.BasicPushNotificationBuilder;
import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.loopj.android.http.PersistentCookieStore;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.PreferenceChangeEvent;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.HttpIO;
import boom.boom.api.LoadingDialog;
import boom.boom.api.Static;
import boom.boom.api.SysApplication;
import boom.boom.api.User;
import boom.boom.api.UserData;
import boom.boom.api.Utils;
import boom.boom.mimazhaohui.Mimazhaohui_activity;
import boom.boom.tianzhan.Tiaozhan_activity;
import boom.boom.zhuceyanzheng.Zhuceyanzheng_activity;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

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
    private Button share;
    private Button sinaSign;
    private Button wechatSign;
    private Button qqSign;
    int plat = 0;
    String userId;
    User userlogin;
    LoadingDialog dialog;
    HashMap<String, Object> Map;
    Platform comletedPlat;
    Handler regestedHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String Data = (String) msg.obj;
           try{
               JSONObject obj = new JSONObject(Data);
               if(obj.getString("state").equals("SUCCESS")) {
                   userlogin = new User("", "");
                   User.session_id = obj.getString("s_id");
                   //Static.session_id = User.session_id;
                   userlogin.ifUserLoggedIn = true;
                   myMessageHandler.sendEmptyMessage(0);
                   comletedPlat.removeAccount();
               }else {
                   Toast.makeText(Main_activity.this,"注册失败！请稍后再试",Toast.LENGTH_SHORT).show();
               }
           }catch (Exception e){
               e.printStackTrace();
           }
            return true;
        }
    });
    Handler thirdpartyComplete = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == 1){
                Static.cookieStore = new PersistentCookieStore(Main_activity.this);
                HttpIO.getCookies();
                String Data = msg.getData().getString("data");
                try{
                    JSONObject obj = new JSONObject(Data);
                    if(obj.getString("state").equals("SUCCESS")){
                        userlogin = new User("","");
                        User.session_id = obj.getString("s_id");
                        //Static.session_id = User.session_id;
                        userlogin.ifUserLoggedIn = true;
                        myMessageHandler.sendEmptyMessage(0);
                        comletedPlat.removeAccount();
                    }else {
                        if(obj.getString("reason").equals("NEED_INITIAL_FIRST")){
                            final HttpIO io = new HttpIO(Utils.serveraddr + "/api/userRegister.php?action=3rdparty_init&type=" + plat + "&userid=" + userId);
                            io.SessionID = Static.session_id;
                            final List<NameValuePair> post = new ArrayList<NameValuePair>();
                            post.add(new BasicNameValuePair("nickname", Utils.UTF8str(comletedPlat.getDb().getUserName()))); // 增加POST表单数据
                            post.add(new BasicNameValuePair("sex",Utils.UTF8str((comletedPlat.getDb().getUserGender().equals("m")?0:1)+"")));
                            post.add(new BasicNameValuePair("avatar_url",Utils.UTF8str(comletedPlat.getDb().getUserIcon())));
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    io.POSTToHTTPServer(post);
                                    Message msg = new Message();
                                    msg.obj = io.getResultData();
                                    regestedHandler.sendMessage(msg);
                                }
                            }).start();
                        }else{
                            Toast.makeText(Main_activity.this,"登陆失败，请稍后再试",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(Main_activity.this,"网络连接失败！请稍后再试",Toast.LENGTH_SHORT).show();
            }

            return true;
        }
    });
    PlatformActionListener pa = new PlatformActionListener() {
        @Override
        public void onComplete(final Platform platform, int i, final HashMap<String, Object> hashMap) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Map = hashMap;
                    comletedPlat = platform;
                    userId = platform.getDb().getUserId();
                    if (platform.getName().equals("SinaWeibo")) {
                        plat = 5;
                    } else if (platform.getName().equals("QQ")) {
                        plat = 3;
                    } else if (platform.getName().equals("Wechat")) {
                        plat = 1;
                    }
                    dialog = new LoadingDialog(Main_activity.this, "正在登陆...");
                    dialog.show();
                    dialog.setCancelable(false);
                    HttpIO.GetHttpEX(Main_activity.this, thirdpartyComplete, Utils.serveraddr + "/api/userlogin.php?action=3rdparty_login&userid=" + userId + "&platform=" + plat);
                }
            });
        }

        @Override
        public void onError(final Platform platform, int i, final Throwable throwable) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(Main_activity.this,"授权失败！",Toast.LENGTH_SHORT).show();
                    Log.e("ShareSdk failed",throwable.getLocalizedMessage());
                    platform.removeAccount();
                }
            });
        }

        @Override
        public void onCancel(Platform platform, int i) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(Main_activity.this,"您已取消授权！",Toast.LENGTH_SHORT).show();
                }
            });
        }
    };
    android.os.Handler myMessageHandler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (userlogin.ifLoggedIn()) {
                /*File file = new File(getCacheDir(), "loginToken.dat");
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
                    }*/
                    Static.cookieStore = new PersistentCookieStore(Main_activity.this);
                    HttpIO.getCookies();
                    UserData data = new UserData(userlogin.getSessionId());
                    Intent intent = new Intent();
//                    intent.putExtra("session_id", userlogin.getSessionId());
//                    intent.putExtra("name", data.QueryData("name"));
//                    intent.putExtra("nickname", data.QueryData("nickname"));
//                    intent.putExtra("uniquesign", data.QueryData("uniquesign"));
//                    intent.putExtra("coins", data.QueryData("coins"));
                    try {
                    //Static.session_id = userlogin.getSessionId();
                    Static.province = Utils.GetSubJSONObject(data.toJSONObject(), "location").getString("province");
                    Static.city = Utils.GetSubJSONObject(data.toJSONObject(), "location").getString("city");
                    Static.area = Utils.GetSubJSONObject(data.toJSONObject(), "location").getString("area");
                    Static.username = data.QueryData("name");
                    Static.nickname = data.QueryData("nickname");
                    Static.uniqueSign = data.QueryData("uniquesign");
                    Static.identifyDigit = data.QueryData("identifyDigit");
                    Static.avatar = data.QueryData("avatar");
                /*HttpIO io = new HttpIO("http://172.24.10.118/api/getimage.php?token=" + Static.avatar);
                io.SessionID=Static.session_id;
                Static.avatarImage = io.getImage();*/
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

                if (String.valueOf(data.QueryData("coins")).equals("null")){
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

        sinaSign = (Button) findViewById(R.id.sinaSign);
        wechatSign = (Button) findViewById(R.id.wechatSign);
        qqSign = (Button) findViewById(R.id.qqSign);
        share = (Button) findViewById(R.id.main_share);
        sinaSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Platform weibo = ShareSDK.getPlatform(Main_activity.this, SinaWeibo.NAME);
                weibo.setPlatformActionListener(pa);
                weibo.showUser(null);
                weibo.getDb();
//移除授权
//weibo.removeAccount();
            }
        });
        qqSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Platform qq = ShareSDK.getPlatform(Main_activity.this, QQ.NAME);
                qq.setPlatformActionListener(pa);
                qq.showUser(null);
                qq.getDb();
            }
        });
        wechatSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Platform wechat = ShareSDK.getPlatform(Main_activity.this, Wechat.NAME);
                wechat.setPlatformActionListener(pa);
                wechat.showUser(null);
                wechat.getDb();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    OnekeyShare oks = new OnekeyShare();
                    //关闭sso授权
                    oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
                    //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
                    // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
                    oks.setTitle("BOOM--民间吉尼斯");
                    // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
                    oks.setTitleUrl("http://boom.corling.com");
                    // text是分享文本，所有平台都需要这个字段
                    oks.setText("撕逼挑战神器一一BOOM，全国首款让用户以视频的方式展现自己特殊技能的短视频挑战应用，全新的互动体验，千奇百怪的挑战内容、实时的动态排名，以及享受游戏给用户带来的无尽的乐趣");
                    oks.setImageUrl("http://boom.corling.com/resources/boom_avatar.png");
                    // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                    //oks.setImagePath("/sdcard/small.jpg");//确保SDcard下面存在此张图片
                    // url仅在微信（包括好友和朋友圈）中使用
                    oks.setUrl("http://sharesdk.cn");
                    // comment是我对这条分享的评论，仅在人人网和QQ空间使用
                    oks.setComment("asdasdasd");
                    // site是分享此内容的网站名称，仅在QQ空间使用
                    oks.setSite(getString(R.string.app_name));
                    // siteUrl是分享此内容的网站地址，仅在QQ空间使用
                    oks.setSiteUrl("http://boom.corling.com");

// 启动分享GUI
                    oks.show(Main_activity.this);
                }
        });


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
                dialog = new LoadingDialog(Main_activity.this, "正在登陆...");
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
                intent.setClass(Main_activity.this,Zhuceyanzheng_activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.base_slide_right_in,R.anim.base_slide_remain);

            }
        });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        SysApplication.getInstance().exit();
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
