package uk.org.cobaltdevelopment.test.db.dataset;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Context for storing values that are made available to any replacement
 * <code>DataSet</code> implementations.
 * 
 * @author "Christopher Edgar"
 * 
 */
public class DataSetContext {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DataSetContext.class);

	private Map<String, Object> context = new HashMap<String, Object>();

	/**
	 * Retrieve value from context by key.
	 * 
	 * @param key
	 * @throws IllegalArgumentException
	 *             thrown when no object found for key
	 */
	public Object get(String key) throws IllegalArgumentException {
		Object result = context.get(key);
		if (result == null) {
			throw new IllegalArgumentException("No object found for key: "
					+ key);
		}
		return result;
	}

	/**
	 * Generics version of context getter.
	 * 
	 * @throws IllegalArgumentException
	 *             thrown when no object found for key
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> valueType, String key)
			throws IllegalArgumentException {
		LOGGER.debug("Retrieving value with key {} from Test Context", key);

		try {
			T result = (T) context.get(key);
			if (result == null) {
				throw new IllegalArgumentException("No object found for key: "
						+ key);
			}
			LOGGER.debug("Return {}", result);
			return result;
		} catch (Exception e) {
			LOGGER.error("ERROR retrieving value from Test Context", e);
			throw new IllegalArgumentException(
					String.format(
							"Error retrieving context value of type [%s] with key [%s]",
							valueType.getSimpleName(), key));
		}
	}

	/**
	 * Set a value into the context identifiable by key. Any value with same key
	 * is replaced.
	 */
	public <T> void set(String key, T value) {
		LOGGER.debug("Setting value with key {} into Test Context", key);
		context.put(key, value);
	}

	/**
	 * Returns the keys for all object sin context as an iterator.
	 * 
	 */
	public Iterator<String> keys() {
		return context.keySet().iterator();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("DataSetContext{context[");
		for (String key : context.keySet()) {
			sb.append(key)
			.append(":")
			.append(context.get(key))
			.append(",");
		}

		sb.append("]}");

		return sb.toString();
	}
}
