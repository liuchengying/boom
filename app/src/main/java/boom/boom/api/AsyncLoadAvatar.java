package boom.boom.api;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

import boom.boom.R;

/**
 * Created by Administrator on 2015/5/21.
 */

/*
    Bitmap GetLocalImage(String avatarUID);     //获取本地图片
    boolean SaveBitmapToLocal(Bitmap target , String avatarUID);   //保存Bitmap到本地文件夹,文件名是头像ID
    Bitmap DownloadBitmap(String avatarUID);    //从服务器下载头像，并返回Bitmap
 */

public class AsyncLoadAvatar {

    public static Bitmap GetLocalImage(Context context, String avatarUID)
    {
        String pathString = Utils.getImagePath() + avatarUID + ".png";
        Bitmap bitmap = null;
        if (avatarUID.equals("null")||avatarUID.equals("")){
            Resources res= context.getResources();
            bitmap = BitmapFactory.decodeResource(res,R.drawable.android_150);
            return bitmap;
        }
        try
        {
            File file = new File(pathString);
            if(file.exists())
            {
                bitmap = BitmapFactory.decodeFile(pathString);
            }
            else {
                bitmap = null;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static boolean SaveBitmapToLocal(Bitmap target , String avatarUID)
    {

        try {
            File f = new File(Utils.getImagePath() + avatarUID + ".png");
            f.createNewFile();
            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            target.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            try {
                fOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            try {
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static Bitmap DownloadBitmap(String avatarUID)
    {
        Bitmap bitmap=null;
        InputStream in = null;
        HttpURLConnection urlConn = null;
        try {
            URL url = new URL("http://172.24.10.118/api/getimage.php?token=" + avatarUID);
            if (url != null) {
                urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setConnectTimeout(5000);// 设置超时时间
                urlConn.setRequestProperty("Cookie","PHPSESSID=" + Static.session_id);
                try {
                    in = urlConn.getInputStream();
                } catch (ConnectException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(in);
            //关闭数据流
            in.close();
            urlConn.disconnect();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
}
