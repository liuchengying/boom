package boom.boom.denglu;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import boom.boom.R;
import boom.boom.api.LoginUser;
import boom.boom.api.SysApplication;
import boom.boom.zhujiemian.Main_activity;

/**
 * Created by 刘成英 on 2015/1/13.
 */
public class dengluzhuce_activity extends Activity {
    private Button fanhui;
    private Button zhucetijiao;
    private EditText text_username;
    private EditText text_password;
    private EditText text_password2;
    private MenuInflater mi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhuce);
        SysApplication.getInstance().addActivity(this);
        zhucetijiao = (Button) findViewById(R.id.zhucetijiao);
        fanhui = (Button) findViewById(R.id.zhucufanhui);
        text_username = (EditText) findViewById(R.id.zhuceyonghuming);
        text_password = (EditText) findViewById(R.id.zhucemima);
        text_password2 = (EditText) findViewById(R.id.zhucemima);
        mi = new MenuInflater(this);
        zhucetijiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text_password.getText().toString().equals(text_password2.getText().toString())) {
                    LoginUser user = new LoginUser(text_username.getText().toString(), text_password.getText().toString());
                    if (user.AttemptToRegisted()) {
                        Intent intent = new Intent();
                        intent.setClass(dengluzhuce_activity.this, Main_activity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "注册失败！服务器返回错误：" + user.GetLastError(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "两次用户名密码不相符。", Toast.LENGTH_SHORT).show();
                }
            }
        });
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(dengluzhuce_activity.this, Main_activity.class);
                startActivity(intent);

            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        mi.inflate(R.menu.exit_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_exit:
                exitDialog(dengluzhuce_activity.this, "BOOM提示", "亲！您真的要退出吗？");
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void exitDialog(Context context, String title, String msg) {
        new AlertDialog.Builder(context).setTitle(title).setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Context.APP_OPS_SERVICE);
                        startActivity(intent);


                    }


                }).setNegativeButton("取消", null).create().show();


    }
}