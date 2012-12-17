/**
 * 
 */
package org.osframework.contract.date;

/**
 * @author kzk55j
 *
 */
public interface ProjectInfoReader {

	public String getImplementationTitle();

	public String getImplementationVersion();

	public String getImplementationVendor();

	static final String KEY_IMPLEMENTATION_TITLE = "Implementation-Title";

	static final String KEY_IMPLEMENTATION_VERSION = "Implementation-Version";

	static final String KEY_IMPLEMENTATION_VENDOR = "Implementation-Vendor";

}
