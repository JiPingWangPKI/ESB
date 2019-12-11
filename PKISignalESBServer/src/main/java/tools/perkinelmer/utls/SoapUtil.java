package tools.perkinelmer.utls;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * soap工具
 * @author wangj01052
 *
 */
public class SoapUtil {
	public static String invokeSrvGet(String endpoint) throws Exception{
		String result="false";
		//创建服务地址
		URL url = new URL(endpoint); 
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET"); 
		connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
		//3.3设置输入输出，因为默认新创建的connection没有读写权限，  
        connection.setDoInput(true);  
        connection.setDoOutput(true);  
        //第五步：接收服务端响应，打印  
        Date date = new Date();
        int responseCode = connection.getResponseCode();
        long interval = new Date().getTime()-date.getTime();
        if(200 == responseCode){//表示服务端响应成功  
        	
            /*InputStream is = connection.getInputStream();  
            InputStreamReader isr = new InputStreamReader(is);  
            BufferedReader br = new BufferedReader(isr);  
            StringBuilder sb = new StringBuilder();  
            String temp = null;  
            while(null != (temp = br.readLine())){
                sb.append(temp);  
            }  
            result = sb.toString();
            is.close();  
            isr.close();  
            br.close();  */
        	result=Long.toString(interval);
        }  
        return result;
	}
	public static String invokeSrvPost(String endpoint,String paramData) throws Exception{
		String result="false";
		//创建服务地址
		URL url = new URL(endpoint); 
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST"); 
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		//3.3设置输入输出，因为默认新创建的connection没有读写权限，  
        connection.setDoInput(true);  
        connection.setDoOutput(true);  
        OutputStream os = connection.getOutputStream();
        os.write(paramData.getBytes(StandardCharsets.UTF_8));
        //第五步：接收服务端响应，打印  
        Date start = new Date();
        int responseCode = connection.getResponseCode();
        long interval = new Date().getTime()-start.getTime();
        if(200 == responseCode){//表示服务端响应成功  
            /*InputStream is = connection.getInputStream();  
            InputStreamReader isr = new InputStreamReader(is);  
            BufferedReader br = new BufferedReader(isr);  
            StringBuilder sb = new StringBuilder();  
            String temp = null;  
            while(null != (temp = br.readLine())){
                sb.append(temp);  
            }  
            result = sb.toString();
            is.close();  
            isr.close();  
            br.close();*/  
        	result = Long.toString(interval);
        }  
        return result;
	}
	
	/**
	 * 
	 * @param endpoint
	 * @param action 有值soap1.1;无值soap1.2协议
	 * @param soapXml soap1.1;soap1.2 文本不一样注意改变
	 * @return
	 * @throws IOException
	 */
	public static String invokeSrvSoap(String endpoint,String action, String soapXml) throws IOException{  
		String result ="false";
		//第一步：创建服务地址，不是WSDL地址  
		        URL url = new URL(endpoint);  
		        //第二步：打开一个通向服务地址的连接  
		        HttpURLConnection connection = (HttpURLConnection) url.openConnection();  
		        //第三步：设置参数  
		        //3.1发送方式设置：POST必须大写  
		        connection.setRequestMethod("POST");  
		        if(action==null||"".equals(action)){//soap1.2协议
		        	//3.2设置数据格式：content-type  
			        connection.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8"); 
		        }else{//soap1.1协议
		        	//3.2设置数据格式：content-type  
			        connection.setRequestProperty("Content-Type", "text/xml;charset=utf-8"); 
			        connection.setRequestProperty("SOAPAction", action);
		        }
		        
		        //3.3设置输入输出，因为默认新创建的connection没有读写权限，  
		        connection.setDoInput(true);  
		        connection.setDoOutput(true);  
		  
		        //第四步：组织SOAP数据，发送请求  
		        OutputStream os = connection.getOutputStream();  
		        os.write(soapXml.getBytes(StandardCharsets.UTF_8));  
		        //第五步：接收服务端响应，打印  
		        Date start = new Date();
		        int responseCode = connection.getResponseCode();  
		        long interval = new Date().getTime()-start.getTime();
		        if(200 == responseCode){//表示服务端响应成功  
		            /*InputStream is = connection.getInputStream();  
		            InputStreamReader isr = new InputStreamReader(is);  
		            BufferedReader br = new BufferedReader(isr);  
		            StringBuilder sb = new StringBuilder();  
		            String temp = null;  
		            while(null != (temp = br.readLine())){
		                sb.append(temp);  
		            }  
		            result = sb.toString();
		            is.close();
		            isr.close();  
		            br.close();*/  
		        	result=Long.toString(interval);
		        }  
		  
		        os.close(); 
		
		return result;
    }  
	
	//工具调用方式类似，这里不能直接用
	public static void main(String[] args) throws Exception {  
		//soap1.1协议
		String soapXML1  = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body><getMobileCodeInfo xmlns=\"http://WebXml.com.cn/\">ajhdfklhalsdf!@#$%<mobileasCode>17625922615</mobileCode><userID></userID></getMobileCodeInfo></soap:Body></soap:Envelope>";
		String endPoint1 = "http://ws.webxml.com.cn/WebServices/MobileCodeWS.asmx";
		String soapAction1 ="http://WebXml.com.cn/getMobileCodeInfo";
		System.out.println(invokeSrvSoap(endPoint1, soapAction1, soapXML1));
		
		//soap1.2协议
		//String aa = "<user>testsystem</user><password>qwer1234</password><methodCode>0001</methodCode><methodName>tesservice</methodName>";
		//String soapXML2 = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\"><soap12:Body><getCountryCityByIp xmlns=\"http://WebXml.com.cn/\"><theIpAddress>127.0.0.1</theIpAddress></getCountryCityByIp></soap12:Body></soap12:Envelope>";
		//String endPoint2 = "http://127.0.0.1:8181/WebServices/IpAddressSearchWebService.asmx";
		//System.out.println("soap1.2:"+invokeSrvSoap(endPoint2, null, soapXML2));
		//String soapXML3 = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\"><soap12:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>testsys11</user><password>1123</password><methodCode>0001</methodCode><methodName>tes1tservice</methodName><parameter>string</parameter></ESP_Input></soap12:Body></soap12:Envelope>";
		
		//get方式 172.26.10.55:8181/WebSiteSouthRJ/CSWebService.asmx?wsdl 172.26.11.89
		//String endPoint4 = "http://172.26.10.55:8181/WebSiteSouthRJ/CSWebService.asmx?wsdl";
		//System.out.println("get:"+invokeSrvGet(endPoint4));
		
		//post方式
		//String endPoint5 = "http://127.0.0.1:8181/WebServices/MobileCodeWS.asmx/getMobileCodeInfo";
		//String paramData5 = "mobileCode=17625922615&userID=";
		//System.out.println("post:"+invokeSrvPost(endPoint5, paramData5));
		
		
		
		
		
		
		//String soapXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>RuiKe</user><password>eea79c4e7f1c453d866bcb0bbd22138e</password><methodCode>EMR-ET-S10</methodCode><methodName>SynchRisReport</methodName><parameter>&lt;CallInfo&gt;&lt;Reportcomment&gt;&lt;ETONo&gt;9690854&lt;/ETONo&gt;&lt;ETODetailNo&gt;10898286&lt;/ETODetailNo&gt;&lt;ReportNo&gt;a87fdc32-204e-4daf-8f47-281f75b8d199&lt;/ReportNo&gt;&lt;ExamDateTime&gt;&lt;/ExamDateTime&gt;&lt;ReportDateTime&gt;2018-04-24 18:40:48&lt;/ReportDateTime&gt;&lt;ExamPart&gt;胸部正位&lt;/ExamPart&gt;&lt;DeptCode&gt;33620262&lt;/DeptCode&gt;&lt;DeptName&gt;33620262&lt;/DeptName&gt;&lt;DoctorId&gt;1034&lt;/DoctorId&gt;&lt;DoctorName&gt;1034&lt;/DoctorName&gt;&lt;CriticalFlag&gt;0&lt;/CriticalFlag&gt;&lt;CriticalContent&gt;&lt;/CriticalContent&gt;&lt;ExamDescript&gt;bjkjo'khg'dosjigh'isdfgk;ldsm,g:DS&gt;,gL:SD&lt;gl;sdgsgdsgsdgds&lt;/ExamDescript&gt;&lt;DiagDescript&gt;&lt;/DiagDescript&gt;&lt;ExamVerdict&gt;!@#$%^&amp;amp;*()YGBHJNTFUVYGBHNOJIiughvdfhokjghskxdfjhkl;hkhblkjhljk&lt;/ExamVerdict&gt;&lt;ExamDiag&gt;&lt;/ExamDiag&gt;&lt;Reporter&gt;zww&lt;/Reporter&gt;&lt;ReporterName&gt;zww&lt;/ReporterName&gt;&lt;DateTime&gt;&lt;/DateTime&gt;&lt;ReportTypeFlag&gt;1&lt;/ReportTypeFlag&gt;&lt;Noter&gt;&lt;/Noter&gt;&lt;ReportURL&gt;&lt;/ReportURL&gt;&lt;ImageURL&gt;&lt;![CDATA[http://172.26.12.37/masterview/mv.jsp?server_name=pacsrjnFIR&amp;user_name=clinic&amp;close_on_exit=true&amp;key_images=false&amp;password=clinic&amp;accession_number=9009690854]]&gt;&lt;/ImageURL&gt;&lt;/Reportcomment&gt;&lt;/CallInfo&gt;</parameter></ESP_Input></soap:Body></soap:Envelope>";
		//String endPoint = "http://172.26.82.208:2210/MedicalServiceESB.asmx";
		//String soapAction = "http://tempuri.org/ESP_Input";
		//System.out.println(invokeSrvSoap(endPoint,soapAction, soapXML));
		
		
		
		
		//内网环境：172.26.11.89;172.26.82.208:2210 ;172.26.10.55:8282
		//soap1.1
		//String soapXML2 = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>RuiKe</user><password>eea79c4e7f1c453d866bcb0bbd22138e</password><methodCode>EMR-ET-Q02</methodCode><methodName>QueryTechOrder</methodName><parameter>&lt;CallInfo&gt;&lt;HisPatId&gt;9690889&lt;/HisPatId&gt;&lt;HisVisitNo&gt;-1&lt;/HisVisitNo&gt;&lt;DeptCode&gt;&lt;/DeptCode&gt;&lt;SysFlag&gt;1&lt;/SysFlag&gt;&lt;/CallInfo&gt;</parameter></ESP_Input></soap:Body></soap:Envelope>";
		//String soapXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>RuiKe</user><password>eea79c4e7f1c453d866bcb0bbd22138e</password><methodCode>EMR-ET-Q01</methodCode><methodName>QueryPatientInfo</methodName><parameter>&lt;CallInfo&gt;&lt;CardNo&gt;987686&lt;/CardNo&gt;&lt;CardTypeFlag&gt;99&lt;/CardTypeFlag&gt;&lt;HospitalId&gt;56650590500&lt;/HospitalId&gt;&lt;HisId&gt;Beta1_TrustHISV1.18&lt;/HisId&gt;&lt;SysFlag&gt;1&lt;/SysFlag&gt;&lt;/CallInfo&gt;</parameter></ESP_Input></soap:Body></soap:Envelope>";
		//String endPoint = "http://172.26.10.55:8282/MedicalServiceESB.asmx";
		//String soapAction = "http://tempuri.org/ESP_Input";
		//System.out.println(invokeSrvSoap(endPoint,soapAction, soapXML2));
		//soap1.2
		//String soapXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\"><soap12:Body><SerializeObject xmlns=\"http://tempuri.org/\" /></soap12:Body></soap12:Envelope>";
		//String endPoint = "http://172.26.11.89/WebSiteSouthRJ/CSWebService.asmx";
		//System.out.println(invokeSrvSoap(endPoint,null, soapXML));
		//get
		//String endPoint = "http://172.26.10.55:8181/WebSiteSouthRJ/CSWebService.asmx?WSDL";
		//System.out.println(invokeSrvGet(endPoint));
		
		
		
		//模拟HIS<->RIS之间信息交互
		
		/*//1	172.26.11.89	http://172.26.10.55:8181/WebSiteSouthRJ/CSWebService.asmx		WSDL,
		String endPoint1 = "http://172.26.10.55:8181/WebSiteSouthRJ/CSWebService.asmx?WSDL";
		System.out.println("1:"+invokeSrvGet(endPoint1));*/
		
		//2	172.26.82.208:2210	http://172.26.10.55:8282/MedicalServiceESB.asmx	\"http://tempuri.org/ESP_Input\"	<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>RuiKe</user><password>eea79c4e7f1c453d866bcb0bbd22138e</password><methodCode>EMR-ET-Q01</methodCode><methodName>QueryPatientInfo</methodName><parameter>&lt;CallInfo&gt;&lt;CardNo&gt;987686&lt;/CardNo&gt;&lt;CardTypeFlag&gt;99&lt;/CardTypeFlag&gt;&lt;HospitalId&gt;56650590500&lt;/HospitalId&gt;&lt;HisId&gt;Beta1_TrustHISV1.18&lt;/HisId&gt;&lt;SysFlag&gt;1&lt;/SysFlag&gt;&lt;/CallInfo&gt;</parameter></ESP_Input></soap:Body></soap:Envelope>
		//String soapXML2 = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>RuiKe12</user><password>eea79c4e7f1c453d866bcb0bbd22138e</password><methodCode>EMR-ET-Q01</methodCode><methodName>QueryPatientInfo</methodName><parameter>&lt;CallInfo&gt;&lt;CardNo&gt;987686&lt;/CardNo&gt;&lt;CardTypeFlag&gt;99&lt;/CardTypeFlag&gt;&lt;HospitalId&gt;56650590500&lt;/HospitalId&gt;&lt;HisId&gt;Beta1_TrustHISV1.18&lt;/HisId&gt;&lt;SysFlag&gt;1&lt;/SysFlag&gt;&lt;/CallInfo&gt;</parameter></ESP_Input></soap:Body></soap:Envelope>";
		//String endPoint2 = "http://172.26.10.55:8282/MedicalServiceESB.asmx";
		//String soapAction2 = "http://tempuri.org/ESP_Input";
		//System.out.println("2"+invokeSrvSoap(endPoint2,soapAction2, soapXML2));

		/*//3	172.26.82.208:2210	http://172.26.10.55:8282/MedicalServiceESB.asmx	\"http://tempuri.org/ESP_Input\"	<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>RuiKe</user><password>eea79c4e7f1c453d866bcb0bbd22138e</password><methodCode>EMR-ET-Q02</methodCode><methodName>QueryTechOrder</methodName><parameter>&lt;CallInfo&gt;&lt;HisPatId&gt;9690889&lt;/HisPatId&gt;&lt;HisVisitNo&gt;-1&lt;/HisVisitNo&gt;&lt;DeptCode&gt;&lt;/DeptCode&gt;&lt;SysFlag&gt;1&lt;/SysFlag&gt;&lt;/CallInfo&gt;</parameter></ESP_Input></soap:Body></soap:Envelope>
		String soapXML3 = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>RuiKe</user><password>eea79c4e7f1c453d866bcb0bbd22138e</password><methodCode>EMR-ET-Q02</methodCode><methodName>QueryTechOrder</methodName><parameter>&lt;CallInfo&gt;&lt;HisPatId&gt;9690889&lt;/HisPatId&gt;&lt;HisVisitNo&gt;-1&lt;/HisVisitNo&gt;&lt;DeptCode&gt;&lt;/DeptCode&gt;&lt;SysFlag&gt;1&lt;/SysFlag&gt;&lt;/CallInfo&gt;</parameter></ESP_Input></soap:Body></soap:Envelope>";
		String endPoint3 = "http://172.26.10.55:8282/MedicalServiceESB.asmx";
		String soapAction3 = "http://tempuri.org/ESP_Input";
		System.out.println("3"+invokeSrvSoap(endPoint3,soapAction3, soapXML3));
		
		//4	172.26.82.208:2210	http://172.26.10.55:8282/MedicalServiceESB.asmx	\"http://tempuri.org/ESP_Input\"	<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>RuiKe</user><password>eea79c4e7f1c453d866bcb0bbd22138e</password><methodCode>EMR-ET-Q03</methodCode><methodName>QueryTechOrderDetail</methodName><parameter>&lt;CallInfo&gt;&lt;ETONo&gt;9690889&lt;/ETONo&gt;&lt;SysFlag&gt;1&lt;/SysFlag&gt;&lt;/CallInfo&gt;</parameter></ESP_Input></soap:Body></soap:Envelope>
		String soapXML4 = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>RuiKe</user><password>eea79c4e7f1c453d866bcb0bbd22138e</password><methodCode>EMR-ET-Q03</methodCode><methodName>QueryTechOrderDetail</methodName><parameter>&lt;CallInfo&gt;&lt;ETONo&gt;9690889&lt;/ETONo&gt;&lt;SysFlag&gt;1&lt;/SysFlag&gt;&lt;/CallInfo&gt;</parameter></ESP_Input></soap:Body></soap:Envelope>";
		String endPoint4 = "http://172.26.10.55:8282/MedicalServiceESB.asmx";
		String soapAction4 = "http://tempuri.org/ESP_Input";
		System.out.println("4"+invokeSrvSoap(endPoint4,soapAction4, soapXML4));
		
		//5	172.26.82.208:2210	http://172.26.10.55:8282/MedicalServiceESB.asmx	\"http://tempuri.org/ESP_Input\"	<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>RuiKe</user><password>eea79c4e7f1c453d866bcb0bbd22138e</password><methodCode>EMR-ET-S02</methodCode><methodName>SynchReceiveInfo</methodName><parameter>&lt;CallInfo&gt;&lt;ETONo&gt;9690889&lt;/ETONo&gt;&lt;ETODetailNo&gt;10898321&lt;/ETODetailNo&gt;&lt;PerformedBy&gt;33620562&lt;/PerformedBy&gt;&lt;Technician&gt;RIS&lt;/Technician&gt;&lt;StatusFlag&gt;1&lt;/StatusFlag&gt;&lt;SysFlag&gt;1&lt;/SysFlag&gt;&lt;/CallInfo&gt; </parameter></ESP_Input></soap:Body></soap:Envelope>
		String soapXML5 = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>RuiKe</user><password>eea79c4e7f1c453d866bcb0bbd22138e</password><methodCode>EMR-ET-S02</methodCode><methodName>SynchReceiveInfo</methodName><parameter>&lt;CallInfo&gt;&lt;ETONo&gt;9690889&lt;/ETONo&gt;&lt;ETODetailNo&gt;10898321&lt;/ETODetailNo&gt;&lt;PerformedBy&gt;33620562&lt;/PerformedBy&gt;&lt;Technician&gt;RIS&lt;/Technician&gt;&lt;StatusFlag&gt;1&lt;/StatusFlag&gt;&lt;SysFlag&gt;1&lt;/SysFlag&gt;&lt;/CallInfo&gt; </parameter></ESP_Input></soap:Body></soap:Envelope>";
		String endPoint5 = "http://172.26.10.55:8282/MedicalServiceESB.asmx";
		String soapAction5 = "http://tempuri.org/ESP_Input";
		System.out.println("5"+invokeSrvSoap(endPoint5,soapAction5, soapXML5));
		
		//6	172.26.82.208:2210	http://172.26.10.55:8282/MedicalServiceESB.asmx	\"http://tempuri.org/ESP_Input\"	<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>RuiKe</user><password>eea79c4e7f1c453d866bcb0bbd22138e</password><methodCode>EMR-ET-S02</methodCode><methodName>SynchReceiveInfo</methodName><parameter>&lt;CallInfo&gt;&lt;ETONo&gt;9690889&lt;/ETONo&gt;&lt;ETODetailNo&gt;10898321&lt;/ETODetailNo&gt;&lt;PerformedBy&gt;33620562&lt;/PerformedBy&gt;&lt;Technician&gt;RIS&lt;/Technician&gt;&lt;StatusFlag&gt;1&lt;/StatusFlag&gt;&lt;SysFlag&gt;1&lt;/SysFlag&gt;&lt;/CallInfo&gt; </parameter></ESP_Input></soap:Body></soap:Envelope>
		String soapXML6 = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>RuiKe</user><password>eea79c4e7f1c453d866bcb0bbd22138e</password><methodCode>EMR-ET-S02</methodCode><methodName>SynchReceiveInfo</methodName><parameter>&lt;CallInfo&gt;&lt;ETONo&gt;9690889&lt;/ETONo&gt;&lt;ETODetailNo&gt;10898321&lt;/ETODetailNo&gt;&lt;PerformedBy&gt;33620562&lt;/PerformedBy&gt;&lt;Technician&gt;RIS&lt;/Technician&gt;&lt;StatusFlag&gt;1&lt;/StatusFlag&gt;&lt;SysFlag&gt;1&lt;/SysFlag&gt;&lt;/CallInfo&gt; </parameter></ESP_Input></soap:Body></soap:Envelope>";
		String endPoint6 = "http://172.26.10.55:8282/MedicalServiceESB.asmx";
		String soapAction6 = "http://tempuri.org/ESP_Input";
		System.out.println("6"+invokeSrvSoap(endPoint6,soapAction6, soapXML6));
		
		//7	172.26.82.208:2210	http://172.26.10.55:8282/MedicalServiceESB.asmx	\"http://tempuri.org/ESP_Input\"	<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>RuiKe</user><password>eea79c4e7f1c453d866bcb0bbd22138e</password><methodCode>EMR-ET-S03</methodCode><methodName>SynchExecuteInfo</methodName><parameter>&lt;CallInfo&gt;&lt;ETONo&gt;9690889&lt;/ETONo&gt;&lt;ETODetailNo&gt;10898321&lt;/ETODetailNo&gt;&lt;ExecuteDate&gt;2018-04-23 16:48:27&lt;/ExecuteDate&gt;&lt;ExecuterId&gt;&lt;/ExecuterId&gt;&lt;/CallInfo&gt; </parameter></ESP_Input></soap:Body></soap:Envelope>
		String soapXML7 = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>RuiKe</user><password>eea79c4e7f1c453d866bcb0bbd22138e</password><methodCode>EMR-ET-S03</methodCode><methodName>SynchExecuteInfo</methodName><parameter>&lt;CallInfo&gt;&lt;ETONo&gt;9690889&lt;/ETONo&gt;&lt;ETODetailNo&gt;10898321&lt;/ETODetailNo&gt;&lt;ExecuteDate&gt;2018-04-23 16:48:27&lt;/ExecuteDate&gt;&lt;ExecuterId&gt;&lt;/ExecuterId&gt;&lt;/CallInfo&gt; </parameter></ESP_Input></soap:Body></soap:Envelope>";
		String endPoint7 = "http://172.26.10.55:8282/MedicalServiceESB.asmx";
		String soapAction7 = "http://tempuri.org/ESP_Input";
		System.out.println("7"+invokeSrvSoap(endPoint7,soapAction7, soapXML7));
		
		//8	172.26.82.208:2210	http://172.26.10.55:8282/MedicalServiceESB.asmx	\"http://tempuri.org/ESP_Input\"	<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>RuiKe</user><password>eea79c4e7f1c453d866bcb0bbd22138e</password><methodCode>EMR-ET-S10</methodCode><methodName>SynchRisReport</methodName><parameter>&lt;CallInfo&gt;&lt;Reportcomment&gt;&lt;ETONo&gt;9690889&lt;/ETONo&gt;&lt;ETODetailNo&gt;10898321&lt;/ETODetailNo&gt;&lt;ReportNo&gt;&lt;/ReportNo&gt;&lt;ExamDateTime&gt;&lt;/ExamDateTime&gt;&lt;ReportDateTime&gt;2018-04-23 16:47:23&lt;/ReportDateTime&gt;&lt;ExamPart&gt;垂体平扫&lt;/ExamPart&gt;&lt;DeptCode&gt;33620562&lt;/DeptCode&gt;&lt;DeptName&gt;33620562&lt;/DeptName&gt;&lt;DoctorId&gt;1034&lt;/DoctorId&gt;&lt;DoctorName&gt;1034&lt;/DoctorName&gt;&lt;CriticalFlag&gt;0&lt;/CriticalFlag&gt;&lt;CriticalContent&gt;&lt;/CriticalContent&gt;&lt;ExamDescript&gt;&lt;/ExamDescript&gt;&lt;DiagDescript&gt;&lt;/DiagDescript&gt;&lt;ExamVerdict&gt;&lt;/ExamVerdict&gt;&lt;ExamDiag&gt;&lt;/ExamDiag&gt;&lt;Reporter&gt;&lt;/Reporter&gt;&lt;ReporterName&gt;&lt;/ReporterName&gt;&lt;DateTime&gt;&lt;/DateTime&gt;&lt;ReportTypeFlag&gt;1&lt;/ReportTypeFlag&gt;&lt;Noter&gt;&lt;/Noter&gt;&lt;ReportURL&gt;&lt;/ReportURL&gt;&lt;ImageURL&gt;&lt;![CDATA[http://172.26.12.37/masterview/mv.jsp?server_name=pacsrjnFIR&amp;user_name=clinic&amp;close_on_exit=true&amp;key_images=false&amp;password=clinic&amp;accession_number=CT9009690889]]&gt;&lt;/ImageURL&gt;&lt;/Reportcomment&gt;&lt;/CallInfo&gt;</parameter></ESP_Input></soap:Body></soap:Envelope>
		String soapXML8 = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>RuiKe</user><password>eea79c4e7f1c453d866bcb0bbd22138e</password><methodCode>EMR-ET-S10</methodCode><methodName>SynchRisReport</methodName><parameter>&lt;CallInfo&gt;&lt;Reportcomment&gt;&lt;ETONo&gt;9690889&lt;/ETONo&gt;&lt;ETODetailNo&gt;10898321&lt;/ETODetailNo&gt;&lt;ReportNo&gt;&lt;/ReportNo&gt;&lt;ExamDateTime&gt;&lt;/ExamDateTime&gt;&lt;ReportDateTime&gt;2018-04-23 16:47:23&lt;/ReportDateTime&gt;&lt;ExamPart&gt;垂体平扫&lt;/ExamPart&gt;&lt;DeptCode&gt;33620562&lt;/DeptCode&gt;&lt;DeptName&gt;33620562&lt;/DeptName&gt;&lt;DoctorId&gt;1034&lt;/DoctorId&gt;&lt;DoctorName&gt;1034&lt;/DoctorName&gt;&lt;CriticalFlag&gt;0&lt;/CriticalFlag&gt;&lt;CriticalContent&gt;&lt;/CriticalContent&gt;&lt;ExamDescript&gt;&lt;/ExamDescript&gt;&lt;DiagDescript&gt;&lt;/DiagDescript&gt;&lt;ExamVerdict&gt;&lt;/ExamVerdict&gt;&lt;ExamDiag&gt;&lt;/ExamDiag&gt;&lt;Reporter&gt;&lt;/Reporter&gt;&lt;ReporterName&gt;&lt;/ReporterName&gt;&lt;DateTime&gt;&lt;/DateTime&gt;&lt;ReportTypeFlag&gt;1&lt;/ReportTypeFlag&gt;&lt;Noter&gt;&lt;/Noter&gt;&lt;ReportURL&gt;&lt;/ReportURL&gt;&lt;ImageURL&gt;&lt;![CDATA[http://172.26.12.37/masterview/mv.jsp?server_name=pacsrjnFIR&amp;user_name=clinic&amp;close_on_exit=true&amp;key_images=false&amp;password=clinic&amp;accession_number=CT9009690889]]&gt;&lt;/ImageURL&gt;&lt;/Reportcomment&gt;&lt;/CallInfo&gt;</parameter></ESP_Input></soap:Body></soap:Envelope>";
		String endPoint8 = "http://172.26.10.55:8282/MedicalServiceESB.asmx";
		String soapAction8 = "http://tempuri.org/ESP_Input";
		System.out.println("8"+invokeSrvSoap(endPoint8,soapAction8, soapXML8));
		
		//9	172.26.82.208:2210	http://172.26.10.55:8282/MedicalServiceESB.asmx	\"http://tempuri.org/ESP_Input\"	<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>RuiKe</user><password>eea79c4e7f1c453d866bcb0bbd22138e</password><methodCode>EMR-ET-S09</methodCode><methodName>SynchRisReportStatus</methodName><parameter>&lt;CallInfo&gt;&lt;ETONo&gt;9690889&lt;/ETONo&gt;&lt;PerformedBy&gt;33620562&lt;/PerformedBy&gt;&lt;Technician&gt;&lt;/Technician&gt;&lt;StatusFlag&gt;1&lt;/StatusFlag&gt;&lt;SysFlag&gt;1&lt;/SysFlag&gt;&lt;/CallInfo&gt;</parameter></ESP_Input></soap:Body></soap:Envelope>
		String soapXML9 = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>RuiKe</user><password>eea79c4e7f1c453d866bcb0bbd22138e</password><methodCode>EMR-ET-S09</methodCode><methodName>SynchRisReportStatus</methodName><parameter>&lt;CallInfo&gt;&lt;ETONo&gt;9690889&lt;/ETONo&gt;&lt;PerformedBy&gt;33620562&lt;/PerformedBy&gt;&lt;Technician&gt;&lt;/Technician&gt;&lt;StatusFlag&gt;1&lt;/StatusFlag&gt;&lt;SysFlag&gt;1&lt;/SysFlag&gt;&lt;/CallInfo&gt;</parameter></ESP_Input></soap:Body></soap:Envelope>";
		String endPoint9 = "http://172.26.10.55:8282/MedicalServiceESB.asmx";
		String soapAction9 = "http://tempuri.org/ESP_Input";
		System.out.println("9"+invokeSrvSoap(endPoint9,soapAction9, soapXML9));
		
		//10	172.26.82.208:2210	http://172.26.10.55:8282/MedicalServiceESB.asmx	\"http://tempuri.org/ESP_Input\"	<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>RuiKe</user><password>eea79c4e7f1c453d866bcb0bbd22138e</password><methodCode>EMR-ET-S09</methodCode><methodName>SynchRisReportStatus</methodName><parameter>&lt;CallInfo&gt;&lt;ETONo&gt;9690889&lt;/ETONo&gt;&lt;PerformedBy&gt;33620562&lt;/PerformedBy&gt;&lt;Technician&gt;&lt;/Technician&gt;&lt;StatusFlag&gt;1&lt;/StatusFlag&gt;&lt;SysFlag&gt;1&lt;/SysFlag&gt;&lt;/CallInfo&gt;</parameter></ESP_Input></soap:Body></soap:Envelope>
		String soapXML10 = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>RuiKe</user><password>eea79c4e7f1c453d866bcb0bbd22138e</password><methodCode>EMR-ET-S09</methodCode><methodName>SynchRisReportStatus</methodName><parameter>&lt;CallInfo&gt;&lt;ETONo&gt;9690889&lt;/ETONo&gt;&lt;PerformedBy&gt;33620562&lt;/PerformedBy&gt;&lt;Technician&gt;&lt;/Technician&gt;&lt;StatusFlag&gt;1&lt;/StatusFlag&gt;&lt;SysFlag&gt;1&lt;/SysFlag&gt;&lt;/CallInfo&gt;</parameter></ESP_Input></soap:Body></soap:Envelope>";
		String endPoint10 = "http://172.26.10.55:8282/MedicalServiceESB.asmx";
		String soapAction10 = "http://tempuri.org/ESP_Input";
		System.out.println("10"+invokeSrvSoap(endPoint10,soapAction10, soapXML10));
		
		//11	172.26.82.208:2210	http://172.26.10.55:8282/MedicalServiceESB.asmx	\"http://tempuri.org/ESP_Input\"	<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>RuiKe</user><password>eea79c4e7f1c453d866bcb0bbd22138e</password><methodCode>EMR-ET-S10</methodCode><methodName>SynchRisReport</methodName><parameter>&lt;CallInfo&gt;&lt;Reportcomment&gt;&lt;ETONo&gt;9690889&lt;/ETONo&gt;&lt;ETODetailNo&gt;10898321&lt;/ETODetailNo&gt;&lt;ReportNo&gt;106c5f73-13c0-411c-b5a4-c0f456a18da1&lt;/ReportNo&gt;&lt;ExamDateTime&gt;&lt;/ExamDateTime&gt;&lt;ReportDateTime&gt;2018-04-23 16:47:53&lt;/ReportDateTime&gt;&lt;ExamPart&gt;垂体平扫&lt;/ExamPart&gt;&lt;DeptCode&gt;33620562&lt;/DeptCode&gt;&lt;DeptName&gt;33620562&lt;/DeptName&gt;&lt;DoctorId&gt;1034&lt;/DoctorId&gt;&lt;DoctorName&gt;1034&lt;/DoctorName&gt;&lt;CriticalFlag&gt;0&lt;/CriticalFlag&gt;&lt;CriticalContent&gt;&lt;/CriticalContent&gt;&lt;ExamDescript&gt;写个报告&lt;/ExamDescript&gt;&lt;DiagDescript&gt;&lt;/DiagDescript&gt;&lt;ExamVerdict&gt;写个报告&lt;/ExamVerdict&gt;&lt;ExamDiag&gt;&lt;/ExamDiag&gt;&lt;Reporter&gt;&lt;/Reporter&gt;&lt;ReporterName&gt;&lt;/ReporterName&gt;&lt;DateTime&gt;&lt;/DateTime&gt;&lt;ReportTypeFlag&gt;1&lt;/ReportTypeFlag&gt;&lt;Noter&gt;&lt;/Noter&gt;&lt;ReportURL&gt;&lt;/ReportURL&gt;&lt;ImageURL&gt;&lt;![CDATA[http://172.26.12.37/masterview/mv.jsp?server_name=pacsrjnFIR&amp;user_name=clinic&amp;close_on_exit=true&amp;key_images=false&amp;password=clinic&amp;accession_number=CT9009690889]]&gt;&lt;/ImageURL&gt;&lt;/Reportcomment&gt;&lt;/CallInfo&gt;</parameter></ESP_Input></soap:Body></soap:Envelope>
		String soapXML11 = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>RuiKe</user><password>eea79c4e7f1c453d866bcb0bbd22138e</password><methodCode>EMR-ET-S10</methodCode><methodName>SynchRisReport</methodName><parameter>&lt;CallInfo&gt;&lt;Reportcomment&gt;&lt;ETONo&gt;9690889&lt;/ETONo&gt;&lt;ETODetailNo&gt;10898321&lt;/ETODetailNo&gt;&lt;ReportNo&gt;106c5f73-13c0-411c-b5a4-c0f456a18da1&lt;/ReportNo&gt;&lt;ExamDateTime&gt;&lt;/ExamDateTime&gt;&lt;ReportDateTime&gt;2018-04-23 16:47:53&lt;/ReportDateTime&gt;&lt;ExamPart&gt;垂体平扫&lt;/ExamPart&gt;&lt;DeptCode&gt;33620562&lt;/DeptCode&gt;&lt;DeptName&gt;33620562&lt;/DeptName&gt;&lt;DoctorId&gt;1034&lt;/DoctorId&gt;&lt;DoctorName&gt;1034&lt;/DoctorName&gt;&lt;CriticalFlag&gt;0&lt;/CriticalFlag&gt;&lt;CriticalContent&gt;&lt;/CriticalContent&gt;&lt;ExamDescript&gt;写个报告&lt;/ExamDescript&gt;&lt;DiagDescript&gt;&lt;/DiagDescript&gt;&lt;ExamVerdict&gt;写个报告&lt;/ExamVerdict&gt;&lt;ExamDiag&gt;&lt;/ExamDiag&gt;&lt;Reporter&gt;&lt;/Reporter&gt;&lt;ReporterName&gt;&lt;/ReporterName&gt;&lt;DateTime&gt;&lt;/DateTime&gt;&lt;ReportTypeFlag&gt;1&lt;/ReportTypeFlag&gt;&lt;Noter&gt;&lt;/Noter&gt;&lt;ReportURL&gt;&lt;/ReportURL&gt;&lt;ImageURL&gt;&lt;![CDATA[http://172.26.12.37/masterview/mv.jsp?server_name=pacsrjnFIR&amp;user_name=clinic&amp;close_on_exit=true&amp;key_images=false&amp;password=clinic&amp;accession_number=CT9009690889]]&gt;&lt;/ImageURL&gt;&lt;/Reportcomment&gt;&lt;/CallInfo&gt;</parameter></ESP_Input></soap:Body></soap:Envelope>";
		String endPoint11 = "http://172.26.10.55:8282/MedicalServiceESB.asmx";
		String soapAction11 = "http://tempuri.org/ESP_Input";
		System.out.println("11"+invokeSrvSoap(endPoint11,soapAction11, soapXML11));
		
		//12	172.26.82.208:2210	http://172.26.10.55:8282/MedicalServiceESB.asmx	\"http://tempuri.org/ESP_Input\"	<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>RuiKe</user><password>eea79c4e7f1c453d866bcb0bbd22138e</password><methodCode>EMR-ET-S10</methodCode><methodName>SynchRisReport</methodName><parameter>&lt;CallInfo&gt;&lt;Reportcomment&gt;&lt;ETONo&gt;9690889&lt;/ETONo&gt;&lt;ETODetailNo&gt;10898321&lt;/ETODetailNo&gt;&lt;ReportNo&gt;106c5f73-13c0-411c-b5a4-c0f456a18da1&lt;/ReportNo&gt;&lt;ExamDateTime&gt;&lt;/ExamDateTime&gt;&lt;ReportDateTime&gt;2018-04-23 16:47:54&lt;/ReportDateTime&gt;&lt;ExamPart&gt;垂体平扫&lt;/ExamPart&gt;&lt;DeptCode&gt;33620562&lt;/DeptCode&gt;&lt;DeptName&gt;33620562&lt;/DeptName&gt;&lt;DoctorId&gt;1034&lt;/DoctorId&gt;&lt;DoctorName&gt;1034&lt;/DoctorName&gt;&lt;CriticalFlag&gt;0&lt;/CriticalFlag&gt;&lt;CriticalContent&gt;&lt;/CriticalContent&gt;&lt;ExamDescript&gt;写个报告&lt;/ExamDescript&gt;&lt;DiagDescript&gt;&lt;/DiagDescript&gt;&lt;ExamVerdict&gt;写个报告&lt;/ExamVerdict&gt;&lt;ExamDiag&gt;&lt;/ExamDiag&gt;&lt;Reporter&gt;zww&lt;/Reporter&gt;&lt;ReporterName&gt;zww&lt;/ReporterName&gt;&lt;DateTime&gt;&lt;/DateTime&gt;&lt;ReportTypeFlag&gt;1&lt;/ReportTypeFlag&gt;&lt;Noter&gt;&lt;/Noter&gt;&lt;ReportURL&gt;&lt;/ReportURL&gt;&lt;ImageURL&gt;&lt;![CDATA[http://172.26.12.37/masterview/mv.jsp?server_name=pacsrjnFIR&amp;user_name=clinic&amp;close_on_exit=true&amp;key_images=false&amp;password=clinic&amp;accession_number=CT9009690889]]&gt;&lt;/ImageURL&gt;&lt;/Reportcomment&gt;&lt;/CallInfo&gt;</parameter></ESP_Input></soap:Body></soap:Envelope>
		String soapXML12 = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>RuiKe</user><password>eea79c4e7f1c453d866bcb0bbd22138e</password><methodCode>EMR-ET-S10</methodCode><methodName>SynchRisReport</methodName><parameter>&lt;CallInfo&gt;&lt;Reportcomment&gt;&lt;ETONo&gt;9690889&lt;/ETONo&gt;&lt;ETODetailNo&gt;10898321&lt;/ETODetailNo&gt;&lt;ReportNo&gt;106c5f73-13c0-411c-b5a4-c0f456a18da1&lt;/ReportNo&gt;&lt;ExamDateTime&gt;&lt;/ExamDateTime&gt;&lt;ReportDateTime&gt;2018-04-23 16:47:54&lt;/ReportDateTime&gt;&lt;ExamPart&gt;垂体平扫&lt;/ExamPart&gt;&lt;DeptCode&gt;33620562&lt;/DeptCode&gt;&lt;DeptName&gt;33620562&lt;/DeptName&gt;&lt;DoctorId&gt;1034&lt;/DoctorId&gt;&lt;DoctorName&gt;1034&lt;/DoctorName&gt;&lt;CriticalFlag&gt;0&lt;/CriticalFlag&gt;&lt;CriticalContent&gt;&lt;/CriticalContent&gt;&lt;ExamDescript&gt;写个报告&lt;/ExamDescript&gt;&lt;DiagDescript&gt;&lt;/DiagDescript&gt;&lt;ExamVerdict&gt;写个报告&lt;/ExamVerdict&gt;&lt;ExamDiag&gt;&lt;/ExamDiag&gt;&lt;Reporter&gt;zww&lt;/Reporter&gt;&lt;ReporterName&gt;zww&lt;/ReporterName&gt;&lt;DateTime&gt;&lt;/DateTime&gt;&lt;ReportTypeFlag&gt;1&lt;/ReportTypeFlag&gt;&lt;Noter&gt;&lt;/Noter&gt;&lt;ReportURL&gt;&lt;/ReportURL&gt;&lt;ImageURL&gt;&lt;![CDATA[http://172.26.12.37/masterview/mv.jsp?server_name=pacsrjnFIR&amp;user_name=clinic&amp;close_on_exit=true&amp;key_images=false&amp;password=clinic&amp;accession_number=CT9009690889]]&gt;&lt;/ImageURL&gt;&lt;/Reportcomment&gt;&lt;/CallInfo&gt;</parameter></ESP_Input></soap:Body></soap:Envelope>";
		String endPoint12 = "http://172.26.10.55:8282/MedicalServiceESB.asmx";
		String soapAction12 = "http://tempuri.org/ESP_Input";
		System.out.println("12"+invokeSrvSoap(endPoint12,soapAction12, soapXML12));
		
		//13	172.26.11.89	http://172.26.10.55:8181/WebSiteSouthRJ/CSWebService.asmx		WSDL,
		String endPoint13 = "http://172.26.10.55:8181/WebSiteSouthRJ/CSWebService.asmx?WSDL";
		System.out.println("13"+invokeSrvGet(endPoint13));
		
		//14	172.26.82.208:2210	http://172.26.10.55:8282/MedicalServiceESB.asmx	\"http://tempuri.org/ESP_Input\"	<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>RuiKe</user><password>eea79c4e7f1c453d866bcb0bbd22138e</password><methodCode>EMR-ET-Q01</methodCode><methodName>QueryPatientInfo</methodName><parameter>&lt;CallInfo&gt;&lt;CardNo&gt;987686&lt;/CardNo&gt;&lt;CardTypeFlag&gt;99&lt;/CardTypeFlag&gt;&lt;HospitalId&gt;56650590500&lt;/HospitalId&gt;&lt;HisId&gt;Beta1_TrustHISV1.18&lt;/HisId&gt;&lt;SysFlag&gt;1&lt;/SysFlag&gt;&lt;/CallInfo&gt;</parameter></ESP_Input></soap:Body></soap:Envelope>
		String soapXML14 = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>RuiKe</user><password>eea79c4e7f1c453d866bcb0bbd22138e</password><methodCode>EMR-ET-Q01</methodCode><methodName>QueryPatientInfo</methodName><parameter>&lt;CallInfo&gt;&lt;CardNo&gt;987686&lt;/CardNo&gt;&lt;CardTypeFlag&gt;99&lt;/CardTypeFlag&gt;&lt;HospitalId&gt;56650590500&lt;/HospitalId&gt;&lt;HisId&gt;Beta1_TrustHISV1.18&lt;/HisId&gt;&lt;SysFlag&gt;1&lt;/SysFlag&gt;&lt;/CallInfo&gt;</parameter></ESP_Input></soap:Body></soap:Envelope>";
		String endPoint14 = "http://172.26.10.55:8282/MedicalServiceESB.asmx";
		String soapAction14 = "http://tempuri.org/ESP_Input";
		System.out.println("14"+invokeSrvSoap(endPoint14,soapAction14, soapXML14));
		
		//15	172.26.82.208:2210	http://172.26.10.55:8282/MedicalServiceESB.asmx	\"http://tempuri.org/ESP_Input\"	<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>RuiKe</user><password>eea79c4e7f1c453d866bcb0bbd22138e</password><methodCode>EMR-ET-Q02</methodCode><methodName>QueryTechOrder</methodName><parameter>&lt;CallInfo&gt;&lt;HisPatId&gt;9690890&lt;/HisPatId&gt;&lt;HisVisitNo&gt;-1&lt;/HisVisitNo&gt;&lt;DeptCode&gt;&lt;/DeptCode&gt;&lt;SysFlag&gt;1&lt;/SysFlag&gt;&lt;/CallInfo&gt;</parameter></ESP_Input></soap:Body></soap:Envelope>
		String soapXML15 = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>RuiKe</user><password>eea79c4e7f1c453d866bcb0bbd22138e</password><methodCode>EMR-ET-Q02</methodCode><methodName>QueryTechOrder</methodName><parameter>&lt;CallInfo&gt;&lt;HisPatId&gt;9690890&lt;/HisPatId&gt;&lt;HisVisitNo&gt;-1&lt;/HisVisitNo&gt;&lt;DeptCode&gt;&lt;/DeptCode&gt;&lt;SysFlag&gt;1&lt;/SysFlag&gt;&lt;/CallInfo&gt;</parameter></ESP_Input></soap:Body></soap:Envelope>";
		String endPoint15 = "http://172.26.10.55:8282/MedicalServiceESB.asmx";
		String soapAction15 = "http://tempuri.org/ESP_Input";
		System.out.println("15"+invokeSrvSoap(endPoint15,soapAction15, soapXML15));
		
		//16	172.26.82.208:2210	http://172.26.10.55:8282/MedicalServiceESB.asmx	\"http://tempuri.org/ESP_Input\"	<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>RuiKe</user><password>eea79c4e7f1c453d866bcb0bbd22138e</password><methodCode>EMR-ET-Q03</methodCode><methodName>QueryTechOrderDetail</methodName><parameter>&lt;CallInfo&gt;&lt;ETONo&gt;9690890&lt;/ETONo&gt;&lt;SysFlag&gt;1&lt;/SysFlag&gt;&lt;/CallInfo&gt;</parameter></ESP_Input></soap:Body></soap:Envelope>
		String soapXML16 = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>RuiKe</user><password>eea79c4e7f1c453d866bcb0bbd22138e</password><methodCode>EMR-ET-Q03</methodCode><methodName>QueryTechOrderDetail</methodName><parameter>&lt;CallInfo&gt;&lt;ETONo&gt;9690890&lt;/ETONo&gt;&lt;SysFlag&gt;1&lt;/SysFlag&gt;&lt;/CallInfo&gt;</parameter></ESP_Input></soap:Body></soap:Envelope>";
		String endPoint16 = "http://172.26.10.55:8282/MedicalServiceESB.asmx";
		String soapAction16 = "http://tempuri.org/ESP_Input";
		System.out.println("16"+invokeSrvSoap(endPoint16,soapAction16, soapXML16));
		
		//17	172.26.82.208:2210	http://172.26.10.55:8282/MedicalServiceESB.asmx	\"http://tempuri.org/ESP_Input\"	<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>RuiKe</user><password>eea79c4e7f1c453d866bcb0bbd22138e</password><methodCode>EMR-ET-S02</methodCode><methodName>SynchReceiveInfo</methodName><parameter>&lt;CallInfo&gt;&lt;ETONo&gt;9690890&lt;/ETONo&gt;&lt;ETODetailNo&gt;10898322&lt;/ETODetailNo&gt;&lt;PerformedBy&gt;33620362&lt;/PerformedBy&gt;&lt;Technician&gt;RIS&lt;/Technician&gt;&lt;StatusFlag&gt;1&lt;/StatusFlag&gt;&lt;SysFlag&gt;1&lt;/SysFlag&gt;&lt;/CallInfo&gt; </parameter></ESP_Input></soap:Body></soap:Envelope>
		String soapXML17 = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>RuiKe</user><password>eea79c4e7f1c453d866bcb0bbd22138e</password><methodCode>EMR-ET-S02</methodCode><methodName>SynchReceiveInfo</methodName><parameter>&lt;CallInfo&gt;&lt;ETONo&gt;9690890&lt;/ETONo&gt;&lt;ETODetailNo&gt;10898322&lt;/ETODetailNo&gt;&lt;PerformedBy&gt;33620362&lt;/PerformedBy&gt;&lt;Technician&gt;RIS&lt;/Technician&gt;&lt;StatusFlag&gt;1&lt;/StatusFlag&gt;&lt;SysFlag&gt;1&lt;/SysFlag&gt;&lt;/CallInfo&gt; </parameter></ESP_Input></soap:Body></soap:Envelope>";
		String endPoint17 = "http://172.26.10.55:8282/MedicalServiceESB.asmx";
		String soapAction17 = "http://tempuri.org/ESP_Input";
		System.out.println("17"+invokeSrvSoap(endPoint17,soapAction17, soapXML17));
		
		//18	172.26.11.89	http://172.26.10.55:8181/WebSiteSouthRJ/CSWebService.asmx		WSDL,
		String endPoint18 = "http://172.26.10.55:8181/WebSiteSouthRJ/CSWebService.asmx?WSDL";
		System.out.println("18"+invokeSrvGet(endPoint18));
		
		//19	172.26.82.208:2210	http://172.26.10.55:8282/MedicalServiceESB.asmx	\"http://tempuri.org/ESP_Input\"	<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>RuiKe</user><password>eea79c4e7f1c453d866bcb0bbd22138e</password><methodCode>EMR-ET-S02</methodCode><methodName>SynchReceiveInfo</methodName><parameter>&lt;CallInfo&gt;&lt;ETONo&gt;9690890&lt;/ETONo&gt;&lt;ETODetailNo&gt;10898322&lt;/ETODetailNo&gt;&lt;PerformedBy&gt;&lt;/PerformedBy&gt;&lt;Technician&gt;RIS&lt;/Technician&gt;&lt;StatusFlag&gt;0&lt;/StatusFlag&gt;&lt;SysFlag&gt;1&lt;/SysFlag&gt;&lt;/CallInfo&gt; </parameter></ESP_Input></soap:Body></soap:Envelope>
		String soapXML19 = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ESP_Input xmlns=\"http://tempuri.org/\"><user>RuiKe</user><password>eea79c4e7f1c453d866bcb0bbd22138e</password><methodCode>EMR-ET-S02</methodCode><methodName>SynchReceiveInfo</methodName><parameter>&lt;CallInfo&gt;&lt;ETONo&gt;9690890&lt;/ETONo&gt;&lt;ETODetailNo&gt;10898322&lt;/ETODetailNo&gt;&lt;PerformedBy&gt;&lt;/PerformedBy&gt;&lt;Technician&gt;RIS&lt;/Technician&gt;&lt;StatusFlag&gt;0&lt;/StatusFlag&gt;&lt;SysFlag&gt;1&lt;/SysFlag&gt;&lt;/CallInfo&gt; </parameter></ESP_Input></soap:Body></soap:Envelope>";
		String endPoint19 = "http://172.26.10.55:8282/MedicalServiceESB.asmx";
		String soapAction19 = "http://tempuri.org/ESP_Input";
		System.out.println("19"+invokeSrvSoap(endPoint19,soapAction19, soapXML19));*/
		
		
		
    }
}
