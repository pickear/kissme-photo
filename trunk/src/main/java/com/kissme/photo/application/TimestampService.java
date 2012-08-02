package com.kissme.photo.application;

/**
 * 
 * @author loudyn
 * 
 */
public interface TimestampService {

	/**
	 * 
	 * @param timestamp
	 */
	void save(String timestamp);

	/**
	 * 
	 * @param timestamp
	 * @return
	 */
	boolean existsTimestamp(String timestamp);
}
