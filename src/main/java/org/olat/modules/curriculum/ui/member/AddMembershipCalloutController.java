/**
 * <a href="https://www.openolat.org">
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
 * frentix GmbH, https://www.frentix.com
 * <p>
 */
package org.olat.modules.curriculum.ui.member;

import java.util.Date;

import org.olat.basesecurity.GroupMembershipStatus;
import org.olat.core.gui.UserRequest;
import org.olat.core.gui.components.form.flexible.FormItem;
import org.olat.core.gui.components.form.flexible.FormItemContainer;
import org.olat.core.gui.components.form.flexible.elements.DateChooser;
import org.olat.core.gui.components.form.flexible.elements.SingleSelection;
import org.olat.core.gui.components.form.flexible.elements.TextElement;
import org.olat.core.gui.components.form.flexible.impl.FormBasicController;
import org.olat.core.gui.components.form.flexible.impl.FormEvent;
import org.olat.core.gui.components.form.flexible.impl.FormLayoutContainer;
import org.olat.core.gui.components.util.SelectionValues;
import org.olat.core.gui.control.Controller;
import org.olat.core.gui.control.Event;
import org.olat.core.gui.control.WindowControl;
import org.olat.core.id.Identity;
import org.olat.core.util.CodeHelper;
import org.olat.core.util.Util;
import org.olat.modules.curriculum.CurriculumElement;
import org.olat.modules.curriculum.CurriculumRoles;
import org.olat.modules.curriculum.ui.CurriculumManagerController;

/**
 * 
 * Initial date: 12 nov. 2024<br>
 * @author srosse, stephane.rosse@frentix.com, http://www.frentix.com
 *
 */
public class AddMembershipCalloutController extends FormBasicController {
	
	private TextElement adminNoteEl;
	private DateChooser confirmUntilEl;
	private SingleSelection applyToEl;
	private SingleSelection confirmationByEl;
	private SingleSelection confirmationTypeEl;
	
	private MembershipModification modification;
	
	private final Identity member;
	private final CurriculumRoles role;
	private final CurriculumElement curriculumElement;
	
	public AddMembershipCalloutController(UserRequest ureq, WindowControl wControl,
			Identity member, CurriculumRoles role, CurriculumElement curriculumElement) {
		super(ureq, wControl, LAYOUT_VERTICAL, Util
				.createPackageTranslator(CurriculumManagerController.class, ureq.getLocale()));
		this.role = role;
		this.member = member;
		this.curriculumElement = curriculumElement;
		initForm(ureq);
		updateUI();
	}
	
	public Identity getMember() {
		return member;
	}
	
	public MembershipModification getModification() {
		return modification;
	}

	@Override
	protected void initForm(FormItemContainer formLayout, Controller listener, UserRequest ureq) {
		String suffix = Long.toString(CodeHelper.getRAMUniqueID());
		SelectionValues confirmationPK = new SelectionValues();
		confirmationPK.add(SelectionValues.entry(ConfirmationMembershipEnum.WITH.name(), translate("confirmation.membership.with")));
		confirmationPK.add(SelectionValues.entry(ConfirmationMembershipEnum.WITHOUT.name(), translate("confirmation.membership.without")));
		confirmationTypeEl = uifactory.addCardSingleSelectVertical("confirmation.membership", formLayout,
				confirmationPK.keys(), confirmationPK.values(), null, null);
		confirmationTypeEl.addActionListener(FormEvent.ONCLICK);
		confirmationTypeEl.select(ConfirmationMembershipEnum.WITH.name(), true);
		
		// confirmation by
		SelectionValues confirmationByPK = new SelectionValues();
		confirmationByPK.add(SelectionValues.entry(ConfirmationByEnum.ADMINISTRATIVE_ROLE.name(), translate("confirmation.membership.by.admin")));
		confirmationByPK.add(SelectionValues.entry(ConfirmationByEnum.PARTICIPANT.name(), translate("confirmation.membership.by.participant")));
		confirmationByEl = uifactory.addRadiosVertical("confirmation.membership.by", formLayout,
				confirmationByPK.keys(), confirmationByPK.values());
		confirmationByEl.select(ConfirmationByEnum.ADMINISTRATIVE_ROLE.name(), true);
		
		// confirmation until
		confirmUntilEl = uifactory.addDateChooser("confirmation.until", "confirmation.until", null, formLayout);
		
		// apply to
		SelectionValues applyToPK = new SelectionValues();
		applyToPK.add(SelectionValues.entry(ChangeApplyToEnum.CONTAINED.name(), translate("apply.membership.to.contained")));
		applyToPK.add(SelectionValues.entry(ChangeApplyToEnum.CURRENT.name(), translate("apply.membership.to.current")));
		applyToEl = uifactory.addRadiosVertical("apply.membership.to." + suffix, "apply.membership.to", formLayout, applyToPK.keys(), applyToPK.values());
		applyToEl.select(ChangeApplyToEnum.CONTAINED.name(), true);
		
		adminNoteEl = uifactory.addTextAreaElement("admin.note." + suffix, "admin.note", 2000, 4, 32, false, false, false, "", formLayout);
		
		FormLayoutContainer buttonsCont = uifactory.addButtonsFormLayout("buttons", null, formLayout);
		uifactory.addFormSubmitButton("add", buttonsCont);
		uifactory.addFormCancelButton("cancel", buttonsCont, ureq, getWindowControl());
	}
	
	private void updateUI() {
		boolean withConfirmation = confirmationTypeEl.isOneSelected()
				&& ConfirmationMembershipEnum.WITH.name().equals(confirmationTypeEl.getSelectedKey());
		confirmationByEl.setVisible(withConfirmation);
		confirmUntilEl.setVisible(withConfirmation);
	}

	@Override
	protected void formInnerEvent(UserRequest ureq, FormItem source, FormEvent event) {
		if(confirmationTypeEl == source) {
			updateUI();
		}
		super.formInnerEvent(ureq, source, event);
	}

	@Override
	protected void formOK(UserRequest ureq) {
		String adminNote = adminNoteEl.getValue();
		ConfirmationMembershipEnum confirmation = ConfirmationMembershipEnum.valueOf(confirmationTypeEl.getSelectedKey());
		ConfirmationByEnum confirmationBy = confirmationByEl.isVisible()
				? ConfirmationByEnum.valueOf(confirmationByEl.getSelectedKey()) : null;
		Date confirmUntil = confirmUntilEl.isVisible() ? confirmUntilEl.getDate() : null;
		boolean applyToDescendants = applyToEl.isOneSelected() && ChangeApplyToEnum.CONTAINED.name().equals(applyToEl.getSelectedKey());
		GroupMembershipStatus nextStatus = (confirmation == ConfirmationMembershipEnum.WITH) ?
				GroupMembershipStatus.reservation : GroupMembershipStatus.active;
		
		modification = new MembershipModification(role, curriculumElement, nextStatus,
				confirmation, confirmationBy, confirmUntil, applyToDescendants, adminNote);
		
		fireEvent(ureq, Event.DONE_EVENT);
	}

	@Override
	protected void formCancelled(UserRequest ureq) {
		this.modification = null;
		fireEvent(ureq, Event.CANCELLED_EVENT);
	}
}
