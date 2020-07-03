package com.example.gameofdraughts;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.content.Context;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
      private Button resetBtn;
      private Context context;
      Intent intent;
//    static Context context;
    Canvas canvas = new Canvas();
    CustomView customView;
    TextView currentPlayerTV, p1CountTV,p2CountTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customView = findViewById(R.id.cv);
        currentPlayerTV =  findViewById(R.id.currentPlayerTV);
        p1CountTV = findViewById(R.id.p1CountTV);
        p2CountTV = findViewById(R.id.p2CountTV);
        customView.setText(currentPlayerTV);
        customView.setp1Text(p1CountTV);
        customView.setp2Text(p2CountTV);


        resetBtn = findViewById(R.id.resetBtn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = getIntent();
//                finish();
//                startActivity(intent);
                customView.init();
                customView.invalidate();
            }
        });


    }
}
