package com.hbbc.mpush;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hbbc.R;
import com.hbbc.mpush.bean.MessageListBean;
import com.hbbc.util.BaseFragment;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.GlobalParameter;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.refreshview.PullToRefreshBase;
import com.hbbc.util.refreshview.PullToRefreshListView;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 加载未读消息页面
 */
public class MessageListFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener, MessageListRecyclerAdapter.OnItemClick {
    private static final String PAGE = "page"; //页数

    private static final String MTID = "MTID"; //通知ID

    private int page;                          //页数

    private String mtid;                        //通知ID

    private PullToRefreshListView mpush_refresh;//下拉刷新控件

    private ListView mpush_rec;               //列表控件

    private int Count = 1;



    /**
     * 获取当前页面
     */
    public static Fragment getInstanceFragment(int what, String mt) {
        Bundle bundle = new Bundle();
        bundle.putInt(PAGE, what);
        bundle.putString(MTID, mt);
        MessageListFragment myBaseFragment = new MessageListFragment();
        myBaseFragment.setArguments(bundle);
        return myBaseFragment;
    }



    @Override
    public void onResume() {
        super.onResume();
        Glide.with(this).onStart();
    }



    @Override
    public void onPause() {
        super.onPause();
        Glide.with(this).onStop();
    }



    /**
     * 获取当前页面数据
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getFragmentBundle().getInt(PAGE);
        mtid = getFragmentBundle().getString(MTID);
        Logger.d("page" + page + ",mtid" + mtid);
    }



    @Override
    protected void initControlView(View view) {
        mpush_refresh = (PullToRefreshListView) view.findViewById(R.id.mpush_refreshlist);
        //获取ListView
        mpush_rec = mpush_refresh.getRefreshableView();
        //设置刷新的监听事件
        mpush_refresh.setOnRefreshListener(this);
        //点击后颜色
        mpush_rec.setSelector(R.drawable.mmain_suggest_edt);
    }



    @Override
    protected int setContentView() {
        return R.layout.mpush_msg_frg;
    }



    /**
     * 消息回调机制
     */
    @Override
    protected void getMessage(Message msg) {

        switch (msg.what) {
            case 0:
                MessageListBean bean = getMessageBeanList(msg);
                if (bean != null) {
                    MessageListRecyclerAdapter myMsgRecyclerAdapter = new MessageListRecyclerAdapter(getContext(), bean);
                    mpush_rec.setAdapter(myMsgRecyclerAdapter);
                    myMsgRecyclerAdapter.setOnItemClick(this);
                }
                break;
        }
    }



    @Override
    protected void stopData() {

    }



    @Override
    protected void LoadData() {
        Logger.d("isCanLoad" + getUserVisibleHint());
        new HttpUtil().callJson(handler, 0, getContext(), GlobalConfig.MPUSH_GETMESSAGELIST, "AppUserID", GlobalConfig.AppUserID, "ECID", GlobalConfig.ECID + "", "MTID", mtid, "PushID", GlobalConfig.PushID, "UserType", GlobalParameter.getPersonflag(), "UserID", GlobalParameter.getMemberid() + GlobalParameter.getManagerid(), "Count", Count + "");
    }



    /**
     * 获取实体数据
     */
    private MessageListBean getMessageBeanList(Message msg) {
        String mmsg = (String) msg.obj;
        MessageListBean messageListBean = new MessageListBean();
        try {
            JSONObject jsonObject = new JSONObject(mmsg);
            String Result = jsonObject.getString("Result");
            String notice = jsonObject.getString("Notice");
            if (Result.equals("1") || Result.equals("2") || Result.equals("3")) {
                if (jsonObject.has("ReadMessageList")) {
                    ArrayList<MessageListBean.ReadMessageList> readMessageLists = null;
                    JSONArray readMessageList = jsonObject.getJSONArray("ReadMessageList");
                    readMessageLists = new ArrayList<>();
                    //遍历JSON
                    for (int i = 0; i < readMessageList.length(); i++) {
                        JSONObject jsonObject1 = readMessageList.getJSONObject(i);
                        MessageListBean.ReadMessageList readMessageList1 = new MessageListBean.ReadMessageList();
                        if (jsonObject1.has("MTMID")) {
                            readMessageList1.setMTMID(jsonObject1.getString("MTMID"));
                        } else {
                            readMessageList1.setMTMID("");
                        }
                        if (jsonObject1.has("MsgUrl")) {
                            readMessageList1.setMsgUrl(jsonObject1.getString("MsgUrl"));
                        } else {
                            readMessageList1.setMsgUrl("");
                        }
                        if (jsonObject1.has("ItemName")) {
                            readMessageList1.setItemName(jsonObject1.getString("ItemName"));
                        } else {
                            readMessageList1.setItemName("");
                        }
                        if (jsonObject1.has("SendTime")) {
                            String SendTime = jsonObject1.getString("SendTime").substring(0, 16);
                            readMessageList1.setSendTime(SendTime);
                        } else {
                            readMessageList1.setSendTime("");
                        }
                        if (jsonObject1.has("Status")) {
                            readMessageList1.setStatus(jsonObject1.getInt("Status"));
                        } else {
                            readMessageList1.setStatus(0);
                        }
                        if (jsonObject1.has("PicFileID")) {
                            readMessageList1.setPicFileID(jsonObject1.getString("PicFileID"));
                        } else {
                            readMessageList1.setPicFileID("");
                        }
                        if (jsonObject1.has("MIID")) {
                            readMessageList1.setMIID(jsonObject1.getString("MIID"));
                        } else {
                            readMessageList1.setMIID("");
                        }

                        if (jsonObject1.has("MsgTitle")) {
                            readMessageList1.setMsgTitle(jsonObject1.getString("MsgTitle"));
                        } else {
                            readMessageList1.setMsgTitle("");
                        }

                        //加入ReadList集合中
                        readMessageLists.add(readMessageList1);
                    }
                    messageListBean.setReadMessageList(readMessageLists);
                }

                if (jsonObject.has("UnReadMessageList")) {
                    ArrayList<MessageListBean.UnReadMessageList> readMessageLists = null;
                    JSONArray readMessageList = jsonObject.getJSONArray("UnReadMessageList");
                    readMessageLists = new ArrayList<>();
                    for (int i = 0; i < readMessageList.length(); i++) {
                        JSONObject jsonObject1 = readMessageList.getJSONObject(i);
                        MessageListBean.UnReadMessageList readMessageList1 = new MessageListBean.UnReadMessageList();
                        if (jsonObject1.has("MTMID")) {
                            readMessageList1.setMTMID(jsonObject1.getString("MTMID"));
                        } else {
                            readMessageList1.setMTMID("");
                        }
                        if (jsonObject1.has("MsgUrl")) {
                            readMessageList1.setMsgUrl(jsonObject1.getString("MsgUrl"));
                        } else {
                            readMessageList1.setMsgUrl("");
                        }
                        if (jsonObject1.has("ItemName")) {
                            readMessageList1.setItemName(jsonObject1.getString("ItemName"));
                        } else {
                            readMessageList1.setItemName("");
                        }
                        if (jsonObject1.has("SendTime")) {
                            if (!jsonObject1.getString("SendTime").equals("")){
                                String SendTime = jsonObject1.getString("SendTime").substring(0, 16);
                                readMessageList1.setSendTime(jsonObject1.getString("SendTime"));
                            }
                        } else {
                            readMessageList1.setSendTime("");
                        }
                        if (jsonObject1.has("Status")) {
                            readMessageList1.setStatus(jsonObject1.getInt("Status"));
                        } else {
                            readMessageList1.setStatus(0);
                        }
                        if (jsonObject1.has("PicFileID")) {
                            readMessageList1.setPicFileID(jsonObject1.getString("PicFileID"));
                        } else {
                            readMessageList1.setPicFileID("");
                        }
                        if (jsonObject1.has("MIID")) {
                            readMessageList1.setMIID(jsonObject1.getString("MIID"));
                        } else {
                            readMessageList1.setMIID("");
                        }
                        if (jsonObject1.has("MsgTitle")) {
                            readMessageList1.setMsgTitle(jsonObject1.getString("MsgTitle"));
                        } else {
                            readMessageList1.setMsgTitle("");
                        }
                        //加入UnReadList集合中
                        readMessageLists.add(readMessageList1);
                    }
                    messageListBean.setUnReadMessageList(readMessageLists);
                }
                return messageListBean;
            } else {
                //错误信息
                Toast.makeText(getContext(), notice, Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }



    @Override
    public void onRefresh(int refresh_mode) {
        if (refresh_mode == PullToRefreshBase.MODE_PULL_DOWN_TO_REFRESH) {
            //下拉刷新
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Count = 1;
                    new HttpUtil().callJson(handler, 0, getContext(), GlobalConfig.MPUSH_GETMESSAGELIST, "AppUserID", GlobalConfig.AppUserID, "ECID", GlobalConfig.ECID + "", "MTID", mtid, "PushID", GlobalConfig.PushID, "UserType", GlobalParameter.getPersonflag(), "UserID", GlobalParameter.getMemberid() + GlobalParameter.getManagerid(), "Count", Count + "");
                    //三秒后停止刷新
                    mpush_refresh.onRefreshComplete();
                }
            }, 3000);
        } else {
            //上拉加载
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new HttpUtil().callJson(handler, 0, getContext(), GlobalConfig.MPUSH_GETMESSAGELIST, "AppUserID", GlobalConfig.AppUserID, "ECID", GlobalConfig.ECID + "", "MTID", mtid, "PushID", GlobalConfig.PushID, "UserType", GlobalParameter.getPersonflag(), "UserID", GlobalParameter.getMemberid() + GlobalParameter.getManagerid(), "Count", ++Count + "");
                    //三秒后停止加载
                    mpush_refresh.onRefreshComplete();
                }
            }, 3000);
        }
    }



    @Override
    public void setOnItemClick(View view, String mtmid) {
        //跳转详情页面
        Intent intent = new Intent();
        intent.setClass(getContext(), MessageInfoActivity.class);
        intent.putExtra("MTMID", mtmid);
        startActivity(intent);
    }


}
