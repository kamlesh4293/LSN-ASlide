package com.app.lsquared.ui;

import android.graphics.drawable.Drawable;

import top.defaults.drawabletoolbox.DrawableBuilder;

public class UtilDrawable {

    public static void getFilled(){
        Drawable drawable = new DrawableBuilder()
                .rectangle()
                .solidColor(0xffe67e22)
                .bottomLeftRadius(20) // in pixels
                .bottomRightRadius(20) // in pixels
//        .cornerRadii(0, 0, 20, 20) // the same as the two lines above
                .build();
    }
}
