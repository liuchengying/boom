package boom.boom.gerenzhuye;


import android.app.Activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;

//import android.app.FragmentActivity;
//import android.FragmentPagerAdapter;
//import android.view.ViewPager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.AsyncLoadAvatar;
import boom.boom.api.HttpIO;
import boom.boom.api.Static;
import boom.boom.api.SysApplication;
import boom.boom.api.Utils;


import boom.boom.liuyanhuifu.Liuyanhuifu_activity;
import boom.boom.myview.CircleImageView;
import boom.boom.myview.SildingFinishLayout;



/**
 * Created by 刘成英 on 2015/3/12.
 */
public class  Gerenzhuye_activity extends FragmentActivity
{
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mDatas;
    private TextView username;
    private TextView sign;
    private TextView mChatTextView;
    private TextView mFriendTextView;
    private TextView mContactTextView;
    private LinearLayout mChatLinearLayout;
    private RelativeLayout gerenzhuyefanhui;
    private TextView l1,l2,l3;
    private CircleImageView gerenzhuye_touxing;
    public static JSONObject obj;
    private String result;
    private Bitmap avatar;
    private ImageView mTabline;
    private int mScreen1_3;
    private String data;
    private int mCurrentPageIndex;
    private String guestID;
    public LinearLayout allLinear;
    private Button liuyan;
    private LinearLayout tjhy_ll;
    private TextView tjhy_tv;
    private LinearLayout tyjj_ll;
    private Button agree;
    private Button disagree;
    private int type;
    public String nickname;
    private String ID;
    android.os.Handler myMessageHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            gerenzhuye_touxing.setImageBitmap(avatar);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.gerenzhuye);
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        tjhy_ll = (LinearLayout) findViewById(R.id.grzy_tjhy_ll);
        tjhy_tv = (TextView) findViewById(R.id.grzy_tjhy_tv);
        tyjj_ll = (LinearLayout) findViewById(R.id.grzy_tyjj_ll);
        agree = (Button) findViewById(R.id.grzy_ty_bt);
        disagree = (Button) findViewById(R.id.grzy_jj_bt);
        guestID = getIntent().getStringExtra("guestID");
        type = getIntent().getIntExtra("type",1);
        ID = getIntent().getStringExtra("ID");






        switch (type)
        {
            case 1://都不显示
            {
                tjhy_ll.setVisibility(View.INVISIBLE);
                tyjj_ll.setVisibility(View.INVISIBLE);
                break;
            }
            case 2://显示添加好友
            {
                tjhy_ll.setVisibility(View.VISIBLE);
                final HttpIO io = new HttpIO(Utils.serveraddr + "api/newfriend.php?action=verify&guest_id="+guestID);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        io.SessionID = Static.session_id;
                        io.getJson();
                    }
                }).start();
                while(io.getResultData() == null);
                String response = null;
                try {
                    JSONObject obj = new JSONObject(io.getResultData());
                    response = obj.getString("status");
                }catch (Exception e){
                    e.printStackTrace();
                }

                if(response.equals("FRIENDSHIP_NOT_EXISTED")){
                    tjhy_tv.setText("添 加 好 友");
                    tjhy_ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final HttpIO io = new HttpIO(Utils.serveraddr + "api/newfriend.php?action=newfriend&guest="+guestID);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    io.SessionID = Static.session_id;
                                    io.getJson();
                                }
                            }).start();
                            while(io.getResultData() == null);
                            tjhy_tv.setText("等 待 通 过");
                        }
                    });
                }else if(response.equals("AWAITING_FRIENDS_ACCEPTED")) {
                    tjhy_tv.setText("等 待 通 过");
                }


                tyjj_ll.setVisibility(View.INVISIBLE);
                break;
            }
            case 3://显示等待通过
            {
                tjhy_ll.setVisibility(View.VISIBLE);
                tjhy_tv.setText("等 待 通 过");
                tyjj_ll.setVisibility(View.INVISIBLE);
                break;
            }
            case 4://显示拒绝添加
            {
                final HttpIO io = new HttpIO(Utils.serveraddr + "api/newfriend.php?action=verify&guest_id="+guestID);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        io.SessionID = Static.session_id;
                        io.getJson();
                    }
                }).start();
                while(io.getResultData() == null);
                String response = null;
                try {
                    JSONObject obj = new JSONObject(io.getResultData());
                    response = obj.getString("status");
                }catch (Exception e){
                    e.printStackTrace();
                }

                if(response.equals("FRIENDSHIP_NOT_EXISTED")){
                    tjhy_ll.setVisibility(View.VISIBLE);
                    tjhy_tv.setText("您 已 拒 绝");
                    tyjj_ll.setVisibility(View.INVISIBLE);
                }else if(response.equals("AWAITING_FRIENDS_ACCEPTED")) {
                    tjhy_ll.setVisibility(View.INVISIBLE);
                    tyjj_ll.setVisibility(View.VISIBLE);
                }else if(response.equals("FRIENDSHIP_EXISTED")){
                    tjhy_ll.setVisibility(View.INVISIBLE);
                    tyjj_ll.setVisibility(View.INVISIBLE);
                }

                agree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final HttpIO io = new HttpIO(Utils.serveraddr + "/api/newfriend.php?action=verify_friend&state=1&id="+ID);
                        io.SessionID = Static.session_id;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                io.GETToHTTPServer();
                            }
                        }).start();
                        while(io.getResultData() == null);
                        tjhy_ll.setVisibility(View.INVISIBLE);
                        tyjj_ll.setVisibility(View.INVISIBLE);
                    }
                });
                disagree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final HttpIO io = new HttpIO(Utils.serveraddr + "/api/newfriend.php?action=verify_friend&state=2&id="+ID);
                        io.SessionID = Static.session_id;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                io.GETToHTTPServer();
                            }
                        }).start();
                        while(io.getResultData() == null);
                        tjhy_ll.setVisibility(View.VISIBLE);
                        tjhy_tv.setText("您 已 拒 绝");
                        tyjj_ll.setVisibility(View.INVISIBLE);
                    }
                });
                break;
            }
        }

        SildingFinishLayout mSildingFinishLayout = (SildingFinishLayout) findViewById(R.id.sildingFinishLayout1);
        mSildingFinishLayout
                .setOnSildingFinishListener(new SildingFinishLayout.OnSildingFinishListener() {

                    @Override
                    public void onSildingFinish() {
                        Gerenzhuye_activity.this.finish();
                    }
                });

        mSildingFinishLayout.setTouchView(mSildingFinishLayout);
        allLinear = (LinearLayout) findViewById(R.id.zhengge);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //http://172.24.10.118/api/userdata.php?action=getFriends&guest_id=10000
                Utils.GetBuilder get  = new Utils.GetBuilder(Utils.serveraddr + Utils.userdata_api);
                get.addItem("action", "getFriends");
                get.addItem("guest_id",guestID);
                HttpIO io = new HttpIO(get.toString());
                io.SetCustomSessionID(Static.session_id);
                io.GETToHTTPServer();
                switch (io.LastError){
                    case HttpIO.CONNECTION_TIMED_OUT:
                        result = "TIME_OUT";
                        break;
                    default:
                        result = io.getResultData();
                        break;
                }
            }
        }).start();
        while (result == null);
        try {
            JSONObject obj = Utils.GetSubJSONObject(new JSONObject(result), "data");

            username = (TextView) findViewById(R.id.gerenzhuye_yonghuming);
            nickname = obj.getString("nickname");
            username.setText(nickname);
            sign = (TextView) findViewById(R.id.gerenzhuye_qianming);
            sign.setText(obj.getString("uniquesign"));
            gerenzhuye_touxing = (CircleImageView) findViewById(R.id.gerenzhuye_touxiang);
            data = obj.getString("avatar");
            if ((avatar = AsyncLoadAvatar.GetLocalImage((String) data)) == null)           //获取存在本地的Bitmap
            {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (AsyncLoadAvatar.SaveBitmapToLocal(AsyncLoadAvatar.DownloadBitmap((String) data), (String) data));  //returned Bitmap   把Bitmap保存到本地
                        {
                            Message m = new Message();
                            Gerenzhuye_activity.this.myMessageHandler.sendMessage(m);
                        }
                    }
                });
                thread.start();
                gerenzhuye_touxing.setImageResource(R.drawable.android_181);

            } else {
                gerenzhuye_touxing.setImageBitmap(avatar);
            }
            gerenzhuyefanhui = (RelativeLayout) findViewById(R.id.gerenzhuyefanhui);
            gerenzhuyefanhui.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(0, R.anim.base_slide_right_out);
                }
            });
            jiantingqiehuan();
            initTabLine();
            initView();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        liuyan = (Button) findViewById(R.id.liuyan_button);
        liuyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Gerenzhuye_activity.this,Liuyanhuifu_activity.class);
                intent.putExtra("nickname",nickname);
                intent.putExtra("ID",guestID);
                intent.putExtra("type", "3");
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.liuyan_in,0);
            }
        });
    }


    private void jiantingqiehuan(){
        l1 = (TextView)findViewById(R.id.grzy_1);
        l2 = (TextView)findViewById(R.id.grzy_2);
        l3 = (TextView)findViewById(R.id.grzy_3);

        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
            }
        });
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);
            }
        });
        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(2);
            }
        });

    }
    private void initTabLine(){
        mTabline = (ImageView) findViewById(R.id.gundongtiao);
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        mScreen1_3 = outMetrics.widthPixels / 3;
        ViewGroup.LayoutParams lp = mTabline.getLayoutParams();
        lp.width = mScreen1_3;
        mTabline.setLayoutParams(lp);
    }

    private void setTranscation(Fragment fragment)
    {
        Bundle bundle = new Bundle();
        bundle.putString("guestID", guestID);

        fragment.setArguments(bundle);

        FragmentManager fragmentManager = this.getSupportFragmentManager();

//开始Fragment事务

        FragmentTransaction fTransaction = fragmentManager.beginTransaction();

//将Fragment添加到事务中，并指定一个TAG

        fTransaction.add(fragment, "179521");

//提交Fragmen事务
        fTransaction.commit();
    }

    private void initView(){
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        mDatas = new ArrayList<Fragment>();
        Shipintianzhan_fragment tab01 = new Shipintianzhan_fragment();
        Liuyanban_fragment tab02 = new Liuyanban_fragment();
        Xiangxiziliao_fragment tab03 = new Xiangxiziliao_fragment();
        Fragment fragment = new Fragment();

        setTranscation((Fragment)fragment);

        mDatas.add(tab01);
        mDatas.add(tab02);
        mDatas.add(tab03);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
        {
            @Override
            public int getCount()
            {
                return mDatas.size();
            }

            @Override
            public Fragment getItem(int arg0)
            {
                Fragment fragment = mDatas.get(arg0);
                return fragment;
            }
        };
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position)
            {


                mCurrentPageIndex = position;

            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPx)
            {
                Log.e("TAG", position + " , " + positionOffset + " , "
                        + positionOffsetPx);

                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabline
                        .getLayoutParams();

                if (mCurrentPageIndex == 0 && position == 0)// 0->1
                {
                    liuyan.setVisibility(View.INVISIBLE);
                    lp.leftMargin = (int) (positionOffset * mScreen1_3 + mCurrentPageIndex
                            * mScreen1_3);
                } else if (mCurrentPageIndex == 1 && position == 0)// 1->0
                {
                    liuyan.setVisibility(View.VISIBLE);
                    lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_3 + (positionOffset - 1)
                            * mScreen1_3);
                } else if (mCurrentPageIndex == 1 && position == 1) // 1->2
                {
                    liuyan.setVisibility(View.VISIBLE);
                    lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_3 + positionOffset
                            * mScreen1_3);
                } else if (mCurrentPageIndex == 2 && position == 1) // 2->1
                {
                    liuyan.setVisibility(View.INVISIBLE);
                    lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_3 + ( positionOffset-1)
                            * mScreen1_3);
                }
                mTabline.setLayoutParams(lp);

            }

            @Override
            public void onPageScrollStateChanged(int arg0)
            {
                // TODO Auto-generated method stub

            }
        });





    }

    protected void resetTextView()
    {
        mChatTextView.setTextColor(Color.BLACK);
        mFriendTextView.setTextColor(Color.BLACK);
        mContactTextView.setTextColor(Color.BLACK);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                try {
                    LinearLayout yincang = (LinearLayout) findViewById(R.id.gerenzhuye_yincang);
                    yincang.setVisibility(View.GONE);
                }catch (Exception e){
                    e.printStackTrace();
                }
        }
        return super.onTouchEvent(ev);
    }
}
