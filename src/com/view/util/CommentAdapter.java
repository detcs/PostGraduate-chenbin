package com.view.util;

import com.app.ydd.R;
import com.data.model.Comment;
import com.data.model.Post;
import com.data.util.CommentUtil;
import com.data.util.ComputeURL;
import com.data.util.DataBuffer;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter implements AdapterFresh {
	private Context context;
	private DataBuffer<Comment> buffer;
	// private String postId;// id of post
	private PostDetailCallback reply;
	private Post vg;

	public CommentAdapter(Context context, PostDetailCallback reply, Post vg) {
		this.context = context;
		// this.postId = postId;
		this.reply = reply;
		this.vg = vg;
		init();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return buffer.getCount() + 1;// 加上正文
	}

	@Override
	public Comment getItem(int position) {
		// TODO Auto-generated method stub
		if (0 == position)
			return null;
		return buffer.getDataItem(position - 1);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (0 == position) {
			return inflatePost(convertView, parent);
		} else {
			return inflateComment(position, convertView, parent);
		}
	}

	// **************init**************
	private void init() {
		buffer = new DataBuffer<Comment>(this, new CommentUtil(vg.getId()));
	}

	// AdapterFresh
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		buffer.destroy();
	}

	@Override
	public void fresh() {
		// TODO Auto-generated method stub
		buffer.reset();
	}

	// *************Call back*************
	public interface PostDetailCallback {
		public void reply(Comment comment, int position);

		public void moreChoice();

		public void share();
	}

	// ******************post******************
	private ImageView headView_p;
	private ImageView sexView_p;
	private TextView nickNameView_p;
	private TextView timeView_p;
	private TextView contentView_p;
	private ImageView shareView_p;
	private ImageView moreView_p;

	private View inflatePost(View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.square_post,
				parent, false);
		findPostViews(view);
		initPostViews();
		setPostListener();
		view.setClickable(false);
		view.setEnabled(false);
		return view;
	}

	private void findPostViews(View view) {
		sexView_p = (ImageView) view.findViewById(R.id.sexImage);
		headView_p = (ImageView) view.findViewById(R.id.headView);
		nickNameView_p = (TextView) view.findViewById(R.id.nickNameView);
		timeView_p = (TextView) view.findViewById(R.id.timeView);
		contentView_p = (TextView) view.findViewById(R.id.contentView);

		shareView_p = (ImageView) view.findViewById(R.id.shareView);
		moreView_p = (ImageView) view.findViewById(R.id.moreView);
	}

	private void initPostViews() {
		// sexView_p.setImageBitmap(null);
		String headimg = vg.getImgNo();
		Picasso.with(context).load(ComputeURL.getHeadImgURL(headimg))
				.resize(100, 100).placeholder(R.drawable.default_head)
				.error(R.drawable.default_head).into(headView_p);
		nickNameView_p.setText(vg.getAuthor());
		timeView_p.setText(vg.getTime());
		contentView_p.setText(vg.getContent());
	}

	private void setPostListener() {
		shareView_p.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				reply.share();
			}
		});
		moreView_p.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// bump more choice
				reply.moreChoice();
			}
		});
	}

	// *********************comments*********************

	private View inflateComment(final int position, View convertView,
			ViewGroup parent) {
		final Comment vg = getItem(position);
		// 如果没有拿到数据，先显示进度条
		if (null == vg) {
			return LayoutInflater.from(context).inflate(
					R.layout.list_item_wait, parent, false);
		}
		ViewHolder holder;
		if (null == convertView || null == convertView.getTag()) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.square_comment_item, parent, false);
			holder = new ViewHolder();
			holder.comment = vg;
			holder.replyBu = (Button) convertView.findViewById(R.id.replyBu);
			holder.headView = (ImageView) convertView
					.findViewById(R.id.headView);
			holder.nickName = (TextView) convertView
					.findViewById(R.id.nickNameView);
			holder.time = (TextView) convertView.findViewById(R.id.timeView);
			holder.contentView = (TextView) convertView
					.findViewById(R.id.commentView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.replyBu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				reply.reply(vg, position);
			}
		});
		Picasso.with(context)
				.load(ComputeURL.getSmallHeadImgURL(vg.getImgNo()))
				.placeholder(R.drawable.default_head)
				.error(R.drawable.default_head).resize(50, 50)
				.into(holder.headView);
		holder.nickName.setText(vg.getAuthor());
		holder.time.setText(vg.getTime());
		holder.contentView.setText(vg.getContent());
		return convertView;
	}

	public static class ViewHolder {
		public Comment comment;
		Button replyBu;
		ImageView headView;
		TextView nickName;
		TextView time;
		TextView contentView;
	}

}
