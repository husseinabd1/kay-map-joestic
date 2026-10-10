package com.kaymapjoystick.tv;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.media.AudioManager;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

public class MapperAccessibilityService extends AccessibilityService {
    @Override
    protected void onServiceConnected() {
        AccessibilityServiceInfo info = getServiceInfo();
        if (info != null) {
            info.flags |= AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS;
            setServiceInfo(info);
        }
    }

    @Override
    public boolean onKeyEvent(KeyEvent event) {
        if (event == null || event.getAction() != KeyEvent.ACTION_DOWN) {
            return false;
        }

        int keyCode = event.getKeyCode();
        if (!ControllerKeys.isSupportedKey(keyCode)) {
            return false;
        }

        MapperPrefs.MapperAction action = MapperPrefs.getActionForKey(this, keyCode);
        if (action == MapperPrefs.MapperAction.DISABLED) {
            return false;
        }

        if (event.getRepeatCount() > 0 && !allowsRepeat(action)) {
            return true;
        }

        runAction(action);
        return true;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
    }

    @Override
    public void onInterrupt() {
    }

    private boolean allowsRepeat(MapperPrefs.MapperAction action) {
        return action == MapperPrefs.MapperAction.VOLUME_UP
                || action == MapperPrefs.MapperAction.VOLUME_DOWN;
    }

    private void runAction(MapperPrefs.MapperAction action) {
        switch (action) {
            case BACK:
                performGlobalAction(GLOBAL_ACTION_BACK);
                break;
            case HOME:
                performGlobalAction(GLOBAL_ACTION_HOME);
                break;
            case RECENTS:
                performGlobalAction(GLOBAL_ACTION_RECENTS);
                break;
            case PLAY_PAUSE:
                sendMediaKey(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
                break;
            case NEXT_TRACK:
                sendMediaKey(KeyEvent.KEYCODE_MEDIA_NEXT);
                break;
            case PREVIOUS_TRACK:
                sendMediaKey(KeyEvent.KEYCODE_MEDIA_PREVIOUS);
                break;
            case VOLUME_UP:
                adjustVolume(AudioManager.ADJUST_RAISE);
                break;
            case VOLUME_DOWN:
                adjustVolume(AudioManager.ADJUST_LOWER);
                break;
            case DISABLED:
            default:
                break;
        }
    }

    private void sendMediaKey(int keyCode) {
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioManager == null) {
            return;
        }

        long now = SystemClock.uptimeMillis();
        KeyEvent down = new KeyEvent(now, now, KeyEvent.ACTION_DOWN, keyCode, 0);
        KeyEvent up = new KeyEvent(now, now, KeyEvent.ACTION_UP, keyCode, 0);
        audioManager.dispatchMediaKeyEvent(down);
        audioManager.dispatchMediaKeyEvent(up);
    }

    private void adjustVolume(int direction) {
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioManager != null) {
            audioManager.adjustStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    direction,
                    AudioManager.FLAG_SHOW_UI
            );
        }
    }
}
