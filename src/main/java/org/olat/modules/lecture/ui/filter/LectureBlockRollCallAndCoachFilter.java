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
package org.olat.modules.lecture.ui.filter;

import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

import org.olat.core.id.Identity;
import org.olat.core.id.User;
import org.olat.modules.lecture.LectureBlock;
import org.olat.modules.lecture.LectureBlockRollCall;
import org.olat.modules.lecture.model.LectureBlockRollCallAndCoach;
import org.olat.repository.RepositoryEntry;
import org.olat.user.propertyhandlers.UserPropertyHandler;

/**
 * 
 * Initial date: 1 juin 2021<br>
 * @author srosse, stephane.rosse@frentix.com, http://www.frentix.com
 *
 */
public class LectureBlockRollCallAndCoachFilter implements Predicate<LectureBlockRollCallAndCoach> {

	private final Locale locale;
	private final String searchString;
	private final List<UserPropertyHandler> userPropertyHandlers;
	
	public LectureBlockRollCallAndCoachFilter(String searchString,
			List<UserPropertyHandler> userPropertyHandlers, Locale locale) {
		this.locale = locale;
		this.searchString = searchString.toLowerCase();
		this.userPropertyHandlers = userPropertyHandlers;
	}

	@Override
	public boolean test(LectureBlockRollCallAndCoach row) {
		LectureBlock block = row.getLectureBlock();
		RepositoryEntry entry = row.getEntry();
		LectureBlockRollCall rollCall = row.getRollCall();
		Identity identity = rollCall == null ? null : rollCall.getIdentity();
		User user = identity == null ? null : identity.getUser();
		return (block != null && FilterHelper.test(block, searchString))
			|| FilterHelper.test(entry, searchString)
			|| FilterHelper.test(identity, searchString)
			|| FilterHelper.test(user, searchString, userPropertyHandlers, locale);
	}
}
