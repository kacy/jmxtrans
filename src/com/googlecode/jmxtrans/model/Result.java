package com.googlecode.jmxtrans.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.ImmutableMap;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;


import java.util.Map;



import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL;

/**
 * Represents the result of a query.
 * 
 * @author jon
 */
@JsonSerialize(include = NON_NULL)
@ThreadSafe
@Immutable
public class Result {
	private final String attributeName;
	private final String className;
	private final String typeName;
	private final ImmutableMap<String, Object> values;
	private final long epoch;
	private final String classNameAlias;

	public Result(long epoch, String attributeName, String className, String classNameAlias, String typeName, Map<String, Object> values) {
		this.className = className;
		this.typeName = typeName;
		this.values = convertJsonValues(values);
		this.epoch = epoch;
		this.attributeName = attributeName;
		this.classNameAlias = classNameAlias;
	}

	private ImmutableMap<String, Object> convertJsonValues(Map<String, Object> values) {

		for (Map.Entry<String, Object> value : values.entrySet()) {

			if (value.getValue().toString().contains("{") && value.getValue().toString().contains("}")) {
				String json = value.getValue().toString();
				json = json.replaceAll("\\{", "");
				json = json.replaceAll("\\}", "");
				String[] objects = json.split(",");

				for (String object : objects) {
					String[] pair = object.split(":");
					values.put(pair[0], pair[1]);
				}

				values.remove(value.getKey());
			}
		}

		ImmutableMap<String, Object> parsed = ImmutableMap.copyOf(values);
		return parsed;
	}

	public String getClassName() {
		return className;
	}

	/**
	 * Specified as part of the query.
	 */
	public String getClassNameAlias() {
		return classNameAlias;
	}

	public String getTypeName() {
		return typeName;
	}

	public ImmutableMap<String, Object> getValues() {
		return values;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public long getEpoch() {
		return this.epoch;
	}

	@Override
	public String toString() {
		return "Result [attributeName=" + attributeName + ", className=" + className + ", typeName=" + typeName + ", values=" + getValues() + ", epoch="
				+ epoch + "]";
	}
}
