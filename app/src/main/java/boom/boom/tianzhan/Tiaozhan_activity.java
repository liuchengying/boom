package boom.boom.tianzhan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.Static;
import boom.boom.api.SysApplication;
import boom.boom.faqitianzhan.Faqitianzhan_activity;
import boom.boom.gerenzhuye.Gerenzhuye_activity;
import boom.boom.guizejieshao.Guizejieshao_activity;
import boom.boom.paihangbang.Paihangbang_activity;
import boom.boom.shezhi.Shezhi_activity;
import boom.boom.slidingMenu.SlidingMenu;
import boom.boom.xinxizhongxin.xinxizhongxin_activity;
/**
 * Created by 刘成英 on 2015/1/13.
 */
public class Tiaozhan_activity extends Activity {
    private SlidingMenu mLeftMenu ;
    private Button cahuaanniu;
    private Button danrentiaozhan;
    private Button jiangpin;
    private Button paihangbang;
    private Button shezhi;
    private TextView nickname;
    private TextView coins;
    private Button button_startChallenge;
    private ImageView cehuatouxiang;
    private Button faqitianzhan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tiaozhan);
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体

        Intent intent = getIntent();
        String user_name = Static.username;
        String user_nickname = Static.nickname;
        int user_coins = Static.coins;
        cehuatouxiang = (ImageView) findViewById(R.id.cehuatouxiang);
        cahuaanniu = (Button) findViewById(R.id.cehuaanniu);
        mLeftMenu = (SlidingMenu) findViewById(R.id.cehuacaidan);
        danrentiaozhan = (Button) findViewById(R.id.danrentiaozhan);
        shezhi = (Button) findViewById(R.id.shezhi);
        faqitianzhan = (Button) findViewById(R.id.fabutiaozhan);
        faqitianzhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Tiaozhan_activity.this, Faqitianzhan_activity.class);
                startActivity(intent);
            }
        });
        button_startChallenge = (Button) findViewById(R.id.button_startchallenge);
        View.OnClickListener toBeginSingleChallenge = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Tiaozhan_activity.this, Guizejieshao_activity.class);
                startActivity(intent);
            }
        };
        danrentiaozhan.setOnClickListener(toBeginSingleChallenge);
        button_startChallenge.setOnClickListener(toBeginSingleChallenge);
        shezhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Tiaozhan_activity.this, Shezhi_activity.class);
                startActivity(intent);
            }
        });
        nickname = (TextView) findViewById(R.id.nickname);
        coins = (TextView) findViewById(R.id.mycoins);
        nickname.setText(user_nickname);
        coins.setText("" + user_coins);
        LinearLayout xxzx=(LinearLayout)findViewById(R.id.ch_xinxizhongxin);
        xxzx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(Tiaozhan_activity.this, xinxizhongxin_activity.class);
                startActivity(intent);
            }
        });
        LinearLayout phg=(LinearLayout)findViewById(R.id.ch_paihangbang);
        phg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(Tiaozhan_activity.this, Paihangbang_activity.class);
                startActivity(intent);

            }
        });
        cehuatouxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Tiaozhan_activity.this, Gerenzhuye_activity.class);
                startActivity(intent);

            }
        });
    }
    public void dianjicehua(View view){
        mLeftMenu.toggle();

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            exitBy2Click();      //调用双击退出函数
        }
        return false;
    }
    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            SysApplication.getInstance().exit();
        }
    }  }