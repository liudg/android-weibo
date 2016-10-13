package com.liudong.weibo.entity;

/**
 * 我页面自定义数据集合
 */
public class UserItem {

    public UserItem(boolean isShowTopDivider, int leftImg, String subhead, String caption) {
        this.isShowTopDivider = isShowTopDivider;
        this.leftImg = leftImg;
        this.subhead = subhead;
        this.caption = caption;
    }

    private boolean isShowTopDivider;
    private int leftImg;
    private String subhead;
    private String caption;

    /**
     * 是否显示分割线
     */
    public boolean isShowTopDivider() {
        return isShowTopDivider;
    }

    public void setShowTopDivider(boolean isShowTopDivider) {
        this.isShowTopDivider = isShowTopDivider;
    }

    public int getLeftImg() {
        return leftImg;
    }

    public void setLeftImg(int leftImg) {
        this.leftImg = leftImg;
    }

    public String getSubhead() {
        return subhead;
    }

    public void setSubhead(String subhead) {
        this.subhead = subhead;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

}
