package com.nalband.ribbit.util;

import com.nalband.ribbit.R;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.nalband.ribbit.ui.MainActivity;
import com.nalband.ribbit.ui.NotificaitonViewActivity;
import com.nalband.ribbit.utils.GlobalConstants;

public class AlarmReceiver extends BroadcastReceiver {

	public static final String TAG = AlarmReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "received alarm, firing notifications");
		fireGoalnotification(GlobalConstants.ID_GOAL1_NOTIFICATION, context, context.getString(R.string.goal_1_title),
				context.getString(R.string.goal_1_content));
		fireGoalnotification(GlobalConstants.ID_GOAL2_NOTIFICATION, context, context.getString(R.string.goal_2_title),
				context.getString(R.string.goal_2_content));
		fireGoalnotification(GlobalConstants.ID_GOAL3_NOTIFICATION, context, context.getString(R.string.goal_3_title),
				context.getString(R.string.goal_3_content));

	}

	private void fireGoalnotification(int id, Context context, String contentTitle, String ContentText) {

		// intent to start the activity upon notification click
		Intent clickResultIntent = new Intent(context, MainActivity.class);
		clickResultIntent.putExtra(GlobalConstants.KEY_GOAL, contentTitle);
		clickResultIntent.putExtra(GlobalConstants.KEY_EXTRA_INTENT_ID, id);
		Log.d(TAG, contentTitle + " id: " + id + " main intent");

		Intent actionPositiveResultIntent = new Intent(context, NotificationActionReceiver.class);
		actionPositiveResultIntent.putExtra(GlobalConstants.KEY_GOAL, contentTitle);
		actionPositiveResultIntent.putExtra(GlobalConstants.KEY_GOAL_STATUS, GlobalConstants.GOAL_DOMINATED);
		actionPositiveResultIntent.putExtra(GlobalConstants.KEY_EXTRA_INTENT_ID, id);
		Log.d(TAG, contentTitle + " id: " + id + " dominated intent");

		Intent actionNegativeResultIntent = new Intent(context, NotificationActionReceiver.class);
		actionNegativeResultIntent.putExtra(GlobalConstants.KEY_GOAL, contentTitle);
		actionNegativeResultIntent.putExtra(GlobalConstants.KEY_GOAL_STATUS, GlobalConstants.GOAL_FAILED);
		actionNegativeResultIntent.putExtra(GlobalConstants.KEY_EXTRA_INTENT_ID, id);
		Log.d(TAG, contentTitle + " id: " + id + " failed intent");

		// create a pending intent to start the activity upon user click
		// (Because clicking the notification opens a new ("special") activity,
		// there's no need to create an artificial back stack.)

		PendingIntent clickResultPendingIntent = PendingIntent.getActivity(context, id + 0, clickResultIntent,
				PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

		PendingIntent actionPositiveResultPendingIntent = PendingIntent.getBroadcast(context, id + 1,
				actionPositiveResultIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

		PendingIntent actionNegativeResultPendingIntent = PendingIntent.getBroadcast(context, id + 2,
				actionNegativeResultIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

		// choose which icon to attach to the notification based on type
		Bitmap bm;
		switch (id) {
		case GlobalConstants.ID_GOAL1_NOTIFICATION:
			bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.notification_icon_exercise_white_64);
			break;
		case GlobalConstants.ID_GOAL2_NOTIFICATION:
			bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.notification_icon_diet_white_64_fill);
			break;
		case GlobalConstants.ID_GOAL3_NOTIFICATION:
			bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.notification_icon_work_white_64);
			break;
		default:
			bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.notification_icon_trophy_white_64);
			break;
		}

		// the large icon
		// requires a bitmap

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.ic_stat_trophy_notifications).setLargeIcon(bm).setContentTitle(contentTitle)
				.setContentText(ContentText).setContentIntent(clickResultPendingIntent)
				// .setAutoCancel(true)
				.addAction(R.drawable.ic_stat_trophy_fill_512, "Dominated", actionPositiveResultPendingIntent)
				.addAction(R.drawable.ic_stat_unlike_512, "Failed", actionNegativeResultPendingIntent);
		NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotifyMgr.notify(id, mBuilder.build());
		Log.d(TAG, "ISSUED NOTIFICATION id: " + id);

	}
}
