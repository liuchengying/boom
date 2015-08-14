package boom.boom.api;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by honorbird on 15/8/14.
 */
public class myVideoView extends VideoView {
    private int mVideoWidth;
    private int mVideoHeight;

    public myVideoView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public myVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public myVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //Log.i("@@@@", "onMeasure");
        int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
        int height = getDefaultSize(mVideoHeight, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
}
