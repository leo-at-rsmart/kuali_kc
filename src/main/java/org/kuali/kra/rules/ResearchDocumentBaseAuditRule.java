/*
 * Copyright 2005-2010 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.kra.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kuali.kra.bo.CustomAttribute;
import org.kuali.kra.bo.CustomAttributeDocument;
import org.kuali.kra.bo.DocumentCustomData;
import org.kuali.kra.document.ResearchDocumentBase;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.kns.util.AuditCluster;
import org.kuali.rice.kns.util.AuditError;
import org.kuali.rice.kns.util.KNSGlobalVariables;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.rules.rule.DocumentAuditRule;

/**
 * This class processes audit rules (warnings) for the Sponsor & Program Information related
 * data of the ProposalDevelopmenDocument.
 */
public class ResearchDocumentBaseAuditRule implements DocumentAuditRule {

    /**
     * @see org.kuali.core.rule.DocumentAuditRule#processRunAuditBusinessRules(org.kuali.rice.krad.document.Document)
     */
    public boolean processRunAuditBusinessRules(Document document) {
        boolean valid = true;

        if (document instanceof ResearchDocumentBase) {
            Map<String, CustomAttributeDocument> customAttributeDocuments = ((ResearchDocumentBase)document).getCustomAttributeDocuments();

            for (Map.Entry<String, CustomAttributeDocument> customAttributeDocumentEntry: customAttributeDocuments.entrySet()) {
                CustomAttributeDocument customAttributeDocument = customAttributeDocumentEntry.getValue();
                CustomAttribute customAttribute = customAttributeDocument.getCustomAttribute();
                List<? extends DocumentCustomData> customDataList = ((ResearchDocumentBase)document).getDocumentCustomData();
                int index = 0;
                for (DocumentCustomData data : customDataList) {
                    if (data.getCustomAttributeId() == customAttribute.getId().longValue()) {
                        customAttribute.setValue(data.getValue());
                        break;
                    }
                    index++;
                }
                if (customAttributeDocument.isRequired() && StringUtils.isEmpty(customAttribute.getValue())) {
                    valid = false;
                    String key = "CustomData" + StringUtils.deleteWhitespace(customAttribute.getGroupName()) + "Errors";
                    AuditCluster auditCluster = (AuditCluster) KNSGlobalVariables.getAuditErrorMap().get(key);
                    if (auditCluster == null) {
                        List<AuditError> auditErrors = new ArrayList<AuditError>();
                        //auditCluster = new AuditCluster("Custom Data: " + customAttribute.getGroupName(), auditErrors, Constants.AUDIT_ERRORS);
                        auditCluster = new AuditCluster(customAttribute.getGroupName(), auditErrors, Constants.AUDIT_ERRORS);
                        KNSGlobalVariables.getAuditErrorMap().put(key, auditCluster);
                    }
                    List<AuditError> auditErrors = auditCluster.getAuditErrorList();
                    auditErrors.add(new AuditError("customDataHelper.customDataList["+index+"].value", RiceKeyConstants.ERROR_REQUIRED, StringUtils.deleteWhitespace(Constants.CUSTOM_ATTRIBUTES_PAGE + "." + customAttribute.getGroupName()), new String[]{customAttribute.getLabel()}));
                }
            }

        }

        return valid;
    }

}
