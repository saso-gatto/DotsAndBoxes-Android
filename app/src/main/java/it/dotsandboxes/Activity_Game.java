package it.dotsandboxes;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ImageView img= (ImageView) findViewById(R.id.immagine);
        // 1. preparazione Bitmap
        Bitmap bmp=Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);

        // 2. istanziamo Canvas basato sulla Bitmap
        Canvas canvas=new Canvas(bmp);

        // 3. assegniamo sfondo blu al Drawable
        canvas.drawColor(Color.BLUE);

        // 4. per disegnare il cerchio, abbiamo bisogno di un Paint
        Paint paint=new Paint();

        // 5. il Paint userà il colore giallo
        paint.setColor(Color.YELLOW);

        // 6. usando il Paint disegniamo un cerchio nella Drawable
        canvas.drawCircle(200,200, 100, paint);

        // 7. la Bitmap disegnata con il Canvas diventa il contenuto della ImageView
        img.setImageBitmap(bmp);

    }
}