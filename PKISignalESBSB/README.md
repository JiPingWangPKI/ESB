[toc]
# 一：项目介绍
    项目是由Streambase10.3.0流处理软件构建而成、担任ESB项目的核心组件中转LIS、UIS、LIS等业务系统的数据总线；具备高并发特性，抗压能力很强，可配置为window service服务；同时提供了liveView实时数据流转的有权限限制的对外开放API，这个api由spotfire作为stream流数据源接入后可做实时数据分析、预警等操作;
    PKISignals_sb10：Streambase项目中的LiveView项目，这个LiveView项目以EvenFlow做为自己的流数据，而EvenFlow模块用于接收其他业务系统的数据（webserice服务）从而打通接入实时数据流存到数据库同时做为实时数据流给到LiveView，对外到spotfire做实时数据分析和预警等操作
    PKISignals_app：sb10.3.0之后版本打包sb项目的方式用streambase applicaiton方式统一打包sb项目，在这个项目中提供了 很多可供配置的配置文件
# 二：部署手册  
    1）PKISignals_sb10 右键 mvn clean install 打包为ep-liveview-fragment项目
    2）PKISignals_app项目中已pom了PKISignals_sb10，项目右键streambase->create streambase application archive最终在target文件下得到了PKISignals-0.0.1-SNAPSHOT-ep-liveview-fragment.zip文件改下名字PKISignals_app.zip
    3）之后就用这个打包好的文件做配置后运行为window service服务
## streambase serivce runing as window service
    参考：TIBCO StreamBase Documentation > Installation Guide > Configuring Windows as a Service 
	1)找到打包好的项目文件PKISignals\PKISignals_app\target\PKISignals_app-0.0.1-SNAPSHOT-ep-application.zip
	2)创建文件夹c:\java\streambase
	3)管理员角色打开StreamBase 10.3 Command Prompt（默认在C:\ProgramData\Microsoft\Windows\Start Menu\Programs\TIBCO\StreamBase 10.3在这个文件夹下）->输入cd c:\java\streambase
	4)执行命令将打包好的应用程序PKISignals_app-0.0.1-SNAPSHOT-ep-application.zip
	注意：为了让ESB后台控制程序找到window服务和节点文件夹，安装时候特意指定节点文件夹名称为PKISiganls.{window service name}，为了和sb7安装的服务区别开来，SB10后面都用一个_SB10结尾
    安装为window service脚本
    epadmin install systemservice --name="PKIESB_SB10" --nodedirectory=C:\java\streambase --nodename=PKISignals.PKIESB_SB10 --application=PKISignals_app.zip --displayname=PKIESB_SB10 --startup=automatic
    卸载window service 脚本
    epadmin remove systemservice --name="PKIESB_SB10"
	脚本意思：将应用程序PKISignals_app.zip安装为window serivce名字叫PKIESB_SB10，服务显示名称PKIESB_SB10,节点文件夹路径c:/java/streambase,节点名称PKISignals.PKIESB_SB10,window启动方式为automatic自动启动
# 三：配置手册
    参考修改两种方式：安装包中修改配置、节点文件夹中修改配置
	方式一：安装包中修改配置，打开PKISignals_app-0.0.1-SNAPSHOT-ep-application.zip看到的配置文件和打开内部压缩包com.perkinelmer-PKISignals-0.0.1-SNAPSHOT-ep-liveview-fragment.zip后看到的配置文件
	方式二：项目运行为window service后会根据我们按照window serivce的脚本会出现一个节点文件夹A.X，打开找到路径C:\java\streambase\A.X\application\fragments\com.perkinelmer.PKISignals下的配置文件；C:\java\streambase\A.X\application\shared下的配置文件
	具体哪些配置文件改哪些参数请看下图
![配置文件集一](https://github.com/JiPingWangPKI/ESB/raw/master/resource/LiveViewPConfigs.png)
![配置文件集二](https://github.com/JiPingWangPKI/ESB/raw/master/resource/SBApplicationConfigs.png)

# 四：资料共享
## 4.1[Streambase开发手册](https://github.com/JiPingWangPKI/ESB/blob/master/resource/streambase开发手册.md)
## 4.2[StreambaseLiveView开发手册](https://github.com/JiPingWangPKI/ESB/blob/master/resource/streambaseLiveView开发手册.md)