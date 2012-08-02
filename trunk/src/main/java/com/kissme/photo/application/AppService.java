package com.kissme.photo.application;

import com.kissme.photo.domain.app.App;
import com.kissme.photo.infrastructure.Page;

/**
 * 
 * @author loudyn
 * 
 */
public interface AppService {

	/**
	 * 
	 * @param id
	 * @return
	 */
	App get(String id);

	/**
	 * 
	 * @param appKey
	 * @return
	 */
	App getByAppKey(String appKey);

	/**
	 * 
	 * @param page
	 * @return
	 */
	Page<App> findPage(Page<App> page);

	/**
	 * 
	 * @param entity
	 */
	void save(App entity);

	/**
	 * 
	 * @param entity
	 */
	void update(App entity);

	/**
	 * 
	 * @param id
	 */
	void delete(String id);

}
