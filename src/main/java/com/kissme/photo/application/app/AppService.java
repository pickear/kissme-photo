package com.kissme.photo.application.app;

import com.kissme.photo.domain.Page;
import com.kissme.photo.domain.app.App;

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
