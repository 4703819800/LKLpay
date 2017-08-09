package com.lklpay.www.bean;

/**
 * Created by liuming on 2017/7/19.
 */

public class loginBean {


    /**
     * info : {"id":"1","name":"测试","typeId":"1","loginName":"111111","password":"111111","connectMan":"aaaa","phone":"15342011636","lacarraNum":"1234567890","address":"天津市滨海新区翠亨村","lnglat":"117.712757,39.038531","isTicket":"1","discount":"0","content":"<p>描述描述<\/p>","img":"uploads/article/20170722153850917.png","disabled":"0"}
     * status : true
     * message : 登陆成功
     */

    private InfoBean info;
    private boolean status;
    private String message;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class InfoBean {
        /**
         * id : 1
         * name : 测试
         * typeId : 1
         * loginName : 111111
         * password : 111111
         * connectMan : aaaa
         * phone : 15342011636
         * lacarraNum : 1234567890
         * address : 天津市滨海新区翠亨村
         * lnglat : 117.712757,39.038531
         * isTicket : 1
         * discount : 0
         * content : <p>描述描述</p>
         * img : uploads/article/20170722153850917.png
         * disabled : 0
         */

        private String id;
        private String name;
        private String typeId;
        private String loginName;
        private String password;
        private String connectMan;
        private String phone;
        private String lacarraNum;
        private String address;
        private String lnglat;
        private String isTicket;
        private String discount;
        private String content;
        private String img;
        private String disabled;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTypeId() {
            return typeId;
        }

        public void setTypeId(String typeId) {
            this.typeId = typeId;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getConnectMan() {
            return connectMan;
        }

        public void setConnectMan(String connectMan) {
            this.connectMan = connectMan;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getLacarraNum() {
            return lacarraNum;
        }

        public void setLacarraNum(String lacarraNum) {
            this.lacarraNum = lacarraNum;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLnglat() {
            return lnglat;
        }

        public void setLnglat(String lnglat) {
            this.lnglat = lnglat;
        }

        public String getIsTicket() {
            return isTicket;
        }

        public void setIsTicket(String isTicket) {
            this.isTicket = isTicket;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getDisabled() {
            return disabled;
        }

        public void setDisabled(String disabled) {
            this.disabled = disabled;
        }
    }
}

