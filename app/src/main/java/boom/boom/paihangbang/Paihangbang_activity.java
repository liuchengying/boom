package boom.boom.paihangbang;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import boom.boom.R;
import boom.boom.api.SysApplication;
import boom.boom.gerenzhuye.Gerenzhuye_activity;
import boom.boom.myview.SildingFinishLayout;
import boom.boom.tianzhan.Tiaozhan_activity;

/**
 * Created by 刘成英 on 2015/3/12.
 */
public class Paihangbang_activity extends FragmentActivity
{
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mDatas;

    private TextView mChatTextView;
    private TextView mFriendTextView;
    private TextView mContactTextView;
    private LinearLayout mChatLinearLayout;
    private TextView t1,t2;



    private ImageView mTabline;
    private int mScreen1_2;

    private int mCurrentPageIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.paihangbang);
        SildingFinishLayout mSildingFinishLayout = (SildingFinishLayout) findViewById(R.id.sildingFinishLayout);
        mSildingFinishLayout
                .setOnSildingFinishListener(new SildingFinishLayout.OnSildingFinishListener() {

                    @Override
                    public void onSildingFinish() {
                        Paihangbang_activity.this.finish();
                    }
                });

        mSildingFinishLayout.setTouchView(mSildingFinishLayout);
        SysApplication.getInstance().addActivity(this);
        RelativeLayout phbfh = (RelativeLayout)findViewById(R.id.paihangbangfanhuis);
        phbfh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, R.anim.base_slide_right_out);
            }
        });
        jiantingqiehuan1();
        initTabLine();
        initView();
    }

    private void initTabLine()
    {
        mTabline = (ImageView) findViewById(R.id.gundongtiao1);
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        mScreen1_2 = outMetrics.widthPixels / 2;
        ViewGroup.LayoutParams lp = mTabline.getLayoutParams();
        lp.width = mScreen1_2;
        mTabline.setLayoutParams(lp);
    }
    private void jiantingqiehuan1(){
        t1 = (TextView) findViewById(R.id.phb_1);
        t2 = (TextView) findViewById(R.id.phb_2);

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
            }
        });
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);
            }
        });
    }

    private void initView()
    {
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager1);



        mDatas = new ArrayList<Fragment>();


        Diqupaihang_fragment tab01 = new Diqupaihang_fragment();
        Quanguopaihang_fragment tab02 = new Quanguopaihang_fragment();


        mDatas.add(tab01);
        mDatas.add(tab02);


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

                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabline
                        .getLayoutParams();

                if (mCurrentPageIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = (int) (positionOffset * mScreen1_2 + mCurrentPageIndex
                            * mScreen1_2);
                } else if (mCurrentPageIndex == 1 && position == 0)// 1->0
                {
                    lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_2 + (positionOffset - 1)
                            * mScreen1_2);
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
}
