package com.hemaapp.wnw.model;

import com.hemaapp.hm_FrameWork.HemaUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.exception.DataParseException;

/**
 * 用户信息(注意User信息必须继承HemaUser,并且User中不用再包含token字段)
 */
public class User extends HemaUser implements Serializable {

    private static final long serialVersionUID = -8412756093515557415L;

    private String id;
    private String username;
    private String inviter_code;
    private String email;
    private String password;
    private String paypassword;
    private String nickname;
    private String feeaccount;
    private String sex;
    private String age;
    private String mobile;
    private String avatar;
    private String avatarbig;
    private String district_name;
    private String onlineflag;
    private String validflag;
    private String lng;
    private String lat;
    private String deviceid;
    private String devicetype;
    private String channelid;
    private String lastloginversion;
    private String lastlogintime;
    private String content;
    private String regdate;
    private String bankuser;
    private String bankname;
    private String bankcard;
    private String bankaddress;
    private String alipay;
    private String realname;
    private String thirdtype;
    private String thirduid;
    private String birthday;
    private String communityid;
    private String inviter;
    private String commision;
    private String tcommision;
    private String qrcode;
    private String team_xiaofei;
    private String rlevel;
    private String upline;
    private String fans;
    private String flag;
    private String edit_flag;
    private String major;
    private String like;

    public User(JSONObject jsonObject) throws DataParseException {
        super(jsonObject);
        if (jsonObject != null) {
            try {
                id = this.get(jsonObject, "id");
                username = this.get(jsonObject, "username");
                inviter_code = this.get(jsonObject, "inviter_code");
                email = this.get(jsonObject, "email");
                password = this.get(jsonObject, "password");
                paypassword = this.get(jsonObject, "paypassword");
                nickname = this.get(jsonObject, "nickname");
                feeaccount = this.get(jsonObject, "feeaccount");
                sex = this.get(jsonObject, "sex");
                age = this.get(jsonObject, "age");
                mobile = this.get(jsonObject, "mobile");
                avatar = this.get(jsonObject, "avatar");
                avatarbig = this.get(jsonObject, "avatarbig");
                district_name = this.get(jsonObject, "district_name");
                onlineflag = this.get(jsonObject, "onlineflag");
                validflag = this.get(jsonObject, "validflag");
                lng = this.get(jsonObject, "lng");
                lat = this.get(jsonObject, "lat");
                deviceid = this.get(jsonObject, "deviceid");
                devicetype = this.get(jsonObject, "devicetype");
                channelid = this.get(jsonObject, "channelid");
                lastloginversion = this.get(jsonObject, "lastloginversion");
                lastlogintime = this.get(jsonObject, "lastlogintime");
                content = this.get(jsonObject, "content");
                regdate = this.get(jsonObject, "regdate");
                bankuser = this.get(jsonObject, "bankuser");
                bankname = this.get(jsonObject, "bankname");
                bankcard = this.get(jsonObject, "bankcard");
                bankaddress = this.get(jsonObject, "bankaddress");
                alipay = this.get(jsonObject, "alipay");
                realname = this.get(jsonObject, "realname");
                thirdtype = this.get(jsonObject, "thirdtype");
                thirduid = this.get(jsonObject, "thirduid");
                birthday = this.get(jsonObject, "birthday");
                communityid = this.get(jsonObject, "communityid");
                inviter = this.get(jsonObject, "inviter");
                commision = this.get(jsonObject, "commision");
                tcommision = this.get(jsonObject, "tcommision");
                qrcode = this.get(jsonObject, "qrcode");
                team_xiaofei = this.get(jsonObject, "team_xiaofei");
                rlevel = this.get(jsonObject, "rlevel");
                upline = this.get(jsonObject, "upline");
                edit_flag = this.get(jsonObject, "edit_flag");
                fans = this.get(jsonObject, "fans");
                flag = this.get(jsonObject, "flag");
                major = this.get(jsonObject, "major");
                like = this.get(jsonObject, "like");
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    public User(String id, String username, String inviter_code,
                String email, String password, String paypassword,
                String nickname, String feeaccount, String sex,
                String age, String mobile, String avatar, String avatarbig,
                String district_name, String onlineflag, String validflag,
                String lng, String lat, String deviceid, String devicetype,
                String channelid, String lastloginversion, String lastlogintime,
                String content, String regdate, String bankuser,
                String bankname, String bankcard, String bankaddress,
                String alipay, String realname, String thirdtype,
                String thirduid, String birthday, String communityid,
                String inviter, String commision, String tcommision,
                String qrcode, String team_xiaofei, String rlevel,
                String upline, String fans, String flag, String edit_flag,
                String major, String like, String token) {
        super(token);
        this.id = id;
        this.username = username;
        this.inviter_code = inviter_code;
        this.email = email;
        this.password = password;
        this.paypassword = paypassword;
        this.nickname = nickname;
        this.feeaccount = feeaccount;
        this.sex = sex;
        this.age = age;
        this.mobile = mobile;
        this.avatar = avatar;
        this.avatarbig = avatarbig;
        this.district_name = district_name;
        this.onlineflag = onlineflag;
        this.validflag = validflag;
        this.lng = lng;
        this.lat = lat;
        this.deviceid = deviceid;
        this.devicetype = devicetype;
        this.channelid = channelid;
        this.lastloginversion = lastloginversion;
        this.lastlogintime = lastlogintime;
        this.content = content;
        this.regdate = regdate;
        this.bankuser = bankuser;
        this.bankname = bankname;
        this.bankcard = bankcard;
        this.bankaddress = bankaddress;
        this.alipay = alipay;
        this.realname = realname;
        this.thirdtype = thirdtype;
        this.thirduid = thirduid;
        this.birthday = birthday;
        this.communityid = communityid;
        this.inviter = inviter;
        this.commision = commision;
        this.tcommision = tcommision;
        this.qrcode = qrcode;
        this.team_xiaofei = team_xiaofei;
        this.rlevel = rlevel;
        this.upline = upline;
        this.fans = fans;
        this.edit_flag = edit_flag;
        this.flag = flag;
        this.major = major;
        this.like = like;
    }

    public String getEdit_flag() {
        return edit_flag;
    }

    public void setEdit_flag(String edit_flag) {
        this.edit_flag = edit_flag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getInviter_code() {
        return inviter_code;
    }

    public void setInviter_code(String inviter_code) {
        this.inviter_code = inviter_code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPaypassword() {
        return paypassword;
    }

    public void setPaypassword(String paypassword) {
        this.paypassword = paypassword;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFeeaccount() {
        return feeaccount;
    }

    public void setFeeaccount(String feeaccount) {
        this.feeaccount = feeaccount;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatarbig() {
        return avatarbig;
    }

    public void setAvatarbig(String avatarbig) {
        this.avatarbig = avatarbig;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    public String getOnlineflag() {
        return onlineflag;
    }

    public void setOnlineflag(String onlineflag) {
        this.onlineflag = onlineflag;
    }

    public String getValidflag() {
        return validflag;
    }

    public void setValidflag(String validflag) {
        this.validflag = validflag;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }

    public String getLastloginversion() {
        return lastloginversion;
    }

    public void setLastloginversion(String lastloginversion) {
        this.lastloginversion = lastloginversion;
    }

    public String getLastlogintime() {
        return lastlogintime;
    }

    public void setLastlogintime(String lastlogintime) {
        this.lastlogintime = lastlogintime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public String getBankuser() {
        if ("null".equals(bankuser)) {
            return "";
        }
        return bankuser;
    }

    public void setBankuser(String bankuser) {
        this.bankuser = bankuser;
    }

    public String getBankname() {
        if ("null".equals(bankname)) {
            return "";
        }
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getBankcard() {
        if ("null".equals(bankcard)) {
            return "";
        }
        return bankcard;
    }

    public void setBankcard(String bankcard) {
        this.bankcard = bankcard;
    }

    public String getBankaddress() {
        return bankaddress;
    }

    public void setBankaddress(String bankaddress) {
        this.bankaddress = bankaddress;
    }

    public String getAlipay() {
        if ("null".equals(alipay)) {
            return "";
        }
        return alipay;
    }

    public void setAlipay(String alipay) {
        this.alipay = alipay;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getThirdtype() {
        return thirdtype;
    }

    public void setThirdtype(String thirdtype) {
        this.thirdtype = thirdtype;
    }

    public String getThirduid() {
        return thirduid;
    }

    public void setThirduid(String thirduid) {
        this.thirduid = thirduid;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCommunityid() {
        return communityid;
    }

    public void setCommunityid(String communityid) {
        this.communityid = communityid;
    }

    public String getInviter() {
        return inviter;
    }

    public void setInviter(String inviter) {
        this.inviter = inviter;
    }

    public String getCommision() {
        return commision;
    }

    public void setCommision(String commision) {
        this.commision = commision;
    }

    public String getTcommision() {
        return tcommision;
    }

    public void setTcommision(String tcommision) {
        this.tcommision = tcommision;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getTeam_xiaofei() {
        return team_xiaofei;
    }

    public void setTeam_xiaofei(String team_xiaofei) {
        this.team_xiaofei = team_xiaofei;
    }

    public String getRlevel() {
        return rlevel;
    }

    public void setRlevel(String rlevel) {
        this.rlevel = rlevel;
    }

    public String getUpline() {
        return upline;
    }

    public void setUpline(String upline) {
        this.upline = upline;
    }

    public String getFans() {
        return fans;
    }

    public void setFans(String fans) {
        this.fans = fans;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public void changeUserData(UserClient userClient) {
        this.id = userClient.getId();
        this.username = userClient.getUsername();
        this.inviter_code = userClient.getInviter_code();
        this.email = userClient.getEmail();
        this.password = userClient.getPassword();
        this.paypassword = userClient.getPaypassword();
        this.nickname = userClient.getNickname();
        this.feeaccount = userClient.getFeeaccount();
        this.sex = userClient.getSex();
        this.age = userClient.getAge();
        this.mobile = userClient.getMobile();
        this.avatar = userClient.getAvatar();
        this.avatarbig = userClient.getAvatarbig();
        this.district_name = userClient.getDistrict_name();
        this.onlineflag = userClient.getOnlineflag();
        this.validflag = userClient.getValidflag();
        this.lng = userClient.getLng();
        this.lat = userClient.getLat();
        this.deviceid = userClient.getDeviceid();
        this.devicetype = userClient.getDevicetype();
        this.channelid = userClient.getChannelid();
        this.lastloginversion = userClient.getLastloginversion();
        this.lastlogintime = userClient.getLastlogintime();
        this.content = userClient.getContent();
        this.regdate = userClient.getRegdate();
        this.bankuser = userClient.getBankuser();
        this.bankname = userClient.getBankname();
        this.bankcard = userClient.getBankcard();
        this.bankaddress = userClient.getBankaddress();
        this.alipay = userClient.getAlipay();
        this.realname = userClient.getRealname();
        this.thirdtype = userClient.getThirdtype();
        this.thirduid = userClient.getThirduid();
        this.birthday = userClient.getBirthday();
        this.communityid = userClient.getCommunityid();
        this.inviter = userClient.getInviter();
        this.commision = userClient.getCommision();
        this.tcommision = userClient.getTcommision();
        this.qrcode = userClient.getQrcode();
        this.team_xiaofei = userClient.getTeam_xiaofei();
        this.rlevel = userClient.getRlevel();
        this.upline = userClient.getUpline();
        this.fans = userClient.getFans();
        this.flag = userClient.getFlag();
        this.major = userClient.getMajor();
        this.fans = userClient.getFans();
        this.like = userClient.getLike();
        this.major = userClient.getMajor();
        this.edit_flag = userClient.getEdit_flag();
    }

    @Override
    protected String get(JSONObject jsonObject, String s) throws JSONException {
        String str = !jsonObject.isNull(s) ? jsonObject.getString(s) : "";
        return str == null ? "" : str;
    }
}
