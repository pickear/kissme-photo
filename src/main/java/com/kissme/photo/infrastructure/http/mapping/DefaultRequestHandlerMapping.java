package com.kissme.photo.infrastructure.http.mapping;

import java.util.Map;

import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang.StringUtils;
import org.jboss.netty.handler.codec.http.HttpMethod;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.kissme.photo.infrastructure.http.RequestHandler;

/**
 * 
 * @author loudyn
 * 
 */
public class DefaultRequestHandlerMapping implements RequestHandlerMapping {

	private PathMappingTrie<RequestHandler> getHandlers = new PathMappingTrie<RequestHandler>("/");
	private PathMappingTrie<RequestHandler> postHandlers = new PathMappingTrie<RequestHandler>("/");
	private PathMappingTrie<RequestHandler> putHandlers = new PathMappingTrie<RequestHandler>("/");
	private PathMappingTrie<RequestHandler> deleteHandlers = new PathMappingTrie<RequestHandler>("/");

	public void register(HttpMethod[] methods, String mapping, RequestHandler handler) {

		for (HttpMethod method : methods) {
			doRegister(method, mapping, handler);
		}
	}

	private void doRegister(HttpMethod method, String mapping, RequestHandler handler) {
		if (HttpMethod.GET == method) {
			getHandlers.insert(mapping, handler);
			return;
		}

		if (HttpMethod.POST == method) {
			postHandlers.insert(mapping, handler);
			return;
		}

		if (HttpMethod.PUT == method) {
			putHandlers.insert(mapping, handler);
			return;
		}

		deleteHandlers.insert(mapping, handler);
	}

	public RequestHandler getHandler(HttpMethod method, String mapping, Map<String, String> pathVariables) {

		if (HttpMethod.GET == method) {
			return getHandlers.retrieve(mapping, pathVariables);
		}
		if (HttpMethod.POST == method) {
			return postHandlers.retrieve(mapping, pathVariables);
		}
		if (HttpMethod.PUT == method) {
			return putHandlers.retrieve(mapping, pathVariables);
		}
		if (HttpMethod.DELETE == method) {
			return deleteHandlers.retrieve(mapping, pathVariables);
		}

		return null;
	}

	public class PathMappingTrie<T> {
		private String pathSeparator;
		private PathTrieNode<T> root;

		public PathMappingTrie(String pathSeparator) {
			this.pathSeparator = pathSeparator;
			root = new PathTrieNode<T>("/", "*", null);
		}

		public void insert(String path, T value) {
			String[] paths = StringUtils.split(path, pathSeparator);
			if (paths.length == 0) {
				root.value = value;
				return;
			}

			int index = 0;
			if (paths.length > 0 && StringUtils.isBlank(paths[0])) {
				index = 1;
			}

			root.insert(paths, index, value);
		}

		public T retrieve(String path, Map<String, String> pathVariables) {

			String[] paths = StringUtils.split(path, pathSeparator);
			if (paths.length == 0) {
				return root.value;
			}

			int index = 0;
			if (paths.length > 0 && StringUtils.isBlank(paths[0])) {
				index = 1;
			}

			return root.retrieve(paths, index, pathVariables);
		}

		class PathTrieNode<V> {
			private String wildcard;
			private String pathWildcard;

			private V value;

			private Map<String, PathTrieNode<V>> children = Maps.newHashMap();

			public PathTrieNode(String path, String wildcard, V value) {
				Preconditions.checkNotNull(path);
				Preconditions.checkNotNull(wildcard);

				this.wildcard = wildcard;
				if (isPathWildcard(path)) {
					pathWildcard = path.replaceAll("\\{(.*)\\}", "$1");
				}

				this.value = value;
			}

			private boolean isPathWildcard() {
				return null != pathWildcard;
			}

			private boolean isPathWildcard(String path) {
				return path.startsWith("{") && path.endsWith("}");
			}

			public void insert(String[] paths, int index, V value) {
				if (index >= paths.length) {
					return;
				}

				String path = paths[index];
				String actual = path;
				if (isPathWildcard(path)) {
					actual = wildcard;
				}

				PathTrieNode<V> node = children.get(actual);
				if (null != node) {

					if (index == paths.length - 1) {
						Preconditions.checkArgument(null == node.value || node.value == value);
						if (null == node.value) {
							node.value = value;
						}
					}
				}
				else {
					if (index == paths.length - 1) {
						node = new PathTrieNode<V>(path, wildcard, value);
					}
					else {
						node = new PathTrieNode<V>(path, wildcard, null);
					}

					children.put(actual, node);
				}

				node.insert(paths, index + 1, value);
			}

			public V retrieve(String[] paths, int index, Map<String, String> pathVariables) {
				if (index >= paths.length) {
					return null;
				}

				String path = paths[index];
				PathTrieNode<V> node = children.get(path);
				if (null == node) {

					node = children.get(wildcard);
					if (null == node) {
						return null;
					}
					else if (null != pathVariables && node.isPathWildcard()) {
						pathVariables.put(node.pathWildcard, StringUtils.trim(decode(path)));
					}
				}

				if (index == paths.length - 1) {
					return node.value;
				}

				return node.retrieve(paths, index + 1, pathVariables);
			}

			private String decode(String path) {
				try {

					return new URLCodec().decode(path, "UTF-8");
				} catch (Exception e) {
					return path;
				}
			}
		}
	}

}
