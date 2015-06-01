package boom.boom.tongxunlu;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.AsyncLoadAvatar;
import boom.boom.api.FriendList;
import boom.boom.api.HttpIO;
import boom.boom.api.Static;
import boom.boom.api.SysApplication;


import boom.boom.api.Utils;
import boom.boom.haoyouliebiao.Haoyouliebiao_activity;
import boom.boom.mimaxiugai.Mimaxiugai_activity;
import boom.boom.myview.RoundedImageView;
import boom.boom.myview.SildingFinishLayout;

import static android.app.PendingIntent.getActivity;

/**
 * Created by Lyp on 2015/1/22.
 */
public class tongxunlu_activity extends Activity{
    LinearLayout horizon;
    String result = null;
    ArrayList<HashMap<String,Object>> listItem;
    MyAdapter mSimpleAdapter;
    String cl_id;
    android.os.Handler myMessageHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mSimpleAdapter.notifyDataSetChanged();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tongxunlu);
        SildingFinishLayout mSildingFinishLayout = (SildingFinishLayout) findViewById(R.id.sildingFinishLayout);
        mSildingFinishLayout
                .setOnSildingFinishListener(new SildingFinishLayout.OnSildingFinishListener() {
                    @Override
                    public void onSildingFinish() {
                        tongxunlu_activity.this.finish();
                    }
                });

        mSildingFinishLayout.setTouchView(mSildingFinishLayout);
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        ListView lv = (ListView) findViewById(R.id.tongxunlu_listview);

        horizon=(LinearLayout)findViewById(R.id.horizon);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                FriendList friendList = new FriendList();
                listItem = friendList.GetFriendList();
            }
        });
        thread.start();
        while(listItem==null);
        cl_id = (String)getIntent().getSerializableExtra("challenge_id");
        mSimpleAdapter=new MyAdapter(listItem,this);
        lv.setAdapter(mSimpleAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSimpleAdapter.select(position);
            }
        });

        LinearLayout fanhui= (LinearLayout) findViewById(R.id.txl_fh);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, R.anim.base_slide_right_out);
            }
        });

        LinearLayout txl_finish = (LinearLayout)findViewById(R.id.txl_finish);
        txl_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final HttpIO io = new HttpIO(Utils.serveraddr + "/api/getChallenge.php?action=invite_post&cl_id="+cl_id);
                Thread thread1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int sum = 0;
                           // 初始化一个连接（此时客户端并未访问服务器）
                        List<NameValuePair> post = new ArrayList<NameValuePair>();
                        for(int i=0;i<mSimpleAdapter.getIsSelected().size();i++)
                        {
                            if(mSimpleAdapter.getIsSelected().get(i)) {

                                post.add(new BasicNameValuePair("fri"+sum, (String)listItem.get(i).get("guestID"))); // 增加POST表单数据
                                sum++;
                            }
                        }
                        io.SessionID = Static.session_id;
                        io.POSTToHTTPServer(post); // 发送访问请求和POST数据
                        result = io.getResultData();
                    }
                });
                thread1.start();
                while(result==null);
                try{
                    JSONObject obj = new JSONObject(result);
                    if(obj.getString("state").equals("SUCCESS"))
                    {
                        Toast.makeText(tongxunlu_activity.this,"挑战邀请发送成功！",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        Toast.makeText(tongxunlu_activity.this,"挑战邀请失败！",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }


    public class MyAdapter extends BaseAdapter {
        // 填充数据的list
        private ArrayList<HashMap<String,Object>> list;
        // 用来控制CheckBox的选中状况
        public HashMap<Integer,Boolean> isSelected;

        // 上下文
        private Context context;
        // 用来导入布局
        private LayoutInflater inflater = null;

        // 构造器
        public MyAdapter(ArrayList<HashMap<String,Object>> list, Context context) {
            this.context = context;
            this.list = list;
            inflater = LayoutInflater.from(context);
            isSelected = new HashMap<Integer, Boolean>();
            // 初始化数据
            initDate();

        }

        // 初始化isSelected的数据
        private void initDate(){
            for(int i=0; i<list.size();i++) {
                getIsSelected().put(i,false);
            }
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        void selectedChanged()
        {
            horizon.removeAllViews();
            for(int i=0;i<getIsSelected().size();i++)
            {
                if(getIsSelected().get(i)){
                    RoundedImageView imageView=new RoundedImageView(tongxunlu_activity.this);
                    imageView.setImageResource(R.drawable.android_181);
                    imageView.setPadding(10, 0, 0, 0);
                    imageView.setId(i);
                    horizon.addView(imageView);
                    Bitmap avatar;
                    final Object data = list.get(i).get("avatar");
                    if ((avatar = AsyncLoadAvatar.GetLocalImage((String) data)) == null)           //获取存在本地的Bitmap
                    {
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (AsyncLoadAvatar.SaveBitmapToLocal(AsyncLoadAvatar.DownloadBitmap((String) data), (String) data));  //returned Bitmap   把Bitmap保存到本地
                                {
                                    Message msg = new Message();
                                    tongxunlu_activity.this.myMessageHandler.sendMessage(msg);
                                }
                            }
                        });
                        thread.start();
                        imageView.setImageResource(R.drawable.android_181);
                    } else {
                        avatar = Utils.zoomImage(avatar,85,85);

                        imageView.setImageBitmap(avatar);
                    }

                }
            }
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                // 获得ViewHolder对象
                holder = new ViewHolder();
                // 导入布局并赋值给convertview
                convertView = inflater.inflate(R.layout.tongxunlu_listview_item, null);
                holder.tv = (TextView) convertView.findViewById(R.id.tongxunlu_nicheng);
                holder.cb = (CheckBox) convertView.findViewById(R.id.tongxunlu_check);
                holder.iv = (RoundedImageView) convertView.findViewById(R.id.tongxunlu_touxiang);
                // 为view设置标签
                convertView.setTag(holder);
            } else {
                // 取出holder
                holder = (ViewHolder) convertView.getTag();
            }
            // 设置list中TextView的显示
            holder.tv.setText((String)list.get(position).get("nickname"));
            // 根据isSelected来设置checkbox的选中状况
            holder.cb.setChecked(getIsSelected().get(position));
            Bitmap avatar;
            final Object data = list.get(position).get("avatar");
            if ((avatar = AsyncLoadAvatar.GetLocalImage((String) data)) == null)           //获取存在本地的Bitmap
            {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (AsyncLoadAvatar.SaveBitmapToLocal(AsyncLoadAvatar.DownloadBitmap((String) data), (String) data));  //returned Bitmap   把Bitmap保存到本地
                        {
                            Message msg = new Message();
                            tongxunlu_activity.this.myMessageHandler.sendMessage(msg);
                        }
                    }
                });
                thread.start();
                holder.iv.setImageResource(R.drawable.android_181);

            } else {
                holder.iv.setImageBitmap(avatar);
            }
            return convertView;
        }

        public HashMap<Integer,Boolean> getIsSelected() {
            return isSelected;
        }

        public void select(int position) {
            getIsSelected().put(position,!getIsSelected().get(position));
            notifyDataSetChanged();
            selectedChanged();
        }
class ViewHolder
{
            TextView tv;
            CheckBox cb;
            RoundedImageView iv;
            Bitmap bp;
}
    }//MyAdapter
}//tongxunlu_activity
