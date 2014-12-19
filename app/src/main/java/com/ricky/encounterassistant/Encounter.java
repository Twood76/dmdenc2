package com.ricky.encounterassistant;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

/**
 * Created by Ricky on 10/12/2014.
 */
public class Encounter {
    private static Encounter uniqueInstance;
    private ArrayList<Character> characters;
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
        characters.add(character);
    }

    public void sortCharacters() {
        Collections.sort(characters);
    }
}
