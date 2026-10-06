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
import android.media.Image;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;

import java.util.ArrayList;
import java.util.List;




public class ActionBar extends RelativeLayout {

    Context context = null;
    LayoutInflater layoutInflater = null;
    ViewGroup view = null;
    ImageButton backButton;
    ImageButton moreButton;

    public List<View> options = new ArrayList<>();
    public List<View> actionModeOptions = new ArrayList<>();
    public boolean isActionModeEnabled = false;
    public OnCallbacks Callbacks;









    public ActionBar(Context context) {
        this(context, null, 0);

    }
    public ActionBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }
    public ActionBar(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        this.context = context;
        layoutInflater = (LayoutInflater)  context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = (ViewGroup) layoutInflater.inflate(R.layout.actionbar, this, false);

        addView(view, 0);

        backButton = view.findViewById(R.id.actionbar_backButton);
        moreButton = view.findViewById(R.id.actionbar_more);

        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Callbacks != null) Callbacks.onBack(view);
            }
        });
        moreButton.setOnClickListener(this::moreButton_onClick);
    }
    public View getRootView() {
        return view;
    }


    public ImageButton getBackButton() {
        return view.findViewById(R.id.actionbar_backButton);
    }

    public void setTitle(String title) {
        TextView tv = view.findViewById(R.id.actionbar_title);
        tv.setText(title);
    }

    public LinearLayout getOptionsContainer() {
        return view.findViewById(R.id.actionbar_options_container);
    }
    public LinearLayout getActionModeOptionsContainer() {
        return view.findViewById(R.id.actionbar_actionModeOptionsContainer);
    }
    public void StartActionMode() {
        View view_ = view.findViewById(R.id.actionbar_options_container);
        View view__ = view.findViewById(R.id.actionbar_actionModeOptionsContainer);

        view_.setVisibility(View.GONE);
        view__.setVisibility(View.VISIBLE);
        isActionModeEnabled=true;

        backButton.setVisibility(View.VISIBLE);
    }
    public void StopActionMode() {
        View view_ = view.findViewById(R.id.actionbar_options_container);
        View view__ = view.findViewById(R.id.actionbar_actionModeOptionsContainer);

        view_.setVisibility(View.VISIBLE);
        view__.setVisibility(View.GONE);

        actionModeOptions.clear();

        getActionModeOptionsContainer().removeAllViews();
        isActionModeEnabled = false;

        backButton.setVisibility(View.GONE);
    }
    public void addOptionsItem(String text) {
        Button button = new Button(this.context);
        LinearLayout.LayoutParams ll_lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        ll_lp.setMargins(0,0,0,0);

        button.setPadding(UIHelper.dpToP(context, 10),UIHelper.dpToP(context, 10),
                UIHelper.dpToP(context, 10),UIHelper.dpToP(context, 10));
        button.setMinimumWidth(0);
        button.setMinimumHeight(0);
        button.setMinWidth(0);
        button.setMinHeight(0);

        button.setTooltipText(text);

        button.setLayoutParams(ll_lp);
        ll_lp.gravity = Gravity.CENTER_VERTICAL;

        button.setText(text);
        button.setTextColor(0xffffffff);
        button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        button.setBackground(null);
        //button.setBackgroundColor(0xffff0000);
        if (isActionModeEnabled) {
            getActionModeOptionsContainer().addView(button, 0);
            actionModeOptions.add(button);
        } else {
            getOptionsContainer().addView(button, 0);
            options.add(button);
        }

        button.setBackground(UIHelper.createDrawableSelector(0x00000000, 0x50ffffff));
    }
    private void moreButton_onClick(View v) {
        PopupMenu popup = new PopupMenu(context, v);
        Menu menu = popup.getMenu();

        // 2. Add items programmatically: add(groupId, itemId, order, title)
        // Use Menu.NONE (0) for parameters you don't need to categorize
        menu.add(Menu.NONE, 1, 1, "Edit");
        menu.add(Menu.NONE, 2, 2, "Share");
        menu.add(Menu.NONE, 3, 3, "Delete");

        // 3. Handle item clicks using the unique itemId
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case 1:
                    Toast.makeText(context, "Edit clicked", Toast.LENGTH_SHORT).show();
                    return true;
                case 2:
                    Toast.makeText(context, "Share clicked", Toast.LENGTH_SHORT).show();
                    return true;
                case 3:
                    Toast.makeText(context, "Delete clicked", Toast.LENGTH_SHORT).show();
                    return true;
                default:
                    return false;
            }
        });

        // 4. Show the popup
        popup.show();

    }

    public void showMoreButton() {
        view.findViewById(R.id.actionbar_more).setVisibility(View.VISIBLE);
    }
    public void hideMoreButton() {
        view.findViewById(R.id.actionbar_more).setVisibility(View.GONE);
    }



    public interface OnCallbacks {
        void onBack(View v);
        void onOptions(View v);
    }


}
