package com.nalband.ribbit.ui;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nalband.ribbit.R;
import com.nalband.ribbit.util.AlarmReceiver;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class NativeLoginActivity extends Activity {

	
	public static final String TAG = NativeLoginActivity.class.getSimpleName();
	protected TextView mSignUpTextView;
	protected EditText mUsername;
	protected EditText mPassword;
	protected Button mLogInButton;
	private Button mFbLoginButton;
	private Dialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_login);

		ActionBar actionBar = getActionBar();
		actionBar.hide();

		mFbLoginButton = (Button) findViewById(R.id.fbLoginButton);
		mFbLoginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onLoginButtonClicked();
			}
		});
		
		// Check if there is a currently logged in user
		// and they are linked to a Facebook account.
		ParseUser currentUser = ParseUser.getCurrentUser();
		if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
			// Go to the user info activity
			showUserDetailsActivity();
		}

	}

	private void onLoginButtonClicked() {
		//NativeLoginActivity.this.progressDialog = ProgressDialog.show(NativeLoginActivity.this, "", "Logging in...", true);
		List<String> permissions = Arrays.asList("public_profile", "user_friends");
		ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException err) {
				//NativeLoginActivity.this.progressDialog.dismiss();
				if (user == null) {
					Log.d(TAG, "Uh oh. The user cancelled the Facebook login.");
				} else if (user.isNew()) {
					showUserDetailsActivity();
				} else {
					Log.d(TAG, "User logged in through Facebook!");
					showUserDetailsActivity();
				}
			}
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}
	
	private void showUserDetailsActivity() {
		setNotificationSchedule(); // schedule daily notifications, run only once	
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		Log.d(TAG, "Started main");
	}

	private void setNotificationSchedule() {
		//sets a recurring alarm which triggers notifications every 24 hrs at the time indicated below
		Log.d(TAG, "setting alarm from" + TAG);
		Intent myIntent = new Intent(this, AlarmReceiver.class);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 9);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 00);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000*60*60*24, pendingIntent);
		Toast.makeText(this, "Daily notifications set for 9AM", Toast.LENGTH_SHORT).show();
	}
}


 