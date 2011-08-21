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
package org.kuali.kra.coi;

import java.util.Map;

import org.kuali.kra.authorization.KraAuthorizationConstants;
import org.kuali.kra.web.struts.form.Auditable;
import org.kuali.kra.web.struts.form.KraTransactionalDocumentFormBase;

public class CoiDisclosureForm extends KraTransactionalDocumentFormBase implements Auditable  {

    @Override
    protected String getDefaultDocumentTypeName() {
        // TODO Auto-generated method stub
        return "CoiDisclosureDocument";
    }

    @Override
    protected String getLockRegion() {
        return KraAuthorizationConstants.LOCK_DESCRIPTOR_COIDISCLOSURE;
    }

    @Override
    protected void setSaveDocumentControl(Map editMode) {
        // TODO Auto-generated method stub
        
    }

    public boolean isAuditActivated() {
        // TODO Auto-generated method stub
        return false;
    }

    public void setAuditActivated(boolean auditActivated) {
        // TODO Auto-generated method stub
        
    }

}
