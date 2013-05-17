/*
 * File: FinancialCalendar.java
 * 
 * Copyright 2012 OSFramework Project.
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
package org.osframework.contract.date.fincal.definition;

import java.io.Serializable;
import java.util.Collections;
import java.util.Currency;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.osframework.contract.date.fincal.ImmutableEntity;

/**
 * Holiday calendar definition for a particular financial market. An instance of
 * this class is an iterable collection of <code>HolidayDefinition</code>s
 * representing the financial ("bank") holidays observed in the associated
 * market's country or economic zone.
 * <p>
 * The {@link #id} property of a <code>FinancialCalendar</code> object is not
 * arbitrary: valid values correspond to the financial calendar codes specified
 * by <b>financialcalendar.com</b>, the <i>de facto</i> authoritative source of
 * calendar data used by financial institutions worldwide.
 * </p>
 * <p>An instance of this class can provide an immutable and thread-safe
 * version of itself.</p>
 * 
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 * @see <a href="http://www.financialcalendar.com/Data/Holidays/Coverage">
   Coverage - Financial Calendar</a>
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "calendar", propOrder = {
	"description",
	"centralBank",
	"holidayDefinitions"
})
public class FinancialCalendar implements Serializable, Iterable<HolidayDefinition>, ImmutableEntity<FinancialCalendar> {

	/**
	 * Serializable UID.
	 */
	private static final long serialVersionUID = 6295632117301841003L;

	private String id;
	private String description;
	private CentralBank centralBank;
	private Set<HolidayDefinition> holidayDefinitions = new HashSet<HolidayDefinition>();

	/**
	 * Default constructor.
	 */
	public FinancialCalendar() {}

	/**
	 * Constructor. Holiday definitions passed to this constructor are copied to
	 * an new collection.
	 *
	 * @param id unique identifier for this instance
	 * @param description short description of this calendar definition
	 * @param centralBank central bank which governs this calendar's holiday definitions
	 * @param holidayDefinitions collection of holiday definitions which comprise this calendar
	 * @throws IllegalArgumentException if centralBank or holidayDefinitions is null
	 */
	public FinancialCalendar(final String id,
			                 final String description,
			                 final CentralBank centralBank,
			                 final Set<HolidayDefinition> holidayDefinitions) {
		this();
		this.setId(id);
		this.setDescription(description);
		this.setCentralBank(centralBank);
		this.setHolidayDefinitions(holidayDefinitions);
	}

	/**
	 * @return unique identifier for this instance
	 */
	@XmlID
	@XmlAttribute(name = "id", required = true)
	public String getId() {
		return id;
	}

	/**
	 * Set unique identifier for this financial calendar.
	 * 
	 * @param id unique identifier for this instance
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return short description of this calendar definition
	 */
	@XmlElement(name = "description", required = true)
	public String getDescription() {
		return description;
	}

	/**
	 * Set description of this financial calendar.
	 * 
	 * @param description short description of this calendar definition
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return central bank which governs this calendar's holiday definitions
	 */
	@XmlIDREF
	public CentralBank getCentralBank() {
		return centralBank;
	}

	/**
	 * Set central bank of this financial calendar.
	 * 
	 * @param centralBank central bank which governs this calendar's holiday
	 *                    definitions
	 * @throws IllegalArgumentException if centralBank is null
	 */
	public void setCentralBank(CentralBank centralBank) {
		if (null == centralBank) {
			throw new IllegalArgumentException("CentralBank argument cannot be null");
		}
		this.centralBank = centralBank;
	}

	/**
	 * @return currency with which this calendar definition is associated
	 */
	@XmlTransient
	public Currency getCurrency() {
		return (null == centralBank) ? null : centralBank.getCurrency();
	}

	/**
	 * @return holiday definitions referenced by this financial calendar
	 */
	@XmlElementWrapper(name = "holidayDefinitions", required = true)
	@XmlElementRef
	public Set<HolidayDefinition> getHolidayDefinitions() {
		return holidayDefinitions;
	}

	/**
	 * Set holiday definitions to be referenced by this financial calendar.
	 * 
	 * @param holidayDefinitions holiday definitions referenced by this
	 *                           financial calendar
	 * @throws IllegalArgumentException if holidayDefinitions is null
	 */
	public void setHolidayDefinitions(Set<HolidayDefinition> holidayDefinitions) {
		if (null == holidayDefinitions) {
			throw new IllegalArgumentException("Set argument cannot be null");
		}
		this.holidayDefinitions.clear();
		this.holidayDefinitions.addAll(holidayDefinitions);
	}

	/**
	 * Add a holiday definition to this financial calendar.
	 * 
	 * @param holidayDefinition holiday definition to be added
	 * @throws IllegalArgumentException if holidayDefinition is null
	 */
	public void addHolidayDefinition(HolidayDefinition holidayDefinition) {
		if (null == holidayDefinition) {
			throw new IllegalArgumentException("holidayDefinition argument cannot be null");
		}
		this.holidayDefinitions.add(holidayDefinition);
	}

	/**
	 * Remove a holiday definition from this financial calendar.
	 * 
	 * @param holidayDefinition holiday definition to be removed
	 * @throws IllegalArgumentException if holidayDefinition is null
	 */
	public void removeHolidayDefinition(HolidayDefinition holidayDefinition) {
		if (null == holidayDefinition) {
			throw new IllegalArgumentException("holidayDefinition argument cannot be null");
		}
		this.holidayDefinitions.remove(holidayDefinition);
	}

	/**
	 * Determine if this calendar contains the specified holiday definition.
	 * 
	 * @param hd holiday definition to be checked
	 * @return <code>true</code> if this calendar contains the holiday
	 *         definition, <code>false</code> otherwise
	 */
	public boolean contains(HolidayDefinition hd) {
		return holidayDefinitions.contains(hd);
	}

	/**
	 * @return iterator over the holiday definitions in this financial calendar
	 */
	public Iterator<HolidayDefinition> iterator() {
		return holidayDefinitions.iterator();
	}

	/**
	 * @return number of holiday definitions in this calendar
	 */
	public int size() {
		return holidayDefinitions.size();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public FinancialCalendar toImmutable() {
		return new ImmutableFinancialCalendar(this.id, this.description, this.centralBank, this.holidayDefinitions);
	}

	public boolean isImmutable() {
		return (this instanceof ImmutableFinancialCalendar);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
                   .append(id)
                   .append(description)
                   .append(centralBank)
                   .append(holidayDefinitions)
                   .toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		boolean result;
		if (this == obj) {
			result = true;
		} else if (obj instanceof FinancialCalendar) {
			final FinancialCalendar other = (FinancialCalendar)obj;
			result = new EqualsBuilder()
			             .append(id, other.id)
			             .append(description, other.description)
			             .append(centralBank, other.centralBank)
			             .append(holidayDefinitions, other.holidayDefinitions)
			             .isEquals();
		} else {
			result = false;
		}
		return result;
	}

	/**
	 * Private immutable subclass of FinancialCalendar. This class is final; it
	 * is intended solely for instantiation by
	 * {@link FinancialCalendar#toImmutable()}.
	 *
	 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
	 */
	private final class ImmutableFinancialCalendar extends FinancialCalendar {
	
		/**
		 * Serializable UID.
		 */
		private static final long serialVersionUID = -7685175750981328587L;

		/**
		 * Cached hash value for this instance.
		 */
		private volatile transient int hashCode;

		/**
		 * Constructor. Accepts all fields as arguments, using the parent
		 * class's setters for initialization.
		 * 
		 * @param id
		 * @param description
		 * @param centralBank
		 * @param holidayDefinitions
		 */
		ImmutableFinancialCalendar(final String id,
				                   final String description,
				                   final CentralBank centralBank,
				                   final Set<HolidayDefinition> holidayDefinitions) {
			super();
			super.setId(id);
			super.setDescription(description);
			super.setCentralBank(centralBank);
			super.setHolidayDefinitions(holidayDefinitions);
		}

		/**
		 * Overridden method to prevent mutability of this instance.
		 * 
		 * @throws UnsupportedOperationException
		 */
		@Override
		public final void setId(String id) {
			throw new UnsupportedOperationException(DEFAULT_EXCEPTION_MSG);
		}

		/**
		 * Overridden method to prevent mutability of this instance.
		 * 
		 * @throws UnsupportedOperationException
		 */
		@Override
		public final void setDescription(String description) {
			throw new UnsupportedOperationException(DEFAULT_EXCEPTION_MSG);
		}

		/**
		 * Overridden method to prevent mutability of this instance.
		 * 
		 * @throws UnsupportedOperationException
		 */
		@Override
		public final void setCentralBank(CentralBank centralBank) {
			throw new UnsupportedOperationException(DEFAULT_EXCEPTION_MSG);
		}

		/**
		 * Overridden method to prevent mutability of this instance.
		 */
		@Override
		public final Set<HolidayDefinition> getHolidayDefinitions() {
			return Collections.unmodifiableSet(super.getHolidayDefinitions());
		}

		/**
		 * Overridden method to prevent mutability of this instance.
		 * 
		 * @throws UnsupportedOperationException
		 */
		@Override
		public final void setHolidayDefinitions(Set<HolidayDefinition> holidayDefinitions) {
			throw new UnsupportedOperationException(DEFAULT_EXCEPTION_MSG);
		}

		/**
		 * Overridden method to prevent mutability of this instance.
		 * 
		 * @throws UnsupportedOperationException
		 */
		@Override
		public final void addHolidayDefinition(HolidayDefinition holidayDefinition) {
			throw new UnsupportedOperationException(DEFAULT_EXCEPTION_MSG);
		}

		/**
		 * Overridden method to prevent mutability of this instance.
		 * 
		 * @throws UnsupportedOperationException
		 */
		@Override
		public final void removeHolidayDefinition(HolidayDefinition holidayDefinition) {
			throw new UnsupportedOperationException(DEFAULT_EXCEPTION_MSG);
		}

		/**
		 * @return unmodifiable iterator over the holiday definitions in this
		 *         financial calendar
		 */
		@Override
		public final Iterator<HolidayDefinition> iterator() {
			return this.getHolidayDefinitions().iterator();
		}

		/**
		 * This implementation simply returns <code>this</code>.
		 */
		@Override
		public final FinancialCalendar toImmutable() {
			return this;
		}

		@Override
		public int hashCode() {
			if (0 == hashCode) {
				hashCode = new HashCodeBuilder()
				               .append(id)
				               .append(description)
				               .append(centralBank)
				               .append(holidayDefinitions)
				               .toHashCode();
			}
			return hashCode;
		}
	}

}
