[toc]
# 一：项目介绍
    作为运行在ESB核心组件所在服务器上的提供对外加密api，用来控制ESB服务：新增删除ESB节点服务、修改节点配置参数等操作；项目是springboot打包的jar包
# 二：部署手册
    下载本项目后命令窗口中cd 项目根目录；
    运行mvn clean package
    找到根目录下target目录下的PKISignalServer.jar包
    由于这个jar包中有启动关闭window service的功能，所以需要具有管理员权限，所以我们需要管理员角色打开命令窗口运行这个jar包：java -jar PKISignalServer.jar路径（[如果需要将其运行为windowservice](https://www.cnblogs.com/xiaoping1993/p/11906068.html)请参考）
# 三：配置手册
    ![PKISignalESBServer配置.png](https://github.com/JiPingWangPKI/ESB/raw/master/resource/PKISignalESBServer配置.png)