package it.dotsandboxes;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.ImageView;

public class Activity_Game extends AppCompatActivity {

    protected static final float radius = (float) 14 / 824;
    protected static final float start = (float) 6 / 824;
    protected static final float add1 = (float) 18 / 824;
    protected static final float add2 = (float) 2 / 824;
    protected static final float add3 = (float) 14 / 824;
    protected static final float add4 = (float) 141 / 824;
    protected static final float add5 = (float) 159 / 824;
    protected static final float add6 = (float) 9 / 824;
    private  GamePlay gamePlay;
    private ImageView disegno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gamePlay = findViewById(R.id.gameView);
        disegno = findViewById(R.id.immagine);


    }
}