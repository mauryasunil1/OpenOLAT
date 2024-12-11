/**
 * <a href="http://www.openolat.org">
 * OpenOLAT - Online Learning and Training</a><br>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); <br>
 * you may not use this file except in compliance with the License.<br>
 * You may obtain a copy of the License at the
 * <a href="http://www.apache.org/licenses/LICENSE-2.0">Apache homepage</a>
 * <p>
 * Unless required by applicable law or agreed to in writing,<br>
 * software distributed under the License is distributed on an "AS IS" BASIS, <br>
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. <br>
 * See the License for the specific language governing permissions and <br>
 * limitations under the License.
 * <p>
 * Initial code contributed and copyrighted by<br>
 * frentix GmbH, http://www.frentix.com
 * <p>
 */
package org.olat.resource.accesscontrol;

import java.util.List;

import org.olat.modules.taxonomy.TaxonomyLevel;

public class CatalogInfo {

	public static final CatalogInfo UNSUPPORTED = new CatalogInfo(false, false, false, null, null, true, null, false, null, null, null, null, true);

	private final boolean catalogSupported;
	private final boolean webCatalogSupported;
	private final boolean showDetails;
	private final String detailsLabel;
	private final String details;
	private final boolean notAvailableEntry;
	private final String notAvailableStatus;
	private final boolean fullyBooked;
	private final String editBusinessPath;
	private final String editLabel;
	private final String catalogBusinessPath;
	private final List<TaxonomyLevel> microsites;
	private final boolean showQRCode;

	public CatalogInfo(boolean catalogSupported, boolean webCatalogSupported, boolean showDetails, String detailsLabel,
			String details, boolean notAvailableEntry, String notAvailableStatus, boolean fullyBooked,
			String editBusinessPath, String editLabel, String catalogBusinessPath, List<TaxonomyLevel> microsites, boolean showQRCode) {
		this.catalogSupported = catalogSupported;
		this.webCatalogSupported = webCatalogSupported;
		this.showDetails = showDetails;
		this.detailsLabel = detailsLabel;
		this.details = details;
		this.notAvailableEntry = notAvailableEntry;
		this.notAvailableStatus = notAvailableStatus;
		this.fullyBooked = fullyBooked;
		this.editBusinessPath = editBusinessPath;
		this.editLabel = editLabel;
		this.catalogBusinessPath = catalogBusinessPath;
		this.microsites = microsites;
		this.showQRCode = showQRCode;
	}

	public boolean isCatalogSupported() {
		return catalogSupported;
	}

	public boolean isWebCatalogSupported() {
		return webCatalogSupported;
	}

	public boolean isShowDetails() {
		return showDetails;
	}

	public String getDetailsLabel() {
		return detailsLabel;
	}

	public String getDetails() {
		return details;
	}

	public boolean isNotAvailableEntry() {
		return notAvailableEntry;
	}

	public String getNotAvailableStatus() {
		return notAvailableStatus;
	}

	public boolean isFullyBooked() {
		return fullyBooked;
	}

	public String getEditBusinessPath() {
		return editBusinessPath;
	}

	public String getEditLabel() {
		return editLabel;
	}

	public String getCatalogBusinessPath() {
		return catalogBusinessPath;
	}

	public List<TaxonomyLevel> getMicrosites() {
		return microsites;
	}

	public boolean isShowQRCode() {
		return showQRCode;
	}

}