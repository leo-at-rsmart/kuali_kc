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
package org.kuali.kra.iacuc.auth;

import java.util.ArrayList;
import java.util.List;

import org.kuali.kra.iacuc.actions.IacucProtocolActionType;
import org.kuali.kra.infrastructure.PermissionConstants;
import org.kuali.kra.protocol.ProtocolBase;
import org.kuali.kra.protocol.auth.AbandonProtocolAuthorizer;
import org.kuali.kra.protocol.auth.ProtocolTaskBase;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.util.GlobalVariables;

public class AbandonIacucProtocolAuthorizer extends IacucProtocolAuthorizer {

//   commented these out because they are not used in the authorizer
//    private static final List<String> APPROVE_ACTION_TYPES;
//    static {
//        final List<String> codes = new ArrayList<String>();     
//        codes.add(IacucProtocolActionType.IACUC_APPROVED);
//        // expedite approval
//        codes.add(IacucProtocolActionType.EXPEDITE_APPROVAL);
//        codes.add(IacucProtocolActionType.GRANT_EXEMPTION);
//        APPROVE_ACTION_TYPES = codes;
//    }

    /**
     * @see org.kuali.kra.protocol.auth.ProtocolAuthorizer#isAuthorized(java.lang.String, org.kuali.kra.protocol.auth.ProtocolTaskBase)
     */
    public boolean isAuthorized(String userId, IacucProtocolTask task) {
        //TODO : permission : PI and protocol has never been approved. protocol status is SRR/SMR
        return canExecuteAction(task.getProtocol(), IacucProtocolActionType.IACUC_ABANDON) 
            //&& isInitialProtocol(task.getProtocol())
            //&& isPrincipalInvestigator(task.getProtocol()) ;
            && (hasPermission(userId, task.getProtocol(), PermissionConstants.SUBMIT_IACUC_PROTOCOL)
                    || hasPermission(userId, task.getProtocol(), PermissionConstants.MODIFY_ANY_IACUC_PROTOCOL));
    }
    
    /*
     * commenting out, not being used for now
     */
//    private boolean isInitialProtocol(ProtocolBase protocol) {
//        boolean initialProtocol = true;
//        
//        /* -- commented as part of GENERATED CODE need to verify
//        for (ProtocolAction action : protocol.getProtocolActions()) {
//            if (APPROVE_ACTION_TYPES.contains(action.getProtocolActionTypeCode())) {
//                initialProtocol = false;
//                break;
//            }
//        }
//        */
//        return initialProtocol;
//    }
//    
    /*
     * commenting out, not being used for now
     */
//    private boolean isPrincipalInvestigator(ProtocolBase protocol) {
//        Person user = GlobalVariables.getUserSession().getPerson();
//        boolean isPi = false;
//        if (user.getPrincipalId().equals(protocol.getPrincipalInvestigator().getPersonId())) {
//            isPi = true;
//        }
//        return isPi;
//    }

}
