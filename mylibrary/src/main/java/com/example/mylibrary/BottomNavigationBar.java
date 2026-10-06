package com.example.mylibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mylibrary.Common;

import java.util.Arrays;
import java.util.LinkedList;

public class BottomNavigationBar extends RelativeLayout {


    private Context context;
    LinearLayout container;
    LinkedList<Item> items = new LinkedList<>();
    int selectedItemIndex = -1;

    public BottomNavigationBar(Context context) {
        super(context);
        init();
    }

    public BottomNavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BottomNavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        this.context = getContext();

        LayoutParams rl_lp;

        container = new LinearLayout(context);
        rl_lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        container.setLayoutParams(rl_lp);
        container.setOrientation(LinearLayout.HORIZONTAL);

        addView(container);
    }


    public void addItem(String name, int iconRes) {
        LinearLayout.LayoutParams ll_lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        LayoutParams rl_lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        RelativeLayout rl = new RelativeLayout(context);
        ll_lp.weight = 1;
        rl.setLayoutParams(ll_lp);

        RelativeLayout rl_= new RelativeLayout(context);
        rl_lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rl_lp.addRule(RelativeLayout.CENTER_VERTICAL);
        rl_.setLayoutParams(rl_lp);

        rl_.setOnTouchListener(new OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.i("BottomNavigationBar", event.getRawX() + " " + event.getRawY());

                Item item = null;
                int itemPos = -1;
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).container == v) {
                        item = items.get(i);
                        itemPos = i;
                        break;

                    }
                }



                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (item.isChecked == false) {
                        setItemFocused(item);
                    }


                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (inView(v, (int)event.getRawX(), (int)event.getRawY()) == false) {
                        clearSelectedItems(true);
                        return false;
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {


                    if (inView(v, (int)event.getRawX(), (int)event.getRawY()) == false) {
                        clearSelectedItems(true);
                        return false;
                    }

                    if (itemPos != selectedItemIndex) {

                        clearSelectedItems(false);


                        setItemSelected(item);
                        onSelectedItemChangeListener.onSelectedItemChange(itemPos);

                        selectedItemIndex = itemPos;
                    }
                    return false;
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    clearSelectedItems(true);
                }
                return false;
            }
        });
        rl_.setTooltipText(name);
        rl_.setPadding((int) Common.convertDpToPixel(10, context),(int)Common.convertDpToPixel(10, context),
                (int)Common.convertDpToPixel(10, context),(int)Common.convertDpToPixel(10, context));

        Item item = new Item();
        item.isChecked = false;
        item.name = name;
        item.container=rl_;

        ImageView iv = new ImageView(context);
        rl_lp = new LayoutParams((int)Common.convertDpToPixel(30,context),(int)Common.convertDpToPixel(30,context));
        rl_lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        iv.setLayoutParams(rl_lp);
        iv.setImageResource(iconRes);

        item.imageView = iv;

        TextView tv = new TextView(context);
        rl_lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rl_lp.setMargins(0,(int)Common.convertDpToPixel(30, context),0,0);
        tv.setLayoutParams(rl_lp);
        item.textView = tv;
        tv.setTextColor(0xffffffff);
        tv.setTextSize(11);
        tv.setText(name);
        tv.setTypeface(null, Typeface.BOLD);
        rl_.addView(iv);
        rl_.addView(tv);
        rl.addView(rl_);
        container.addView(rl);

        items.add(item);




        rl_.setClickable(true);
        rl_.setFocusable(true);
        setUsd(rl_);
    }
    public void addItem(String name) {
        LinearLayout.LayoutParams ll_lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        LayoutParams rl_lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        RelativeLayout rl = new RelativeLayout(context);
        ll_lp.weight = 1;
        rl.setLayoutParams(ll_lp);

        RelativeLayout rl_= new RelativeLayout(context);
        rl_lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rl_lp.addRule(RelativeLayout.CENTER_VERTICAL);
        rl_.setLayoutParams(rl_lp);

        rl_.setOnTouchListener(new OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.i("BottomNavigationBar", event.getRawX() + " " + event.getRawY());

                Item item = null;
                int itemPos = -1;
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).container == v) {
                        item = items.get(i);
                        itemPos = i;
                        break;

                    }
                }



                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (item.isChecked == false) {
                        setItemFocused(item);
                    }


                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (inView(v, (int)event.getRawX(), (int)event.getRawY()) == false) {
                        clearSelectedItems(true);
                        return false;
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {


                    if (inView(v, (int)event.getRawX(), (int)event.getRawY()) == false) {
                        clearSelectedItems(true);
                        return false;
                    }

                    if (itemPos != selectedItemIndex) {

                        clearSelectedItems(false);


                        setItemSelected(item);
                        onSelectedItemChangeListener.onSelectedItemChange(itemPos);

                        selectedItemIndex = itemPos;
                    }
                    return false;
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    clearSelectedItems(true);
                }
                return false;
            }
        });
        rl_.setTooltipText(name);
        rl_.setPadding((int)Common.convertDpToPixel(15, context),(int)Common.convertDpToPixel(15, context),
                (int)Common.convertDpToPixel(15, context),(int)Common.convertDpToPixel(15, context));

        Item item = new Item();
        item.isChecked = false;
        item.name = name;
        item.container=rl_;


        TextView tv = new TextView(context);
        rl_lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rl_lp.setMargins(0,(int)Common.convertDpToPixel(0, context),0,0);
        tv.setLayoutParams(rl_lp);
        item.textView = tv;
        tv.setTextColor(0xffffffff);
        tv.setTextSize(14);
        tv.setText(name);
        tv.setTypeface(null, Typeface.BOLD);
        rl_.addView(tv);
        rl.addView(rl_);
        container.addView(rl);

        items.add(item);




        rl_.setClickable(true);
        rl_.setFocusable(true);
        setUsd(rl_);
    }

    private void setItemFocused(Item item) {
        item.textView.setTextColor(0xff808080);
        if (item.imageView !=null)
        item.imageView.setColorFilter(Color.argb(255, 128, 128, 128), PorterDuff.Mode.MULTIPLY);
    }
    private void setItemSelected(Item item) {
        item.textView.setTextColor(0xff6750A4);
        if(item.imageView!=null)
        item.imageView.setColorFilter(Color.argb(255, 0x67, 0x50, 0xA4), PorterDuff.Mode.MULTIPLY);
        item.isChecked = true;
        setSelectedD(item.container);
    }
    private void clearSelectedItems(boolean leaveChecked) {
        for (int i = 0; i < items.size(); i++) {
            if (leaveChecked && items.get(i).isChecked) {
                continue;
            }

            items.get(i).textView.setTextColor(0xffffffff);
            if (items.get(i).imageView !=null)
            items.get(i).imageView.setColorFilter(Color.argb(255, 255, 255, 255), PorterDuff.Mode.MULTIPLY);
            items.get(i).isChecked = false;

            setUsd(items.get(i).container);
        }
    }
    public void setSelectedItem(int index) {

        if (selectedItemIndex == index) {
            return;
        }
        clearSelectedItems(false);

        Item item = items.get(index);
        setItemSelected(item);
        selectedItemIndex=index;
        onSelectedItemChangeListener.onSelectedItemChange(index);
    }
    OnSelectedItemChangeListener onSelectedItemChangeListener;
    public void setOnSelectedItemChangeListener(OnSelectedItemChangeListener listener) {
        onSelectedItemChangeListener = listener;
    }
    private void setSelectedD(View v) {
        int color = 0xff0000ff;
        ColorStateList pressedColor = ColorStateList.valueOf(0x20ffffff);
        Drawable defaultColor = getRippleColor(0x20ffffff);
        Drawable rippleColor = getRippleColor(0xffffffff);
        RippleDrawable rd =  new RippleDrawable(
                pressedColor,
                defaultColor,
                rippleColor
        );
v.setBackground(rd);
    }
    private void setUsd(View v) {
        int color = 0xff0000ff;
        ColorStateList pressedColor = ColorStateList.valueOf(0x20ffffff);
        Drawable defaultColor = getRippleColor(0x00ffffff);
        Drawable rippleColor = getRippleColor(0xffffffff);
        RippleDrawable rd =  new RippleDrawable(
                pressedColor,
                defaultColor,
                rippleColor
        );
        v.setBackground(rd);
    }

    private static Drawable getRippleColor(int color) {
        float[] outerRadii = new float[8];
        Arrays.fill(outerRadii, 20);
        RoundRectShape r = new RoundRectShape(outerRadii, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(r);
        shapeDrawable.getPaint().setColor(color);
        return shapeDrawable;
    }

    public interface OnSelectedItemChangeListener {
        public void onSelectedItemChange(int position);
    }
    public class Item {

        public String name;
        public boolean isChecked;

        RelativeLayout container;
        ImageView imageView;
        TextView textView;
    }



    private boolean inView(View view, int rx, int ry) {
        int[] l = new int[2];
        view.getLocationOnScreen(l);

        int x = l[0];
        int y = l[1];
        int w = view.getWidth();
        int h = view.getHeight();

        if (rx < x || rx > x + w || ry < y || ry > y + h) {
            return false;
        }
        return true;
    }
}
