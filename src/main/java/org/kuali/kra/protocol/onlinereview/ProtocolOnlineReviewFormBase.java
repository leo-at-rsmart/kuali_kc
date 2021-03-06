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
package org.kuali.kra.protocol.onlinereview;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.struts.action.ActionMapping;
import org.kuali.kra.authorization.KraAuthorizationConstants;
import org.kuali.kra.budget.document.BudgetDocument;
import org.kuali.kra.common.customattributes.CustomDataHelperBase;
import org.kuali.kra.common.permissions.web.struts.form.PermissionsForm;
import org.kuali.kra.common.permissions.web.struts.form.PermissionsHelperBase;
import org.kuali.kra.document.ResearchDocumentBase;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.infrastructure.PermissionConstants;
import org.kuali.kra.protocol.ProtocolDocumentBase;
import org.kuali.kra.protocol.ProtocolOnlineReviewDocumentBase;
import org.kuali.kra.protocol.onlinereview.authorization.ProtocolOnlineReviewTask;
import org.kuali.kra.service.KraAuthorizationService;
import org.kuali.kra.service.TaskAuthorizationService;
import org.kuali.kra.web.struts.form.Auditable;
import org.kuali.kra.web.struts.form.KraTransactionalDocumentFormBase;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kns.service.DataDictionaryService;
import org.kuali.rice.kns.web.ui.ExtraButton;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;

/**
 * This class...
 * @author Kuali Nervous System Team (kualidev@oncourse.iu.edu)
 */
public abstract class ProtocolOnlineReviewFormBase extends KraTransactionalDocumentFormBase implements PermissionsForm, Auditable  {
    
    private static final long serialVersionUID = -7633960906991275328L;
    
    ProtocolOnlineReviewDocumentBase document;
    
// TODO *********commented the code below during IACUC refactoring*********     
//    private static org.apache.commons.logging.Log LOG = org.apache.commons.logging.LogFactory.getLog(ProtocolOnlineReviewFormBase.class);

//    private static final Map<String,String> ONLINE_REVIEW_APPROVE_BUTTON_MAP;

// TODO *********commented the code below during IACUC refactoring*********     
//    static {
//        ONLINE_REVIEW_APPROVE_BUTTON_MAP = new HashMap<String,String>();
//        ONLINE_REVIEW_APPROVE_BUTTON_MAP.put(Constants.ONLINE_REVIEW_ROUTE_NODE_ADMIN_INITIAL_REVIEW, "buttonsmall_send_review_request.gif");
//        ONLINE_REVIEW_APPROVE_BUTTON_MAP.put(Constants.ONLINE_REVIEW_ROUTE_NODE_ADMIN_REVIEW,"buttonsmall_accept_review_comments.gif");
//        ONLINE_REVIEW_APPROVE_BUTTON_MAP.put(Constants.ONLINE_REVIEW_ROUTE_NODE_ONLINE_REVIEWER, "buttonsmall_approve_this_review.gif");
//    }
    
    private static final String DEFAULT_APPROVE_BUTTON = "buttonsmall_approve_this_review.gif";
    
    
    public ProtocolOnlineReviewFormBase() throws Exception {
        super();
        this.registerEditableProperty("methodToCall");
    }

    /**
     * Gets a {@link ProtocolDocumentBase ProtocolDocumentBase}.
     * @return {@link ProtocolDocumentBase ProtocolDocumentBase}
     */
    public ProtocolOnlineReviewDocumentBase getProtocolOnlineReviewDocument() {
        return (ProtocolOnlineReviewDocumentBase) super.getDocument();
    }

    
    protected abstract String getDefaultDocumentTypeName();
 
// TODO *********commented the code below during IACUC refactoring*********     
//    /** {@inheritDoc} */
//    @Override
//    protected String getDefaultDocumentTypeName() {
//        return "ProtocolOnlineReviewDocumentBase";
//    }
    
    
    
    /*
     * Override of the set document so we can populate this form
     * with doucment information outside of a request.
     */
    @Override
    public void setDocument(Document document) {
        super.setDocument(document);
        this.setDocTypeName(getDefaultDocumentTypeName());
    }
    
    /**
     * 
     * This method is a wrapper method for getting DataDictionary Service using the Service Locator.
     * @return
     */
    protected DataDictionaryService getDataDictionaryService(){
        return (DataDictionaryService) KraServiceLocator.getService(Constants.DATA_DICTIONARY_SERVICE_NAME);
    }

    @Override
    public void populate(HttpServletRequest request) {
        super.populate(request);
    }
    
    @Override
    public void populateHeaderFields(WorkflowDocument workflowDocument) {
        super.populateHeaderFields(workflowDocument);
    }

    /* Reset method
     * @param mapping
     * @param request
     */
    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
    }
    
    
    @Override
    protected void setSaveDocumentControl(Map editMode) {
      
    }
    
    
    protected abstract String getLockRegion();
    
// TODO *********commented the code below during IACUC refactoring*********     
//    @Override
//    protected String getLockRegion() {
//        return KraAuthorizationConstants.LOCK_DESCRIPTOR_PROTOCOL;
//    }
    
    @Override
    public String getActionName() {
        return "protocol";
    }

    /**
     * @see org.kuali.kra.web.struts.form.SpecialReviewFormBase#getResearchDocument()
     */
    public ResearchDocumentBase getResearchDocument() {
        return (ResearchDocumentBase) this.getDocument();
    }

    public PermissionsHelperBase getPermissionsHelper() {
        // TODO Auto-generated method stub
        return null;
    }

    public CustomDataHelperBase getCustomDataHelper() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isAuditActivated() {
        // TODO Auto-generated method stub
        return false;
    }

    public void setAuditActivated(boolean auditActivated) {
        // TODO Auto-generated method stub
        
    }


    public abstract List<ExtraButton> getExtraActionsButtons();
    
// TODO *********commented the code below during IACUC refactoring*********     
//    public List<ExtraButton> getExtraActionsButtons() {
//        // clear out the extra buttons array
//        extraButtons.clear();
//        ProtocolOnlineReviewDocumentBase doc = this.getProtocolOnlineReviewDocument();
//        String externalImageURL = Constants.KRA_EXTERNALIZABLE_IMAGES_URI_KEY;
//    
//        TaskAuthorizationService tas = KraServiceLocator.getService(TaskAuthorizationService.class);
//               
//        if( tas.isAuthorized(GlobalVariables.getUserSession().getPrincipalId(), new ProtocolOnlineReviewTask("rejectProtocolOnlineReview",doc))
//                && doc.getDocumentHeader().getWorkflowDocument().isEnroute()
//                && ProtocolOnlineReviewStatus.FINAL_STATUS_CD.equals(doc.getProtocolOnlineReview().getProtocolOnlineReviewStatusCode())) {
//            String resubmissionImage = KRADServiceLocator.getKualiConfigurationService().getPropertyValueAsString(externalImageURL) + "buttonsmall_return_to_reviewer.gif";
//            addExtraButton("methodToCall.rejectOnlineReview", resubmissionImage, "Return to reviewer");
//        }
//        
//        return extraButtons;
//    }
    

    /**
     * This is a utility method to add a new button to the extra buttons
     * collection.
     *   
     * @param property
     * @param source
     * @param altText
     */ 
    @SuppressWarnings("deprecation")
    protected void addExtraButton(String property, String source, String altText){
        
        ExtraButton newButton = new ExtraButton();
        
        newButton.setExtraButtonProperty(property);
        newButton.setExtraButtonSource(source);
        newButton.setExtraButtonAltText(altText);
        
        extraButtons.add(newButton);
    }

    @Override
    public List<ExtraButton> getExtraButtons() {
        return getExtraActionsButtons();
    }
    
    
    public abstract boolean getAdminFieldsEditable();

// TODO *********commented the code below during IACUC refactoring*********     
//    public boolean getAdminFieldsEditable() {
//        return KraServiceLocator.getService(KraAuthorizationService.class).hasPermission(GlobalVariables.getUserSession().getPrincipalId(), getProtocolOnlineReviewDocument().getProtocolOnlineReview().getProtocol(),PermissionConstants.MAINTAIN_IACUC_ONLINE_REVIEWS);
//    }
    
    public Set<String> getCurrentRouteNodes() {
        Set<String> nodes;
        try {
            nodes = getDocument().getDocumentHeader().getWorkflowDocument().getNodeNames();
        } catch (Exception e) {
            getLogHook().warn(String.format("Workflow exception thrown while trying to get list of current route nodes. Message:%s",e.getMessage()));
            nodes = new HashSet<String>();
        }
        return nodes;
    }

    protected abstract Log getLogHook();

    public String getApproveImageName() {
        //we take the first route node the document is on.
        Set<String> routeNodes = getCurrentRouteNodes();
        String routeNodeName = routeNodes.size() == 0? null : routeNodes.iterator().next();
        if (routeNodeName!=null) {
            return getOnlineReviewApproveButtonMapHook().get(routeNodeName)!=null? getOnlineReviewApproveButtonMapHook().get(routeNodeName):DEFAULT_APPROVE_BUTTON;
        } else {
            return DEFAULT_APPROVE_BUTTON;
        }
    }

    protected abstract Map<String, String> getOnlineReviewApproveButtonMapHook();
    
    
}
