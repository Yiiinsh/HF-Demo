package org.tju.HFDemo.web.model;

/**
 * Created by shaohan.yin on 13/05/2017.
 */
public class BlockInfo {
    private long number;
    private String previousHash;
        private String data;

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
