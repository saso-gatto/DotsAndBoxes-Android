package mat.unical.it.dotsandboxes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class Activity_End extends AppCompatActivity {

    private Button home, ricomincia;
    private TextView nomeVincitore, tempo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        home = (Button) findViewById(R.id.btnHome_activityEnd);
        ricomincia = (Button) findViewById(R.id.btnRicomincia_activityEnd);

        nomeVincitore = (TextView) findViewById(R.id.nomeVincitore_endActivity);
        tempo = (TextView) findViewById(R.id.time_endActivity);


    }


}