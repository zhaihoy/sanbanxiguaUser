package com.hbbc.mshell;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.hbbc.R;
import com.hbbc.mshell.bean.ShellItemListBean;

import java.util.List;

/**
 * RecyclerView适配器
 */
public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.MyViewHolder> {

    private List<ShellItemListBean.ShellItemList> t;   //数据集合

    private Context context;                               //上下文环境

    private MyRecyclerItemListener myRecyclerItemListener; //RecyclerView监听事件



    public MainRecyclerViewAdapter(Context context, List<ShellItemListBean.ShellItemList> t) {
        this.t = t;
        this.context = context;
    }



    /**
     * 视图绑定
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.mshell_recycler_item, null);
        return new MyViewHolder(view);
    }



    /**
     * 绑定视图，进行视图的逻辑操作
     */
    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int i) {
        //类型判断，当类型为0的业务逻辑
        if (getItemViewType(i) == 0) {
            myViewHolder.materialRippleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myRecyclerItemListener.setOnMyRecyclerItemListener(view, i,t.get(i).getItemURL());
                }
            });
            Glide.with(context).load(t.get(i).getItemPicFileID()).error(R.drawable.global_img_error).into(myViewHolder.imageView);
        }
    }



    /**
     * 项目的类型
     */
    @Override
    public int getItemViewType(int position) {
        return 0;
    }



    /**
     * 数据的大小
     */
    @Override
    public int getItemCount() {
        return t.size();
    }



    /**
     * 设置监听
     */
    public void setOnMyRecyclerItemListener(MyRecyclerItemListener myRecyclerItemListener) {
        this.myRecyclerItemListener = myRecyclerItemListener;
    }



    /**
     * 点击监听的接口
     */
    interface MyRecyclerItemListener {
        void setOnMyRecyclerItemListener(View view, int position,String url);
    }

    /**
     * 自定义ViewHolder
     */
    class MyViewHolder extends RecyclerView.ViewHolder {
        MaterialRippleLayout materialRippleLayout;

        ImageView imageView;



        public MyViewHolder(View itemView) {
            super(itemView);
            materialRippleLayout = (MaterialRippleLayout) itemView.findViewById(R.id.rcv_item_mrl);
            imageView = (ImageView) itemView.findViewById(R.id.rcv_mrl_img);
        }
    }
}
