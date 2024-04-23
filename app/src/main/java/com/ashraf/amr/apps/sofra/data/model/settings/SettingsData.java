
package com.ashraf.amr.apps.sofra.data.model.settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SettingsData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("facebook")
    @Expose
    private String facebook;
    @SerializedName("twitter")
    @Expose
    private String twitter;
    @SerializedName("instagram")
    @Expose
    private String instagram;
    @SerializedName("commission")
    @Expose
    private String commission;
    @SerializedName("about_app")
    @Expose
    private String aboutApp;
    @SerializedName("terms")
    @Expose
    private String terms;
    @SerializedName("commissions_text")
    @Expose
    private Object commissionsText;
    @SerializedName("bank_accounts")
    @Expose
    private Object bankAccounts;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getAboutApp() {
        return aboutApp;
    }

    public void setAboutApp(String aboutApp) {
        this.aboutApp = aboutApp;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public Object getCommissionsText() {
        return commissionsText;
    }

    public void setCommissionsText(Object commissionsText) {
        this.commissionsText = commissionsText;
    }

    public Object getBankAccounts() {
        return bankAccounts;
    }

    public void setBankAccounts(Object bankAccounts) {
        this.bankAccounts = bankAccounts;
    }

}
