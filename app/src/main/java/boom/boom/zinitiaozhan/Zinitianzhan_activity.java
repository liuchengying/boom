package boom.boom.zinitiaozhan;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.myview.SildingFinishLayout;


/**
 * Created by 刘成英 on 2015/3/25.
 */
public class Zinitianzhan_activity extends Activity {
    private ProgressBar mprogress2;
    private LinearLayout zntz_shangchuan;
    private TextView zn_dianjishangchuan;
    private ImageView znsc_scchenggong;
    int zhuangtai = 0;
    int a;
    Handler myMessageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (a == 9) {
                zn_dianjishangchuan.setText("上传成功");
                znsc_scchenggong.setVisibility(View.VISIBLE);

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.zinitiaozhan);
        SildingFinishLayout mSildingFinishLayout = (SildingFinishLayout) findViewById(R.id.sildingFinishLayout);
        mSildingFinishLayout
                .setOnSildingFinishListener(new SildingFinishLayout.OnSildingFinishListener() {

                    @Override
                    public void onSildingFinish() {
                        Zinitianzhan_activity.this.finish();
                    }
                });

        mSildingFinishLayout.setTouchView(mSildingFinishLayout);
        LinearLayout zntz_fh = (LinearLayout) findViewById(R.id.zntz_fh);
        zntz_fh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, R.anim.base_slide_right_out);
            }
        });
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        mprogress2 = (ProgressBar) findViewById(R.id.mprogress1);
        zntz_shangchuan = (LinearLayout) findViewById(R.id.zntz_shangchuan);
        zn_dianjishangchuan = (TextView) findViewById(R.id.zn_shangchuanshipin);
        znsc_scchenggong = (ImageView) findViewById(R.id.zntz_scchonggong);
        zntz_shangchuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                zn_dianjishangchuan.setText("正在上传视频...");
                new Thread(new Runnable() {
                    @Override

                    public void run() {
                        for (a = 0; a < 10; a++) {
                            try {
                                mprogress2.incrementProgressBy(10);
                                Message m = new Message();
                                m.what = 1;
                                Zinitianzhan_activity.this.myMessageHandler.sendMessage(m);

                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();


            }


        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }

}

