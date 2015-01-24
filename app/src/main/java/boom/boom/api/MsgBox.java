package boom.boom.api;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import boom.boom.R;

public class MsgBox extends Activity {

    /*
     *  MsgBox：在页面上绘制一个MsgBox弹窗。
     *  示例：
     *
     *  class MainActivity extends Activity{
     *  private final int REQUESTCODE=1;    // 返回的结果码
     *
     *  public void onCreate(Bundle savedInstanceState){
     *      super.onCreate(savedInstanceState);
     *
     *      //
     *      // ... 你的代码
     *      //
     *
     *      Intent intent = new Intent(MainActivity.this, MsgBox.class);
     *      intent.putExtra("title", "我的标题");   // 这里编写标题
     *      intent.putExtra("text", "hello world!");    // 这里编写弹窗正文
     *      intent.putExtra("flags", MsgBox.MB_YESNO); // 设置确定取消按钮类型
     *      startActivityForResult(intent, REQUESTCODE);    //返回结果
     *  }
     *  @Override
     *  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     *      super.onActivityResult(requestCode, resultCode, data);
     *      switch (resultCode){
     *      case 1:
     *          Toast.makeText(getApplicationContext(), "你点击了是/OK/确定！", Toast.LENGTH_SHORT).show();
     *      break;
     *      case 0:
     *          Toast.makeText(getApplicationContext(), "你点击了否/Cancel/取消！", Toast.LENGTH_SHORT).show();
     *      break;
     *      }
     *  }
     *
     *  //
     *  // ...
     *  //
     *
     *  }
     *
     */

    public static final int MB_OK = 5234543;
    /*
     *  MB_OK   只显示一个确定按钮，返回值恒为1。
     */
    public static final int MB_YESNO = 2343;
    /*
     *  MB_YRSNO    显示两个按钮“确定”、“取消”，返回值确定为1，取消为0。
     */
    public static final int MB_RETRY = 23455;
    /*
     *  MB_RETRY    显示两个按钮“重试”、“放弃”，返回值重试为1，放弃为0。
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String text = intent.getStringExtra("text");
        int Flags = intent.getIntExtra("flags", MB_OK);
        switch (Flags){
            case MB_OK: {
                setContentView(R.layout.msgbox_ok);
                TextView _title = (TextView) findViewById(R.id.ok_title);
                TextView _text = (TextView) findViewById(R.id.ok_text);
                Button button = (Button) findViewById(R.id.ok_button);
                _title.setText(title);
                _text.setText(text);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("result", 0);
                        setResult(0, intent);
                        finish();
                    }
                });
            }
                break;
            case MB_YESNO: {
                setContentView(R.layout.msgbox_yesno);
                TextView _title = (TextView) findViewById(R.id.yn_title);
                TextView _text = (TextView) findViewById(R.id.yn_text);
                Button button_yes = (Button) findViewById(R.id.button_ok_yn);
                Button button_no = (Button) findViewById(R.id.button_cancel_yn);
                _title.setText(title);
                _text.setText(text);
                button_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("result", 1);
                        setResult(1, intent);
                        finish();
                    }
                });
                button_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("result", 0);
                        setResult(0, intent);
                        finish();
                    }
                });
            }
                break;
            case MB_RETRY:{
                setContentView(R.layout.msgbox_yesno);
                TextView _title = (TextView) findViewById(R.id.rt_title);
                TextView _text = (TextView) findViewById(R.id.rt_text);
                Button button_yes = (Button) findViewById(R.id.rt_retry);
                Button button_no = (Button) findViewById(R.id.rt_cancel);
                _title.setText(title);
                _text.setText(text);
                button_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("result", 1);
                        setResult(1, intent);
                        finish();
                    }
                });
                button_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("result", 0);
                        setResult(0, intent);
                        finish();
                    }
                });
            }
            break;
        }
        finish();
    }
}
