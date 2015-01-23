package boom.boom.tianzhan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import boom.boom.R;
import boom.boom.guizejieshao.Guizejieshao_activity;
import boom.boom.shezhi.Shezhi_activity;
import boom.boom.slidingMenu.SlidingMenu;
import boom.boom.xinxizhongxin.xinxizhongxin_activity;
import boom.boom.tongxunlu.tongxunlu_activity;
/**
 * Created by 刘成英 on 2015/1/13.
 */
public class Tiaozhan_activity extends Activity {
    private SlidingMenu mLeftMenu ;
    private Button cahuaanniu;
    private Button danrentiaozhan;
    private Button jiangpin;
    private Button paihangbang;
    private Button shezhi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tiaozhan);
        cahuaanniu = (Button) findViewById(R.id.cehuaanniu);
        mLeftMenu = (SlidingMenu) findViewById(R.id.cehuacaidan);
        danrentiaozhan = (Button) findViewById(R.id.danrentiaozhan);
        shezhi = (Button) findViewById(R.id.shezhi);
        danrentiaozhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Tiaozhan_activity.this, Guizejieshao_activity.class);
                startActivity(intent);
            }
        });
        shezhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Tiaozhan_activity.this, Shezhi_activity.class);
                startActivity(intent);
            }
        });

        LinearLayout xxzx=(LinearLayout)findViewById(R.id.ch_xinxizhongxin);
        xxzx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent=new Intent();
                myIntent.setClass(Tiaozhan_activity.this,xinxizhongxin_activity.class);    //第一个参数不知道对不对
                Tiaozhan_activity.this.startActivity(myIntent);
            }
        });                                                                              //信息中心

        LinearLayout hylb=(LinearLayout)findViewById(R.id.ch_haoyouliebiao);
        hylb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent=new Intent();
                myIntent.setClass(Tiaozhan_activity.this,tongxunlu_activity.class);
                Tiaozhan_activity.this.startActivity(myIntent);
            }
        });                                                                             //好友列表->通讯录

        LinearLayout jfsc=(LinearLayout)findViewById(R.id.ch_jifenshangcheng);
        jfsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Tiaozhan_activity.this, "此功能尚未实现，敬请期待！",Toast.LENGTH_SHORT).show();
            }
        });                                                                             //积分商城

        LinearLayout phb=(LinearLayout)findViewById(R.id.ch_paihangbang);
        phb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Tiaozhan_activity.this, "此功能尚未实现，敬请期待！", Toast.LENGTH_SHORT).show();
            }
        });                                                                             //排行榜

        LinearLayout xtsz=(LinearLayout)findViewById(R.id.ch_xitongshezhi);
        xtsz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent=new Intent();
                myIntent.setClass(Tiaozhan_activity.this,Shezhi_activity.class);
                Tiaozhan_activity.this.startActivity(myIntent);
            }
        });                                                                             //系统设置->设置


    }
    public void dianjicehua(View view){
        mLeftMenu.toggle();

    }


}
