[toc]
#一：streambase项目理解
[参考官网](https://docs.tibco.com/emp/sb-cep/10.3.0/doc/html/index.html)

流处理软件，通过组件结合处理流数据
#二：streambase service running as window service
## sb7.6.7、sb7.7.7
	1）给sbapp封装个deploy
	2）之后给机器配置streambase环境变量具体可看
	C:\TIBCO\sb-cep\7.6\setenv.bat文件内容配置
	3）命令窗口输入命令
	安装 windowservice服务命令：sbd --install-service --service-name "name1,name11"
	卸载 windowservice服务命令：sbd --remove-service "name1"
	4）将服务配置到注册表中，让安装的window service生效
	在HKEY_LOCAL_MACHINE\SOFTWARE\Wow6432Node\TIBCO Software Inc.\Settings\sb-cep\7.6中为安装的name1配置相关参数具体参考
	-b -f C:\Users\Administrator\Documents\"StreamBase Studio 7.6 Workspace"\PKISignals\sbd.sbconf C:\Users\Administrator\Documents\"StreamBase Studio 7.6 Workspace"\PKISignals\8886_LISToHIS.sbdeploy --working-directory C:\Users\Administrator\Documents\"StreamBase Studio 7.6 Workspace"\PKISignals -p 10006
	5）最后启动服务即可
## sb10.3.0、sb10.4.4
	参考：帮助文档—》TIBCO StreamBase Documentation—》Installation Guide—》Configuring Windows as a Service；
	 
	1） 先确保Streambase EventFlow Fragment类型项目没有报错
	2） 打包项目文件clean install得到jar包
	3） 创建Streambase Application
	4） 再将需要变为window service的项目clean install 的jar包中想要发布的ep-eventflow-fragment worflow文件导入pom中
	5） 创建Streambase Application Archive文件
	得到….zip,….jar两个文件前者是sb文件，后者是java项目文件
	6） 之后将zip文件放在你想要的项目文件夹下，在streambase 10.3 Command Prompt命令窗口中
	通过命令安装：
	epadmin install systemservice --name="PKIapp" --nodedirectory=C:\java\streambase\test --nodename=A.PKI --application=PKISignals_app-0.0.1-SNAPSHOT-ep-application.zip
	通过命令卸载：epadmin remove systemservice --name="PKIapp"
	7） 注意点：
	a.想要将原来deploy文件（用于控制.sbapp文件参数）应用于streambase application项目需要在configruation文件夹下添加配置文件HOCON:他可以streambase项目的任何东西：服务启动端口号、项目编码、deploy文件配置可以参考帮助文档:TIBCO Streambase Documentation—》Configuration Guide—》HOCON Configuration Introducton
	如：配置deploy
	配置端口号
	b.      通过命令窗口创建的window service 实际上是在regedit注册表中有对应配置：regedit: HKEY_LOCAL_MACHINE\SOFTWARE\Wow6432Node\TIBCO Software Inc.\Settings\sb-cep\PKIapp
	注册表中字段代表意思：
	c.       启动streambase service时检查服务有无启动启动出现异常可在Event Viewer 中查看异常报错信息
	d.      发布的的window service 的node 文件中..\B.PKI\application\deploy-configs中的deploy.conf文件对sb服务参数有效果！所以可以通过修改这里的文件来修改了sb service的参数
	e.      The process for StreamBase 10 engines is DtmEngine.exe，如果想要删除node文件夹却删不了可以尝试杀掉DtmEngine.exe进程试试
	f.        关闭window service服务命令
	                                                                           i.      epadmin --servicename=A.B stop node
	                                                                         ii.      epadmin --servicename=A.B remove node
	The 'remove node' command will delete the node directory and stop the DtmEngine.exe processes.
	g.       设置环境变量编码：STREAMBASE_STUDIO_VMARGS=-Xms512M -Xmx1024M -Dstreambase.tuple-charset=UTF-8；STREAMBASE_TUPLE_CHARSET= UTF8
# 三：sb7.6.0项目升级到sb10.3.0
将项目导出为Archive File 以.ZIP为后缀，再在sb10.3.0导入此文件，之后再右键项目升级项目，最后就是调整新生成项目的报错问题了！
10之后的项目启动时间都变长了（现在项目启动时间变为1min左右了）
# 四：sb10.3.0 Debug SB application archive
这种debug方式能将断点定位到每个控件上，其中如不做特殊处理断点不会作用于java类上，需要在Debug流中重新选择Source path
指定好java文件后断点就能进入java文件中了
# 五：sb10.3.0 通过cdc控件绑定数据库抓取新增数据做liveView实时数据源
	 1）CDC控件简单介绍
        能通过与数据库绑定从而监听数据的变化，拿到变化数据在streambase里走一遍
        2）CDC控件使用方法
        参考：TIBCO StreamBase Documentation > Adapters Guide > StreamBase Embedded Adapters>Database Change Data Capture Input Adapter
        目前只支持sqlserver和oracle，通过控件库中java operate 控件找到此控件，加载此java 控件需要额外的包除了自己添加的包还需要sqlserver或者oracle驱动程序，这些都加载完就能正常使用cdc控件
        如果报错“Windows service SQLSERVERAGENT does not exist”
        首先需要确定数据库的SQLSERVERAGENT服务启动这样才能支持数据库cdc，参考：https://support.tibco.com/s/case/5002L00000nCDyhQAG/cdc-inputadapters-problem
    3）将cdc控件 作为数据源给liveView发送数据
    将cdc控件作为liveView的table部分即可代码资源
	ESB\resource\PKISignals_LiveView
	
# 六 streambase webserviceInputAaptor项目配置多线程，增加项目并发线程数
	1）bd.config文件中<param name="jvm-args" value="-Xms128m -Xmx512m"/>这里的配置项配置jvm给的内存权限，容量
	2）配置这里可达到多线程的作用
![配置图](https://note.youdao.com/yws/res/16248/WEBRESOURCE3a3f23d6a8488284ef16bac471285318)