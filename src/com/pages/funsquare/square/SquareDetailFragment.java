package com.pages.funsquare.square;

import com.app.ydd.R;
import com.data.model.Comment;
import com.data.model.Post;
import com.data.util.NetCall;
import com.data.util.SysCall;
import com.data.util.NetCall.UploadData;
import com.pages.funsquare.square.SquarePostShareBump.PostShareCallBack;
import com.view.util.AdapterFresh;
import com.view.util.CommentAdapter;
import com.view.util.CommentAdapter.PostDetailCallback;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class SquareDetailFragment extends Fragment implements
		PostDetailCallback, UploadData, PostShareCallBack {
	private static final String TAG = "SquareDetailFragment";
	private View base;
	private FrameLayout frame;

	private View rootView;
	private SquareJump jump;
	// head bar
	private ImageView backBu;
	private ImageView informBu;
	// show zone
	private ListView listView1;
	// comment bar
	private EditText editText;
	private Button commentBu;

	private CommentAdapter commentAdapter;
	private Post vg;
	// comment
	private String userId;
	private String replyHead;

	public SquareDetailFragment() {
		// TODO Auto-generated constructor stub
	}

	public SquareDetailFragment(Post vg) {
		// TODO Auto-generated constructor stub
		this.vg = vg;
	}

	@Override
	public void onAttach(android.app.Activity activity) {
		super.onAttach(activity);
		jump = (SquareJump) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		if (null == base) {
			base = inflater.inflate(R.layout.frame, container, false);
			frame = (FrameLayout) base.findViewById(R.id.FrameLayout1);
		}
		if (null == rootView) {
			rootView = inflater.inflate(R.layout.fragment_square_detail,
					container, false);
		}
		init(rootView);
		frame.addView(rootView);
		return base;
	}

	@Override
	public void onStop() {
		commentAdapter.destroy();
		super.onStop();
	}

	// ***************init***************
	private void init(View view) {
		findViews(view);
		initListView();
		setListener();
	}

	private void findViews(View view) {
		backBu = (ImageView) view.findViewById(R.id.backBu);
		informBu = (ImageView) view.findViewById(R.id.informBu);
		listView1 = (ListView) view.findViewById(R.id.listView1);
		commentBu = (Button) view.findViewById(R.id.commentBu);
		editText = (EditText) view.findViewById(R.id.editText);
	}

	private void initListView() {
		commentAdapter = new CommentAdapter(getActivity(), vg.getId(), this, vg);
		listView1.setAdapter(commentAdapter);
	}

	private void setListener() {
		backBu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SysCall.clickBack();
			}
		});
		informBu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				jump.inform();
			}
		});

		commentBu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String content = editText.getText().toString();
				if (null == content) {
					Log.i(TAG, content + "");
				}
				if (!content.startsWith(replyHead + "")) {
					userId = "";
				}
				NetCall.comment(vg.getId(), content, userId,
						SquareDetailFragment.this);
			}
		});
	}

	// CommentAdapter.PostDetailCallback
	@Override
	public void reply(Comment comment, int position) {
		// TODO Auto-generated method stub
		this.userId = comment.getUserId();
		if (position != 0) {
			replyHead = "回复" + comment.getAuthor();
			editText.setText(replyHead);
			SysCall.bumpSoftInput(editText, getActivity());
		}
	}

	@Override
	public void moreChoice() {
		// TODO Auto-generated method stub
		rootView.clearFocus();
		if (null == frame.findViewWithTag(TAG)) {
			new SquarePostShareBump(frame, getActivity(), TAG, this).show();
		}
	}

	@Override
	public void share() {
		// TODO Auto-generated method stub
		rootView.clearFocus();
	}

	// 这两个方法仅仅是为了通知用户的，真正的通知本地数据刷新不在这里做
	@Override
	public void updateSuccess() {
		// TODO Auto-generated method stub
		SysCall.hint(getActivity(), "评论成功");
		((AdapterFresh) commentAdapter).destroy();
		editText.setText("");
	}

	@Override
	public void updatetFail() {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "评论失败", Toast.LENGTH_SHORT).show();
	}

	// SquarePostShareBump.PostShareCallBack
	@Override
	public void shareSuccess() {
		// TODO Auto-generated method stub

	}

	@Override
	public void shareFail() {
		// TODO Auto-generated method stub

	}

}
