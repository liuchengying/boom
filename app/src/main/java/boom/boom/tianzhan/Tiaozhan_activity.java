package boom.boom.tianzhan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.Static;
import boom.boom.api.SysApplication;
import boom.boom.api.myImageView;
import boom.boom.faqitianzhan.Faqitianzhan_activity;
import boom.boom.gerenzhuye.Gerenzhuye_activity;
import boom.boom.guizejieshao.Guizejieshao_activity;
import boom.boom.haoyouliebiao.Haoyouliebiao_activity;
import boom.boom.myview.BadgeView;
import boom.boom.myview.CircleImageView;
import boom.boom.paihangbang.Paihangbang_activity;
import boom.boom.qiandao.Qiandao_activity;
import boom.boom.shezhi.Shezhi_activity;
import boom.boom.slidingMenu.SlidingMenu;
import boom.boom.xinxizhongxin.xinxizhongxin_activity;
import cn.smssdk.SMSSDK;

/**
 * Created by 刘成英 on 2015/1/13.
 */
public class Tiaozhan_activity extends FragmentActivity {
    private SlidingMenu mLeftMenu ;
    private Button cahuaanniu;
    private Button danrentiaozhan;
    private Button jiangpin;
    private Button paihangbang;
    private Button shezhi;
    private TextView nickname;
    private TextView coins;
    private Button qiandao;
    private myImageView cehuatouxiang;
    private Button faqitianzhan;
    private Button tz_grzy;
    private LinearLayout ch_xitongshezhi;
    private LinearLayout ch_haoyouliebiao;
    private LinearLayout xiaoxizhongxin_jiaobiao;
    private BadgeView mBadgeView;

    public android.os.Handler myMessageHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            nickname.setText(Static.nickname);
            coins.setText("" + Static.coins);
            cehuatouxiang.setImageBitmap(Static.avatarImage);
        }
    };

            @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tiaozhan);
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        Static.tiaozhan_handler=this.myMessageHandler;
        Intent intent = getIntent();
        String user_name = Static.username;
        String user_nickname = Static.nickname;
        int user_coins = Static.coins;
        cehuatouxiang = (myImageView) findViewById(R.id.cehuatouxiang);
        cahuaanniu = (Button) findViewById(R.id.cehuaanniu);
        mLeftMenu = (SlidingMenu) findViewById(R.id.cehuacaidan);
        danrentiaozhan = (Button) findViewById(R.id.danrentiaozhan);
        shezhi = (Button) findViewById(R.id.shezhi);
        paihangbang = (Button) findViewById(R.id.paihangbang);
        paihangbang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Tiaozhan_activity.this, Paihangbang_activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.base_slide_remain, R.anim.base_slide_right_in);
            }
        });
                // *** 消息提示红色角标 ***
                xiaoxizhongxin_jiaobiao = (LinearLayout) findViewById(R.id.xinxizhongxin_jiaobiao);

                if (mBadgeView != null)
                {
                    xiaoxizhongxin_jiaobiao.removeView(mBadgeView);
                }
                mBadgeView = new BadgeView(Tiaozhan_activity.this);
                mBadgeView.setBackgroundResource(R.drawable.android_202);
                mBadgeView.setBadgeCount(0);
                mBadgeView.setTextSize(12);
                xiaoxizhongxin_jiaobiao.addView(mBadgeView);
                Static.badgeView = mBadgeView;



                tz_grzy = (Button) findViewById(R.id.tz_grzy);
        tz_grzy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Tiaozhan_activity.this, Gerenzhuye_activity.class);
                intent.putExtra("type",1);
                intent.putExtra("guestID",Static.identifyDigit);
                startActivity(intent);
                overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
            }
        });
        faqitianzhan = (Button) findViewById(R.id.fabutiaozhan);

        faqitianzhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Tiaozhan_activity.this, Faqitianzhan_activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
            }
        });
        qiandao = (Button) findViewById(R.id.qiandao);
        qiandao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent();
                intent.setClass(Tiaozhan_activity.this, Qiandao_activity.class);
                startActivity(intent);
                Timer timer=new Timer();
                TimerTask timerTask=new TimerTask() {
                    @Override
                    public void run() {
                        dianjicehua(v);
                    }
                };
                timer.schedule(timerTask, 800);
                overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
            }
        });
//        View.OnClickListener toBeginSingleChallenge = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(Tiaozhan_activity.this, Guizejieshao_activity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
//
//            }
//        };
//        danrentiaozhan.setOnClickListener(toBeginSingleChallenge);
        danrentiaozhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Tiaozhan_activity.this, Guizejieshao_activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.base_slide_right_in,R.anim.base_slide_remain);

            }
        });


        shezhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent();
                intent.setClass(Tiaozhan_activity.this, Shezhi_activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);



            }
        });

        nickname = (TextView) findViewById(R.id.nickname);
        coins = (TextView) findViewById(R.id.mycoins);
        nickname.setText(user_nickname);
        coins.setText("" + user_coins);
        LinearLayout xxzx=(LinearLayout)findViewById(R.id.ch_xinxizhongxin);
        xxzx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent=new Intent();
                intent.setClass(Tiaozhan_activity.this, xinxizhongxin_activity.class);
                startActivity(intent);
                Timer timer=new Timer();
                TimerTask timerTask=new TimerTask() {
                    @Override
                    public void run() {
                        dianjicehua(v);
                    }
                };
                timer.schedule(timerTask, 800);
                overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
            }
        });
        LinearLayout phg=(LinearLayout)findViewById(R.id.ch_paihangbang);
        phg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent=new Intent();
                intent.setClass(Tiaozhan_activity.this, Paihangbang_activity.class);
                startActivity(intent);
                Timer timer=new Timer();
                TimerTask timerTask=new TimerTask() {
                    @Override
                    public void run() {
                        dianjicehua(v);
                    }
                };
                timer.schedule(timerTask, 800);
                overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
            }
        });
        cehuatouxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent();
                intent.setClass(Tiaozhan_activity.this, Gerenzhuye_activity.class);
                intent.putExtra("type",1);
                intent.putExtra("guestID",Static.identifyDigit);
                startActivity(intent);
                Timer timer=new Timer();
                TimerTask timerTask=new TimerTask() {
                    @Override
                    public void run() {
                        dianjicehua(v);
                    }
                };
                timer.schedule(timerTask, 800);
                overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
            }
        });
        if(Static.avatarImage!=null) {
            cehuatouxiang.setImageBitmap(Static.avatarImage);
        }
        ch_xitongshezhi = (LinearLayout) findViewById(R.id.ch_xitongshezhi);
        ch_xitongshezhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent();
                intent.setClass(Tiaozhan_activity.this, Shezhi_activity.class);
                startActivity(intent);
                Timer timer=new Timer();
                TimerTask timerTask=new TimerTask() {
                    @Override
                    public void run() {
                        dianjicehua(v);
                    }
                };
                timer.schedule(timerTask, 800);
                overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
            }
        });
        ch_haoyouliebiao = (LinearLayout) findViewById(R.id.ch_haoyouliebiao);
        ch_haoyouliebiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent();
                intent.setClass(Tiaozhan_activity.this, Haoyouliebiao_activity.class);
                startActivity(intent);
                Timer timer=new Timer();
                TimerTask timerTask=new TimerTask() {
                    @Override
                    public void run() {
                        dianjicehua(v);
                    }
                };
                timer.schedule(timerTask, 800);
                overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
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
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }
}