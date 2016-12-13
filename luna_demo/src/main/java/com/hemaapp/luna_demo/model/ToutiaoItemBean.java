package com.hemaapp.luna_demo.model;

/**
 * Created by Administrator on 2016/8/18 0018.
 */
public class ToutiaoItemBean {
    private String title;//":"看了几天热闹 终于回过味 所有人都被这对男女耍了",
    private String date;//":"2016-08-18 07:36",
    private String author_name;//":"企鹅美食坊",
    private String thumbnail_pic_s;//":"http:\/\/00.imgmini.eastday.com\/mobile\/20160818\/20160818073614_fd9d4dcfef04285a110ab6219b20a2ec_1_mwpm_03200403.jpeg",
    private String thumbnail_pic_s02;//":"http:\/\/00.imgmini.eastday.com\/mobile\/20160818\/20160818073614_fd9d4dcfef04285a110ab6219b20a2ec_1_mwpl_05500201.jpeg",
    private String thumbnail_pic_s03;//":"http:\/\/00.imgmini.eastday.com\/mobile\/20160818\/20160818073614_fd9d4dcfef04285a110ab6219b20a2ec_1_mwpl_05500201.jpeg",
    private String url;//":"http:\/\/mini.eastday.com\/mobile\/160818073614130.html?qid=juheshuju",
    private String uniquekey;//":"160818073614130",
    private String type;//":"头条",
    private String realtype;//":"娱乐"

    public ToutiaoItemBean(String title, String date, String author_name, String thumbnail_pic_s, String thumbnail_pic_s02, String thumbnail_pic_s03) {
        this.title = title;
        this.date = date;
        this.author_name = author_name;
        this.thumbnail_pic_s = thumbnail_pic_s;
        this.thumbnail_pic_s02 = thumbnail_pic_s02;
        this.thumbnail_pic_s03 = thumbnail_pic_s03;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getThumbnail_pic_s() {
        return thumbnail_pic_s;
    }

    public void setThumbnail_pic_s(String thumbnail_pic_s) {
        this.thumbnail_pic_s = thumbnail_pic_s;
    }

    public String getThumbnail_pic_s02() {
        return thumbnail_pic_s02;
    }

    public void setThumbnail_pic_s02(String thumbnail_pic_s02) {
        this.thumbnail_pic_s02 = thumbnail_pic_s02;
    }

    public String getThumbnail_pic_s03() {
        return thumbnail_pic_s03;
    }

    public void setThumbnail_pic_s03(String thumbnail_pic_s03) {
        this.thumbnail_pic_s03 = thumbnail_pic_s03;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUniquekey() {
        return uniquekey;
    }

    public void setUniquekey(String uniquekey) {
        this.uniquekey = uniquekey;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRealtype() {
        return realtype;
    }

    public void setRealtype(String realtype) {
        this.realtype = realtype;
    }
}
