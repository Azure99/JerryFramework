package com.rainng.jerry.mouse.http;

import com.rainng.jerry.mouse.util.HttpDateHelper;

import java.util.Date;

public class Cookie {
    //Cookie名称
    private String name;
    //Cookie值
    private String value;
    //Cookie过期时间
    private Date expires;
    //Cookie存活时间
    private int maxAge = Integer.MIN_VALUE;
    //Cookie作用域名
    private String domain;
    //Cookie作用路径
    private String path;
    //是否安全
    private boolean secure;

    //此Cookie是否来自HttpRequest
    private boolean fromRequest = false;
    //来自HttpRequest的Cookie是否被修改
    private boolean changed = false;

    /**
     * 创建Cookie
     *
     * @param name  Cookie名称
     * @param value Cookie值
     */
    public Cookie(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * 创建Cookie
     *
     * @param name        Cookie名称
     * @param value       Cookie值
     * @param fromRequest Cookie是否来自HttpRequest
     */
    public Cookie(String name, String value, boolean fromRequest) {
        this(name, value);
        this.fromRequest = fromRequest;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        changed = true;
        this.value = value;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        changed = true;
        this.expires = expires;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        changed = true;
        this.maxAge = maxAge;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        changed = true;
        this.domain = domain;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        changed = true;
        this.path = path;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        changed = true;
        this.secure = secure;
    }

    /**
     * 是否来自于
     *
     * @return
     */
    public boolean isFromRequest() {
        return fromRequest;
    }

    /**
     * 自此Cookie创建后是否进行过修改,
     * 注意: 如果Cookie来自HttpRequest且进行过任何修改 则会写入HttpResponse的Set-Cookie头中
     *
     * @return
     */
    public boolean isChanged() {
        return changed;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(name + "=" + value);

        if (expires != null) {
            builder.append("; Expires=" + HttpDateHelper.getDateString(expires));
        }
        if (maxAge != Integer.MIN_VALUE) {
            builder.append("; Max-Age=" + Integer.toString(maxAge));
        }
        if (domain != null) {
            builder.append("; Domain=" + domain);
        }
        if (path != null) {
            builder.append("; Path=" + path);
        }
        if (secure != false) {
            builder.append("; Secure");
        }

        return builder.toString();
    }
}
