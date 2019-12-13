[toc]
# 一：项目介绍
## 1.1 项目简介
	上海仁济南院需要搭建一个集成平台，需要接入各个业务系统中转，并做业务数据的实时分析和预警，为此streambase可以担任ESB数据总线的角色；现稳定运行于上海仁济南院，中转HIS与业务系统LIS、UIS、RIS等的信息交互中转站、再结合spotfire做业务数据的分析和实时预警；
整个平台有四个部分：

1）[PKISignalSBESB](https://github.com/JiPingWangPKI/ESB/tree/master/PKISignalESBSB)：streambase服务担任ESB数据总线

2）[PKISignalServer](https://github.com/JiPingWangPKI/ESB/tree/master/PKISignalESBServer):ESB所在机器提供的对外操控API

3）[PKISignalsManager](https://github.com/JiPingWangPKI/ESB/tree/master/PKISignalESBManager)：ESB项目后台管理系统，可以控制ESB服务的很多配置（监控节点状态、修改占用端口、服务开关、创建新ESB节点组件、删除ESB节点组件......,就是调用运行在PKISignalsSBESB所在服务器上的PKISignalServer提供的服务来操控的） 

4）[spotfire服务](还没有配置出来，等出来再填):分析流转数据、实时预警、监控
## 1.2 测试环境地址
1）[www.webxml.com.cn的ESB中转服务](http://47.103.133.15:8882/WebServices/StockInfoWS.asmx?wsdl)

2）[实时业务数据的LiveView平台](http://47.103.133.15:10080/)(username:xiaoping;password:xiaoping)

3）[实时业务数据的spotfire预警平台](http://47.103.133.15:90/spotfire/wp/analysis?file=/ESB/%E5%AE%9E%E6%97%B6%E5%88%86%E6%9E%90%E5%92%8C%E9%A2%84%E8%AD%A6CallLogs&waid=ZdxCFLCNfkyXwoAqCqM-V-12163271e0F210&wavid=0)

4）[ESB后台管理系统](http://47.103.133.15:8888/WSManager/login)（username:admin;password:admin）

# 二：部署手册
## 3.1配置环境
准备四台机器，**机器A**（部署PKISignalsESB服务、部署后台API服务、）**机器B**（做负载备份机部署ESB服务、后台API服务）、**机器C**作负载均衡机器用来负载机器A机器B的网络请求（具体如何搭建可参考nginx或直接找外包团队提供，本项目使用的是其他负载均衡厂商）、**机器D**（部署后台管理系统PKISignalsManager、数据库、spotfire做实时分析、离线分析和预警，这三个服务也可以分别使用一台机器搭建）

安装streambase10.4.4（[参考官方链接](https://docs.tibco.com/emp/sb-cep/10.3.0/doc/html/install/index.html)）：现在sb只有7.7.7和10.4.4两个版本了，虽说我之前用的是10.3.0但现在使用10.4.4依然可以用

安装spotfire最新版([参考官方链接服务端](https://docs.tibco.com/pub/spotfire_server/10.6.0/doc/pdf/TIB_sfire_server_10.6.0_installation.pdf)：数据分析服务端；[官方链接客户端](https://docs.tibco.com/pub/spotfire_server/10.3.4/doc/html/TIB_sfire_server_tsas_admin_help/GUID-DA9AF747-B0AD-42FD-85DF-17D859837E93.html):数据分析客户端)

配置jdk1.8环境（[参考链接](https://www.cnblogs.com/xiaoping1993/p/java01.html)）：我也测试了版本jdk13但有问题还是jdk8稳定

配置负载均衡（[参考链接niginx配置负载均衡](https://www.cnblogs.com/qlqwjy/p/8536779.html)）:负载均衡搭建核心组件（简单方式）

## 3.2 安装使用（参考各自子项目的文档介绍）
	1）PKISignalESBSB服务的安装配置（推荐安装为window service）
	2）PKISignalServer安装配置（推荐安装为window service）
	3）PKISIgnalManager安装配置（推荐安装为window service）
	4）spotfire的安装配置（不配置安装也可以主要用于分析用的）
	PKISignalManager通过PKISignalServer管理PKISignalSBESB服务
参考资料：
[tomcat安装为window service](https://www.cnblogs.com/xiaoping1993/p/11906068.html)
[springboot jar包安装为window service](https://www.cnblogs.com/xiaoping1993/p/11906068.html)
[StreamBase 安装为window service](https://github.com/JiPingWangPKI/ESB/tree/master/PKISignalESBSB#streambase-serivce-runing-as-window-service)

# 四：注意事项
	PKISignalESBManager后台关系系统其中对于workflow节点的参数修改中lvUserName,lvPassWord只能看不能修改否则会出问题，但可以liveView改端口