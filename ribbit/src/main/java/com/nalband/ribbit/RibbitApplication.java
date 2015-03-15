package com.nalband.ribbit;

import android.app.Application;

import com.nalband.ribbit.ui.MainActivity;
import com.nalband.ribbit.utils.ParseConstants;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.PushService;

public class RibbitApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		Parse.initialize(this, "BPSWuhswSiWhxy0ojQcYxU5AMhJPLQldau7Pe5sV", "4ZIAnjExVl2fPFcjSRdBt9Fd4wCN2xHGDq4EpCJd");
		ParseFacebookUtils.initialize(getString(R.string.app_id));
		//PushService.setDefaultPushCallback(this, MainActivity.class);
		
		}
	
	public static void updateParseInstallation(ParseUser user){
		ParseInstallation installation = ParseInstallation.getCurrentInstallation();
		installation.put(ParseConstants.KEY_USER_ID, user.getObjectId());
		installation.saveInBackground();
	}
}
