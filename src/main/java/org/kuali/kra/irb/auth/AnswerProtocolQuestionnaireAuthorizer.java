/*
 * Copyright 2006-2008 The Kuali Foundation
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
package org.kuali.kra.irb.auth;

import org.kuali.kra.infrastructure.PermissionConstants;

/**
 * 
 * This class is to check authorization to answer protocol questionnaire.
 */
public class AnswerProtocolQuestionnaireAuthorizer extends ProtocolAuthorizer {

    /**
     * @see org.kuali.kra.irb.auth.ProtocolAuthorizer#isAuthorized(java.lang.String, org.kuali.kra.irb.auth.ProtocolTask)
     */
    public boolean isAuthorized(String userId, ProtocolTask task) {

        return !task.getProtocol().getProtocolDocument().isViewOnly()
                && !kraWorkflowService.isInWorkflow(task.getProtocol().getProtocolDocument())
                && hasPermission(userId, task.getProtocol(), PermissionConstants.ANSWER_PROTOCOL_QUESTIONNAIRE);
    }

}
