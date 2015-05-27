package boom.boom.api;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import boom.boom.R;

/**
 * Created by Administrator on 2015/4/7.
 */
public class LoadingDialog extends Dialog {
    private TextView tv;
    private String text;
    public LoadingDialog(Context context , String showText) {
        super(context, R.style.loadingDialogStyle);
        this.setCanceledOnTouchOutside(false);
        text = showText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        //tv = (TextView)findViewById(R.id.tv);
        //tv.setText("正在上传.....");
        LinearLayout linearLayout = (LinearLayout)this.findViewById(R.id.LinearLayout);
        TextView showing = (TextView)findViewById(R.id.dialog_text);
        showing.setText(text);
        //linearLayout.getBackground().setAlpha(210);
    }
}
