[toc]
# 项目介绍
	这是数据总线ESB服务（Streambase项目PKISignalSBESB）的后台管理系统，主要功能有：权限管理、日志管理、ESB服务管理与配置、接入平台系统的管理
# 组织架构
	PKISignalManager
	├── Config --项目配置模板（）
	├── Controller -- 控制层代码模块（主要提供前端api接口服务）
	├── entity -- 实体类模块
	├── Enum -- 枚举类模板
	├── Mapper -- 操作数据库模板
	├── security -- 系统权限模板
	├── Service -- 服务层模板
	└── Utils -- 工具类
# 技术选型

技术|说明|官网
:---:|:---:|:---:
Spring Boot|容器+MVC框架|https://spring.io/projects/spring-boot
Spring Security|认证和授权框架|https://spring.io/projects/spring-security
MyBatis|ORM框架|http://www.mybatis.org/mybatis-3/zh/index.html
	
# 功能手册
其中“账户维护”、“服务维护”，“订阅维护”用来管理接入系统：
系统A若想接入ESB平台，需要给他添加一个账户（账户维护模块），但这还不够，系统A接入平台后提供各种服务，可先注册各自系统服务（即医院通常的webserice服务，服务维护），这时候由管理员订阅指定的账号下指定服务（订阅维护模块），这时候如果ESB服务开通了权限验证，则流入平台的消息，平台会验证账号是否正确，同时验证订阅的服务是否存在，一切通过才能流转到ESB平台；
调用日志：目前可查看日志信息
统计分析：spotfire中离线数据，实时数据的分析和预警
用户配置、角色配置：系统权限配置
workFlow配置：由于ESB接入系统我们为了区分开来会单独开一个ESB服务节点，就是我们这里所谓的worfFlow节点，在这里我们可以控制每个ESB服务的参数、开关、新增删除等操作、功能很管理员谨慎操作
新增workFLow配置功能：获得修改LiveView的端口、liveViewUserName,liveViewPassWord的能力

这里提供部分业务图：

![订阅维护.jpg](https://github.com/JiPingWangPKI/ESB/raw/master/resource/订阅维护.jpg)

![服务维护.jpg](https://github.com/JiPingWangPKI/ESB/raw/master/resource/服务维护.jpg)

![角色配置.jpg](https://github.com/JiPingWangPKI/ESB/raw/master/resource/角色配置.jpg)

![信息集成平台-管理ESB页面.png](https://github.com/JiPingWangPKI/ESB/raw/master/resource/信息集成平台-管理ESB页面.png)

# 部署手册
    1.配置java1.8，tomcat9.0，maven3.3.9环境（自行百度）
    2.项目资源文件下载下来后，命令窗口中输入cd 项目路径；mvn clean package
    3.在项目路径下的target文件夹下找到打包好的war包
    4.将此war包放到tomcat容器中，配置好tomcat启动运行即可
    5.数据库准备：下载sqlserver2017,找到项目文件下ESB\PKISignalESBManager\src\main\resources\wsm.sql导入wsm数据库
# 配置手册
    ![PKISignalManager配置.png](https://github.com/JiPingWangPKI/ESB/raw/master/resource/PKISignalManager配置.png)


