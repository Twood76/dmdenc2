package com.ricky.encounterassistant.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ricky.encounterassistant.R;
import com.ricky.encounterassistant.models.Character;
import com.ricky.encounterassistant.models.*;

import java.util.UUID;

/**
 * Created by Ricky on 12/29/2014.
 */
public class CharacterEditActivity extends Activity {
    private static final String TAG = "characterEditActivity";

    public static final String EXTRA_NAME = "com.ricky.encounterassistant.name";
    public static final String EXTRA_HEALTH = "com.ricky.encounterassistant.health";
    public static final String EXTRA_MAX_HEALTH = "com.ricky.encounterassistant.maxhealth";
    public static final String EXTRA_INITIATIVE = "com.ricky.encounterassistant.initiative";
    public static final String EXTRA_AC = "com.ricky.encounterassistant.ac";

    public static final int RESULT_DELETE = 1;

    private EditText nameEditText;
    private EditText healthEditText;
    private EditText maxHealthEditText;
    private EditText initiativeEditText;
    private EditText acEditText;
    private Spinner avatarSpinner;
    private Button confirmButton;
    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_edit);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        UUID id = (UUID) getIntent().getSerializableExtra(CharacterActivity.EXTRA_CHARACTER_ID);
        final Character character = Encounter.getUniqueInstance(this).getCharacter(id);

        nameEditText = (EditText) findViewById(R.id.character_edit_nameEditText);
        nameEditText.setText(character.getName());
        nameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (nameEditText.getText().toString().equals("")) {
                    nameEditText.setText("name");
                }
                return false;
            }
        });


        maxHealthEditText = (EditText) findViewById(R.id.character_edit_maxHealthEditText);
        maxHealthEditText.setText(Integer.toString(character.getMaxHP()));
        maxHealthEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (maxHealthEditText.getText().toString().equals("")) {
                    maxHealthEditText.setText("0");
                }
                return false;
            }
        });

        healthEditText = (EditText) findViewById(R.id.character_edit_healthEditText);
        healthEditText.setText(Integer.toString(character.getHP()));
        healthEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (healthEditText.getText().toString().equals("") ||
                        Integer.parseInt(healthEditText.getText().toString()) < 0) {
                    healthEditText.setText("0");
                } else if (Integer.parseInt(healthEditText.getText().toString()) >
                        Integer.parseInt(maxHealthEditText.getText().toString())) {
                    healthEditText.setText(maxHealthEditText.getText());
                }
                return false;
            }
        });

        initiativeEditText = (EditText) findViewById(R.id.character_edit_initiativeEditText);
        initiativeEditText.setText(Integer.toString(character.getInit()));
        initiativeEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (initiativeEditText.getText().toString().equals("")) {
                    initiativeEditText.setText("0");
                }
                return false;
            }
        });

        acEditText = (EditText) findViewById(R.id.character_edit_acEditText);
        acEditText.setText(Integer.toString(character.getAC()));
        acEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (acEditText.getText().toString().equals("")) {
                    acEditText.setText("0");
                }
                return false;
            }
        });



        /**
         * Avatar Spinner used to select what image to display for the character.
         */
        avatarSpinner = (Spinner) findViewById(R.id.character_edit_avatarSpinner);
        AvatarAdapter avatarSpinnerAdapter = new AvatarAdapter(Avatar.avatarList);
        avatarSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        avatarSpinner.setAdapter(avatarSpinnerAdapter);
        avatarSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Avatar avatar = new Avatar(getApplicationContext(), Avatar.avatarList[i]);
                character.changeAvatar(avatar);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        confirmButton = (Button) findViewById(R.id.character_edit_confirmButton);
        deleteButton = (Button) findViewById(R.id.character_edit_deleteButton);
    } // End OnCreate

    @Override
    protected void onResume() {
        super.onResume();
        showKeyboard();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        showKeyboard();
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideKeyboard();

    }

    @Override
    protected void onStop() {
        super.onStop();
        hideKeyboard();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * On Back Button pressed, ignore values in text fields and return to Character Activity.
     */
    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    /**
     * On Enter Button pressed, values in text fields are synced with the character and return to Character Activity.
     * @param view
     */
    public void enterButton(View view) {
        if (nameEditText.getText().toString().equals("") || maxHealthEditText.getText().toString().equals("") ||
                initiativeEditText.getText().toString().equals("") || acEditText.getText().toString().equals("")) {
            Toast toast = Toast.makeText(this, "Fill Out All Fields", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_NAME, nameEditText.getText().toString());
        intent.putExtra(EXTRA_HEALTH, Integer.parseInt(healthEditText.getText().toString()));
        intent.putExtra(EXTRA_MAX_HEALTH, Integer.parseInt(maxHealthEditText.getText().toString()));
        intent.putExtra(EXTRA_INITIATIVE, Integer.parseInt(initiativeEditText.getText().toString()));
        intent.putExtra(EXTRA_AC, Integer.parseInt(acEditText.getText().toString()));

        setResult(RESULT_OK, intent);
        finish();
    }

    public void deleteButton(View view) {
        setResult(RESULT_FIRST_USER + RESULT_DELETE);
        finish();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.findViewById(android.R.id.content).getWindowToken(), 0);
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

    /**
     * Adapter Class to show images of each avatar in the spinner
     */
    private class AvatarAdapter extends ArrayAdapter<String> {
        private AvatarAdapter(String[] array) {
            super(getApplicationContext(), 0, array);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater()
                        .inflate(R.layout.list_item_avatar, null);
            }
            ImageView avatarImageView = (ImageView) convertView.findViewById(R.id.list_item_avatar_avatarImageView);
            Avatar avatar = new Avatar(getApplicationContext(), getItem(position));
            avatarImageView.setImageDrawable(avatar.toDrawable());

            TextView textView = (TextView) convertView.findViewById(R.id.list_item_avatar_nameTextView);
            textView.setText(getItem(position));

            return convertView;
        }
    }
}
