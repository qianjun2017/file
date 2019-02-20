/**
 * 
 */
package com.cc.common.tools;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Administrator
 *
 */
public class JsonTools {
	
	private static ObjectMapper mapper=new ObjectMapper();
    private static SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.setDateFormat(format);
    }

	/**
	 * object转为json字符串
	 * @param object
	 * @return
	 */
	public static String toJsonString(Object object){
		StringWriter writer = new StringWriter();
		try {
			JsonGenerator generator = new JsonFactory().createGenerator(writer);
			mapper.writeValue(generator, object);
			writer.flush();
			generator.close();
			return writer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * json字符串转为简单object
	 * @param jsonString
	 * @param clazz
	 * @return
	 */
	public static <T> T toObject(String jsonString, Class<T> clazz){
        try {
            return mapper.readValue(jsonString, clazz);
        } catch (JsonParseException e) {
            throw new RuntimeException("JSON 格式化出错",e);
        } catch (JsonMappingException e) {
            throw new RuntimeException("JSON 格式化出错",e);
        } catch (IOException e) {
            throw new RuntimeException("网络连接异常",e);
        }
    }
}
