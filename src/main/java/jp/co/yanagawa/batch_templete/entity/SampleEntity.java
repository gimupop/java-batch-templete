package jp.co.yanagawa.bachTemplete.entity;

public class AudienceIdEncryption {
    private String encEncryptionKey;
    private String encEncryptionIv;

    public String getEncEncryptionKey() {
        return encEncryptionKey;
    }

    public void setEncEncryptionKey(String encEncryptionKey) {
        this.encEncryptionKey = encEncryptionKey;
    }

    public String getEncEncryptionIv() {
        return encEncryptionIv;
    }

    public void setEncEncryptionIv(String encEncryptionIv) {
        this.encEncryptionIv = encEncryptionIv;
    }
}