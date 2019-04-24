package hu.first.saytheword;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import hu.first.saytheword.SinglePlayerGame;

public class Achivements extends AppCompatActivity {

    private TextView nickname;
    private TextView points;
    private TextView date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achivements);

        // ez csak tesztnek, egyebkent rendezni kellene a beerkezo adatot
        nickname = findViewById(R.id.first_nickname);
        points = findViewById(R.id.first_points);
        date = findViewById(R.id.first_date);

        Intent intent = getIntent();
        nickname.setText(intent.getStringExtra(SaveTheGame.EXTRA_NN));
        points.setText(intent.getStringExtra(SaveTheGame.EXTRA_P));
        date.setText(intent.getStringExtra(SaveTheGame.EXTRA_D));

        // TODO beerkezo uj eredmeny eseten rendezni, else csak megjeleniteni adatbazisbol
    }

    /*@Override
    public void onBackPressed () {

    }*/
}
