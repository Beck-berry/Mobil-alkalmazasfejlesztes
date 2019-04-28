package hu.first.saytheword;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Documentation used:
 * https://developer.android.com/training/data-storage/sqlite
 * https://www.tutorialspoint.com/android/android_sqlite_database.htm
 * https://stackoverflow.com/questions/5457699/cursor-adapter-and-sqlite-example/20532937#20532937
 */

public class Achivements extends AppCompatActivity {

    //private TextView nickname;
    //private TextView points;
    //private TextView date;
    private TableLayout tl;

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS Achivements(nickname VARCHAR, points VARCHAR, date VARCHAR, id NUMBER PRIMARY KEY);";
    private static final String LIST = "SELECT nickname, points, date FROM Achivements ORDER BY points desc, date asc LIMIT 10";

    public static class DataBase implements BaseColumns {
        public static final String TABLE_NAME = "Achivements";
        public static final String NICKNAME_COL = "nickname";
        public static final String POINTS_COL = "points";
        public static final String DATE_COL = "date";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achivements);

        tl = findViewById(R.id.tableLayout);

        Intent intent = getIntent();

        /* ez csak tesztnek
        nickname = findViewById(R.id.first_nickname);
        points = findViewById(R.id.first_points);
        date = findViewById(R.id.first_date);

        nickname.setText(intent.getStringExtra(SaveTheGame.EXTRA_NN));
        points.setText(intent.getStringExtra(SaveTheGame.EXTRA_P));
        date.setText(intent.getStringExtra(SaveTheGame.EXTRA_D));*/

        ContentValues values = new ContentValues();
        values.put(DataBase.NICKNAME_COL, intent.getStringExtra(SaveTheGame.EXTRA_NN));
        values.put(DataBase.POINTS_COL, intent.getStringExtra(SaveTheGame.EXTRA_P));
        values.put(DataBase.DATE_COL, intent.getStringExtra(SaveTheGame.EXTRA_D));

        // TODO beerkezo uj eredmeny eseten rendezni, else csak megjeleniteni adatbazisbol
        SQLiteDatabase mydatabase = openOrCreateDatabase("sayTheWordDB", MODE_PRIVATE,null);
        mydatabase.execSQL(CREATE_TABLE);
        mydatabase.insert(DataBase.TABLE_NAME, null, values);

        Cursor resultSet = mydatabase.rawQuery(LIST,null);
        int rows = resultSet.getCount();
        int cols = 3;
        if (rows != 0) {
            resultSet.moveToFirst();
            do {
                TableRow row = new TableRow(this);
                row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                for (int j = 0; j < cols; j++) {
                    TextView tv = new TextView(this);
                    tv.setLayoutParams(new TableRow.LayoutParams(j+1));
                    tv.setGravity(Gravity.CENTER);
                    tv.setText(resultSet.getString(j));
                    row.addView(tv, j);
                }
                tl.addView(row);
            } while (resultSet.moveToNext());
        }
        resultSet.close();
    }
}
