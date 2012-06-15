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
package org.kuali.kra.iacuc.actions.approve;

import java.sql.Date;

import org.kuali.kra.iacuc.actions.IacucActionHelper;
import org.kuali.kra.iacuc.actions.genericactions.IacucProtocolGenericActionBean;
import org.kuali.kra.iacuc.actions.risklevel.IacucProtocolRiskLevelBean;
import org.kuali.kra.protocol.actions.approve.ProtocolApproveBean;
import org.kuali.kra.protocol.actions.risklevel.ProtocolRiskLevelBean;

public class IacucProtocolApproveBean extends IacucProtocolGenericActionBean implements ProtocolApproveBean {
    
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -8184965873953673608L;
    
    private Date approvalDate;
    private Date expirationDate;
    
    private String errorPropertyKey;
    private IacucProtocolRiskLevelBean protocolRiskLevelBean;
    
    /**
     * Constructs a ProtocolApproveBean.
     * @param actionHelper a reference back to the parent helper
     */
    public IacucProtocolApproveBean(IacucActionHelper actionHelper, String errorPropertyKey) {
        super(actionHelper, errorPropertyKey);
        
        this.errorPropertyKey = errorPropertyKey;
        protocolRiskLevelBean = new IacucProtocolRiskLevelBean(errorPropertyKey);
    }
    
    public Date getApprovalDate() {
        return approvalDate;
    }
    
    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }
    
    public Date getExpirationDate() {
        return expirationDate;
    }
    
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
    
    public String getErrorPropertyKey() {
        return errorPropertyKey;
    }
       
    public ProtocolRiskLevelBean getProtocolRiskLevelBean() {
        return protocolRiskLevelBean;
    }

}