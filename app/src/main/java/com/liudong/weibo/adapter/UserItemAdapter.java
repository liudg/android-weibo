package com.liudong.weibo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liudong.weibo.R;
import com.liudong.weibo.entity.UserItem;

import java.util.ArrayList;

/**
 * Created by liudong on 2016/5/23.
 */
public class UserItemAdapter extends RecyclerView.Adapter<UserItemAdapter.ViewHolder> {

    private Context context;
    private ArrayList<UserItem> datas;

    public UserItemAdapter(Context context, ArrayList<UserItem> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //绑定数据
        UserItem item = datas.get(position);
        holder.iv_left.setImageResource(item.getLeftImg());
        holder.tv_subhead.setText(item.getSubhead());
        holder.tv_caption.setText(item.getCaption());
        holder.iv_divider.setVisibility(item.isShowTopDivider() ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_divider;
        private LinearLayout ll_content;
        private ImageView iv_left;
        private TextView tv_subhead;
        private TextView tv_caption;

        public ViewHolder(View itemView) {
            super(itemView);

            iv_divider = (ImageView) itemView.findViewById(R.id.iv_divider);
            ll_content = (LinearLayout) itemView.findViewById(R.id.ll_content);
            iv_left = (ImageView) itemView.findViewById(R.id.iv_left);
            tv_subhead = (TextView) itemView.findViewById(R.id.tv_subhead);
            tv_caption = (TextView) itemView.findViewById(R.id.tv_caption);
        }
    }
}
