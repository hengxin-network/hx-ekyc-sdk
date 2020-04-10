package xin.heng.dto;

import xin.heng.vo.PubData;

import java.util.List;

public class TransactionRequest {
    private String asset;
    private List<String> opponent_addresses;
    private String trace_id;
    private PubData pub_data;

    public String getAsset() {
        return asset;
    }

    public TransactionRequest setAsset(String asset) {
        this.asset = asset;
        return this;
    }

    public List<String> getOpponent_addresses() {
        return opponent_addresses;
    }

    public TransactionRequest setOpponent_addresses(List<String> opponent_addresses) {
        this.opponent_addresses = opponent_addresses;
        return this;
    }

    public String getTrace_id() {
        return trace_id;
    }

    public TransactionRequest setTrace_id(String trace_id) {
        this.trace_id = trace_id;
        return this;
    }

    public PubData getPub_data() {
        return pub_data;
    }

    public TransactionRequest setPub_data(PubData pub_data) {
        this.pub_data = pub_data;
        return this;
    }
}
