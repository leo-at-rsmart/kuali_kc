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
package org.kuali.kra.protocol;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.kuali.kra.common.notification.web.struts.form.NotificationHelper;
import org.kuali.kra.common.permissions.web.struts.form.PermissionsForm;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.medusa.MedusaBean;
import org.kuali.kra.protocol.actions.ActionHelperBase;
import org.kuali.kra.protocol.customdata.ProtocolCustomDataHelperBase;
import org.kuali.kra.protocol.noteattachment.NotesAttachmentsHelperBase;
import org.kuali.kra.protocol.notification.ProtocolNotificationContextBase;
import org.kuali.kra.protocol.onlinereview.OnlineReviewsActionHelperBase;
import org.kuali.kra.protocol.permission.PermissionsHelperBase;
import org.kuali.kra.protocol.personnel.PersonnelHelperBase;
import org.kuali.kra.protocol.protocol.ProtocolHelperBase;
import org.kuali.kra.protocol.protocol.funding.ProtocolFundingSourceBase;
import org.kuali.kra.protocol.protocol.reference.ProtocolReferenceBeanBase;
import org.kuali.kra.protocol.questionnaire.QuestionnaireHelperBase;
import org.kuali.kra.protocol.specialreview.ProtocolSpecialReviewHelperBase;
import org.kuali.kra.questionnaire.QuestionableFormInterface;
import org.kuali.kra.service.KraAuthorizationService;
import org.kuali.kra.web.struts.form.Auditable;
import org.kuali.kra.web.struts.form.KraTransactionalDocumentFormBase;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kns.service.DataDictionaryService;
import org.kuali.rice.kns.web.ui.ExtraButton;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.KRADConstants;

/**
 * This class...
 * @author Kuali Nervous System Team (kualidev@oncourse.iu.edu)
 */
public abstract class ProtocolFormBase extends KraTransactionalDocumentFormBase implements PermissionsForm, Auditable, QuestionableFormInterface {
    
    private static final long serialVersionUID = 4646326030098259702L;
    
    private static final String DATE_FORMAT = "MM/dd/yyyy";
    private static final String DATE_TIME_FORMAT = "hh:mm a MM/dd/yyyy";
    
    
//    /**
//     * When true, the online review header will not be displayed when it is disabled.
//     */
    protected static final boolean HIDE_ONLINE_REVIEW_WHEN_DISABLED = true;
    protected static final String ONLINE_REVIEW_NAV_TO = "onlineReview";
    protected static final String CUSTOM_DATA_NAV_TO = "customData";
    
    
    private ProtocolHelperBase protocolHelper;
    private PermissionsHelperBase permissionsHelper;
    private PersonnelHelperBase personnelHelper;
    private ProtocolCustomDataHelperBase customDataHelper;
    private ProtocolSpecialReviewHelperBase specialReviewHelper;
    private ActionHelperBase actionHelper;    
    private OnlineReviewsActionHelperBase onlineReviewsActionHelper;
    private QuestionnaireHelperBase questionnaireHelper;    
    private NotificationHelper<ProtocolNotificationContextBase> protocolNotificationHelper;  
    //transient so that the helper and its members don't have to be serializable or transient
    //reinitialized in the getter
    private transient NotesAttachmentsHelperBase notesAttachmentsHelper;
    private boolean auditActivated;
    
    private ProtocolReferenceBeanBase newProtocolReferenceBean;
    private MedusaBean medusaBean;
    
    //KNS Lookup hooks
    private String lookupResultsSequenceNumber;
    private String lookupResultsBOClassName;
 
    private boolean javaScriptEnabled = true;
    
    private String detailId;
    // temp field : set in presave and then referenced in postsave
    private transient List<ProtocolFundingSourceBase> deletedProtocolFundingSources;
 
    
    public ProtocolFormBase() throws Exception {
        super();
        initialize();
        this.registerEditableProperty("methodToCall");
    }
    

    /**
     *
     * This method initialize all form variables
     * @throws Exception 
     */
    public void initialize() throws Exception {
        setProtocolHelper(createNewProtocolHelperInstanceHook(this));
        setPersonnelHelper(createNewPersonnelHelperInstanceHook(this));
        setPermissionsHelper(createNewPermissionsHelperInstanceHook(this));        
        setCustomDataHelper(createNewCustomDataHelperInstanceHook(this)); 
        setSpecialReviewHelper(createNewSpecialReviewHelperInstanceHook(this));
        setActionHelper(createNewActionHelperInstanceHook(this));
        setQuestionnaireHelper(createNewQuestionnaireHelperInstanceHook(this));        
        setNotesAttachmentsHelper(createNewNotesAttachmentsHelperInstanceHook(this));   
        this.notesAttachmentsHelper.prepareView();        
        setNewProtocolReferenceBean(createNewProtocolReferenceBeanInstance());
        setOnlineReviewsActionHelper(createNewOnlineReviewsActionHelperInstanceHook(this));
        setNotificationHelper(getNotificationHelperHook());
        setMedusaBean(new MedusaBean());
    }
       
  

    protected abstract NotificationHelper<? extends ProtocolNotificationContextBase> getNotificationHelperHook();
    
    protected abstract ProtocolReferenceBeanBase createNewProtocolReferenceBeanInstance();

    protected abstract QuestionnaireHelperBase createNewQuestionnaireHelperInstanceHook(ProtocolFormBase protocolForm);
    protected abstract ActionHelperBase createNewActionHelperInstanceHook(ProtocolFormBase protocolForm) throws Exception;
    protected abstract ProtocolSpecialReviewHelperBase createNewSpecialReviewHelperInstanceHook(ProtocolFormBase protocolForm);
    protected abstract ProtocolCustomDataHelperBase createNewCustomDataHelperInstanceHook(ProtocolFormBase protocolForm);
    protected abstract OnlineReviewsActionHelperBase createNewOnlineReviewsActionHelperInstanceHook(ProtocolFormBase protocolForm);
    protected abstract ProtocolHelperBase createNewProtocolHelperInstanceHook(ProtocolFormBase protocolForm);
    protected abstract PermissionsHelperBase createNewPermissionsHelperInstanceHook(ProtocolFormBase protocolForm);
    protected abstract PersonnelHelperBase createNewPersonnelHelperInstanceHook(ProtocolFormBase protocolForm);
    protected abstract QuestionnaireHelperBase createNewQuestionnaireHelper(ProtocolFormBase protocolForm);
    protected abstract NotesAttachmentsHelperBase createNewNotesAttachmentsHelperInstanceHook(ProtocolFormBase protocolForm);
    
    
    
    
// TODO *********uncomment the code below in increments as needed during refactoring*********     
//    /**
//     * @see org.kuali.rice.kns.web.struts.form.KualiForm#getHeaderNavigationTabs()
//     * 
//     * We only enable the Online Review tab if the protocol is in a state to be reviewed and
//     * the user has the IRB Admin role or the user has an Online Review. 
//     * 
//     * If HIDE_ONLINE_REVIEW_WHEN_DISABLED is true, then the tab will be removed from the tabs 
//     * List if it is disabled.
//     * 
//     */
//    @Override
//    public HeaderNavigation[] getHeaderNavigationTabs() {
//        
//        HeaderNavigation[] navigation = super.getHeaderNavigationTabs();
//        
//        ProtocolOnlineReviewService onlineReviewService = getProtocolOnlineReviewService();
//        List<HeaderNavigation> resultList = new ArrayList<HeaderNavigation>();
//        boolean onlineReviewTabEnabled = false;
//
//        if (getProtocolDocument() != null && getProtocolDocument().getProtocol() != null) {
//            String principalId = GlobalVariables.getUserSession().getPrincipalId();
//            ProtocolSubmissionBase submission = getProtocolDocument().getProtocol().getProtocolSubmission();
//            boolean isUserOnlineReviewer = onlineReviewService.isProtocolReviewer(principalId, false, submission);
//            boolean isUserIrbAdmin = getKraAuthorizationService().hasRole(GlobalVariables.getUserSession().getPrincipalId(), "KC-UNT", "IRB Administrator"); 
//            onlineReviewTabEnabled = (isUserOnlineReviewer || isUserIrbAdmin) 
//                    && onlineReviewService.isProtocolInStateToBeReviewed(getProtocolDocument().getProtocol());
//        }
//        
//            //We have to copy the HeaderNavigation elements into a new collection as the 
//            //List returned by DD is it's cached copy of the header navigation list.
//        for (HeaderNavigation nav : navigation) {
//            if (StringUtils.equals(nav.getHeaderTabNavigateTo(),ONLINE_REVIEW_NAV_TO)) {
//                nav.setDisabled(!onlineReviewTabEnabled);
//                if (onlineReviewTabEnabled || ((!onlineReviewTabEnabled) && (!HIDE_ONLINE_REVIEW_WHEN_DISABLED))) {
//                    resultList.add(nav);
//                }
////            } else if (StringUtils.equals(nav.getHeaderTabNavigateTo(),CUSTOM_DATA_NAV_TO)) {
////                boolean displayTab = this.getCustomDataHelper().canDisplayCustomDataTab();
////                nav.setDisabled(!displayTab);
////                if (displayTab) {
////                    resultList.add(nav);
////                }
//            } else {
//                resultList.add(nav);
//            }
//        }
//        
//        HeaderNavigation[] result = new HeaderNavigation[resultList.size()];
//        resultList.toArray(result);
//        return result;
//    }
    
    /**
     * 
     * This method is a wrapper method for getting DataDictionary Service using the Service Locator.
     * @return
     */
    protected DataDictionaryService getDataDictionaryService(){
        return (DataDictionaryService) KraServiceLocator.getService(Constants.DATA_DICTIONARY_SERVICE_NAME);
    }

// TODO *********commented the code below during IACUC refactoring********* 
//    /**
//     * 
//     * This method is a wrapper method for getting ProtocolOnlineReviewerService service.
//     * @return
//     */
//    protected ProtocolOnlineReviewService getProtocolOnlineReviewService() {
//        return KraServiceLocator.getService(ProtocolOnlineReviewService.class);
//    }
//
//    
//    
//    @Override
//    public void populate(HttpServletRequest request) { 
//        initAnswerList(request);
//        super.populate(request);
//        
//        // Temporary hack for KRACOEUS-489
//        if (getActionFormUtilMap() instanceof ActionFormUtilMap) {
//            ((ActionFormUtilMap) getActionFormUtilMap()).clear();
//        }
//    }
//    
//    /*
//     * For submission questionnaire, it is a popup and not a session document.
//     * so, it has to be retrieved, then populate with the new data.
//     */
//    private void initAnswerList(HttpServletRequest request) {
//        
//        String protocolNumber = request.getParameter("questionnaireHelper.protocolNumber");
//        String submissionNumber = request.getParameter("questionnaireHelper.submissionNumber");
//        if (StringUtils.isNotBlank(protocolNumber) && protocolNumber.endsWith("T")) {
//            ModuleQuestionnaireBean moduleQuestionnaireBean = new ModuleQuestionnaireBean(CoeusModule.IRB_MODULE_CODE, protocolNumber, CoeusSubModule.PROTOCOL_SUBMISSION, submissionNumber, false);
//            this.getQuestionnaireHelper().setAnswerHeaders(
//                    getQuestionnaireAnswerService().getQuestionnaireAnswer(moduleQuestionnaireBean));
//        }
//    }
//
//    private QuestionnaireAnswerService getQuestionnaireAnswerService() {
//        return KraServiceLocator.getService(QuestionnaireAnswerService.class);
//}

    @SuppressWarnings("deprecation")
    @Override
    public void populateHeaderFields(WorkflowDocument workflowDocument) {
        super.populateHeaderFields(workflowDocument);
    }
    
    protected String getFormattedDate(Date dateInput) {
        return CoreApiServiceLocator.getDateTimeService().toString(dateInput, DATE_FORMAT);        
    }

    protected String getFormattedDateTime(Timestamp dateTimeInput) {
        return CoreApiServiceLocator.getDateTimeService().toString(dateTimeInput, DATE_TIME_FORMAT);        
    }
    
    /* Reset method
     * @param mapping
     * @param request
     */
    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        this.setLookupResultsSequenceNumber(null);
        this.setLookupResultsBOClassName(null);
        
        onlineReviewsActionHelper.init(true);
    }
    
    public String getLookupResultsSequenceNumber() {
        return lookupResultsSequenceNumber;
    }

    public void setLookupResultsSequenceNumber(String lookupResultsSequenceNumber) {
        this.lookupResultsSequenceNumber = lookupResultsSequenceNumber;
    }
    
    public String getLookupResultsBOClassName() {
        return lookupResultsBOClassName;
    }

    public void setLookupResultsBOClassName(String lookupResultsBOClassName) {
        this.lookupResultsBOClassName = lookupResultsBOClassName;
    }
    

    public void setProtocolHelper(ProtocolHelperBase protocolHelper) {
        this.protocolHelper = protocolHelper;
    }

    public ProtocolHelperBase getProtocolHelper() {
        return protocolHelper;
    }
    
    private void setPersonnelHelper(PersonnelHelperBase personnelHelper) {
        this.personnelHelper = personnelHelper;
    }
    
    public PersonnelHelperBase getPersonnelHelper() {
        return personnelHelper;
    }
    
    private void setPermissionsHelper(PermissionsHelperBase permissionsHelper) {
        this.permissionsHelper = permissionsHelper;
    }
    
    public PermissionsHelperBase getPermissionsHelper() {
          return permissionsHelper;
    }
    
    
    public void setNewProtocolReferenceBean(ProtocolReferenceBeanBase newProtocolReferenceBean) {
        this.newProtocolReferenceBean = newProtocolReferenceBean;
    }

    public ProtocolReferenceBeanBase getNewProtocolReferenceBean() {
        return newProtocolReferenceBean;
    }
    
    @Override
    protected void setSaveDocumentControl(Map editMode) {
      
    }
   
    public ProtocolCustomDataHelperBase getCustomDataHelper() {
        return customDataHelper;
    }

    public void setCustomDataHelper(ProtocolCustomDataHelperBase customDataHelper) {
        this.customDataHelper = customDataHelper;
    }
    
    
    /** {@inheritDoc} */
    public boolean isAuditActivated() {
        return this.auditActivated;
    }

    /** {@inheritDoc} */
    public void setAuditActivated(boolean auditActivated) {
        this.auditActivated = auditActivated;
    }

    public ProtocolSpecialReviewHelperBase getSpecialReviewHelper() {
        return specialReviewHelper;
    }
    

    public void setSpecialReviewHelper(ProtocolSpecialReviewHelperBase specialReviewHelper) {
        this.specialReviewHelper = specialReviewHelper;
    }

    /**
     * Gets the Notes & Attachments Helper.
     * @return Notes & Attachments Helper
     */
    public NotesAttachmentsHelperBase getNotesAttachmentsHelper() {
        if (notesAttachmentsHelper == null) {
            notesAttachmentsHelper = createNewNotesAttachmentsHelperInstanceHook(this);
        }
        
        return notesAttachmentsHelper;
    }

    /**
     * Sets the Notes & Attachments Helper.
     * @param notesAttachmentsHelper the Notes & Attachments Helper
     */
    public void setNotesAttachmentsHelper(NotesAttachmentsHelperBase notesAttachmentsHelper) {
        this.notesAttachmentsHelper = notesAttachmentsHelper;
    }
    
    public ActionHelperBase getActionHelper() {
        return actionHelper;
    }
    
    protected void setActionHelper(ActionHelperBase actionHelper) {
        this.actionHelper = actionHelper;
    }


    public boolean isJavaScriptEnabled() {
        return javaScriptEnabled;
    }

    public void setJavaScriptEnabled(boolean javaScriptEnabled) {
        this.javaScriptEnabled = javaScriptEnabled;
    }

    
    public ProtocolDocumentBase getProtocolDocument() {
        return (ProtocolDocumentBase) getDocument();
    }

    public QuestionnaireHelperBase getQuestionnaireHelper() {
        return questionnaireHelper;
    }

    public void setQuestionnaireHelper(QuestionnaireHelperBase questionnaireHelper) {
        this.questionnaireHelper = questionnaireHelper;
    }

    public void setOnlineReviewsActionHelper(OnlineReviewsActionHelperBase onlineReviewActionHelper) {
        this.onlineReviewsActionHelper = onlineReviewActionHelper;
    }

    public OnlineReviewsActionHelperBase getOnlineReviewsActionHelper() {
        return onlineReviewsActionHelper;
    }

    public NotificationHelper<ProtocolNotificationContextBase> getNotificationHelper() {
        return protocolNotificationHelper;
    }

    public void setNotificationHelper(NotificationHelper notificationHelper) {
        this.protocolNotificationHelper = notificationHelper;
    }

   

// TODO *********commented the code below during IACUC refactoring*********     
//    @Override
//    public boolean isPropertyEditable(String propertyName) {
//        if (propertyName.startsWith("actionHelper.protocolSubmitAction.reviewer") ||
//                propertyName.startsWith("methodToCall.printSubmissionQuestionnaireAnswer.line")
//                || propertyName.startsWith("methodToCall.saveCorrespondence")
//                || propertyName.startsWith("methodToCall.closeCorrespondence")) {
//            return true;
//        } else {
//            return super.isPropertyEditable(propertyName);
//        }
//    }
    
    public KraAuthorizationService getKraAuthorizationService() {
        return KraServiceLocator.getService(KraAuthorizationService.class);
    }

// TODO ********************** commented out during IRB backfit ************************ PUSHED DOWN TO IRB    
//    /**
//     * 
//     * This method returns true if the risk level panel should be displayed.
//     * @return
//     */
//    public boolean getDisplayRiskLevelPanel() {
//        return true;
//// TODO *********commented the code below during IACUC refactoring*********        
////        return this.getProtocolDocument().getProtocol().getProtocolRiskLevels() != null 
////            && this.getProtocolDocument().getProtocol().getProtocolRiskLevels().size() > 0;
//        
//    }
    
    public List<ExtraButton> getExtraActionsButtons() {
        // clear out the extra buttons array
        extraButtons.clear();

        String externalImageURL = Constants.KR_EXTERNALIZABLE_IMAGES_URI_KEY;
        ConfigurationService configurationService = KRADServiceLocator.getKualiConfigurationService();
        
        boolean suppressRoutingControls = getActionHelper().getCanApproveFull() || !getActionHelper().getCanApproveOther();
        if (suppressRoutingControls && getDocumentActions().get(KRADConstants.KUALI_ACTION_CAN_SEND_ADHOC_REQUESTS) != null) {
            String sendAdHocRequestsImage = configurationService.getPropertyValueAsString(externalImageURL) + "buttonsmall_sendadhocreq.gif";
            addExtraButton("methodToCall.sendAdHocRequests", sendAdHocRequestsImage, "Send AdHoc Requests");
        }
        externalImageURL = Constants.KRA_EXTERNALIZABLE_IMAGES_URI_KEY;
        
        String sendNotificationImage = configurationService.getPropertyValueAsString(externalImageURL) + "buttonsmall_send_notification.gif";
        addExtraButton("methodToCall.sendNotification", sendNotificationImage, "Send Notification");
        
        return extraButtons;
    }
     
    public abstract String getModuleCode();
// TODO *********commented the code below during IACUC refactoring*********       
//    public String getModuleCode() {
//        return CoeusModule.IRB_MODULE_CODE;
//    }

      
    
    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public List<ProtocolFundingSourceBase> getDeletedProtocolFundingSources() {
        return deletedProtocolFundingSources;
    }

    public void setDeletedProtocolFundingSources(List<ProtocolFundingSourceBase> deletedProtocolFundingSources) {
        this.deletedProtocolFundingSources = deletedProtocolFundingSources;
    }
    
    public String getQuestionnaireFieldStarter() {
        return "questionnaireHelper.answerHeaders[";
    }
    
    public String getQuestionnaireFieldMiddle() {
        return DEFAULT_MIDDLE;
    }
    
    public String getQuestionnaireFieldEnd() {
        return DEFAULT_END;
    }

    public MedusaBean getMedusaBean() {
        return medusaBean;
    }


    public void setMedusaBean(MedusaBean medusaBean) {
        this.medusaBean = medusaBean;
    }
    
}
