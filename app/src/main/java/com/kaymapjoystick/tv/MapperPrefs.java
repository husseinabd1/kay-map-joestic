package com.kaymapjoystick.tv;

import android.content.Context;
import android.content.SharedPreferences;

public final class MapperPrefs {
    private static final String PREFS_NAME = "joystick_mapper";
    private static final String KEY_PREFIX = "map_";

    private MapperPrefs() {
    }

    public enum MapperAction {
        DISABLED,
        BACK,
        HOME,
        RECENTS,
        PLAY_PAUSE,
        NEXT_TRACK,
        PREVIOUS_TRACK,
        VOLUME_UP,
        VOLUME_DOWN
    }

    public static MapperAction getActionForKey(Context context, int keyCode) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String actionName = prefs.getString(KEY_PREFIX + keyCode, MapperAction.DISABLED.name());
        try {
            return MapperAction.valueOf(actionName);
        } catch (IllegalArgumentException ignored) {
            return MapperAction.DISABLED;
        }
    }

    public static void setActionForKey(Context context, int keyCode, MapperAction action) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_PREFIX + keyCode, action.name())
                .apply();
    }

    public static void reset(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        for (int keyCode : ControllerKeys.SUPPORTED_KEYS) {
            editor.remove(KEY_PREFIX + keyCode);
        }
        editor.apply();
    }

    public static String getActionLabel(Context context, MapperAction action) {
        switch (action) {
            case BACK:
                return context.getString(R.string.action_back);
            case HOME:
                return context.getString(R.string.action_home);
            case RECENTS:
                return context.getString(R.string.action_recents);
            case PLAY_PAUSE:
                return context.getString(R.string.action_play_pause);
            case NEXT_TRACK:
                return context.getString(R.string.action_next_track);
            case PREVIOUS_TRACK:
                return context.getString(R.string.action_previous_track);
            case VOLUME_UP:
                return context.getString(R.string.action_volume_up);
            case VOLUME_DOWN:
                return context.getString(R.string.action_volume_down);
            case DISABLED:
            default:
                return context.getString(R.string.action_disabled);
        }
    }
}
