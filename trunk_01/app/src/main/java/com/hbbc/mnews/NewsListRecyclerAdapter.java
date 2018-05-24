package com.hbbc.mnews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hbbc.R;
import com.hbbc.mnews.bean.NewsListBeans;

import java.util.List;

/**
 * 新闻条目适配器
 */
public class NewsListRecyclerAdapter extends RecyclerView.Adapter<NewsListRecyclerAdapter.MyViewHolder> {


    private List<NewsListBeans.NewsListBean> list;

    private Context context;

    private ItemClick itemClick;



    public NewsListRecyclerAdapter(Context context, List<NewsListBeans.NewsListBean> list) {

        this.context = context;
        this.list = list;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.mnews_frag_item, parent, false);
        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.mnews_frgrec_title.setText(list.get(position).getHeadline());
        holder.mnews_frgrec_content.setText(list.get(position).getDigest());
        Glide.with(context).load(list.get(position).getPicFileID()).into(holder.mnews_frgrec_img);
        //获取时间长度大于16
        if (!list.get(position).getPublishTime().equals("")) {
            String date = list.get(position).getPublishTime().substring(0, 16);
            holder.mnews_frgrec_time.setText(date);
        }
        holder.mnews_frg_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itemClick.OnItemClick(holder.getAdapterPosition(), list.get(holder.getAdapterPosition()).getNewsContentID() + "");
            }
        });

    }



    @Override
    public int getItemCount() {

        return list.size();
    }



    @Override
    public int getItemViewType(int position) {
        //通过接受的数据来控制hotspot显示还是隐藏
        return super.getItemViewType(position);
    }



    /**
     * 设置监听事件（回调）
     */
    public void setOnItemClick(ItemClick itemClick) {

        this.itemClick = itemClick;
    }



    interface ItemClick {

        void OnItemClick(int position, String newsContentID);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mnews_frgrec_title, mnews_frgrec_content, mnews_frgrec_time;

        ImageView mnews_frgrec_img;

        Button mnews_frgrec_hotspot;


        RelativeLayout mnews_frg_item;



        public MyViewHolder(View itemView) {

            super(itemView);
            mnews_frg_item = (RelativeLayout) itemView.findViewById(R.id.mnews_frg_item);
            mnews_frgrec_title = (TextView) itemView.findViewById(R.id.mnews_frgrec_title);
            mnews_frgrec_content = (TextView) itemView.findViewById(R.id.mnews_frgrec_content);
            mnews_frgrec_time = (TextView) itemView.findViewById(R.id.mnews_frgrec_time);
            mnews_frgrec_img = (ImageView) itemView.findViewById(R.id.mnews_frgrec_img);
            mnews_frgrec_hotspot = (Button) itemView.findViewById(R.id.mnews_frgrec_hotspot);
        }
    }
}
