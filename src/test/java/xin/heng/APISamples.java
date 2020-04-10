package xin.heng;

import com.alibaba.fastjson.JSON;
import xin.heng.dto.*;
import xin.heng.vo.PubData;
import xin.heng.vo.UserInfo;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.UUID;

public class APISamples {

    static String testAsset = "6b4d1e14ea651021fa5720b9b6e540fcc048760733bc1b0c8756eb84af40f0fa";
    static String opponentAddress = "HX19vwbnbazqbynReuZfywqLRN7XqVWDwCp4MDDLhjWarWCJ7ic3Lnnm8uLLvXSHJSvtEwywq9Qobn9X8rD6VX73AHMJZFNnmHhhcWQbA69kgtqyrixmSAndwX8RL9tu1HSk7SGwDcRzqcvJWXuwids9p3osiHYFDnirhHW5P6qHBDbfbUnQAmSwqDjg7Sm5oExqH22eEfHNEjzgsCUkfVFbMU6";

    public static void main(String[] args) throws IOException {


        EKYCProxy proxy = new EKYCProxy();
        proxy.injectClient(new ProxyHttpClient(new URL("http://106.14.59.4:8950")));

        System.out.println("UserInfoResponse:");
        ProxyResponse<UserInfo> info = proxy.getInfo();
        System.out.println("UsersResponse:");
        ProxyResponse<UserInfo> users = proxy.postUsers();

        SnapshotsRequest snapshotsRequest = new SnapshotsRequest();
        snapshotsRequest.setAsset(testAsset);
        snapshotsRequest.setOrder(SnapshotsRequest.ORDER_DESC);
        System.out.println("SnapshotsResponse:");
        ProxyResponse<SnapshotsResponse> snapshots = proxy.getSnapshots(snapshotsRequest);

        TransactionRequest transactionRequest = new TransactionRequest()
                .setAsset(testAsset)
                .setOpponent_addresses(Collections.singletonList(opponentAddress))
                .setPub_data(new PubData().setD("test-d").setH("test-h").setT("test-t"))
                .setTrace_id(UUID.randomUUID().toString());

        System.out.println("postTransaction:");
        ProxyResponse<SnapshotResponse> postTransaction = proxy.postTransaction(transactionRequest);
//        System.out.println(JSON.toJSONString(postTransaction));

        File file = new File("./src/test/java/xin/heng/ProxyHttpClient.java");
        System.out.println("postFileTransaction:");
        ProxyResponse<SnapshotResponse> postFileTransaction = proxy.postFileTransaction(transactionRequest, file);
        System.out.println(JSON.toJSONString(postFileTransaction));


    }

}
