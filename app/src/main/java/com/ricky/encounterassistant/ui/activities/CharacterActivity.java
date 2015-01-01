package com.ricky.encounterassistant.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.ricky.encounterassistant.*;
import com.ricky.encounterassistant.models.*;

import java.util.UUID;


public class CharacterActivity extends Activity {
    private static final String TAG = "CharacterActivity";
    public static final String EXTRA_CHARACTER_ID = "com.riyu.encounterassistant.character_id";
    private static final int REQUEST_CHARACTER_EDIT = 1;

    com.ricky.encounterassistant.models.Character character;

    private TextView nameTextView;
    private TextView initiativeTextView;
    private TextView hpTextView;
    private TextView acTextView;
    private ImageView avatarImageView;
    private TextView equipmentTitleTextView;
    private TextView equipmentShownTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);

        Intent intent = getIntent();
        UUID characterId = (UUID) intent.getSerializableExtra(EXTRA_CHARACTER_ID);

        character = Encounter.getUniqueInstance(this).getCharacter(characterId);

        nameTextView = (TextView) findViewById(R.id.character_nameTextView);
        nameTextView.setText(character.getName());

        initiativeTextView = (TextView) findViewById(R.id.character_initiativeTextView);
        initiativeTextView.setText("Initiative: " + character.getInit());

        hpTextView = (TextView) findViewById(R.id.character_hp);
        hpTextView.setText(character.getHP() + "/" + character.getMaxHP());
        if (character.getMaxHP() == 0 || (character.getHP() < (character.getHP()/character.getMaxHP()))) {
            hpTextView.setTextColor(Color.parseColor("#A80000"));
        } else {
            hpTextView.setTextColor(Color.parseColor("#59B31D"));
        }

        acTextView = (TextView) findViewById(R.id.character_acTextView);
        acTextView.setText("Armor Class: " + character.getAC());

        avatarImageView =(ImageView) findViewById(R.id.character_avatarImageView);
        avatarImageView.setImageDrawable(character.getAvatar());

        equipmentTitleTextView = (TextView) findViewById(R.id.character_equipmentTitleTextView);

        equipmentShownTextView = (TextView) findViewById(R.id.character_equipmentShownTextView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.character, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_edit) {
            Intent intent = new Intent(CharacterActivity.this, CharacterEditActivity.class);
            intent.putExtra(EXTRA_CHARACTER_ID, getIntent().getSerializableExtra(EXTRA_CHARACTER_ID));
            startActivityForResult(intent, REQUEST_CHARACTER_EDIT);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*
         * When returning from Character Edit, change details of the character
         */
        if(resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CHARACTER_EDIT) {
                character.setName(data.getStringExtra(CharacterEditActivity.EXTRA_NAME));
                character.setAC(data.getIntExtra(CharacterEditActivity.EXTRA_AC, 0));
                character.setMaxHP(data.getIntExtra(CharacterEditActivity.EXTRA_MAX_HEALTH, 0));
                character.setInit(data.getIntExtra(CharacterEditActivity.EXTRA_INITIATIVE, 0));
                updateCharacterInfo();
                Encounter.getUniqueInstance(this).sortCharacters();
                return;
            }
        } if (resultCode == RESULT_CANCELED) {
            Log.d(TAG, "cancelled");
        }
    }

    public void updateCharacterInfo() {
        nameTextView.setText(character.getName());
        initiativeTextView.setText("Initiative: " + character.getInit());
        hpTextView.setText(character.getHP() + "/" + character.getMaxHP());
        if (character.getMaxHP() == 0 || (character.getHP() < (character.getHP()/character.getMaxHP()))) {
            hpTextView.setTextColor(Color.parseColor("#A80000"));
        } else {
            hpTextView.setTextColor(Color.parseColor("#59B31D"));
        }
        acTextView.setText("Armor Class: " + character.getAC());
        avatarImageView.setImageDrawable(character.getAvatar());
    }
}
