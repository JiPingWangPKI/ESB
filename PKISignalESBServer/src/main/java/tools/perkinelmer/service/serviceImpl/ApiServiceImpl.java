package tools.perkinelmer.service.serviceImpl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import tools.perkinelmer.Enum.ResultEnum;
import tools.perkinelmer.entity.Result;
import tools.perkinelmer.service.ApiService;
import tools.perkinelmer.utls.ResultUtil;
@Service
public class ApiServiceImpl implements ApiService{
	protected final static Logger log = LoggerFactory.getLogger(ApiServiceImpl.class);
	@Value("${workflowBaseUrl_sb7}")
	private String workflowBaseUrl_sb7;
	@Value("${workflowBaseUrl_sb10}")
	private String workflowBaseUrl_sb10;
	@Override
	public Result switchService(String serviceName, String method) {
		Runtime runtime = Runtime.getRuntime();
		Process process;
		try {
			if("on".equals(method)){
				String commandline = "";
				String[] envp = {"LANG=UTF-8"};
				process = runtime.exec("cmd /c net start \""+serviceName+"\"");
				InputStream success = process.getInputStream();
				InputStream error = process.getErrorStream();
				BufferedReader readerSuccess = new BufferedReader(new InputStreamReader(success,"GBK"));//window中都是gbk编码如果换成linux就需要换下这个编码规则
				BufferedReader readerError = new BufferedReader(new InputStreamReader(error,"GBK"));
				String line;
				while ((line = readerSuccess.readLine())!= null) {
					commandline+=line;
			    }
				while ((line = readerError.readLine())!= null) {
					commandline+=line;
			    }
				readerSuccess.close();   
				readerError.close();
		        process.waitFor();   
		        process.destroy(); 
				if(commandline.contains("was started successfully")){//开启成功
					return ResultUtil.success("the "+serviceName+"was started successfully!"); 
				}else{//开启失败
					return ResultUtil.error(ResultEnum.ServiceStartFail.getCode(),commandline);
				}		
			}else if("off".equals(method)){
				String commandline = "";
				String[] envp = {"LANG=UTF-8"};
				process = runtime.exec("cmd /c net stop \""+serviceName+"\"");
				InputStream success = process.getInputStream();
				InputStream error = process.getErrorStream();
				BufferedReader readerSuccess = new BufferedReader(new InputStreamReader(success,"GBK"));  
				BufferedReader readerError = new BufferedReader(new InputStreamReader(error,"GBK"));  
				String line;
				while ((line = readerSuccess.readLine())!= null) {
					commandline+=line;
			    }
				while ((line = readerError.readLine())!= null) {
					commandline+=line;
			    }
				readerSuccess.close();   
				readerError.close();
		        process.waitFor();   
		        process.destroy(); 
				if(commandline.contains("was stopped successfully")){//关闭成功
					return ResultUtil.success("the "+serviceName+"was stopped successfully!"); 
				}else{//开启失败
					return ResultUtil.error(ResultEnum.ServiceStopFail.getCode(),commandline);
				}		
			}else{
				return ResultUtil.error(ResultEnum.ErrorMethod.getCode(), ResultEnum.ErrorMethod.getMsg());
			}
		} catch (Exception e) {
			log.error(e.toString());
			return ResultUtil.error(ResultEnum.UNKONW_ERROR.getCode(), ResultEnum.UNKONW_ERROR.getMsg());
		}
	}
	/**
	 * 
	 * @param targetIp
	 * @param platformIp
	 * @param platformIp_vip
	 * @param requestIp
	 * @param authority
	 * @param workFlowName 节点文件夹名称
	 * @return
	 */
	private Result modifyWorkFlowParams_sb10(String targetIp, String platformIp,String platformIp_vip,String requestIp, String authority, String workFlowName,String liveViewPort, String liveViewUserName, String liveViewPassWord) {
		try {
			//修改PKIWS.lvconf配置文件
			String filePath = workflowBaseUrl_sb10+"\\"+workFlowName+"\\application\\fragments\\com.perkinelmer.PKISignals\\PKIWS.lvconf";
			File file = new File(filePath);
			BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
			StringBuilder deployConfigContent = new StringBuilder();
			String s = null;
			while((s = br.readLine())!=null){//使用readLine方法，一次读一行
				deployConfigContent.append(s+System.lineSeparator());
			}
			br.close();
			String content =deployConfigContent.toString();
			content = content.replaceFirst("<parameter name=\"UrlReplaceFrom1\" value=.*/>","<parameter name=\"UrlReplaceFrom1\" value=\"&quot;"+platformIp_vip+"&quot;\"/>")
				.replaceFirst("<parameter name=\"UrlReplaceFrom\" value=.*/>","<parameter name=\"UrlReplaceFrom\" value=\"&quot;"+platformIp+"&quot;\"/>")
				.replaceFirst("<parameter name=\"UrlReplaceTo\" value=.*/>","<parameter name=\"UrlReplaceTo\" value=\"&quot;"+targetIp+"&quot;\"/>")
				.replaceFirst("<parameter name=\"RequestIPAddress\" value=.*/>","<parameter name=\"RequestIPAddress\" value=\"&quot;"+requestIp+"&quot;\"/>")
				.replaceFirst("<parameter name=\"UseAuthoritys\" value=.*/>","<parameter name=\"UseAuthoritys\" value=\""+authority+"\"/>");
			//写配置文件中去
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			//修改PKIWS.lvconf配置文件
			String filePath2 = workflowBaseUrl_sb10+"\\"+workFlowName+"\\application\\shared\\clientApiListener.conf";
			File file2 = new File(filePath2);
			BufferedReader br2 = new BufferedReader(new FileReader(file2));//构造一个BufferedReader类来读取文件
			StringBuilder deployConfigContent2 = new StringBuilder();
			String s2 = null;
			while((s2 = br2.readLine())!=null){//使用readLine方法，一次读一行
				deployConfigContent2.append(s2+System.lineSeparator());
			}
			br2.close();
			String content2 =deployConfigContent2.toString();
			content2 = content2.replaceFirst("portNumber\\s*=\\s*[0-9]*","portNumber = "+liveViewPort);
			//写配置文件中去
			FileWriter fw2 = new FileWriter(file2);
			BufferedWriter bw2 = new BufferedWriter(fw2);
			bw2.write(content2);
			bw2.close();
			
			//修改PKIWS.lvconf配置文件
			String filePath3 = workflowBaseUrl_sb10+"\\"+workFlowName+"\\application\\shared\\authRealm.conf";
			File file3 = new File(filePath3);
			BufferedReader br3 = new BufferedReader(new FileReader(file3));//构造一个BufferedReader类来读取文件
			StringBuilder deployConfigContent3 = new StringBuilder();
			String s3 = null;
			while((s3 = br3.readLine())!=null){//使用readLine方法，一次读一行
				deployConfigContent3.append(s3+System.lineSeparator());
			}
			br3.close();
			String content3 =deployConfigContent3.toString();
			content3 = content3.replaceFirst("userName\\s*=\\s*\".*\"","userName = \""+liveViewUserName+"\"")
					.replaceFirst("password\\s*=\\s*\".*\"","password =\""+liveViewPassWord+"\"");
			//写配置文件中去
			FileWriter fw3 = new FileWriter(file3);
			BufferedWriter bw3 = new BufferedWriter(fw3);
			bw3.write(content3);
			bw3.close();
	        return ResultUtil.success();
		} catch (Exception e) {
			log.error(e.toString());
			return ResultUtil.error(ResultEnum.UNKONW_ERROR.getCode(), ResultEnum.UNKONW_ERROR.getMsg());
		}
	}

	private Result getWorkflowData_sb10() {
		JSONArray result = new JSONArray();
		try {
			//获得workflow文件夹下所有的节点文件夹
			File file = new File(workflowBaseUrl_sb10);
			if(!file.exists()) {
				return ResultUtil.success(result);
			}
			String[] fileNames = file.list(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					if(dir.isDirectory()) {
						return true;
					}
					return false;
				}
			});
			for (String fileName : fileNames) {
				String workflowDeployUrl = workflowBaseUrl_sb10+"\\"+fileName+"\\application\\fragments\\com.perkinelmer.PKISignals\\PKIWS.lvconf";
				File file1 = new File(workflowDeployUrl);
				if(file1.exists()) {
					JSONObject jo = new JSONObject();
					//拿liveView相关参数LiveViewPort,LiveViewUserName,LiveViewPassWord
					String clientApiListenerPath = workflowBaseUrl_sb10+"\\"+fileName+"\\application\\shared\\clientApiListener.conf";
					String authRealmPath = workflowBaseUrl_sb10+"\\"+fileName+"\\application\\shared\\authRealm.conf";
					File clientApiListenerFile = new File(clientApiListenerPath);
					File authRealmFile = new File(authRealmPath);
					StringBuilder clientApiListenerContent = new StringBuilder();
					StringBuilder authRealmContent = new StringBuilder();
					BufferedReader clientApiListenerBr = new BufferedReader(new FileReader(clientApiListenerFile));
					BufferedReader authRealmBr = new BufferedReader(new FileReader(authRealmFile));
					String clientApiListenerS = null;
					String authRealmS = null;
					while((clientApiListenerS = clientApiListenerBr.readLine())!=null){//使用readLine方法，一次读一行
						clientApiListenerContent.append(System.lineSeparator()+clientApiListenerS);
					}
					clientApiListenerBr.close();
					String clientApiListenercontent =clientApiListenerContent.toString();
					String clientApiListenerdeployRegex ="portNumber\\s*=\\s*([0-9]*)";
					Pattern clientApiListenerdeployPattern = Pattern.compile(clientApiListenerdeployRegex);
					Matcher clientApiListenerdeployMatcher = clientApiListenerdeployPattern.matcher(clientApiListenercontent);
					clientApiListenerdeployMatcher.find();
					String liveViewPort = clientApiListenerdeployMatcher.group(1);
					while((authRealmS = authRealmBr.readLine())!=null){//使用readLine方法，一次读一行
						authRealmContent.append(System.lineSeparator()+authRealmS);
					}
					authRealmBr.close();
					String authRealmcontent =authRealmContent.toString();
					String authRealmRegex ="userName\\s*=\\s*\"(.*)\"\\s*password\\s*=\\s*\"(.*)\"";
					Pattern authRealmPattern = Pattern.compile(authRealmRegex);
					Matcher authRealmMatcher = authRealmPattern.matcher(authRealmcontent);
					authRealmMatcher.find();
					String liveViewUserName = authRealmMatcher.group(1);
					String liveViewPassWord = authRealmMatcher.group(2);
					//获得配置文件中特定数据值
					StringBuilder deployConfigContent = new StringBuilder(); 
					BufferedReader br = new BufferedReader(new FileReader(file1));//构造一个BufferedReader类来读取文件
					String s = null;
					while((s = br.readLine())!=null){//使用readLine方法，一次读一行
						deployConfigContent.append(System.lineSeparator()+s);
					}
					br.close();
					String content =deployConfigContent.toString();
					String deployRegex ="\\s*<parameter name=\"UrlReplaceFrom\" value=\"(.*)\"/>\\s*<parameter name=\"UrlReplaceFrom1\" value=\"(.*)\"/>\\s*<parameter name=\"UrlReplaceTo\" value=\"(.*)\"/>\\s*<parameter name=\"RequestIPAddress\" value=\"(.*)\"/>\\s*<parameter name=\"UseAuthoritys\" value=\"(.*)\"/>\\s*<parameter name=\"WorkFlowName\" value=\"(.*)\"/>";
					Pattern deployPattern = Pattern.compile(deployRegex);
					Matcher deployMatcher = deployPattern.matcher(content);
					while (deployMatcher.find()) {  
						jo.put("urlReplaceFrom1", deployMatcher.group(1).replace("&quot;", ""));
						jo.put("urlReplaceFrom", deployMatcher.group(2).replace("&quot;", ""));
						jo.put("urlReplaceTo", deployMatcher.group(3).replace("&quot;", ""));
						jo.put("requestIPAddress", deployMatcher.group(4).replace("&quot;", ""));
						jo.put("authoritys", deployMatcher.group(5));
						//拿liveView相关参数
						jo.put("liveViewPort", liveViewPort);
						jo.put("liveViewUserName", liveViewUserName);
						jo.put("liveViewPassWord", liveViewPassWord);
						result.add(jo);
					}
					//查询对应的服务有没有开启,特意指定了window servicename和节点文件夹之间的关系
					String windowServiceName = fileName.replace("PKISignals.", "");
					jo.put("serviceName", windowServiceName);
					jo.put("workFlowName", fileName);
					boolean serviceState = windowServiceState(windowServiceName);
					jo.put("serviceState", serviceState);
				}
			}
			return ResultUtil.success(result);
		} catch (Exception e) {
			log.error(e.toString());
			return ResultUtil.error(ResultEnum.UNKONW_ERROR.getCode(), ResultEnum.UNKONW_ERROR.getMsg());
		}
	}
	public Result modifyWorkFlowParams_sb7(String targetIp, String platformIp,String platformIp_vip,String requestIp, String authority, String workFlowName) {
		try {
			//修改指定xml文件
			Document doc=new SAXReader().read(new File(workflowBaseUrl_sb7+"\\"+workFlowName+".sbdeploy"));
			Element UrlReplaceFrom=(Element)doc.selectSingleNode("//module-parameters/param[@name='UrlReplaceFrom']");
			UrlReplaceFrom.setAttributeValue("value", "'"+platformIp+"'");
			Element UrlReplaceFrom1=(Element)doc.selectSingleNode("//module-parameters/param[@name='UrlReplaceFrom1']");
			UrlReplaceFrom1.setAttributeValue("value", "'"+platformIp_vip+"'");
			Element UrlReplaceTo=(Element)doc.selectSingleNode("//module-parameters/param[@name='UrlReplaceTo']");
			UrlReplaceTo.setAttributeValue("value", "'"+targetIp+"'");
			Element RequestIPAddress=(Element)doc.selectSingleNode("//module-parameters/param[@name='RequestIPAddress']");
			RequestIPAddress.setAttributeValue("value", "'"+requestIp+"'");
			Element UseAuthoritys=(Element)doc.selectSingleNode("//module-parameters/param[@name='UseAuthoritys']");
			UseAuthoritys.setAttributeValue("value", authority);
			//写回去
	        FileOutputStream fos=new FileOutputStream(workflowBaseUrl_sb7+"\\"+workFlowName+".sbdeploy");
			@SuppressWarnings("static-access")
			OutputFormat format=new OutputFormat().createPrettyPrint();
	        format.setEncoding("utf-8");
	        XMLWriter writer=new XMLWriter(fos,format);
	        writer.write(doc);
	        writer.close();
	        return ResultUtil.success();
		} catch (Exception e) {
			log.error(e.toString());
			return ResultUtil.error(ResultEnum.UNKONW_ERROR.getCode(), ResultEnum.UNKONW_ERROR.getMsg());
		}
	}

	public Result getWorkflowData_sb7() {
		JSONArray result = new JSONArray();
		try {
			//获得workflow文件夹下所有的workflow
			File file = new File(workflowBaseUrl_sb7);
			if(!file.exists()) {
				return ResultUtil.success(result);
			}
			String[] fileNames = file.list();
			for(String fileName:fileNames){
				if(fileName.contains(".sbdeploy")){//workflow配置文件
					JSONObject jo = new JSONObject();
					String workflowUrl = workflowBaseUrl_sb7+"\\"+fileName;
					Document doc=new SAXReader().read(new File(workflowUrl));
					Element UrlReplaceFrom=(Element)doc.selectSingleNode("//module-parameters/param[@name='UrlReplaceFrom']");
					String UrlReplaceFromValue =UrlReplaceFrom.attribute("value").getStringValue().replace("'", "");
					jo.put("urlReplaceFrom", UrlReplaceFromValue);
					Element UrlReplaceFrom1=(Element)doc.selectSingleNode("//module-parameters/param[@name='UrlReplaceFrom1']");
					String UrlReplaceFrom1Value =UrlReplaceFrom1.attribute("value").getStringValue().replace("'", "");
					jo.put("urlReplaceFrom1", UrlReplaceFrom1Value);
					Element UrlReplaceTo=(Element)doc.selectSingleNode("//module-parameters/param[@name='UrlReplaceTo']");
					String UrlReplaceToValue =UrlReplaceTo.attribute("value").getStringValue().replace("'", "");
					jo.put("urlReplaceTo", UrlReplaceToValue);
					Element RequestIPAddress=(Element)doc.selectSingleNode("//module-parameters/param[@name='RequestIPAddress']");
					String RequestIPAddressValue =RequestIPAddress.attribute("value").getStringValue().replace("'", "");
					jo.put("requestIPAddress", RequestIPAddressValue);
					Element UseAuthoritys=(Element)doc.selectSingleNode("//module-parameters/param[@name='UseAuthoritys']");
					String UseAuthoritysValue =UseAuthoritys.attribute("value").getStringValue().replace("'", "");
					jo.put("authoritys", UseAuthoritysValue);
					//查询对应的服务有没有开启
					String windowServiceName = "SB"+fileName.replace("_", "").replace("To", "").replace(".sbdeploy", "");
					jo.put("serviceName", windowServiceName);
					jo.put("workFlowName", fileName.replace(".sbdeploy", ""));
					boolean serviceState = windowServiceState(windowServiceName);
					jo.put("serviceState", serviceState);
					//旧版本暂时不给提供LiveView功能
					jo.put("liveViewPort", "00000");
					jo.put("liveViewUserName", "");
					jo.put("liveViewPassWord", "");
					result.add(jo);
				}
			}
	        return ResultUtil.success(result);
		} catch (Exception e) {
			log.error(e.toString());
			return ResultUtil.error(ResultEnum.UNKONW_ERROR.getCode(), ResultEnum.UNKONW_ERROR.getMsg());
		}
	}
	/**
	 * 检查window service状态
	 * @param serviceName
	 * @return
	 * @throws Exception
	 */
	public boolean windowServiceState(String serviceName) throws Exception{
		Runtime runtime = Runtime.getRuntime();
		Process process;
		String commandline = "";
		process = runtime.exec("cmd /c net start");
		InputStream success = process.getInputStream();
		InputStream error = process.getErrorStream();
		BufferedReader readerSuccess = new BufferedReader(new InputStreamReader(success));  
		BufferedReader readerError = new BufferedReader(new InputStreamReader(error));  
		String line;
		while ((line = readerSuccess.readLine())!= null) {
			commandline+=line;
	    }
		while ((line = readerError.readLine())!= null) {
			commandline+=line;
	    }
		readerSuccess.close();   
		readerError.close();
        process.waitFor();   
        process.destroy();
        if(commandline.contains(serviceName)){
        	return true;
        }else{
        	return false;
        }
	}

	@Override
	public Result modifyWorkFlowParams(String serviceName,String targetIp, String platformIp, String platformIp_vip, String requestIp,
			String authority, String workFlowName,String liveViewPort, String liveViewUserName, String liveViewPassWord) {
		Result result = new Result();
		if(serviceName.contains("SB10")){
			result = modifyWorkFlowParams_sb10(targetIp, platformIp, platformIp_vip, requestIp,authority, workFlowName,liveViewPort, liveViewUserName, liveViewPassWord);	
		}else{
			result = modifyWorkFlowParams_sb7(targetIp, platformIp, platformIp_vip, requestIp,authority, workFlowName);//旧版本暂时不提供LiveView功能
		}
		return result;
	}

	@Override
	public Result getWorkflowData() {
		Result result1 = getWorkflowData_sb7();
		Result result2 = getWorkflowData_sb10();
		//合拼两个result
		if(result1.getCode()==0&&result2.getCode()==0){//都正确返回值
			JSONArray result = new JSONArray();
			result.addAll((JSONArray)result1.getData());
			result.addAll((JSONArray)result2.getData());
			return ResultUtil.success(result);
		}else{
			return result1;
		}
	}
}
