package boom.boom.shouquanshibai;

import android.app.Activity;
import android.os.Bundle;

import boom.boom.R;
import boom.boom.api.SysApplication;

/**
 * Created by 刘成英 on 2015/3/12.
 */
public class Shouquanshibai_activity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shouquanshibai);
        SysApplication.getInstance().addActivity(this);
    }
}
