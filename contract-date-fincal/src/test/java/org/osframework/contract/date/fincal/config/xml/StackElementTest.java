package org.osframework.contract.date.fincal.config.xml;

import static org.testng.Assert.*;

import org.testng.annotations.Test;

public class StackElementTest {

	@Test
	public void testConstructorTypicalCase() {
		StackElement el = new StackElement("calendar", "NYB", null);
		assertEquals(el.getElementName(), "calendar");
		assertEquals(el.getElementId(), "NYB");
		assertNull(el.getElementRefId());
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testConstructorNullElementName() {
		new StackElement(null, "NYB", null);
		fail("Expected IllegalArgumentException to be thrown");
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testConstructorBlankElementName() {
		new StackElement("", "NYB", null);
		fail("Expected IllegalArgumentException to be thrown");
	}

}
