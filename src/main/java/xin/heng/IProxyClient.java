package xin.heng;

import xin.heng.dto.ProxyResponse;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public interface IProxyClient {

    /***
     * Java URL
     * @return URL
     */
    URL getBaseUrl();

    ProxyResponse<String> get(String path, Map<String, String> headers);

    ProxyResponse<String> post(String path, Map<String, String> headers, String body);

    <T> T optFromJson(String jsonString, Class<T> clz);

    <T> String optToJson(T obj);
}
