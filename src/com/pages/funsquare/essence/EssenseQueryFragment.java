package com.pages.funsquare.essence;

import com.app.ydd.R;
import com.view.util.EssenseAdapter.ViewHolder;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class EssenseQueryFragment extends Fragment {
	private View rootView;
	private EssenseJump jump;

	private AutoCompleteTextView searchView;
	private Button searchBu;
	private ListView listView1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		if (null == rootView) {
			rootView = inflater.inflate(R.layout.fragment_essense_query,
					container, false);
		}
		init(rootView);
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof EssenseJump)) {
			throw new IllegalStateException("error");
		}
		jump = (EssenseJump) activity;
	}

	private void init(View view) {
		findViews(view);
		initListView();
		setListener();
	}

	private void findViews(View view) {
		searchView = (AutoCompleteTextView) view.findViewById(R.id.searchView);
		searchBu = (Button) view.findViewById(R.id.searchBu);
		listView1 = (ListView) view.findViewById(R.id.listView1);
	}

	private void initListView() {
		listView1.setAdapter(null);
	}

	private void setListener() {
		searchView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				// hint better choices
			}
		});
		searchBu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// show the query result
			}
		});
		listView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				jump(view);
			}
		});
	}

	private void jump(View view) {
		ViewHolder holder = (ViewHolder) view.getTag();
		String id = holder.id;
		jump.detail(id);
	}
}
