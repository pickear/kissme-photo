package com.kissme.photo.domain.gallery;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.kissme.photo.domain.AbstractDomain;
import com.kissme.photo.domain.app.App;

/**
 * 
 * @author loudyn
 * 
 */
public class Gallery extends AbstractDomain {

	private static final long serialVersionUID = 1L;

	private String name;
	private int capacity = 256;
	private App app;

	public String getName() {
		return name;
	}

	public Gallery setName(String name) {
		this.name = name;
		return this;
	}

	public int getCapacity() {
		return capacity;
	}

	public Gallery setCapacity(int capacity) {
		this.capacity = capacity;
		return this;
	}

	@JsonIgnore
	public App getApp() {
		return app;
	}

	public Gallery setApp(App app) {
		this.app = app;
		return this;
	}

}
