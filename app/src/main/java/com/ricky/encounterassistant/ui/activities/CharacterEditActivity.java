package com.ricky.encounterassistant.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ricky.encounterassistant.R;
import com.ricky.encounterassistant.models.*;

import java.util.UUID;

/**
 * Created by Ricky on 12/29/2014.
 */
public class CharacterEditActivity extends Activity {
    public static final String EXTRA_NAME = "com.riyu.encounterassistant.name";
    public static final String EXTRA_MAX_HEALTH = "com.riyu.encounterassistant.maxhealth";
    public static final String EXTRA_INITIATIVE = "com.riyu.encounterassistant.initiative";
    public static final String EXTRA_AC = "com.riyu.encounterassistant.ac";

    private EditText nameEditText;
    private EditText maxHealthEditText;
    private EditText initiativeEditText;
    private EditText acEditText;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_edit);

        UUID id = (UUID) getIntent().getSerializableExtra(CharacterActivity.EXTRA_CHARACTER_ID);
        com.ricky.encounterassistant.models.Character character = Encounter.getUniqueInstance(this).getCharacter(id);

        nameEditText = (EditText) findViewById(R.id.character_edit_nameEditText);
        nameEditText.setText(character.getName());

        maxHealthEditText = (EditText) findViewById(R.id.character_edit_maxHealthEditText);
        maxHealthEditText.setText(Integer.toString(character.getMaxHP()));

        initiativeEditText = (EditText) findViewById(R.id.character_edit_initiativeEditText);
        initiativeEditText.setText(Integer.toString(character.getInit()));

        acEditText = (EditText) findViewById(R.id.character_edit_acEditText);
        acEditText.setText(Integer.toString(character.getAC()));

        confirmButton = (Button) findViewById(R.id.character_edit_confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_NAME, nameEditText.getText().toString());
                intent.putExtra(EXTRA_MAX_HEALTH, Integer.parseInt(maxHealthEditText.getText().toString()));
                intent.putExtra(EXTRA_INITIATIVE, Integer.parseInt(initiativeEditText.getText().toString()));
                intent.putExtra(EXTRA_AC, Integer.parseInt(acEditText.getText().toString()));

                setResult(RESULT_OK, intent);
                finish();
                return;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}
