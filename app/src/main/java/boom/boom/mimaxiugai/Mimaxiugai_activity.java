package boom.boom.mimaxiugai;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.LoadingDialog;
import boom.boom.api.SysApplication;
import boom.boom.mimazhaohui.Mimazhaohui_activity;
import boom.boom.myview.SildingFinishLayout;
import boom.boom.api.ChangePassword;
import boom.boom.zhujiemian.Main_activity;

/**
 * Created by 刘成英 on 2015/3/19.
 */
public class Mimaxiugai_activity extends Activity{
    TextView oldPassword;
    TextView newPassword;
    TextView confirmPassword;
    RelativeLayout confirm;
    String sOld;
    String sNew;
    String sConfirm;
    LoadingDialog dialog;
    ChangePassword changePassword;
    android.os.Handler myMessageHandler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dialog.cancel();
            switch (msg.what)
            {
                case 1:AlertDialog alertDialog=new AlertDialog.Builder(Mimaxiugai_activity.this).create();
                    alertDialog.show();
                    alertDialog.setCancelable(false);
                    Window window=alertDialog.getWindow();
                    window.setContentView(R.layout.mbox_ok);
                    TextView ok_title=(TextView)window.findViewById(R.id.ok_title);
                    TextView ok_text=(TextView)window.findViewById(R.id.ok_text);
                    ok_title.setText("修改密码成功！");
                    ok_text.setText("修改密码成功！请重新登录！");
                    Button ok=(Button)window.findViewById(R.id.ok_button);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = getBaseContext().getPackageManager()
                                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            //重启应用程序
                        }
                    });
                    break;
                case 2:
                    Toast.makeText(Mimaxiugai_activity.this,"修改失败："+changePassword.ServerErr,Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.xiugaimima);
        SildingFinishLayout mSildingFinishLayout = (SildingFinishLayout) findViewById(R.id.sildingFinishLayout);
        mSildingFinishLayout
                .setOnSildingFinishListener(new SildingFinishLayout.OnSildingFinishListener() {

                    @Override
                    public void onSildingFinish() {
                        Mimaxiugai_activity.this.finish();
                    }
                });

        mSildingFinishLayout.setTouchView(mSildingFinishLayout);
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体

        oldPassword=(TextView)findViewById(R.id.xgmm_oldpw);
        newPassword=(TextView)findViewById(R.id.xgmm_newpw);
        confirmPassword=(TextView)findViewById(R.id.xgmm_confirmpw);
        confirm=(RelativeLayout)findViewById(R.id.xgmm_ok);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sOld=oldPassword.getText().toString();
                sNew=newPassword.getText().toString();
                sConfirm=confirmPassword.getText().toString();
                if(sOld.equals("")||sNew.equals("")||sConfirm.equals(""))
                {
                    Toast.makeText(Mimaxiugai_activity.this,"密码不能为空!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(sNew.equals(sConfirm))
                {
                    dialog = new LoadingDialog(Mimaxiugai_activity.this,"正在加载..");
                    dialog.show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            changePassword=new ChangePassword();
                            if(changePassword.Change(Mimaxiugai_activity.this,sOld,sNew))
                            {
                                Message m = new Message();
                                m.what=1;
                                Mimaxiugai_activity.this.myMessageHandler.sendMessage(m);
                            }else{
                                Message m = new Message();
                                m.what=2;
                                Mimaxiugai_activity.this.myMessageHandler.sendMessage(m);
                            }

                        }
                    }).run();


                    //访问服务器
                }
                else {
                    Toast.makeText(Mimaxiugai_activity.this,"两次密码输入不一致！",Toast.LENGTH_SHORT).show();
                }
            }
        });

        RelativeLayout mmxg_fh = (RelativeLayout)findViewById(R.id.mimaxiugai_fh);
        mmxg_fh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, R.anim.base_slide_right_out);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }

}
