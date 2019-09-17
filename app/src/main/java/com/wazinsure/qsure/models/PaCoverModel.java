package com.wazinsure.qsure.models;

public class PaCoverModel {
    String pa_cover_id;
    String cover_name;
    String cover_desc;
    String currency;
    String annual_premium;
    String benefit1_name;
    String benefit1_desc;
    String benefit1_amount;
    String benefit2_name;
    String benefit2_desc;
    String benefit2_amount;
    String benefit3_name;
    String benefit3_desc;
    String benefit3_amount;
    String product;

    public PaCoverModel(String pa_cover_id, String cover_name, String cover_desc, String currency, String annual_premium, String benefit1_name, String benefit1_desc, String benefit1_amount, String benefit2_name, String benefit2_desc, String benefit2_amount, String benefit3_name, String benefit3_desc, String benefit3_amount, String product) {
        this.pa_cover_id = pa_cover_id;
        this.cover_name = cover_name;
        this.cover_desc = cover_desc;
        this.currency = currency;
        this.annual_premium = annual_premium;
        this.benefit1_name = benefit1_name;
        this.benefit1_desc = benefit1_desc;
        this.benefit1_amount = benefit1_amount;
        this.benefit2_name = benefit2_name;
        this.benefit2_desc = benefit2_desc;
        this.benefit2_amount = benefit2_amount;
        this.benefit3_name = benefit3_name;
        this.benefit3_desc = benefit3_desc;
        this.benefit3_amount = benefit3_amount;
        this.product = product;
    }

    public String getPa_cover_id() {
        return pa_cover_id;
    }

    public void setPa_cover_id(String pa_cover_id) {
        this.pa_cover_id = pa_cover_id;
    }

    public String getCover_name() {
        return cover_name;
    }

    public void setCover_name(String cover_name) {
        this.cover_name = cover_name;
    }

    public String getCover_desc() {
        return cover_desc;
    }

    public void setCover_desc(String cover_desc) {
        this.cover_desc = cover_desc;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAnnual_premium() {
        return annual_premium;
    }

    public void setAnnual_premium(String annual_premium) {
        this.annual_premium = annual_premium;
    }

    public String getBenefit1_name() {
        return benefit1_name;
    }

    public void setBenefit1_name(String benefit1_name) {
        this.benefit1_name = benefit1_name;
    }

    public String getBenefit1_desc() {
        return benefit1_desc;
    }

    public void setBenefit1_desc(String benefit1_desc) {
        this.benefit1_desc = benefit1_desc;
    }

    public String getBenefit1_amount() {
        return benefit1_amount;
    }

    public void setBenefit1_amount(String benefit1_amount) {
        this.benefit1_amount = benefit1_amount;
    }

    public String getBenefit2_name() {
        return benefit2_name;
    }

    public void setBenefit2_name(String benefit2_name) {
        this.benefit2_name = benefit2_name;
    }

    public String getBenefit2_desc() {
        return benefit2_desc;
    }

    public void setBenefit2_desc(String benefit2_desc) {
        this.benefit2_desc = benefit2_desc;
    }

    public String getBenefit2_amount() {
        return benefit2_amount;
    }

    public void setBenefit2_amount(String benefit2_amount) {
        this.benefit2_amount = benefit2_amount;
    }

    public String getBenefit3_name() {
        return benefit3_name;
    }

    public void setBenefit3_name(String benefit3_name) {
        this.benefit3_name = benefit3_name;
    }

    public String getBenefit3_desc() {
        return benefit3_desc;
    }

    public void setBenefit3_desc(String benefit3_desc) {
        this.benefit3_desc = benefit3_desc;
    }

    public String getBenefit3_amount() {
        return benefit3_amount;
    }

    public void setBenefit3_amount(String benefit3_amount) {
        this.benefit3_amount = benefit3_amount;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
}
