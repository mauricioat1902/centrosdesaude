package com.example.mauricioecamila.centrosdesaude;

import android.app.Dialog;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Mauricio e Camila on 23/02/2017.
 */

public class MyDialog extends Dialog {

    private Context context;
    private TextView tvLoadText;
    private ImageView ivLoadImg;

    public MyDialog(Context context, String text) {
        super(context);
        setContentView(R.layout.my_custom_dialog);

    }
}
