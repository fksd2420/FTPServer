package com.example.mylibrary;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ActionBar {

    Context context = null;
    LayoutInflater layoutInflater = null;
    ViewGroup rootView=null;
    ViewGroup view = null;
    public List<View> options = new ArrayList<>();
    public ActionBar(Context context, ViewGroup rootView) {
        this.context = context;
        layoutInflater = (LayoutInflater)  context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.rootView = rootView;
        view = (ViewGroup) layoutInflater.inflate(R.layout.actionbar, rootView, false);

        rootView.addView(view, 0);
    }

    public View getRootView() {
        return view;
    }


    public ImageButton getMenuButton() {
        return view.findViewById(R.id.actionbar_menubutton);
    }

    public TextView getTitleTextView() {
        return view.findViewById(R.id.actionbar_title);
    }
    public void setTitle(String title) {
        TextView tv = view.findViewById(R.id.actionbar_title);
        tv.setText(title);
    }

    public LinearLayout getOptionsContainer() {
        return view.findViewById(R.id.actionbar_options_container);
    }

    public void addOptionsItem(String text) {
        Button button = new Button(this.context);
        LinearLayout.LayoutParams ll_lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        ll_lp.setMargins(0,0,dpToP(15),0);

        button.setPadding(dpToP(10),dpToP(10),dpToP(10),dpToP(10));
        button.setMinimumWidth(0);
        button.setMinimumHeight(0);
        button.setMinWidth(0);
        button.setMinHeight(0);

        button.setLayoutParams(ll_lp);
        ll_lp.gravity = Gravity.CENTER_VERTICAL;

        button.setText(text);
        button.setTextColor(0xffffffff);
        options.add(button);
        button.setBackground(null);
        //button.setBackgroundColor(0xffff0000);
        getOptionsContainer().addView(button, 0);

        button.setBackground(createDrawableSelector());
    }
    public static StateListDrawable createDrawableSelector() {
        StateListDrawable selector = new StateListDrawable();

        Drawable defaultDrawable = new ColorDrawable(0x00000000);
        Drawable pressedDrawable = new ColorDrawable(0x0FFFFFFF);;

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
    public void addOptionsItem(View item, int width, int height) {
        LinearLayout.LayoutParams ll_lp = new LinearLayout.LayoutParams(dpToP(width), dpToP(height));
        item.setLayoutParams(ll_lp);

        getOptionsContainer().addView(item, 0);
    }
    public ImageButton getMoreButton() {
        return view.findViewById(R.id.actionbar_more);
    }
    public void showMoreButton() {
        view.findViewById(R.id.actionbar_more).setVisibility(View.VISIBLE);
    }
    public void hideMoreButton() {
        view.findViewById(R.id.actionbar_more).setVisibility(View.GONE);
    }
    public void hideMenuButton()
    {
        view.findViewById((R.id.actionbar_menubutton)).setVisibility(View.GONE);
    }




    private int dpToP(int dp) {
        Resources r = this.context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

}
