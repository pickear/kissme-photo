package com.kissme.photo.domain;
/**
 * 
 * @author loudyn
 *
 * @param <T>
 */
public interface DomainObject<T> {
	/**
	 * 
	 * @param other
	 * @return
	 */
	boolean sameIdentityAs(T other);
}
