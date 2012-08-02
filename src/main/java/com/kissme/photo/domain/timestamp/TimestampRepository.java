package com.kissme.photo.domain.timestamp;

/**
 * 
 * @author loudyn
 * 
 */
public interface TimestampRepository {

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
