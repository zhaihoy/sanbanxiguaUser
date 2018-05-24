package com.hbbc.mshare.user.detail;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hbbc.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/12.
 *
 */
public class Image_Adapter extends BaseAdapter {

    private List<String> urls=new ArrayList<>();

    private int iv_height;//动态计算出我们所需要的ImageView的高度

    @Override
    public int getCount() {

        return urls.size();
    }

    @Override
    public String getItem(int position) {

        return urls.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(iv_height==0){
            iv_height=calculateImageViewHeight(parent);//动态计算每个图片就得的宽度
            String TAG="tag";
            Log.d(TAG, "getView: iv_height==="+iv_height);
        }

        String url = getItem(position);
        ImageView img=new ImageView(parent.getContext());
//        GridView.MarginLayoutParams params=new GridView.MarginLayoutParams(GridView.LayoutParams.MATCH_PARENT,iv_height);
        AbsListView.LayoutParams params=new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,iv_height);

        img.setLayoutParams(params);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(parent.getContext()).load(url).diskCacheStrategy(DiskCacheStrategy.ALL)//全部缓存
                .placeholder(R.drawable.ic_launcher).into(img);
        return img;

    }


    /**
     * 动态计算出要添加的ImageView的高度是多少像素值
     * 高:宽=0.618
     */
    private int calculateImageViewHeight(ViewGroup parent) {

        GridView g= (GridView) parent;
        GridView.MarginLayoutParams params = (GridView.MarginLayoutParams) g.getLayoutParams();

        int leftMargin = params.leftMargin;
        int rightMargin=params.rightMargin;
        int leftPadding=g.getPaddingLeft();
        int rightPadding=g.getPaddingRight();

        int horizontalSpacing = g.getHorizontalSpacing();
        int numColumns = g.getNumColumns();
        int measuredWidth = g.getMeasuredWidth();
        Log.d("tag", "calculateImageViewHeight: measuredWidth==="+measuredWidth);

        int width=measuredWidth-leftMargin-rightMargin-leftPadding-rightPadding-(numColumns-1)*horizontalSpacing;
        return (int) (0.618*width/numColumns+0.5);
    }

    public void setData(List<String> urls){
        for (int i = 0; i < urls.size(); i++) {
            this.urls.add(urls.get(i));
        }
        notifyDataSetChanged();
    }

}
