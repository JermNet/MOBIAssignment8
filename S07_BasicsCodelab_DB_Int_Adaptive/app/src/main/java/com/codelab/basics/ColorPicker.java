package com.codelab.basics;

import android.content.Context;
import android.util.Log;

import androidx.core.content.ContextCompat;

// A class to pick a color based on the type of Pokemon, as well as an inverted color so there's something nice to look on top of the first color
public class ColorPicker {

    private Context context;

    public ColorPicker(Context context) {
        this.context = context;
    }

    public int getColor(String type, int legendary) {
        Log.v("ColorPicker", "A normal color is being picked!");
        if (legendary == 1) {
            return ContextCompat.getColor(context, R.color.LEGENDARY);
        }
        // The best way to do this would probably be to have a class for each type (to keep with the open closed principle but for something I did as bonus, I don't think that's necessary (Pokemon hasn't added a new type in over a decade anyway)
        if (type.equalsIgnoreCase("Normal")) {
            return ContextCompat.getColor(context, R.color.NORMAL);
        } else if (type.equalsIgnoreCase("Fire")) {
            return ContextCompat.getColor(context, R.color.FIRE);
        } else if (type.equalsIgnoreCase("Water")) {
            return ContextCompat.getColor(context, R.color.WATER);
        } else if (type.equalsIgnoreCase("Grass")) {
            return ContextCompat.getColor(context, R.color.GRASS);
        } else if (type.equalsIgnoreCase("Electric")) {
            return ContextCompat.getColor(context, R.color.ELECTRIC);
        } else if (type.equalsIgnoreCase("Psychic")) {
            return ContextCompat.getColor(context, R.color.PSYCHIC);
        } else if (type.equalsIgnoreCase("Ice")) {
            return ContextCompat.getColor(context, R.color.ICE);
        } else if (type.equalsIgnoreCase("Dragon")) {
            return ContextCompat.getColor(context, R.color.DRAGON);
        } else if (type.equalsIgnoreCase("Dark")) {
            return ContextCompat.getColor(context, R.color.DARK);
        } else if (type.equalsIgnoreCase("Fairy")) {
            return ContextCompat.getColor(context, R.color.FAIRY);
        } else if (type.equalsIgnoreCase("Fighting")) {
            return ContextCompat.getColor(context, R.color.FIGHTING);
        } else if (type.equalsIgnoreCase("Flying")) {
            return ContextCompat.getColor(context, R.color.FLYING);
        } else if (type.equalsIgnoreCase("Poison")) {
            return ContextCompat.getColor(context, R.color.POISON);
        } else if (type.equalsIgnoreCase("Ground")) {
            return ContextCompat.getColor(context, R.color.GROUND);
        } else if (type.equalsIgnoreCase("Rock")) {
            return ContextCompat.getColor(context, R.color.ROCK);
        } else if (type.equalsIgnoreCase("Bug")) {
            return ContextCompat.getColor(context, R.color.BUG);
        } else if (type.equalsIgnoreCase("Ghost")) {
            return ContextCompat.getColor(context, R.color.GHOST);
        } else if (type.equalsIgnoreCase("Steel")) {
            return ContextCompat.getColor(context, R.color.STEEL);
        }
        return ContextCompat.getColor(context, R.color.NONE);
    }

    public int getOppositeColor(String type, int legendary) {
        Log.v("ColorPicker", "An opposite color is being picked!");
        if (legendary == 1) {
            return ContextCompat.getColor(context, R.color.EVIL_LEGENDARY);
        }

        if (type.equalsIgnoreCase("Normal")) {
            return ContextCompat.getColor(context, R.color.EVIL_NORMAL);
        } else if (type.equalsIgnoreCase("Fire")) {
            return ContextCompat.getColor(context, R.color.EVIL_FIRE);
        } else if (type.equalsIgnoreCase("Water")) {
            return ContextCompat.getColor(context, R.color.EVIL_WATER);
        } else if (type.equalsIgnoreCase("Grass")) {
            return ContextCompat.getColor(context, R.color.EVIL_GRASS);
        } else if (type.equalsIgnoreCase("Electric")) {
            return ContextCompat.getColor(context, R.color.EVIL_ELECTRIC);
        } else if (type.equalsIgnoreCase("Psychic")) {
            return ContextCompat.getColor(context, R.color.EVIL_PSYCHIC);
        } else if (type.equalsIgnoreCase("Ice")) {
            return ContextCompat.getColor(context, R.color.EVIL_ICE);
        } else if (type.equalsIgnoreCase("Dragon")) {
            return ContextCompat.getColor(context, R.color.EVIL_DRAGON);
        } else if (type.equalsIgnoreCase("Dark")) {
            return ContextCompat.getColor(context, R.color.EVIL_DARK);
        } else if (type.equalsIgnoreCase("Fairy")) {
            return ContextCompat.getColor(context, R.color.EVIL_FAIRY);
        } else if (type.equalsIgnoreCase("Fighting")) {
            return ContextCompat.getColor(context, R.color.EVIL_FIGHTING);
        } else if (type.equalsIgnoreCase("Flying")) {
            return ContextCompat.getColor(context, R.color.EVIL_FLYING);
        } else if (type.equalsIgnoreCase("Poison")) {
            return ContextCompat.getColor(context, R.color.EVIL_POISON);
        } else if (type.equalsIgnoreCase("Ground")) {
            return ContextCompat.getColor(context, R.color.EVIL_GROUND);
        } else if (type.equalsIgnoreCase("Rock")) {
            return ContextCompat.getColor(context, R.color.EVIL_ROCK);
        } else if (type.equalsIgnoreCase("Bug")) {
            return ContextCompat.getColor(context, R.color.EVIL_BUG);
        } else if (type.equalsIgnoreCase("Ghost")) {
            return ContextCompat.getColor(context, R.color.EVIL_GHOST);
        } else if (type.equalsIgnoreCase("Steel")) {
            return ContextCompat.getColor(context, R.color.EVIL_STEEL);
        }
        return ContextCompat.getColor(context, R.color.NONE);
    }
}
