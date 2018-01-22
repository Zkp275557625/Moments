package com.zhoukp.photo.bean;

/**
 * time：2018/1/22 9:57
 * mail：zhoukaiping@szy.cn
 * for：
 *
 * @author zhoukp
 */

public class Contact {
    /**
     * 姓名
     */
    private String name;
    /**
     * 头像链接
     */
    private String headUrl;
    /**
     * 姓名的首字母
     */
    private String sortKey;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public Contact(String name, String headUrl, String sortKey) {
        this.name = name;
        this.headUrl = headUrl;
        this.sortKey = sortKey;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((sortKey == null) ? 0 : sortKey.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Contact other = (Contact) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (sortKey == null) {
            if (other.sortKey != null) {
                return false;
            }
        } else if (!sortKey.equals(other.sortKey)) {
            return false;
        }
        return true;
    }
}
