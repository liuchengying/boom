package boom.boom.liuyanbanpinglun;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.AsyncLoadAvatar;
import boom.boom.api.HttpIO;
import boom.boom.api.LoadingDialog;
import boom.boom.api.Static;
import boom.boom.api.SysApplication;
import boom.boom.api.Utils;
import boom.boom.liuyanhuifu.Liuyanhuifu_activity;
import boom.boom.myview.RoundedImageView;
import boom.boom.myview.SildingFinishLayout;

/**
 * Created by lcy on 2015/5/27.
 */
public class Liuyanban_pinglun extends FragmentActivity {
 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.liuyanban_pinglun);
        getFragmentManager().beginTransaction().add(R.id.liuyanpinglun_frame,new liuyanpinglun_fragment()).commit();



    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(1);
        overridePendingTransition(R.anim.base_slide_remain, R.anim.base_slide_right_out);
    }

}
