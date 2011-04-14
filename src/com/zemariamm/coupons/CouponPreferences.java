package com.zemariamm.coupons;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class CouponPreferences {
	public static final String DB_PREFERENCES="DB_COUPON";
	public static SharedPreferences getPreferences(Context context) {
		int mode = Activity.MODE_PRIVATE;
		return context.getSharedPreferences(DB_PREFERENCES, mode);
	}
	
	public static boolean isUnblocked(Context context)
	{
		SharedPreferences prefs = CouponPreferences.getPreferences(context);
		return prefs.getBoolean("unblocked", false);
	}
	
	public static void markUnblocked(Context context)
	{
		SharedPreferences.Editor editor = CouponPreferences.getPreferences(context).edit();
		editor.putBoolean("unblocked",true);
		editor.commit();
	}
	
}
