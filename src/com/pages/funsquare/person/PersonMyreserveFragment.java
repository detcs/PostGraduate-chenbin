package com.pages.funsquare.person;

import com.app.ydd.R;
import com.data.model.Reserve;
import com.data.util.SysCall;
import com.pages.funsquare.essence.EssenseActivity;
import com.view.util.ReserveAdapter;
import com.view.util.ReserveAdapter.ViewHolder;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class PersonMyreserveFragment extends Fragment {
	private View rootView;
	private View backView;
	private ListView listView1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		if (null == rootView) {
			rootView = inflater.inflate(R.layout.fragment_person_reserve,
					container, false);
		}
		init(rootView);
		return rootView;
	}

	private void init(View view) {
		findViews(view);
		initListView();
		setListener();
	}

	private void findViews(View view) {
		backView = view.findViewById(R.id.backView);
		listView1 = (ListView) view.findViewById(R.id.listView1);
	}

	private void initListView() {
		listView1.setAdapter(new ReserveAdapter(getActivity()));
	}

	private void setListener() {
		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SysCall.clickBack();
			}
		});
		listView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ViewHolder holder = (ViewHolder) view.getTag();
				Reserve vg = holder.vg;
				String reserveid = vg.getId_();
				Intent intent = new Intent(getActivity(), EssenseActivity.class);
				intent.putExtra("essenseId", reserveid);
				startActivity(intent);
			}
		});
	}
}
