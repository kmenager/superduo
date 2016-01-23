package it.jaschke.alexandria.data.model;

/**
 * Created by kevinmenager on 14/12/15.
 */
public class Book {
    private String mEan;
    private String mTitle;
    private String mImageUrl;
    private String mSubtitle;
    private String mDescription;
    private String mPublisher;
    private String mPublishedDate;
    private int mPageCount;

    public String getEan() {
        return mEan;
    }

    public void setEan(String ean) {
        mEan = ean;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getSubtitle() {
        return mSubtitle;
    }

    public void setSubtitle(String subtitle) {
        mSubtitle = subtitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getPublisher() {
        return mPublisher;
    }

    public void setPublisher(String publisher) {
        mPublisher = publisher;
    }

    public String getPublishedDate() {
        return mPublishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        mPublishedDate = publishedDate;
    }

    public int getPageCount() {
        return mPageCount;
    }

    public void setPageCount(int pageCount) {
        mPageCount = pageCount;
    }
}
