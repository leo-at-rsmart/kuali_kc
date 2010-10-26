/*
 * Copyright 2005-2010 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.kra.award.specialreview;

import org.apache.commons.lang.BooleanUtils;
import org.kuali.kra.award.AwardForm;
import org.kuali.kra.common.specialreview.web.struts.form.SpecialReviewHelperBase;
import org.kuali.rice.kns.authorization.AuthorizationConstants;

/**
 * Defines the Special Review Helper for Award.
 */
public class SpecialReviewHelper extends SpecialReviewHelperBase<AwardSpecialReview> {

    private static final long serialVersionUID = -3062893807666409229L;
    
    private AwardForm form;
    
    /**
     * Constructs a SpecialReviewHelper.
     * @param form the container form
     */
    public SpecialReviewHelper(AwardForm form) {
        this.form = form;
        setNewSpecialReview(new AwardSpecialReview());
    }

    @Override
    protected boolean hasModifySpecialReviewPermission(String principalId) {
        return BooleanUtils.toBoolean((String) form.getEditingMode().get(AuthorizationConstants.EditMode.FULL_ENTRY));
    }
    
}