package boom.boom.zinitiaozhan;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import boom.boom.R;

/**
 * Created by 刘成英 on 2015/3/25.
 */
public class Zinitianzhan_activity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zinitiaozhan);
        LinearLayout zntz_fh = (LinearLayout)findViewById(R.id.zntz_fh);
        zntz_fh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
