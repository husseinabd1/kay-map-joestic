package com.kaymapjoystick.tv;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final int COLOR_BACKGROUND = Color.rgb(16, 20, 24);
    private static final int COLOR_PANEL = Color.rgb(31, 38, 46);
    private static final int COLOR_TEXT = Color.rgb(244, 247, 250);
    private static final int COLOR_MUTED = Color.rgb(174, 186, 198);
    private static final int COLOR_ON = Color.rgb(74, 222, 128);
    private static final int COLOR_OFF = Color.rgb(251, 146, 60);

    private TextView statusText;
    private TextView lastKeyText;
    private LinearLayout mappingsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(createContentView());
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateAccessibilityStatus();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event != null
                && event.getAction() == KeyEvent.ACTION_DOWN
                && ControllerKeys.isSupportedKey(event.getKeyCode())) {
            lastKeyText.setText(getString(
                    R.string.last_key_format,
                    ControllerKeys.getDisplayName(this, event.getKeyCode()),
                    event.getKeyCode()
            ));
        }
        return super.dispatchKeyEvent(event);
    }

    private View createContentView() {
        ScrollView scrollView = new ScrollView(this);
        scrollView.setFillViewport(false);
        scrollView.setBackgroundColor(COLOR_BACKGROUND);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(28), dp(24), dp(28), dp(28));
        scrollView.addView(root, new ScrollView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        TextView title = createText(getString(R.string.title), 30, COLOR_TEXT, true);
        root.addView(title);

        TextView subtitle = createText(getString(R.string.subtitle), 17, COLOR_MUTED, false);
        subtitle.setPadding(0, dp(6), 0, dp(18));
        root.addView(subtitle);

        LinearLayout statusPanel = createPanel();
        statusText = createText("", 18, COLOR_OFF, true);
        statusPanel.addView(statusText);

        Button accessibilityButton = createButton(getString(R.string.open_accessibility_settings));
        accessibilityButton.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)));
        statusPanel.addView(accessibilityButton, matchWrapParamsWithTopMargin(14));
        root.addView(statusPanel, matchWrapParamsWithBottomMargin(18));

        LinearLayout testPanel = createPanel();
        testPanel.addView(createText(getString(R.string.test_title), 22, COLOR_TEXT, true));
        lastKeyText = createText(getString(R.string.last_key_empty), 16, COLOR_MUTED, false);
        lastKeyText.setPadding(0, dp(10), 0, 0);
        testPanel.addView(lastKeyText);
        root.addView(testPanel, matchWrapParamsWithBottomMargin(18));

        LinearLayout mappingPanel = createPanel();
        mappingPanel.addView(createText(getString(R.string.mapping_title), 22, COLOR_TEXT, true));

        TextView note = createText(getString(R.string.mapping_note), 14, COLOR_MUTED, false);
        note.setPadding(0, dp(8), 0, dp(12));
        mappingPanel.addView(note);

        mappingsContainer = new LinearLayout(this);
        mappingsContainer.setOrientation(LinearLayout.VERTICAL);
        mappingPanel.addView(mappingsContainer);

        Button resetButton = createButton(getString(R.string.reset_mappings));
        resetButton.setOnClickListener(v -> {
            MapperPrefs.reset(this);
            rebuildMappings();
        });
        mappingPanel.addView(resetButton, matchWrapParamsWithTopMargin(16));

        root.addView(mappingPanel);

        rebuildMappings();
        updateAccessibilityStatus();
        return scrollView;
    }

    private void rebuildMappings() {
        mappingsContainer.removeAllViews();
        for (int keyCode : ControllerKeys.SUPPORTED_KEYS) {
            mappingsContainer.addView(createMappingRow(keyCode), matchWrapParamsWithBottomMargin(10));
        }
    }

    private View createMappingRow(int keyCode) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(0, dp(4), 0, dp(4));

        TextView keyLabel = createText(ControllerKeys.getDisplayName(this, keyCode), 17, COLOR_TEXT, false);
        row.addView(keyLabel, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        Spinner spinner = new Spinner(this);
        MapperPrefs.MapperAction[] actions = MapperPrefs.MapperAction.values();
        ArrayAdapter<MapperPrefs.MapperAction> adapter = new ActionAdapter(actions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(MapperPrefs.getActionForKey(this, keyCode).ordinal());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MapperPrefs.setActionForKey(MainActivity.this, keyCode, actions[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        row.addView(spinner, new LinearLayout.LayoutParams(dp(260), ViewGroup.LayoutParams.WRAP_CONTENT));
        return row;
    }

    private LinearLayout createPanel() {
        LinearLayout panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setPadding(dp(18), dp(16), dp(18), dp(16));
        GradientDrawable background = new GradientDrawable();
        background.setColor(COLOR_PANEL);
        background.setCornerRadius(dp(8));
        panel.setBackground(background);
        return panel;
    }

    private TextView createText(String text, int sp, int color, boolean bold) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(sp);
        textView.setTextColor(color);
        textView.setIncludeFontPadding(true);
        if (bold) {
            textView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        }
        return textView;
    }

    private Button createButton(String text) {
        Button button = new Button(this);
        button.setText(text);
        button.setAllCaps(false);
        button.setMinHeight(dp(48));
        return button;
    }

    private LinearLayout.LayoutParams matchWrapParamsWithBottomMargin(int marginDp) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, dp(marginDp));
        return params;
    }

    private LinearLayout.LayoutParams matchWrapParamsWithTopMargin(int marginDp) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, dp(marginDp), 0, 0);
        return params;
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    private void updateAccessibilityStatus() {
        if (statusText == null) {
            return;
        }
        boolean enabled = isAccessibilityEnabled();
        statusText.setText(enabled
                ? getString(R.string.accessibility_status_on)
                : getString(R.string.accessibility_status_off));
        statusText.setTextColor(enabled ? COLOR_ON : COLOR_OFF);
    }

    private boolean isAccessibilityEnabled() {
        String enabledServices = Settings.Secure.getString(
                getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        );
        if (enabledServices == null) {
            return false;
        }

        String expected = new ComponentName(this, MapperAccessibilityService.class).flattenToString();
        TextUtils.SimpleStringSplitter splitter = new TextUtils.SimpleStringSplitter(':');
        splitter.setString(enabledServices);
        while (splitter.hasNext()) {
            if (splitter.next().equalsIgnoreCase(expected)) {
                return true;
            }
        }
        return false;
    }

    private class ActionAdapter extends ArrayAdapter<MapperPrefs.MapperAction> {
        ActionAdapter(MapperPrefs.MapperAction[] actions) {
            super(MainActivity.this, android.R.layout.simple_spinner_item, actions);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setText(MapperPrefs.getActionLabel(MainActivity.this, getItem(position)));
            view.setTextColor(Color.BLACK);
            view.setTextSize(16);
            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position, convertView, parent);
            view.setText(MapperPrefs.getActionLabel(MainActivity.this, getItem(position)));
            view.setTextSize(16);
            return view;
        }
    }
}
