package com.lizarda.lizarda;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by arifinfrds on 11/24/17.
 */

public class Utils {

    public static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
