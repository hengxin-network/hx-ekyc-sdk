package xin.heng;

import com.alibaba.fastjson.JSON;
import okhttp3.*;
import xin.heng.dto.ProxyResponse;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;

public class ProxyHttpClient implements IProxyClient {

    private URL url;
    private OkHttpClient client;

    public ProxyHttpClient(URL baseUrl) {
        url = baseUrl;
        client = new OkHttpClient.Builder()
                .connectionSpecs(Arrays.asList(
                        ConnectionSpec.CLEARTEXT,
                        ConnectionSpec.COMPATIBLE_TLS,
                        ConnectionSpec.MODERN_TLS,
                        ConnectionSpec.RESTRICTED_TLS
                ))
                .build();
    }

    @Override
    public URL getBaseUrl() {
        return url;
    }

    @Override
    public ProxyResponse<String> get(String path, Map<String, String> headers) {
        String url = "http://" + getBaseUrl().getHost() + ":" + getBaseUrl().getPort() + path;
        Request request = new Request.Builder()
                .headers(Headers.of(headers))
                .url(url)
                .get()
                .build();
        Call call = client.newCall(request);
        ProxyResponse<String> proxyResponse = new ProxyResponse<>();
        try {
            Response response = call.execute();
            proxyResponse.httpCode = response.code();
            proxyResponse.originError = null;
            proxyResponse.body = response.body().string();
            System.out.println(proxyResponse.body);
        } catch (IOException e) {
            e.printStackTrace();
            proxyResponse.originError = e;
        }
        return proxyResponse;
    }

    @Override
    public ProxyResponse<String> post(String path, Map<String, String> headers, String body) {
        String url = "http://" + getBaseUrl().getHost() + ":" + getBaseUrl().getPort() + path;
        Request request = new Request.Builder()
                .headers(Headers.of(headers))
                .url(url)
                .post(RequestBody.create(body, MediaType.parse(headers.getOrDefault("Content-Type", "application/json; charset=utf-8"))))
                .build();
        Call call = client.newCall(request);
        ProxyResponse<String> proxyResponse = new ProxyResponse<>();
        try {
            Response response = call.execute();
            proxyResponse.httpCode = response.code();
            proxyResponse.originError = null;
            proxyResponse.body = response.body().string();
            System.out.println(proxyResponse.body);
        } catch (IOException e) {
            e.printStackTrace();
            proxyResponse.originError = e;
        }
        return proxyResponse;
    }

    @Override
    public <T> T optFromJson(String jsonString, Class<T> clz) {
        return JSON.parseObject(jsonString, clz);
    }

    @Override
    public <T> String optToJson(T obj) {
        return JSON.toJSONString(obj);
    }
}
