package com.rider.democube.praise;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rider.democube.R;

/**
 * Created by wubin on 2017/10/25 0025.
 */

public class PraiseActivity extends Activity {

    private EditText editText;
    private PraiseView praiseView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_praise);

        editText = findViewById(R.id.praise_et_num);
        Button button =findViewById(R.id.praise_btn_set);
        praiseView = findViewById(R.id.praiseview);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = editText.getText().toString();
                try {
                    praiseView.setPraiseNum(Integer.parseInt(data));
                }catch (Exception e){
                    praiseView.setPraiseNum(0);
                }
            }
        });


    }
}
