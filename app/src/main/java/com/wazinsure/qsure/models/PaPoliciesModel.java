package com.wazinsure.qsure.models;

public class PaPoliciesModel {
    String pa_policy_id;
    String policy_no;
    String cover_name;
    String product_name;
    String premium;
    String applicant_dob;
    String applicant_name;
    String applicant_idno;
    String applicant_phone_number;
    String applicant_email;
    String applicant_idfront;
    String applicant_idback;
    String applicant_photo;
    String application_date;
    String start_date;
    String end_date;
    String declaration_goodhealth;
    String notin_goodhealth;
    String previous_pacovers;
    String previous_paunderwritter;
    String activation_status;
    String activation_comments;
    String activated_by;
    String activated_on;


    public PaPoliciesModel(String pa_policy_id, String policy_no, String cover_name, String product_name, String premium, String applicant_dob, String applicant_name, String applicant_idno, String applicant_phone_number, String applicant_email, String applicant_idfront, String applicant_idback, String applicant_photo, String application_date, String start_date, String end_date, String declaration_goodhealth, String notin_goodhealth, String previous_pacovers, String previous_paunderwritter, String activation_status, String activation_comments, String activated_by, String activated_on) {
        this.pa_policy_id = pa_policy_id;
        this.policy_no = policy_no;
        this.cover_name = cover_name;
        this.product_name = product_name;
        this.premium = premium;
        this.applicant_dob = applicant_dob;
        this.applicant_name = applicant_name;
        this.applicant_idno = applicant_idno;
        this.applicant_phone_number = applicant_phone_number;
        this.applicant_email = applicant_email;
        this.applicant_idfront = applicant_idfront;
        this.applicant_idback = applicant_idback;
        this.applicant_photo = applicant_photo;
        this.application_date = application_date;
        this.start_date = start_date;
        this.end_date = end_date;
        this.declaration_goodhealth = declaration_goodhealth;
        this.notin_goodhealth = notin_goodhealth;
        this.previous_pacovers = previous_pacovers;
        this.previous_paunderwritter = previous_paunderwritter;
        this.activation_status = activation_status;
        this.activation_comments = activation_comments;
        this.activated_by = activated_by;
        this.activated_on = activated_on;
    }

    public String getPa_policy_id() {
        return pa_policy_id;
    }

    public void setPa_policy_id(String pa_policy_id) {
        this.pa_policy_id = pa_policy_id;
    }

    public String getPolicy_no() {
        return policy_no;
    }

    public void setPolicy_no(String policy_no) {
        this.policy_no = policy_no;
    }

    public String getCover_name() {
        return cover_name;
    }

    public void setCover_name(String cover_name) {
        this.cover_name = cover_name;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getPremium() {
        return premium;
    }

    public void setPremium(String premium) {
        this.premium = premium;
    }

    public String getApplicant_dob() {
        return applicant_dob;
    }

    public void setApplicant_dob(String applicant_dob) {
        this.applicant_dob = applicant_dob;
    }

    public String getApplicant_name() {
        return applicant_name;
    }

    public void setApplicant_name(String applicant_name) {
        this.applicant_name = applicant_name;
    }

    public String getApplicant_idno() {
        return applicant_idno;
    }

    public void setApplicant_idno(String applicant_idno) {
        this.applicant_idno = applicant_idno;
    }

    public String getApplicant_phone_number() {
        return applicant_phone_number;
    }

    public void setApplicant_phone_number(String applicant_phone_number) {
        this.applicant_phone_number = applicant_phone_number;
    }

    public String getApplicant_email() {
        return applicant_email;
    }

    public void setApplicant_email(String applicant_email) {
        this.applicant_email = applicant_email;
    }

    public String getApplicant_idfront() {
        return applicant_idfront;
    }

    public void setApplicant_idfront(String applicant_idfront) {
        this.applicant_idfront = applicant_idfront;
    }

    public String getApplicant_idback() {
        return applicant_idback;
    }

    public void setApplicant_idback(String applicant_idback) {
        this.applicant_idback = applicant_idback;
    }

    public String getApplicant_photo() {
        return applicant_photo;
    }

    public void setApplicant_photo(String applicant_photo) {
        this.applicant_photo = applicant_photo;
    }

    public String getApplication_date() {
        return application_date;
    }

    public void setApplication_date(String application_date) {
        this.application_date = application_date;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getDeclaration_goodhealth() {
        return declaration_goodhealth;
    }

    public void setDeclaration_goodhealth(String declaration_goodhealth) {
        this.declaration_goodhealth = declaration_goodhealth;
    }

    public String getNotin_goodhealth() {
        return notin_goodhealth;
    }

    public void setNotin_goodhealth(String notin_goodhealth) {
        this.notin_goodhealth = notin_goodhealth;
    }

    public String getPrevious_pacovers() {
        return previous_pacovers;
    }

    public void setPrevious_pacovers(String previous_pacovers) {
        this.previous_pacovers = previous_pacovers;
    }

    public String getPrevious_paunderwritter() {
        return previous_paunderwritter;
    }

    public void setPrevious_paunderwritter(String previous_paunderwritter) {
        this.previous_paunderwritter = previous_paunderwritter;
    }

    public String getActivation_status() {
        return activation_status;
    }

    public void setActivation_status(String activation_status) {
        this.activation_status = activation_status;
    }

    public String getActivation_comments() {
        return activation_comments;
    }

    public void setActivation_comments(String activation_comments) {
        this.activation_comments = activation_comments;
    }

    public String getActivated_by() {
        return activated_by;
    }

    public void setActivated_by(String activated_by) {
        this.activated_by = activated_by;
    }

    public String getActivated_on() {
        return activated_on;
    }

    public void setActivated_on(String activated_on) {
        this.activated_on = activated_on;
    }
}
