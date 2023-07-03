package com.example.gh_train_app.OthersFiles;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.gh_train_app.R;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Tools {
    public static String APPID = "1048";
    public static String CONTENT_TYPE = "application/json;charset=UTF-8";
    public static String APIKEY = "sZQsh4LGEDXJqVFGENJbCN5PTM7br3RzTjcQLmpCDnq3w6ZFZA";
    public static String CLIENTREFERENCE = "Book for a train ticket";
    public static String DESCRIPTION = "Amount to pay for a ticket.";
    public static String NICKNAME = "Jayvon Solutions";
    public static String BASE_URL = "https://api.reddeonline.com/";
    public static String SEND_MONEY = "Send_money";
    public static String RECEIVE_MONEY = "Receive_money";
    public static String PUSH_ID = "push_id";
    public static String BOOK_ID = "book_id";
    public static String TRANSACTION_ID = "transaction_id";
    private static final Random random = new Random();
    private static final String CHARS = "abcdefghijkmnopqrstuvwxyz1234567890";

    public static void setSystemBarColor(Activity act) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(act.getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    public static void displayImageOriginal(Context ctx, ImageView img, @DrawableRes int drawable) {
        try {
            Glide.with(ctx).load(drawable)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(img);
        } catch (Exception e) {
        }
    }

    public static void copyToClipboard(Context context, String data) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("clipboard", data);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "Booking code copied to clipboard", Toast.LENGTH_SHORT).show();
    }


    public static void nestedScrollTo(final NestedScrollView nested, final View targetView) {
        nested.post(new Runnable() {
            @Override
            public void run() {
                nested.scrollTo(500, targetView.getBottom());
            }
        });
    }


    public static int dpToPx(Context c, int dp) {
        Resources r = c.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    //Validate Email
    public static boolean validateEmail(String email) {
        String emailPattern = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher((CharSequence) email);
        return matcher.matches();
    }

    // Validate Password
    public static boolean validatePassword(String pass) {
        if (pass != null && pass.length() >= 6 && pass.length() <= 20) {
            return true;
        } else
            return false;
    }

    // Validate phone number
    public static boolean validateMobile(String mobile) {
        String regexStr = "^[+]?[0-9]{10,13}$";
        if (mobile.length() < 10 || mobile.length() > 13 || mobile.matches(regexStr) == false) {
            return false;
        } else
            return true;
    }

    //    function to generate client transaction id
    public static String generateClientTransactionId(int length) {
        StringBuilder token = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            token.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return token.toString();
    }

    // simple method to show toast message
    public static void showMessage(Context context, CharSequence message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

}
