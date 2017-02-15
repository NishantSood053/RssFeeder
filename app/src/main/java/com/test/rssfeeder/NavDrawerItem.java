package com.test.rssfeeder;

/**
 * Created by nishant- on 02-12-2015.
 */
public class NavDrawerItem {

    private boolean showNotify;
    private String title;


    public NavDrawerItem() {
    }

    public NavDrawerItem(boolean showNotify, String title) {
        this.showNotify = showNotify;
        this.title = title;
    }

    // -------------- SETTERS
    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    //----------------GETTERS
    public boolean isShowNotify() {
        return showNotify;
    }

    public String getTitle() {
        return title;
    }

}
