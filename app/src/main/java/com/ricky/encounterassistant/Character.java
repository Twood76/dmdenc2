package com.ricky.encounterassistant;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Ricky on 10/12/2014.
 */
public class Character implements Serializable, Comparable<Character> {
    private UUID id;
    private String name;
    private int AC, FO, RE, WI, HP, maxHP, init;
    private boolean downed;
    private Context context;
    public enum Avatar {
        SKELETON, ZOMBIE
    }
    private Avatar avatar;

    public Character(String name, int AC, int HP, int maxHP, int init, Avatar avatar,
                     Context context) {

        this.id = UUID.randomUUID();
        this.name = name;
        this.AC = AC;
        this.HP = HP;
        this.maxHP = maxHP;
        this.init = init;
        this.avatar = avatar;
        this.context = context;
        this.downed = false;
    }

    public void decreaseHP(int amount) {
        HP -= amount;


        if(HP <= 0) {
            downed = true;
        }
    }

    public void increaseHP(int amount) {
        HP += amount;
        if(HP > maxHP) {
            HP = maxHP;
        }

        if(downed == true &&
                HP > 0) {
            downed = false;
        }
    }

    public void changeAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public Drawable getAvatar() {
        switch (avatar) {
            case SKELETON:
                return context.getResources().getDrawable(R.drawable.skull);
            case ZOMBIE:
                return context.getResources().getDrawable(R.drawable.zombo);
            default:
                return context.getResources().getDrawable(R.drawable.skull);
        }
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public int getAC() {
        return AC;
    }

    public int getHP() {
        return HP;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public int getInit() {
        return init;
    }

    @Override
    public int compareTo(Character c) {
        int cinit = c.getInit();
        if (this.init == cinit)
            return 0;
        else if (this.init > cinit)
            return 1;
        else
            return -1;
    }
}
