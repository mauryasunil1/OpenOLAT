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
package org.olat.modules.catalog.filter;

import static org.olat.core.gui.components.util.SelectionValues.VALUE_ASC;
import static org.olat.core.gui.components.util.SelectionValues.entry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.olat.core.gui.UserRequest;
import org.olat.core.gui.components.form.flexible.elements.FlexiTableExtendedFilter;
import org.olat.core.gui.components.form.flexible.elements.FlexiTableFilter;
import org.olat.core.gui.components.form.flexible.impl.elements.table.filter.FlexiTableMultiSelectionFilter;
import org.olat.core.gui.components.util.SelectionValues;
import org.olat.core.gui.control.Controller;
import org.olat.core.gui.control.WindowControl;
import org.olat.core.gui.translator.Translator;
import org.olat.core.util.StringHelper;
import org.olat.modules.catalog.CatalogEntry;
import org.olat.modules.catalog.CatalogFilter;
import org.olat.modules.catalog.CatalogFilterHandler;
import org.olat.modules.catalog.ui.CatalogEntryRow;
import org.olat.modules.catalog.ui.admin.CatalogFilterTaxonomyLevelController;
import org.olat.modules.taxonomy.TaxonomyLevel;
import org.olat.modules.taxonomy.TaxonomyModule;
import org.olat.modules.taxonomy.TaxonomyService;
import org.olat.modules.taxonomy.manager.TaxonomyLevelDAO;
import org.olat.modules.taxonomy.ui.TaxonomyUIFactory;
import org.olat.repository.RepositoryModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * Initial date: 2 Jun 2022<br>
 * @author uhensler, urs.hensler@frentix.com, http://www.frentix.com
 *
 */
@Service
public class TaxonomyLevelFilterHandler implements CatalogFilterHandler {
	
	private static final String TYPE = "taxonomy.level";
	
	@Autowired
	private TaxonomyModule taxonomyModule;
	@Autowired
	private TaxonomyService taxonomyService;
	@Autowired
	private TaxonomyLevelDAO taxonomyLevelDao;
	@Autowired
	private RepositoryModule repositoryModule;

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public boolean isEnabled(boolean isGuestOnly) {
		return taxonomyModule.isEnabled() && !repositoryModule.getTaxonomyRefs().isEmpty();
	}

	@Override
	public int getSortOrder() {
		return 300;
	}

	@Override
	public String getTypeI18nKey() {
		return "filter.taxonomy.level.type";
	}

	@Override
	public String getAddI18nKey() {
		return "filter.taxonomy.level.add";
	}

	@Override
	public String getEditI18nKey() {
		return "filter.taxonomy.level.edit";
	}

	@Override
	public String getDetails(Translator translator, CatalogFilter catalogFilter) {
		String config = catalogFilter.getConfig();
		if (StringHelper.isLong(config)) {
			TaxonomyLevel taxonomyLevel = taxonomyService.getTaxonomyLevel(() -> Long.valueOf(config));
			if (taxonomyLevel != null) {
				return TaxonomyUIFactory.translateDisplayName(translator, taxonomyLevel);
			}
		}
		return "-";
	}

	@Override
	public boolean isMultiInstance() {
		return true;
	}

	@Override
	public Controller createEditController(UserRequest ureq, WindowControl wControl, CatalogFilter catalogFilter) {
		return new CatalogFilterTaxonomyLevelController(ureq, wControl, this, catalogFilter);
	}

	@Override
	public FlexiTableExtendedFilter createFlexiTableFilter(Translator translator, CatalogFilter catalogFilter,
			List<CatalogEntry> catalogEntries, TaxonomyLevel launcherTaxonomyLevel) {
		if (!StringHelper.isLong(catalogFilter.getConfig())) {
			return null;
		}
		
		// No filter if taxonomy level does not exist (anymore).
		Long configTaxonomyLevelKey = Long.valueOf(catalogFilter.getConfig());
		TaxonomyLevel taxonomyLevel = taxonomyService.getTaxonomyLevel(() -> configTaxonomyLevelKey);
		if (taxonomyLevel == null) {
			return null;
		}
		
		// No filter if taxonomy level if the same es the parent of the launcher taxonomy level
		Long launcherTaxonomyLevelKey = launcherTaxonomyLevel != null ? launcherTaxonomyLevel.getKey() : null;
		Long launcherParentTaxonomyLevelKey = null;
		if (launcherTaxonomyLevelKey != null) {
			TaxonomyLevel launcherParentTaxonomyLevel = taxonomyService.getTaxonomyLevel(() -> launcherTaxonomyLevelKey);
			if (launcherParentTaxonomyLevel != null && launcherParentTaxonomyLevel.getParent() != null) {
				launcherParentTaxonomyLevelKey = launcherParentTaxonomyLevel.getParent().getKey();
			}
		}
		if (Objects.equals(configTaxonomyLevelKey, launcherParentTaxonomyLevelKey)) {
			return null;
		}
		
		List<TaxonomyLevel> descendants = taxonomyLevelDao.getDescendants(taxonomyLevel, taxonomyLevel.getTaxonomy());
		if (launcherTaxonomyLevelKey != null) {
			descendants.removeIf(level -> launcherTaxonomyLevelKey.equals(level.getKey()));
		}
		if (descendants.isEmpty()) {
			return null;
		}
		
		SelectionValues taxonomyValues = getTaxonomyLevelsSV(translator, taxonomyLevel, descendants);
		FlexiTableMultiSelectionFilter flexiTableFilter = new FlexiTableMultiSelectionFilter(
				TaxonomyUIFactory.translateDisplayName(translator, taxonomyLevel), TYPE, taxonomyValues,
				catalogFilter.isDefaultVisible());
		return flexiTableFilter;
	}
	
	private SelectionValues getTaxonomyLevelsSV(Translator translator, TaxonomyLevel taxonomyLevel, List<TaxonomyLevel> descendants) {
		SelectionValues keyValues = new SelectionValues();
		for (TaxonomyLevel level: descendants) {
			List<String> names = new ArrayList<>();
			addParentNames(translator, names, level, taxonomyLevel);
			Collections.reverse(names);
			String value = String.join(" / ", names);
			keyValues.add(entry(level.getMaterializedPathKeys(), value));
		}
		keyValues.sort(VALUE_ASC);
		return keyValues;
	}
	
	private void addParentNames(Translator translator, List<String> names, TaxonomyLevel level, TaxonomyLevel topLevel) {
		names.add(StringHelper.escapeHtml(TaxonomyUIFactory.translateDisplayName(translator, level)));
		TaxonomyLevel parent = level.getParent();
		if (parent != null && !parent.equals(topLevel)) {
			addParentNames(translator, names, parent, topLevel);
		}
	}
	
	@Override
	public void filter(FlexiTableFilter flexiTableFilter, List<CatalogEntryRow> rows) {
		List<String> taxonomyLevelPathKeys = ((FlexiTableMultiSelectionFilter)flexiTableFilter).getValues();
		if (taxonomyLevelPathKeys != null && !taxonomyLevelPathKeys.isEmpty()) {
			rows.removeIf(row -> !isMatch(row, taxonomyLevelPathKeys));
		}
	}
	
	// Show entry if any taxonomy level of the entry is a at least a sublevel of the selected level
	private boolean isMatch(CatalogEntryRow row, List<String> taxonomyLevelPathKeys) {
		if (row.getTaxonomyLevels() != null && !row.getTaxonomyLevels().isEmpty()) {
			for (TaxonomyLevel taxonomyLevel : row.getTaxonomyLevels()) {
				for (String taxonomyLevelPathKey : taxonomyLevelPathKeys) {
					if (taxonomyLevel.getMaterializedPathKeys().startsWith(taxonomyLevelPathKey)) {
						return true;
					}
				}
				
			}
		}
		return false;
	}
	
}
