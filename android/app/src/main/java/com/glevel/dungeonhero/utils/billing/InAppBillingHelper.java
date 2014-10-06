package com.glevel.dungeonhero.utils.billing;

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

import java.util.ArrayList;
import java.util.List;

public class InAppBillingHelper {

    public static final int BILLING_REQUEST_CODE = 3000;

    private static final int API_VERSION = 3;
    private static final String IN_APP_PURCHASE_TYPE = "in-app";
    private static final String DEVELOPER_PAYLOAD = "doobiididadoobiiidoodidadoodidaaaaaa";
    private static final String SERVICE_INTENT = "com.android.vending.billing.InAppBillingService.BIND";
    private static final String RESPONSE_CODE_KEY = "RESPONSE_CODE";
    private static final String RESPONSE_PURCHASE_ITEMS_KEY = "INAPP_PURCHASE_ITEM_LIST";
    private static final int RESPONSE_SUCCESS = 0;


    private Activity mActivity;
    private IInAppBillingService mService;
    private ServiceConnection mServiceConnection;

    public InAppBillingHelper(Activity activity, final OnBillingServiceConnectedListener callback) {
        mActivity = activity;
        mServiceConnection = new ServiceConnection() {
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
        Intent serviceIntent = new Intent(SERVICE_INTENT);
        serviceIntent.setPackage("com.android.vending");
        mActivity.bindService(serviceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void doIOwn(List<? extends InAppProduct> inAppProducts) {
        try {
            Bundle response = mService.getPurchases(API_VERSION, mActivity.getPackageName(), IN_APP_PURCHASE_TYPE, null);
            int responseCode = response.getInt(RESPONSE_CODE_KEY);
            if (responseCode == RESPONSE_SUCCESS) {
                ArrayList<String> ownedItems = response.getStringArrayList(RESPONSE_PURCHASE_ITEMS_KEY);
                for (InAppProduct inAppProduct : inAppProducts) {
                    if (!inAppProduct.isAvailable()) {
                        inAppProduct.setHasBeenBought(false);
                        for (String productId : ownedItems) {
                            if (inAppProduct.getProductId().equals(productId)) {
                                inAppProduct.setHasBeenBought(true);
                                break;
                            }
                        }
                    }
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void doIOwn(InAppProduct inAppProduct) {
        if (!inAppProduct.isAvailable()) {
            try {
                Bundle response = mService.getPurchases(API_VERSION, mActivity.getPackageName(), IN_APP_PURCHASE_TYPE, null);
                int responseCode = response.getInt(RESPONSE_CODE_KEY);
                if (responseCode == RESPONSE_SUCCESS) {
                    ArrayList<String> ownedItems = response.getStringArrayList(RESPONSE_PURCHASE_ITEMS_KEY);
                    inAppProduct.setHasBeenBought(false);
                    for (String productId : ownedItems) {
                        if (inAppProduct.getProductId().equals(productId)) {
                            inAppProduct.setHasBeenBought(true);
                            break;
                        }
                    }
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void purchaseItem(InAppProduct inAppProduct) {
        if (mService != null && !inAppProduct.isAvailable()) {
            try {
                Bundle buyIntentBundle = mService.getBuyIntent(API_VERSION, mActivity.getPackageName(), inAppProduct.getProductId(), IN_APP_PURCHASE_TYPE, DEVELOPER_PAYLOAD);
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
    }

    public void onDestroy() {
        if (mServiceConnection != null) {
            mActivity.unbindService(mServiceConnection);
            mActivity = null;
        }
    }

}
