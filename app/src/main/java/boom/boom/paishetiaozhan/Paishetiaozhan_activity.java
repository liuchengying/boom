package boom.boom.paishetiaozhan;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.graphics.PixelFormat;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Timer;

import boom.boom.R;
import boom.boom.guizejieshao.Guizejieshao_activity;
import boom.boom.tingzhitiaozhan.Tingzhipaishe_activity;

/**
 * Created by 刘成英 on 2015/1/20.
 */
public class Paishetiaozhan_activity extends Activity implements SurfaceHolder.Callback{
    private Button paishefanhui;
    private Button kaishipaishe;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.paishetiaozhan);
        paishefanhui = (Button) findViewById(R.id.paishefanhui);
        kaishipaishe = (Button) findViewById(R.id.kaishipaishe);
        sv = (SurfaceView) findViewById(R.id.challengerecord_surface);
        surfaceHolder = sv.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        kaishipaishe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecording();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Message message = new Message();
                            message.what = 1;
                            Thread.sleep(20);
                            stopRecording();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                Intent intent = new Intent();
                intent.putExtra("challenge_name", "cups");
                intent.setClass(Paishetiaozhan_activity.this,Tingzhipaishe_activity.class);
                startActivity(intent);
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
        p.setPreviewSize(width, height);
        p.setPreviewFormat(PixelFormat.JPEG);
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
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            mediaRecorder.setMaxDuration(maxDurationInMs);
            StoreFile = new File(getCacheDir(),tmpFilename);
            mediaRecorder.setOutputFile(StoreFile.getPath());
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
        camera.lock();
    }

}
