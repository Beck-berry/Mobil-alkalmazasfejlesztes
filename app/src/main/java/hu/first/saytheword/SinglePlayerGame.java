package hu.first.saytheword;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// import hu.first.saytheword.Game;

/** Official documentation used:
 *  https://developer.android.com/reference/android/speech/SpeechRecognizer
 *
 *  Non-official documentation used:
 *  https://www.techjini.com/blog/android-speech-to-text-tutorial-part1/
 *  https://androidforums.com/threads/on-press-and-hold.421554/
 *  https://stackoverflow.com/questions/7973023/what-is-the-list-of-supported-languages-locales-on-android
 */

public class SinglePlayerGame extends AppCompatActivity {
    private TextView mSzo;
    private TextView mPoints;
    private Button newBtn;
    private ImageButton listenBtn;
    private String[] words;
    private ArrayList<CharSequence> usedWords = new ArrayList<>();
    private static final int RESULT_SPEECH = 3000;
    private SpeechRecognizer sr = null;

    /**
     * Megkeresi a szukseges View-kat, valtozoba teszi oket.
     * Legeneralja az elso random szot.
     * Random szot general.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player_game);

        this.words = this.getResources().getStringArray(R.array.words);
        this.newBtn = findViewById(R.id.new_button);
        this.listenBtn = findViewById(R.id.listen1_button);
        this.mPoints = findViewById(R.id.points_textView);
        this.mSzo = findViewById(R.id.word_textView);

        this.mSzo.setText(words[new Random().nextInt(words.length)]);
        this.usedWords.add(mSzo.getText());

        setNewRound();

        /** Ha nincs internet kapcsolat, nem mukodik majd a hangfelismeres. Ertesiteni kell a usert.*/
        if(!isThereInternetConnection()){
            new AlertDialog.Builder(this)
                    .setTitle(R.string.noConnTitle)
                    .setMessage(R.string.noConnDesc)
                    .setNeutralButton("OK", null) // TODO vissza a kezdolapra
                    .show();
        }
    }

    public boolean isThereInternetConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }

    /**
     * A gombra kattintaskor uj szot ad.
     * Ha a szo mar szerepelt, ujat general.
     */
    public void onClickNew(View view){
        do {
            this.mSzo.setText(words[new Random().nextInt(words.length)]);
        } while (usedWords.contains(mSzo.getText()));
        usedWords.add(mSzo.getText());

        setNewRound();
    }

    /**
     * A gomb nyomva tartasa alatt fut a speech recognition, az elengedesevel vege a kornek.
     * @param view
     * @return
     */
    public void onClickListen(View view) {
        //Game listener = new Game();
        sr = SpeechRecognizer.createSpeechRecognizer(this);
        //sr.setRecognitionListener(listener);
        listenBtn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    listenBtn.setBackgroundColor(Color.GREEN);
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, mSzo.getText());

                    //sr.startListening(intent);

                    try {
                        startActivityForResult(intent, RESULT_SPEECH);
                    } catch (ActivityNotFoundException a) {
                        Toast.makeText(getApplicationContext(),"HIBA",Toast.LENGTH_SHORT).show();
                    }
                } else if(event.getAction() == android.view.MotionEvent.ACTION_UP){
                    sr.destroy();
                }
                return true;
            }
        });
    }

    /**
     * A hangfelismeres eredmenyet osszeveti az elvart valasszal.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String mAnswer;

        if (requestCode == RESULT_SPEECH) {
            if (resultCode == RESULT_OK) {
                List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                mAnswer = results.get(0);
                if (mAnswer.toLowerCase().equals(mSzo.getText().toString().toLowerCase())){
                    setPlayerWasRight();
                    int tmp = Integer.parseInt(mPoints.getText().toString())+1;
                    mPoints.setText(String.valueOf(tmp));
                } else {
                    setPlayerWasWrong();
                }
            }
        }
    }

    /**
     * Az aktualis jatek eredmenyet lehet elmenteni. Uj Activityt indit az adatok megadasahoz.
     * @param view
     */
    public void onClickSave(View view){
        Intent intent = new Intent(this, SaveTheGame.class);
        intent.putExtra(Intent.EXTRA_TEXT, mPoints.getText());
        intent.setType("text/plain");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
            usedWords.clear();
            finish();
        }
    }

    private void setNewRound(){
        this.mSzo.setTextColor(Color.BLACK);
        this.listenBtn.setBackgroundColor(Color.TRANSPARENT);
        this.newBtn.setClickable(false);
        this.newBtn.setBackgroundColor(Color.GRAY);
    }

    private void setPlayerWasRight(){
        mSzo.setTextColor(Color.GREEN);
        newBtn.setBackgroundColor(Color.GREEN);
        newBtn.setClickable(true);
    }

    private void setPlayerWasWrong(){
        mSzo.setTextColor(Color.RED);
        listenBtn.setBackgroundColor(Color.RED);
    }
}
