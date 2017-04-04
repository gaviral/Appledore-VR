package com.vroneinc.vrone.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Created by Emre on 02/04/2017.
 */

public class ForumEditText extends EditText {
    public ForumEditText(Context context) {
        super(context);
    }

    public ForumEditText(Context context, AttributeSet attribute_set) {
        super(context, attribute_set);
    }

    public ForumEditText(Context context, AttributeSet attribute_set, int def_style_attribute) {
        super(context, attribute_set, def_style_attribute);
    }

    @Override
    public boolean onKeyPreIme(int key_code, KeyEvent event) {
        // This is for clearing focus from the edit text onBackPressed
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
            this.clearFocus();

        return super.onKeyPreIme(key_code, event);
    }
}
