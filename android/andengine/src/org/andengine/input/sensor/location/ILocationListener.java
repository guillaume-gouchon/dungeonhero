package org.andengine.input.sensor.location;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 10:39:23 - 31.10.2010
 */
public interface ILocationListener {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * @see {@link LocationListener#onProviderEnabled(String)}
	 */
    void onLocationProviderEnabled();

	/**
	 * @see {@link LocationListener#onLocationChanged(Location)}
	 */
    void onLocationChanged(final Location pLocation);

	void onLocationLost();

	/**
	 * @see {@link LocationListener#onProviderDisabled(String)}
	 */
    void onLocationProviderDisabled();

	/**
	 * @see {@link LocationListener#onStatusChanged(String, int, android.os.Bundle)}
	 */
    void onLocationProviderStatusChanged(final LocationProviderStatus pLocationProviderStatus, final Bundle pBundle);
}
