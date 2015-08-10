package boom.boom.liuyanhuifu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URLEncoder;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.HttpIO;
import boom.boom.api.Static;
import boom.boom.api.SysApplication;
import boom.boom.api.Utils;

/**
 * Created by lcy on 2015/5/28.
 */
public class Liuyanhuifu_activity extends Activity {
    private RelativeLayout liuyanhuifu_fh;
    private EditText commentText;
    private String ID;
    private Button commit;
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pinglun_shurukuang);
        SysApplication.getInstance().addActivity(this);
        Intent intent = getIntent();
        ID = intent.getStringExtra("ID");
        type = intent.getStringExtra("type");
        commentText = (EditText) findViewById(R.id.commentText);
        commit = (Button) findViewById(R.id.submit_liuyan);
        commentText.setHint("回复"+intent.getStringExtra("nickname")+":");
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        liuyanhuifu_fh = (RelativeLayout) findViewById(R.id.liuyan_fh);
        liuyanhuifu_fh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(0,R.anim.liuyan_out);
            }
        });
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = commentText.getText().toString();
                comment = comment.replace("\n","\\n");
                try {
                    final HttpIO io = new HttpIO(Utils.serveraddr + "/api/comment.php?action=refer&type=" + type + "&id=" + ID + "&comment=" + URLEncoder.encode(comment, "UTF-8"));
                    if (type.equals("3")) {
                        io.SetURL(Utils.serveraddr + "/api/comment.php?action=comment&type=3&comment=" + URLEncoder.encode(comment, "UTF-8") + "&positionId=" + ID);
                    }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        io.SessionID = Static.session_id;
                        io.getJson();
                    }
                }).start();
                while(io.getResultData() == null);
                try {
                    JSONObject obj = new JSONObject(io.getResultData());
                    if(obj.getString("state").equals("SUCCESS")){
                        Toast.makeText(Liuyanhuifu_activity.this,"留言成功！",Toast.LENGTH_SHORT).show();
                        setResult(1);
                        finish();
                        overridePendingTransition(0,R.anim.liuyan_out);
                    }else {
                        Toast.makeText(Liuyanhuifu_activity.this,"留言失败！",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(Liuyanhuifu_activity.this,"留言失败！",Toast.LENGTH_SHORT).show();
                }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
