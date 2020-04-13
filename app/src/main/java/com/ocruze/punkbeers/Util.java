package com.ocruze.punkbeers;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

public class Util {

    public static void showToast(Context c, String msg) {
        Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
        System.out.println(msg);
    }
}
