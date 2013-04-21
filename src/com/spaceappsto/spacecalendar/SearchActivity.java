package com.spaceappsto.spacecalendar;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.spaceappsto.spacecalendar.adapter.SearchAdapter;
import com.spaceappsto.spacecalendar.network.ObservationsHolder;
import com.spaceappsto.spacecalendar.utility.RaDecUtility;
import com.squareup.timessquare.objects.Observation;

public class SearchActivity extends Activity {

	private Dialog coordsDialog;
	private TextView ra_text;
	private TextView dec_text;
	private String ra = "00:00:00.00";
	private String dec = "+000:00:00.00";

	private ArrayList<Observation> obsSearch;
	private SearchAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);

		obsSearch = new ArrayList<Observation>(ObservationsHolder.getObservations());

		setUpRaDec();
		setUpListView();
		setUpDialog();
	}

	private void setUpRaDec() {
		ra_text = (TextView) findViewById(R.id.ra_text);
		dec_text = (TextView) findViewById(R.id.dec_text);
	}

	private void setUpListView() {
		ListView listView = (ListView) findViewById(R.id.listview);
		adapter = new SearchAdapter(this, 0, 0, obsSearch);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent(SearchActivity.this, ObservationActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable(ObservationActivity.BUNDLE_KEY, obsSearch.get(arg2));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}

	private void search() {
		obsSearch.clear();

		double raVal = 0;
		double decVal = 0;

		try {
			raVal = RaDecUtility.parseRAToRadians(ra);
			decVal = RaDecUtility.parseDecToRadians(dec);
		} catch (IllegalStateException e) {
			Log.e("RA/DEC", "Could not parse ra/dec " + e.getMessage());
			return;
		}

		for (Observation o : ObservationsHolder.getObservations()) {
			try {
				double oRa = RaDecUtility.parseRAToRadians(o.target.ra);
				double oDec = RaDecUtility.parseDecToRadians(o.target.dec);
				Log.d("Han", "Search ra: " + raVal + " Target ra: " + oRa);
				Log.d("Han", "Search dec: " + decVal + " Target dec: " + oDec);
				if (RaDecUtility.compareRadians(raVal, oRa) && RaDecUtility.compareRadians(decVal, oDec)) {
					obsSearch.add(o);
				}
			} catch (IllegalStateException e) {
				Log.e("RA/DEC", "Could not parse ra/dec " + e.getMessage());
			}
		}
		
		adapter.setList(obsSearch);
		adapter.notifyDataSetChanged();
	}

	private void setUpDialog() {
		coordsDialog = new Dialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
		coordsDialog.setContentView(R.layout.coords_dialog);
		coordsDialog.findViewById(R.id.dialog_button).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ra = ((EditText) coordsDialog.findViewById(R.id.ra_1)).getText().toString() + ":";
				ra += ((EditText) coordsDialog.findViewById(R.id.ra_2)).getText().toString() + ":";
				ra += ((EditText) coordsDialog.findViewById(R.id.ra_3)).getText().toString() + ".";
				ra += ((EditText) coordsDialog.findViewById(R.id.ra_4)).getText().toString();

				dec = ((EditText) coordsDialog.findViewById(R.id.dec_1)).getText().toString() + ":";
				dec += ((EditText) coordsDialog.findViewById(R.id.dec_2)).getText().toString() + ":";
				dec += ((EditText) coordsDialog.findViewById(R.id.dec_3)).getText().toString() + ".";
				dec += ((EditText) coordsDialog.findViewById(R.id.dec_4)).getText().toString();

				ra_text.setText("RA " + ra);
				dec_text.setText("DEC " + dec);
				coordsDialog.dismiss();
				search();
			}
		});
	}

	public void onClickEditSearch(View view) {
		coordsDialog.show();
	}
}
