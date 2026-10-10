package com.kaymapjoystick.tv;

import android.content.Context;
import android.view.KeyEvent;

public final class ControllerKeys {
    public static final int[] SUPPORTED_KEYS = {
            KeyEvent.KEYCODE_BUTTON_A,
            KeyEvent.KEYCODE_BUTTON_B,
            KeyEvent.KEYCODE_BUTTON_X,
            KeyEvent.KEYCODE_BUTTON_Y,
            KeyEvent.KEYCODE_BUTTON_L1,
            KeyEvent.KEYCODE_BUTTON_R1,
            KeyEvent.KEYCODE_BUTTON_L2,
            KeyEvent.KEYCODE_BUTTON_R2,
            KeyEvent.KEYCODE_BUTTON_START,
            KeyEvent.KEYCODE_BUTTON_SELECT,
            KeyEvent.KEYCODE_BUTTON_THUMBL,
            KeyEvent.KEYCODE_BUTTON_THUMBR,
            KeyEvent.KEYCODE_BUTTON_MODE
    };

    private ControllerKeys() {
    }

    public static boolean isSupportedKey(int keyCode) {
        for (int supportedKey : SUPPORTED_KEYS) {
            if (supportedKey == keyCode) {
                return true;
            }
        }
        return false;
    }

    public static String getDisplayName(Context context, int keyCode) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BUTTON_A:
                return context.getString(R.string.key_cross);
            case KeyEvent.KEYCODE_BUTTON_B:
                return context.getString(R.string.key_circle);
            case KeyEvent.KEYCODE_BUTTON_X:
                return context.getString(R.string.key_square);
            case KeyEvent.KEYCODE_BUTTON_Y:
                return context.getString(R.string.key_triangle);
            case KeyEvent.KEYCODE_BUTTON_L1:
                return context.getString(R.string.key_l1);
            case KeyEvent.KEYCODE_BUTTON_R1:
                return context.getString(R.string.key_r1);
            case KeyEvent.KEYCODE_BUTTON_L2:
                return context.getString(R.string.key_l2);
            case KeyEvent.KEYCODE_BUTTON_R2:
                return context.getString(R.string.key_r2);
            case KeyEvent.KEYCODE_BUTTON_START:
                return context.getString(R.string.key_options);
            case KeyEvent.KEYCODE_BUTTON_SELECT:
                return context.getString(R.string.key_share);
            case KeyEvent.KEYCODE_BUTTON_THUMBL:
                return context.getString(R.string.key_l3);
            case KeyEvent.KEYCODE_BUTTON_THUMBR:
                return context.getString(R.string.key_r3);
            case KeyEvent.KEYCODE_BUTTON_MODE:
                return context.getString(R.string.key_ps);
            default:
                return context.getString(R.string.key_unknown);
        }
    }
}
