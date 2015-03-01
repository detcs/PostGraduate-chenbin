package com.pages.funsquare.square;

import com.app.ydd.R;
import com.data.util.NetCall;
import com.data.util.NetCall.MsgCountCallback;
import com.data.util.SysCall;
import com.view.util.SquareAdapter;
import com.view.util.SquareAdapter.ViewHolder;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

public class SquareFragment extends Fragment implements MsgCountCallback {
	private View rootView;

	private ImageView backBu;
	private ImageView informBu;
	private ImageView red_dot;

	private ListView listView1;

	private Button publishBu;

	private SquareAdapter postAdapter;
	private SquareJump pubOrDetail;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		pubOrDetail = (SquareJump) activity;
	};

	@Override
	public void onResume() {
		super.onResume();
		initInform();
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		if (null == rootView) {
			rootView = inflater.inflate(R.layout.fragment_square, container,
					false);
		}
		init(rootView);
		return rootView;
	}

	// ******************init******************

	private void init(View view) {
		findViews(view);
		initListView();
		setListener();
		initInform();
	}

	private void findViews(View view) {
		listView1 = (ListView) view.findViewById(R.id.listView1);
		backBu = (ImageView) view.findViewById(R.id.backBu);
		publishBu = (Button) view.findViewById(R.id.publishBu);
		informBu = (ImageView) view.findViewById(R.id.informBu);
		red_dot = (ImageView) view.findViewById(R.id.red_dot);
	}

	private void initListView() {
		Context context = getActivity().getBaseContext();
		postAdapter = new SquareAdapter(context);
		listView1.setAdapter(postAdapter);
	}

	private void setListener() {
		backBu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SysCall.clickBack();
			}
		});
		publishBu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pubOrDetail.publish();
			}
		});

		informBu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pubOrDetail.inform();
			}
		});

		listView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ViewHolder holder = (ViewHolder) view.getTag();
				pubOrDetail.detail(holder.vg);
			}
		});
	}

	private void initInform() {
		NetCall.getMsgCount(this);
	}

	// NetCall.MsgCountCallback
	@Override
	public void getSuccess(int essence_, int square_) {
		// TODO Auto-generated method stub
		// informBu.setImageResource(resId);
		if (square_ > 0) {
			red_dot.setVisibility(View.VISIBLE);
		}
	}
}
