## 1.LiveView能做的事
    1.1 原理
        一个LiveView项目首先需要配置一个Table,然后需要有一个数据流，流内成员正好符合liveview配置到table字段，此时这个实时的数据流就会流入table中，由table来管理，监听这些实时的临时的数据
    1.2 liveView Web中配置table展示方式（表，图），动态展示可视化展示实时数据
    1.3 监听实时数据，如果莫条数据超过限定值，可以配置报警提示
        
## 2.如何在已有streambase服务上集成LiveView
    问题未解决，现列出已解决问题
    2.1 将liveView集成到streambase application中
        这里我们可以参考之前EvenFlow项目集成到streambase applicaion中模式，liveView项目集成也是一样，但需要注意的是，如果你的LIveView项目是通过在原有EvenFlow项目中修改过来（单纯添加lvconf等实时表配置信息）在需要将其打包为<packaging>ep-liveview-fragment</packaging>项目给到streabase application中
    2.2 streambase applicaition中deloy对于EvenFlow的参数修改不生效了？待解决
        经过与tibco support的沟通，但运行的是LiveView服务时，针对evenFlow的deploy配置文件就是不会生效的，然后找了个跟deploy功能相似的配置文件叫PKIWS.lvconf(给Livetable（在我的项目中叫CallLogs.conf）提供数据源的evenFlow项目的指定文件)中指定evenFlow的parameters
        ![lvconf中配置evenflow的参数](https://note.youdao.com/yws/res/16213/WEBRESOURCE3ad520618f56f08c2fddaedf7287c2fa)
    2.3 将此streambase application 安装为window service时解决报错“Unable to the retrieve start-up status from the node: Node cannot perform requested command. Reason: switch notifier not available: not found out after 120 seconds.”？
        这个问题今天解决了：只要将java环境由JDK13变为JDK8
## 3.LiveView展示数据中文乱码
    参考帮助文档：TIBCO Live Datamart Documentation > LiveView Admin Guide
    增加配置文件ldmengine.conf
    name = "ldmengine"
    version = "1.0.0"
    type = "com.tibco.ep.ldm.configuration.ldmengine"

    // LiveView-specific engine configuration schema. A subclass of the EventFlow engine configuration.
    configuration = {

	// LiveView engine configuration. A subclass of the EventFlow engine configuration.
	LDMEngine = {
		systemProperties = {"file.encoding" = "UTF-8", "streambase.tuple-charset" 
      = "UTF-8"}
	}
}
## 4.LiveView配置用户名密码
    增加几个liveView验证相关的配置文件即可 
<html>
<details>
<summary>authRealm.conf</summary>
<pre><code>
name = "localAuthRealm"
version = "1.0.0"
type = "com.tibco.ep.dtm.configuration.security"
configuration = {
  LocalAuthenticationRealm = {
    name = "localAuthRealm"
    initialPrincipals = [
      {
        userName = "xiaoping"
        password ="xiaoping"
        roles = [ "LVAdmin" ]
      }
    ]
  }
}
</code></pre>
</details>
<details>
<summary>clientApiListener.conf</summary>
<pre><code>
name = "clientApiListener"
type = "com.tibco.ep.ldm.configuration.ldmclientapilistener"
version = "1.0.0"

// This configuration object contains settings for a LiveView engine's client API listener.
configuration = {

    // LiveView client API listener address
    ClientAPIListener = {

        // Authentication realm associated with this listener, indicating that user authentication is to be performed
        // for requests handled by this listener. This key is optional and has no default value.
        authenticationRealmName = "localAuthRealm"

        // TCP port for the LiveView client API to listen on. This key is optional and its default value is 10080. A
        // zero value means that the server will find a random port to listen on.
        portNumber = 10070
    }
}

</code></pre>
</details>
<details>
<summary>roleMappings.conf</summary>
<pre><code>
name = "my-role-mappings"
version = "1.0.0"
type = "com.tibco.ep.dtm.configuration.security"
configuration = {
  RoleToPrivilegeMappings = {
    privileges = {
      LVAdmin = [
        {
          privilege = "LiveViewAll"
        }
      ]
    }
  }
}
</code></pre>
</details>
<details>
<summary>internalCredentials.conf</summary>
<pre><code>
name = "my-internal-credentials"
type = "com.tibco.ep.ldm.configuration.ldminternalcredentials"
version = "1.0.0"
configuration = {
  InternalCredentials = {
    ldmInternalUserName = "xiaoping"
    ldmInternalPassword = "xiaoping"
  }
}
</code></pre>
</details>
</html>
    
    
## 5.给LiveView增加定时任务（定时清除表缓存）
    方式一：通过lv-client方式参考TIBCO StreamBase Documentation > StreamBase References > StreamBase Command Reference
    方式二：通过访问web客户端（http://ip:10080）如图配置定时任务
    

## 6.研发中需要注意的点
    3.1 最好的管理liveView Table方法
        https://community.tibco.com/wiki/what-are-most-frequently-used-ways-trim-rows-liveview-table
        控制表的大小：
            3.1.1 命令窗口中：
            lv-client -u sb://lvserver:port "delete from tablename where predicate"；
            lv-client -u sb://localhost:10000 "delete from ItemSales where Item='Wagon'"
            3.1.2 lv 控件
            在streambase中的lv控件都是java控件调用了liveView 的javaapi,javascript api来操作live表
            3.1.3 通过配置alert来控制
                建议(Time Base Alert0)
                    内容：0* * * * ? *：每分钟执行一次
    3.2 解决liveView Web页面出现中文乱码
    3.3 解决liveView Web页面表字段值被html自动解析为其他内容