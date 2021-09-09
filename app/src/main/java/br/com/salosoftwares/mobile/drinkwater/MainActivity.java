package br.com.salosoftwares.mobile.drinkwater;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText etxtMinutes;
    private TimePicker clockDrinkWater;
    private Button btnNotify;
    private Boolean activated;
    private SharedPreferences dbSP;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    // ON CREATE OPEN !!!
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etxtMinutes = findViewById(R.id.etxt_Minutes);
        clockDrinkWater = findViewById(R.id.clock_Drink_Water);
        btnNotify = findViewById(R.id.btn_Notify);
        dbSP =getSharedPreferences("db", Context.MODE_PRIVATE);

        clockDrinkWater.setIs24HourView(true);
        //dbSP.getBoolean("activated",false);

        activated=dbSP.getBoolean("activated",false);
        if(activated){
            btnNotify.setText(R.string.pause_btnNotify);
            btnNotify.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.black));
            int interval= dbSP.getInt("interval",0);
            int hour = dbSP.getInt("hour",clockDrinkWater.getCurrentHour());
            int minutes = dbSP.getInt("minutes",clockDrinkWater.getCurrentMinute());

            etxtMinutes.setText(String.valueOf(interval));
            clockDrinkWater.setCurrentHour(hour);
            clockDrinkWater.setCurrentMinute(minutes);

        }
    }
    // ON CREATE CLOSE !!!

    //CLICK EVENT USING XML!!! OPEN:
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void notifyClick(View view) {

        String stringInterval = etxtMinutes.getText().toString();
        int hour = clockDrinkWater.getCurrentHour();
        int minutes = clockDrinkWater.getCurrentMinute();


        if(stringInterval.isEmpty()){
            Toast.makeText(this,R.string.error_empty_interval,Toast.LENGTH_LONG).show();
            return;
        }

        int interval = Integer.parseInt(stringInterval);

        if(!activated){
            btnNotify.setText(R.string.pause_btnNotify);
            btnNotify.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.black));
            activated=true;
            SharedPreferences.Editor editor = dbSP.edit();
            editor.putBoolean("activated",true);
            editor.putInt("hour",hour);
            editor.putInt("minutes",minutes);
            editor.putInt("interval",interval);
            editor.apply();

        }else{
            btnNotify.setText(R.string.btn_notify);
            btnNotify.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.teal_700));
            activated=false;
            SharedPreferences.Editor editor = dbSP.edit();
            editor.putBoolean("activated",false);
            editor.remove("hour");
            editor.remove("minutes");
            editor.remove("interval");
            editor.apply();
        }


        Log.i("Clicou", "Clicked on notify, range: "+interval);
    }
    //CLICK EVENT USING XML!!! CLOSE:

}
