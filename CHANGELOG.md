### 2020.4.9
* 修改了proxy方法，拆分get为改为hxGet和eKYCGet，post为hxPost和eKYCPost
* 新增了getSnapshots ，postTransaction等方法

### 2020.4.8
* 提供proxy get和proxy post方法，提供IHttpClient抽象注入机制
* 新增getInfo和postUser的proxy调用.