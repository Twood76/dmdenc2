package com.ricky.encounterassistant.models;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Created by Ricky on 10/12/2014.
 */
public class Encounter {
    private static final String TAG = "Encounter";
    private static Encounter uniqueInstance;

    private List<com.ricky.encounterassistant.models.Character> characters;
    private int selected;

    private Context appContext;

    public Encounter(Context c) {
        appContext = c;
        characters = new ArrayList<>();
        selected = 0;
    }

    public static Encounter getUniqueInstance(Context c) {
        if (uniqueInstance == null) {
            uniqueInstance = new Encounter(c.getApplicationContext());
        }
        return uniqueInstance;
    }

    public Character getCharacter(UUID id) {
        for(Character character : characters) {
            if(character.getId().equals(id))
                return character;
        }
        return null;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public void addCharacter(Character character) {
        removeCharacter(character.getId());
        characters.add(character);
    }

    public void addCharacterList(List<Character> characterList) {
        characters = characterList;
    }

    public boolean hasCharacter(UUID id) {
        for(Character character : characters) {
            if(character.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public void removeCharacter(UUID id) {
        for(Character character : characters) {
            if(character.getId() == id) {
                characters.remove(character);
                return;
            }
        }
    }

    public void removeCharacter(Character character) {
        characters.remove(character);
    }

    public void sortCharacters() {
        Character c = characters.get(selected);
        Collections.sort(characters, Collections.reverseOrder());
        selected = characters.indexOf(c);
    }

    public int getSelectedIndex() {
        return selected;
    }

    public void next() {
        selected = (selected + 1) % characters.size();
    }

    public void previous() {
        selected = (selected - 1) % characters.size();
    }

    public void reset() {
        selected = 0;
    }
}
