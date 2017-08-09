package com.lklpay.www.bean;

import java.util.List;

/**
 * Created by liuming on 2017/7/31.
 */

public class orderBean {


    private List<OrderListBean> orderList;

    public List<OrderListBean> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderListBean> orderList) {
        this.orderList = orderList;
    }

    public static class OrderListBean {
        /**
         * id : 1
         * userId : 1
         * shopId : 1
         * status : 1
         * orderNum : 1234201707271023254567
         * pay_status : 1
         * date : 2017-07-27 10:00:00
         * disabled : 0
         * money : 100
         * ticketId : null
         * name : 张三
         * img : http://www.qqpk.cn/Article/UploadFiles/201110/20111020102349724.jpg
         */

        private String id;
        private String userId;
        private String shopId;
        private String status;
        private String orderNum;
        private String pay_status;
        private String date;
        private String disabled;
        private String money;
        private Object ticketId;
        private String name;
        private String img;

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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(String orderNum) {
            this.orderNum = orderNum;
        }

        public String getPay_status() {
            return pay_status;
        }

        public void setPay_status(String pay_status) {
            this.pay_status = pay_status;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDisabled() {
            return disabled;
        }

        public void setDisabled(String disabled) {
            this.disabled = disabled;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public Object getTicketId() {
            return ticketId;
        }

        public void setTicketId(Object ticketId) {
            this.ticketId = ticketId;
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
    }
}
