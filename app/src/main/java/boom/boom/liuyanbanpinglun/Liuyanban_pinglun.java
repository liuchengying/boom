package boom.boom.liuyanbanpinglun;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.SysApplication;
import boom.boom.myview.SildingFinishLayout;

/**
 * Created by lcy on 2015/5/27.
 */
public class Liuyanban_pinglun extends Activity {

    private ListView lv;
    private LinearLayout liuyanban_fh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.liuyanban_pinglun);
        SildingFinishLayout mSildingFinishLayout = (SildingFinishLayout) findViewById(R.id.sildingFinishLayout);
        mSildingFinishLayout
                .setOnSildingFinishListener(new SildingFinishLayout.OnSildingFinishListener() {

                    @Override
                    public void onSildingFinish() {
                        Liuyanban_pinglun.this.finish();
                    }
                });

        mSildingFinishLayout.setTouchView(mSildingFinishLayout);

        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        lv = (ListView) findViewById(R.id.liuyanban_pinglun_listview);
        //留言板评论listview
        List<Map<String , Object>> mSelfData = new ArrayList<Map<String,Object>>();

        /**
         * 获取ListView组件。
         */
        lv = (ListView) findViewById(R.id.liuyanban_pinglun_listview);


        /**
         * 生成数据。
         */
        for (int i = 0; i < 10; i++) {
            HashMap<String, Object> mMap = new HashMap<String, Object>();
            mMap.put("img", R.drawable.android_181);
            mMap.put("name", "第" + i+"人");
            mMap.put("text", "第" + i);
            mSelfData.add(mMap);
        }

        /**
         * 自定义Adapter。
         */
        final SimpleAdapter mSimpleAdapter = new SimpleAdapter(this, mSelfData,
                R.layout.shipinpinglun_item, new String[] { "img", "name" ,"text"}, new int[] {
                R.id.shipinpinglun_Item_touxiang, R.id.shipinpinglun_nickname ,R.id.shipinpinglun_neirong});

        /**
         * 向ListView设置Adapter。
         */
        lv.setAdapter(mSimpleAdapter);
        liuyanban_fh = (LinearLayout) findViewById(R.id.liuyanban_fh);
        liuyanban_fh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0,R.anim.base_slide_right_out);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }
}
