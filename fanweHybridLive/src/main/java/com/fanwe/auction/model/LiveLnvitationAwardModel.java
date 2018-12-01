package com.fanwe.auction.model;

import com.fanwe.hybrid.model.BaseActModel;

import java.io.Serializable;

public class LiveLnvitationAwardModel extends BaseActModel {
    private String all_money;
    private String yongjin;
    private String renshu;
    private String zhuanru;
    private String yue;
    private String android_down_url;
    private String dec;

    public String getAll_money() {
        return all_money;
    }

    public void setAll_money(String all_money) {
        this.all_money = all_money;
    }

    public String getYongjin() {
        return yongjin;
    }

    public void setYongjin(String yongjin) {
        this.yongjin = yongjin;
    }

    public String getRenshu() {
        return renshu;
    }

    public void setRenshu(String renshu) {
        this.renshu = renshu;
    }

    public String getZhuanru() {
        return zhuanru;
    }

    public void setZhuanru(String zhuanru) {
        this.zhuanru = zhuanru;
    }

    public String getYue() {
        return yue;
    }

    public void setYue(String yue) {
        this.yue = yue;
    }

    public String getAndroid_down_url() {
        return android_down_url;
    }

    public void setAndroid_down_url(String android_down_url) {
        this.android_down_url = android_down_url;
    }

    public String getDec() {
        return dec;
    }

    public void setDec(String dec) {
        this.dec = dec;
    }


    @Override
    public String toString() {
        return "LiveLnvitationAwardModel{" +
                "all_money='" + all_money + '\'' +
                ", yongjin='" + yongjin + '\'' +
                ", renshu='" + renshu + '\'' +
                ", zhuanru='" + zhuanru + '\'' +
                ", yue='" + yue + '\'' +
                ", android_down_url='" + android_down_url + '\'' +
                ", dec='" + dec + '\'' +
                '}';
    }
}
