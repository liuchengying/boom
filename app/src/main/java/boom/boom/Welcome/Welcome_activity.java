package boom.boom.Welcome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.Static;
import boom.boom.api.SysApplication;
import boom.boom.api.User;
import boom.boom.api.UserData;
import boom.boom.api.Utils;
import boom.boom.tianzhan.Tiaozhan_activity;
import boom.boom.zhujiemian.Main_activity;

/**
 * Created by 刘成英 on 2015/1/13.
 */
public class Welcome_activity extends Activity {
    public String session;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            final int msg_what = msg.what;
            switch (msg.what){
                case 1:
                    UserData data  = new UserData(session);
                    Static.session_id = session;
                    Static.username = data.QueryData("name");
                    Static.nickname = data.QueryData("nickname");
                    Static.uniqueSign = data.QueryData("uniquesign");
                    Static.identifyDigit = data.QueryData("identifyDigit");
                    Static.avatar = data.QueryData("avatar");
                    Intent intent = new Intent();
                    intent.setClass(Welcome_activity.this, Tiaozhan_activity.class);
                    startActivity(intent);
                    break;
                case 2:
                case 3:
                Timer timer = new Timer();
                TimerTask task = new TimerTask(){
                    @Override
                    public void run() {
                        Intent intent = new Intent(Welcome_activity.this,Main_activity.class);
                        intent.putExtra("State", msg_what);
                        startActivity(intent);
                        Welcome_activity.this.finish();
                    }
                };
                timer.schedule(task, 1000);
            }
            Welcome_activity.this.finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.huanyingjiemian);
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        File file = new File(Utils.getWorkPath());
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
            Message mmm = new Message();
            mmm.what = 3;
            this.handler.sendMessage(mmm);
        }
        byte [] buffer = new byte[255];
        try {
            FileInputStream in = new FileInputStream(file);
            try {
                in.read(buffer);
//                this.session = buffer.toString();
                this.session = new String(buffer, "UTF-8");
                int counter;
                for (counter = 0; buffer[counter] != '\u0000'; counter++);
                this.session = this.session.substring(0, counter);
                User user = new User(session);
                if (user.ifLoggedIn()){
                    Message m  = new Message();
                    m.what = 1;
                    this.handler.sendMessage(m);
                }else{
                    Message mm = new Message();
                    mm.what = 2;
                    this.handler.sendMessage(mm);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
