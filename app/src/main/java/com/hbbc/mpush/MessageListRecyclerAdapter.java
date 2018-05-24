package com.hbbc.mpush;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hbbc.R;
import com.hbbc.mpush.bean.MessageListBean;
import com.hbbc.util.GlobalParameter;

import static com.hbbc.R.id.mpush_item_msg;
import static com.hbbc.R.id.mpush_item_time;
import static com.hbbc.R.id.mpush_item_title;
import static com.hbbc.R.id.mpush_item_to;
import static com.hbbc.R.id.mpush_msg_itemimg;
import static com.hbbc.R.id.mpush_msg_linear;
import static com.hbbc.R.id.toMsgInfo;


/**
 * 消息列表适配器
 */
public class MessageListRecyclerAdapter extends BaseAdapter {

    private Context context;

    private MessageListBean messageListBean;

    private int count;                 //item数量

    private int themeColor;             //主题色

    private OnItemClick onItemClick;    //项目点击事件



    public MessageListRecyclerAdapter(Context context, MessageListBean messageListBean) {
        this.context = context;
        this.messageListBean = messageListBean;

        initTheme();
    }



    /**
     * 主题色
     */
    private void initTheme() {
        themeColor = GlobalParameter.getThemeColor();
    }



    /**
     * 消息的条数
     */
    @Override
    public int getCount() {

        if (messageListBean.getReadMessageList() != null && messageListBean.getUnReadMessageList() != null) {
            //已读消息数量
            count = messageListBean.getReadMessageList().size() + messageListBean.getUnReadMessageList().size();
        } else if (messageListBean.getReadMessageList() != null && messageListBean.getUnReadMessageList() == null) {
            //已读消息
            count = messageListBean.getReadMessageList().size();
        } else if (messageListBean.getUnReadMessageList() != null && messageListBean.getReadMessageList() == null) {
            //未读消息数量
            count = messageListBean.getUnReadMessageList().size();
        }
        return count;
    }



    @Override
    public Object getItem(int position) {
        if (messageListBean.getReadMessageList() == null && messageListBean.getUnReadMessageList() == null) {
            return null;
        }
        //返回已读消息
        else if (messageListBean.getReadMessageList() != null && messageListBean.getUnReadMessageList() == null) {
            return messageListBean.getReadMessageList().get(position);
        }
        //返回未读消息
        else if (messageListBean.getUnReadMessageList() != null && messageListBean.getReadMessageList() == null) {
            return messageListBean.getUnReadMessageList().get(position);
        }
        //当消息都不为空时,先判断未读消息是否小于当前的position，如果小于就返回消息类型为1
        else if (messageListBean.getUnReadMessageList() != null && messageListBean.getReadMessageList() != null && messageListBean.getUnReadMessageList().size() > position) {
            return messageListBean.getUnReadMessageList().get(position);
        }
        //当消息都不为空时,先判断未读消息是否大于等于当前的position,如果大于等于就返回消息类型为0
        else {
            return messageListBean.getReadMessageList().get(position);
        }

    }



    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyMsgViewHolder myMsgViewHolder = null;
        switch (getItemViewType(position)) {
            case 0:
                //未读消息
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.mpush_msg_frgitem, null, false);

                    myMsgViewHolder = new MyMsgViewHolder();
                    myMsgViewHolder.toMsgInfo = (RelativeLayout) convertView.findViewById(toMsgInfo);
                    myMsgViewHolder.mpush_msg_itemimg = (ImageView) convertView.findViewById(mpush_msg_itemimg);
                    myMsgViewHolder.mpush_msg_linear = (LinearLayout) convertView.findViewById(mpush_msg_linear);
                    myMsgViewHolder.mpush_item_msg = (TextView) convertView.findViewById(mpush_item_title);
                    myMsgViewHolder.mpush_item_time = (TextView) convertView.findViewById(mpush_item_time);
                    myMsgViewHolder.mpush_item_title = (TextView) convertView.findViewById(mpush_item_msg);
                    myMsgViewHolder.mpush_item_to = (ImageView) convertView.findViewById(mpush_item_to);
                    //主题色
                    myMsgViewHolder.mpush_item_to.setColorFilter(themeColor);
                    convertView.setTag(myMsgViewHolder);
                } else {
                    myMsgViewHolder = (MyMsgViewHolder) convertView.getTag();
                }

                final MessageListBean.UnReadMessageList unReadMessageList;
                //未读信息
                if (messageListBean.getReadMessageList() != null) {
                    //当已读消息不为空
                    unReadMessageList = messageListBean.getUnReadMessageList().get(position - messageListBean.getReadMessageList().size());
                } else {
                    unReadMessageList = messageListBean.getUnReadMessageList().get(position);
                }
                //标题
                myMsgViewHolder.mpush_item_title.setText(unReadMessageList.getMsgTitle());
                //时间
                myMsgViewHolder.mpush_item_time.setText(unReadMessageList.getSendTime());
                //内容
                myMsgViewHolder.mpush_item_msg.setText(unReadMessageList.getItemName());
                //标题图片
                Glide.with(context).load(unReadMessageList.getPicFileID()).into(myMsgViewHolder.mpush_msg_itemimg);


                myMsgViewHolder.toMsgInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClick.setOnItemClick(v, unReadMessageList.getMTMID());
                    }
                });
                break;

            case 1:
                //已读消息
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.mpush_msg_frgitem_un, null, false);

                    myMsgViewHolder = new MyMsgViewHolder();
                    myMsgViewHolder.toMsgInfo = (RelativeLayout) convertView.findViewById(toMsgInfo);
                    myMsgViewHolder.mpush_msg_itemimg = (ImageView) convertView.findViewById(mpush_msg_itemimg);
                    myMsgViewHolder.mpush_msg_linear = (LinearLayout) convertView.findViewById(mpush_msg_linear);
                    myMsgViewHolder.mpush_item_msg = (TextView) convertView.findViewById(mpush_item_title);
                    myMsgViewHolder.mpush_item_time = (TextView) convertView.findViewById(mpush_item_time);
                    myMsgViewHolder.mpush_item_title = (TextView) convertView.findViewById(mpush_item_msg);
                    myMsgViewHolder.mpush_item_to = (ImageView) convertView.findViewById(mpush_item_to);
                    //主题色
                    myMsgViewHolder.mpush_item_to.setColorFilter(themeColor);

                    convertView.setTag(myMsgViewHolder);
                } else {
                    myMsgViewHolder = (MyMsgViewHolder) convertView.getTag();
                }

                //已读信息
                final MessageListBean.ReadMessageList readMessageList = messageListBean.getReadMessageList().get(position);
                //标题
                myMsgViewHolder.mpush_item_title.setText(readMessageList.getMsgTitle());
                //时间
                myMsgViewHolder.mpush_item_time.setText(readMessageList.getSendTime());
                //内容
                myMsgViewHolder.mpush_item_msg.setText(readMessageList.getItemName());
                //标题图片
                Glide.with(context).load(readMessageList.getPicFileID()).into(myMsgViewHolder.mpush_msg_itemimg);

                myMsgViewHolder.toMsgInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClick.setOnItemClick(v, readMessageList.getMTMID());
                    }
                });

                break;
        }
        return convertView;
    }



    @Override
    public int getItemViewType(int position) {

        //返回未读消息
        if (messageListBean.getUnReadMessageList() != null && messageListBean.getReadMessageList() == null) {
            return 0;
        } else if (messageListBean.getUnReadMessageList() == null && messageListBean.getReadMessageList() != null) {
            return 1;
        }
        //返回已读消息
        else if (messageListBean.getReadMessageList() != null && messageListBean.getUnReadMessageList() != null && messageListBean.getReadMessageList().size() > position) {
            return 1;
        }
        //当消息都不为空时,先判断未读消息是否小于当前的position，如果小于就返回消息类型为0
        else {
            return 0;
        }
    }



    public void setOnItemClick(OnItemClick click) {
        this.onItemClick = click;
    }



    interface OnItemClick {
        void setOnItemClick(View view, String mtmid);
    }

    class MyMsgViewHolder {
        RelativeLayout toMsgInfo;

        ImageView mpush_msg_itemimg, mpush_item_to;   //图标,

        LinearLayout mpush_msg_linear;  //消息条目布局

        TextView mpush_item_title, mpush_item_time, mpush_item_msg;   //标题，时间，内容
    }
}
