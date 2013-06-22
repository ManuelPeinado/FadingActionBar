package com.manuelpeinado.fadingactionbar;

import android.content.Context;
import android.view.WindowManager;

public class Utils {
    public static int getDisplayHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        @SuppressWarnings("deprecation")
        int displayHeight = wm.getDefaultDisplay().getHeight();
        return displayHeight;
    }
}
