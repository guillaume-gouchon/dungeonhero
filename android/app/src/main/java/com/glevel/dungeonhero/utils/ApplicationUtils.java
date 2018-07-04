package com.glevel.dungeonhero.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.glevel.dungeonhero.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ApplicationUtils {

    private static final String TAG = ApplicationUtils.class.getName();

    public static final String PREFS_NB_LAUNCHES = "nb_launches";
    public static final String PREFS_RATE_DIALOG_IN = "rate_dialog_in";
    public static final int NB_LAUNCHES_RATE_DIALOG_APPEARS = 5;
    public static final int NB_LAUNCHES_WITH_SPLASHSCREEN = 8;

    public static void showRateDialogIfNeeded(final Activity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        if (prefs.getInt(PREFS_RATE_DIALOG_IN, NB_LAUNCHES_RATE_DIALOG_APPEARS) == 0) {
            Log.d(TAG, "show rate dialog");
            final Editor editor = prefs.edit();

            // Create and show custom alert dialog
            final Dialog dialog = new Dialog(activity, R.style.Dialog);
            dialog.setContentView(R.layout.custom_rate_dialog);

            ((TextView) dialog.findViewById(R.id.message)).setText(activity.getString(R.string.rate_message, activity.getString(R.string.app_name)));
            dialog.findViewById(R.id.cancel_btn).setOnClickListener(v -> {
                editor.putInt(PREFS_RATE_DIALOG_IN, -1);
                editor.apply();
                dialog.dismiss();
            });
            dialog.findViewById(R.id.neutralButton).setOnClickListener(v -> {
                editor.putInt(PREFS_RATE_DIALOG_IN, 5);
                dialog.dismiss();
            });
            dialog.findViewById(R.id.ok_btn).setOnClickListener(v -> {
                editor.putInt(PREFS_RATE_DIALOG_IN, -1);
                editor.apply();
                rateTheApp(activity);
                dialog.dismiss();
            });

            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

    public static void rateTheApp(Activity activity) {
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + activity.getPackageName()));
        activity.startActivity(goToMarket);
    }

    public static void contactSupport(Context context) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{context.getString(R.string.mail_address)});
        i.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.contact_title) + context.getString(R.string.app_name));
        try {
            context.startActivity(Intent.createChooser(i, context.getString(R.string.contact_support_via)));
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(context, context.getString(R.string.no_mail_client), Toast.LENGTH_LONG).show();
        }
    }

    public static void showToast(Context context, String text, int duration) {
        // setup custom toast view
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_toast, null);
        TextView tv = layout.findViewById(R.id.text);
        tv.setText(text);

        Toast toast = new Toast(context);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
    }

    public static void showToast(Context context, int textResourceId, int duration) {
        showToast(context, context.getString(textResourceId), duration);
    }

    public static void openDialogFragment(FragmentActivity activity, DialogFragment dialog, Bundle bundle) {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        Fragment prev = activity.getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        dialog.setArguments(bundle);
        dialog.show(ft, "dialog");
    }

    public static Runnable addStormBackgroundAtmosphere(final ImageView backgroundView, final int fromAlpha, final int stormAlpha) {
        // Storm atmosphere
        backgroundView.setColorFilter(Color.argb(fromAlpha, 0, 0, 0));
        Runnable stormEffectRunnable = new Runnable() {

            private boolean isThunder = false;

            public void run() {
                if (Math.random() < 0.03) {
                    // thunderstruck !
                    isThunder = true;
                    backgroundView.setColorFilter(Color.argb(stormAlpha, 0, 0, 0));
                } else if (isThunder) {
                    isThunder = false;
                    backgroundView.setColorFilter(Color.argb(fromAlpha, 0, 0, 0));
                }
                backgroundView.postDelayed(this, 200);
            }
        };
        backgroundView.postDelayed(stormEffectRunnable, 200);
        return stormEffectRunnable;
    }

    public static void startSharing(Activity activity, String subject, String text, int image) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
        activity.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public static boolean isAppInstalled(Context context, String uri) {
        try {
            PackageManager pm = context.getPackageManager();
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, "cannot find application " + uri);
        }
        return false;
    }

    public static int convertDpToPixels(Context context, int dp) {
        Resources res = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
    }

    public static void showKeyboard(final Context context, final View view) {
        // shows the keyboard
        view.postDelayed(() -> {
            InputMethodManager keyboard = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            keyboard.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }, 150);
    }

    public static String getAppVersion(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "unknown";
    }

    public static Object deepCopy(Object object) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            oos.flush();
            oos.close();
            bos.close();
            byte[] byteData = bos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
            return new ObjectInputStream(bais).readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Bitmap getBitmapFromAsset(Context context, String imageName) {
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open(imageName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(inputStream);
    }

    public static void saveToLocalFile(Context context, String filename, Object content) {
        try {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(content);
            oos.flush();
            oos.close();
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object readFromLocalFile(Context context, String fileName) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object object = ois.readObject();
            ois.close();
            fis.close();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setAlpha(View view, float alpha) {
        AlphaAnimation anim = new AlphaAnimation(alpha, alpha);
        anim.setDuration(0);
        anim.setFillAfter(true);
        view.startAnimation(anim);
    }

}
