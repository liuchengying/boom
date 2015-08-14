package boom.boom.xinxizhongxin;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.AsyncLoadAvatar;
import boom.boom.api.Msg;
import boom.boom.api.Static;
import boom.boom.api.SysApplication;
import boom.boom.ExpandableTextView.ExpandableTextView;
import boom.boom.gerenzhuye.Gerenzhuye_activity;
import boom.boom.guizejieshao.Guizejieshao_activity;
import boom.boom.liuyanbanpinglun.Liuyanban_pinglun;
import boom.boom.myview.SildingFinishLayout;
import boom.boom.myview.XListView;
import boom.boom.shipintianzhanpinglun.Shipintianzhan_pinglun;

/**
 * Created by Lyp on 2015/1/22
 */

public class xinxizhongxin_activity extends Activity implements XListView.IXListViewListener{
    private XListView list;
    private final static String DATE_FORMAT_STR = "yyyy-MM-dd HH:mm";
    private Msg msg;
    ArrayList<HashMap<String,Object>> arrayList = new ArrayList<HashMap<String, Object>>();
    final ExpandableAdapter adapter = new ExpandableAdapter(xinxizhongxin_activity.this,arrayList);
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message m) {
            if(m.what == 1){
                arrayList = msg.GetList(false,xinxizhongxin_activity.this,arrayList);
                adapter.notifyDataSetChanged();

                // ExpandableAdapter.setListViewHeightBasedOnChildren(list);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int type = (int) adapter.list.get(position-1).get("type");
                        switch (type) {
                            case 1:
                                Intent intent = new Intent();
                                intent.putExtra("ID",(String)adapter.list.get(position-1).get("ID"));
                                intent.putExtra("cl_id",(String)adapter.list.get(position-1).get("cl_id"));
                                intent.setClass(xinxizhongxin_activity.this, Shipintianzhan_pinglun.class);
                                startActivity(intent);
                                break;
                            case 2:
                                Intent intent1 = new Intent();
                                intent1.putExtra("challenge_number",(String)adapter.list.get(position-1).get("identifyDigit"));
                                intent1.putExtra("ifFaqi",1);
                                intent1.setClass(xinxizhongxin_activity.this, Guizejieshao_activity.class);
                                startActivity(intent1);
                                break;
                            case 3:
                                Intent intent2 = new Intent();
                                intent2.putExtra("ID",(String)adapter.list.get(position-1).get("ID"));
                                intent2.putExtra("cl_id",(String)adapter.list.get(position-1).get("challenge_id"));
                                intent2.setClass(xinxizhongxin_activity.this, Shipintianzhan_pinglun.class);
                                startActivity(intent2);
                                break;
                            case 4:
                                ExpandableTextView e = (ExpandableTextView) view.findViewById(R.id.expand_text_view);
                                e.onClick(view);
                                break;
                            case 5:
                                Intent intent3 = new Intent();
                                intent3.putExtra("guestID",(String)adapter.list.get(position-1).get("host_id"));
                                intent3.putExtra("type",4);
                                intent3.putExtra("ID",(String)adapter.list.get(position-1).get("ID"));
                                intent3.setClass(xinxizhongxin_activity.this, Gerenzhuye_activity.class);
                                startActivity(intent3);
                                break;
                            case 6:
                                Intent intent4 = new Intent();
                                intent4.putExtra("ID",(String)adapter.list.get(position-1).get("ID"));
                                intent4.putExtra("cl_id",(String)adapter.list.get(position-1).get("challenge_id"));
/*                                intent4.putExtra("nickname",(String)adapter.list.get(position-1).get("nickname"));
                                intent4.putExtra("cl_name",(String)adapter.list.get(position-1).get("cl_name"));
                                intent4.putExtra("date",(String)adapter.list.get(position-1).get("date"));
                                intent4.putExtra("elapsed",(String)adapter.list.get(position-1).get("elapsed"));*/
                                intent4.setClass(xinxizhongxin_activity.this, Shipintianzhan_pinglun.class);
                                startActivity(intent4);
                                break;
                            case 7:
                                if((int)adapter.list.get(position-1).get("comment_type")<3){
                                    Intent intent5 = new Intent();
                                    intent5.putExtra("cl_id",(String)adapter.list.get(position-1).get("cl_id"));
                                    intent5.putExtra("ID",(String)adapter.list.get(position-1).get("ID"));
                           /* intent5.putExtra("nickname",(String)adapter.list.get(position-1).get("nickname"));
                            intent5.putExtra("cl_name",(String)adapter.list.get(position-1).get("text"));
                            intent5.putExtra("date",(String)adapter.list.get(position-1).get("date"));
                            intent5.putExtra("elapsed",(String)adapter.list.get(position-1).get("elapsed"));*/
                                    intent5.setClass(xinxizhongxin_activity.this, Shipintianzhan_pinglun.class);
                                    startActivity(intent5);
                                }else{
                                    Intent intent6 = new Intent();
                                    intent6.putExtra("ID",(String)adapter.list.get(position-1).get("ID"));
                           /* intent6.putExtra("nickname",(String)adapter.list.get(position-1).get("nickname"));
                            intent6.putExtra("text",(String)adapter.list.get(position-1).get("text"));
                            intent6.putExtra("date",(String)adapter.list.get(position-1).get("date"));*/
                                    intent6.setClass(xinxizhongxin_activity.this, Liuyanban_pinglun.class);
                                    startActivity(intent6);
                                }
                                break;
                            case 8:
                                Intent intent7 = new Intent();
                                intent7.putExtra("challenge_number", (int) adapter.list.get(position - 1).get("cl_id"));
                                intent7.putExtra("pf_iv",(int)adapter.list.get(position-1).get("pf_iv"));
                                intent7.putExtra("ifFaqi",1);
                                intent7.setClass(xinxizhongxin_activity.this,Guizejieshao_activity.class);
                                startActivity(intent7);
                                break;
                            case 9:
                                Intent intent8 = new Intent();
                                intent8.putExtra("guestID",(String)adapter.list.get(position-1).get("guest_id"));
                                intent8.putExtra("type",(boolean)adapter.list.get(position-1).get("pass")?1:2);
                                intent8.setClass(xinxizhongxin_activity.this,Gerenzhuye_activity.class);
                                startActivity(intent8);
                                break;
                            case 10:
                                break;
                            case 11:
                                Intent intent9 = new Intent();
                                intent9.putExtra("challenge_number",(String)adapter.list.get(position-1).get("identifyDigit"));
                                intent9.putExtra("ifFaqi",1);
                                intent9.setClass(xinxizhongxin_activity.this, Guizejieshao_activity.class);
                                startActivity(intent9);
                                break;
                            case 12:
                                Intent intent10 = new Intent();

                                if((int)adapter.list.get(position-1).get("commentType")<3){
                                    intent10.putExtra("cl_id",(String)adapter.list.get(position-1).get("cl_id"));
                                    intent10.putExtra("ID",(String)adapter.list.get(position-1).get("ID"));
                           /* intent5.putExtra("nickname",(String)adapter.list.get(position-1).get("nickname"));
                            intent5.putExtra("cl_name",(String)adapter.list.get(position-1).get("text"));
                            intent5.putExtra("date",(String)adapter.list.get(position-1).get("date"));
                            intent5.putExtra("elapsed",(String)adapter.list.get(position-1).get("elapsed"));*/
                                    intent10.setClass(xinxizhongxin_activity.this, Shipintianzhan_pinglun.class);
                                    startActivity(intent10);
                                }else{

                                    intent10.putExtra("ID",(String)adapter.list.get(position-1).get("ID"));
                           /* intent6.putExtra("nickname",(String)adapter.list.get(position-1).get("nickname"));
                            intent6.putExtra("text",(String)adapter.list.get(position-1).get("text"));
                            intent6.putExtra("date",(String)adapter.list.get(position-1).get("date"));*/
                                    intent10.setClass(xinxizhongxin_activity.this, Liuyanban_pinglun.class);
                                    startActivity(intent10);
                                }
                                break;
                        }
                    }
                });
                list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(getApplicationContext(), "删除、回信功能尚未实现，敬请期待。", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });

            }else {
                Toast.makeText(xinxizhongxin_activity.this,"网络连接错误！请检查网络连接",Toast.LENGTH_SHORT).show();
            }
            onLoad();
            return true;
        }
    });
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.xinxizhongxin);
        Static.badgeView.setBadgeCount(0);
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        RelativeLayout fh = (RelativeLayout) findViewById(R.id.xxzx_fanhui);
        fh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.base_slide_remain, R.anim.base_slide_right_out);
            }
        });

//        Button rightSideButton;
        final ArrayList<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
        list = (XListView) findViewById(R.id.challenge_list);
        list.setPullLoadEnable(false);
        list.setPullRefreshEnable(true);
        list.setXListViewListener(this);
        list.setAdapter(adapter);
        msg = new Msg(xinxizhongxin_activity.this,handler);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.base_slide_remain, R.anim.base_slide_right_out);
    }
    private void onLoad() {
        list.stopRefresh();
        list.stopLoadMore();
        list.setRefreshTime(new SimpleDateFormat(DATE_FORMAT_STR, Locale.CHINA).format(new Date()));
    }
    @Override
    public void onRefresh() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                msg = new Msg(xinxizhongxin_activity.this,handler);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Static.badgeView.setBadgeCount(0);
        onRefresh();
    }

    @Override
    public void onLoadMore() {

    }
}
    class ExpandableAdapter extends BaseAdapter {

    private final Context mContext;
    private final SparseBooleanArray mCollapsedStatus;
    private final SparseIntArray mCollapsedHeight;
    //private final String[] sampleStrings;
    public ArrayList<HashMap<String,Object>> list;
    private ArrayList<View> viewList = new ArrayList<>();
    public ExpandableAdapter(Context context,ArrayList<HashMap<String,Object>> arrlist) {
        mContext  = context;
        mCollapsedStatus = new SparseBooleanArray();
        mCollapsedHeight = new SparseIntArray();
        list = arrlist;
        //sampleStrings = mContext.getResources().getStringArray(R.array.sampleStrings);
    }

    @Override
    public int getCount() {
        int size = 0;
        try {
            size = list.size();
        }catch (Exception e){
            e.printStackTrace();
        }
        return size;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

        @Override
        public int getItemViewType(int position) {
            HashMap<String, Object> hashMap = list.get(position);
            int type = (int) hashMap.get("type");
            if(type == 4){
                return 0;
            }else {
                return 1;
            }
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }
        private void updateUI(View view,HashMap<String,Object>hashMap, ViewGroup parent){
            TextView title2 = (TextView) view.findViewById(R.id.xxzx_item_title);
            TextView text2 = (TextView) view.findViewById(R.id.xxzx_item_text);
            TextView date2 = (TextView) view.findViewById(R.id.xxzx_item_date);
            ImageView icon2 = (ImageView) view.findViewById(R.id.xxzx_item_icon);
            ImageView smallicon = (ImageView) view.findViewById(R.id.xxzx_item_smallicon);
            title2.setText((String) hashMap.get("title"));
            text2.setText((String) hashMap.get("content"));
            date2.setText((String) hashMap.get("date"));
            icon2.setImageBitmap((Bitmap) hashMap.get("icon"));
            Bitmap bmSmallicon = (Bitmap) hashMap.get("smallicon");
            if (bmSmallicon == null) {
                smallicon.setVisibility(View.INVISIBLE);
                ImageView whiteshader = (ImageView) view.findViewById(R.id.whiteshader);
                whiteshader.setVisibility(View.INVISIBLE);
            } else {
                smallicon.setImageBitmap(bmSmallicon);
            }
        }

        @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        System.gc();
            viewholder0 vh0 = null;
            viewholder1 vh1 = null;
            HashMap<String, Object> hashMap = list.get(position);
            int type = (int) hashMap.get("type");
        if(convertView != null){
            if(type == 4){
                vh0 = (viewholder0) convertView.getTag();
            }else {
                vh1 = (viewholder1) convertView.getTag();
            }
        }else {
            if(type == 4){
                vh0 = new viewholder0();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.xiaoxixiang, parent, false);
                vh0.expandableTextView = (ExpandableTextView) convertView.findViewById(R.id.expand_text_view);
                vh0.title = (TextView) convertView.findViewById(R.id.xxx_title);
                vh0.date = (TextView) convertView.findViewById(R.id.xxx_time);
                vh0.icon = (ImageView) convertView.findViewById(R.id.xxx_cursor);
                convertView.setTag(vh0);
            }else {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.xiaoxizhongxin_item2, parent, false);
                vh1 = new viewholder1();
                vh1.title2 = (TextView) convertView.findViewById(R.id.xxzx_item_title);
                vh1.text2 = (TextView) convertView.findViewById(R.id.xxzx_item_text);
                vh1.date2 = (TextView) convertView.findViewById(R.id.xxzx_item_date);
                vh1.icon2 = (ImageView) convertView.findViewById(R.id.xxzx_item_icon);
                vh1.smallicon = (ImageView) convertView.findViewById(R.id.xxzx_item_smallicon);
                vh1.whiteshader = (ImageView) convertView.findViewById(R.id.whiteshader);
                convertView.setTag(vh1);
            }
        }



                if(type == 4){
                    //ExpandableTextView expandableTextView = (ExpandableTextView) convertView.findViewById(R.id.expand_text_view);
                    vh0.expandableTextView.setText(((String) hashMap.get("content")), mCollapsedStatus,mCollapsedHeight, position);
                    //title = (TextView) convertView.findViewById(R.id.xxx_title);
                    //TextView date = (TextView) convertView.findViewById(R.id.xxx_time);
                    //ImageView icon = (ImageView) convertView.findViewById(R.id.xxx_cursor);
                    vh0.title.setText((String) hashMap.get("title"));
                    vh0.date.setText((String) hashMap.get("date"));
                    vh0.icon.setImageDrawable(new BitmapDrawable((Bitmap) hashMap.get("icon")));
                }else {
                    vh1.title2.setText((String) hashMap.get("title"));
                    vh1.text2.setText((String) hashMap.get("content"));
                    vh1.date2.setText((String) hashMap.get("date"));
                    vh1.icon2.setImageBitmap((Bitmap) hashMap.get("icon"));
                    Bitmap bmSmallicon = (Bitmap) hashMap.get("smallicon");
                    if (bmSmallicon == null) {
                        vh1.smallicon.setVisibility(View.INVISIBLE);
                        vh1.whiteshader.setVisibility(View.INVISIBLE);
                    } else {
                        vh1.smallicon.setImageBitmap(bmSmallicon);
                    }
                    switch (type){
                        case 5:
                        case 7:
                        case 8:
                        case 9:
                        case 12:
                            final ImageView imgviewAvatar = (hashMap.get("smallavatar")!=null)?(vh1.smallicon):(vh1.icon2);//(ImageView) convertView.findViewById((hashMap.get("smallavatar")!=null)?R.id.xxzx_item_smallicon:R.id.xxzx_item_icon);
                            final String data = (String) hashMap.get("avatar");
                            final Drawable avatar;
                            if ((avatar = AsyncLoadAvatar.GetLocalImage(mContext, (String) data)) == null)           //获取存在本地的Bitmap
                            {
                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (AsyncLoadAvatar.SaveBitmapToLocal(AsyncLoadAvatar.DownloadBitmap((String) data), (String) data));  //returned Bitmap   把Bitmap保存到本地
                                        {
                                            ((Activity)mContext).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    imgviewAvatar.setImageDrawable(AsyncLoadAvatar.GetLocalImage(mContext, (String) data));
                                                }
                                            });
                                        }
                                    }
                                });
                                thread.start();
                            }else {
                                imgviewAvatar.setImageDrawable(avatar);
                                //avatar.recycle();
                            }
                            break;
                        default:

                    }

                }

            return convertView;
        }
        class viewholder0{
            ExpandableTextView expandableTextView;
            TextView title;
            TextView date;
            ImageView icon;
        }
        class viewholder1{
            TextView title2;
            TextView text2;
            TextView date2;
            ImageView icon2;
            ImageView smallicon;
            ImageView whiteshader;
        }
}