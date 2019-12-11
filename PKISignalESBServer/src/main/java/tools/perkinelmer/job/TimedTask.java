package tools.perkinelmer.job;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSONObject;
import tools.perkinelmer.mapper.CommMapper;


/**
 * 定时任务
 * @author wangjiping
 *
 */
@Component
public class TimedTask {
	private final Logger log = LoggerFactory.getLogger(TimedTask.class);
	@Autowired
	private CommMapper commMapper;
	@Value("${workflowBaseUrl_sb7}")
	private String workflowBaseUrl_sb7;
	@Value("${workflowBaseUrl_sb10}")
	private String workflowBaseUrl_sb10;
	/**
	 * 定时将未保存的日志文件重新写入数据库成功后清空文件内容
	 */
	@Scheduled(cron="0 0 2 * * ?")
	@Transactional(rollbackFor = Exception.class)
	public void doFileToDB(){
		String unSaveLogPath_sb7 = workflowBaseUrl_sb7;
		doFileToDBByPath(unSaveLogPath_sb7);
		//sb10的模式会每个节点都会存储一份自己那份unSaveLog，所以需要遍历文件夹找所有的
		File file = new File(workflowBaseUrl_sb10);
		if(file.isDirectory()) {//遍历这个文件夹下的所有节点文件夹
			String[] files =file.list(new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String name) {
					if(dir.isDirectory()) {
						return true;	
					}
					return false;
				}
			});
			for (String filePath : files) {
				String unSaveLogPath_s10 = filePath +"\\application\\engines\\default-engine-for-com.perkinelmer.PKISignals\\engine-data-area";
				File file1 = new File(unSaveLogPath_s10);
				if(file1.exists()) {
					doFileToDBByPath(unSaveLogPath_s10);
				}
			}
		}
	}
	public void doFileToDBByPath(String unSaveLogPath){
		Integer sum = 0;
		try {
			File file = new File(unSaveLogPath+"\\unSaveLog.xml");
			if(file.exists()) {
				throw new Exception(unSaveLogPath+"\\unSaveLog.xml "+"文件不存在，请重新配置相关配置");
			}
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String temp = "";// 用于临时保存每次读取的内容
			Integer succSum = 0;
			while((temp=br.readLine())!=null){
				if(temp.contains("<originalTuple>")){
					String tuple = temp.replace("<originalTuple>", "").replace("</originalTuple>", "").trim();
					if("null".equals(tuple)){
						continue;
					}
					JSONObject tupleJO = JSONObject.parseObject(tuple);
		 			String RequestId = tupleJO.getString("RequestId");
		 			String ServiceName = tupleJO.getString("ServiceName");
		 			String RequestIPAddress = tupleJO.getString("RequestIPAddress");
		 			String SoapAction = tupleJO.getString("SoapAction");
		 			String StartDt = tupleJO.getString("StartDt").replace("+0800", "");
		 			String EndDt = tupleJO.getString("EndDt").replace("+0800", "");
		 			String WSInterval = tupleJO.getString("WSInterval");
		 			String MessageInfo = tupleJO.getString("MessageInfo");
		 			String MessageResponse = tupleJO.getString("ResponseData");
		 			String RemoteServer = tupleJO.getString("RemoteServer");
		 			String StreamBaseServer = tupleJO.getString("StreamBaseServer");
		 			String Status = tupleJO.getString("Status");
		 			String MessageType = tupleJO.getString("MessageType");
		 			String SubScription_Id = tupleJO.getString("SubScriptionId");
		 			String WorkFlowName = tupleJO.getString("WorkFlowName");
		 			//将这些内容写入数据库
		 			succSum += commMapper.commInsertSqlForCallInfo(RequestId,ServiceName,RequestIPAddress,SoapAction,StartDt,EndDt,WSInterval,MessageInfo,MessageResponse,RemoteServer,StreamBaseServer,Status,MessageType,SubScription_Id,WorkFlowName);
		 			sum++;
				}
			}
			log.info("发现未更新记录"+sum+"条，其中"+succSum+"条记录成功更新");
			//读完文件清空文件内容
			FileOutputStream fos = new FileOutputStream(unSaveLogPath+"\\unSaveLog.xml");
			fos.write(new String("").getBytes());
			fos.close();
			br.close();
		} catch (Exception e) {
			log.error(sum.toString());
			log.error(e.toString());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//如果updata2()抛了异常,updata()会回滚,不影响事物正常执行
		}
	}
}
