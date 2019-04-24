package hu.first.saytheword;

/** Feladatkiiras:
Say the Word: A SpeechRecognisation implementálása. A strings.xml-ben egy string tömb tárolása,
melynek egy random eleme megjelenik a képernyőn. Alatta megjelenik egy gomb, melyet megnyomva a
felhasználónak ki kell mondania a szöveget, ha ez sikeres volt, a felirat kizöldül, a START gomb
pedig egy NEW-ra változik, amelyre nyomva egy új szó jelenik meg vörösen a képernyőn.
 */

/**
 * Tesztelos keszulek:
 * Xiaomi Redmi 4X
 * Android: 7.1.2 Nougat, API level 25
 */

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner sp = findViewById(R.id.lang_spinner);
        if(sp != null) {
            sp.setOnItemSelectedListener(this);
        }
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                        this,
                        R.array.languages,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if(sp != null) {
            sp.setAdapter(adapter);
        }
    }

    public void onclickDetails(View view){
        startActivity(new Intent(this, Details.class));
    }

    public void onclickPlay(View view){
        int id = view.getId();

        switch(id) {
            case R.id.single_button:
                startActivity(new Intent(this, SinglePlayerGame.class));
                break;
            case R.id.race_button:
                startActivity(new Intent(this, TwoPlayersGame.class));
                break;
            default:
                break;
        }
    }

    /**
     * A ranglista megjelenitese.
     * @param view
     */
    public void onclickAch(View view){
        startActivity(new Intent(this, Achivements.class));
    }

    /**
     * User altal nyelvvalasztas.
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String lang = parent.getItemAtPosition(position).toString();
        //if (!lang.equals(" ")) {
            changeLanguage(lang);
        //}
    }

    /**
     * Ha a user nem valtoztat nyelvet, az az android rendszer default-ja marad.
     * @param parent
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    /**
     * Nyelvvaltoztato metodus.
     * @param lang
     */
    private void changeLanguage(String lang){
        /*Resources resources = getResources();
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = locale;
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        /*finish();
        startActivity(getIntent());*/
    }
}
