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
package org.kuali.kra.irb;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.kuali.kra.authorization.KraAuthorizationConstants;
import org.kuali.kra.bo.CoeusModule;
import org.kuali.kra.bo.CoeusSubModule;
import org.kuali.kra.common.notification.web.struts.form.NotificationHelper;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.irb.actions.ActionHelper;
import org.kuali.kra.irb.actions.ProtocolStatus;
import org.kuali.kra.irb.actions.submit.ProtocolSubmission;
import org.kuali.kra.irb.customdata.CustomDataHelper;
import org.kuali.kra.irb.noteattachment.NotesAttachmentsHelper;
import org.kuali.kra.irb.notification.IRBNotificationContext;
import org.kuali.kra.irb.onlinereview.OnlineReviewsActionHelper;
import org.kuali.kra.irb.onlinereview.ProtocolOnlineReviewService;
import org.kuali.kra.irb.permission.PermissionsHelper;
import org.kuali.kra.irb.personnel.PersonnelHelper;
import org.kuali.kra.irb.protocol.ProtocolHelper;
import org.kuali.kra.irb.protocol.reference.ProtocolReferenceBean;
import org.kuali.kra.irb.questionnaire.ProtocolModuleQuestionnaireBean;
import org.kuali.kra.irb.questionnaire.QuestionnaireHelper;
import org.kuali.kra.irb.specialreview.SpecialReviewHelper;
import org.kuali.kra.protocol.ProtocolFormBase;
import org.kuali.kra.protocol.actions.ActionHelperBase;
import org.kuali.kra.protocol.customdata.ProtocolCustomDataHelperBase;
import org.kuali.kra.protocol.noteattachment.NotesAttachmentsHelperBase;
import org.kuali.kra.protocol.notification.ProtocolNotificationContextBase;
import org.kuali.kra.protocol.onlinereview.OnlineReviewsActionHelperBase;
import org.kuali.kra.protocol.permission.PermissionsHelperBase;
import org.kuali.kra.protocol.personnel.PersonnelHelperBase;
import org.kuali.kra.protocol.protocol.ProtocolHelperBase;
import org.kuali.kra.protocol.protocol.reference.ProtocolReferenceBeanBase;
import org.kuali.kra.protocol.questionnaire.QuestionnaireHelperBase;
import org.kuali.kra.protocol.specialreview.ProtocolSpecialReviewHelperBase;
import org.kuali.kra.questionnaire.answer.ModuleQuestionnaireBean;
import org.kuali.kra.questionnaire.answer.QuestionnaireAnswerService;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kns.datadictionary.HeaderNavigation;
import org.kuali.rice.kns.util.ActionFormUtilMap;
import org.kuali.rice.kns.web.ui.HeaderField;
import org.kuali.rice.krad.util.GlobalVariables;

/**
 * This class...
 * @author Kuali Nervous System Team (kualidev@oncourse.iu.edu)
 */
public class ProtocolForm extends ProtocolFormBase {
    
    private static final long serialVersionUID = 3736148760520952504L;
    
    /**
     * When true, the online review header will not be displayed when it is disabled.
     */
    private static final boolean HIDE_ONLINE_REVIEW_WHEN_DISABLED = true;
    private static final String ONLINE_REVIEW_NAV_TO = "onlineReview";
    private static final String CUSTOM_DATA_NAV_TO = "customData";
    
// TODO ********************** commented out during IRB backfit ************************
//    private ProtocolHelper protocolHelper;
//    private PersonnelHelper personnelHelper;
//    private PermissionsHelper permissionsHelper;
//    private CustomDataHelper customDataHelper;
//    private SpecialReviewHelper specialReviewHelper;
//    private ActionHelper actionHelper;
//    private OnlineReviewsActionHelper onlineReviewsActionHelper;
//    private QuestionnaireHelper questionnaireHelper;
//    private NotificationHelper<IRBNotificationContext> notificationHelper;
//    private MedusaBean medusaBean;
//    //transient so that the helper and its members don't have to be serializable or transient
//    //reinitialized in the getter
//    private transient NotesAttachmentsHelper notesAttachmentsHelper;
//    private boolean auditActivated;
//    
//    private ProtocolReferenceBean newProtocolReferenceBean;
//    
//    //KNS Lookup hooks
//    private String lookupResultsSequenceNumber;
//    private String lookupResultsBOClassName;
//    
//    private boolean javaScriptEnabled = true;
//    
//    private String detailId;
//    // temp field : set in presave and then referenced in postsave
//    private transient List<ProtocolFundingSource> deletedProtocolFundingSources;
//    

    
    public ProtocolForm() throws Exception {
        super();
    }
  
    
    
    /** {@inheritDoc} */
    @Override
    protected String getDefaultDocumentTypeName() {
        return "ProtocolDocument";
    }

//    /**
//     *
//     * This method initialize all form variables
//     * @throws Exception 
//     */
//    public void initialize() throws Exception {
//        setProtocolHelper(new ProtocolHelper(this));        
//        setPersonnelHelper(new PersonnelHelper(this));
//        setPermissionsHelper(new PermissionsHelper(this));
//        setCustomDataHelper(new CustomDataHelper(this));
//        setSpecialReviewHelper(new SpecialReviewHelper(this));
//        setActionHelper(new ActionHelper(this));
//        setQuestionnaireHelper(new QuestionnaireHelper(this));
//        setNotesAttachmentsHelper(new NotesAttachmentsHelper(this));
//        this.notesAttachmentsHelper.prepareView();
//        setNewProtocolReferenceBean(new ProtocolReferenceBean());
//        setOnlineReviewsActionHelper(new OnlineReviewsActionHelper(this));
//        setNotificationHelper(new NotificationHelper<IRBNotificationContext>());
//        setMedusaBean(new MedusaBean());
//    }

    /**
     * @see org.kuali.rice.kns.web.struts.form.KualiForm#getHeaderNavigationTabs()
     * 
     * We only enable the Online Review tab if the protocol is in a state to be reviewed and
     * the user has the IRB Admin role or the user has an Online Review. 
     * 
     * If HIDE_ONLINE_REVIEW_WHEN_DISABLED is true, then the tab will be removed from the tabs 
     * List if it is disabled.
     * 
     */
    @SuppressWarnings("deprecation")
    @Override
    public HeaderNavigation[] getHeaderNavigationTabs() {
        
        HeaderNavigation[] navigation = super.getHeaderNavigationTabs();
        
        ProtocolOnlineReviewService onlineReviewService = getProtocolOnlineReviewService();
        List<HeaderNavigation> resultList = new ArrayList<HeaderNavigation>();
        boolean onlineReviewTabEnabled = false;

        if (getProtocolDocument() != null && getProtocolDocument().getProtocol() != null) {
            String principalId = GlobalVariables.getUserSession().getPrincipalId();
            ProtocolSubmission submission = (ProtocolSubmission) getProtocolDocument().getProtocol().getProtocolSubmission();
            boolean isUserOnlineReviewer = onlineReviewService.isProtocolReviewer(principalId, false, submission);
            boolean isUserIrbAdmin = getKraAuthorizationService().hasRole(GlobalVariables.getUserSession().getPrincipalId(), "KC-UNT", "IRB Administrator"); 
            onlineReviewTabEnabled = (isUserOnlineReviewer || isUserIrbAdmin) 
                    && onlineReviewService.isProtocolInStateToBeReviewed((Protocol) getProtocolDocument().getProtocol());
        }
        
            //We have to copy the HeaderNavigation elements into a new collection as the 
            //List returned by DD is it's cached copy of the header navigation list.
        for (HeaderNavigation nav : navigation) {
            if (StringUtils.equals(nav.getHeaderTabNavigateTo(),ONLINE_REVIEW_NAV_TO)) {
                nav.setDisabled(!onlineReviewTabEnabled);
                if (onlineReviewTabEnabled || ((!onlineReviewTabEnabled) && (!HIDE_ONLINE_REVIEW_WHEN_DISABLED))) {
                    resultList.add(nav);
                }
            } else if (StringUtils.equals(nav.getHeaderTabNavigateTo(),CUSTOM_DATA_NAV_TO)) {
                
// TODO *********TEMPORARILY COMMENTED OUT DURING IRB BACKFITTING*********                 
                boolean displayTab = this.getCustomDataHelper().canDisplayCustomDataTab();
                nav.setDisabled(!displayTab);
                if (displayTab) {
                    resultList.add(nav);
                }
            } else {
                resultList.add(nav);
            }
        }
        
        HeaderNavigation[] result = new HeaderNavigation[resultList.size()];
        resultList.toArray(result);
        return result;
    }
    

// TODO ********************** commented out during IRB backfit ************************    
//    /**
//     * 
//     * This method is a wrapper method for getting DataDictionary Service using the Service Locator.
//     * @return
//     */
//    protected DataDictionaryService getDataDictionaryService(){
//        return (DataDictionaryService) KraServiceLocator.getService(Constants.DATA_DICTIONARY_SERVICE_NAME);
//    }


    /**
     * 
     * This method is a wrapper method for getting ProtocolOnlineReviewerService service.
     * @return
     */
    protected ProtocolOnlineReviewService getProtocolOnlineReviewService() {
        return KraServiceLocator.getService(ProtocolOnlineReviewService.class);
    }

    
    
    @SuppressWarnings("deprecation")
    @Override
    public void populate(HttpServletRequest request) { 
        initAnswerList(request);
        super.populate(request);
        
        // Temporary hack for KRACOEUS-489
        if (getActionFormUtilMap() instanceof ActionFormUtilMap) {
            ((ActionFormUtilMap) getActionFormUtilMap()).clear();
        }
    }
    
    /*
     * For submission questionnaire, it is a popup and not a session document.
     * so, it has to be retrieved, then populate with the new data.
     */
    private void initAnswerList(HttpServletRequest request) {
        
        String protocolNumber = request.getParameter("questionnaireHelper.protocolNumber");
        String submissionNumber = request.getParameter("questionnaireHelper.submissionNumber");
        if (StringUtils.isNotBlank(protocolNumber) && protocolNumber.endsWith("T")) {
            ModuleQuestionnaireBean moduleQuestionnaireBean = new ProtocolModuleQuestionnaireBean(CoeusModule.IRB_MODULE_CODE, protocolNumber, CoeusSubModule.PROTOCOL_SUBMISSION, submissionNumber, false);
            this.getQuestionnaireHelper().setAnswerHeaders(
                    getQuestionnaireAnswerService().getQuestionnaireAnswer(moduleQuestionnaireBean));
        }
    }

    private QuestionnaireAnswerService getQuestionnaireAnswerService() {
        return KraServiceLocator.getService(QuestionnaireAnswerService.class);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void populateHeaderFields(WorkflowDocument workflowDocument) {
        super.populateHeaderFields(workflowDocument);
        ProtocolDocument pd = (ProtocolDocument) getProtocolDocument();
        
        HeaderField documentNumber = getDocInfo().get(0);
        documentNumber.setDdAttributeEntryName("DataDictionary.ProtocolDocument.attributes.documentNumber");
        
        ProtocolStatus protocolStatus = (ProtocolStatus) ((pd == null) ? null : pd.getProtocol().getProtocolStatus());
        HeaderField docStatus = new HeaderField("DataDictionary.AttributeReferenceDummy.attributes.workflowDocumentStatus", protocolStatus == null? "" : protocolStatus.getDescription());
        getDocInfo().set(1, docStatus);
        
        String lastUpdatedDateStr = null;
        if(pd != null && pd.getUpdateTimestamp() != null) {
            lastUpdatedDateStr = CoreApiServiceLocator.getDateTimeService().toString(pd.getUpdateTimestamp(), "hh:mm a MM/dd/yyyy");
        }
        
        if(getDocInfo().size() > 2) {
            HeaderField initiatorField = getDocInfo().get(2);
            String modifiedInitiatorFieldStr = initiatorField.getDisplayValue();
            if(StringUtils.isNotBlank(lastUpdatedDateStr)) {
                modifiedInitiatorFieldStr = modifiedInitiatorFieldStr + " : " + lastUpdatedDateStr;
            }
            getDocInfo().set(2, new HeaderField("DataDictionary.Protocol.attributes.initiatorLastUpdated", modifiedInitiatorFieldStr));
        }
        
        String protocolSubmissionStatusStr = null;
        if(pd != null && pd.getProtocol() != null && pd.getProtocol().getProtocolSubmission() != null) {
            pd.getProtocol().getProtocolSubmission().refreshReferenceObject("submissionStatus");
            protocolSubmissionStatusStr = pd.getProtocol().getProtocolSubmission().getSubmissionStatus().getDescription();
        }
        HeaderField protocolSubmissionStatus = new HeaderField("DataDictionary.Protocol.attributes.protocolSubmissionStatus", protocolSubmissionStatusStr);
        getDocInfo().set(3, protocolSubmissionStatus);
        
        getDocInfo().add(new HeaderField("DataDictionary.Protocol.attributes.protocolNumber", (pd == null) ? null : pd.getProtocol().getProtocolNumber()));

        String expirationDateStr = null;
        if(pd != null && pd.getProtocol().getExpirationDate() != null) {
            expirationDateStr = CoreApiServiceLocator.getDateTimeService().toString(pd.getProtocol().getExpirationDate(), "MM/dd/yyyy");
        }
        
        HeaderField expirationDate = new HeaderField("DataDictionary.Protocol.attributes.expirationDate", expirationDateStr);
        getDocInfo().add(expirationDate);
    }

// TODO ********************** commented out during IRB backfit ************************    
//    /* Reset method
//     * @param mapping
//     * @param request
//     */
//    @Override
//    public void reset(ActionMapping mapping, HttpServletRequest request) {
//        super.reset(mapping, request);
//        this.setLookupResultsSequenceNumber(null);
//        this.setLookupResultsBOClassName(null);
//        onlineReviewsActionHelper.init(true);
//    }
//    
//    public String getLookupResultsSequenceNumber() {
//        return lookupResultsSequenceNumber;
//    }
//
//    public void setLookupResultsSequenceNumber(String lookupResultsSequenceNumber) {
//        this.lookupResultsSequenceNumber = lookupResultsSequenceNumber;
//    }
//    
//    public String getLookupResultsBOClassName() {
//        return lookupResultsBOClassName;
//    }
//
//    public void setLookupResultsBOClassName(String lookupResultsBOClassName) {
//        this.lookupResultsBOClassName = lookupResultsBOClassName;
//    }
//    
//    public void setProtocolHelper(ProtocolHelper protocolHelper) {
//        this.protocolHelper = protocolHelper;
//    }

    public ProtocolHelper getProtocolHelper() {
        return (ProtocolHelper) super.getProtocolHelper();
    }
    
//    private void setPersonnelHelper(PersonnelHelper personnelHelper) {
//        this.personnelHelper = personnelHelper;
//    }
    
    public PersonnelHelper getPersonnelHelper() {
        return (PersonnelHelper) super.getPersonnelHelper();
    }
    
//    private void setPermissionsHelper(PermissionsHelper permissionsHelper) {
//        this.permissionsHelper = permissionsHelper;
//    }
    
    public PermissionsHelper getPermissionsHelper() {
        return (PermissionsHelper) super.getPermissionsHelper();
    }
    
//    public void setNewProtocolReferenceBean(ProtocolReferenceBean newProtocolReferenceBean) {
//        this.newProtocolReferenceBean = newProtocolReferenceBean;
//    }

    
    public ProtocolReferenceBean getNewProtocolReferenceBean() {
        return (ProtocolReferenceBean) super.getNewProtocolReferenceBean();
    }
    

//    @Override
//    protected void setSaveDocumentControl(Map editMode) {
//      
//    }

    
    
    
    // TODO this method has been removed from the refactored parent class code, let subclasses provide hook implementations
    @Override
    protected String getLockRegion() {
        return KraAuthorizationConstants.LOCK_DESCRIPTOR_PROTOCOL;
    }
    
    // TODO this method has been removed from the refactored parent class code, let subclasses provide hook implementations
    @Override
    public String getActionName() {
        return "protocol";
    }
 

//    public CustomDataHelper getCustomDataHelper() {
//        return customDataHelper;
//    }
//
//    public void setCustomDataHelper(CustomDataHelper customDataHelper) {
//        this.customDataHelper = customDataHelper;
//    }
//    
//    /** {@inheritDoc} */
//    public boolean isAuditActivated() {
//        return this.auditActivated;
//    }

//    /** {@inheritDoc} */
//    public void setAuditActivated(boolean auditActivated) {
//        this.auditActivated = auditActivated;
//    }
//
//    public SpecialReviewHelper getSpecialReviewHelper() {
//        return specialReviewHelper;
//    }
//
//    public void setSpecialReviewHelper(SpecialReviewHelper specialReviewHelper) {
//        this.specialReviewHelper = specialReviewHelper;
//    }
//
    /**
     * Gets the Notes & Attachments Helper.
     * @return Notes & Attachments Helper
     */
    public NotesAttachmentsHelper getNotesAttachmentsHelper() {
        return (NotesAttachmentsHelper) super.getNotesAttachmentsHelper();
    }

//    /**
//     * Sets the Notes & Attachments Helper.
//     * @param notesAttachmentsHelper the Notes & Attachments Helper
//     */
//    public void setNotesAttachmentsHelper(NotesAttachmentsHelper notesAttachmentsHelper) {
//        this.notesAttachmentsHelper = notesAttachmentsHelper;
//    }
    
    
    public ActionHelper getActionHelper() {
        return (ActionHelper) super.getActionHelper();
    }

// TODO ********************** commented out during IRB backfit ************************    
//    private void setActionHelper(ActionHelper actionHelper) {
//        this.actionHelper = actionHelper;
//    }
//
//    public boolean isJavaScriptEnabled() {
//        return javaScriptEnabled;
//    }
//
//    public void setJavaScriptEnabled(boolean javaScriptEnabled) {
//        this.javaScriptEnabled = javaScriptEnabled;
//    }
    

    public ProtocolDocument getProtocolDocument() {
        return (ProtocolDocument) super.getProtocolDocument();
    }

// TODO ********************** commented out during IRB backfit ************************    
//    public QuestionnaireHelper getQuestionnaireHelper() {
//        return questionnaireHelper;
//    }
//
//    public void setQuestionnaireHelper(QuestionnaireHelper questionnaireHelper) {
//        this.questionnaireHelper = questionnaireHelper;
//    }
//
//    public void setOnlineReviewsActionHelper(OnlineReviewsActionHelper onlineReviewActionHelper) {
//        this.onlineReviewsActionHelper = onlineReviewActionHelper;
//    }
//
//    public OnlineReviewsActionHelper getOnlineReviewsActionHelper() {
//        return onlineReviewsActionHelper;
//    }
//
//    public NotificationHelper<IRBNotificationContext> getNotificationHelper() {
//        return notificationHelper;
//    }
//
//    public void setNotificationHelper(NotificationHelper<IRBNotificationContext> notificationHelper) {
//        this.notificationHelper = notificationHelper;
//    }

    @Override
    public boolean isPropertyEditable(String propertyName) {
        if (propertyName.startsWith("actionHelper.protocolSubmitAction.reviewer") ||
                propertyName.startsWith("methodToCall.printSubmissionQuestionnaireAnswer.line")
                || propertyName.startsWith("methodToCall.saveCorrespondence")
                || propertyName.startsWith("methodToCall.closeCorrespondence")) {
            return true;
        } else {
            return super.isPropertyEditable(propertyName);
        }
    }
   
//    public KraAuthorizationService getKraAuthorizationService() {
//        return KraServiceLocator.getService(KraAuthorizationService.class);
//    }
    
    /**
     * 
     * This method returns true if the risk level panel should be displayed.
     * @return
     */
    public boolean getDisplayRiskLevelPanel() {
        return ((Protocol) this.getProtocolDocument().getProtocol()).getProtocolRiskLevels() != null 
            && ((Protocol) this.getProtocolDocument().getProtocol()).getProtocolRiskLevels().size() > 0;
        
    }
    
//    public List<ExtraButton> getExtraActionsButtons() {
//        // clear out the extra buttons array
//        extraButtons.clear();
//
//        String externalImageURL = Constants.KR_EXTERNALIZABLE_IMAGES_URI_KEY;
//        ConfigurationService configurationService = KRADServiceLocator.getKualiConfigurationService();
//        
//        boolean suppressRoutingControls = getActionHelper().getCanApproveFull() || !getActionHelper().getCanApproveOther();
//        if (suppressRoutingControls && getDocumentActions().get(KRADConstants.KUALI_ACTION_CAN_SEND_ADHOC_REQUESTS) != null) {
//            String sendAdHocRequestsImage = configurationService.getPropertyValueAsString(externalImageURL) + "buttonsmall_sendadhocreq.gif";
//            addExtraButton("methodToCall.sendAdHocRequests", sendAdHocRequestsImage, "Send AdHoc Requests");
//        }
//        externalImageURL = Constants.KRA_EXTERNALIZABLE_IMAGES_URI_KEY;
//        
//        String sendNotificationImage = configurationService.getPropertyValueAsString(externalImageURL) + "buttonsmall_send_notification.gif";
//        addExtraButton("methodToCall.sendNotification", sendNotificationImage, "Send Notification");
//        
//        return extraButtons;
//    }
     
    public String getModuleCode() {
        return CoeusModule.IRB_MODULE_CODE;
    }



    @Override
    protected NotificationHelper<? extends ProtocolNotificationContextBase> getNotificationHelperHook() {
        return new NotificationHelper<IRBNotificationContext>();
    }



    @Override
    protected ProtocolReferenceBeanBase createNewProtocolReferenceBeanInstance() {
        return new ProtocolReferenceBean();
    }



    @Override
    protected QuestionnaireHelperBase createNewQuestionnaireHelperInstanceHook(ProtocolFormBase protocolForm) {
        return new QuestionnaireHelper((ProtocolForm) protocolForm);
    }



    @Override
    protected ActionHelperBase createNewActionHelperInstanceHook(ProtocolFormBase protocolForm) throws Exception {
        return new ActionHelper((ProtocolForm) protocolForm);
    }



    @Override
    protected ProtocolSpecialReviewHelperBase createNewSpecialReviewHelperInstanceHook(ProtocolFormBase protocolForm) {
        return new SpecialReviewHelper((ProtocolForm) protocolForm);
    }



    @Override
    protected ProtocolCustomDataHelperBase createNewCustomDataHelperInstanceHook(ProtocolFormBase protocolForm) {
        return new CustomDataHelper((ProtocolForm) protocolForm);
    }



    @Override
    protected OnlineReviewsActionHelperBase createNewOnlineReviewsActionHelperInstanceHook(ProtocolFormBase protocolForm) {
        return new OnlineReviewsActionHelper((ProtocolForm) protocolForm);
    }



    @Override
    protected ProtocolHelperBase createNewProtocolHelperInstanceHook(ProtocolFormBase protocolForm) {
        return new ProtocolHelper((ProtocolForm) protocolForm);
    }



    @Override
    protected PermissionsHelperBase createNewPermissionsHelperInstanceHook(ProtocolFormBase protocolForm) {
        return new PermissionsHelper((ProtocolForm) protocolForm);
    }



    @Override
    protected PersonnelHelperBase createNewPersonnelHelperInstanceHook(ProtocolFormBase protocolForm) {
        return new PersonnelHelper((ProtocolForm) protocolForm);
    }



    @Override
    protected QuestionnaireHelperBase createNewQuestionnaireHelper(ProtocolFormBase protocolForm) {
        return new QuestionnaireHelper(protocolForm);
    }



    @Override
    protected NotesAttachmentsHelperBase createNewNotesAttachmentsHelperInstanceHook(ProtocolFormBase protocolForm) {
        return new NotesAttachmentsHelper((ProtocolForm) protocolForm);
    }
    

// TODO ********************** commented out during IRB backfit ************************    
//    public String getDetailId() {
//        return detailId;
//    }
//
//    public void setDetailId(String detailId) {
//        this.detailId = detailId;
//    }
//
//    public List<ProtocolFundingSource> getDeletedProtocolFundingSources() {
//        return deletedProtocolFundingSources;
//    }
//
//    public void setDeletedProtocolFundingSources(List<ProtocolFundingSource> deletedProtocolFundingSources) {
//        this.deletedProtocolFundingSources = deletedProtocolFundingSources;
//    }
//    
//    public String getQuestionnaireFieldStarter() {
//        return "questionnaireHelper.answerHeaders[";
//    }
//    
//    public String getQuestionnaireFieldMiddle() {
//        return DEFAULT_MIDDLE;
//    }
//    
//    public String getQuestionnaireFieldEnd() {
//        return DEFAULT_END;
//    }
//
//
//    public MedusaBean getMedusaBean() {
//        return medusaBean;
//    }
//
//
//    public void setMedusaBean(MedusaBean medusaBean) {
//        this.medusaBean = medusaBean;
//    }    
    
}
