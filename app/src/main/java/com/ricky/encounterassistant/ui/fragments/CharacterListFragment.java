package com.ricky.encounterassistant.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ricky.encounterassistant.*;
import com.ricky.encounterassistant.models.Character;
import com.ricky.encounterassistant.models.Encounter;
import com.ricky.encounterassistant.ui.dialogs.NewCharacterDialog;
import com.ricky.encounterassistant.ui.activities.CharacterActivity;

import java.util.ArrayList;

/**
 * Created by Ricky on 10/11/2014.
 */
public class CharacterListFragment extends ListFragment {
    private static final int REQUEST_CHARACTER = 1;
    private static final String DIALOG_NEW_CHARACTER = "new_character";

    private Encounter encounter;
    private ArrayList<Character> characters;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        getActivity().setTitle(R.string.encounter_title);
        encounter = Encounter.getUniqueInstance(getActivity());
        characters = encounter.getCharacters();

        CharacterAdapter adapter = new CharacterAdapter(characters);
        setListAdapter(adapter);

        setRetainInstance(true);
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

    private class CharacterAdapter extends ArrayAdapter<Character> {
        private CharacterAdapter(ArrayList<Character> characters) {
            super(getActivity(), 0, characters);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_character, null);
            }

            Character character = getItem(position);

            TextView nameTextView = (TextView) convertView.findViewById(R.id.list_item_character_nameTextView);
            nameTextView.setText(character.getName());

            TextView HPTextView = (TextView) convertView.findViewById(R.id.list_item_character_hpTextView);
            HPTextView.setText(character.getHP() + "/" + character.getMaxHP());

            if (character.getMaxHP() == 0 || (character.getHP() < (character.getHP()/character.getMaxHP()))) {
                HPTextView.setTextColor(Color.parseColor("#A80000"));
            } else {
                HPTextView.setTextColor(Color.parseColor("#59B31D"));
            }

            TextView initiativeTextView = (TextView) convertView.findViewById(R.id.list_item_character_initiativeTextView);
            initiativeTextView.setText("Init: " + character.getInit());

            TextView ACTextView = (TextView) convertView.findViewById(R.id.list_item_character_acTextView);
            ACTextView.setText("AC: " + character.getAC());

            ImageView avatarImageView = (ImageView) convertView.findViewById(R.id.list_item_character_avatarImageView);
            avatarImageView.setImageDrawable(character.getAvatarDrawable());

            return convertView;
        }
    }
}
