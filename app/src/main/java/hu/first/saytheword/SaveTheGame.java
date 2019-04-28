package hu.first.saytheword;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SaveTheGame extends AppCompatActivity {

    private EditText nickname;
    private TextView earnedPoints;
    private TextView today;

    public static final String EXTRA_NN = "hu.first.saytheword.Save.nickname";
    public static final String EXTRA_P = "hu.first.saytheword.Save.points";
    public static final String EXTRA_D = "hu.first.saytheword.Save.date";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_the_game);

        nickname = findViewById(R.id.name_editText);
        earnedPoints = findViewById(R.id.earnedPoints_textView);
        today = findViewById(R.id.today_textView);

        Intent intent = getIntent();
        earnedPoints.setText(intent.getStringExtra(Intent.EXTRA_TEXT));
        today.setText(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(new Date()));
    }


    public void onClickSend(View view){
        Intent intent = new Intent(this, Achivements.class);
        intent.putExtra(EXTRA_NN, nickname.getText().toString())
                .putExtra(EXTRA_P, earnedPoints.getText())
                .putExtra(EXTRA_D, today.getText());
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
            finish();
        }
    }
}
