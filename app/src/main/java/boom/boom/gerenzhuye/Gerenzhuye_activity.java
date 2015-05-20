package boom.boom.gerenzhuye;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.Static;
import boom.boom.api.SysApplication;
import boom.boom.bangzhuyufankui.Bangzhuyufankui_activity;
import boom.boom.liuyan.Liuyan_activity;
import boom.boom.myview.CircleImageView;
import boom.boom.myview.SildingFinishLayout;
import boom.boom.paihangbang.Paihangbang_activity;
import boom.boom.shipintianzhanpinglun.Shipintianzhan_pinglun;


/**
 * Created by 刘成英 on 2015/3/12.
 */
public class Gerenzhuye_activity extends FragmentActivity
{
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mDatas;

    private TextView mChatTextView;
    private TextView mFriendTextView;
    private TextView mContactTextView;
    private LinearLayout mChatLinearLayout;
    private LinearLayout gerenzhuyefanhui;
    private TextView l1,l2,l3;
    private CircleImageView gerenzhuye_touxing;
    public static JSONObject obj;
    private Button liuyan;


    private TextView username;
    private TextView sign;


    private ImageView mTabline;
    private int mScreen1_3;

    private int mCurrentPageIndex;
    Handler intent_switch = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            Intent intent = new Intent();
            intent.setClass(Gerenzhuye_activity.this, Shipintianzhan_pinglun.class);
            startActivity(intent);
        }
    };
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.gerenzhuye);
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体

        SildingFinishLayout mSildingFinishLayout = (SildingFinishLayout) findViewById(R.id.sildingFinishLayout1);
        mSildingFinishLayout
                .setOnSildingFinishListener(new SildingFinishLayout.OnSildingFinishListener() {

                    @Override
                    public void onSildingFinish() {
                        Gerenzhuye_activity.this.finish();
                    }
                });

        mSildingFinishLayout.setTouchView(mSildingFinishLayout);
        liuyan = (Button) findViewById(R.id.liuyan_button);
        liuyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Gerenzhuye_activity.this, Liuyan_activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.liuyan_in,0);

            }
        });

        username=(TextView)findViewById(R.id.gerenzhuye_yonghuming);
        username.setText(Static.nickname);
        sign=(TextView)findViewById(R.id.gerenzhuye_qianming);
        sign.setText(Static.uniqueSign);
       gerenzhuye_touxing = (CircleImageView) findViewById(R.id.gerenzhuye_touxiang);
        gerenzhuye_touxing.setImageBitmap(Static.avatarImage);
        gerenzhuyefanhui = (LinearLayout)findViewById(R.id.gerenzhuyefanhui);
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
        //liuyan();
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

    private void initView(){
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        mDatas = new ArrayList<Fragment>();
        Shipintianzhan_fragment tab01 = new Shipintianzhan_fragment();
        Liuyanban_fragment tab02 = new Liuyanban_fragment();
        Xiangxiziliao_fragment tab03 = new Xiangxiziliao_fragment();

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
                return mDatas.get(arg0);
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

                LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) mTabline
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
//    LinearLayout yincang = (LinearLayout) findViewById(R.id.gerenzhuye_yincang);

   /* private void liuyan(){
        if( == 1){
            Toast.makeText(this,"这是第",Toast.LENGTH_SHORT).show();
            liuyan.setVisibility(View.VISIBLE);
            }else {
            liuyan.setVisibility(View.INVISIBLE);
        }
    }*/
}
