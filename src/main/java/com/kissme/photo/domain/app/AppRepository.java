package com.kissme.photo.domain.app;

import com.kissme.photo.infrastructure.Page;

/**
 * 
 * @author loudyn
 * 
 */
public interface AppRepository {

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
	 * @param entity
	 * @return
	 */
	boolean existsApp(App entity);

	/**
	 * 
	 * @param page
	 * @return
	 */
	Page<App> findPage(Page<App> page);

}
