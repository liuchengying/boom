package boom.boom.liuyan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.HttpIO;
import boom.boom.api.Static;
import boom.boom.api.SysApplication;
import boom.boom.api.Utils;
import boom.boom.tianzhan.Tiaozhan_activity;

/**
 * Created by lcy on 2015/5/7.
 */
public class Liuyan_activity extends Activity {
    private RelativeLayout liuyan_fh;
    private TextView    commentText;
    private Button      button_onSubmit;
    private String      commentStr = null;
    private static String      resultStr = null;
    private Thread      thread_onsubmitData = new Thread(new Runnable() {
        @Override
        public void run() {
            Utils.GetBuilder get = new Utils.GetBuilder(Utils.serveraddr + Utils.comment_api);
            get.addItem("action", "comment");
            get.addItem("type", "1");
            get.addItem("comment", commentStr);
            get.addItem("positionId", Static.identifyDigit);
            HttpIO io = new HttpIO(get.toString());
            io.SetCustomSessionID(Static.session_id);
            io.GETToHTTPServer();
            resultStr = io.getResultData();
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.liuyan);
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        commentText = (TextView) findViewById(R.id.commentText);
        button_onSubmit = (Button) findViewById(R.id.submit_liuyan);
        button_onSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                http://172.24.10.118/api/comment.php?action=comment&type=1&comment=hello&positionId=10002
                commentStr = commentText.getText().toString();
                thread_onsubmitData.start();
                while (resultStr == null){
                    Log.e("Liuyan", "UI thread reply string ==> " + resultStr);
                }
                try {
                    String result_state = new JSONObject(resultStr).getString("state");
                    if (result_state.equals("SUCCESS")) {
                        onLiuyanSubmitOK();
                    }else{
                        onLiuyanSubmitFailed();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    onLiuyanSubmitFailed();
                }
            }
        });
        liuyan_fh = (RelativeLayout) findViewById(R.id.liuyan_fh);
        liuyan_fh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0,R.anim.liuyan_out);
            }
        });

    }
    public void onLiuyanSubmitOK(){
        Intent intent = new Intent();
        intent.setClass(Liuyan_activity.this, Tiaozhan_activity.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "发布留言成功。", Toast.LENGTH_SHORT).show();
        this.finish();
    }

    public void onLiuyanSubmitFailed(){
        Toast.makeText(getApplicationContext(), "发布留言失败(-｡-;)", Toast.LENGTH_SHORT).show();
    }
}
