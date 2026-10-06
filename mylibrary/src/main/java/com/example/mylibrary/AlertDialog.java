package com.example.mylibrary;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;

import java.util.function.Function;

public class AlertDialog {

    private Thread curr = null;
    public int result = -1;
    private Function<Integer, Void> onCompleteF= null;
    public boolean selectable = false;
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
                if (selectable) {
                    TextView messageView = alertDialog.findViewById(android.R.id.message);

                    messageView.setTextIsSelectable(true);

                }
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
    public static boolean monospace=false;
    public static int ResultInt = -1;
    public static int RESULT_POSITIVE = 0;
    public static int RESULT_NEUTRAL = 2;
    public static int RESULT_NEGATIVE = 1;
    public static boolean enableScroll = false;

    public static void Show(Context context, String title, String[] items, CallbackI callback) {

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.filepicker, null);

        ListView listView = dialogView.findViewById(R.id.alertdialog_list_listview);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_list_item_1,
                items
        );
        listView.setAdapter(adapter);


        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setView(dialogView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.callback();
            }
        });
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        listView.setOnItemClickListener((parent, view, position, id) -> {
            ResultInt = position;
            dialog.dismiss();
            callback.callback();
        });
        ResultInt = -1;
        dialog.show();

    }
    public static void show(Context context, String title, String message, String positiveButton, String neutralButton, String negativeButton, Callback onResult) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setTitle(title);
        TextView textView = new TextView(context);
        textView.setText(message);
        textView.setTextSize(16);
        if (enableScroll){
            NestedScrollView nestedScrollView = new NestedScrollView(context);
            HorizontalScrollView horizontalScrollView = new HorizontalScrollView(context);
            horizontalScrollView.addView(textView);
            nestedScrollView.addView(horizontalScrollView);
            builder.setView(nestedScrollView);
        } else {

            builder.setView(textView);
        }
        if (monospace)
        textView.setTypeface(Typeface.MONOSPACE);
        textView.setMovementMethod(new ScrollingMovementMethod());
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
                    ResultInt = RESULT_NEGATIVE;
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
                    ResultInt = RESULT_NEUTRAL;
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
                ResultInt = RESULT_POSITIVE;
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


    public interface CallbackI {
        public void callback();
    }





    public interface Callback {
        public void onResult(int result);
    }
}
