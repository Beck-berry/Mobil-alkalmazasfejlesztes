package hu.first.saytheword;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import java.util.Random;

/** Documentation used:
 *  https://developer.android.com/reference/android/speech/SpeechRecognizer
 *  https://www.techjini.com/blog/android-speech-to-text-tutorial-part1/
 *  https://androidforums.com/threads/on-press-and-hold.421554/
 *  https://stackoverflow.com/questions/7973023/what-is-the-list-of-supported-languages-locales-on-android
 */

public class SinglePlayerGame extends AppCompatActivity implements RecognitionListener {
    private TextView mSzo;
    private TextView mPoints;
    private Button newBtn;
    private ImageButton listenBtn;
    private String[] words;
    private ArrayList<CharSequence> usedWords = new ArrayList<>();
    private static final int RESULT_SPEECH = 3000;
    private static final int REQUEST_RECORD_PERMISSION = 100;
    private SpeechRecognizer sr = null;
    private Intent intent = null;

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

        /** Ha nincs internet kapcsolat, nem mukodik majd a hangfelismeres. Ertesiteni kell a usert.*/
        if(!isThereInternetConnection()){
            new AlertDialog.Builder(this)
                    .setTitle(R.string.noConnTitle)
                    .setMessage(R.string.noConnDesc)
                    .setNeutralButton("OK", null) // TODO vissza a kezdolapra
                    .show();
        } else setNewRound();
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
        sr = SpeechRecognizer.createSpeechRecognizer(this);
        sr.setRecognitionListener(this);
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, mSzo.getText());
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

        listenBtn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    listenBtn.setBackgroundColor(Color.GREEN);

                    //sr.startListening(intent);
                    ActivityCompat.requestPermissions(SinglePlayerGame.this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        REQUEST_RECORD_PERMISSION);

                    /*try {
                        startActivityForResult(intent, RESULT_SPEECH);
                    } catch (ActivityNotFoundException a) {
                        Toast.makeText(getApplicationContext(),"HIBA",Toast.LENGTH_SHORT).show();
                    }*/
                } else if(event.getAction() == android.view.MotionEvent.ACTION_UP){
                    listenBtn.setBackgroundColor(Color.TRANSPARENT);
                    sr.stopListening();
                }
                return true;
            }
        });
    }

    /*
     * A hangfelismeres eredmenyet osszeveti az elvart valasszal.
     * @param requestCode
     * @param resultCode
     * @param data

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
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sr.startListening(intent);
                } else {
                    Toast.makeText(SinglePlayerGame.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onStop() {
        super.onStop();
        if (sr != null) {
            sr.destroy();
        }
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
    }

    @Override
    public void onBeginningOfSpeech() {
    }

    @Override
    public void onRmsChanged(float rmsdB) {
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
    }

    @Override
    public void onEndOfSpeech() {
    }

    @Override
    public void onError(int error) {
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> speech = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String mAnswer = speech.get(0);
        if (mAnswer.toLowerCase().equals(mSzo.getText().toString().toLowerCase())){
            setPlayerWasRight();
            int tmp = Integer.parseInt(mPoints.getText().toString())+1;
            mPoints.setText(String.valueOf(tmp));
        } else {
            setPlayerWasWrong();
        }
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
    }
}
