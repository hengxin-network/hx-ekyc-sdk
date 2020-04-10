package xin.heng.vo;

public class PubData<D> {
    private String t; // type
    private D d; // data

    public String getT() {
        return t;
    }

    public PubData<D> setT(String t) {
        this.t = t;
        return this;
    }

    public D getD() {
        return d;
    }

    public PubData<D> setD(D d) {
        this.d = d;
        return this;
    }
}
