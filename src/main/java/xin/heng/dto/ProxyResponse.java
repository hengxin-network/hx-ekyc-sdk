package xin.heng.dto;

public class ProxyResponse<T> {
    public T body;
    public int httpCode;
    public Exception originError = null;

    public ProxyResponse(){
    }

    public ProxyResponse(int code, Exception e) {
        this.httpCode = code;
        this.originError = e;
    }
}
