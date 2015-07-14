package boom.boom.bianjixinxi;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;

import boom.boom.R;

/**
 * Created by 刘成英 on 2015/3/19.
 */
public class Bianjixinxi_activity  extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bianjiziliao_one);
        getFragmentManager().beginTransaction().add(R.id.bianjiziliao_frame,new Bianjixinxi_Fragment()).commit();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.base_slide_remain, R.anim.base_slide_right_out);
    }

}
