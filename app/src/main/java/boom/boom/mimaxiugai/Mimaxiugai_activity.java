package boom.boom.mimaxiugai;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import boom.boom.R;
import boom.boom.api.SysApplication;

/**
 * Created by 刘成英 on 2015/3/19.
 */
public class Mimaxiugai_activity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xiugaimima);
        SysApplication.getInstance().addActivity(this);
        LinearLayout mmxg_fh = (LinearLayout)findViewById(R.id.mimaxiugai_fh);
        finish();
    }
}
