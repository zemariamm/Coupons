package com.zemariamm.coupons;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

public class Coupon {
	
	private static String getPackageName(Context context) {
		String packageName = "";
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			packageName = info.packageName;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return packageName;
	}
	// "To view this game you must have the full version"
	public static void createForbiddenDialog(final ProcessCoupon parent, final String warning, final String packageNameFull ) {
		final Context context = (Context) parent;
		final ProcessCoupon cparent = (ProcessCoupon) parent;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(warning)
		.setPositiveButton("Buy", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				String mlink = "market://search?q=pname:" + packageNameFull;
				Uri uri = Uri.parse(mlink);
				Intent intent = new Intent(Intent.ACTION_VIEW,uri);
				context.startActivity(intent);
			}
		})
		.setNeutralButton("Use coupon", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
				cparent.launchCoupon();
			}
		})
		.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public static void createCouponDialog(final Context context,final ProcessCoupon parent,final String appname, final String host) {
		createCouponDialog(context,parent,appname,host,"default");
	}
	public static void createCouponDialog(final Context context,final ProcessCoupon parent,final String appname, final String host, final String deviceId) {
		AlertDialog.Builder builderInvite = new AlertDialog.Builder(context);
		builderInvite.setMessage("Insert Coupon");
		final EditText coupon = new EditText(context);
		builderInvite.setView(coupon);
		builderInvite.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				AsyncTask<String,Void,Transporter> task = new AsyncTask<String, Void, Transporter>() {
					@Override
		            protected Transporter doInBackground(String...params) {
						String coupon = params[0];
						String appname = params[1];
						String device = params[3];
						String message = "Coupon not valid";
						boolean result = false;
						
						// url must be: http://droidcoupons.appspot.com/check/
						if (coupon == null)
							return new Transporter(false, message);
						String url = params[2] + appname + "/" + coupon + "?deviceid=" + device;
						Log.d("Coupons","Calling url:" + url);
						try {
							String answer = RestClient.connect(url);
							if (answer == null)
								throw new Exception("No internet connection available at the moment");
							try {
								JSONObject result_json = new JSONObject(answer);
								result = result_json.getBoolean("result");
								if (result)
									message = "You have unlocked " + appname + " !";
							}catch (JSONException e) {
								throw new Exception(message);
							}
						}
						catch (IllegalArgumentException e) {
							Log.d("Coupon message", e.getMessage());
						}
						catch (Exception e) {
							//e.printStackTrace();
							Log.d("Coupon message", e.getMessage());
							e.printStackTrace();
							message = e.getMessage();
						}
						return new Transporter(result, message);
				}
					
					@Override
					protected void onPostExecute(Transporter result) {
						if (result == null) 
						{
							parent.processInvalidCoupon();
							launchInvalidCouponDialog(context,"Coupon not valid");
						}
						else if (!result.isValid())
						{
							parent.processInvalidCoupon();
							launchInvalidCouponDialog(context,result.getMessage());
						}
							else
						{
							parent.processValidCoupon();
							launchConfirmationDialog(context,result.getMessage());
						}
					}
				};	
				task.execute(coupon.getText().toString(),appname,host,"teste");
			}
		}).setNegativeButton("Cancel", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builderInvite.create().show();
	}
	public static void launchConfirmationDialog(Context context, final String message) {
		try{
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage(message).setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
				}
			});
			builder.create();
			builder.show();
		}
		catch(Exception e){e.printStackTrace();}
	}
	public static void launchInvalidCouponDialog(Context context, String message) {
		try{
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage(message).setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
				}
			});
			builder.create();
			builder.show();
		}
		catch(Exception e){e.printStackTrace();}
	}
	
}
