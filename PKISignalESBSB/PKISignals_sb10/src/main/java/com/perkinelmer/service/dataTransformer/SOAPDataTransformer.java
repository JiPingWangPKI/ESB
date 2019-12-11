//
// 王吉平
//

package com.perkinelmer.service.dataTransformer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.jetty.server.Request;
import org.slf4j.Logger;
import com.streambase.sb.DataType;
import com.streambase.sb.Schema;
import com.streambase.sb.StreamBaseException;
import com.streambase.sb.Tuple;
import com.streambase.sb.TupleException;
import com.streambase.sb.adapter.webserver.data.BaseRequestDataTransformer;
import com.streambase.sb.adapter.webserver.data.IRequestDataTupleSender;
import com.streambase.sb.operator.TypecheckException;

public class SOAPDataTransformer extends BaseRequestDataTransformer {
    public static final String FIELD_DATA = "Data";

    public static final String SETTING_BUFFER_SIZE = "BufferSize";
    public static final int DEFAULT_BUFFER_SIZE = 4096;

    protected List<String> getSettingNames() {
        return Arrays.asList(new String[] { SETTING_BUFFER_SIZE });
    }

    private int getBufferSize(Map<String, String> settings) {
        if (settings.containsKey(SETTING_BUFFER_SIZE)) {
            return Integer.parseInt(settings.get(SETTING_BUFFER_SIZE));
        }
        return DEFAULT_BUFFER_SIZE;
    }

    @Override
    public void validateSettings(Map<String, String> settings, Schema schema) throws TypecheckException {
        checkSettingNames(settings);
        try {
            if (!schema.hasField(FIELD_DATA) || schema.getField(FIELD_DATA).getDataType() != DataType.STRING) {
                throw new TypecheckException("The schema must contain a field named '" + FIELD_DATA + "' of type " + DataType.STRING.toString());
            }
        } catch (TupleException e) {
            throw new TypecheckException("Error checking schema: " + e.getMessage(), e);
        }
        if (settings.containsKey(SETTING_BUFFER_SIZE)) {
            try {
                int bufferSize = Integer.parseInt(settings.get(SETTING_BUFFER_SIZE));
                if (bufferSize <= 0) {
                    throw new TypecheckException("If '" + SETTING_BUFFER_SIZE + "' is specified is must be an integer value greater than 0");
                }
            } catch (NumberFormatException e) {
                throw new TypecheckException("If '" + SETTING_BUFFER_SIZE + "' is specified is must be an integer value greater than 0");
            }
        }
    }

    @Override
    public void transformRequestData(IRequestDataTupleSender sender, Logger logger, Map<String, String> settings, Schema schema, HttpServletRequest httpServletRequest, Request request) throws StreamBaseException {
        int bufferSize = getBufferSize(settings);
        //if (request.getMethod().equalsIgnoreCase("POST")) {
            try {
                readInputStream(sender, httpServletRequest.getInputStream(), schema, "", bufferSize);
            } catch (IOException e) {
                throw new StreamBaseException("Error reading stream: " + e.getMessage(), e);
            }
        //}
    }

    private void readInputStream(IRequestDataTupleSender sender, InputStream is, Schema schema, String name, int bufferSize) throws TupleException, IOException {
    	//方式一
    	/*StringBuilder sb = new StringBuilder();
        byte[] readBytes = new byte[bufferSize];
        while (is.available() > 0) {
        	java.util.Arrays.fill(readBytes,(byte)0);//每次将之前的缓存清除
            int readCount = is.read(readBytes);
            if (readCount <= 0) {
                break;
            }
            sb.append(new String(readBytes, "UTF-8"));
        }
        is.close();
        String result = sb.toString().trim();*/
    	//方式二,成功了！
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[bufferSize];
        int length;
        while ((length = is.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        is.close();
        String result = baos.toString("UTF-8").trim();
    	//方式三
    	/*String newLine = System.getProperty("line.separator");
    	BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
    	StringBuilder sb = new StringBuilder();
    	String line; boolean flag = false;
    	while ((line = reader.readLine()) != null) {
    	    sb.append(flag? newLine: "").append(line);
    	    flag = true;
    	}
    	reader.close();
    	String result = sb.toString().trim();*/
        Tuple tuple = schema.createTuple();        
        tuple.setString(FIELD_DATA, result);
        sender.sendTuple(tuple);
    }
}
