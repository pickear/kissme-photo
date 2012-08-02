package com.kissme.photo.infrastructure;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

/**
 * @author loudyn
 */
public class Jsons {
	private final static ObjectMapper mapper = new ObjectMapper();

	static {
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
	}

	/**
	 * @param bean
	 * @return
	 */
	public static String toJsonString(Object bean) {
		try {

			return mapper.writeValueAsString(bean);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @param <T>
	 * @param jsonString
	 * @param clazz
	 * @return
	 */
	public static <T> T fromJsonString(String jsonString, Class<T> clazz) {
		try {

			return mapper.readValue(jsonString, clazz);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @param bean
	 * @param clazz
	 * @return
	 */
	public static <T> T newfor(Object bean, Class<T> clazz) {
		try {

			return mapper.convertValue(bean, clazz);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
