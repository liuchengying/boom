package boom.boom.shezhi;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

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

/**
 * Created by Administrator on 2015/7/9.
 */
public class Shezhi_Fragment extends Fragment {
    private RelativeLayout shezhifanhui;
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
    HttpIO io;
    android.os.Handler myMessageHandler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){

                //dialog.dismiss();
            }
            else {
                //dialog.dismiss();
                Toast.makeText(getActivity(), editInformation.ServerErr, Toast.LENGTH_SHORT).show();
            }

        }};
    Handler linkHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == 1){
            try {
                JSONObject obj = new JSONObject(msg.getData().getString("data"));
                switch (obj.getInt("current")){
                    case 3://所有人可见
                        sz_img3.setVisibility(View.VISIBLE);
                        sz_img2.setVisibility(View.INVISIBLE);
                        sz_img1.setVisibility(View.INVISIBLE);
                        break;
                    case 5://仅好友可见
                        sz_img2.setVisibility(View.VISIBLE);
                        sz_img1.setVisibility(View.INVISIBLE);
                        sz_img3.setVisibility(View.INVISIBLE);
                        break;
                    case 7://仅自己可见
                        sz_img1.setVisibility(View.VISIBLE);
                        sz_img2.setVisibility(View.INVISIBLE);
                        sz_img3.setVisibility(View.INVISIBLE);
                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }}
            else {
                Toast.makeText(getActivity(),"网络连接失败！请检查网络连接",Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    });
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shizhi_fragment,container,false);
        editInformation = new EditInformation();
        SysApplication.getInstance().addActivity(getActivity());
        FontManager.changeFonts(FontManager.getContentView(getActivity()), getActivity());//字体
        vibrator = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
        /*shezhifanhui = (RelativeLayout) view.findViewById(R.id.shezhifanhui);
        shezhifanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });*/
        sz_jinzijikejian = (RelativeLayout) view.findViewById(R.id.sz_jinzijikejian);
        sz_jinhaoyoukejian = (RelativeLayout) view.findViewById(R.id.sz_jinhaoyoukejian);
        sz_suoyourenkejian = (RelativeLayout) view.findViewById(R.id.sz_suoyourenkejian);
        sz_img1 = (ImageView) view.findViewById(R.id.sz_img1);
        sz_img2 = (ImageView) view.findViewById(R.id.sz_img2);
        sz_img3 = (ImageView) view.findViewById(R.id.sz_img3);
        sz_img();
        /*io = new HttpIO(Utils.serveraddr + "api/userdata.php?action=get_pri");
        io.SessionID = Static.session_id;
        new Thread(new Runnable() {
            @Override
            public void run() {
                io.GETToHTTPServer();
                Message msg = new Message();
                linkHandler.sendMessage(msg);
            }
        }).start();
*/
        HttpIO.GetHttpEX(getActivity(),linkHandler,Utils.serveraddr + "api/userdata.php?action=get_pri");

        RelativeLayout mmxg = (RelativeLayout)view.findViewById(R.id.sz_mmxg);
        mmxg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), Mimaxiugai_activity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.base_slide_right_in,R.anim.base_slide_remain);
            }
        });
        RelativeLayout xggezl = (RelativeLayout)view.findViewById(R.id.sz_xggrzl);
        xggezl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog = new LoadingDialog(getActivity(),"正在加载...");
                //dialog.show();
                //dialog.setCancelable(false);
                Intent intent = new Intent();
                intent.setClass(getActivity(),Bianjixinxi_activity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);


            }
        });

        RelativeLayout sz_bzyfk = (RelativeLayout) view.findViewById(R.id.sz_bbyfk);
        sz_bzyfk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), Bangzhuyufankui_activity.class);
                startActivity(intent);
            }
        });
        RelativeLayout sz_gywm = (RelativeLayout) view.findViewById(R.id.sz_gywm);
        sz_gywm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), Guanyuwomen_activity.class);
                startActivity(intent);
            }
        });

        tuichuzhanghu = (Button)view.findViewById(R.id.tuichuzhanghu);
        tuichuzhanghu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(getActivity().getCacheDir(), "loginToken.dat");
                if (file.exists())  file.delete();
                SysApplication.getInstance().exit();

            }
        });
        final ToggleButton mTogBtn = (ToggleButton) view.findViewById(R.id.sz_toggle1);
        final ToggleButton mTogBtn1 = (ToggleButton) view.findViewById(R.id.sz_toggle2);
        final ToggleButton mTogBtn2 = (ToggleButton) view.findViewById(R.id.sz_toggle3);// 获取到控件

        mTogBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {

                    vibrator.cancel();
                }else{
                    vibrator.vibrate(new long[]{10, 100, 0, 0, 0}, -1);

                }
            }
        });
        mTogBtn1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    //isChecken = ture   条件下的操作
                }else{
                    //isChecken = ture
                }
            }
        });
        mTogBtn2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    //isChecken = ture   条件下的操作
                }else{
                    //isChecken = ture
                }
            }
        });
        return view;
    }
    private void sz_img(){
        sz_jinzijikejian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sz_img1.getVisibility() != View.VISIBLE){
                    sz_img1.setVisibility(View.VISIBLE);
                    sz_img2.setVisibility(View.INVISIBLE);
                    sz_img3.setVisibility(View.INVISIBLE);
                    friendvisiblity(7);
                }
            }
        });
        sz_jinhaoyoukejian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sz_img2.getVisibility()!=View.VISIBLE) {
                    sz_img2.setVisibility(View.VISIBLE);
                    sz_img1.setVisibility(View.INVISIBLE);
                    sz_img3.setVisibility(View.INVISIBLE);
                    friendvisiblity(5);
                }
            }
        });
        sz_suoyourenkejian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sz_img3.getVisibility()!=View.VISIBLE) {
                    sz_img3.setVisibility(View.VISIBLE);
                    sz_img2.setVisibility(View.INVISIBLE);
                    sz_img1.setVisibility(View.INVISIBLE);
                    friendvisiblity(3);
                }
            }
        });
    }
    private  void friendvisiblity(final int opt){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpIO io = new HttpIO(Utils.serveraddr+"/api/userdata.php?action=set_pri&type="+opt);
                io.SessionID = Static.session_id;
                io.GETToHTTPServer();
                try{
                    JSONObject obj = new JSONObject(io.getResultData());
                    String state = obj.getString("state");
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
