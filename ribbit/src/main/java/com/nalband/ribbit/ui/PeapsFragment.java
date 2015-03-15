package com.nalband.ribbit.ui;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nalband.ribbit.R;
import com.nalband.ribbit.utils.GlobalConstants;
import com.nalband.ribbit.utils.ParseConstants;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class PeapsFragment extends Fragment {

	protected List<ParseObject> mExercisesDominated;
	protected List<ParseObject> mExerciseResults;
	protected List<ParseObject> mDietResults;
	protected List<ParseObject> mWorkResults;
	private TextView mExerciseCount;
	private TextView mDietCount;
	private TextView mWorkCount;
	private ProgressBar mDietBar;
	private ProgressBar mExerciseBar;
	private ProgressBar mWorkBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_peaps, container, false);

		mExerciseCount = (TextView) rootView.findViewById(R.id.userResults1);
		mExerciseCount.setText("");

		mDietCount = (TextView) rootView.findViewById(R.id.userResults2);
		mDietCount.setText("");

		mWorkCount = (TextView) rootView.findViewById(R.id.userResults3);
		mWorkCount.setText("");

		mExerciseBar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
		mExerciseBar.setMax(100);

		mDietBar = (ProgressBar) rootView.findViewById(R.id.progressBar2);
		mDietBar.setMax(100);

		mWorkBar = (ProgressBar) rootView.findViewById(R.id.progressBar3);
		mWorkBar.setMax(100);

		// mProgressBar = (ProgressBar)
		// rootView.findViewById(R.id.progressBarToday);
		/*
		 * mTotalCount = (TextView) rootView.findViewById(R.id.userTotal);
		 * mTotalCount.setText("");
		 */
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();

		getActivity().setProgressBarIndeterminateVisibility(true);

		ParseQuery<ParseObject> exerciseQuery = new ParseQuery<ParseObject>(ParseConstants.CLASS_LOG_ENTRY);
		exerciseQuery.whereEqualTo(ParseConstants.KEY_USER_ID, ParseUser.getCurrentUser().getObjectId());
		exerciseQuery.whereEqualTo(GlobalConstants.KEY_GOAL, getString(R.string.goal_1_title));

		ParseQuery<ParseObject> dietQuery = new ParseQuery<ParseObject>(ParseConstants.CLASS_LOG_ENTRY);
		dietQuery.whereEqualTo(ParseConstants.KEY_USER_ID, ParseUser.getCurrentUser().getObjectId());
		dietQuery.whereEqualTo(GlobalConstants.KEY_GOAL, getString(R.string.goal_2_title));

		ParseQuery<ParseObject> workQuery = new ParseQuery<ParseObject>(ParseConstants.CLASS_LOG_ENTRY);
		workQuery.whereEqualTo(ParseConstants.KEY_USER_ID, ParseUser.getCurrentUser().getObjectId());
		workQuery.whereEqualTo(GlobalConstants.KEY_GOAL, getString(R.string.goal_3_title));

		exerciseQuery.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> exerciseResults, ParseException e) {
				getActivity().setProgressBarIndeterminateVisibility(false);
				if (e == null) { // found messages
					int[] resultValues = tallyResults(exerciseResults);

					String days_text = " days";
					if (resultValues[GlobalConstants.SUCCESSFUL_DAYS] == 1){
						days_text=" day";
					}
					
					mExerciseCount.setText(String.valueOf(resultValues[GlobalConstants.SUCCESSFUL_DAYS]) 
							//+ "/" + String.valueOf(resultValues[GlobalConstants.TOTAL_DAYS]) 
							+ days_text);
					mExerciseBar.setProgress(resultValues[GlobalConstants.SUCCESS_RATE]);
				}
			}
		});

		dietQuery.findInBackground(new FindCallback<ParseObject>() {

			
			
			@Override
			public void done(List<ParseObject> dietResults, ParseException e) {
				getActivity().setProgressBarIndeterminateVisibility(false);
				if (e == null) {
					// found messages
					int[] resultValues = tallyResults(dietResults);
					
					String days_text = " days";
					if (resultValues[GlobalConstants.SUCCESSFUL_DAYS] == 1){
						days_text=" day";
					}
					
					mDietCount.setText(String.valueOf(resultValues[GlobalConstants.SUCCESSFUL_DAYS]) 
							//+ "/" + String.valueOf(resultValues[GlobalConstants.TOTAL_DAYS]) 
							+ days_text);
					mDietBar.setProgress(resultValues[GlobalConstants.SUCCESS_RATE]);
				}
			}
		});

		workQuery.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> workResults, ParseException e) {
				getActivity().setProgressBarIndeterminateVisibility(false);
				if (e == null) {
					// found messages
					int[] resultValues = tallyResults(workResults);

					String days_text = " days";
					if (resultValues[GlobalConstants.SUCCESSFUL_DAYS] == 1){
						days_text=" day";
					}
					
					mWorkCount.setText(String.valueOf(resultValues[GlobalConstants.SUCCESSFUL_DAYS]) 
							//+ "/" + String.valueOf(resultValues[GlobalConstants.TOTAL_DAYS]) 
							+ days_text);
					mWorkBar.setProgress(resultValues[GlobalConstants.SUCCESS_RATE]);
				}
			}
		});

		/*
		 * Animation an = new RotateAnimation(0.0f, 90.0f, 100f, 100f);
		 * //RotateAnimation(0.0f, 90.0f, Animation.RELATIVE_TO_SELF, 0,
		 * Animation.RELATIVE_TO_SELF, 0); an.setFillAfter(true);
		 * mProgressBar.startAnimation(an); // 0.0f, 90.0f, 250f, 273f
		 */

	}

	public int[] tallyResults(List<ParseObject> resultsList) {
		int[] results = { 0, 0, 0 };
		int days = resultsList.size();
		int days_dominated = 0;
		int days_failed = 0;
		ParseObject log;

		int i = 0;
		for (i = 0; i < resultsList.size(); i++) {
			log = resultsList.get(i);
			if (log.getString(GlobalConstants.KEY_GOAL_STATUS).equals(GlobalConstants.GOAL_DOMINATED)) {
				days_dominated++;
			} else if (log.getString(GlobalConstants.KEY_GOAL_STATUS).equals(GlobalConstants.GOAL_FAILED)) {
				days_failed++;
			} else {
				Log.d("error", "invalid result for exercise goal");
			}
		}

		int days_total = days_dominated + days_failed;
		int success_rate_int = (int) Math.round(((float) days_dominated / (float) days_total) * 100);
		results[GlobalConstants.SUCCESSFUL_DAYS] = days_dominated;
		results[GlobalConstants.TOTAL_DAYS] = days_total;
		results[GlobalConstants.SUCCESS_RATE] = success_rate_int;

		return results;
	}
}
