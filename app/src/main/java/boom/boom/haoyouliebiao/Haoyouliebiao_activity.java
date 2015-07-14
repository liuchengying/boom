package boom.boom.haoyouliebiao;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.AsyncLoadAvatar;
import boom.boom.api.FriendList;
import boom.boom.api.SysApplication;
import boom.boom.gerenzhuye.Gerenzhuye_activity;
import boom.boom.myview.SildingFinishLayout;
import boom.boom.search_friends.Search_friends_activity;


/**
 * Created by lcy on 2015/4/29.
 */
public class Haoyouliebiao_activity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.haoyouliebiao);
        getFragmentManager().beginTransaction().add(R.id.haoyouliebiao_frame,new haoyouliebiao_fragment()).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.base_slide_remain, R.anim.base_slide_right_out);
    }

}
