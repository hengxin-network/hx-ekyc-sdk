# eKYC Proxy SDK

提供了一个EKYCProxy对象来进行对eKYC及链上数据的访问的Java SDK

SDK中包含了Proxy对象，HttpClient的抽象注入和一些数据model.

jar包在 output 文件夹下可以找到

简要 Sample 可以参考src/test/java中的APISamples


0. SDK初始化
```java
    EKYCProxy proxy = new EKYCProxy();
    proxy.injectClient(new ProxyHttpClient(new URL("http://106.14.59.4:8950")));
```

1. 创建新的区块链地址
通过proxy的postUsers方法调用

```java
    ProxyResponse<UserInfo> user = proxy.postUsers();
```

2. 查询记录
有两个方法，一个是根据查询参数返回一组记录，一个是根据id返回单条记录
    - 根据参数返回一组记录
    SnapshotsRequest 参数为 asset, limit, from, order
    ```java
        SnapshotsRequest snapshotsRequest = new SnapshotsRequest();
        snapshotsRequest.setAsset(testAsset);
        snapshotsRequest.setOrder(SnapshotsRequest.ORDER_DESC);
        ProxyResponse<SnapshotsResponse> snapshots = proxy.getSnapshots(snapshotsRequest);
    ```

    - 根据id返回单条记录
    id 是一个long型数字
    ```java
        ProxyResponse<SnapshotResponse> snapshotResponse = proxy.getSnapshot(375);
    ```
3. 下载文件
Snapshot中，如果记录中包含了上传的文件，则会有files这个字段类型,是一个List<FileInfo>

FileInfo对应了文件的信息，文件下载可以通过```getFile```方法来进行下载，getFile需要传入FileInfo和File参数

- FileInfo即为从snapshot中获取到的文件信息
- File是一个本地文件，getFile会把数据写入到该本地文件中

```java
    File downloadFile = new File("./download/test_" + System.currentTimeMillis() + ".java");
        if (!downloadFile.exists()) {
            File parent = new File(downloadFile.getParent());
            if (!parent.exists()) parent.mkdirs();
            downloadFile.createNewFile();
        }
    ProxyResponse<File> fileResponse = proxy.getFile(fileInfo, downloadFile);
```

4. 提交数据上链

提交数据上链分3种情况

- 提交结构化数据上链
- 提交文件
- 使用链上已有文件提交数据

通用的提交参数为```TransactionRequest```其中包含了asset, opponent_addresses, pub_data, trace_id

pub_data中包含t和d，t为type，d即为结构化的数据

提交结构化数据上链使用```postTransaction```方法

```java
    TransactionRequest transactionRequest = new TransactionRequest()
                    .setAsset(testAsset)
                    .setOpponent_addresses(Collections.singletonList(opponentAddress))
                    .setPub_data(new PubData<TestCard>().setD(new TestCard("张三", "4/10")).setT("test-t"))
                    .setTrace_id(UUID.randomUUID().toString());
    ProxyResponse<SnapshotResponse> postTransaction = proxy.postTransaction(transactionRequest);
```

提交文件上链使用```postFileTransaction```方法

除了需要TransactionRequest外，还需要提供需上传的文件File
```java
    TransactionRequest fileTransactionRequest = new TransactionRequest()
                    .setAsset(testAsset)
                    .setOpponent_addresses(Collections.singletonList(opponentAddress))
                    .setPub_data(new PubData<TestCard>().setD(new TestCard("张三", "4/10")).setT("test-t"))
                    .setTrace_id(UUID.randomUUID().toString());
    
    File file = new File("./src/test/java/xin/heng/ProxyHttpClient.java");
    ProxyResponse<SnapshotResponse> postFileTransaction = proxy.postFileTransaction(fileTransactionRequest, file);
```

使用链上已有文件提交数据，除了TransactionRequest外，需要提供已有文件的List<FileInfo>信息

```java
    TransactionRequest filesTransactionRequest = new TransactionRequest()
                    .setAsset(testAsset)
                    .setOpponent_addresses(Collections.singletonList(opponentAddress))
                    .setPub_data(new PubData<TestCard>().setD(new TestCard("张三", "4/10")).setT("test-t"))
                    .setTrace_id(UUID.randomUUID().toString());
    // 这里创建了一个之前在snapshot中已存在的FileInfo记录，也可以直接从已有的Snapshot中getFiles获取到List<FileInfo>
    FileInfo fileInfo = new FileInfo();
    fileInfo.setH("b1d31c3cf98cef34ce9f5c105f8ed488b09a64ec165b753a16e03f9f72ec1472");
    fileInfo.setRh("4285d38c8b1eea23062ebbf366c9db2f3de81778955df534e74b88c5cbf7f44b");
    fileInfo.setS(2992);
    ProxyResponse<SnapshotResponse> postFileInfosTransaction = proxy.postFileInfosTransaction(filesTransactionRequest, Collections.singletonList(fileInfo));
```