package com.codelab.basics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DBClass extends SQLiteOpenHelper implements DB_Interface {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "pokemon.db";
    private static final String TABLE_NAME = "sample_table";
    private static final String TEXT_TYPE = " TEXT";
    private static final String NUM_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String _ID = "_ID";
    private static final String _COL_1 = "dex_col";
    private static final String _COL_2 = "name_col";
    private static final String _COL_3 = "type1_col";
    private static final String _COL_4 = "type2_col";
    private static final String _COL_5 = "total_col";
    private static final String _COL_6 = "hp_col";
    private static final String _COL_7 = "attack_col";
    private static final String _COL_8 = "defense_col";
    private static final String _COL_9 = "specialAttack_col";
    private static final String _COL_10 = "specialDefense_col";
    private static final String _COL_11 = "speed_col";
    private static final String _COL_12 = "generation_col";
    private static final String _COL_13 = "legendary_col";
    private static final String _COL_14 = "access_col";
    private static final String _COL_15 = "image_col";
    private static final String _COL_16 = "link_col";

    // Create the table in Java, use a BLOB (Binary Large OBject) to store lots of binary data, in this case, an image
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, dex_col INTEGER, name_col VARCHAR(256), type1_col VARCHAR(256), type2_col VARCHAR(256), total_col INTEGER, hp_col INTEGER, attack_col INTEGER, defense_col INTEGER, specialAttack_col INTEGER, specialDefense_col INTEGER, speed_col INTEGER, generation_col INTEGER, legendary_col INTEGER, access_col INTEGER, image_col BLOB, link_col VARCHAR(500))";
    private static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private Context context;

    public DBClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // Create the table in SQL and log, implementing interface methods
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DBClass", "DB onCreate() " + SQL_CREATE_TABLE);
        db.execSQL(SQL_CREATE_TABLE);
        Log.d("DBClass", "DB onCreate()");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        Log.d("DBClass", "DB onUpgrade() to version " + DATABASE_VERSION);
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }

    @Override
    public int count() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        Log.v("DBClass", "getCount=" + cnt);
        return cnt;
    }

    @Override
    public int save(PokeModel pokeModel) {
        // Logs and puts the pokeModel (Pokemon) into the DB
        Log.v("DBClass", "add=>  " + pokeModel.toString());

        // Get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // Create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(_COL_1, pokeModel.getDexNumber());
        values.put(_COL_2, pokeModel.getPokemonName());
        values.put(_COL_3, pokeModel.getType1());
        values.put(_COL_4, pokeModel.getType2());
        values.put(_COL_5, pokeModel.getTotal());
        values.put(_COL_6, pokeModel.getHP());
        values.put(_COL_7, pokeModel.getAttack());
        values.put(_COL_8, pokeModel.getDefense());
        values.put(_COL_9, pokeModel.getSpecialAttack());
        values.put(_COL_10, pokeModel.getSpecialDefense());
        values.put(_COL_11, pokeModel.getSpeed());
        values.put(_COL_12, pokeModel.getGeneration());
        values.put(_COL_13, pokeModel.getLegendary());
        values.put(_COL_14, pokeModel.getAccess());
        values.put(_COL_15, pokeModel.getImage());
        values.put(_COL_16, pokeModel.getLink());

        // Insert
        db.insert(TABLE_NAME, null, values);

        // Close
        db.close();
        return 0;
    }

    // Since the only data that is actively being changed is the access count, this update method just updates that
    @Override
    public void update(PokeModel pokeModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(_COL_14, pokeModel.getAccess()+1);

        db.update(TABLE_NAME, values, "name_col = ?", new String[] { pokeModel.getPokemonName() } );
        db.close();
    }

    // Now gets data from the CSV file and adds it to the DB, now in a try catch since we're working with more outside factors
    private void addDefaultRows(){
        // Call count once
        int doCount = this.count();
        if (doCount > 1) {
            Log.v("DBClass", "Already rows in DB");

        } else {
            Log.v("DBClass", "No rows in DB... Adding from CSV");
            BufferedReader bufferedReader = null;
            int loop = 0;
            try {
                InputStream inputStream = context.getAssets().open("pokemon.csv");
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                // This is to skip the first line, which contains the names for the cols in the CSV
                bufferedReader.readLine();

                while ((line = bufferedReader.readLine()) != null) {
                    String[] pokeData = line.split(",");
                    int id = loop;
                    loop++;

                    int dexNumber = Integer.parseInt(pokeData[0]);
                    String name = pokeData[1];
                    String type1 = pokeData[2];

                    // Instead of having an empty value for a second type if the Pokemon doesn't have it, "None" is used. No big reason for it, I just prefer it this way
                    String type2 = !pokeData[3].isEmpty() ? pokeData[3] : "None";
                    int total = Integer.parseInt(pokeData[4]);
                    int hp = Integer.parseInt(pokeData[5]);
                    int attack = Integer.parseInt(pokeData[6]);
                    int defense = Integer.parseInt(pokeData[7]);
                    int specialAttack = Integer.parseInt(pokeData[8]);
                    int specialDefense = Integer.parseInt(pokeData[9]);
                    int speed = Integer.parseInt(pokeData[10]);
                    int generation = Integer.parseInt(pokeData[11]);

                    // Legendary check because I can't use boolean because Google is dumb (no cursor.getBoolean method)
                    int legendary;
                    if (pokeData[12].equals("True")) {
                        legendary = 1;
                    } else {
                        legendary = 0;
                    }
                    int access = 0;
                    // Image is null since we don't have one as default (if we did, that would kind of defeat the point of the link)
                    byte[] image = null;
                    String link = pokeData[13];

                    PokeModel pokeModel = new PokeModel(id, dexNumber, name, type1, type2, total, hp, attack,
                            defense, specialAttack, specialDefense, speed, generation, legendary, access, image, link);
                    this.save(pokeModel);
                }

            } catch (IOException e){
                Log.e("DBClass", "Error reading CSV", e);
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        Log.e("DBClass", "Error closing reader", e);
                    }
                }
            }
        }

    }

    @Override
    public List<PokeModel> findAll() {
        List<PokeModel> temp = new ArrayList<PokeModel>();

        // If no rows, add
        addDefaultRows();

        // Build the query
        String query = "SELECT  * FROM " + TABLE_NAME;

        // Get reference to writable DB
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // Go over each row, build and add it to list
        PokeModel item;
        if (cursor.moveToFirst()) {
            do {
                // This code puts a pokeModel object into the PlaceHolder for the fragment if you had more columns in the DB, you'd format  them in the non-details list here
                item = new PokeModel(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6), cursor.getInt(7), cursor.getInt(8), cursor.getInt(9), cursor.getInt(10), cursor.getInt(11), cursor.getInt(12), cursor.getInt(13), cursor.getInt(14), cursor.getBlob(15), cursor.getString(16));
                temp.add(item);
            } while (cursor.moveToNext());
        }
        Log.v("DBClass", "findAll=> " + temp.toString());

        // Return all
        return temp;
    }

    // Despite not being used, still decided to implement these functions for the fun of it and so they're not nothing burger functions, nothing fancy. See findImageByName for why I did this try catch stuff
    @Override
    public String getNameById(Long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String name = null;
        String query = "SELECT " + _COL_2 + " FROM " + TABLE_NAME + " WHERE id = ?";
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, new String[]{String.valueOf(id)});
            if (cursor != null && cursor.moveToFirst()) {
                name = cursor.getString(0);
            }
        } catch (Exception e) {
            Log.e("DBClass", "Couldn't find name by ID", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return name;
    }

    @Override
    public int deleteById(Long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted;
    }

    // Finds an image by name, id works just as well, but name is easier to understand, and the names are still unique, even factoring the same Pokemon with more than one form
    public byte[] findImageByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        byte[] imageBytes = null;

        // Creates the SQL query to update the image in the db
        String query = "SELECT " + _COL_15 + " FROM " + TABLE_NAME + " WHERE " + _COL_2 + " = ?";
        Cursor cursor = null;
        // Was having some issues, this was not the problem but, as a result, it's fancy with a try catch when making sure this was not the problem. (Good to have if the Pokemon doesn't exist for whatever reason, too, so implemented in the getNameById function as well).
        try {
            cursor = db.rawQuery(query, new String[]{name});
            if (cursor != null && cursor.moveToFirst()) {
                imageBytes = cursor.getBlob(0);
            }
        } catch (Exception e) {
            Log.e("DBClass", "Error fetching image", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return imageBytes;
    }

    // Update the image. Since the Pokemon don't have default images, this is 100% required. Using name for the same reason as findImageByName
    public void updateImageByName(String name, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(_COL_15, image);
        db.update(TABLE_NAME, values, _COL_2 + " = ?", new String[]{name});
        db.close();
    }
}
