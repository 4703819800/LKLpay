package com.lklpay.www.bean;

/**
 * Created by liuming on 2017/8/7.
 */

public class userBean {

    /**
     * info : {"id":"2","lacarraNum":"6786563425","userId":"3","name":"bingo","img":"http://wx.qlogo.cn/mmopen/ajNVdqHZLLA6UfgibYJpicEKxOpPtsAqanpBgB8uI08ktFagbIFrlBwkxF5s641WZIo75rm2bn5k2amjRaXFAdnA/0","openId":"oKJdlw3X8FGX0bFeOZCIXuCpgB6M","status":"1","shopId":"2"}
     * status : true
     */

    private InfoBean info;
    private boolean status;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public static class InfoBean {
        /**
         * id : 2
         * lacarraNum : 6786563425
         * userId : 3
         * name : bingo
         * img : http://wx.qlogo.cn/mmopen/ajNVdqHZLLA6UfgibYJpicEKxOpPtsAqanpBgB8uI08ktFagbIFrlBwkxF5s641WZIo75rm2bn5k2amjRaXFAdnA/0
         * openId : oKJdlw3X8FGX0bFeOZCIXuCpgB6M
         * status : 1
         * shopId : 2
         */

        private String id;
        private String lacarraNum;
        private String userId;
        private String name;
        private String img;
        private String openId;
        private String status;
        private String shopId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLacarraNum() {
            return lacarraNum;
        }

        public void setLacarraNum(String lacarraNum) {
            this.lacarraNum = lacarraNum;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getOpenId() {
            return openId;
        }

        public void setOpenId(String openId) {
            this.openId = openId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }
    }
}
