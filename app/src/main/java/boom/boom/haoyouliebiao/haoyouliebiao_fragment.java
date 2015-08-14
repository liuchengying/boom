package boom.boom.haoyouliebiao;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.AsyncLoadAvatar;
import boom.boom.api.FriendList;
import boom.boom.api.SysApplication;
import boom.boom.gerenzhuye.Gerenzhuye_activity;
import boom.boom.search_friends.Search_friends_activity;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by Administrator on 2015/7/11.
 */
public class haoyouliebiao_fragment extends Fragment {


    ListView lv;
    LinearLayout invite;
    RelativeLayout haoyouliebiao_fh;
    SimpleAdapter mSimpleAdapter;
    TextView hylb_search;
    FriendList friendList;
    ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();
    android.os.Handler myMessageHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mSimpleAdapter.notifyDataSetChanged();
        }
    };
    Handler getListHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == 1){
                friendList.GetFriendList(listItem);

                mSimpleAdapter.notifyDataSetChanged();
            }else {
                Toast.makeText(getActivity(), "网络连接错误！请检查网络连接", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    });
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("BOOM--民间吉尼斯");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://boom.corling.com");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("撕逼挑战神器一一BOOM，全国首款让用户以视频的方式展现自己特殊技能的短视频挑战应用，全新的互动体验，千奇百怪的挑战内容、实时的动态排名，以及享受游戏给用户带来的无尽的乐趣");
        oks.setImageUrl("http://boom.corling.com/resources/boom_avatar.png");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/small.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("asdasdasd");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://boom.corling.com");

// 启动分享GUI
        oks.show(getActivity());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.haoyouliebiao_frame,container,false);
        {
            try {
                SysApplication.getInstance().addActivity(getActivity());
                FontManager.changeFonts(FontManager.getContentView(getActivity()), getActivity());//字体
                lv = (ListView) v.findViewById(R.id.haoyouliebiao_listview);
                friendList = new FriendList(getActivity(),getListHandler);
                mSimpleAdapter = new SimpleAdapter(getActivity(), listItem,//需要绑定的数据
                        R.layout.haoyouliebiao_item_down,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                        new String[]{
                                "avatar", "nickname"},
                        new int[]{R.id.haoyouliebiao_touxiang, R.id.haoyouliebiao_nicheng}
                );
                invite = (LinearLayout) v.findViewById(R.id.haoyouiebiao_yaoqing);
                invite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showShare();
                    }
                });
                mSimpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                    @Override
                    public boolean setViewValue(View view, final Object data, String textRepresentation) {
                        Drawable avatar;
                        if (view instanceof ImageView && data instanceof String) {
                            ImageView imageView = (ImageView) view;
                            if ((avatar = AsyncLoadAvatar.GetLocalImage(getActivity(),(String) data)) == null)           //获取存在本地的Bitmap
                            {
                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (AsyncLoadAvatar.SaveBitmapToLocal(AsyncLoadAvatar.DownloadBitmap((String) data), (String) data));  //returned Bitmap   把Bitmap保存到本地
                                        {
                                            Message msg = new Message();
                                            myMessageHandler.sendMessage(msg);
                                        }
                                    }
                                });
                                thread.start();
                                imageView.setImageResource(R.drawable.android_181);
                                return true;
                            } else {
                                imageView.setImageDrawable(avatar);
                                return true;
                            }
                        }
                        return false;
                    }
                });
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String guestID = (String) ((HashMap<String, Object>) listItem.get(position)).get("guestID");
                        Intent intent = new Intent();
                        intent.putExtra("guestID", guestID);
                        intent.putExtra("type",1);
                        intent.setClass(getActivity(), Gerenzhuye_activity.class);
                        startActivity(intent);
                    }
                });

                hylb_search = (TextView) v.findViewById(R.id.hylb_search);
                hylb_search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent();
                        intent.setClass(getActivity(), Search_friends_activity.class);
                        startActivity(intent);
                    }
                });
                lv.setAdapter(mSimpleAdapter);
                haoyouliebiao_fh = (RelativeLayout) getActivity().findViewById(R.id.haoyouliebiao_fh);
                haoyouliebiao_fh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().finish();
                        getActivity().overridePendingTransition(R.anim.base_slide_remain, R.anim.base_slide_right_out);
                    }
                });
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return v;
    }
}
