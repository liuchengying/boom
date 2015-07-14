package boom.boom.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import boom.boom.gerenzhuye.Gerenzhuye_activity;
import boom.boom.shipintianzhanpinglun.Shipintianzhan_pinglun;

/**
 * Created by lcy on 2015/6/22.
 */
public class Touchanimation extends ListView implements AbsListView.OnScrollListener {
    private float Y = 0;
    private float z = 0;
    private float F = 0;
    LinearLayout all;
    public Context mContext;

    public Touchanimation(Context context) {
        super(context);
    }
    public Touchanimation(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public Touchanimation(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
    boolean animating ;
    static boolean ok;
    public boolean onTouchEvent(MotionEvent ev){
        Y = ev.getRawY();
       switch (ev.getAction()){
           case MotionEvent.ACTION_DOWN:
               Y = ev.getRawY();
               break;
           case MotionEvent.ACTION_MOVE:
               z = ev.getRawY();
               F = z - Y;
               try {
                   //all = ((Shipintianzhan_pinglun) mContext).allLinear;
               }catch (Exception e)
               {
                   e.printStackTrace();
               }

               if(all != null) {
                   if (F < -5) {
                       //手指向上滑动
                       if (getFirstVisiblePosition() > 0) {

                           if (!animating) {
                               if (!ok) {
                                   AnimationSet animationSet = new AnimationSet(true);
                                   TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -510);
                                   translateAnimation.setDuration(500);
                                   translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                                       @Override
                                       public void onAnimationStart(Animation animation) {
                                           all.setVisibility(View.GONE);
                                       }

                                       @Override
                                       public void onAnimationEnd(Animation animation) {
                                           RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                                                   all.getLayoutParams());

                                           params.setMargins(0, -390, 0, 0);
                                           animating = false;
                                           all.clearAnimation();
                                           all.setLayoutParams(params);
                                           all.setVisibility(View.VISIBLE);

                                       }

                                       @Override
                                       public void onAnimationRepeat(Animation animation) {

                                       }
                                   });
                                   animationSet.addAnimation(translateAnimation);
                                   animationSet.setFillAfter(true);
                                   all.startAnimation(animationSet);
                                   animating = true;
                                   ok = true;
                               }
                           }
                       }
                   }
                   if (F > 5) {

                       //手指向下滑动
                       if (getFirstVisiblePosition() <= 1 && getFirstVisiblePosition() >= 0) {
                           if (!animating) {
                               if (ok) {
                                   AnimationSet animationSet = new AnimationSet(true);
                                   TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, 490);
                                   translateAnimation.setDuration(500);
                                   translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                                       @Override
                                       public void onAnimationStart(Animation animation) {
                                           all.setVisibility(View.GONE);
                                       }

                                       @Override
                                       public void onAnimationEnd(Animation animation) {
                                           RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                                                   all.getLayoutParams());

                                           params.setMargins(0, 110, 0, 0);
                                           animating = false;
                                           all.clearAnimation();
                                           all.setLayoutParams(params);
                                           all.setVisibility(View.VISIBLE);

                                       }

                                       @Override
                                       public void onAnimationRepeat(Animation animation) {

                                       }
                                   });
                                   animationSet.addAnimation(translateAnimation);
                                   animationSet.setFillAfter(true);
                                   all.startAnimation(animationSet);
                                   animating = true;
                                   ok = false;
                               }
                           }
                       }
                   }
               }
               break;

           case MotionEvent.ACTION_UP:
       }




        return super.onTouchEvent(ev);

    }
}
