package xin.heng.vo;

import java.util.List;

public class Snapshot {
    private long id;
    private String created_at;
    private String tx_hash;
    private String asset;
    private String user_id;
    private String opponent_id;
    private String amount;
    private PubData pub_data;
    private String memo;
    private List<FileInfo> files;
    private long height;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getTx_hash() {
        return tx_hash;
    }

    public void setTx_hash(String tx_hash) {
        this.tx_hash = tx_hash;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOpponent_id() {
        return opponent_id;
    }

    public void setOpponent_id(String opponent_id) {
        this.opponent_id = opponent_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public PubData getPub_data() {
        return pub_data;
    }

    public void setPub_data(PubData pub_data) {
        this.pub_data = pub_data;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public List<FileInfo> getFiles() {
        return files;
    }

    public void setFiles(List<FileInfo> files) {
        this.files = files;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }
}
