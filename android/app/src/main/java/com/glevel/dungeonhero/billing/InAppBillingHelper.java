package com.glevel.dungeonhero.billing;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.android.vending.billing.IInAppBillingService;

public class InAppBillingHelper {

    private static final String IN_APP_PURCHASE_TYPE = "inapp";
    public static final int BILLING_REQUEST_CODE = 3000;

    private Activity mActivity;
    private IInAppBillingService mService;
    private ServiceConnection mServiceConn;

    public InAppBillingHelper(Activity activity, final OnBillingServiceConnectedListener callback) {
        this.mActivity = activity;
        mServiceConn = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mService = null;
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mService = IInAppBillingService.Stub.asInterface(service);
                callback.onBillingServiceConnected();
            }
        };
        mActivity.bindService(new Intent("com.android.vending.billing.InAppBillingService.BIND"), mServiceConn,
                Context.BIND_AUTO_CREATE);
    }

    public IInAppBillingService getService() {
        return mService;
    }

    public ServiceConnection getServiceConn() {
        return mServiceConn;
    }

    public void purchaseItem(String productId) {
        try {
            Bundle buyIntentBundle = mService.getBuyIntent(3, mActivity.getPackageName(), productId,
                    IN_APP_PURCHASE_TYPE, "doobiididadoobiiidoodidadoodidaaaaaa");
            PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
            if (pendingIntent != null) {
                mActivity.startIntentSenderForResult(pendingIntent.getIntentSender(), BILLING_REQUEST_CODE,
                        new Intent(), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (SendIntentException e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        if (mServiceConn != null) {
            mActivity.unbindService(mServiceConn);
        }
    }

}
