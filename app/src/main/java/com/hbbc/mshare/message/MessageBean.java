package com.hbbc.mshare.message;


import com.hbbc.mshare.login.BaseResultBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/24.
 * {
 " Result": true 						// 执行成功返回true，否则返回错误类型
 ," Notice":"操作成功"				// 说明。错误时会显示详细错误信息
 , “MessageList”:[{
 “MessageID”:”1”                //消息编号
 ,“MessageName”:”优惠大酬宾”    //消息名称
 ,“MessageTypeID”:”1”           //消息类型编号
 ,“MessageContent:”今天月卡买一送一”   //消息内容
 ,“MessageTypePic”:”1.png”            //消息类型图标
 }
 *
 */
public class MessageBean extends BaseResultBean{

    private ArrayList<Message> MessageList;


    public ArrayList<Message> getMessageList() {

        return MessageList;
    }


    public void setMessageList(ArrayList<Message> messageList) {

        MessageList = messageList;
    }

    public class Message{

        private String MessageID;
        private String MessageName;
        private String MessageTypeID;
        private String MessageContent;
        private String MessageTypePic;



        public String getMessageID() {

            return MessageID;
        }



        public void setMessageID(String messageID) {

            MessageID = messageID;
        }



        public String getMessageName() {

            return MessageName;
        }



        public void setMessageName(String messageName) {

            MessageName = messageName;
        }



        public String getMessageTypeID() {

            return MessageTypeID;
        }



        public void setMessageTypeID(String messageTypeID) {

            MessageTypeID = messageTypeID;
        }



        public String getMessageContent() {

            return MessageContent;
        }



        public void setMessageContent(String messageContent) {

            MessageContent = messageContent;
        }



        public String getMessageTypePic() {

            return MessageTypePic;
        }



        public void setMessageTypePic(String messageTypePic) {

            MessageTypePic = messageTypePic;
        }
    }



}
