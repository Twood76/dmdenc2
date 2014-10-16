package com.ricky.encounterassistant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.UUID;


public class CharacterActivity extends Activity {
    private static final String TAG = "CharacterActivity";
    public static final String EXTRA_CHARACTER_ID = "com.riyu.encounterassistant.character_id";

    Character character;

    private TextView nameTextView;
    private TextView initiativeTextView;
    private TextView hpTextView;
    private TextView acTextView;
    private TextView foTextView;
    private TextView reTextView;
    private TextView wiTextView;
    private ImageView avatarImageView;

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
        if ((character.getHP() < (character.getHP()/character.getMaxHP()))) {
            hpTextView.setTextColor(Color.parseColor("#A80000"));
        } else {
            hpTextView.setTextColor(Color.parseColor("#59B31D"));
        }

        acTextView = (TextView) findViewById(R.id.character_ac);
        acTextView.setText("Armor Class: " + character.getAC());

        foTextView = (TextView) findViewById(R.id.character_fo);
        foTextView.setText("Fortitude: " + character.getFO());

        reTextView = (TextView) findViewById(R.id.character_re);
        reTextView.setText("Reflex: " + character.getRE());

        wiTextView = (TextView) findViewById(R.id.character_wi);
        wiTextView.setText("Will: " + character.getWI());

        avatarImageView =(ImageView) findViewById(R.id.character_avatarImageView);
        avatarImageView.setImageDrawable(character.getAvatar());

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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
