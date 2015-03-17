package boom.boom.shangchuanchenggong;

import android.app.Activity;
import android.os.Bundle;

import boom.boom.R;
import boom.boom.api.SysApplication;

/**
 * Created by 刘成英 on 2015/1/20.
 */
public class Shangchuanchenggong_activity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shangchuanchenggong);
        SysApplication.getInstance().addActivity(this);

    }
}
