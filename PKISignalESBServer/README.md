[toc]
# 一：项目介绍
## 1.1 项目简介
	上海仁济南院需要搭建一个集成平台，需要接入各个业务系统中转，并做业务数据的实时分析和预警，为此streambase可以担任ESB数据总线的角色；现稳定运行于上海仁济南院，中转HIS与业务系统LIS、UIS、RIS等的信息交互中转站、再结合spotfire做业务数据的分析和实时预警；
整个平台有四个部分：

1）[PKISignalsSB](https://github.com/JiPingWangPKI/ESB/tree/master/PKISignals)：streambase服务担任ESB数据总线
2）[PKISignalServer](https://github.com/JiPingWangPKI/ESB/tree/master/PKISignals):ESB所在机器提供的对外操控API
3）[PKISignalsManager](https://github.com/JiPingWangPKI/ESB/tree/master/)：ESB项目后台管理系统，可以控制ESB服务的很多配置（监控节点状态、修改占用端口、服务开关、创建新ESB节点组件、删除ESB节点组件......） 

4）[spotfire服务](还没有配置出来，等出来再填):分析流转数据、实时预警、监控
## 1.2 测试环境地址
1）[www.webxml.com.cn的ESB中转服务](http://47.103.133.15:8882/WebServices/StockInfoWS.asmx?wsdl)

2）[实时业务数据的LiveView平台](http://47.103.133.15:10080/)(username:xiaoping;password:xiaoping)

3）[实时业务数据的spotfire预警平台](http://47.103.133.15:10080/lvweb/#/login)

4）[ESB后台管理系统](http://47.103.133.15:8888/WSManager/login)（username:admin;password:admin）

# 二：项目文件夹介绍
	PKISignalESBManager:ESB后台管理项目，Springboot项目
	PKISignalESBSB:核心组件担任数据总线ESB，Streambase项目
	PKISignalESBServer:后台API服务，Springboot项目

# 三：部署手册
## 3.1配置环境
准备四台机器，**机器A**（部署PKISignalsESB服务、部署后台API服务、）**机器B**（做负载备份机部署ESB服务、后台API服务）、**机器C**作负载均衡机器用来负载机器A机器B的网络请求（具体如何搭建可参考nginx或直接找外包团队提供，本项目使用的是其他负载均衡厂商）、**机器D**（部署后台管理系统PKISignalsManager、数据库、spotfire做实时分析、离线分析和预警，这三个服务也可以分别使用一台机器搭建）

安装streambase10.4.4（[参考官方链接](https://docs.tibco.com/emp/sb-cep/10.3.0/doc/html/install/index.html)）：现在sb只有7.7.7和10.4.4两个版本了，虽说我之前用的是10.3.0但现在使用10.4.4依然可以用

安装spotfire最新版([参考官方链接服务端](https://docs.tibco.com/pub/spotfire_server/10.6.0/doc/pdf/TIB_sfire_server_10.6.0_installation.pdf)：数据分析服务端；[官方链接客户端](https://docs.tibco.com/pub/spotfire_server/10.3.4/doc/html/TIB_sfire_server_tsas_admin_help/GUID-DA9AF747-B0AD-42FD-85DF-17D859837E93.html):数据分析客户端)

配置jdk1.8环境（[参考链接](https://www.cnblogs.com/xiaoping1993/p/java01.html)）：我也测试了版本jdk13但有问题还是jdk8稳定

配置负载均衡（[参考链接niginx配置负载均衡](https://www.cnblogs.com/qlqwjy/p/8536779.html)）:负载均衡搭建核心组件（简单方式）
## 3.2配置为window service
	参考：TIBCO StreamBase Documentation > Installation Guide > Configuring Windows as a Service 
	1)找到打包好的项目文件PKISignals\PKISignals_app\target\PKISignals_app-0.0.1-SNAPSHOT-ep-application.zip
	2)创建文件夹c:\java\streambase
	3)管理员角色打开StreamBase 10.3 Command Prompt（默认在C:\ProgramData\Microsoft\Windows\Start Menu\Programs\TIBCO\StreamBase 10.3在这个文件夹下）->输入cd c:\java\streambase
	4)执行命令将打包好的应用程序PKISignals_app-0.0.1-SNAPSHOT-ep-application.zip安装为window service
	安装为window service脚本
	epadmin install systemservice --name="PKIWS" --nodedirectory=C:\java\streambase --nodename=A.X --application=PKISignals_app-0.0.1-SNAPSHOT-ep-application.zip --displayname=PKIWS --startup=automatic
	卸载window service 脚本
	epadmin remove systemservice --name="PKIWS"
	脚本意思：将应用程序PKISignals_app-0.0.1-SNAPSHOT-ep-application.zip安装为window serivce名字叫PKIWS，服务显示名称PKI,节点文件夹路径c:/java/streambase,节点名称A.X,window启动方式为automatic自动启动
## 3.3参数配置
	参考修改两种方式：安装包中修改配置、节点文件夹中修改配置
	方式一：安装包中修改配置，打开PKISignals_app-0.0.1-SNAPSHOT-ep-application.zip看到的配置文件和打开内部压缩包com.perkinelmer-PKISignals-0.0.1-SNAPSHOT-ep-liveview-fragment.zip后看到的配置文件
	方式二：项目运行为window service后会根据我们按照window serivce的脚本会出现一个节点文件夹A.X，打开找到路径C:\java\streambase\A.X\application\fragments\com.perkinelmer.PKISignals下的配置文件；C:\java\streambase\A.X\application\shared下的配置文件
	具体哪些配置文件改哪些参数请看下图
![配置文件集一](https://github.com/JiPingWangPKI/ESB/raw/master/resource/LiveViewPConfigs.png)
![配置文件集二](https://github.com/JiPingWangPKI/ESB/raw/master/resource/SBApplicationConfigs.png)
# 四：资料共享
## 4.1[Streambase开发手册](https://github.com/JiPingWangPKI/ESB/blob/master/resource/streambase开发手册.md)
## 4.2[StreambaseLiveView开发手册](https://github.com/JiPingWangPKI/ESB/blob/master/resource/streambaseLiveView开发手册.md)