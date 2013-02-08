/*
 * File: BusinessDayConventionResolver.java
 * 
 * Copyright 2013 OSFramework Project.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.osframework.contract.date.convention;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.jfin.date.BusinessDayConvention;

/**
 * Utility for resolution of business day convention from a text notation.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public final class BusinessDayConventionResolver {

	private static final BusinessDayConvention DEFAULT = BusinessDayConvention.UNADJUSTED;
	private static final Map<BusinessDayConvention, List<String>> ALIAS_MAP;

	static {
		ALIAS_MAP = new HashMap<BusinessDayConvention, List<String>>();
		ALIAS_MAP.put(BusinessDayConvention.FOLLOWING, Arrays.asList("FOLLOWING", "FOLLOW", "FOL", "F"));
		ALIAS_MAP.put(BusinessDayConvention.MODIFIED_FOLLOWING, Arrays.asList("MODIFIED_FOLLOWING", "MOD_FOLLOWING", "MOD_FOLLOW", "MOD_FOL", "MF"));
		ALIAS_MAP.put(BusinessDayConvention.MODIFIED_PRECEDING, Arrays.asList("MODIFIED_PRECEDING", "MOD_PRECEDING", "MOD_PRECEDE", "MOD_PRE", "MP"));
		ALIAS_MAP.put(BusinessDayConvention.MONTH_END_REFERENCE, Arrays.asList("MONTH_END_REFERENCE", "MONTH_END", "MONTHEND", "MER", "ME"));
		ALIAS_MAP.put(BusinessDayConvention.PRECEDING, Arrays.asList("PRECEDING", "PRECEDE", "PRE", "P"));
		ALIAS_MAP.put(BusinessDayConvention.UNADJUSTED, Arrays.asList("UNADJUSTED", "NONE"));
	}

	private BusinessDayConventionResolver() {}

	/**
	 * Resolve specified notation to a recognized business day convention.
	 * 
	 * @param notation human-entered notation to be resolved
	 * @return identified business day convention
	 * @throws IllegalArgumentException if notation cannot be recognized
	 */
	public static BusinessDayConvention resolve(final String notation) {
		if (StringUtils.isBlank(notation)) {
			throw new IllegalArgumentException("Notation argument cannot be null or empty string");
		}
		BusinessDayConvention bdc = null;
		String n = notation.trim().replaceAll("\\s+", "_").toUpperCase(Locale.ENGLISH);
		Iterator<Entry<BusinessDayConvention, List<String>>> it = ALIAS_MAP.entrySet().iterator();
		while (it.hasNext()) {
			Entry<BusinessDayConvention, List<String>> entry = it.next();
			if (entry.getValue().contains(n)) {
				bdc = entry.getKey();
				break;
			}
		}
		if (null == bdc) {
			throw new IllegalArgumentException("Cannot resolve notation '" + notation + "' to business day convention");
		}
		return bdc;
	}

	public static BusinessDayConvention defaultBusinessDayConvention() {
		return DEFAULT;
	}

}
