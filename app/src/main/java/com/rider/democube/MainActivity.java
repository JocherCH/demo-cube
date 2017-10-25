package com.rider.democube;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rider.democube.praise.PraiseActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.btn_praise);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpView(PraiseActivity.class);
            }
        });

    }


    private void jumpView(Class nextView){
        Intent intent = new Intent(this,nextView);
        startActivity(intent);
    }
}
