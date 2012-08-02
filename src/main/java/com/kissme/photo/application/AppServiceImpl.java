package com.kissme.photo.application;

import com.google.inject.Inject;
import com.kissme.photo.domain.app.App;
import com.kissme.photo.domain.app.AppExistsException;
import com.kissme.photo.domain.app.AppRepository;
import com.kissme.photo.infrastructure.Page;

/**
 * 
 * @author loudyn
 * 
 */
public class AppServiceImpl implements AppService {

	private AppRepository appRepository;

	@Inject
	public AppServiceImpl(AppRepository appRepository) {
		this.appRepository = appRepository;
	}

	@Override
	public void save(App entity) {
		checkAppNotExists(entity);
		appRepository.save(entity);
	}

	private void checkAppNotExists(App entity) {
		if (appRepository.existsApp(entity)) {
			throw new AppExistsException();
		}
	}

	@Override
	public void update(App entity) {
		appRepository.update(entity);
	}

	@Override
	public void delete(String id) {
		appRepository.delete(id);
	}

	@Override
	public App get(String id) {
		return appRepository.get(id);
	}

	@Override
	public App getByAppKey(String appKey) {
		return appRepository.getByAppKey(appKey);
	}

	@Override
	public Page<App> findPage(Page<App> page) {
		return appRepository.findPage(page);
	}

}
