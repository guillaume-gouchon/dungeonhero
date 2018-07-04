package org.andengine.util.levelstats;

import android.content.Context;
import android.content.SharedPreferences;

import org.andengine.util.call.Callback;
import org.andengine.util.math.MathUtils;
import org.andengine.util.preferences.SimplePreferences;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 21:13:55 - 18.10.2010
 */
class LevelStatsDBConnector {
    // ===========================================================
    // Constants
    // ===========================================================

    private static final String PREFERENCES_LEVELSTATSDBCONNECTOR_PLAYERID_ID = "preferences.levelstatsdbconnector.playerid";

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    public LevelStatsDBConnector(final Context pContext, final String pSecret, final String pSubmitURL) {
        final SharedPreferences simplePreferences = SimplePreferences.getInstance(pContext);
        final int playerID = simplePreferences.getInt(PREFERENCES_LEVELSTATSDBCONNECTOR_PLAYERID_ID, -1);
        int mPlayerID;
        if (playerID != -1) {
        } else {
            mPlayerID = MathUtils.random(1000000000, Integer.MAX_VALUE);
            SimplePreferences.getEditorInstance(pContext).putInt(PREFERENCES_LEVELSTATSDBCONNECTOR_PLAYERID_ID, mPlayerID).commit();
        }
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    public void submitAsync(final int pLevelID, final boolean pSolved, final int pSecondsElapsed) {
        this.submitAsync(pLevelID, pSolved, pSecondsElapsed, null);
    }

    private void submitAsync(final int pLevelID, final boolean pSolved, final int pSecondsElapsed, final Callback<Boolean> pCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                try {
//                    /* Create a new HttpClient and Post Header. */
//                    final HttpClient httpClient = new DefaultHttpClient();
//                    final HttpPost httpPost = new HttpPost(LevelStatsDBConnector.this.mSubmitURL);
//
//                    /* Append POST data. */
//                    final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
//
//                    nameValuePairs.add(new BasicNameValuePair("level_id", String.valueOf(pLevelID)));
//                    nameValuePairs.add(new BasicNameValuePair("solved", (pSolved) ? "1" : "0"));
//                    nameValuePairs.add(new BasicNameValuePair("secondsplayed", String.valueOf(pSecondsElapsed)));
//                    nameValuePairs.add(new BasicNameValuePair("player_id", String.valueOf(LevelStatsDBConnector.this.mPlayerID)));
//                    nameValuePairs.add(new BasicNameValuePair("secret", String.valueOf(LevelStatsDBConnector.this.mSecret)));
//
//                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//                    /* Execute HTTP Post Request. */
//                    final HttpResponse httpResponse = httpClient.execute(httpPost);
//
//                    final int statusCode = httpResponse.getStatusLine().getStatusCode();
//                    if (statusCode == HttpStatus.SC_OK) {
//                        final String response = StreamUtils.readFully(httpResponse.getEntity().getContent());
//
//                        if (response.equals("<success/>")) {
//                            if (pCallback != null) {
//                                pCallback.onCallback(true);
//                            }
//                        } else {
//                            if (pCallback != null) {
//                                pCallback.onCallback(false);
//                            }
//                        }
//                    } else {
//                        if (pCallback != null) {
//                            pCallback.onCallback(false);
//                        }
//                    }
//                } catch (final IOException e) {
//                    Debug.e(e);
//                    if (pCallback != null) {
//                        pCallback.onCallback(false);
//                    }
//                }
            }
        }).start();
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
