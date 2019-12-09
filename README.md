[toc]
# 一：项目介绍
## 1.1 项目简介
	上海仁济南院需要搭建一个集成平台，需要接入各个业务系统中转，并做业务数据的实时分析和预警，为此streambase可以担任ESB数据总线的角色；现稳定运行于上海仁济南院，中转HIS与业务系统LIS、UIS、RIS等的信息交互中转站、再结合spotfire做业务数据的分析和实时预警
## 1.2 测试环境地址
1）[www.webxml.com.cn的ESB中转服务](http://47.103.133.15:8881/WebServices/StockInfoWS.asmx?wsdl)

2）[实时业务数据的LiveView平台](http://47.103.133.15:10070/lvweb/#/login)(username:xiaoping;password:xiaoping)

3）[实时业务数据的spotfire预警平台](http://47.103.133.15:10070/lvweb/#/login)

4）[ESB后台管理系统](http://47.103.133.15:8888/WSManager/login)（username:admin;password:admin）
	
# 二：环境准备
[streambase10.4.4](shttps://edelivery.tibco.com/storefront/view-download.ep?sku=11032-4&version=10.4.4)现在sb只有7.7.7和10.4.4两个版本了，虽说我之前用的是10.3.0但现在使用10.4.4依然可以用

[jdk8.0](https://download.oracle.com/otn/java/jdk/8u231-b11/5b13a193868b4bf28bcb45c792fce896/jdk-8u231-windows-x64.exe)我也测试了版本jdk13但有问题还是jdk8稳定

# 三：部署手册
## 3.1配置环境
安装streambase10.4.4（[参考官方链接](https://docs.tibco.com/emp/sb-cep/10.3.0/doc/html/install/index.html)）

配置jdk1.8环境（[参考链接](https://www.cnblogs.com/xiaoping1993/p/java01.html)）
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
![配置文件集一](https://raw.githubusercontent.com/JiPingWangPKI/ESB/master/resource/SBApplicationConfigs.png?token=AJNF2D52UH46C7YKBOP3TWK55YF7A)
![配置文件集二](https://raw.githubusercontent.com/JiPingWangPKI/ESB/master/resource/SBApplicationConfigs.png?token=AJNF2DY2Q3NSB6ODILZ7RKS55YGBY)
# 四：开发手册
## 4.1[Streambase开发手册](https://github.com/JiPingWangPKI/ESB/blob/master/resource/streambase开发手册.md)
## 4.2[StreambaseLiveView开发手册](https://github.com/JiPingWangPKI/ESB/blob/master/resource/streambaseLiveView开发手册.md)