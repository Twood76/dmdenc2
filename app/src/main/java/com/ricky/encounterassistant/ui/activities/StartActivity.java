package com.ricky.encounterassistant.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.ricky.encounterassistant.*;
import com.ricky.encounterassistant.databases.CharacterDB;
import com.ricky.encounterassistant.models.Avatar;
import com.ricky.encounterassistant.models.Character;
import com.ricky.encounterassistant.models.Encounter;

import java.util.List;
import java.util.UUID;

/*
 * This activity is the initial activity that loads when the application first starts
 * If Player Character button is pressed, go to character screen
 * If Dungeon Master button is pressed, go to dungeon master screen
 */
public class StartActivity extends Activity {
    private static final String TAG = "StartActivity";

    public static final String KEY_ID = "id";
    Button PCButton;
    Button DMButton;
    public static UUID characterId;
    private static final int REQUEST_CHARACTER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        PCButton = (Button) findViewById(R.id.start_activity_pcButton);
        PCButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, CharacterActivity.class);

                // Get device's character
                characterId = getIdFromPreferences();
                getCharacterFromDatabase(characterId);

                // Set up encounter
                CharacterDB db = new CharacterDB(getApplicationContext());
                db.open();
                List<Character> list = db.extractAllCharacter();
                Encounter.getUniqueInstance(getApplicationContext()).addCharacterList(list);
                Encounter.getUniqueInstance(getApplicationContext()).sortCharacters();
                db.close();

                intent.putExtra(CharacterActivity.EXTRA_CHARACTER_ID, characterId);
                startActivityForResult(intent, REQUEST_CHARACTER);
            }
        });

        DMButton = (Button) findViewById(R.id.start_activity_dmButton);
        DMButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, CharacterListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Character getCharacterFromDatabase(UUID id) {
        Character character;
        CharacterDB database = new CharacterDB(getApplicationContext());
        database.open();
        if(database.hasCharacter(id)) {
            character = database.extractCharacter(id);
        } else {
            character = new Character("name", 0, 0, 0, 0,
                    new Avatar(this, "skeleton"), this);
            character.setId(characterId);
            database.insertUpdateCharacter(character);
        }
        database.close();
        return character;
    }

    private UUID getIdFromPreferences() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        UUID id;
        if (preferences.contains(KEY_ID)) {
            id = UUID.fromString(preferences.getString(KEY_ID, null));
        } else {
            id = UUID.randomUUID();
            editor.putString(KEY_ID, id.toString());
            editor.commit();
        }
        return id;
    }
}
