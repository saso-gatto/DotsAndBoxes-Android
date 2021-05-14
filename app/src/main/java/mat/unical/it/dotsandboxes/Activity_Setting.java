package mat.unical.it.dotsandboxes;

import android.app.Activity;
import android.app.assist.AssistStructure;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class Activity_Setting extends AppCompatActivity {
    public static Switch toggleMusica;
    public View temp;

    public static boolean getChecked(){
        if (toggleMusica==null)
            return true;
        return toggleMusica.isChecked();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        toggleMusica = findViewById(R.id.btnMusica);
        Button home = findViewById(R.id.btnHome);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Setting.this, Activity_Start.class);
                startActivity(intent);
               finish();
            }
        });
        Button info = findViewById(R.id.btnInfo);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Setting.this, Activity_Info.class);
                startActivity(intent);

            }
        });
    }
}