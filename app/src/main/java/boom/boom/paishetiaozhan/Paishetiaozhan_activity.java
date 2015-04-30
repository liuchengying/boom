package boom.boom.paishetiaozhan;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.SysApplication;
import boom.boom.api.Utils;
import boom.boom.guizejieshao.Guizejieshao_activity;
import boom.boom.shangchuandengdai.Shangchuandengdai_activity;

/**
 * Created by 刘成英 on 2015/1/20.
 */
public class Paishetiaozhan_activity extends Activity implements SurfaceHolder.Callback{
    private Button paishefanhui;
    private Button kaishipaishe;
    private Button fangqipaishe;
    private SurfaceView sv;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private boolean previewRunning;
    private File StoreFile;
    private MediaRecorder mediaRecorder;
    private final String tmpFilename = "tmpvideo";
    private final int maxDurationInMs = 20000;
    private final long maxFileSizeInBytes = 500000;
    private final int videoFramesPerSecond = 20;
    private ProgressBar mprogress;
    private int a = 0;
    private boolean onThreadStartStop = false;

    //private Handler myMessageHandler;
    /*Handler myMessageHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            sss.setText(a+"s");
            super.handleMessage(msg);
        }
    };*/
    Handler myMessageHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            sss.setText(a+"s");
            }};
    Thread thread_progress = new Thread(new Runnable() {
        @Override
        public void run() {
            for (; a < 20; a++) {
                try {
//                    mprogress.incrementProgressBy(1);
                    mprogress.setProgress(a);
                    Message m = new Message();
                    m.what = 1;
                    Paishetiaozhan_activity.this.myMessageHandler.sendMessage(m);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });
    Handler on_thread_progress = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if (onThreadStartStop = !onThreadStartStop){
                thread_progress.start();
            }else{
                a = 20;
                thread_progress.interrupt();
            }
        }
    };
    private TextView sss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent intent = getIntent();
        int position = intent.getIntExtra("challenge_number", 1);
        final String cl_name = intent.getStringExtra("challenge_name");

        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.paishetiaozhan);
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        paishefanhui = (Button) findViewById(R.id.paishefanhui);
        kaishipaishe = (Button) findViewById(R.id.kaishipaishe);
        fangqipaishe = (Button) findViewById(R.id.fangqipaishe);
        sv = (SurfaceView) findViewById(R.id.syncRecord_monitor);
        mprogress = (ProgressBar) findViewById(R.id.mprogress);
        sss= (TextView) findViewById(R.id.ssss);
        surfaceHolder = sv.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        kaishipaishe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (kaishipaishe.getText().equals("开始")) {
                    Message m = new Message();
                    m.what = 1;
                    Paishetiaozhan_activity.this.on_thread_progress.sendMessage(m);
                    kaishipaishe.setText("停止");
                    startRecording();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
//                                Message message = new Message();
//                                message.what = 1;
                                Thread.sleep(20000);
                                onStopRecording();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else if (kaishipaishe.getText().equals("停止")) {
                    onStopRecording();
                    Message m = new Message();
                    m.what = 1;
                    Paishetiaozhan_activity.this.on_thread_progress.sendMessage(m);
                }else if (kaishipaishe.getText().equals("上传")){
                    Intent intent = new Intent();
                    intent.putExtra("file_path", StoreFile.getAbsolutePath());
                    intent.setClass(Paishetiaozhan_activity.this,Shangchuandengdai_activity.class);
                    startActivity(intent);
                }
            }
        });

        paishefanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Paishetiaozhan_activity.this,Guizejieshao_activity.class);
                startActivity(intent);
            }
        });

    }

    public void onStopRecording(){
        stopRecording();
        kaishipaishe.setText("上传");
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, -0.6f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f);
        translateAnimation.setDuration(1000);
        animationSet.addAnimation(translateAnimation);
        animationSet.setFillAfter(true);
        kaishipaishe.startAnimation(animationSet);
        fangqipaishe.setVisibility(View.VISIBLE);
        AnimationSet animationSet1 = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(-1, 1);
        alphaAnimation.setDuration(2000);
        animationSet1.addAnimation(alphaAnimation);
        animationSet1.setFillAfter(true);
        fangqipaishe.startAnimation(animationSet1);
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();
        if (camera != null){
            Camera.Parameters params = camera.getParameters();
            camera.setParameters(params);
        }
        else {
            Toast.makeText(getApplicationContext(), "Camera not available!", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (previewRunning){
            camera.stopPreview();
        }
        Camera.Parameters p = camera.getParameters();
        //p.setPreviewSize(width, height);
        //p.setPreviewFormat(PixelFormat.JPEG);
        camera.setDisplayOrientation(90);
        camera.setParameters(p);

        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
            previewRunning = true;
        }
        catch (IOException e) {
            Log.e("CAMERA", "Camera meets internal error.");
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        previewRunning = false;
        camera.release();
    }

    public boolean startRecording(){
        try {
            camera.unlock();
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setCamera(camera);
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            mediaRecorder.setMaxDuration(maxDurationInMs);
            File dirStorage = new File(Utils.getVideoPath());
            if (dirStorage == null){
                Log.e("CAMERA", "Unable to got the sdcard read access. Fall back to /data mode.");
                dirStorage = getCacheDir();
            }
            Log.d("CAMERA", "Store path: ==> " + dirStorage.getAbsolutePath());
            String random_name = Utils.getRandomName("mp4");
            Log.d("CAMERA", "Store file path ==> "+ dirStorage + "/" + random_name);
            StoreFile = new File(dirStorage, random_name);
            if (StoreFile.exists() == false){
                StoreFile.createNewFile();
            }
            Log.d("CAMERA", "File absolutely path ==> "+ StoreFile.getAbsolutePath());
            mediaRecorder.setOutputFile(StoreFile.getAbsolutePath());
            mediaRecorder.setVideoFrameRate(videoFramesPerSecond);
            mediaRecorder.setVideoSize(sv.getWidth(), sv.getHeight());
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
            mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
            mediaRecorder.setMaxFileSize(maxFileSizeInBytes);
            mediaRecorder.prepare();
            mediaRecorder.start();
            return true;
        } catch (IllegalStateException e) {
            Log.e("CAMERA","Caught a illegal error.");
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            Log.e("CAMERA","Caught a I/O error.");
            e.printStackTrace();
            return false;
        }
    }

    public void stopRecording(){
        mediaRecorder.stop();
        camera.setPreviewCallback(null);
        camera.lock();
    }

}
