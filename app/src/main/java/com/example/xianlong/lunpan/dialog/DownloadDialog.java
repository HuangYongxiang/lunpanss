package com.example.xianlong.lunpan.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xianlong.lunpan.R;

/**
 * Created by job on 2017/9/11.
 */
public class DownloadDialog extends Dialog {

    private ImageView spaceshipImage;
    private TextView tipTextView;


    public DownloadDialog(Context context, String text) {
        super(context, R.style.loading_dialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.load_dlg, null);
        spaceshipImage = (ImageView) view.findViewById(R.id.img);
        spaceshipImage.setVisibility(View.GONE);
        tipTextView = (TextView) view.findViewById(R.id.tipTextView);
        tipTextView.setText(text);
        setCancelable(false);
        setContentView(view);
    }


    public void showDialog(){
        this.show();
    }


    /**
     * ���öԻ����İ�
     *
     * @param content
     */

    public void setTextContent(String content) {
        if (this.isShowing() && tipTextView != null) {
            tipTextView.setText(content);
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

    }

    @Override
    public void dismiss() {
        super.dismiss();

    }




}
