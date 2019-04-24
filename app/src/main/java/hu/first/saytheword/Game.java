package hu.first.saytheword;

// https://developer.android.com/guide/topics/media/mediarecorder

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.TextView;
import hu.first.saytheword.SinglePlayerGame;
import java.util.ArrayList;

/** Official documentation used:
 *  https://developer.android.com/reference/android/speech/SpeechRecognizer
 *  https://www.techjini.com/blog/android-speech-to-text-tutorial-part1/
 *  https://riptutorial.com/android/example/21621/speech-to-text-conversion
 */

public class Game implements RecognitionListener {
    private String mAnswer;

    public Game(){

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
        /*Intent data = getIntent();
        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        mAnswer = results.get(0).toString();
        if (mAnswer.toLowerCase().equals(mSzo.getText().toString().toLowerCase())){
            mSzo.setTextColor(Color.GREEN);
            int tmp = Integer.parseInt(mPoints.getText().toString())+1;
            SinglePlayerGame.mPoints.setText(String.valueOf(tmp));
            this.newBtn.setClickable(true);
            this.newBtn.setBackgroundColor(Color.GREEN);
        } else {
            mSzo.setTextColor(Color.RED);
        }*/
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }
}
