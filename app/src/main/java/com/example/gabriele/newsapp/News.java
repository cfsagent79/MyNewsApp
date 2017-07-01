package com.example.gabriele.newsapp;

/**
 * Created by Gabriele on 01/07/2017.
 */

/**
 * A (@link News) object contains information related to a single News.
 */

public class News {

    /**
     * News title
     */
    private String mWebTitle;

    /**
     * News Section
     */
    private String mSectionName;

    /**
     * News Date
     */
    private String mWebPublicationDate;

    /**
     * News URL
     */
    private String mNewsUrl;



    /**
     * Construct a new News object
     * @param title    is the title of the news
     * @param section   is the section which news  belongs
     * @param date      is the publication date
     */
    public News(String title, String section, String date, String newsUrl) {
        mWebTitle = title;
        mSectionName = section;
        mWebPublicationDate = date;
        mNewsUrl = newsUrl;
    }
    /**
     * Returns the title of the News
     */
    public String getTitle() {
        return mWebTitle;
    }

    /**
     * Returns the section whic the news belongs
     */
    public String getSection() {
        return mSectionName;
    }

    /**
     * Returns the publication date
     */

    public String getDate () { return mWebPublicationDate; }

    /**
     * Returns the URL of the book
     */
    public String getNewsUrl() {
        return mNewsUrl;
    }

}
