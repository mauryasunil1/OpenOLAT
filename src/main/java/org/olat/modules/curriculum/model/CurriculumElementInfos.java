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
package org.olat.modules.curriculum.model;

import org.olat.modules.curriculum.Curriculum;
import org.olat.modules.curriculum.CurriculumElement;
import org.olat.modules.curriculum.CurriculumElementRef;

/**
 * 
 * Initial date: 29 juin 2018<br>
 * @author srosse, stephane.rosse@frentix.com, http://www.frentix.com
 *
 */
public class CurriculumElementInfos implements CurriculumElementRef {
	
	private final CurriculumElement curriculumElement;
	private final Curriculum curriculum;
	private final long numOfResources;
	private final long numOfParticipants;
	private final long numOfCoaches;
	private final long numOfOwners;
	
	public CurriculumElementInfos(CurriculumElement curriculumElement, long numOfResources,
			long numOfParticipants, long numOfCoaches, long numOfOwners) {
		this.curriculumElement = curriculumElement;
		this.curriculum = curriculumElement.getCurriculum();
		this.numOfResources = numOfResources;
		this.numOfParticipants = numOfParticipants;
		this.numOfCoaches = numOfCoaches;
		this.numOfOwners = numOfOwners;
	}
	
	@Override
	public Long getKey() {
		return curriculumElement.getKey();
	}

	public CurriculumElement getCurriculumElement() {
		return curriculumElement;
	}
	
	public Curriculum getCurriculum() {
		return curriculum;
	}

	public long getNumOfResources() {
		return numOfResources;
	}

	public long getNumOfParticipants() {
		return numOfParticipants;
	}

	public long getNumOfCoaches() {
		return numOfCoaches;
	}

	public long getNumOfOwners() {
		return numOfOwners;
	}
}
