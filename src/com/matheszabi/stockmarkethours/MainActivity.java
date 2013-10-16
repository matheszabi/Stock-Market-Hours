package com.matheszabi.stockmarkethours;

import java.util.TimeZone;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

// http://en.wikipedia.org/wiki/List_of_market_opening_times
// take the local time and the timezone. Convert here to UTC
public class MainActivity extends Activity {

	private LabeledInterval[] mLabeledIntervals = new LabeledInterval[]{
			LabeledIntervalFactory.createLondon(),
			LabeledIntervalFactory.createNewYork()			
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initData();
	}

	private void initData() {

		String[] timezoneIds = TimeZone.getAvailableIDs();
		for (String tzid : timezoneIds) {
			Log.d("tzid", tzid);// Europe/London, America/New_York
		}


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public LabeledInterval[] getLabeledIntervals() {
		return mLabeledIntervals.clone();
	}

}
