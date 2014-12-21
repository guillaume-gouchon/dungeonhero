package com.glevel.dungeonhero.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.glevel.dungeonhero.R;

public class ApplicationUtils {

    public static final String PREFS_NB_LAUNCHES = "nb_launches";
    public static final String PREFS_RATE_DIALOG_IN = "rate_dialog_in";
    public static final int NB_LAUNCHES_RATE_DIALOG_APPEARS = 5;
    public static final int NB_LAUNCHES_WITH_SPLASHSCREEN = 8;
    public static final int NB_LAUNCHES_ADVERTISEMENT_1 = 3;
    public static final int NB_LAUNCHES_ADVERTISEMENT_2 = 7;

    public static void showRateDialogIfNeeded(final Activity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        if (prefs.getInt(PREFS_RATE_DIALOG_IN, NB_LAUNCHES_RATE_DIALOG_APPEARS) == 0) {
            final Editor editor = prefs.edit();

            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.custom_rate_dialog, null);

            // Create and show custom alert dialog
            final Dialog dialog = new AlertDialog.Builder(activity, R.style.Dialog).setView(view).create();

            ((TextView) view.findViewById(R.id.message)).setText(activity.getString(R.string.rate_message, activity.getString(R.string.app_name)));
            view.findViewById(R.id.cancelButton).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.putInt(PREFS_RATE_DIALOG_IN, -1);
                    editor.apply();
                    dialog.dismiss();
                }
            });
            view.findViewById(R.id.neutralButton).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.putInt(PREFS_RATE_DIALOG_IN, 5);
                    dialog.dismiss();
                }
            });
            view.findViewById(R.id.okButton).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.putInt(PREFS_RATE_DIALOG_IN, -1);
                    editor.apply();
                    rateTheApp(activity);
                    dialog.dismiss();
                }
            });

            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            // Remove padding from parent
            ViewGroup parent = (ViewGroup) view.getParent();
            parent.setPadding(0, 0, 0, 0);
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
        TextView tv = (TextView) layout.findViewById(R.id.text);
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

    public static Point getScreenDimensions(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
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
        }
        return false;
    }

    public static void showAdvertisementIfNeeded(Activity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        int nbLaunches = prefs.getInt(PREFS_NB_LAUNCHES, 0);
        if ((nbLaunches == NB_LAUNCHES_ADVERTISEMENT_1 || nbLaunches == NB_LAUNCHES_ADVERTISEMENT_2) && !isAppInstalled(activity, "com.giggs.apps.chaos")) {
            Dialog mAdvertisementDialog = new Dialog(activity, R.style.Dialog);
            mAdvertisementDialog.setCancelable(true);
            mAdvertisementDialog.setContentView(R.layout.dialog_advertisement);
            TextView titleTV = (TextView) mAdvertisementDialog.findViewById(R.id.title);
            titleTV.setMovementMethod(LinkMovementMethod.getInstance());
            mAdvertisementDialog.findViewById(R.id.image).startAnimation(AnimationUtils.loadAnimation(activity, R.anim.loading_dots));
            mAdvertisementDialog.show();
        }
    }

    public static int convertDpToPixels(Context context, int dp) {
        Resources res = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
    }

    public static void showKeyboard(final Context context, final View view) {
        // shows the keyboard
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 150);
    }

}
