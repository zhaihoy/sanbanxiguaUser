package com.hbbc.marti;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hbbc.R;
import com.hbbc.marti.bean.MartiListBeans;

import java.util.List;

/**
 * 文章条目适配器
 */
public class MartiListRecyclerAdapter extends RecyclerView.Adapter<MartiListRecyclerAdapter.MyArtiViewHolder> {

    private List<MartiListBeans.ArticleListBean> t;

    private Context context;


    private ArtiItemClick artiItemClick;



    MartiListRecyclerAdapter(Context context, List<MartiListBeans.ArticleListBean> t) {

        this.context = context;
        this.t = t;
    }



    public void setOnArtiItemClick(ArtiItemClick artiItemClick) {

        this.artiItemClick = artiItemClick;
    }



    @Override
    public MyArtiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.marti_frag_item, parent, false);
        return new MyArtiViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final MyArtiViewHolder holder, int position) {

        holder.marti_frgrec_content.setText(t.get(position).getDigest());
        holder.marti_frgrec_title.setText(t.get(position).getHeadline());
        Glide.with(context).load(t.get(position).getPicFileID()).into(holder.marti_frgrec_img);
        holder.marti_frg_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                artiItemClick.setOnArtiItemClick(holder.getAdapterPosition(), t.get(holder.getAdapterPosition()).getArticleContentID() + "");
            }
        });
    }



    @Override
    public int getItemCount() {

        return t.size();
    }



    public interface ArtiItemClick {

        void setOnArtiItemClick(int position, String ArticleContentID);
    }

    class MyArtiViewHolder extends RecyclerView.ViewHolder {

        ImageView marti_frgrec_img;

        RelativeLayout marti_frg_item;

        TextView marti_frgrec_title;

        TextView marti_frgrec_content;



        public MyArtiViewHolder(View itemView) {

            super(itemView);

            marti_frgrec_img = (ImageView) itemView.findViewById(R.id.marti_frgrec_img);
            marti_frg_item = (RelativeLayout) itemView.findViewById(R.id.marti_frg_item);
            marti_frgrec_title = (TextView) itemView.findViewById(R.id.marti_frgrec_title);
            marti_frgrec_content = (TextView) itemView.findViewById(R.id.marti_frgrec_content);
        }
    }
}
