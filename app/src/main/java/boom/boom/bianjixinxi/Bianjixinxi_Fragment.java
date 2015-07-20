package boom.boom.bianjixinxi;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.EditInformation;
import boom.boom.api.LoadingDialog;
import boom.boom.api.Static;
import boom.boom.api.SysApplication;
import boom.boom.myview.ImageTools;
import boom.boom.myview.RoundedImageView;
import boom.boom.wheelcity.AddressData;
import boom.boom.wheelcity.OnWheelChangedListener;
import boom.boom.wheelcity.WheelView;
import boom.boom.wheelcity.adapters.AbstractWheelTextAdapter;
import boom.boom.wheelcity.adapters.ArrayWheelAdapter;
import boom.boom.widget1.MyAlertDialog;
import boom.boom.youxiangyanzheng.Youxiangyanzheng_activity;

/**
 * Created by Administrator on 2015/7/9.
 */
public class Bianjixinxi_Fragment extends Fragment {
    private List<String> starlist = new ArrayList<String>();
    private List<String> sexlist = new ArrayList<String>();
    private static View thisView;
    private Spinner sex;
    private Spinner star;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapter1;
    private RoundedImageView sz_touxiang;
    private Button button;
    private Button confirmButton;
    private Button cancleButton;
    private PopupWindow popupWindow;
    private View popupWindowView;
    LoadingDialog dialog;
    private Uri uritempFile;
    private TextView text1;
    private boolean avatarChanged;
    private EditInformation editInformation;

    private static final int TAKE_PICTURE = 4;
    private static final int CHOOSE_PICTURE = 1;
    private static final int CROP = 2;
    private static final int CROP_PICTURE = 3;

    private static final int SCALE = 5;//照片缩小比例
    private RoundedImageView iv_image = null;

    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择

    EditText nickname;
    EditText age;
    EditText job;
    TextView location;
    EditText school;
    EditText company;
    TextView email;
    EditText sign;
    Bitmap avatar;
    Timer timer;
    TimerTask task;
    RelativeLayout save;
    android.os.Handler myMessageHandler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                Toast.makeText(getActivity(), "保存成功！", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
            else{
                dialog.dismiss();
                Toast.makeText(getActivity(),"保存失败，请重试！",Toast.LENGTH_SHORT).show();
            }
        }};
    Handler onCreateHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                RelativeLayout fanhui = (RelativeLayout) getActivity().findViewById(R.id.bianjixinxi_fh);
                fanhui.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().finish();
                        getActivity().overridePendingTransition(R.anim.base_slide_remain,R.anim.base_slide_right_out);
                    }
                });
                //editInformation=(EditInformation)getIntent().getExtras().getSerializable("info");


                sexlist.add("男");
                sexlist.add("女");
                sex = (Spinner) Bianjixinxi_Fragment.thisView.findViewById(R.id.sex);
                adapter = new ArrayAdapter<String>(getActivity(), R.layout.shezhi_spinner_style1, sexlist);
                adapter.setDropDownViewResource(R.layout.shezhi_spinner_style);
                sex.setAdapter(adapter);
                star = (Spinner) Bianjixinxi_Fragment.thisView.findViewById(R.id.star);
                //iv_image = (RoundedImageView) findViewById(R.id.bianjiziliao_touxiang);
                sex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String str = parent.getItemAtPosition(position).toString();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                starlist.add("白羊座");
                starlist.add("金牛座");
                starlist.add("双子座");
                starlist.add("巨蟹座");
                starlist.add("狮子座");
                starlist.add("处女座");
                starlist.add("天秤座");
                starlist.add("天蝎座");
                starlist.add("射手座");
                starlist.add("摩羯座");
                starlist.add("水瓶座");
                starlist.add("双鱼座");

                adapter1 = new ArrayAdapter<String>(getActivity(), R.layout.shezhi_spinner_style1, starlist);
                adapter1.setDropDownViewResource(R.layout.shezhi_spinner_style);
                star.setAdapter(adapter1);
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
                popupWindowView = inflater.inflate(R.layout.shezhi_touxiang, null);
                popupWindow = new PopupWindow(popupWindowView, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                sz_touxiang = (RoundedImageView) Bianjixinxi_Fragment.thisView.findViewById(R.id.bianjiziliao_touxiang);
                sz_touxiang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //设置PopupWindow的弹出和消失效果
                        popupWindow.setAnimationStyle(R.style.popupAnimation);
                        confirmButton = (Button) popupWindowView.findViewById(R.id.sz_touxiang_paishe);

                        cancleButton = (Button) popupWindowView.findViewById(R.id.cancleButton);
                        button = (Button) popupWindowView.findViewById(R.id.sz_touxiang_bendi);
                        popupWindow.showAtLocation(confirmButton, Gravity.CENTER, 0, 0);
                        confirmButton.setOnClickListener(Itemclick);
                        cancleButton.setOnClickListener(Itemclick);
                        button.setOnClickListener(Itemclick);

                    }
                });
                TextView suozaidi = (TextView) Bianjixinxi_Fragment.thisView.findViewById(R.id.bjxx_szd);
                suozaidi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View view = dialogm();
                        final MyAlertDialog dialog1 = new MyAlertDialog(getActivity())
                                .builder()
                                .setTitle("请选择地区：")
                                .setView(view)
                                .setNegativeButton("取消", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                });
                        dialog1.setPositiveButton("保存", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getActivity(), cityTxt, Toast.LENGTH_SHORT).show();
                                location.setText(cityTxt);
                            }
                        });
                        dialog1.show();
                    }
                });
                try {
                    //dialog = new LoadingDialog(getActivity(), "正在加载...");
                    sex.setSelection(editInformation.sex, false);
                    star.setSelection(editInformation.star, false);
                    sign = (EditText) Bianjixinxi_Fragment.thisView.findViewById(R.id.gexingqianming);
                    sign.setText(editInformation.uniquesign);
                    nickname = (EditText) Bianjixinxi_Fragment.thisView.findViewById(R.id.bjxx_nc);
                    nickname.setText(editInformation.nickname);
                    age = (EditText) Bianjixinxi_Fragment.thisView.findViewById(R.id.bjxx_nl);
                    age.setText("" + editInformation.age);
                    job = (EditText) Bianjixinxi_Fragment.thisView.findViewById(R.id.bjxx_zy);
                    job.setText(editInformation.job);
                    location = (TextView) Bianjixinxi_Fragment.thisView.findViewById(R.id.bjxx_szd);
                    location.setText(editInformation.address);
                    school = (EditText) Bianjixinxi_Fragment.thisView.findViewById(R.id.bjxx_xx);
                    school.setText(editInformation.school);
                    company = (EditText) Bianjixinxi_Fragment.thisView.findViewById(R.id.bjxx_gs);
                    company.setText(editInformation.company);
                    email = (TextView) Bianjixinxi_Fragment.thisView.findViewById(R.id.bjxx_yx);
                    email.setText(editInformation.email);
                    email.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setClass(getActivity(),Youxiangyanzheng_activity.class);
                            startActivityForResult(intent,500);
                        }
                    });
                    sz_touxiang.setImageBitmap(Static.avatarImage);
                    save = (RelativeLayout) getActivity().findViewById(R.id.bianjixinxi_baocun);
                    dialog.dismiss();
                    task.cancel();
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editInformation.nickname = nickname.getText().toString();
                            editInformation.sex = sex.getSelectedItemPosition();
                            editInformation.age = Integer.parseInt(age.getText().toString());
                            editInformation.star = star.getSelectedItemPosition();
                            editInformation.job = job.getText().toString();
                            editInformation.address = location.getText().toString();
                            editInformation.school = school.getText().toString();
                            editInformation.company = company.getText().toString();
                            //editInformation.email = email.getText().toString();
                            editInformation.avatarImage = avatar;
                            editInformation.uniquesign = sign.getText().toString();

                            //dialog.show();
                            //dialog.setCancelable(false);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    try {
                                        Message m = new Message();
                                        m.what = editInformation.save(avatarChanged);
                                        myMessageHandler.sendMessage(m);
                                        Synch();
                                        Message mm = new Message();
                                        mm.what = avatarChanged ? 1 : 0;
                                        Static.tiaozhan_handler.sendMessage(mm);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(getActivity(),"网络连接失败！请检查网络连接",Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
                return true;
            }

    });



    private void Synch(){
        try{
            editInformation.GetInformation();
            Static.nickname=editInformation.nickname;
            Static.coins=editInformation.coins;
            Static.avatar=editInformation.avatar;
            Static.uniqueSign=editInformation.uniquesign;
            if(avatarChanged)
                Static.avatarImage=avatar;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bianjixinxi_frame,container,false);
        thisView = view;
        SysApplication.getInstance().addActivity(getActivity());
        FontManager.changeFonts(FontManager.getContentView(getActivity()), getActivity());//字体

        dialog = new LoadingDialog(getActivity(),"正在加载...");
        dialog.show();
        dialog.setCancelable(false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    editInformation = new EditInformation();
                    editInformation.GetInformation();
                    Message msg = new Message();
                    msg.what = 1;
                    onCreateHandler.sendMessage(msg);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        task=new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 0;
                onCreateHandler.sendMessage(msg);
            }
        };
        timer=new Timer(true);
        timer.schedule(task, 5000);
        return view;



    }



    private View.OnClickListener Itemclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (popupWindow.isShowing()){
                popupWindow.setAnimationStyle(R.style.popupAnimation);
                popupWindow.dismiss();


                switch (v.getId()) {
                    case R.id.sz_touxiang_paishe:
                        which =TAKE_PICTURE;
                        showPicturePicker(getActivity(),true);
                        break;
                    case R.id.sz_touxiang_bendi:
                        which =CHOOSE_PICTURE ;
                        showPicturePicker(getActivity(),true);


                        break;
                    default:
                        break;
                }
            }


        }
    };





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    //将保存在本地的图片取出并缩小后显示在界面上
                    Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/image.jpg");
                    Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
                    //由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
                    bitmap.recycle();

                    //将处理过的图片显示在界面上，并保存到本地
                    sz_touxiang.setImageBitmap(newBitmap);
                    ImageTools.savePhotoToSDCard(newBitmap, Environment.getExternalStorageDirectory().getAbsolutePath(), String.valueOf(System.currentTimeMillis()));

                    break;

                case CHOOSE_PICTURE:
                    ContentResolver resolver = getActivity().getContentResolver();
                    //照片的原始资源地址
                    Uri originalUri = data.getData();
                    try {
                        //使用ContentProvider通过URI获取原始图片
                        Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                        if (photo != null) {
                            //为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                            Bitmap smallBitmap = ImageTools.zoomBitmap(photo, photo.getWidth() / SCALE, photo.getHeight() / SCALE);
                            //释放原始图片占用的内存，防止out of memory异常发生
                            photo.recycle();

                            sz_touxiang.setImageBitmap(smallBitmap);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case CROP:
                    Uri uri = null;
                    if (data != null) {
                        uri = data.getData();
                        System.out.println("Data");
                    }else {
                        System.out.println("File");
                        String fileName = getActivity().getSharedPreferences("temp", Context.MODE_WORLD_WRITEABLE).getString("tempName", "");
                        uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),fileName));
                    }
                    cropImage(uri, 500, 500, CROP_PICTURE);
                    break;

                case CROP_PICTURE:
                    Bitmap photo = null;
                    /*Uri photoUri = data.getData();
                    if (photoUri != null) {*/
                    try {
                        photo = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uritempFile));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    //photo = BitmapFactory.decodeFile(photoUri.getPath());
                    //
                    // }
                    if (photo == null) {
                        Bundle extra = data.getExtras();
                        if (extra != null) {
                            photo = (Bitmap)extra.get("data");
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        }
                    }
                    avatar=photo;
                    avatarChanged=true;
                    sz_touxiang.setImageBitmap(photo);
                    break;
                default:
                    break;
            }
        }else if (resultCode == 500){
            email.setText(data.getStringExtra("data"));
        }
    }


    int which;


    public void showPicturePicker(Context context,boolean isCrop){
        final boolean crop = isCrop;

        //类型码
        int REQUEST_CODE;


        switch (which) {
            case TAKE_PICTURE:
                Uri imageUri = null;
                String fileName = null;
                Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


                if (crop) {
                    REQUEST_CODE = CROP;
                    //删除上一次截图的临时文件
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("temp",Context.MODE_WORLD_WRITEABLE);
                    ImageTools.deletePhotoAtPathAndName(Environment.getExternalStorageDirectory().getAbsolutePath(), sharedPreferences.getString("tempName", ""));

                    //保存本次截图临时文件名字
                    fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("tempName", fileName);
                    editor.commit();
                }else {
                    REQUEST_CODE = TAKE_PICTURE;
                    fileName = "image.jpg";
                }
                imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),fileName));
                //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);


                startActivityForResult(openCameraIntent, REQUEST_CODE);
                break;

            case CHOOSE_PICTURE:
                Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                if (crop) {
                    REQUEST_CODE = CROP;
                }else {
                    REQUEST_CODE = CHOOSE_PICTURE;
                }
                openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(openAlbumIntent, REQUEST_CODE);
                break;

            default:
                break;
        }
    }





    public void cropImage(Uri uri, int outputX, int outputY, int requestCode){
        Intent intent = new Intent("com.android.camera.action.CROP");
        // intent.setDataAndType(uri,"tempName");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);
        //intent.putExtra("return-data", true);

        uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, requestCode);
    }
    //选地区部分代码
    private String cityTxt;
    private View dialogm() {
        View contentView = LayoutInflater.from(getActivity()).inflate(
                R.layout.wheelcity_cities_layout, null);
        final WheelView country = (WheelView) contentView
                .findViewById(R.id.wheelcity_country);
        country.setVisibleItems(3);
        country.setViewAdapter((boom.boom.wheelcity.adapters.WheelViewAdapter) new CountryAdapter(getActivity()));

        final String cities[][] = AddressData.CITIES;
        final String ccities[][][] = AddressData.COUNTIES;
        final WheelView city = (WheelView) contentView
                .findViewById(R.id.wheelcity_city);
        city.setVisibleItems(0);

        // 地区选择
        final WheelView ccity = (WheelView) contentView
                .findViewById(R.id.wheelcity_ccity);
        ccity.setVisibleItems(0);// 不限城市

        country.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateCities(city, cities, newValue);



                cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
                        + " | "
                        + AddressData.CITIES[country.getCurrentItem()][city
                        .getCurrentItem()]
                        + " | "
                        + AddressData.COUNTIES[country.getCurrentItem()][city
                        .getCurrentItem()][ccity.getCurrentItem()];
            }
        });

        city.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updatecCities(ccity, ccities, country.getCurrentItem(),
                        newValue);
                cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
                        + " | "
                        + AddressData.CITIES[country.getCurrentItem()][city
                        .getCurrentItem()]
                        + " | "
                        + AddressData.COUNTIES[country.getCurrentItem()][city
                        .getCurrentItem()][ccity.getCurrentItem()];
                Static.province = AddressData.PROVINCES[country.getCurrentItem()];
                Static.city = AddressData.CITIES[country.getCurrentItem()][city
                        .getCurrentItem()];
                Static.area = AddressData.COUNTIES[country.getCurrentItem()][city
                        .getCurrentItem()][ccity.getCurrentItem()];
            }
        });

        ccity.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
                        + " | "
                        + AddressData.CITIES[country.getCurrentItem()][city
                        .getCurrentItem()]
                        + " | "
                        + AddressData.COUNTIES[country.getCurrentItem()][city
                        .getCurrentItem()][ccity.getCurrentItem()];
                Static.province = AddressData.PROVINCES[country.getCurrentItem()];
                Static.city = AddressData.CITIES[country.getCurrentItem()][city
                        .getCurrentItem()];
                Static.area = AddressData.COUNTIES[country.getCurrentItem()][city
                        .getCurrentItem()][ccity.getCurrentItem()];
            }
        });

        country.setCurrentItem(1);// 设置北京
        city.setCurrentItem(1);
        ccity.setCurrentItem(1);
        return contentView;
    }

    /**
     * Updates the city wheel
     */
    private void updateCities(WheelView city, String cities[][], int index) {
        ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(getActivity(),
                cities[index]);
        adapter.setTextSize(18);
        city.setViewAdapter(adapter);
        city.setCurrentItem(0);
    }

    /**
     * Updates the ccity wheel
     */
    private void updatecCities(WheelView city, String ccities[][][], int index,
                               int index2) {
        ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(getActivity(),
                ccities[index][index2]);
        adapter.setTextSize(18);
        city.setViewAdapter(adapter);
        city.setCurrentItem(0);
    }

    /**
     * Adapter for countries
     */
    private class CountryAdapter extends AbstractWheelTextAdapter {
        // Countries names
        private String countries[] = AddressData.PROVINCES;

        /**
         * Constructor
         */
        protected CountryAdapter(Context context) {
            super(context, R.layout.wheelcity_country_layout, NO_RESOURCE);
            setItemTextResource(R.id.wheelcity_country_name);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return countries.length;
        }

        @Override
        protected CharSequence getItemText(int index) {
            return countries[index];
        }
    }
}
