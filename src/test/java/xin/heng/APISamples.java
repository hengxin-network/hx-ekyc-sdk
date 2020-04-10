package xin.heng;

import xin.heng.dto.*;
import xin.heng.vo.FileInfo;
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

        System.out.println("getInfo UserInfoResponse:");
        ProxyResponse<UserInfo> info = proxy.getInfo();
        System.out.println("postUsers UserInfoResponse:");
        ProxyResponse<UserInfo> users = proxy.postUsers();

        System.out.println("getSnapshot with id:");
        ProxyResponse<SnapshotResponse> snapshotResponse = proxy.getSnapshot(375);

        SnapshotsRequest snapshotsRequest = new SnapshotsRequest();
        snapshotsRequest.setAsset(testAsset);
        snapshotsRequest.setOrder(SnapshotsRequest.ORDER_DESC);
        System.out.println("getSnapshots SnapshotsResponse:");
        ProxyResponse<SnapshotsResponse> snapshots = proxy.getSnapshots(snapshotsRequest);

//        TransactionRequest transactionRequest = new TransactionRequest()
//                .setAsset(testAsset)
//                .setOpponent_addresses(Collections.singletonList(opponentAddress))
//                .setPub_data(new PubData().setD("test-d").setH("test-h").setT("test-t"))
//                .setTrace_id(UUID.randomUUID().toString());
//
//        System.out.println("postTransaction:");
//        ProxyResponse<SnapshotResponse> postTransaction = proxy.postTransaction(transactionRequest);
//
//        TransactionRequest fileTransactionRequest = new TransactionRequest()
//                .setAsset(testAsset)
//                .setOpponent_addresses(Collections.singletonList(opponentAddress))
//                .setPub_data(new PubData().setD("test-d").setH("test-h").setT("test-t"))
//                .setTrace_id(UUID.randomUUID().toString());
//
//        File file = new File("./src/test/java/xin/heng/ProxyHttpClient.java");
//        System.out.println("postFileTransaction:");
//        ProxyResponse<SnapshotResponse> postFileTransaction = proxy.postFileTransaction(fileTransactionRequest, file);
//
        TransactionRequest filesTransactionRequest = new TransactionRequest()
                .setAsset(testAsset)
                .setOpponent_addresses(Collections.singletonList(opponentAddress))
                .setPub_data(new PubData().setD("test-d").setH("test-h").setT("test-t"))
                .setTrace_id(UUID.randomUUID().toString());

        FileInfo fileInfo = new FileInfo();
        fileInfo.setH("b1d31c3cf98cef34ce9f5c105f8ed488b09a64ec165b753a16e03f9f72ec1472");
        fileInfo.setRh("4285d38c8b1eea23062ebbf366c9db2f3de81778955df534e74b88c5cbf7f44b");
        fileInfo.setS(2992);
        System.out.println("postFileInfosTransaction:");
        ProxyResponse<SnapshotResponse> postFileInfosTransaction = proxy.postFileInfosTransaction(filesTransactionRequest, Collections.singletonList(fileInfo));

        File downloadFile = new File("./download/test_" + System.currentTimeMillis() + ".java");
        if (!downloadFile.exists()) {
            File parent = new File(downloadFile.getParent());
            if (!parent.exists()) parent.mkdirs();
            downloadFile.createNewFile();
        }

        System.out.println("getFile: ");
        ProxyResponse<File> fileResponse = proxy.getFile(fileInfo, downloadFile);
    }

}
