package com.example.mylibrary;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.TypedValue;
import android.view.View;

import java.util.Arrays;

public class UIHelper {

    public static StateListDrawable createCircularDrawableSelector(int defaultColor, int pressedColor) {
        GradientDrawable oval = new GradientDrawable();
        oval.setShape(GradientDrawable.OVAL);
        oval.setColor(pressedColor);
        int strokeWidthInPixels = 0;
        oval.setStroke(strokeWidthInPixels, Color.parseColor("#FFFFFF"));

        Drawable defaultDrawable = new ColorDrawable(defaultColor);
        Drawable pressedDrawable = oval;;

        StateListDrawable selector = new StateListDrawable();

        // Add the pressed state
        selector.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);

        // Add the default state last
        selector.addState(new int[]{}, defaultDrawable);

        return selector;

    }
    public static StateListDrawable createDrawableSelector(int defaultColor, int pressedColor) {
        StateListDrawable selector = new StateListDrawable();

        Drawable defaultDrawable = new ColorDrawable(defaultColor);
        Drawable pressedDrawable = new ColorDrawable(pressedColor);;

        // Add the pressed state
        selector.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);

        // Add the default state last
        selector.addState(new int[]{}, defaultDrawable);

        return selector;
//
//
//        GradientDrawable shape = new GradientDrawable();
//        shape.setShape(GradientDrawable.RECTANGLE);
//        shape.setColor(Color.RED);
//        shape.setCornerRadius(15f * getResources().getDisplayMetrics().density); // Set all corners
//          shape.setStroke(2, Color.RED);
//// For specific corners:
//// float[] radii = new float[] { 8, 8, 8, 8, 0, 0, 0, 0 };
//// shape.setCornerRadii(radii);

    }
    public static void setSelectedD(View v, int backgroundColor, int rippleColor) {

        RippleDrawable rippleDrawable = new RippleDrawable(
                ColorStateList.valueOf(rippleColor), // Ripple color
                new ColorDrawable(backgroundColor),                                                 // Content drawable
                new ColorDrawable(Color.WHITE)                        // Mask drawable
        );

        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{}, rippleDrawable);


        v.setBackground(stateListDrawable);
    }



    public static int dpToP(Context context, int dp) {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }


}
