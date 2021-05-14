package mat.unical.it.dotsandboxes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Activity_End extends AppCompatActivity {

    private Button home, condividi;
    private TextView nomeVincitore, tempo;

    private String vincitore;
    private int tempoCronometro, punteggio1, punteggio2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            vincitore = extras.getString("vincitore");
            tempoCronometro = extras.getInt("tempo");

            punteggio1 = extras.getInt("punteggio1");
            punteggio2 = extras.getInt("punteggio2");

            System.out.println(punteggio1+" "+punteggio2);


        }

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.layoutEnd);
        if(punteggio1 > punteggio2)
            relativeLayout.setBackgroundResource(R.mipmap.vincitoreblu);
        else if (punteggio1 < punteggio2)
            relativeLayout.setBackgroundResource(R.mipmap.vincitorearancione);



        home = (Button) findViewById(R.id.btnHome_activityEnd);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_End.this, Activity_Start.class);
                startActivity(intent);
                finish();
            }
        });


        condividi = (Button) findViewById(R.id.btnCondividi_activityEnd);
        condividi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        nomeVincitore = (TextView) findViewById(R.id.nomeVincitore_endActivity);
        nomeVincitore.setText(vincitore);
        if(punteggio1 > punteggio2)
            nomeVincitore.setTextColor(Color.parseColor("#02a3d0"));
        else
            nomeVincitore.setTextColor(Color.parseColor("#fc7e3f"));

        tempo = (TextView) findViewById(R.id.time_endActivity);
        tempo.setText(String.valueOf(tempoCronometro));



    }


}