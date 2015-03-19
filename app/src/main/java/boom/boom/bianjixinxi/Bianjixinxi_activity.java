package boom.boom.bianjixinxi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import boom.boom.R;
import boom.boom.api.SysApplication;
import boom.boom.shezhi.Shezhi_activity;
import boom.boom.zhujiemian.Main_activity;

/**
 * Created by 刘成英 on 2015/3/19.
 */
public class Bianjixinxi_activity  extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bianjiziliao);
        SysApplication.getInstance().addActivity(this);
        LinearLayout fanhui = (LinearLayout)findViewById(R.id.bianjixinxi_fh);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
