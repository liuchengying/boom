package boom.boom.liuyanbanpinglun;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.AsyncLoadAvatar;
import boom.boom.api.HttpIO;
import boom.boom.api.Static;
import boom.boom.api.SysApplication;
import boom.boom.api.Utils;
import boom.boom.liuyanhuifu.Liuyanhuifu_activity;
import boom.boom.myview.SildingFinishLayout;

/**
 * Created by lcy on 2015/5/27.
 */
public class Liuyanban_pinglun extends Activity {

    private ListView lv;
    private LinearLayout liuyanban_fh;
    private Button liuyan;
    private String ID;
    private SimpleAdapter mSimpleAdapter;
    private String nickname;
    private String text;
    private String time;
    private TextView nickname_tv;
    private TextView text_tv;
    private TextView time_tv;

    List<Map<String , Object>> listItem = new ArrayList<Map<String,Object>>();
    android.os.Handler myMessageHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mSimpleAdapter.notifyDataSetChanged();
        }
    };
    private void LoadData (){
        try {
            listItem.clear();
            final HttpIO io = new HttpIO(Utils.serveraddr + "/api/comment.php?action=getsingle&id=" + ID);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    io.SessionID = Static.session_id;
                    io.GETToHTTPServer();
                }
            }).start();
            while(io.getResultData() == null);
            String result = io.getResultData();
            JSONObject obj = new JSONObject(result);
            JSONObject refer_data = Utils.GetSubJSONObject(obj,"refer_data");
            int i = 0;
            JSONObject line;
            while((line = Utils.GetSubJSONObject(refer_data,"line"+i))!=null){
                HashMap<String,Object> map = new HashMap<>();
                map.put("ID",line.getString("ID"));
                map.put("text",line.getString("text_value"));
                map.put("date",line.getString("assign_date"));
                String positionID = line.getString("identifyDigit");
                map.put("nickname",(positionID.equals(Static.identifyDigit))?"我":line.getString("nickname"));
                map.put("avatar",line.getString("avatar"));
                listItem.add(map);
                i++;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.liuyanban_pinglun);
        SildingFinishLayout mSildingFinishLayout = (SildingFinishLayout) findViewById(R.id.sildingFinishLayout);
        mSildingFinishLayout
                .setOnSildingFinishListener(new SildingFinishLayout.OnSildingFinishListener() {

                    @Override
                    public void onSildingFinish() {
                        Liuyanban_pinglun.this.finish();
                    }
                });

        mSildingFinishLayout.setTouchView(mSildingFinishLayout);

        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        lv = (ListView) findViewById(R.id.liuyanban_pinglun_listview);
        //留言板评论listview

        ID = getIntent().getStringExtra("ID");
        nickname = getIntent().getStringExtra("nickname");
        text = getIntent().getStringExtra("text");
        time = getIntent().getStringExtra("time");
        nickname_tv = (TextView) findViewById(R.id.lypl_nickname);
        text_tv = (TextView) findViewById(R.id.lypl_text);
        time_tv = (TextView) findViewById(R.id.lypl_time);
        nickname_tv.setText(nickname);
        text_tv.setText(text);
        time_tv.setText(time);
        /**
         * 获取ListView组件。
         */
        lv = (ListView) findViewById(R.id.liuyanban_pinglun_listview);


        LoadData();
        /**
         * 生成数据。
         */
       /* for (int i = 0; i < 10; i++) {
            HashMap<String, Object> mMap = new HashMap<String, Object>();
            mMap.put("img", R.drawable.android_181);
            mMap.put("name", "第" + i+"人");
            mMap.put("text", "第" + i);
            mSelfData.add(mMap);
        }*/

        /**
         * 自定义Adapter。
         */
        mSimpleAdapter = new SimpleAdapter(Liuyanban_pinglun.this,listItem,R.layout.shipinpinglun_item,new String[]{
                "nickname","text","date","avatar"},new int[]{R.id.shipinpinglun_nickname,R.id.shipinpinglun_neirong,R.id.shipinpinglun_date,R.id.shipinpinglun_Item_touxiang}
        );
        mSimpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, final Object data, String textRepresentation) {
                Bitmap avatar;
                if (view instanceof ImageView && data instanceof String) {
                    ImageView imageView = (ImageView) view;
                    if ((avatar = AsyncLoadAvatar.GetLocalImage((String) data)) == null)           //获取存在本地的Bitmap
                    {
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (AsyncLoadAvatar.SaveBitmapToLocal(AsyncLoadAvatar.DownloadBitmap((String) data), (String) data));  //returned Bitmap   把Bitmap保存到本地
                                {
                                    Message msg = new Message();
                                    Liuyanban_pinglun.this.myMessageHandler.sendMessage(msg);
                                }
                            }
                        });
                        thread.start();
                        imageView.setImageResource(R.drawable.android_181);
                        return true;
                    } else {
                        imageView.setImageBitmap(avatar);
                        return true;
                    }
                }
                return false;
            }
        });
        /**
         * 向ListView设置Adapter。
         */
        lv.setAdapter(mSimpleAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(Liuyanban_pinglun.this, Liuyanhuifu_activity.class);
                intent.putExtra("nickname",(String)listItem.get(position).get("nickname"));
                intent.putExtra("ID",(String)listItem.get(position).get("ID"));
                intent.putExtra("type","5");
                startActivityForResult(intent, 1);
                overridePendingTransition(0, R.anim.liuyan_in);
            }
        });
        liuyan = (Button) findViewById(R.id.pinglun_liuyan);
        liuyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Liuyanban_pinglun.this,Liuyanhuifu_activity.class);
                intent.putExtra("type","4");
                intent.putExtra("nickname",nickname);
                intent.putExtra("ID",ID);
                startActivityForResult(intent,1);
                overridePendingTransition(0, R.anim.liuyan_in);
            }
        });
        liuyanban_fh = (LinearLayout) findViewById(R.id.liuyanban_fh);
        liuyanban_fh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0,R.anim.base_slide_right_out);
            }
        });
        final ToggleButton mTogBtn = (ToggleButton) findViewById(R.id.liuyanpinglun_toggle);


        mTogBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {

                    //togglebutton ture 的状态
                }else{
                    //togglebutton false 的状态

                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LoadData();
        mSimpleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }
}
