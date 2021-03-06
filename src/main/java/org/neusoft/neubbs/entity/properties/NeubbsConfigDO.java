package org.neusoft.neubbs.entity.properties;

import org.neusoft.neubbs.constant.api.SetConst;

/**
 * 配置文件 neubbs.properties 实体类
 *
 * @author Suvan
 */
public class NeubbsConfigDO {

    private Integer cookieAutoLoginMaxAgeDay;
    private Integer topicsApiRequestParamLimitDefault;
    private String userImageUploadPath;
    private String accountApiVaslidateUrl;

    private String emailServiceSendAccountUsername;
    private String emailServiceSendAccountAuthorizationCode;

    private String ftpIp;
    private Integer ftpPort;
    private String ftpUsername;
    private String ftpPassword;

    private String nginxUrl;

    /**
     * Setter
     */
    public void setCookieAutoLoginMaxAgeDay(Integer cookieAutoLoginMaxAgeDay) {
        this.cookieAutoLoginMaxAgeDay = cookieAutoLoginMaxAgeDay;
    }

    public void setTopicsApiRequestParamLimitDefault(Integer topicsApiRequestParamLimitDefault) {
        this.topicsApiRequestParamLimitDefault = topicsApiRequestParamLimitDefault;
    }

    public void setUserImageUploadPath(String userImageUploadPath) {
        this.userImageUploadPath = userImageUploadPath;
    }

    public void setAccountApiVaslidateUrl(String accountApiVaslidateUrl) {
        this.accountApiVaslidateUrl = accountApiVaslidateUrl;
    }

    public void setEmailServiceSendAccountUsername(String emailServiceSendAccountUsername) {
        this.emailServiceSendAccountUsername = emailServiceSendAccountUsername;
    }

    public void setEmailServiceSendAccountAuthorizationCode(String emailServiceSendAccountAuthorizationCode) {
        this.emailServiceSendAccountAuthorizationCode = emailServiceSendAccountAuthorizationCode;
    }

    public void setFtpIp(String ftpIp) {
        this.ftpIp = ftpIp;
    }

    public void setFtpPort(Integer ftpPort) {
        this.ftpPort = ftpPort;
    }

    public void setFtpUsername(String ftpUsername) {
        this.ftpUsername = ftpUsername;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }

    public void setNginxUrl(String nginxUrl) {
        this.nginxUrl = nginxUrl;
    }

    /**
     * Getter
     */
    public Integer getCookieAutoLoginMaxAgeDay() {
        //day -> second
        return cookieAutoLoginMaxAgeDay * SetConst.TWENTY_FOUR * SetConst.SIXTY * SetConst.SIXTY;
    }

    public Integer getTopicsApiRequestParamLimitDefault() {
        return topicsApiRequestParamLimitDefault;
    }

    public String getUserImageUploadPath() {
        return userImageUploadPath;
    }

    public String getAccountApiVaslidateUrl() {
        return accountApiVaslidateUrl;
    }

    public String getEmailServiceSendAccountUsername() {
        return emailServiceSendAccountUsername;
    }

    public String getEmailServiceSendAccountAuthorizationCode() {
        return emailServiceSendAccountAuthorizationCode;
    }

    public String getFtpIp() {
        return ftpIp;
    }

    public Integer getFtpPort() {
        return ftpPort;
    }

    public String getFtpUsername() {
        return ftpUsername;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public String getNginxUrl() {
        return nginxUrl;
    }
}
