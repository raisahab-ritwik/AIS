package com.knwedu.ourschool.bo;

/**
 * Created by Rajhrita on 09/06/16.
 */
public class PayTMResponse {
    private String TXNID;
    private String TXNAMOUNT;
    private String CURRENCY;
    private String STATUS;
    private String RESPCODE;
    private String RESPMSG;
    private String TXNDATE;
    private String BANKTXNID;
    private String GATEWAYNAME;
    private String BANKNAME;
    private String PAYMENTMODE;

    public PayTMResponse() {

    }

    public String getTXNAMOUNT() {
        return TXNAMOUNT;
    }

    public void setTXNAMOUNT(String tXNAMOUNT) {
        TXNAMOUNT = tXNAMOUNT;
    }

    public String getCURRENCY() {
        return CURRENCY;
    }

    public void setCURRENCY(String cURRENCY) {
        CURRENCY = cURRENCY;
    }

    public String getTXNID() {
        return TXNID;
    }

    public void setTXNID(String tXNID) {
        TXNID = tXNID;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String sTATUS) {
        STATUS = sTATUS;
    }

    public String getRESPCODE() {
        return RESPCODE;
    }

    public void setRESPCODE(String rESPCODE) {
        RESPCODE = rESPCODE;
    }

    public String getRESPMSG() {
        return RESPMSG;
    }

    public void setRESPMSG(String rESPMSG) {
        RESPMSG = rESPMSG;
    }

    public String getTXNDATE() {
        return TXNDATE;
    }

    public void setTXNDATE(String tXNDATE) {
        TXNDATE = tXNDATE;
    }

    public String getBANKTXNID() {
        return BANKTXNID;
    }

    public void setBANKTXNID(String bANKTXNID) {
        BANKTXNID = bANKTXNID;
    }

    public String getGATEWAYNAME() {
        return GATEWAYNAME;
    }

    public void setGATEWAYNAME(String gATEWAYNAME) {
        GATEWAYNAME = gATEWAYNAME;
    }

    public String getBANKNAME() {
        return BANKNAME;
    }

    public void setBANKNAME(String bANKNAME) {
        BANKNAME = bANKNAME;
    }

    public String getPAYMENTMODE() {
        return PAYMENTMODE;
    }

    public void setPAYMENTMODE(String pAYMENTMODE) {
        PAYMENTMODE = pAYMENTMODE;
    }

}

