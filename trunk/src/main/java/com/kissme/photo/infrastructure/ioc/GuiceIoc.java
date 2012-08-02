package com.kissme.photo.infrastructure.ioc;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.inject.Binding;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.kissme.photo.infrastructure.lookup.Lookups;

/**
 * 
 * @author loudyn
 * 
 */
public class GuiceIoc implements Ioc {
	private static Injector injector;

	static {

		final Iterator<Module> modules = Lookups.lookupMultiple(Module.class);
		injector = Guice.createInjector(new Iterable<Module>() {

			public Iterator<Module> iterator() {
				return modules;
			}

		});
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
