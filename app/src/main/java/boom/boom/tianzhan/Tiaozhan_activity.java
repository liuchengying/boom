package boom.boom.tianzhan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import boom.boom.R;
import boom.boom.guizejieshao.Guizejieshao_activity;
import boom.boom.shezhi.Shezhi_activity;
import boom.boom.slidingMenu.SlidingMenu;

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
    private TextView nickname;
    private TextView coins;

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
        nickname = (TextView) findViewById(R.id.nickname);
        coins = (TextView) findViewById(R.id.mycoins);
        Intent intent = getIntent();
        String user_name = intent.getStringExtra("name");
        String user_nickname = intent.getStringExtra("nickname");
        String user_coins = intent.getStringExtra("coins");
        nickname.setText(user_nickname);
        coins.setText("积分：" + user_coins);
    }
    public void dianjicehua(View view){
        mLeftMenu.toggle();

    }

    @Override
    public void onBackPressed() {
        exitDialog(Tiaozhan_activity.this, "QQ提示", "亲！您真的要退出吗？");

    }
    private void exitDialog(Context context, String title, String msg) {
        new AlertDialog.Builder(context).setTitle(title).setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(1);
                    }
                }).setNegativeButton("取消", null).create().show();
    }

}
