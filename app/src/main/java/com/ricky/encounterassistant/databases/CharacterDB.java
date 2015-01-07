package com.ricky.encounterassistant.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ricky.encounterassistant.models.Avatar;
import com.ricky.encounterassistant.models.Character;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Ricky on 1/3/2015.
 */
public class CharacterDB {
    private static final String TAG = "characterDB";

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_AC = "ac";
    public static final String KEY_MAX_HP = "max_hp";
    public static final String KEY_HP = "hp";
    public static final String KEY_INIT = "init";
    public static final String KEY_AVATAR = "avatar";

    public static final String DATABASE_NAME = "CharacterDB";
    public static final String DATABASE_TABLE = "CharacterTable";
    public static final int DATABASE_VERSION = 2;
    private static final String DATABASE_CREATE_SQL =
            "create table " + DATABASE_TABLE
            + " ("
            + KEY_ID + " string not null, "
            + KEY_NAME + " string not null, "
            + KEY_AC + " int not null, "
            + KEY_MAX_HP + " int not null, "
            + KEY_HP + " int not null, "
            + KEY_INIT + " int not null, "
            + KEY_AVATAR + " string not null"
            + ");";

    private final Context context;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public CharacterDB(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(this.context);
    }

    public CharacterDB open() {
        database = databaseHelper.getWritableDatabase();
        return this;
    }

    public void insertUpdateCharacter(Character character) {
        UUID id = character.getId();
        /**
         * If cursor contains a row that has the same id as the character passed in,
         * update that character.
         * Otherwise insert a new entry for that character into the table.
          */
        if(hasCharacter(id)) {
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, character.getName());
            values.put(KEY_AC, character.getAC());
            values.put(KEY_MAX_HP, character.getMaxHP());
            values.put(KEY_HP, character.getHP());
            values.put(KEY_INIT, character.getInit());
            values.put(KEY_AVATAR, character.getAvatarString());
            database.update(DATABASE_TABLE, values, KEY_ID + " = ?",
                    new String[] {character.getId().toString()}
            );
        } else {
            ContentValues values = new ContentValues();
            values.put(KEY_ID, character.getId().toString());
            values.put(KEY_NAME, character.getName());
            values.put(KEY_AC, character.getAC());
            values.put(KEY_MAX_HP, character.getMaxHP());
            values.put(KEY_HP, character.getHP());
            values.put(KEY_INIT, character.getInit());
            values.put(KEY_AVATAR, character.getAvatarString());
            database.insert(DATABASE_TABLE, null, values);
        }
    }

    public void removeCharacter(UUID id) {
        database.delete(DATABASE_TABLE,
                KEY_ID + " = ?", new String[] {id.toString()});
    }

    public Character extractCharacter(UUID id) {
        Cursor cursor = getCharacterCursor(id);
        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME));
        int ac = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_AC));
        int max = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_MAX_HP));
        int hp = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_HP));
        int init = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_INIT));
        String avatar = cursor.getString(cursor.getColumnIndexOrThrow(KEY_AVATAR));
        Character character = new Character(name, ac, max, hp, init, new Avatar(context, avatar), context);
        character.setId(UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID))));
        cursor.close();
        return character;
    }

    public List<Character> extractAllCharacter() {
        Cursor cursor = database.rawQuery("select * from " + DATABASE_TABLE, null);
        List<Character> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            UUID id = UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID)));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME));
            int ac = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_AC));
            int max = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_MAX_HP));
            int hp = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_HP));
            int init = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_INIT));
            String avatar = cursor.getString(cursor.getColumnIndexOrThrow(KEY_AVATAR));
            Character character = new Character(name, ac, max, hp, init, new Avatar(context, avatar), context);
            character.setId(id);
            list.add(character);
        }
        cursor.close();
        return list;
    }

    public boolean hasCharacter(UUID id) {
        Cursor cursor = getCharacterCursor(id);
        boolean r = cursor != null && cursor.getCount() > 0;
        cursor.close();
        return r;
    }

    private Cursor getCharacterCursor(UUID id) {
        return database.rawQuery(
                    "select * from " + DATABASE_TABLE
                    + " where " + KEY_ID + " = ?"
                    , new String[] {id.toString()}
            );
    }

    public void close() {
        database.close();
    }

    /////////////////////////////////////////////////////////////////////
    //	Private Helper Classes:
    /////////////////////////////////////////////////////////////////////

    /**
     * Private class which handles database creation and upgrading.
     * Used to handle low-level database access.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DATABASE_CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

            // Recreate new database:
            onCreate(_db);
        }
    }
}
