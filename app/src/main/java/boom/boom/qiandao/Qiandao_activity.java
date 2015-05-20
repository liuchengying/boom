package boom.boom.qiandao;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.CheckIn;
import boom.boom.api.SysApplication;
import boom.boom.myview.SildingFinishLayout;

/**
 * Created by 刘成英 on 2015/3/12.
 */
public class Qiandao_activity extends Activity {
    private Button wancheng;
    private TextView checked;
    private TextView remain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.qiandao);
        SildingFinishLayout mSildingFinishLayout = (SildingFinishLayout) findViewById(R.id.sildingFinishLayout);
        mSildingFinishLayout
                .setOnSildingFinishListener(new SildingFinishLayout.OnSildingFinishListener() {

                    @Override
                    public void onSildingFinish() {
                        Qiandao_activity.this.finish();
                    }
                });

        int days[]=new int[2];
        CheckIn checkIn=new CheckIn();

        checked=(TextView)findViewById(R.id.qiandao_checked);
        remain=(TextView)findViewById(R.id.qiandao_remain);
        if(checkIn.Checkin(days)) {

            Toast.makeText(Qiandao_activity.this,"签到成功！",Toast.LENGTH_SHORT).show();
        }
        else {

            Toast.makeText(Qiandao_activity.this,checkIn.ServerErr,Toast.LENGTH_SHORT).show();
        }
        checked.setText("已累计到第 "+days[0]+" 天");
        remain.setText("距离获得积分还有 "+days[1]+" 天");
        mSildingFinishLayout.setTouchView(mSildingFinishLayout);
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        wancheng = (Button) findViewById(R.id.qiandao_wancheng);

        wancheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

                overridePendingTransition(0, R.anim.base_slide_right_out);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }
}
