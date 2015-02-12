package com.ricky.encounterassistant.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ricky.encounterassistant.*;
import com.ricky.encounterassistant.databases.CharacterDB;
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
    private Button hpMinusButton;
    private Button hpPlusButton;
    private Button hpMinus10Button;
    private Button hpPlus10Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);

        Intent intent = getIntent();
        UUID characterId = (UUID) intent.getSerializableExtra(EXTRA_CHARACTER_ID);

        character = Encounter.getUniqueInstance(CharacterActivity.this).getCharacter(characterId);

        Log.d(TAG, characterId.toString());

        nameTextView = (TextView) findViewById(R.id.character_nameTextView);
        initiativeTextView = (TextView) findViewById(R.id.character_initiativeTextView);
        hpTextView = (TextView) findViewById(R.id.character_hp);
        acTextView = (TextView) findViewById(R.id.character_acTextView);
        avatarImageView =(ImageView) findViewById(R.id.character_avatarImageView);
        updateCharacterInfo();

        equipmentTitleTextView = (TextView) findViewById(R.id.character_equipmentTitleTextView);

        equipmentShownTextView = (TextView) findViewById(R.id.character_equipmentShownTextView);

        hpMinusButton = (Button) findViewById(R.id.character_hpMinusButton);
        hpMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                character.decreaseHP(1);
                updateCharacterInfo();
                syncCharacterToDatabase();
            }
        });

        hpPlusButton = (Button) findViewById(R.id.character_hpPlusButton);
        hpPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                character.increaseHP(1);
                updateCharacterInfo();
                syncCharacterToDatabase();
            }
        });

        hpMinus10Button = (Button) findViewById(R.id.character_hpMinus10Button);
        hpMinus10Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                character.decreaseHP(10);
                updateCharacterInfo();
                syncCharacterToDatabase();
            }
        });

        hpPlus10Button = (Button) findViewById(R.id.character_hpPlus10Button);
        hpPlus10Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                character.increaseHP(10);
                updateCharacterInfo();
                syncCharacterToDatabase();
            }
        });
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
        } if (id == R.id.action_delete) {
            deleteCharacter();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*
         * When returning from Character Edit, change details of the character if the
         * result code is ok, do nothing if it is cancelled, and delete if result code was
         * delete
         */
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CHARACTER_EDIT) {
                character.setName(data.getStringExtra(CharacterEditActivity.EXTRA_NAME));
                character.setAC(data.getIntExtra(CharacterEditActivity.EXTRA_AC, 0));
                character.setMaxHP(data.getIntExtra(CharacterEditActivity.EXTRA_MAX_HEALTH, 0));
                int hp = data.getIntExtra(CharacterEditActivity.EXTRA_HEALTH, 0);
                if(hp > character.getMaxHP()) {
                    hp = character.getMaxHP();
                } else if(hp < 0) {
                    hp = 0;
                }
                character.setHP(hp);
                character.setInit(data.getIntExtra(CharacterEditActivity.EXTRA_INITIATIVE, 0));
                updateCharacterInfo();
                syncCharacterToDatabase();
                Encounter.getUniqueInstance(this).sortCharacters();
                return;
            }
        } if (resultCode == RESULT_FIRST_USER + CharacterEditActivity.RESULT_DELETE) {
            deleteCharacter();
            finish();
        }

    }

    private void updateCharacterInfo() {
        nameTextView.setText(character.getName());
        initiativeTextView.setText("Initiative: " + character.getInit());
        hpTextView.setText(character.getHP() + "/" + character.getMaxHP());
        if(character.getHP() <= 0) {
            hpTextView.setTextColor(getResources().getColor(R.color.dead));
        } else if ((character.getHP() <= (character.getMaxHP()/2))) {
            hpTextView.setTextColor(getResources().getColor(R.color.bloody));
        } else {
            hpTextView.setTextColor(getResources().getColor(R.color.healthy));
        }
        acTextView.setText("Armor Class: " + character.getAC());
        avatarImageView.setImageDrawable(character.getAvatarDrawable());
    }

    private void syncCharacterToDatabase() {
        CharacterDB database = new CharacterDB(getApplicationContext());
        database.open();
        database.insertUpdateCharacter(character);
        database.close();
    }

    public void deleteCharacter() {
        Encounter.getUniqueInstance(getApplicationContext()).removeCharacter(character.getId());
        CharacterDB db = new CharacterDB(getApplicationContext());
        db.open();
        db.removeCharacter(character.getId());
        db.close();
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(StartActivity.KEY_ID);
        editor.commit();
    }
}
