package boom.boom.wanshanxinxi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.baidu.android.pushservice.PushMessageReceiver;

import org.json.JSONObject;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.HttpIO;
import boom.boom.api.Static;
import boom.boom.api.SysApplication;
import boom.boom.api.Utils;
import boom.boom.wheelcity.AddressData;
import boom.boom.wheelcity.OnWheelChangedListener;
import boom.boom.wheelcity.WheelView;
import boom.boom.wheelcity.adapters.AbstractWheelTextAdapter;
import boom.boom.wheelcity.adapters.ArrayWheelAdapter;
import boom.boom.widget1.MyAlertDialog;
import boom.boom.zhujiemian.Main_activity;

/**
 * Created by 刘成英 on 2015/1/16.
 */
public class Wanshanxinxi_activity extends Activity{
    private RelativeLayout wanshanxinxi_fh;
    private EditText loginname;
    private EditText nickname;
    private ToggleButton sex_boy;
    private ToggleButton sex_girl;
    private Button commit;
    private TextView addr;
    private String cityTxt;
    private String identifyDigit;
    Handler commitHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == 1){
                try{
                    JSONObject obj = new JSONObject(msg.getData().getString("data"));
                    if(obj.getString("state").equals("SUCCESS")){
                        Intent intent = new Intent();
                        intent.setClass(Wanshanxinxi_activity.this, Main_activity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        if(obj.getString("reason").equals("REG_FAILED_USER_EXISTED"))
                        {
                            Toast.makeText(Wanshanxinxi_activity.this,"用户名已存在！",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(Wanshanxinxi_activity.this, "服务器内部错误，请稍后再试！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(Wanshanxinxi_activity.this,"网络连接超时，请稍后再试！",Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wanshanxinxi);
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        wanshanxinxi_fh = (RelativeLayout) findViewById(R.id.wanshanxinxi_fh);
        loginname = (EditText) findViewById(R.id.loginnanme);
        nickname = (EditText) findViewById(R.id.nickname);
        sex_boy = (ToggleButton) findViewById(R.id.sex);
        sex_girl = (ToggleButton) findViewById(R.id.sex1);
        commit = (Button) findViewById(R.id.commit);
        addr = (TextView) findViewById(R.id.addr);
        identifyDigit = getIntent().getStringExtra("identifyDigit");
        wanshanxinxi_fh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //完成按钮点击
                if(loginname.getText().toString().equals("")){
                    Toast.makeText(Wanshanxinxi_activity.this,"登陆名不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                String optional = "";
                if(!nickname.getText().toString().equals("")){
                    optional += "&nickname=" + nickname.getText();
                }
                if (sex_boy.isChecked()||sex_girl.isChecked()){
                    optional += "&sex=" + (sex_boy.isChecked()?0:1);
                }
                if (!addr.getText().toString().equals("")){
                    optional += "&province=" + Static.province + "&city=" + Static.city + "&area=" + Static.area;
                }
                HttpIO.GetHttpEX(Wanshanxinxi_activity.this,commitHandler, Utils.serveraddr+"/api/userRegister.php?action=initLoginName&identify="+identifyDigit+"&name="+loginname.getText()+optional);
            }
        });
        //选择性别
        sex_boy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sex_girl.setChecked(false);
                }
            }
        });
        sex_girl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sex_boy.setChecked(false);
                }
            }
        });
        addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = dialogm();
                final MyAlertDialog dialog1 = new MyAlertDialog(Wanshanxinxi_activity.this)
                        .builder()
                        .setTitle("请选择地区：")
                        .setView(view)
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                dialog1.setPositiveButton("保存", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(Wanshanxinxi_activity.this, cityTxt, Toast.LENGTH_SHORT).show();
                        addr.setText(cityTxt);
                    }
                });
                dialog1.show();
            }
        });
    }

        //设置地区
    private View dialogm() {
        View contentView = LayoutInflater.from(Wanshanxinxi_activity.this).inflate(
                R.layout.wheelcity_cities_layout, null);
        final WheelView country = (WheelView) contentView
                .findViewById(R.id.wheelcity_country);
        country.setVisibleItems(3);
        country.setViewAdapter((boom.boom.wheelcity.adapters.WheelViewAdapter) new CountryAdapter(Wanshanxinxi_activity.this));

        final String cities[][] = AddressData.CITIES;
        final String ccities[][][] = AddressData.COUNTIES;
        final WheelView city = (WheelView) contentView
                .findViewById(R.id.wheelcity_city);
        city.setVisibleItems(0);

        // 地区选择
        final WheelView ccity = (WheelView) contentView
                .findViewById(R.id.wheelcity_ccity);
        ccity.setVisibleItems(0);// 不限城市

        country.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateCities(city, cities, newValue);



                cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
                        + " | "
                        + AddressData.CITIES[country.getCurrentItem()][city
                        .getCurrentItem()]
                        + " | "
                        + AddressData.COUNTIES[country.getCurrentItem()][city
                        .getCurrentItem()][ccity.getCurrentItem()];
            }
        });

        city.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updatecCities(ccity, ccities, country.getCurrentItem(),
                        newValue);
                cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
                        + " | "
                        + AddressData.CITIES[country.getCurrentItem()][city
                        .getCurrentItem()]
                        + " | "
                        + AddressData.COUNTIES[country.getCurrentItem()][city
                        .getCurrentItem()][ccity.getCurrentItem()];
                Static.province = AddressData.PROVINCES[country.getCurrentItem()];
                Static.city = AddressData.CITIES[country.getCurrentItem()][city
                        .getCurrentItem()];
                Static.area = AddressData.COUNTIES[country.getCurrentItem()][city
                        .getCurrentItem()][ccity.getCurrentItem()];
            }
        });

        ccity.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
                        + " | "
                        + AddressData.CITIES[country.getCurrentItem()][city
                        .getCurrentItem()]
                        + " | "
                        + AddressData.COUNTIES[country.getCurrentItem()][city
                        .getCurrentItem()][ccity.getCurrentItem()];
                Static.province = AddressData.PROVINCES[country.getCurrentItem()];
                Static.city = AddressData.CITIES[country.getCurrentItem()][city
                        .getCurrentItem()];
                Static.area = AddressData.COUNTIES[country.getCurrentItem()][city
                        .getCurrentItem()][ccity.getCurrentItem()];
            }
        });

        country.setCurrentItem(1);// 设置北京
        city.setCurrentItem(1);
        ccity.setCurrentItem(1);
        return contentView;
    }

    /**
     * Updates the city wheel
     */
    private void updateCities(WheelView city, String cities[][], int index) {
        ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(Wanshanxinxi_activity.this,
                cities[index]);
        adapter.setTextSize(18);
        city.setViewAdapter(adapter);
        city.setCurrentItem(0);
    }

    /**
     * Updates the ccity wheel
     */
    private void updatecCities(WheelView city, String ccities[][][], int index,
                               int index2) {
        ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(Wanshanxinxi_activity.this,
                ccities[index][index2]);
        adapter.setTextSize(18);
        city.setViewAdapter(adapter);
        city.setCurrentItem(0);
    }

    /**
     * Adapter for countries
     */
    private class CountryAdapter extends AbstractWheelTextAdapter {
        // Countries names
        private String countries[] = AddressData.PROVINCES;

        /**
         * Constructor
         */
        protected CountryAdapter(Context context) {
            super(context, R.layout.wheelcity_country_layout, NO_RESOURCE);
            setItemTextResource(R.id.wheelcity_country_name);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return countries.length;
        }

        @Override
        protected CharSequence getItemText(int index) {
            return countries[index];
        }
    }

    }

