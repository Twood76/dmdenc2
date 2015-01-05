package com.ricky.encounterassistant.models;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.ricky.encounterassistant.R;

/**
 * Created by Ricky on 1/3/2015.
 */
public class Avatar {
    public static final String[] avatarList = {"skeleton", "zombie"};
    private String avatar;

    private Context context;

    public Avatar(Context context, String a) {
        this.context = context;

        for (String i : avatarList) {
            if (a.equals(i)) {
                this.avatar = a;
                return;
            }
        }
        throw new RuntimeException("String passed to Avatar Constructor not in Avatar List");
    }

    @Override
    public String toString() {
        return avatar;
    }

    public Drawable toDrawable() {
            switch (avatar) {
            case "skeleton":
                return context.getResources().getDrawable(R.drawable.skull);
            case "zombie":
                return context.getResources().getDrawable(R.drawable.zombo);
            default:
                return context.getResources().getDrawable(R.drawable.skull);
        }
    }
}
