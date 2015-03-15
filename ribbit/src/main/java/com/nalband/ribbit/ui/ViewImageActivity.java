package com.nalband.ribbit.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.nalband.ribbit.R;
import com.nalband.ribbit.R.id;
import com.nalband.ribbit.R.layout;
import com.squareup.picasso.Picasso;

public class ViewImageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_image);
		
		ImageView imageView = (ImageView)findViewById(R.id.imageView);
		Uri image = getIntent().getData();
		Picasso.with(this).load(image.toString()).into(imageView);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				//back to inbox
				finish();
			}
		}, 10*1000); //10 seconds
		
		
		
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
	//	if (id == R.id.action_settings) {
	//		return true;
	// 	}
		return super.onOptionsItemSelected(item);
	}
}
