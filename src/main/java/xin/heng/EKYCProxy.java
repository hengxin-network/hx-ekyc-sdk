package xin.heng;

import xin.heng.dto.*;
import xin.heng.vo.FileInfo;
import xin.heng.vo.UserInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EKYCProxy {

    private IProxyClient client;

    public void injectClient(IProxyClient client) {
        this.client = client;
    }

    private ProxyResponse<String> eKYCGet(String uri, Map<String, String> headers) {
        HashMap<String, Object> proxyBody = new HashMap<>();
        proxyBody.put("method", "POST");
        proxyBody.put("uri", uri);
        return client.post("/ekyc-proxy", headers, client.optToJson(proxyBody));
    }

    private ProxyResponse<String> eKYCPost(String uri, Map<String, String> headers, String body) {
        HashMap<String, Object> proxyBody = new HashMap<>();
        proxyBody.put("method", "POST");
        proxyBody.put("uri", uri);
        proxyBody.put("body", body);
        return client.post("/ekyc-proxy", headers, client.optToJson(proxyBody));
    }

    private ProxyResponse<String> hxGet(String uri, Map<String, String> headers) {
        HashMap<String, Object> proxyBody = new HashMap<>();
        proxyBody.put("method", "GET");
        proxyBody.put("uri", uri);
        return client.post("/hx-proxy", headers, client.optToJson(proxyBody));
    }

    private ProxyResponse<String> hxPost(String uri, Map<String, String> headers, String body) {
        HashMap<String, Object> proxyBody = new HashMap<>();
        proxyBody.put("method", "POST");
        proxyBody.put("uri", uri);
        proxyBody.put("content_type",headers.get("Content-Type"));
        proxyBody.put("body", body);
        return client.post("/hx-proxy", headers, client.optToJson(proxyBody));
    }

    public ProxyResponse<UserInfo> getInfo() {
        String path = "/info";
        ProxyResponse<String> response = hxGet(path, contentTypeJsonHeader());
        ProxyResponse<UserInfo> userResponse = new ProxyResponse<>(response.httpCode, response.originError);
        userResponse.body = client.optFromJson(response.body, UserInfo.class);
        return userResponse;
    }

    public ProxyResponse<UserInfo> postUsers() {
        String path = "/users";
        ProxyResponse<String> response = hxPost(path, contentTypeJsonHeader(), null);
        ProxyResponse<UserInfo> userResponse = new ProxyResponse<>(response.httpCode, response.originError);
        userResponse.body = client.optFromJson(response.body, UserInfo.class);
        return userResponse;
    }

    public ProxyResponse<SnapshotsResponse> getSnapshots(SnapshotsRequest request) {
        String path = "/snapshots";
        HashMap<String, String> queries = new HashMap<>();
        queries.put("from", String.valueOf(request.getFrom()));
        queries.put("limit", String.valueOf(request.getLimit()));
        queries.put("order", request.getOrder());
        if (request.getAsset() != null && request.getAsset().length() != 0) {
            queries.put("asset", request.getAsset());
        }
        ProxyResponse<String> response = hxGet(path, contentTypeJsonHeader());
        ProxyResponse<SnapshotsResponse> snapshotsResponse = new ProxyResponse<>(response.httpCode, response.originError);
        snapshotsResponse.body = client.optFromJson(response.body, SnapshotsResponse.class);
        return snapshotsResponse;
    }

    public ProxyResponse<SnapshotResponse> getSnapshot(long snapshot_id) {
        String path = "/snapshots/" + snapshot_id;
        ProxyResponse<String> response = hxGet(path, contentTypeJsonHeader());
        ProxyResponse<SnapshotResponse> snapshotResponse = new ProxyResponse<>(response.httpCode, response.originError);
        snapshotResponse.body = client.optFromJson(response.body, SnapshotResponse.class);
        return snapshotResponse;
    }

    public ProxyResponse<SnapshotResponse> postTransaction(TransactionRequest request) throws IOException {
        String path = "/transactions";
        HashMap<String, Object> body = new HashMap<>();
        body.put("asset", request.getAsset());
        body.put("opponent_addresses", request.getOpponent_addresses());
        body.put("trace_id", request.getTrace_id());
        body.put("pub_data", request.getPub_data());
        String boundary = UUID.randomUUID().toString();
        ProxyResponse<String> response = hxPost(path, contentTypeMultipartHeader(boundary), new String(convertMultiPartFormData(client, body, null, boundary)));
        ProxyResponse<SnapshotResponse> transactionResponse = new ProxyResponse<>(response.httpCode, response.originError);
        transactionResponse.body = client.optFromJson(response.body, SnapshotResponse.class);
        return transactionResponse;
    }

    public ProxyResponse<SnapshotResponse> postFileTransaction(TransactionRequest request, File file) throws IOException {
        String path = "/transactions";
        HashMap<String, Object> body = new HashMap<>();
        body.put("asset", request.getAsset());
        body.put("opponent_addresses", request.getOpponent_addresses());
        body.put("trace_id", request.getTrace_id());
        body.put("pub_data", request.getPub_data());
        String boundary = UUID.randomUUID().toString();
        byte[] multiPartFormData = convertMultiPartFormData(client, body, file, boundary);
        ProxyResponse<String> response = hxPost(path, contentTypeMultipartHeader(boundary), new String(multiPartFormData));
        ProxyResponse<SnapshotResponse> responseResponse = new ProxyResponse<>(response.httpCode, response.originError);
        responseResponse.body = client.optFromJson(response.body, SnapshotResponse.class);
        return responseResponse;
    }

    public ProxyResponse<String> updatePermission(TransactionRequest request, List<FileInfo> fileInfos) throws IOException {
        String path = "/transactions";
        HashMap<String, Object> body = new HashMap<>();
        body.put("asset", request.getAsset());
        body.put("opponent_addresses", request.getOpponent_addresses());
        body.put("trace_id", request.getTrace_id());
        body.put("pub_data", request.getPub_data());
        body.put("files", client.optToJson(fileInfos));
        String boundary = UUID.randomUUID().toString();
        ProxyResponse<String> response = hxPost(path, contentTypeMultipartHeader(boundary), new String(convertMultiPartFormData(client, body, null, boundary)));
        return response;
    }

    private static Map<String, String> contentTypeJsonHeader() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json;charset=utf-8");
        return headers;
    }

    private static Map<String, String> contentTypeMultipartHeader(String boundary) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "multipart/form-data;boundary=" + boundary);
        return headers;
    }


    private static byte[] convertMultiPartFormData(IProxyClient client, Map<String, Object> body, File file, String boundary) throws IOException {
        String end = "\r\n";
        StringBuilder prev = new StringBuilder();

        final StringBuilder formDataBuilder = new StringBuilder();
        byte[] fileBytes;
        if (file != null) {
            if (file.length() > Integer.MAX_VALUE) {
                throw new IOException("File too big, please slice file first.");
            }
            prev.append("--");
            prev.append(boundary);
            prev.append(end);
            prev.append("Content-Disposition: form-data; name=\"file\"; filename=\"");
            prev.append(file.getName());
            prev.append("\"\r\n");
            prev.append("Content-Type: application/octet-stream");
            prev.append(end + end);

            int fileLength = (int) file.length();
            fileBytes = new byte[fileLength];
            FileInputStream is = new FileInputStream(file);
            is.read(fileBytes);
            is.close();

            formDataBuilder.append(end); // 给文件末尾换行
        } else {
            fileBytes = new byte[0];
        }

        body.forEach((k, v) -> {
            formDataBuilder.append("--");
            formDataBuilder.append(boundary);
            formDataBuilder.append(end);
            formDataBuilder.append("Content-Disposition: form-data; name=\"");
            formDataBuilder.append(k);
            formDataBuilder.append("\"");
            formDataBuilder.append(end + end);
            if (v instanceof String) {
                formDataBuilder.append((String) v);
            } else {
                formDataBuilder.append(client.optToJson(v));
            }

            formDataBuilder.append(end);
        });

        StringBuilder after = new StringBuilder();
        after.append("--" + boundary + "--\r\n\r\n");

        byte[] prevBytes = prev.toString().getBytes();
        byte[] formBytes = formDataBuilder.toString().getBytes();
        byte[] afterBytes = after.toString().getBytes();
        byte[] out = new byte[prevBytes.length + fileBytes.length + formBytes.length + afterBytes.length];
        System.arraycopy(prevBytes, 0, out, 0, prevBytes.length);
        System.arraycopy(fileBytes, 0, out, prevBytes.length, fileBytes.length);
        System.arraycopy(formBytes, 0, out, prevBytes.length + fileBytes.length, formBytes.length);
        System.arraycopy(afterBytes, 0, out, prevBytes.length + fileBytes.length + formBytes.length, afterBytes.length);

        return out;
    }
}
