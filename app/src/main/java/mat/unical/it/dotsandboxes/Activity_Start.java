package mat.unical.it.dotsandboxes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anubhav.android.customdialog.CustomDialog;


public class Activity_Start extends AppCompatActivity {

    private int sceltag1=0;
    private int sceltag2=0;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        context=this;

        Button btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sceltag1 == 0 || sceltag2 == 0)
                    Toast.makeText(getApplicationContext(),"Seleziona i giocatori!",Toast.LENGTH_SHORT).show();
                else {
                    int indice = calcolaIndice();
                    Intent intent = new Intent(Activity_Start.this, MainActivity.class);
                    intent.putExtra("key",indice);
                    startActivity(intent);
                    finish();
                }
            }
        });


        Button btnSetting = (Button) findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new CustomDialog.Builder(context)
                        .setCustomView(R.layout.setting_layout)
                        .setBtnConfirmText("Indietro")
                        .setBtnConfirmTextSizeDp(16)
                        .setBtnConfirmTextColor("#95D3EC")
                        .show();
         /*

                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View customView = inflater.inflate(R.layout.setting_layout, null);

                Dialog simpleDialog = DialogUtils.createSimpleDialog(context, customView, true);
                if (simpleDialog != null && !simpleDialog.isShowing()) {
                    simpleDialog.show();
                }

*/
            }
        });










    }

    private int calcolaIndice() {
        if(sceltag1==1 && sceltag2 == 1) // Umano umano = 1
            return 1;
        else if(sceltag1==1 && sceltag2 == 2) // Umano dlv = 2
            return 2;
        else if(sceltag1==2 && sceltag2 == 1) // dlv umano = 3
            return 3;
        else if(sceltag1==2 && sceltag2 == 2) // dlv dlv = 4
            return 4;

        return 0;
    }

    public void onRadioButtonClickedG1(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.g1Umano:
                if (checked) {
                    sceltag1 = 1;
                    break;
                }
            case R.id.g1Dlv:
                if (checked) {
                    sceltag1 = 2;
                    break;
                }
        }

    }

    public void onRadioButtonClickedG2(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.g2Umano:
                if (checked){
                    sceltag2=1;
                    break;
                }
            case R.id.g2Dlv:
                if (checked) {
                    sceltag2 = 2;
                    break;
                }
        }
    }


}