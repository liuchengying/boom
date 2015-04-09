package boom.boom.api;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import boom.boom.R;

/**
 * Created by Administrator on 2015/4/7.
 */
public class LoadingDialog extends Dialog {
    private TextView tv;

    public LoadingDialog(Context context) {
        super(context, R.style.loadingDialogStyle);
        this.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        //tv = (TextView)findViewById(R.id.tv);
        //tv.setText("正在上传.....");
        LinearLayout linearLayout = (LinearLayout)this.findViewById(R.id.LinearLayout);
        //linearLayout.getBackground().setAlpha(210);
    }
}
