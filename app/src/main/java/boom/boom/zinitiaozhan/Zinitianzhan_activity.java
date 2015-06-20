package boom.boom.zinitiaozhan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.HttpIO;
import boom.boom.api.ProgressListener;
import boom.boom.api.Static;
import boom.boom.api.Utils;
import boom.boom.api.uploadFile;
import boom.boom.myview.SildingFinishLayout;
import boom.boom.paishetiaozhan.Paishetiaozhan_activity;
import boom.boom.tianzhan.Tiaozhan_activity;


/**
 * Created by 刘成英 on 2015/3/25.
 */
public class Zinitianzhan_activity extends Activity {
    private ProgressBar mprogress2;
    private LinearLayout zntz_shangchuan;
    private TextView zn_dianjishangchuan;
    private ImageView znsc_scchenggong;
    private Button zntz_tijiaoshenhe;
    private EditText title;
    private EditText description;
    private String file_path = null;
    private boolean forRecordState = false;
    int zhuangtai = 0;
    int a = 0;
    int percent = 0;

    Handler handler_onUploadMonitor = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
                    if (a == 9) {
                        mprogress2.setProgress(percent);
                        zn_dianjishangchuan.setText("上传成功");
                        znsc_scchenggong.setVisibility(View.VISIBLE);
                    }
        }
    };

    Handler handler_onUploadFailure = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            Toast.makeText(Zinitianzhan_activity.this, "对不起，上传失败。", Toast.LENGTH_SHORT).show();
            zn_dianjishangchuan.setText("录制完毕，等待上传");
        }
    };

    Handler handler_onUploadSuccess = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent();
            intent.setClass(Zinitianzhan_activity.this, Tiaozhan_activity.class);
            startActivity(intent);
            Toast.makeText(Zinitianzhan_activity.this.getApplicationContext(), "发布成功。", Toast.LENGTH_SHORT).show();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Zinitianzhan_activity.this.finish();
                }
            }, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.zinitiaozhan);
        zntz_tijiaoshenhe = (Button) findViewById(R.id.zntz_tijiaoshenhe);
        SildingFinishLayout mSildingFinishLayout = (SildingFinishLayout) findViewById(R.id.sildingFinishLayout);
        mSildingFinishLayout
                .setOnSildingFinishListener(new SildingFinishLayout.OnSildingFinishListener() {

                    @Override
                    public void onSildingFinish() {
                        Zinitianzhan_activity.this.finish();
                    }
                });

        mSildingFinishLayout.setTouchView(mSildingFinishLayout);
        LinearLayout zntz_fh = (LinearLayout) findViewById(R.id.zntz_fh);
        zntz_fh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(file_path!=null) {
                    final AlertDialog alertDialog=new AlertDialog.Builder(Zinitianzhan_activity.this).create();
                    alertDialog.show();
                    alertDialog.setCancelable(false);
                    Window window=alertDialog.getWindow();
                    window.setContentView(R.layout.mbox_yesno);
                    TextView ok_title=(TextView)window.findViewById(R.id.yn_title);
                    TextView ok_text=(TextView)window.findViewById(R.id.yn_text);
                    ok_title.setText("提示");
                    ok_text.setText("您已录制过一次视频，你想要放弃之前录制的视频吗？");
                    Button yes=(Button)window.findViewById(R.id.button_ok_yn);
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                            overridePendingTransition(0, R.anim.base_slide_right_out);
                        }
                    });
                    Button no = (Button)window.findViewById(R.id.button_cancel_yn);
                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.cancel();
                        }
                    });
                }else {
                    finish();
                }
            }
        });
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        mprogress2 = (ProgressBar) findViewById(R.id.mprogress1);
        zntz_shangchuan = (LinearLayout) findViewById(R.id.zntz_shangchuan);
        zn_dianjishangchuan = (TextView) findViewById(R.id.zn_shangchuanshipin);
        znsc_scchenggong = (ImageView) findViewById(R.id.zntz_scchonggong);
        title = (EditText) findViewById(R.id.zn_title);
        description = (EditText) findViewById(R.id.short_describes);
        zn_dianjishangchuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (file_path != null) {
                    Log.e("Paishetiaozhan", "Last time already recorded.");
                    /*new AlertDialog.Builder(Zinitianzhan_activity.this).
                            setTitle("提示").
                            setMessage("您已录制过一次视频，你想要放弃之前录制的视频吗？").
                            setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent();
                                    intent.setClass(Zinitianzhan_activity.this, Paishetiaozhan_activity.class);
                                    intent.putExtra("IfReturn", true);
                                    startActivityForResult(intent, 0);
                                }
                            }).
                            setNegativeButton("取消", null).show();*/
                    final AlertDialog alertDialog=new AlertDialog.Builder(Zinitianzhan_activity.this).create();
                    alertDialog.show();
                    alertDialog.setCancelable(false);
                    Window window=alertDialog.getWindow();
                    window.setContentView(R.layout.mbox_yesno);
                    TextView ok_title=(TextView)window.findViewById(R.id.yn_title);
                    TextView ok_text=(TextView)window.findViewById(R.id.yn_text);
                    ok_title.setText("提示");
                    ok_text.setText("您已录制过一次视频，你想要放弃之前录制的视频吗？");
                    Button yes=(Button)window.findViewById(R.id.button_ok_yn);
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setClass(Zinitianzhan_activity.this, Paishetiaozhan_activity.class);
                            intent.putExtra("IfReturn", true);
                            startActivityForResult(intent, 0);
                            alertDialog.cancel();
                        }
                    });
                    Button no = (Button)window.findViewById(R.id.button_cancel_yn);
                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.cancel();
                        }
                    });
                }else{
                    Intent intent = new Intent();
                    intent.setClass(Zinitianzhan_activity.this, Paishetiaozhan_activity.class);
                    intent.putExtra("IfReturn", true);
                    startActivityForResult(intent, 0);
                }
            }
        });

        zntz_tijiaoshenhe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (forRecordState == false) {
                    Toast.makeText(getApplicationContext(), "您还尚未录制视频哦！", Toast.LENGTH_SHORT).show();
                } else {
                    zn_dianjishangchuan.setText("正在上传视频...");
                    new Thread(new Runnable() {
                        @Override

                        public void run() {
//                            for (a = 0; a < 10; a++) {
//                                try {
//                                    mprogress2.incrementProgressBy(10);
//                                    Message m = new Message();
//                                    m.what = 1;
//                                    Zinitianzhan_activity.this.myMessageHandler.sendMessage(m);
//
//                                    Thread.sleep(1000);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }
                            Utils.GetBuilder get = new Utils.GetBuilder(Utils.serveraddr + Utils.put_file_api);
                            get.addItem("s_id", Static.session_id);
                            get.addItem("type", "video");
                            String result = uploadFile.uploadFile(new ProgressListener() {
                                @Override
                                public void transferred(int transferedBytes) {
                                    Message m = new Message();
                                    m.what = 3;
                                    percent = transferedBytes;
                                    Zinitianzhan_activity.this.handler_onUploadMonitor.sendMessage(m);
                                }

                                public void transferred(long transferedBytes) {

                                }
                            }, get.toString(), file_path, "heheda.mp4");
                            try {
                                JSONObject obj = new JSONObject(result);
                                String tmp = obj.getString("state");
                                if (tmp.equals("FAILED")) {
                                    Message mm = new Message();
                                    mm.what = 1;
                                    Zinitianzhan_activity.this.handler_onUploadFailure.sendMessage(mm);
                                } else {
                                    String token = obj.getString("fileToken");
                                    Utils.GetBuilder get1 = new Utils.GetBuilder(Utils.serveraddr + Utils.newCl_api);
                                    get1.addItem("action", "newcl");
                                    get1.addItem("frontname", Utils.UTF8str(title.getText().toString()));
                                    get1.addItem("dvtoken", token);
                                    get1.addItem("shortintro", Utils.UTF8str(description.getText().toString()));
                                    final HttpIO io = new HttpIO(get1.toString());
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            io.SetCustomSessionID(Static.session_id);
                                            io.GETToHTTPServer();
                                        }
                                    }).start();
                                    while(io.getResultData() == null);
                                    if (io.LastError == 0) {
                                        String result1 = io.getResultData();
                                        JSONObject obj1 = new JSONObject(result1);
                                        String state = obj1.getString("state");
                                        if (state.equals("SUCCESS")) {
                                            Message mmm = new Message();
                                            mmm.what = 2;
                                            Zinitianzhan_activity.this.handler_onUploadSuccess.sendMessage(mmm);
                                        } else {
                                            Message m = new Message();
                                            m.what = 1;
                                            Zinitianzhan_activity.this.handler_onUploadFailure.sendMessage(m);
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
             }
        });
        }
        @Override
        public void onBackPressed () {
            super.onBackPressed();
            overridePendingTransition(0, R.anim.base_slide_right_out);
        }

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            switch (resultCode) {
                case RESULT_OK:
                    String tmp;
                    this.file_path = data.getStringExtra("file_path");
                    zn_dianjishangchuan.setText("录制完毕，等待上传");
                    if ((tmp = this.file_path) == null) tmp = "NULL";
                    Log.e("File path", tmp);
                    forRecordState = true;
            }
        }
    }


