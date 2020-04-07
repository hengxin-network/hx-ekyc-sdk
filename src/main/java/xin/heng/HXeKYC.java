package xin.heng;

import xin.heng.service.HXConstants;
import xin.heng.service.HXService;
import xin.heng.service.dto.HXResponse;
import xin.heng.service.vo.HXJwtBuildMaterial;

import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

public class HXeKYC extends HXService {

    private HXResponse<String> proxyGet(String address, String uri) throws SignatureException {
        HashMap<String, Object> proxyBody = new HashMap<>();
        proxyBody.put("method", "POST");
        proxyBody.put("uri", uri);
        byte[] bodyData = HXUtils.convertJsonData(proxyBody);

        HXJwtBuildMaterial jwtMaterial = new HXJwtBuildMaterial();
        jwtMaterial.setAddress(address)
                .setExpiredTime(expiredTime)
                .setBody(bodyData)
                .setRequestMethod(HXConstants.HTTP_METHOD_GET)
                .setUrl(uri);
        String jwtToken = HXUtils.buildJwtString(wallet, jwtMaterial);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwtToken);
        headers.put("Content-Type", "application/json;charset=utf-8");

        return httpClient.post(uri, null, headers, bodyData);
    }

    private HXResponse<String> proxyPost(String address, String uri, Map<String, Object> body) throws SignatureException {
        HashMap<String, Object> proxyBody = new HashMap<>();
        proxyBody.put("method", "POST");
        proxyBody.put("uri", uri);
        proxyBody.put("body", HXUtils.optToJson(body));
        byte[] bodyData = HXUtils.convertJsonData(proxyBody);

        HXJwtBuildMaterial jwtMaterial = new HXJwtBuildMaterial();
        jwtMaterial.setAddress(address)
                .setExpiredTime(expiredTime)
                .setBody(bodyData)
                .setRequestMethod(HXConstants.HTTP_METHOD_GET)
                .setUrl("/proxy");

        String jwtToken = HXUtils.buildJwtString(wallet, jwtMaterial);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwtToken);
        headers.put("Content-Type", "application/json;charset=utf-8");

        return httpClient.post("/proxy", null, headers, bodyData);
    }
}
