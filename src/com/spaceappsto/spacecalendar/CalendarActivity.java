package com.spaceappsto.spacecalendar;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spaceappsto.spacecalendar.network.ObservationsHolder;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.CalendarPickerView.OnDateSelectedListener;
import com.squareup.timessquare.DotUtility;
import com.squareup.timessquare.objects.Satellite;

public class CalendarActivity extends Activity {
	private static final String TAG = "SampleTimesSquareActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_picker);

		populateCalendar();
		populateLegend();
	}

	private void populateLegend() {
		int numPerCol = (int) Math.ceil(ObservationsHolder.getSatellites().size() / 3.0);

		LinearLayout col1 = (LinearLayout) findViewById(R.id.legend_col1);
		fillColumn(numPerCol, 0, col1);

		LinearLayout col2 = (LinearLayout) findViewById(R.id.legend_col2);
		fillColumn(numPerCol, 1, col2);

		LinearLayout col3 = (LinearLayout) findViewById(R.id.legend_col3);
		fillColumn(numPerCol, 2, col3);
	}

	private void fillColumn(int numPerCol, int colIndex, LinearLayout col) {
		for (int i = 0; i < numPerCol; i++) {
			if (ObservationsHolder.getSatellites().size() > numPerCol * colIndex + i) {
				Satellite satellite = ObservationsHolder.getSatellites().get(numPerCol * colIndex + i);
				TextView legendLabel = (TextView) getLayoutInflater().inflate(R.layout.legend_text, null);
				legendLabel.setText(satellite.name);

				Drawable drawable = DotUtility.getDotWithColorIndex(this, satellite.id);
				legendLabel.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
				col.addView(legendLabel);
			} else
				break;
		}
	}

	private void populateCalendar() {
		Calendar startDate = Calendar.getInstance();
		startDate.add(Calendar.YEAR, -1);

		Calendar futureEnd = Calendar.getInstance();
		futureEnd.add(Calendar.YEAR, 1);

		final CalendarPickerView calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
		calendar.init(new Date(), startDate.getTime(), futureEnd.getTime(), ObservationsHolder.getObservations());

		calendar.setOnDateSelectedListener(new OnDateSelectedListener() {

			@Override
			public void onDateSelected(Date date) {
				Log.d(TAG, "Selected time in millis: " + calendar.getSelectedDate().getTime());
				startActivity(new Intent(CalendarActivity.this, DayActivity.class));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_calendar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_search:
			startActivity(new Intent(this, SearchActivity.class));
			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}
}
