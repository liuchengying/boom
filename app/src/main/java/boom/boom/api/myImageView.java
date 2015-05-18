package boom.boom.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import boom.boom.myview.CircleImageView;

public class myImageView extends CircleImageView {

    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if(msg.what == 0){
                setImageBitmap((Bitmap)msg.obj);
            }
        }

    };
    public myImageView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public myImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public myImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public void InitMyImageView(){

    }
    public void SetImageForHttp(final String UrlPath){
        if(UrlPath ==null || UrlPath.length()<1)return;

        new Thread() {
            public void run() {
                Bitmap btm =  getHttpBitmap(UrlPath);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = btm;
                mHandler.sendMessage(msg);
            };
        }.start();


    }

    public static Bitmap getHttpBitmap(String url){
        URL myFileURL;
        Bitmap bitmap=null;
        try{
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            conn.setRequestProperty("Cookie","PHPSESSID=" + Static.session_id);
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return bitmap;

    }
}