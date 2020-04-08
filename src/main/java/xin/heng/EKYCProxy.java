package xin.heng;

import xin.heng.dto.ProxyResponse;
import xin.heng.dto.SnapshotsRequest;
import xin.heng.dto.SnapshotsResponse;
import xin.heng.vo.UserInfo;

import java.util.HashMap;
import java.util.Map;

public class EKYCProxy {
    private IProxyClient client;

    private ProxyResponse<String> get(String address, String uri, Map<String, String> queries) {
        HashMap<String, Object> proxyBody = new HashMap<>();
        proxyBody.put("method", "POST");
        proxyBody.put("uri", uri);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json;charset=utf-8");
        return client.post(uri, headers, queries, proxyBody);
    }

    private ProxyResponse<String> post(String address, String uri, Map<String, String> queries, Map<String, Object> body) {
        HashMap<String, Object> proxyBody = new HashMap<>();
        proxyBody.put("method", "POST");
        proxyBody.put("uri", uri);
        proxyBody.put("body", body);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json;charset=utf-8");
        return client.post("/proxy", headers, queries, proxyBody);
    }

    public ProxyResponse<UserInfo> getInfo(String address) {
        String path = "/info";
        ProxyResponse<String> response = get(address, path, null);
        ProxyResponse<UserInfo> userResponse = new ProxyResponse<>(response.httpCode, response.originError);
        userResponse.body = client.optFromJson(response.body, UserInfo.class);
        return userResponse;
    }

    public ProxyResponse<UserInfo> postUsers(String address) {
        String path = "/users";
        ProxyResponse<String> response = post(address, path, null, null);
        ProxyResponse<UserInfo> userResponse = new ProxyResponse<>(response.httpCode, response.originError);
        userResponse.body = client.optFromJson(response.body, UserInfo.class);
        return userResponse;
    }

}
