package com.firstresponder.app.Models;

public class Rule {

    private int id;
    private String status;
    private String msg;
    private String reply;
    private String msgCount;
    private String specificContact;
    private String ignoredContact;

    public String getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(String msgCount) {
        this.msgCount = msgCount;
    }

    public String getSpecificContact() {
        return specificContact;
    }

    public void setSpecificContact(String specificContact) {
        this.specificContact = specificContact;
    }

    public String getIgnoredContact() {
        return ignoredContact;
    }

    public void setIgnoredContact(String ignoredContact) {
        this.ignoredContact = ignoredContact;
    }

    public int getId() {
        return id;
    }

    public Rule(int id, String status, String msg, String reply, String msgCount, String specificContact, String ignoredContact) {
        this.id = id;
        this.status = status;
        this.msg = msg;
        this.reply = reply;
        this.msgCount = msgCount;
        this.specificContact = specificContact;
        this.ignoredContact = ignoredContact;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}
