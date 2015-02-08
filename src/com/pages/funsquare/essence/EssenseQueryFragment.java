package com.pages.funsquare.essence;

import java.util.ArrayList;
import java.util.List;

import com.app.ydd.R;
import com.data.model.Essense;
import com.data.util.GloableData;
import com.data.util.NetCall;
import com.data.util.SysCall;
import com.data.util.NetCall.RecommendKeysCallback;
import com.view.util.EssenseAdapter.ListDownEssense;
import com.view.util.EssenseQueryAdapter;
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
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class EssenseQueryFragment extends Fragment implements ListDownEssense,
		RecommendKeysCallback {
	// private String TAG = "EssenseQueryFragment";
	private View rootView;
	private EssenseJump jump;

	private EditText searchView;
	private View cleanView;
	private View searchBu;
	private View quitView;
	private ListView resultList;
	private ListView hintList;
	private EssenseQueryAdapter adapter;

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
		initOthers();
		setListener();
	}

	private void findViews(View view) {
		hintList = (ListView) view.findViewById(R.id.hintList);
		searchView = (EditText) view.findViewById(R.id.searchView);
		cleanView = view.findViewById(R.id.cleanView);
		searchBu = view.findViewById(R.id.searchBu);
		quitView = view.findViewById(R.id.quitView);
		resultList = (ListView) view.findViewById(R.id.resultList);
	}

	private void initOthers() {
		setHintList();
		SysCall.bumpSoftInput(searchView, getActivity());
	}

	private void initListView() {
		if (null == adapter) {
			adapter = new EssenseQueryAdapter(getActivity(),
					GloableData.TYPE_SEARCH, this);
			resultList.setAdapter(adapter);
		}
	}

	private void setListener() {

		cleanView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				searchView.setText("");
			}
		});

		searchBu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// save the search key
				// change the editText's background
				// show the query result
				String key = searchView.getText().toString();
				initListView();
				adapter.search(key);
				SysCall.error("save the search key");
			}
		});
		quitView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SysCall.clickBack();
			}
		});
		resultList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				jump(view);
			}
		});

		searchView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				String key = s.toString().trim();
				if (!s.equals("")) {
					NetCall.askRecommendKeys(key, EssenseQueryFragment.this);
				} else {
					setHintList();
				}
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
				String str = s.toString();
				if (str.length() > 5) {
					cleanView.setVisibility(View.VISIBLE);
					cleanView.setClickable(true);
					cleanView.setEnabled(true);
				} else {
					cleanView.setVisibility(View.INVISIBLE);
					cleanView.setClickable(false);
					cleanView.setEnabled(false);
				}
			}
		});
	}

	// if the key is "":show the search history
	private void setHintList() {
		// read search history
		final List<String> history = new ArrayList<String>();
		history.add("haha");
		history.add("hehe");
		hintList.setAdapter(new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				if (0 == position) {
					return LayoutInflater.from(getActivity()).inflate(
							R.layout.essense_query_hisroty_hint, parent, false);
				} else if (history.size() + 1 == position) {
					View his_bottom = LayoutInflater.from(getActivity())
							.inflate(R.layout.essense_query_clean_his_hint,
									parent, false);
					his_bottom.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							SysCall.error("clean history records");
						}
					});
					return his_bottom;
				} else {
					View view = LayoutInflater.from(getActivity()).inflate(
							R.layout.essense_query_hint_item, parent, false);
					TextView textView1 = (TextView) view
							.findViewById(R.id.textView1);
					textView1.setText(history.get(position - 1));
					return view;
				}
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public String getItem(int position) {
				// TODO Auto-generated method stub
				// if (0 == position || history.size() + 1 == position) {
				// return "";
				// }
				// return history.get(position - 1);
				return null;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return history.size() + 2;
			}
		});
	}

	// NetCall.RecommendKeysCallback
	@Override
	public void success(final List<String> keys) {
		// TODO Auto-generated method stub
		hintList.setAdapter(new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				ViewHolder holder;
				if (null == convertView) {
					convertView = LayoutInflater.from(getActivity()).inflate(
							R.layout.essense_query_hint_item, parent, false);
					holder = new ViewHolder();
					holder.text = (TextView) convertView
							.findViewById(R.id.textView1);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				holder.text.setText(getItem(position));
				return convertView;
			}

			class ViewHolder {
				TextView text;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public String getItem(int position) {
				// TODO Auto-generated method stub
				return keys.get(position);
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return keys.size();
			}
		});
	}

	private void jump(View view) {
		ViewHolder holder = (ViewHolder) view.getTag();
		String id = holder.id;
		jump.detail(id);
	}

	// EssenseAdapter.ListDownEssense
	@Override
	public void down(Essense e) {
		// TODO Auto-generated method stub

	}

}
