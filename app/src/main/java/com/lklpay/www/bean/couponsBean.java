package com.lklpay.www.bean;

import java.util.List;

/**
 * Created by liuming on 2017/7/28.
 */

public class couponsBean {

    /**
     * status : true
     * message :
     */
    private boolean status;
    private String message;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private List<InfoBean> info;

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * id : 1
         * userId : 1
         * shopId : 1
         * tId : 1
         * disabled : 0
         * name : 优惠券
         * endDate : 2017-08-29
         * money : 1000
         * startTime : 9:00
         * endTime : 12:00
         * startDate : 2017-07-28
         * status : 1
         * shopType : 1
         *
         * showCheckBox ture 显示 false 不显示
         * isCheckBox true 选中 false 未选中
         */
        private boolean showCheckBox;
        private boolean isCheckBox;

        private String id;
        private String userId;
        private String shopId;
        private String tId;
        private String disabled;
        private String name;
        private String endDate;
        private String money;
        private String startTime;
        private String endTime;
        private String startDate;
        private String status;
        private String shopType;


        public boolean getShowCheckBox() {
            return showCheckBox;
        }

        public void setShowCheckBox(boolean showCheckBox) {
            this.showCheckBox = showCheckBox;
        }

        public boolean getCheckBox() {
            return isCheckBox;
        }

        public void setCheckBox(boolean checkBox) {
            isCheckBox = checkBox;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getTId() {
            return tId;
        }

        public void setTId(String tId) {
            this.tId = tId;
        }

        public String getDisabled() {
            return disabled;
        }

        public void setDisabled(String disabled) {
            this.disabled = disabled;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getShopType() {
            return shopType;
        }

        public void setShopType(String shopType) {
            this.shopType = shopType;
        }

    }
}
