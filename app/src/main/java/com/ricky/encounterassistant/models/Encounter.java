package com.ricky.encounterassistant.models;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.UUID;

/**
 * Created by Ricky on 10/12/2014.
 */
public class Encounter {
    private static final String TAG = "Encounter";
    private static Encounter uniqueInstance;
    private ArrayList<com.ricky.encounterassistant.models.Character> characters;
    private Context appContext;

    public Encounter(Context c) {
        appContext = c;
        characters = new ArrayList<Character>();
    }

    public static Encounter getUniqueInstance(Context c) {
        if (uniqueInstance == null) {
            uniqueInstance = new Encounter(c.getApplicationContext());
        }
        return uniqueInstance;
    }

    public ArrayList<Character> getCharacters() {
        return characters;
    }

    public Character getCharacter(UUID id) {
        for(Character character : characters) {
            if(character.getId().equals(id))
                return character;
        }
        return null;
    }

    public void addCharacter(Character character) {
        Log.d(TAG, character.getId().toString());
        characters.add(character);
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
        Iterator<Character> itr = characters.iterator();
        while(itr.hasNext()) {
            Character character = itr.next();
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
        Collections.sort(characters, Collections.reverseOrder());
    }
}
