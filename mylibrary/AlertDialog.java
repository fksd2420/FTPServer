package com.example.mylibrary;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.core.text.HtmlCompat;

import java.util.function.Function;

public class AlertDialog {

    private Thread curr = null;
    public int result = -1;
    private Function<Integer, Void> onCompleteF= null;

    public void setOnCompleteCallback(Function<Integer, Void> f) {
        onCompleteF = f;
    }
    public void show(android.content.Context context, String title, String message, String pB, String nB) {
        curr = Thread.currentThread();
        var this_=this;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                builder.setTitle(title);
                builder.setMessage(message);
                builder.setCancelable(false);

                if (pB != null) {
                    builder.setPositiveButton(pB, (DialogInterface.OnClickListener) (dialog, which) -> {
                        result = 1;
                        synchronized (this_) {
                            this_.notify();
                        }
                        if (onCompleteF != null)
                            onCompleteF.apply(1);
                    });
                }
                if (nB != null) {
                    builder.setNegativeButton(nB, (DialogInterface.OnClickListener) (dialog, which) -> {
                        result = 0;
                        synchronized (this_) {
                            this_.notify();
                        }

                        if (onCompleteF != null)
                            onCompleteF.apply(0);
                    });
                }

                androidx.appcompat.app.AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });
//        ()(context).runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
    }
    public void wait_() {
        Log.i("abc", "waiting on thread");
        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.i("abc", "thread resumed");
    }


    public static Object wait_object = new Object();
    public static String result_ = new String();


    public static int RESULT_POSITIVE = 0;
    public static int RESULT_NEUTRAL = 9;
    public static int RESULT_NEGATIVE = -1;

    public static void show(Context context, String title, String message, String positiveButton, String neutralButton, String negativeButton, Callback onResult) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setTitle(title);
        TextView textView = new TextView(context);
        textView.setText(message);
        builder.setView(textView);
        textView.setTextSize(16);
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.dialogPreferredPadding, typedValue, true);
        int paddingPixels = TypedValue.complexToDimensionPixelSize(typedValue.data, context.getResources().getDisplayMetrics());
        int dp20 = dpToP(context, 20);
        textView.setPadding(paddingPixels, dp20,paddingPixels,dp20);
        // Doesn't preserve white-space.
        //builder.setMessage(HtmlCompat.fromHtml(String.format("<pre style='white-space:pre-wrap;'>     %s</pre>", message).replace("\n", "<br/>"), HtmlCompat.FROM_HTML_MODE_COMPACT));
        builder.setCancelable(false);

        if (negativeButton != null) {
            builder.setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result_ = negativeButton;
                    if (onResult != null) {
                        onResult.onResult(RESULT_NEGATIVE);
                    }
                    synchronized (wait_object) {
                        wait_object.notify();
                    }
                }
            });
        }
        if (neutralButton != null) {
            builder.setNeutralButton(neutralButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result_ = neutralButton;
                    if (onResult != null) {
                        onResult.onResult(RESULT_NEUTRAL);
                    }
                    synchronized (wait_object) {
                        wait_object.notify();
                    }
                }
            });
        }
        builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                result_ = positiveButton;
                if (onResult != null) {
                    onResult.onResult(RESULT_POSITIVE);
                }
                synchronized (wait_object) {
                    wait_object.notify();
                }
            }
        });

        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                androidx.appcompat.app.AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }
    public static void show(Context context, String title, String message, String positiveButton, String neutralButton, String negativeButton) {
        show(context, title, message, positiveButton, neutralButton, negativeButton, null);
    }
    public static String waitForResult() {
        try {
            synchronized (wait_object) {
                wait_object.wait();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return result_;
    }
    private static int dpToP(Context context, int dp) {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }





    public interface Callback {
        public void onResult(int result);
    }
}
