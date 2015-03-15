package com.nalband.ribbit.util;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.nalband.ribbit.utils.GlobalConstants;
import com.nalband.ribbit.utils.ParseConstants;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class NotificationActionReceiver extends BroadcastReceiver {

	protected String mGoal;
	protected String mGoalStatus;

	public static final String TAG = NotificationActionReceiver.class.getSimpleName();
	
	@Override
	public void onReceive(Context context, Intent intent) {

		int notificationId = intent.getExtras().getInt(GlobalConstants.KEY_EXTRA_INTENT_ID);
		
		
		mGoal = intent.getExtras().getString(GlobalConstants.KEY_GOAL);
		mGoalStatus = intent.getExtras().getString(GlobalConstants.KEY_GOAL_STATUS);

		Log.d(TAG, "received action" + " id: " + notificationId + " goal:" + mGoal + " status:" + mGoalStatus);
		
		ParseObject logEntry = createLogEntry(mGoal, mGoalStatus);		
		saveLogEntry(logEntry);
        
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(notificationId);
        Log.d(TAG, "cancelling notification " + " id: " + notificationId);

	}

	private void saveLogEntry(ParseObject logEntry) {

		logEntry.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				if (e == null) {
					// success
					// Toast.makeText(context,
					// R.string.intro_button_test_message,
					// Toast.LENGTH_LONG).show();
					// sendPushNotifications();
				} else {
					// sync error
					// TODO: handle the sync error by saving and uploading later
					/*
					 * AlertDialog.Builder builder = new
					 * AlertDialog.Builder(context);
					 * builder.setMessage(R.string.error_saving_log_message)
					 * .setTitle(R.string.error_saving_log_title)
					 * .setPositiveButton(android.R.string.ok, null);
					 * AlertDialog dialog = builder.create(); dialog.show();
					 */
				}

			}
		});
	}

	protected ParseObject createLogEntry(String goal, String status) {

		ParseUser user = ParseUser.getCurrentUser();
		String userId = ParseUser.getCurrentUser().getObjectId();
		String username = user.getString("username");
				
		ParseObject logEntry = new ParseObject(ParseConstants.CLASS_LOG_ENTRY);
		logEntry.put(GlobalConstants.KEY_GOAL_STATUS, status);
		logEntry.put(ParseConstants.KEY_USER_ID, userId);
		logEntry.put(ParseConstants.KEY_USERNAME, username);
		logEntry.put(GlobalConstants.KEY_GOAL, goal);
		return logEntry;
	}

}
