package com.kissme.photo.infrastructure.ioc;

import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.inject.Binding;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Stage;

/**
 * 
 * @author loudyn
 * 
 */
public final class GuiceIoc implements Ioc {
	private Injector injector;

	/**
	 * 
	 * @param modules
	 */
	public GuiceIoc(Iterable<Module> modules) {
		injector = Guice.createInjector(Stage.PRODUCTION, modules);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kissme.photo.infrastructure.ioc.Ioc#getBean(java.lang.Class)
	 */
	public <T> T getBean(Class<T> clz) {
		return injector.getInstance(clz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kissme.photo.infrastructure.ioc.Ioc#getAllBeans()
	 */
	public List<Object> getAllBeans() {
		Map<Key<?>, Binding<?>> bindings = injector.getAllBindings();
		return Lists.transform(Lists.newArrayList(bindings.keySet()), new Function<Key<?>, Object>() {

			public Object apply(Key<?> input) {
				return injector.getInstance(input);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kissme.photo.infrastructure.ioc.Ioc#inject(java.lang.Object)
	 */
	public void inject(Object bean) {
		injector.injectMembers(bean);
	}

}
