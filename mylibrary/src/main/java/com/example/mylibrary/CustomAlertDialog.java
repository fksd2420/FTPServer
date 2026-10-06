package com.example.mylibrary;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

public class CustomAlertDialog extends DialogFragment {

    AppCompatActivity context;
    View view;
    TextView title_textview;
    TextView summary_textview;
    LinearLayout leftButtons_container;
    LinearLayout rightButtons_container;
    List<Button> buttons;
    RelativeLayout scrollview;
    OnClickListener onClickListener;

    public CustomAlertDialog(AppCompatActivity context) {
        this.context = context;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the custom layout
        View view = inflater.inflate(R.layout.alertdialog_custom, container, false);

        title_textview = view.findViewById(R.id.customAlertDialog_title);
        summary_textview = view.findViewById(R.id.customALertDialog_summary);

        leftButtons_container = view.findViewById(R.id.customAlertDialog_buttonsLeftContainer);
        rightButtons_container = view.findViewById(R.id.customAlertDialog_buttonsRightContainer);

        scrollview = view.findViewById(R.id.customAlertDialog_scrollview);
        this.view = view;



        return view;
    }


    public void Show(String title, String summary, View view_ , String[] buttons_, OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        showNow(context.getSupportFragmentManager(), "CustomAlertDialog");



        title_textview.setText(title);
        summary_textview.setText(summary);

        scrollview.addView(view_);


        this.buttons = new ArrayList<>();
        for (int i = 0; i < buttons_.length; i++) {

            Button button = new Button(context);
            button.setText(buttons_[i]);
            TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            button.setBackgroundResource(outValue.resourceId);
            LinearLayout.LayoutParams ll_lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            ll_lp.setMargins(dpToP(10),0,dpToP(0),0);

            button.setPadding(dpToP(5),dpToP(5),dpToP(5),dpToP(5));
            button.setMinimumWidth(0);
            button.setMinimumHeight(0);
            button.setMinWidth(0);
            button.setMinHeight(0);
            button.setAllCaps(false);


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button button_ = (Button) view;
                    String s = button_.getText().toString();

                    int index = -1;
                    for (int j = 0; j < buttons_.length; j++) {
                        if (s.equals(buttons_[j])) {
                            index = j;
                            break;
                        }
                    }
                    if (CustomAlertDialog.this.onClickListener != null) {
                        CustomAlertDialog.this.onClickListener.onClick(button_, index);
                    }
                }
            });

            if (buttons_.length != 1 && i == buttons_.length - 1) {
                ll_lp.setMargins(dpToP(0),0,dpToP(10),0);
                rightButtons_container.addView(button);
            } else {
                leftButtons_container.addView(button);
            }



        }
        view.post(new Runnable() {
            @Override
            public void run() {
                onClickListener.onShow();
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        if (getDialog() != null && getDialog().getWindow() != null) {
            // Get the current screen metrics
            int screenWidth = getResources().getDisplayMetrics().widthPixels;

            // Calculate 90% of the screen width
            int dialogWidth = (int) (screenWidth * 0.90);
            int dialogHeight = ViewGroup.LayoutParams.WRAP_CONTENT; // Wrap height around contents

            // Apply to the dialog window
            getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
        }
    }


    public interface OnClickListener {
        public void onClick(View view, int pos);
        void onShow();
    }

    private int dpToP(int dp) {
        Resources r = this.context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }
}
