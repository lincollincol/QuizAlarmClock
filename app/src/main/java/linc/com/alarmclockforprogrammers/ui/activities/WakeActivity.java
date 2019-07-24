package linc.com.alarmclockforprogrammers.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import linc.com.alarmclockforprogrammers.R;

public class WakeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wake);

        TextView tv = findViewById(R.id.text);
        String s = "id" +  getIntent().getIntExtra("ALARM_ID", 0);
        tv.setText(s);
    }
}
