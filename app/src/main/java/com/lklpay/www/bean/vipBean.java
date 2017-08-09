package com.lklpay.www.bean;

import java.util.List;

/**
 * Created by liuming on 2017/7/31.
 */

public class vipBean {


    private List<MemberListBean> memberList;

    public List<MemberListBean> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<MemberListBean> memberList) {
        this.memberList = memberList;
    }

    public static class MemberListBean {
        /**
         * userId : 1
         * checkBox : ture|false
         * info : {"id":"1","name":"张三","img":"http://www.qqpk.cn/Article/UploadFiles/201110/20111020102349724.jpg","phone":"188888888888","openId":"ow6xZwhR141TEt7ua5Cv-Nn-JEIY","disabled":"0"}
         */

        private String userId;
        private Boolean checkBox =false;
        private InfoBean info;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Boolean getCheckBox() {
            return checkBox;
        }

        public void setCheckBox(Boolean checkBox) {
            this.checkBox = checkBox;
        }

        public InfoBean getInfo() {
            return info;
        }

        public void setInfo(InfoBean info) {
            this.info = info;
        }

        public static class InfoBean {
            /**
             * id : 1
             * name : 张三
             * img : http://www.qqpk.cn/Article/UploadFiles/201110/20111020102349724.jpg
             * phone : 188888888888
             * openId : ow6xZwhR141TEt7ua5Cv-Nn-JEIY
             * disabled : 0
             */

            private String id;
            private String name;
            private String img;
            private String phone;
            private String openId;
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

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getOpenId() {
                return openId;
            }

            public void setOpenId(String openId) {
                this.openId = openId;
            }

            public String getDisabled() {
                return disabled;
            }

            public void setDisabled(String disabled) {
                this.disabled = disabled;
            }
        }
    }
}
