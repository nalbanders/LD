package com.nalband.ribbit.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.nalband.ribbit.R;
import com.nalband.ribbit.utils.ParseConstants;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class NotificaitonViewActivity extends Activity {
	
	
	public static final String KEY_LOG_ENTRY= "com.nalband.ribbit.ui.NotificationViewActivity.logEntry";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//hide the action bar
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
	    getActionBar().hide();
       
	    //show the view
		setContentView(R.layout.activity_notification_action);
		
	}


}



/*
 * Intent myIntent = new Intent; AlarmManager alarmManager =
 * (AlarmManager)getSystemService(ALARM_SERVICE); pendingIntent =
 * PendingIntent.getService(this, 0, myIntent, 0);
 * 
 * Calendar calendar = Calendar.getInstance(); calendar.set(Calendar.SECOND, 0);
 * calendar.set(Calendar.MINUTE, 0); calendar.set(Calendar.HOUR, 0);
 * calendar.set(Calendar.AM_PM, Calendar.AM);
 * calendar.add(Calendar.DAY_OF_MONTH, 1);
 * 
 * alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
 * calendar.getTimeInMillis(), 1000*60*60*24 , pendingIntent);
 */