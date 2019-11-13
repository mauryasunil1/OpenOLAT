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
package org.olat.modules.assessment.ui;

import java.util.Locale;

import org.olat.course.run.scoring.AssessmentEvaluation;

/**
 * 
 * Initial date: 12 Nov 2019<br>
 * @author uhensler, urs.hensler@frentix.com, http://www.frentix.com
 *
 */
public class LearningProgressRenderer extends AbstractLearningProgressRenderer {
	
	public LearningProgressRenderer(Locale locale) {
		super(locale);
	}

	@Override
	protected AssessmentEvaluation getAssessmentEvaluation(Object cellValue) {
		if (cellValue instanceof AssessmentEvaluation) {
			return (AssessmentEvaluation)cellValue;
		}
		return null;
	}
	
	@Override
	protected float getActual(Object cellValue) {
		float actual = 0.0f;
		if (cellValue instanceof AssessmentEvaluation) {
			AssessmentEvaluation assessmentEvaluation = (AssessmentEvaluation)cellValue;
			Double completion = assessmentEvaluation.getCompletion();
			if (completion != null) {
				actual = completion.floatValue();
			}
		}
		return actual;
	}
	
}
