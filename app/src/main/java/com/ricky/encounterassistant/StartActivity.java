package com.ricky.encounterassistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.UUID;

/*
 * This activity is the initial activity that loads when the application first starts
 * If Player Character button is pressed, go to character screen
 * If Dungeon Master button is pressed, go to dungeon master screen
 */
public class StartActivity extends Activity {
    Button PCButton;
    Button DMButton;
    public static UUID characterID;
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
                if(characterID == null) {
                    Character character = new Character("name", 0, 0, 0, 0, Character.Avatar.SKELETON, StartActivity.this);
                    characterID = character.getId();
                    Encounter.getUniqueInstance(StartActivity.this).addCharacter(character);
                }
                intent.putExtra(CharacterActivity.EXTRA_CHARACTER_ID, characterID);
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
}
