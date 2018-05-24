package com.hbbc.mshare.user.gongyu_section;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hbbc.R;

import java.util.List;

/**
 * Created by Administrator on 2017/11/14.
 */
public class GongYu_Label_Adapter extends BaseAdapter {

    private int drawables[] = {R.drawable.mshare_gongyu_circle_blue,R.drawable.mshare_gongyu_circle_purple,
        R.drawable.mshare_gongyu_circle_pink,R.drawable.mshare_gongyu_circle_red,R.drawable.mshare_gongyu_circle_yellow,
        R.drawable.mshare_gongyu_circle_green};

    private List<String> labels;

    public GongYu_Label_Adapter(List<String> labels) {
        this.labels = labels;
    }



    @Override
    public int getCount() {

        return labels.size();
    }



    @Override
    public String getItem(int position) {

        return labels.get(position);
    }



    @Override
    public long getItemId(int position) {

        return 0;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String label = getItem(position);
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_gongyu_layout, parent, false);
        TextView tv_name = (TextView) inflate.findViewById(R.id.tv_name);
        TextView tv_content = (TextView) inflate.findViewById(R.id.tv_content);
        tv_name.setCompoundDrawablesWithIntrinsicBounds(parent.getContext().getResources().getDrawable(drawables[(position%drawables.length)]),null,null,null);

        String[] split = label.split(",");
        tv_name.setText(split[0]);
        tv_content.setText(split[1]);

        return inflate;
    }
}

