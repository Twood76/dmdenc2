package com.ricky.encounterassistant.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ricky.encounterassistant.*;
import com.ricky.encounterassistant.databases.CharacterDB;
import com.ricky.encounterassistant.models.Character;
import com.ricky.encounterassistant.models.Encounter;
import com.ricky.encounterassistant.ui.dialogs.NewCharacterDialog;
import com.ricky.encounterassistant.ui.activities.CharacterActivity;

import java.util.List;

/**
 * Created by Ricky on 10/11/2014.
 */
public class CharacterListFragment extends ListFragment {
    private static final int REQUEST_CHARACTER = 1;
    private static final String DIALOG_NEW_CHARACTER = "new_character";

    private Button nextButton;

    private Encounter encounter;
    private List<Character> characters;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        getActivity().setTitle(R.string.encounter_title);
        encounter = Encounter.getUniqueInstance(getActivity());
        characters = encounter.getCharacters();

        final CharacterAdapter adapter = new CharacterAdapter(characters);
        setListAdapter(adapter);

        setRetainInstance(true);

        /**
         * Next button clicked, next character is highlighted in the list.
         */
        nextButton = (Button) getActivity().findViewById(R.id.encounter_nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                encounter.next();
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        characters = Encounter.getUniqueInstance(getActivity()).getCharacters();
        ((CharacterAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == REQUEST_CHARACTER) {
            Character character = (Character) data.getSerializableExtra(NewCharacterDialog.EXTRA_CHARACTER);
            /**
             * Send new character to database
              */
            CharacterDB db = new CharacterDB(getActivity().getApplicationContext());
            db.open();
            db.insertUpdateCharacter(character);
            db.close();

            /**
             * Send new character to encounter list
             */
            Encounter.getUniqueInstance(getActivity()).addCharacter(character);
            Encounter.getUniqueInstance(getActivity()).sortCharacters();
            characters = Encounter.getUniqueInstance(getActivity()).getCharacters();
            ((CharacterAdapter)getListAdapter()).notifyDataSetChanged();
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Character character = ((CharacterAdapter)getListAdapter()).getItem(position);

        Intent intent = new Intent(getActivity(), CharacterActivity.class);
        intent.putExtra(CharacterActivity.EXTRA_CHARACTER_ID, character.getId());
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_character_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                FragmentManager fm = getActivity()
                        .getSupportFragmentManager();
                NewCharacterDialog dialog = new NewCharacterDialog();
                dialog.setTargetFragment(CharacterListFragment.this, REQUEST_CHARACTER);
                dialog.show(fm, DIALOG_NEW_CHARACTER);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Character Adapter Class to adapt a list of characters to a list view
     */
    private class CharacterAdapter extends ArrayAdapter<Character> {
        private CharacterAdapter(List<Character> characters) {
            super(getActivity(), 0, characters);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_character, null);
            }

            Character character = getItem(position);
            int selectedIndex = encounter.getSelectedIndex();

            /**
             * Indicate the selected item on the list
             */
            TextView nameTextView = (TextView) convertView.findViewById(R.id.list_item_character_nameTextView);
            nameTextView.setText(character.getName());
            if (position == selectedIndex) {
                nameTextView.setTextColor(getResources().getColor(R.color.selected));
            } else {
                nameTextView.setTextColor(getResources().getColor(R.color.unselected));
            }

            TextView hpTextView = (TextView) convertView.findViewById(R.id.list_item_character_hpTextView);
            hpTextView.setText(character.getHP() + "/" + character.getMaxHP());

            if(character.getHP() <= 0) {
                hpTextView.setTextColor(getResources().getColor(R.color.dead));
            } else if ((character.getHP() <= (character.getMaxHP()/2))) {
                hpTextView.setTextColor(getResources().getColor(R.color.bloody));
            } else {
                hpTextView.setTextColor(getResources().getColor(R.color.healthy));
            }

            TextView initiativeTextView = (TextView) convertView.findViewById(R.id.list_item_character_initiativeTextView);
            initiativeTextView.setText("Init: " + character.getInit());

            TextView ACTextView = (TextView) convertView.findViewById(R.id.list_item_character_acTextView);
            ACTextView.setText("AC: " + character.getAC());

            ImageView avatarImageView = (ImageView) convertView.findViewById(R.id.list_item_avatar_avatarImageView);
            avatarImageView.setImageDrawable(character.getAvatarDrawable());

            return convertView;
        }
    }
}
