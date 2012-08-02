package com.kissme.photo.infrastructure.ioc;

import java.util.List;

/**
 * 
 * @author loudyn
 * 
 */
public interface Ioc {

	/**
	 * 
	 * @param clz
	 * @return
	 */
	public <T> T getBean(Class<T> clz);

	/**
	 * 
	 * @return
	 */
	public List<Object> getAllBeans();

	/**
	 * 
	 * @param bean
	 */
	public void inject(Object bean);
}
