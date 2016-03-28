package org.ninehertzindia.clipped.footerfragment;

import java.util.ArrayList;
import java.util.List;

import org.ninehertzindia.clipped.R;
import org.ninehertzindia.clipped.adapter.HistoryAdapter;
import org.ninehertzindia.clipped.database.DatabaseHandler;
import org.ninehertzindia.clipped.util.CommonMethods;
import org.ninehertzindia.clipped.util.HistoryVO;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint({ "NewApi", "ResourceAsColor" })
public class HistoryFragment extends Fragment {

	private View view;
	private Activity activity;
	private CommonMethods commonMethods;
	private TextView middletxt;
	private HistoryAdapter historyAdapter;
	private ListView lv_history;
	private DatabaseHandler db;
	private List<HistoryVO> HistoryVo;
	private TextView noHistoryTV;

	public static HistoryFragment newInstance() {
		HistoryFragment fragment = new HistoryFragment();
		return fragment;
	}

	public HistoryFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.history_fragment, container, false);
		setBodyUI();
		return view;
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void setBodyUI() {
		
		activity = getActivity();
		db = new DatabaseHandler(activity);
		middletxt = (TextView) view.findViewById(R.id.middletxt);
		middletxt.setText("Recent History");
		lv_history = (ListView) view.findViewById(R.id.lv_history);
		noHistoryTV = (TextView) view.findViewById(R.id.noHistoryTV);
		commonMethods = new CommonMethods(activity, this);
	}

	@Override
	public void onResume() {
		// commonMethods.defaultActionBarProcess("SEARCH", "EXPLORE", "",
		// "MENU");
		super.onResume();

		HistoryVo = db.getAllHistoryVOs();

		if (HistoryVo.size() < 1) {

			noHistoryTV.setVisibility(View.VISIBLE);
		} else {
			historyAdapter = new HistoryAdapter(activity, HistoryVo);
			lv_history.setAdapter(historyAdapter);
		}

	}

}
