package it.jaschke.alexandria.data.model;


import android.os.Parcel;
import android.os.Parcelable;

public class BookDetailModel implements Parcelable {
    private String mIsbn;
    private String mTitleBook;
    private String mSubTitle;
    private String mDesc;
    private String mAuthors;
    private String mImgUrl;
    private String mPublisher;
    private String mPublishedOn;
    private int mPages;
    private String mCategories;

    public BookDetailModel() {

    }

    public BookDetailModel(Book book, Author author, Category category) {
        mIsbn = book.getEan();
        mTitleBook = book.getTitle();
        mSubTitle = book.getSubtitle();
        mDesc = book.getDescription();
        mAuthors = author.getAuthor();
        mImgUrl = book.getImageUrl();
        mPublisher = book.getPublisher();
        mPublishedOn = book.getPublishedDate();
        mPages = book.getPageCount();
        mCategories = category.getCategory();
    }

    protected BookDetailModel(Parcel in) {
        mIsbn = in.readString();
        mTitleBook = in.readString();
        mSubTitle = in.readString();
        mDesc = in.readString();
        mAuthors = in.readString();
        mImgUrl = in.readString();
        mPublisher = in.readString();
        mPublishedOn = in.readString();
        mPages = in.readInt();
        mCategories = in.readString();
    }

    public static final Creator<BookDetailModel> CREATOR = new Creator<BookDetailModel>() {
        @Override
        public BookDetailModel createFromParcel(Parcel in) {
            return new BookDetailModel(in);
        }

        @Override
        public BookDetailModel[] newArray(int size) {
            return new BookDetailModel[size];
        }
    };

    public String getIsbn() {
        return mIsbn;
    }

    public void setIsbn(String isbn) {
        mIsbn = isbn;
    }

    public String getTitleBook() {
        return mTitleBook;
    }

    public void setTitleBook(String title) {
        mTitleBook = title;
    }

    public String getSubTitle() {
        return mSubTitle;
    }

    public void setSubTitle(String subTitle) {
        mSubTitle = subTitle;
    }

    public String getDesc() {
        return mDesc;
    }

    public void setDesc(String desc) {
        mDesc = desc;
    }

    public String getAuthors() {
        return mAuthors;
    }

    public void setAuthors(String authors) {
        mAuthors = authors;
    }

    public String getImgUrl() {
        return mImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        mImgUrl = imgUrl;
    }

    public String getPublisher() {
        return mPublisher;
    }

    public void setPublisher(String publisher) {
        mPublisher = publisher;
    }

    public String getPublishedOn() {
        return mPublishedOn;
    }

    public void setPublishedOn(String publishedOn) {
        mPublishedOn = publishedOn;
    }

    public int getPages() {
        return mPages;
    }

    public void setPages(int pages) {
        mPages = pages;
    }

    public String getCategories() {
        return mCategories;
    }

    public void setCategories(String categories) {
        mCategories = categories;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mIsbn);
        dest.writeString(mTitleBook);
        dest.writeString(mSubTitle);
        dest.writeString(mDesc);
        dest.writeString(mAuthors);
        dest.writeString(mImgUrl);
        dest.writeString(mPublisher);
        dest.writeString(mPublishedOn);
        dest.writeInt(mPages);
        dest.writeString(mCategories);
    }
}
