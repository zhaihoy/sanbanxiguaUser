package com.hbbc.mshell.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dkx on 2016/3/30.
 */
public class CarouselBean implements Serializable {


    /**
     * Result : true
     * CarouselList : [{"LinkURL":"http://www.bjjdkfs.com/about/gongzuodongtai/2016/0719/940.html","ImgFileID":"http://192.168.1.176:80/pgynbo/asset/distfile/10b7ca45-28c5-4193-8376-42a21e6d0a04.png","Title":"走进学校开展宣传"},{"LinkURL":"http://www.bjjdkfs.com/about/gongzuodongtai/2016/0719/937.html","ImgFileID":"http://192.168.1.176:80/pgynbo/asset/distfile/a9679016-2153-4ebb-8214-408d20ab6e32.jpg","Title":"进企业戒毒宣传公益行"},{"LinkURL":"http://www.bjjdkfs.com/about/gongzuodongtai/2016/0728/943.html","ImgFileID":"http://192.168.1.176:80/pgynbo/asset/distfile/6be40281-6c10-4367-8506-99597a5c631a.JPG","Title":"青少年暑期毒品预防教育"},{"LinkURL":"7","ImgFileID":"http://192.168.1.176:80/pgynbo/asset/distfile/12adfaa8-90f8-4780-b75e-f53d2105fd97.jpg","Title":"蒲公英"},{"LinkURL":"1","ImgFileID":"http://192.168.1.176:80/pgynbo/asset/distfile/8d348b3b-f856-4b08-a395-9ec0cb88be31.jpg","Title":"红旗飘飘"},{"LinkURL":"5","ImgFileID":"http://192.168.1.176:80/pgynbo/asset/distfile/bb24ccaf-3e8e-4dcc-8012-e76e4ee9eb86.jpg","Title":"康复人员宿舍"},{"LinkURL":"6","ImgFileID":"http://192.168.1.176:80/pgynbo/asset/distfile/435702f4-a4ac-42c7-8b09-675cbe7187dc.jpg","Title":"健身房"},{"LinkURL":"4","ImgFileID":"http://192.168.1.176:80/pgynbo/asset/distfile/ae54c15f-9435-464c-aaac-ce2087322335.jpg","Title":"音乐放松"}]
     * Notice : 操作成功
     */

    private boolean Result;

    /**
     * LinkURL : http://www.bjjdkfs.com/about/gongzuodongtai/2016/0719/940.html
     * ImgFileID : http://192.168.1.176:80/pgynbo/asset/distfile/10b7ca45-28c5-4193-8376-42a21e6d0a04.png
     * Title : 走进学校开展宣传
     */

    private List<CarouselList> CarouselList;

    public boolean isResult() {
        return Result;
    }

    public void setResult(boolean Result) {
        this.Result = Result;
    }


    public List<CarouselList> getCarouselList() {
        return CarouselList;
    }

    public void setCarouselList(List<CarouselList> CarouselList) {
        this.CarouselList = CarouselList;
    }

    public static class CarouselList {
        private String LinkURL;
        private String ImgFileID;
        private String Title;

        public String getLinkURL() {
            return LinkURL;
        }

        public void setLinkURL(String LinkURL) {
            this.LinkURL = LinkURL;
        }

        public String getImgFileID() {
            return ImgFileID;
        }

        public void setImgFileID(String ImgFileID) {
            this.ImgFileID = ImgFileID;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }
    }
    private String Notice;

    public String getNotice() {
        return Notice;
    }

    public void setNotice(String Notice) {
        this.Notice = Notice;
    }
}
