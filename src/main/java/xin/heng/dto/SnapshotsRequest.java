package xin.heng.dto;

public class SnapshotsRequest {

    public static final String ORDER_ASC = "ASC";
    public static final String ORDER_DESC = "DESC";

    private String asset;
    private int from;
    private int limit = 20;
    private String order = ORDER_ASC;

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
