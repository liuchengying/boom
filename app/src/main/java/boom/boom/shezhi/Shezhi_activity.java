package boom.boom.shezhi;



import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.EditInformation;
import boom.boom.api.HttpIO;
import boom.boom.api.LoadingDialog;
import boom.boom.api.Static;
import boom.boom.api.SysApplication;
import boom.boom.api.Utils;
import boom.boom.bangzhuyufankui.Bangzhuyufankui_activity;
import boom.boom.bianjixinxi.Bianjixinxi_activity;
import boom.boom.guanyuwomen.Guanyuwomen_activity;
import boom.boom.mimaxiugai.Mimaxiugai_activity;
import boom.boom.myview.RoundedImageView;
import boom.boom.myview.SildingFinishLayout;

/**
 * Created by 刘成英 on 2015/1/15.
 */

public class Shezhi_activity extends FragmentActivity {
   /* private RelativeLayout shezhifanhui;
    private Button tuichuzhanghu;
    private Vibrator vibrator;
    private RelativeLayout sz_jinzijikejian;
    private RelativeLayout sz_jinhaoyoukejian;
    private RelativeLayout sz_suoyourenkejian;
    private ImageView sz_img1;
    private ImageView sz_img2;
    private ImageView sz_img3;
    private LoadingDialog dialog;
    public EditInformation editInformation;

    android.os.Handler myMessageHandler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                Intent intent = new Intent();
                //Bundle bundle=new Bundle();
                //bundle.putSerializable("info",editInformation);
                //intent.putExtras(bundle);
                intent.setClass(Shezhi_activity.this, Bianjixinxi_activity.class);
                startActivity(intent);
                //dialog.dismiss();
            }
            else {
                //dialog.dismiss();
                Toast.makeText(Shezhi_activity.this,editInformation.ServerErr,Toast.LENGTH_SHORT).show();
            }

        }};*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.shezhi);
        getFragmentManager().beginTransaction().add(R.id.shezhi_frame,new Shezhi_Fragment()).commit();
        RelativeLayout shezhifanhui = (RelativeLayout)findViewById(R.id.shezhifanhui);
        shezhifanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }

}
