package com.lklpay.www.bean;

/**
 * Created by liuming on 2017/7/31.
 */

public class orderPayBean {


    /**
     * ticketStatus : true
     * ticketMessage : 优惠券可用
     * status : true
     * orderNum : 5835201707311745376380
     * orderId : 6
     */

    private boolean ticketStatus;
    private String ticketMessage;
    private boolean status;
    private String orderNum;
    private int orderId;

    public boolean isTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(boolean ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getTicketMessage() {
        return ticketMessage;
    }

    public void setTicketMessage(String ticketMessage) {
        this.ticketMessage = ticketMessage;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
