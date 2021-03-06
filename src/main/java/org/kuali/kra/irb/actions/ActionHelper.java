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
package org.kuali.kra.irb.actions;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.kra.bo.CoeusModule;
import org.kuali.kra.bo.CoeusSubModule;
import org.kuali.kra.committee.service.CommitteeScheduleService;
import org.kuali.kra.committee.service.CommitteeService;
import org.kuali.kra.common.committee.service.CommitteeScheduleServiceBase;
import org.kuali.kra.common.committee.service.CommitteeServiceBase;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.infrastructure.RoleConstants;
import org.kuali.kra.infrastructure.TaskName;
import org.kuali.kra.irb.Protocol;
import org.kuali.kra.irb.ProtocolDocument;
import org.kuali.kra.irb.ProtocolForm;
import org.kuali.kra.irb.ProtocolVersionService;
import org.kuali.kra.irb.actions.amendrenew.ProtocolAmendRenewService;
import org.kuali.kra.irb.actions.amendrenew.ProtocolAmendRenewal;
import org.kuali.kra.irb.actions.amendrenew.ProtocolAmendmentBean;
import org.kuali.kra.irb.actions.amendrenew.ProtocolModule;
import org.kuali.kra.irb.actions.approve.ProtocolApproveBean;
import org.kuali.kra.irb.actions.assignagenda.ProtocolAssignToAgendaBean;
import org.kuali.kra.irb.actions.assigncmtsched.ProtocolAssignCmtSchedBean;
import org.kuali.kra.irb.actions.assignreviewers.ProtocolAssignReviewersBean;
import org.kuali.kra.irb.actions.correction.AdminCorrectionBean;
import org.kuali.kra.irb.actions.decision.CommitteeDecision;
import org.kuali.kra.irb.actions.decision.CommitteeDecisionService;
import org.kuali.kra.irb.actions.delete.ProtocolDeleteBean;
import org.kuali.kra.irb.actions.expeditedapprove.ProtocolExpeditedApproveBean;
import org.kuali.kra.irb.actions.followup.FollowupActionService;
import org.kuali.kra.irb.actions.genericactions.ProtocolGenericActionBean;
import org.kuali.kra.irb.actions.grantexemption.ProtocolGrantExemptionBean;
import org.kuali.kra.irb.actions.modifysubmission.ProtocolModifySubmissionBean;
import org.kuali.kra.irb.actions.noreview.ProtocolReviewNotRequiredBean;
import org.kuali.kra.irb.actions.notifycommittee.ProtocolNotifyCommitteeBean;
// import org.kuali.kra.irb.actions.notifyirb.ProtocolActionAttachment;
import org.kuali.kra.irb.actions.notifyirb.ProtocolNotifyIrbBean;
import org.kuali.kra.irb.actions.print.ProtocolQuestionnairePrintingService;
import org.kuali.kra.irb.actions.request.ProtocolRequestBean;
import org.kuali.kra.irb.actions.reviewcomments.ReviewCommentsService;
import org.kuali.kra.irb.actions.submit.ProtocolActionService;
import org.kuali.kra.irb.actions.submit.ProtocolSubmissionType;
import org.kuali.kra.irb.actions.submit.ProtocolSubmitAction;
import org.kuali.kra.irb.actions.submit.ProtocolSubmitActionService;
import org.kuali.kra.irb.actions.undo.UndoLastActionBean;
import org.kuali.kra.irb.actions.withdraw.ProtocolAdministrativelyIncompleteBean;
import org.kuali.kra.irb.actions.withdraw.ProtocolAdministrativelyWithdrawBean;
import org.kuali.kra.irb.actions.withdraw.ProtocolWithdrawBean;
import org.kuali.kra.irb.auth.GenericProtocolAuthorizer;
import org.kuali.kra.irb.auth.ProtocolTask;
import org.kuali.kra.irb.questionnaire.ProtocolModuleQuestionnaireBean;
import org.kuali.kra.meeting.CommitteeScheduleMinute;
import org.kuali.kra.protocol.ProtocolBase;
import org.kuali.kra.protocol.ProtocolDocumentBase;
import org.kuali.kra.protocol.actions.ActionHelperBase;
import org.kuali.kra.protocol.actions.ProtocolActionBase;
import org.kuali.kra.protocol.actions.ProtocolEditableBean;
import org.kuali.kra.protocol.actions.ProtocolSubmissionDocBase;
import org.kuali.kra.protocol.actions.amendrenew.ProtocolAmendRenewModuleBase;
import org.kuali.kra.protocol.actions.amendrenew.ProtocolAmendRenewalBase;
import org.kuali.kra.protocol.actions.notify.ProtocolActionAttachment;
import org.kuali.kra.protocol.auth.ProtocolTaskBase;
import org.kuali.kra.protocol.questionnaire.ProtocolModuleQuestionnaireBeanBase;
import org.kuali.kra.questionnaire.answer.ModuleQuestionnaireBean;
import org.kuali.kra.questionnaire.answer.QuestionnaireAnswerService;
import org.kuali.kra.rules.ErrorReporter;
import org.kuali.kra.service.TaskAuthorizationService;
import org.kuali.kra.util.DateUtils;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;

/**
 * The form helper class for the Protocol Actions tab.
 */
@SuppressWarnings("serial")
public class ActionHelper extends ActionHelperBase {

    private static final long ONE_DAY = 1000L * 60L * 60L * 24L;
    private static final String NAMESPACE = "KC-UNT";
    private static final List<String> ACTION_TYPE_SUBMISSION_DOC;
    static {
        final List<String> codes = new ArrayList<String>();     
        codes.add(ProtocolActionType.NOTIFY_IRB);
        codes.add(ProtocolActionType.REQUEST_TO_CLOSE);
        codes.add(ProtocolActionType.REQUEST_FOR_DATA_ANALYSIS_ONLY);
        codes.add(ProtocolActionType.REQUEST_FOR_SUSPENSION);
        codes.add(ProtocolActionType.REQUEST_TO_REOPEN_ENROLLMENT);
        codes.add(ProtocolActionType.REQUEST_FOR_TERMINATION);
        codes.add(ProtocolActionType.REQUEST_TO_CLOSE_ENROLLMENT);
        ACTION_TYPE_SUBMISSION_DOC = codes;
    }

    /**
     * Each Helper must contain a reference to its document form
     * so that it can access the document.
     */
    
// TODO ********************** commented out during IRB backfit ************************    
//    private ProtocolForm form;
//    
//    private boolean canSubmitProtocol = false;
//    private boolean canSubmitProtocolUnavailable = false;
//    private String submissionConstraint;
//    
//    private boolean canCreateAmendment = false;
//    private boolean canCreateAmendmentUnavailable = false;
//    private boolean canModifyAmendmentSections = false;
//    private boolean canModifyAmendmentSectionsUnavailable = false;
//    private boolean canCreateRenewal = false;
//    private boolean canCreateRenewalUnavailable = false;
    
    private boolean canNotifyIrb = false;
    private boolean canNotifyIrbUnavailable = false;
    
//    private boolean canNotifyCommittee = false;
//    private boolean canNotifyCommitteeUnavailable = false;
//    private boolean canWithdraw = false;
//    private boolean canWithdrawUnavailable = false;
//    private boolean canRequestClose = false;
//    private boolean canRequestCloseUnavailable = false;
//    private boolean canRequestSuspension = false;
//    private boolean canRequestSuspensionUnavailable = false;
    
    private boolean canRequestCloseEnrollment = false;
    private boolean canRequestCloseEnrollmentUnavailable = false;
    private boolean canRequestReOpenEnrollment = false;
    private boolean canRequestReOpenEnrollmentUnavailable = false;
    private boolean canRequestDataAnalysis = false;
    private boolean canRequestDataAnalysisUnavailable = false;
   
// TODO ********************** commented out during IRB backfit ************************    
//    private boolean canRequestTerminate = false;
//    private boolean canRequestTerminateUnavailable = false;
//    private boolean canDeleteProtocolAmendRenew = false;
//    private boolean canDeleteProtocolAmendRenewUnavailable = false;
//    private boolean canAssignToAgenda = false;
//    private boolean canAssignToAgendaUnavailable = false;
//    private boolean canAssignCmtSched = false;
//    private boolean canAssignCmtSchedUnavailable = false;
//    private boolean canAssignReviewers = false;
//    private boolean canAssignReviewersCmtSel = false;
//    private boolean canAssignReviewersUnavailable = false;
    
    private boolean canGrantExemption = false;
    private boolean canGrantExemptionUnavailable = false;
    
// TODO ********************** commented out during IRB backfit ************************    
//    private boolean canApproveFull = false;
//    private boolean canApproveFullUnavailable = false;
    
    private boolean canApproveExpedited = false;    
    private boolean canApproveExpeditedUnavailable = false;
    
// TODO ********************** commented out during IRB backfit ************************    
//    private boolean canApproveResponse = false;
//    private boolean canApproveResponseUnavailable = false;
//    private boolean canDisapprove = false;
//    private boolean canDisapproveUnavailable = false;
//    private boolean canReturnForSMR = false;
//    private boolean canReturnForSMRUnavailable = false;
//    private boolean canReturnForSRR = false;
//    private boolean canReturnForSRRUnavailable = false;
    
    private boolean canReopenEnrollment = false;
    private boolean canReopenEnrollmentUnavailable = false;
    private boolean canCloseEnrollment = false;
    private boolean canCloseEnrollmentUnavailable = false;
    
// TODO ********************** commented out during IRB backfit ************************    
//    private boolean canSuspend = false;
//    private boolean canSuspendUnavailable = false;
    
    
    private boolean canSuspendByDsmb = false;
    private boolean canSuspendByDsmbUnavailable = false;
    
// TODO ********************** commented out during IRB backfit ************************    
//    private boolean canClose = false;
//    private boolean canCloseUnavailable = false;
//    private boolean canExpire = false;
//    private boolean canExpireUnavailable = false;
//    private boolean canTerminate = false;
//    private boolean canTerminateUnavailable = false;
    
    private boolean canPermitDataAnalysis = false;
    private boolean canPermitDataAnalysisUnavailable = false;
    
    
    private boolean canEnterRiskLevel = false;
    
// TODO ********************** commented out during IRB backfit ************************    
//    private boolean canMakeAdminCorrection = false;
//    private boolean canMakeAdminCorrectionUnavailable = false;
//    private boolean canRecordCommitteeDecision = false;
//    private boolean canRecordCommitteeDecisionUnavailable = false;
//    private boolean canUndoLastAction = false;
//    private boolean canUndoLastActionUnavailable = false;
//    private boolean canModifyProtocolSubmission = false;
//    private boolean canModifyProtocolSubmissionUnavailable = false;
    
    private boolean canIrbAcknowledgement = false;
    private boolean canIrbAcknowledgementUnavailable = false;
    private boolean canDefer = false;
    private boolean canDeferUnavailable = false;
    private boolean canReviewNotRequired = false;
    private boolean canReviewNotRequiredUnavailable = false;
    
// TODO ********************** commented out during IRB backfit ************************    
//    private boolean canManageReviewComments = false;
//    private boolean canManageReviewCommentsUnavailable = false;
//    private boolean canApproveOther = false;
//    private boolean canManageNotes = false;
//    private boolean canManageNotesUnavailable = false;
//    private boolean canAbandon = false;
//    private boolean canReturnToPI = false;
//    private boolean canReturnToPIUnavailable = false;

//    private boolean isApproveOpenForFollowup;
//    private boolean isDisapproveOpenForFollowup;
//    private boolean isReturnForSMROpenForFollowup;
//    private boolean isReturnForSRROpenForFollowup;
    
//    private List<ValidProtocolActionAction> followupActionActions;
    
//    private boolean canViewOnlineReviewers;
//    private boolean canViewOnlineReviewerComments;
//    
//    private boolean canAddCloseReviewerComments;
    
    private boolean canAddCloseEnrollmentReviewerComments;
    private boolean canAddDataAnalysisReviewerComments;
    private boolean canAddReopenEnrollmentReviewerComments;
    
    
// TODO ********************** commented out during IRB backfit ************************    
//    private boolean canAddSuspendReviewerComments;
//    private boolean canAddTerminateReviewerComments;
    
//    private ProtocolSubmitAction protocolSubmitAction;
//    private ProtocolWithdrawBean protocolWithdrawBean;
//    private ProtocolRequestBean protocolCloseRequestBean;
//    private ProtocolRequestBean protocolSuspendRequestBean;
    
    private ProtocolRequestBean protocolCloseEnrollmentRequestBean;
    private ProtocolRequestBean protocolReOpenEnrollmentRequestBean;
    private ProtocolRequestBean protocolDataAnalysisRequestBean;
    
// TODO ********************** commented out during IRB backfit ************************    
//    private ProtocolRequestBean protocolTerminateRequestBean;
    
    private ProtocolNotifyIrbBean protocolNotifyIrbBean;
    
// TODO ********************** commented out during IRB backfit ************************    
//    private ProtocolNotifyCommitteeBean protocolNotifyCommitteeBean;
//    private ProtocolAmendmentBean protocolAmendmentBean;
//    private ProtocolAmendmentBean protocolRenewAmendmentBean;
//    private ProtocolDeleteBean protocolDeleteBean;
//    private ProtocolAssignToAgendaBean assignToAgendaBean;
    
    
    private ProtocolAssignCmtSchedBean assignCmtSchedBean;
    
    private ProtocolAssignReviewersBean protocolAssignReviewersBean;
    private ProtocolGrantExemptionBean protocolGrantExemptionBean;
    
// TODO ********************** commented out during IRB backfit ************************    
//    private ProtocolApproveBean protocolFullApprovalBean;
    
    private ProtocolExpeditedApproveBean protocolExpeditedApprovalBean;
    private ProtocolApproveBean protocolResponseApprovalBean;
    
//    private ProtocolGenericActionBean protocolDisapproveBean;
//    private ProtocolGenericActionBean protocolSMRBean;
//    private ProtocolGenericActionBean protocolSRRBean;
    
    private ProtocolGenericActionBean protocolReopenEnrollmentBean;
    private ProtocolGenericActionBean protocolCloseEnrollmentBean;
    
// TODO ********************** commented out during IRB backfit ************************    
//    private ProtocolGenericActionBean protocolSuspendBean;
    
    private ProtocolGenericActionBean protocolSuspendByDsmbBean;
    
// TODO ********************** commented out during IRB backfit ************************    
//    private ProtocolGenericActionBean protocolCloseBean;
//    private ProtocolGenericActionBean protocolExpireBean;
//    private ProtocolGenericActionBean protocolTerminateBean;
    
    private ProtocolGenericActionBean protocolPermitDataAnalysisBean;
    private ProtocolGenericActionBean protocolIrbAcknowledgementBean;
    
// TODO ********************** commented out during IRB backfit ************************    
//    private AdminCorrectionBean protocolAdminCorrectionBean;
    
    private UndoLastActionBean undoLastActionBean;
    
// TODO ********************** commented out during IRB backfit ************************    
//    private CommitteeDecision committeeDecision;
    
    private ProtocolModifySubmissionBean protocolModifySubmissionBean;    
    private ProtocolGenericActionBean protocolDeferBean;
    
    private ProtocolReviewNotRequiredBean protocolReviewNotRequiredBean;

// TODO ********************** commented out during IRB backfit ************************    
//    private ProtocolGenericActionBean protocolManageReviewCommentsBean;
//    private ProtocolGenericActionBean protocolAbandonBean;
//    private ProtocolGenericActionBean protocolReturnToPIBean;
//    private String currentTaskName = "";
//    private boolean prevDisabled;
//    private boolean nextDisabled;
//    private transient ParameterService parameterService;
//    private transient TaskAuthorizationService taskAuthorizationService;
//    private transient ProtocolAmendRenewService protocolAmendRenewService;
//    private transient ProtocolVersionService protocolVersionService;
    
    private transient ProtocolSubmitActionService protocolSubmitActionService;
    private transient ProtocolActionService protocolActionService;
    
// TODO ********************** commented out during IRB backfit ************************    
//    private boolean hasAmendments;
//    private boolean hasRenewals;
//    private boolean submissionHasNoAmendmentDetails;
    
    /*
     * Identifies the protocol "document" to print.
     */
    private String printTag;
  
// TODO ********************** commented out during IRB backfit ************************    
//    private ProtocolSummaryPrintOptions protocolSummaryPrintOptions;
//
//    private Boolean summaryReport;
//    private Boolean fullReport;
//    private Boolean historyReport;
//    private Boolean reviewCommentsReport;
//    
//    private ProtocolSummary protocolSummary;
//    private ProtocolSummary prevProtocolSummary;
//    private int currentSequenceNumber = -1;
//    
//    private String selectedHistoryItem;
//    private Date filteredHistoryStartDate;
//    private Date filteredHistoryEndDate;
//    
//    // additional properties for Submission Details
//    private ProtocolSubmission selectedSubmission;
//    private List<CommitteeScheduleMinute> reviewComments;        
//    private List<ProtocolReviewAttachment> reviewAttachments;        
//    private List<ProtocolVoteAbstainee> abstainees;        
//    private List<ProtocolVoteRecused> recusers;        
//    private List<ProtocolReviewer> protocolReviewers;        
//    private int currentSubmissionNumber;
//    private String renewalSummary;
    
    // indicator for whether there is submission questionnaire answer exist.
    // ie, questionnaire has been saved for a request/notify irb action
    private boolean submissionQuestionnaireExist;
    // check if there is submission questionnaire to answer
    private boolean toAnswerSubmissionQuestionnaire;
    
// TODO ********************** commented out during IRB backfit ************************
//    private transient CommitteeScheduleService committeeScheduleService;
//    private transient KcPersonService kcPersonService;
//    private transient KraAuthorizationService kraAuthorizationService;
//    private transient BusinessObjectService businessObjectService;
//    private transient FollowupActionService followupActionService;
//    private Map<String, ProtocolRequestBean>  actionTypeRequestBeanMap = new HashMap<String, ProtocolRequestBean>();
//    private Map<String,Boolean> followupActionMap = new HashMap<String,Boolean>();
//    
//    private Map<String, ProtocolActionBean> actionBeanTaskMap = new HashMap<String, ProtocolActionBean>();    
//    // protocol print
//    private ProtocolSummaryPrintOptions protocolPrintOption = new ProtocolSummaryPrintOptions();
//    private List<QuestionnairePrintOption> questionnairesToPrints;
//    
//    
//    // flag if versioned protocol questionnaire exist
//    private boolean summaryQuestionnaireExist;
//    private boolean hideReviewerName;
//    private boolean hideSubmissionReviewerName;
//    private boolean hideReviewerNameForAttachment;
//    private ProtocolCorrespondence protocolCorrespondence;

    /**
     * Constructs an ActionHelper.
     * @param form the protocol form
     * @throws Exception 
     */
    public ActionHelper(ProtocolForm form) throws Exception {
        super(form);
        
// TODO ********************** commented out during IRB backfit ************************        
//        protocolSubmitAction = new ProtocolSubmitAction(this);
//        protocolWithdrawBean = new ProtocolWithdrawBean(this);
        
        
        protocolNotifyIrbBean = new ProtocolNotifyIrbBean(this);
        // setting the attachment here so new files can be attached to newActionAttachment
        protocolNotifyIrbBean.setNewActionAttachment(new ProtocolActionAttachment());
        
// TODO ********************** commented out during IRB backfit ************************        
//        protocolNotifyCommitteeBean = new ProtocolNotifyCommitteeBean(this);
//        protocolAmendmentBean = createAmendmentBean();
//        protocolRenewAmendmentBean = createAmendmentBean();
//        
//        protocolDeleteBean = new ProtocolDeleteBean(this);
//        assignToAgendaBean = new ProtocolAssignToAgendaBean(this);
//        assignToAgendaBean.getReviewCommentsBean().setReviewComments(getCopiedReviewComments());
        
        assignCmtSchedBean = new ProtocolAssignCmtSchedBean(this);
        assignCmtSchedBean.init();
        protocolAssignReviewersBean = new ProtocolAssignReviewersBean(this);
        protocolGrantExemptionBean = new ProtocolGrantExemptionBean(this);
        protocolGrantExemptionBean.getReviewCommentsBean().setReviewComments(getCopiedReviewComments());
        
// TODO ********************** commented out during IRB backfit ************************        
//        protocolFullApprovalBean = buildProtocolApproveBean(ProtocolActionType.APPROVED, 
//                Constants.PROTOCOL_FULL_APPROVAL_ACTION_PROPERTY_KEY);
        
        protocolExpeditedApprovalBean = buildProtocolExpeditedApproveBean(ProtocolActionType.EXPEDITE_APPROVAL);
        protocolResponseApprovalBean = (ProtocolApproveBean) buildProtocolApproveBean(ProtocolActionType.RESPONSE_APPROVAL, 
                Constants.PROTOCOL_RESPONSE_APPROVAL_ACTION_PROPERTY_KEY);
        
// TODO ********************** commented out during IRB backfit ************************        
//        protocolDisapproveBean = buildProtocolGenericActionBean(ProtocolActionType.DISAPPROVED, 
//                Constants.PROTOCOL_DISAPPROVE_ACTION_PROPERTY_KEY);
//        protocolSMRBean = buildProtocolGenericActionBean(ProtocolActionType.SPECIFIC_MINOR_REVISIONS_REQUIRED, 
//                Constants.PROTOCOL_SMR_ACTION_PROPERTY_KEY);
//        protocolSRRBean = buildProtocolGenericActionBean(ProtocolActionType.SUBSTANTIVE_REVISIONS_REQUIRED, 
//                Constants.PROTOCOL_SRR_ACTION_PROPERTY_KEY);
        
        protocolReopenEnrollmentBean = buildProtocolGenericActionBean(ProtocolActionType.REOPEN_ENROLLMENT, 
                Constants.PROTOCOL_REOPEN_ENROLLMENT_ACTION_PROPERTY_KEY);
        protocolCloseEnrollmentBean = buildProtocolGenericActionBean(ProtocolActionType.CLOSED_FOR_ENROLLMENT, 
                Constants.PROTOCOL_CLOSE_ENROLLMENT_ACTION_PROPERTY_KEY);
        
// TODO ********************** commented out during IRB backfit ************************        
//        protocolSuspendBean = buildProtocolGenericActionBean(ProtocolActionType.SUSPENDED, 
//                Constants.PROTOCOL_SUSPEND_ACTION_PROPERTY_KEY);
        
        protocolSuspendByDsmbBean = buildProtocolGenericActionBean(ProtocolActionType.SUSPENDED_BY_DSMB, 
                Constants.PROTOCOL_SUSPEND_BY_DSMB_ACTION_PROPERTY_KEY);
        protocolCloseBean = buildProtocolGenericActionBean(ProtocolActionType.CLOSED_ADMINISTRATIVELY_CLOSED, 
                Constants.PROTOCOL_CLOSE_ACTION_PROPERTY_KEY);
        
// TODO ********************** commented out during IRB backfit ************************        
//        protocolExpireBean = buildProtocolGenericActionBean(ProtocolActionType.EXPIRED, 
//                Constants.PROTOCOL_EXPIRE_ACTION_PROPERTY_KEY);
//        protocolTerminateBean = buildProtocolGenericActionBean(ProtocolActionType.TERMINATED, 
//                Constants.PROTOCOL_TERMINATE_ACTION_PROPERTY_KEY);
        
        protocolPermitDataAnalysisBean = buildProtocolGenericActionBean(ProtocolActionType.DATA_ANALYSIS_ONLY, 
                Constants.PROTOCOL_PERMIT_DATA_ANALYSIS_ACTION_PROPERTY_KEY);
        protocolIrbAcknowledgementBean = buildProtocolGenericActionBean(ProtocolActionType.IRB_ACKNOWLEDGEMENT, 
                Constants.PROTOCOL_IRB_ACKNOWLEDGEMENT_ACTION_PROPERTY_KEY);
  
// TODO ********************** commented out during IRB backfit ************************        
//        protocolAbandonBean = buildProtocolGenericActionBean(ProtocolActionType.ABANDON_PROTOCOL, 
//                Constants.PROTOCOL_ABANDON_ACTION_PROPERTY_KEY);
//                
//        protocolReturnToPIBean = buildProtocolGenericActionBean(ProtocolActionType.RETURNED_TO_PI,
//                Constants.PROTOCOL_RETURN_TO_PI_PROPERTY_KEY);
//        
//        protocolAdminCorrectionBean = createAdminCorrectionBean();
        
        undoLastActionBean = createUndoLastActionBean((Protocol) getProtocol());
        
// TODO ********************** commented out during IRB backfit ************************        
//        committeeDecision = new CommitteeDecision(this);
//        committeeDecision.init();
//        committeeDecision.getReviewCommentsBean().setReviewComments(getCopiedReviewComments());
        
//        committeeDecision.getReviewCommentsBean().setHideReviewerName(getReviewCommentsService().setHideReviewerName(committeeDecision.getReviewCommentsBean().getReviewComments()));
        
        protocolModifySubmissionBean = new ProtocolModifySubmissionBean(this);
        protocolDeferBean = buildProtocolGenericActionBean(ProtocolActionType.DEFERRED, 
                Constants.PROTOCOL_DEFER_ACTION_PROPERTY_KEY);
        protocolReviewNotRequiredBean = new ProtocolReviewNotRequiredBean(this);
        
// TODO ********************** commented out during IRB backfit ************************        
//        protocolManageReviewCommentsBean = buildProtocolGenericActionBean(ProtocolActionType.MANAGE_REVIEW_COMMENTS, 
//                Constants.PROTOCOL_MANAGE_REVIEW_COMMENTS_KEY);
//        getProtocol().getProtocolSubmission().refreshReferenceObject("reviewAttachments");
//        protocolManageReviewCommentsBean.getReviewAttachmentsBean().setReviewAttachments(getProtocol().getProtocolSubmission().getReviewAttachments());
//        if (CollectionUtils.isNotEmpty(protocolManageReviewCommentsBean.getReviewAttachmentsBean().getReviewAttachments())) {
//            protocolManageReviewCommentsBean.getReviewAttachmentsBean().setHideReviewerName(getReviewerCommentsService().setHideReviewerName(protocolManageReviewCommentsBean.getReviewAttachmentsBean().getReviewAttachments()));
//        }
        
        protocolCloseRequestBean = new ProtocolRequestBean(this, ProtocolActionType.REQUEST_TO_CLOSE, 
                ProtocolSubmissionType.REQUEST_TO_CLOSE, "protocolCloseRequestBean");
        protocolSuspendRequestBean = new ProtocolRequestBean(this, ProtocolActionType.REQUEST_FOR_SUSPENSION, 
                ProtocolSubmissionType.REQUEST_FOR_SUSPENSION, "protocolSuspendRequestBean");
        protocolCloseEnrollmentRequestBean = new ProtocolRequestBean(this, ProtocolActionType.REQUEST_TO_CLOSE_ENROLLMENT, 
                ProtocolSubmissionType.REQUEST_TO_CLOSE_ENROLLMENT, "protocolCloseEnrollmentRequestBean");
        protocolReOpenEnrollmentRequestBean = new ProtocolRequestBean(this, ProtocolActionType.REQUEST_TO_REOPEN_ENROLLMENT,
                ProtocolSubmissionType.REQUEST_TO_REOPEN_ENROLLMENT, "protocolReOpenEnrollmentRequestBean");
        protocolDataAnalysisRequestBean = new ProtocolRequestBean(this, ProtocolActionType.REQUEST_FOR_DATA_ANALYSIS_ONLY,
                ProtocolSubmissionType.REQUEST_FOR_DATA_ANALYSIS_ONLY, "protocolDataAnalysisRequestBean");
        protocolTerminateRequestBean = new ProtocolRequestBean(this, ProtocolActionType.REQUEST_FOR_TERMINATION,
                ProtocolSubmissionType.REQUEST_FOR_TERMINATION, "protocolTerminateRequestBean");

// TODO ********************** commented out during IRB backfit ************************        
//        initActionBeanTaskMap();
//         
//        protocolSummaryPrintOptions = new ProtocolSummaryPrintOptions();
        
        toAnswerSubmissionQuestionnaire = hasSubmissionQuestionnaire();
        
//        protocolPrintOption = new ProtocolSummaryPrintOptions();
//        initPrintQuestionnaire();
        
        
// TODO ********************** newly added during IRB backfit ************************        
        initIRBSpecificActionBeanTaskMap();
    }
    
    /**
     * Initializes the mapping between the task names and the beans.  This is used to get the bean associated to the task name passed in from the tag file.
     * The reason TaskName (a text code) is used and ProtocolActionType (a number code) is not is because not every task is mapped to a ProtocolActionType.
     */
 // TODO ********************** method name changed during IRB backfit ************************    
    private void initIRBSpecificActionBeanTaskMap() {
    
// TODO ********************** commented out during IRB backfit ************************        
//        actionBeanTaskMap.put(TaskName.PROTOCOL_ADMIN_CORRECTION, protocolAdminCorrectionBean);
//        actionBeanTaskMap.put(TaskName.CREATE_PROTOCOL_AMMENDMENT, protocolAmendmentBean);
//        actionBeanTaskMap.put(TaskName.CREATE_PROTOCOL_RENEWAL, protocolRenewAmendmentBean);
//        actionBeanTaskMap.put(TaskName.APPROVE_PROTOCOL, protocolFullApprovalBean);
        
        actionBeanTaskMap.put(TaskName.ASSIGN_TO_COMMITTEE_SCHEDULE, assignCmtSchedBean);
        actionBeanTaskMap.put(TaskName.ASSIGN_REVIEWERS, protocolAssignReviewersBean);
  
// TODO ********************** commented out during IRB backfit ************************        
//        actionBeanTaskMap.put(TaskName.ASSIGN_TO_AGENDA, assignToAgendaBean);
        
        actionBeanTaskMap.put(TaskName.CLOSE_PROTOCOL, protocolCloseBean);
        actionBeanTaskMap.put(TaskName.CLOSE_ENROLLMENT_PROTOCOL, protocolCloseEnrollmentBean);
        actionBeanTaskMap.put(TaskName.PROTOCOL_REQUEST_CLOSE_ENROLLMENT, protocolCloseEnrollmentRequestBean);
        actionBeanTaskMap.put(TaskName.PROTOCOL_REQUEST_CLOSE, protocolCloseRequestBean);
        
// TODO ********************** commented out during IRB backfit ************************
//        actionBeanTaskMap.put(TaskName.RECORD_COMMITTEE_DECISION, committeeDecision);
        
        actionBeanTaskMap.put(TaskName.PERMIT_DATA_ANALYSIS, protocolPermitDataAnalysisBean);
        actionBeanTaskMap.put(TaskName.PROTOCOL_REQUEST_DATA_ANALYSIS, protocolDataAnalysisRequestBean);
        
// TODO ********************** commented out during IRB backfit ************************        
//        actionBeanTaskMap.put(TaskName.PROTOCOL_AMEND_RENEW_DELETE, protocolDeleteBean);
        
        actionBeanTaskMap.put(TaskName.DEFER_PROTOCOL, protocolDeferBean);
        
// TODO ********************** commented out during IRB backfit ************************        
//        actionBeanTaskMap.put(TaskName.DISAPPROVE_PROTOCOL, protocolDisapproveBean);
        actionBeanTaskMap.put(TaskName.EXPEDITE_APPROVAL, protocolExpeditedApprovalBean);
        
// TODO ********************** commented out during IRB backfit ************************        
//        actionBeanTaskMap.put(TaskName.EXPIRE_PROTOCOL, protocolExpireBean);
        
        actionBeanTaskMap.put(TaskName.GRANT_EXEMPTION, protocolGrantExemptionBean);
        actionBeanTaskMap.put(TaskName.IRB_ACKNOWLEDGEMENT, protocolIrbAcknowledgementBean);
        
// TODO ********************** commented out during IRB backfit ************************        
//        actionBeanTaskMap.put(TaskName.PROTOCOL_MANAGE_REVIEW_COMMENTS, protocolManageReviewCommentsBean);
        actionBeanTaskMap.put(TaskName.MODIFY_PROTOCOL_SUBMISSION, protocolModifySubmissionBean);
        actionBeanTaskMap.put(TaskName.NOTIFY_IRB, protocolNotifyIrbBean);
        actionBeanTaskMap.put(TaskName.NOTIFY_COMMITTEE, protocolNotifyCommitteeBean);
        actionBeanTaskMap.put(TaskName.REOPEN_PROTOCOL, protocolReopenEnrollmentBean);
        actionBeanTaskMap.put(TaskName.PROTOCOL_REQUEST_REOPEN_ENROLLMENT, protocolReOpenEnrollmentRequestBean);
        actionBeanTaskMap.put(TaskName.RESPONSE_APPROVAL, protocolResponseApprovalBean);
        actionBeanTaskMap.put(TaskName.PROTOCOL_REQUEST_CLOSE, protocolCloseRequestBean);
        actionBeanTaskMap.put(TaskName.PROTOCOL_REQUEST_CLOSE_ENROLLMENT, protocolCloseEnrollmentRequestBean);
        actionBeanTaskMap.put(TaskName.PROTOCOL_REQUEST_REOPEN_ENROLLMENT, protocolReOpenEnrollmentRequestBean);
        actionBeanTaskMap.put(TaskName.PROTOCOL_REQUEST_DATA_ANALYSIS, protocolDataAnalysisRequestBean);
        actionBeanTaskMap.put(TaskName.PROTOCOL_REQUEST_SUSPENSION, protocolSuspendRequestBean);
        actionBeanTaskMap.put(TaskName.PROTOCOL_REQUEST_TERMINATE, protocolTerminateRequestBean);
        actionBeanTaskMap.put(TaskName.PROTOCOL_REVIEW_NOT_REQUIRED, protocolReviewNotRequiredBean);
        
// TODO ********************** commented out during IRB backfit ************************        
//        actionBeanTaskMap.put(TaskName.RETURN_FOR_SMR, protocolSMRBean);
//        actionBeanTaskMap.put(TaskName.RETURN_FOR_SRR, protocolSRRBean);
//        actionBeanTaskMap.put(TaskName.SUBMIT_PROTOCOL, protocolSubmitAction);
//        actionBeanTaskMap.put(TaskName.SUSPEND_PROTOCOL, protocolSuspendBean);
        
        actionBeanTaskMap.put(TaskName.SUSPEND_PROTOCOL_BY_DSMB, protocolSuspendByDsmbBean);
        actionBeanTaskMap.put(TaskName.PROTOCOL_REQUEST_SUSPENSION, protocolSuspendRequestBean);
        
// TODO ********************** commented out during IRB backfit ************************        
//        actionBeanTaskMap.put(TaskName.TERMINATE_PROTOCOL, protocolTerminateBean);
        
        actionBeanTaskMap.put(TaskName.PROTOCOL_REQUEST_TERMINATE, protocolTerminateRequestBean);
        actionBeanTaskMap.put(TaskName.PROTOCOL_UNDO_LAST_ACTION, undoLastActionBean);
        
// TODO ********************** commented out during IRB backfit ************************        
//        actionBeanTaskMap.put(TaskName.PROTOCOL_WITHDRAW, protocolWithdrawBean);       
//        actionBeanTaskMap.put(TaskName.RETURN_TO_PI_PROTOCOL, protocolReturnToPIBean);
    }
    
    /**
     *     
     * This method builds a ProtocolGenericActionBean.  A number of different beans
     * in this object are of type ProtocolGenericActionBean, and all need to add
     * reviewer comments.  This encapsulates that.
     * @return a ProtocolGenericActionBean, and pre-populated with reviewer comments if any exist
     */
    protected ProtocolGenericActionBean buildProtocolGenericActionBean(String actionTypeCode, String errorPropertyKey) {
        ProtocolGenericActionBean bean = new ProtocolGenericActionBean(this, errorPropertyKey);
        
        bean.getReviewCommentsBean().setReviewComments(getCopiedReviewComments());
//        bean.getReviewCommentsBean().setHideReviewerName(getReviewCommentsService().setHideReviewerName(bean.getReviewCommentsBean().getReviewComments()));            
        ProtocolAction protocolAction = (ProtocolAction) findProtocolAction(actionTypeCode, getProtocol().getProtocolActions(), getProtocol().getProtocolSubmission());
        if (protocolAction != null) {
            bean.setComments(protocolAction.getComments());
            bean.setActionDate(new Date(protocolAction.getActionDate().getTime()));
        }
        
        return bean;
    }

// TODO ********************** commented out during IRB backfit ************************    
//    private ReviewCommentsService getReviewCommentsService() {
//        return KraServiceLocator.getService(ReviewCommentsService.class);
//    }
//
//    protected ProtocolApproveBean buildProtocolApproveBean(String actionTypeCode, String errorPropertyKey) throws Exception {
//        
//        ProtocolApproveBean bean = new ProtocolApproveBean(this, errorPropertyKey);
//        
//        bean.getReviewCommentsBean().setReviewComments(getCopiedReviewComments());
////        bean.getReviewAttachmentsBean().setReviewAttachments(getProtocol().getProtocolSubmission().getReviewAttachments());
////        bean.getReviewCommentsBean().setHideReviewerName(getReviewCommentsService().setHideReviewerName(bean.getReviewCommentsBean().getReviewComments()));            
//       ProtocolAction protocolAction = findProtocolAction(actionTypeCode, getProtocol().getProtocolActions(), getProtocol().getProtocolSubmission());
//        if (protocolAction != null) {
//            bean.setComments(protocolAction.getComments());
//            bean.setActionDate(new Date(protocolAction.getActionDate().getTime()));
//        }
//        bean.setApprovalDate(buildApprovalDate(getProtocol()));
//        bean.setExpirationDate(buildExpirationDate(getProtocol(), bean.getApprovalDate()));
//        return bean;
//    }
    
    private ProtocolExpeditedApproveBean buildProtocolExpeditedApproveBean(String actionTypeCode) throws Exception {
        
        ProtocolExpeditedApproveBean bean = new ProtocolExpeditedApproveBean(this);
        
        bean.getReviewCommentsBean().setReviewComments(getCopiedReviewComments());
        ProtocolAction protocolAction = (ProtocolAction) findProtocolAction(actionTypeCode, getProtocol().getProtocolActions(), getProtocol().getProtocolSubmission());
        if (protocolAction != null) {
            bean.setComments(protocolAction.getComments());
            bean.setActionDate(new Date(protocolAction.getActionDate().getTime()));
        }
        bean.setApprovalDate(buildApprovalDate(getProtocol()));
        bean.setExpirationDate(buildExpirationDate(getProtocol(), bean.getApprovalDate()));
        return bean;
    }
    
// TODO ********************** commented out during IRB backfit ************************    
//    /**
//     * Builds an approval date, defaulting to the approval date from the protocol.
//     * 
//     * If the approval date from the protocol is null, or if the protocol is new or a renewal, then if the committee has scheduled a meeting to approve the 
//     * protocol, sets to the scheduled approval date; otherwise, sets to the current date.
//     * 
//     * @param protocol
//     * @return a non-null approval date
//     */
//    private Date buildApprovalDate(Protocol protocol) {
//        Date approvalDate = protocol.getApprovalDate();
//        
//        if (approvalDate == null || protocol.isNew() || protocol.isRenewal()) {
//            CommitteeSchedule committeeSchedule = (CommitteeSchedule) protocol.getProtocolSubmission().getCommitteeSchedule();
//            if (committeeSchedule != null) {
//                approvalDate = committeeSchedule.getScheduledDate();
//            } else {
//                approvalDate = new Date(System.currentTimeMillis());
//            }
//        }
//        
//        return approvalDate;
//    }
//    
//    /**
//     * Builds an expiration date, defaulting to the expiration date from the protocol.  
//     * 
//     * If the expiration date from the protocol is null, or if the protocol is new or a renewal, creates an expiration date exactly one year ahead and one day 
//     * less than the approval date.
//     * 
//     * @param protocol
//     * @param approvalDate
//     * @return a non-null expiration date
//     */
//    private Date buildExpirationDate(Protocol protocol, Date approvalDate) {
//        Date expirationDate = protocol.getExpirationDate();
//        
//        if (expirationDate == null || protocol.isNew() || protocol.isRenewal()) {
//            java.util.Date newExpirationDate = DateUtils.addYears(approvalDate, 1);
//            newExpirationDate = DateUtils.addDays(newExpirationDate, -1);
//            expirationDate = DateUtils.convertToSqlDate(newExpirationDate);
//        }
//        
//        return expirationDate;
//    }
//
//    
//    private ProtocolAction findProtocolAction(String actionTypeCode, List<ProtocolAction> protocolActions, ProtocolSubmission currentSubmission) {
//
//        for (ProtocolAction pa : protocolActions) {
//            if (pa.getProtocolActionType().getProtocolActionTypeCode().equals(actionTypeCode)
//                    && (pa.getProtocolSubmission() == null || pa.getProtocolSubmission().equals(currentSubmission))) {
//                return pa;
//            }
//        }
//        return null;
//    }
//
//    // always reinitialize amendment beans, otherwise a second pass thru prepareView() will show same
//    // amendment creation options as previous passes
//    public void initAmendmentBeans(boolean forceReset) throws Exception {
//        if (protocolAmendmentBean == null || forceReset) {
//            protocolAmendmentBean = createAmendmentBean();
//        }
//        if (protocolRenewAmendmentBean == null || forceReset) {
//            protocolRenewAmendmentBean = createAmendmentBean();
//        }
//    }
//
//    /**
//     * Create an Amendment Bean.  The modules that can be selected depends upon the
//     * current outstanding amendments.  If a module is currently being modified by a
//     * previous amendment, it cannot be modified by another amendment.  Once the 
//     * previous amendment has completed (approved, disapproved, etc), then a new
//     * amendment can modify the same module.
//     * @return
//     * @throws Exception 
//     */
//    private ProtocolAmendmentBean createAmendmentBean() throws Exception {
//        ProtocolAmendmentBean amendmentBean = new ProtocolAmendmentBean(this);
//        List<String> moduleTypeCodes;
//
//        if (StringUtils.isNotEmpty(getProtocol().getProtocolNumber()) && (getProtocol().isAmendment() || getProtocol().isRenewal())) {
//            moduleTypeCodes = getProtocolAmendRenewService().getAvailableModules(getProtocol().getAmendedProtocolNumber());
//            populateExistingAmendmentBean(amendmentBean, moduleTypeCodes);
//        } else {
//            moduleTypeCodes = getProtocolAmendRenewService().getAvailableModules(getProtocol().getProtocolNumber());
//        }
//        
//        for (String moduleTypeCode : moduleTypeCodes) {
//            enableModuleOption(moduleTypeCode, amendmentBean);
//        }
//        
//        return amendmentBean;
//    }

    /**
     * This method copies the settings from the ProtocolAmendRenewal bo to the amendmentBean and enables the
     * corresponding modules. 
     * @param amendmentBean
     */
    protected void populateExistingAmendmentBean(org.kuali.kra.protocol.actions.amendrenew.ProtocolAmendmentBean amendmentBean, List<String> moduleTypeCodes) {
        ProtocolAmendRenewal protocolAmendRenewal = (ProtocolAmendRenewal) getProtocol().getProtocolAmendRenewal();
        amendmentBean.setSummary(protocolAmendRenewal.getSummary());
        for (ProtocolAmendRenewModuleBase module : protocolAmendRenewal.getModules()) {
            moduleTypeCodes.add(module.getProtocolModuleTypeCode());
            if (StringUtils.equals(ProtocolModule.GENERAL_INFO, module.getProtocolModuleTypeCode())) {
                amendmentBean.setGeneralInfo(true);
            } 
            else if (StringUtils.equals(ProtocolModule.ADD_MODIFY_ATTACHMENTS, module.getProtocolModuleTypeCode())) {
                amendmentBean.setAddModifyAttachments(true);
            }
            else if (StringUtils.equals(ProtocolModule.AREAS_OF_RESEARCH, module.getProtocolModuleTypeCode())) {
                amendmentBean.setAreasOfResearch(true);
            }
            else if (StringUtils.equals(ProtocolModule.FUNDING_SOURCE, module.getProtocolModuleTypeCode())) {
                amendmentBean.setFundingSource(true);
            }
            else if (StringUtils.equals(ProtocolModule.OTHERS, module.getProtocolModuleTypeCode())) {
                amendmentBean.setOthers(true);
            }
            else if (StringUtils.equals(ProtocolModule.PROTOCOL_ORGANIZATIONS, module.getProtocolModuleTypeCode())) {
                amendmentBean.setProtocolOrganizations(true);
            }
            else if (StringUtils.equals(ProtocolModule.PROTOCOL_PERSONNEL, module.getProtocolModuleTypeCode())) {
                amendmentBean.setProtocolPersonnel(true);
            }
            else if (StringUtils.equals(ProtocolModule.PROTOCOL_REFERENCES, module.getProtocolModuleTypeCode())) {
                amendmentBean.setProtocolReferencesAndOtherIdentifiers(true);
            }
            else if (StringUtils.equals(ProtocolModule.SPECIAL_REVIEW, module.getProtocolModuleTypeCode())) {
                amendmentBean.setSpecialReview(true);
            }
            else if (StringUtils.equals(ProtocolModule.SUBJECTS, module.getProtocolModuleTypeCode())) {
                amendmentBean.setSubjects(true);
            }
            else if (StringUtils.equals(ProtocolModule.PROTOCOL_PERMISSIONS, module.getProtocolModuleTypeCode())) {
                amendmentBean.setProtocolPermissions(true);
            }
            else if (StringUtils.equals(ProtocolModule.QUESTIONNAIRE, module.getProtocolModuleTypeCode())) {
                amendmentBean.setQuestionnaire(true);
            }
        }
    }

// TODO ********************** commented out during IRB backfit ************************
//    /**
//     * Create an AdminCorrection Bean.  The modules that can be edited (or corrected) depends upon the
//     * current outstanding amendments.  If a module is currently being modified by a
//     * an amendment, it cannot be corrected through Administrative Correction.  
//     * @return
//     * @throws Exception 
//     */
//    private AdminCorrectionBean createAdminCorrectionBean() throws Exception {
//        AdminCorrectionBean adminCorrectionBean = new AdminCorrectionBean(this);
//        List<String> moduleTypeCodes = getProtocolAmendRenewService().getAvailableModules(getProtocol().getProtocolNumber());
//        
//        for (String moduleTypeCode : moduleTypeCodes) {
//            enableModuleOption(moduleTypeCode, adminCorrectionBean);
//        }
//        
//        return adminCorrectionBean;
//    }
    
    private UndoLastActionBean createUndoLastActionBean(Protocol protocol) throws Exception {
        undoLastActionBean = new UndoLastActionBean(this);
        undoLastActionBean.setProtocol(protocol);
        Collections.sort((List)protocol.getProtocolActions(), new Comparator<ProtocolAction>() {
            public int compare(ProtocolAction action1, ProtocolAction action2) {
                return action2.getActualActionDate().compareTo(action1.getActualActionDate());
            }
        });
        undoLastActionBean.setActionsPerformed((List)protocol.getProtocolActions());
        return undoLastActionBean;
    }
    
    /**
     * Enable a module for selection by a user by setting its corresponding enabled
     * flag to true in the amendment bean.
     * @param moduleTypeCode
     * @param amendmentBean
     */
    protected void enableModuleOption(String moduleTypeCode, ProtocolEditableBean amendmentBean) {
        if (StringUtils.equals(ProtocolModule.GENERAL_INFO, moduleTypeCode)) {
            amendmentBean.setGeneralInfoEnabled(true);
        } 
        else if (StringUtils.equals(ProtocolModule.ADD_MODIFY_ATTACHMENTS, moduleTypeCode)) {
            amendmentBean.setAddModifyAttachmentsEnabled(true);
        }
        else if (StringUtils.equals(ProtocolModule.AREAS_OF_RESEARCH, moduleTypeCode)) {
            amendmentBean.setAreasOfResearchEnabled(true);
        }
        else if (StringUtils.equals(ProtocolModule.FUNDING_SOURCE, moduleTypeCode)) {
            amendmentBean.setFundingSourceEnabled(true);
        }
        else if (StringUtils.equals(ProtocolModule.OTHERS, moduleTypeCode)) {
            amendmentBean.setOthersEnabled(true);
        }
        else if (StringUtils.equals(ProtocolModule.PROTOCOL_ORGANIZATIONS, moduleTypeCode)) {
            amendmentBean.setProtocolOrganizationsEnabled(true);
        }
        else if (StringUtils.equals(ProtocolModule.PROTOCOL_PERSONNEL, moduleTypeCode)) {
            amendmentBean.setProtocolPersonnelEnabled(true);
        }
        else if (StringUtils.equals(ProtocolModule.PROTOCOL_REFERENCES, moduleTypeCode)) {
            amendmentBean.setProtocolReferencesEnabled(true);
        }
        else if (StringUtils.equals(ProtocolModule.SPECIAL_REVIEW, moduleTypeCode)) {
            amendmentBean.setSpecialReviewEnabled(true);
        }
        else if (StringUtils.equals(ProtocolModule.SUBJECTS,moduleTypeCode)) {
            amendmentBean.setSubjectsEnabled(true);
        }
        else if (StringUtils.equals(ProtocolModule.PROTOCOL_PERMISSIONS,moduleTypeCode)) {
            amendmentBean.setProtocolPermissionsEnabled(true);
        }
        else if (StringUtils.equals(ProtocolModule.QUESTIONNAIRE,moduleTypeCode)) {
            amendmentBean.setQuestionnaireEnabled(true);
        }
    }

    protected ProtocolAmendRenewService getProtocolAmendRenewServiceHook() {
        if (this.protocolAmendRenewService == null) {
            this.protocolAmendRenewService = KraServiceLocator.getService(ProtocolAmendRenewService.class);        
        }
        return (ProtocolAmendRenewService) this.protocolAmendRenewService;
    }

    public void prepareView() throws Exception {
        
        super.prepareView();
        
// TODO ********************** commented out during IRB backfit ************************        
//        protocolSubmitAction.prepareView();
//        canSubmitProtocol = hasSubmitProtocolPermission();
//        canSubmitProtocolUnavailable = hasSubmitProtocolUnavailablePermission();
//        assignToAgendaBean.prepareView();
        
        assignCmtSchedBean.prepareView();
        protocolAssignReviewersBean.prepareView();
        protocolExpeditedApprovalBean.prepareView();
        submissionConstraint = getParameterValue(Constants.PARAMETER_IRB_COMM_SELECTION_DURING_SUBMISSION);
   
// TODO ********************** commented out during IRB backfit ************************        
//        canCreateAmendment = hasCreateAmendmentPermission();
//        canCreateAmendmentUnavailable = hasCreateAmendmentUnavailablePermission();
//        canModifyAmendmentSections = hasModifyAmendmentSectionsPermission();
//        canModifyAmendmentSectionsUnavailable = hasModifyAmendmentSectionsUnavailablePermission();
//        canCreateRenewal = hasCreateRenewalPermission();
//        canCreateRenewalUnavailable = hasCreateRenewalUnavailablePermission();
        
        
        canNotifyIrb = hasNotifyIrbPermission();
        canNotifyIrbUnavailable = hasNotifyIrbUnavailablePermission();
        canNotifyCommittee = hasNotifyCommitteePermission();
        canNotifyCommitteeUnavailable = hasNotifyCommitteeUnavailablePermission();
        
// TODO ********************** commented out during IRB backfit ************************        
//        canWithdraw = hasWithdrawPermission();
//        canWithdrawUnavailable = hasWithdrawUnavailablePermission();
        
        canRequestClose = hasRequestClosePermission();
        canRequestCloseUnavailable = hasRequestCloseUnavailablePermission();
        canRequestSuspension = hasRequestSuspensionPermission();
        canRequestSuspensionUnavailable = hasRequestSuspensionUnavailablePermission();
        canRequestCloseEnrollment = hasRequestCloseEnrollmentPermission();
        canRequestCloseEnrollmentUnavailable = hasRequestCloseEnrollmentUnavailablePermission();
        canRequestReOpenEnrollment = hasRequestReOpenEnrollmentPermission();
        canRequestReOpenEnrollmentUnavailable = hasRequestReOpenEnrollmentUnavailablePermission();
        canRequestDataAnalysis = hasRequestDataAnalysisPermission();
        canRequestDataAnalysisUnavailable = hasRequestDataAnalysisUnavailablePermission();
        canRequestTerminate = hasRequestTerminatePermission();
        canRequestTerminateUnavailable = hasRequestTerminateUnavailablePermission();
        
//        canDeleteProtocolAmendRenew = hasDeleteProtocolAmendRenewPermission();
//        canDeleteProtocolAmendRenewUnavailable = hasDeleteProtocolAmendRenewUnavailablePermission();
//        canAssignToAgenda = hasAssignToAgendaPermission();
//        canAssignToAgendaUnavailable = hasAssignToAgendaUnavailablePermission();
        
        canAssignCmtSched = hasAssignCmtSchedPermission();
        canAssignCmtSchedUnavailable = hasAssignCmtSchedUnavailablePermission();
        canAssignReviewers = hasAssignReviewersPermission();
        canAssignReviewersCmtSel = hasAssignReviewersCmtSel();
        canAssignReviewersUnavailable = hasAssignReviewersUnavailablePermission();
        canGrantExemption = hasGrantExemptionPermission();
        canGrantExemptionUnavailable = hasGrantExemptionUnavailablePermission();
        
// TODO ********************** commented out during IRB backfit ************************        
//        canApproveFull = hasFullApprovePermission();
//        canApproveFullUnavailable = hasFullApproveUnavailablePermission();
        
        canApproveExpedited = hasExpeditedApprovalPermission();
        canApproveExpeditedUnavailable = hasExpeditedApprovalUnavailablePermission();
        canApproveResponse = hasResponseApprovalPermission();
        canApproveResponseUnavailable = hasResponseApprovalUnavailablePermission();
        
// TODO ********************** commented out during IRB backfit ************************        
//        canDisapprove = hasDisapprovePermission();
//        canDisapproveUnavailable = hasDisapproveUnavailablePermission();
//        canReturnForSMR = hasReturnForSMRPermission();
//        canReturnForSMRUnavailable = hasReturnForSMRUnavailablePermission();
//        canReturnForSRR = hasReturnForSRRPermission();
//        canReturnForSRRUnavailable = hasReturnForSRRUnavailablePermission();
        
        canReopenEnrollment = hasReopenEnrollmentPermission();
        canReopenEnrollmentUnavailable = hasReopenEnrollmentUnavailablePermission();
        canCloseEnrollment = hasCloseEnrollmentPermission();
        canCloseEnrollmentUnavailable = hasCloseEnrollmentUnavailablePermission();
        
// TODO ********************** commented out during IRB backfit ************************        
//        canSuspend = hasSuspendPermission();
//        canSuspendUnavailable = hasSuspendUnavailablePermission();
        
        canSuspendByDsmb = hasSuspendByDsmbPermission();
        canSuspendByDsmbUnavailable = hasSuspendByDsmbUnavailablePermission();
        canClose = hasClosePermission();
        canCloseUnavailable = hasCloseUnavailablePermission();
        
// TODO ********************** commented out during IRB backfit ************************        
//        canExpire = hasExpirePermission();
//        canExpireUnavailable = hasExpireUnavailablePermission();
//        canTerminate = hasTerminatePermission();
//        canTerminateUnavailable = hasTerminateUnavailablePermission();
        
        canPermitDataAnalysis = hasPermitDataAnalysisPermission();
        canPermitDataAnalysisUnavailable = hasPermitDataAnalysisUnavailablePermission();
        
        
// TODO ********************** commented out during IRB backfit ************************        
//        canMakeAdminCorrection = hasAdminCorrectionPermission();
//        canMakeAdminCorrectionUnavailable = hasAdminCorrectionUnavailablePermission();
//        canRecordCommitteeDecision = hasRecordCommitteeDecisionPermission();
//        canRecordCommitteeDecisionUnavailable = hasRecordCommitteeDecisionUnavailablePermission();
        
        canEnterRiskLevel = hasEnterRiskLevelPermission();
        canUndoLastAction = hasUndoLastActionPermission();
        canUndoLastActionUnavailable = hasUndoLastActionUnavailablePermission();
        canIrbAcknowledgement = hasIrbAcknowledgementPermission();
        canIrbAcknowledgementUnavailable = hasIrbAcknowledgementUnavailablePermission();
        canDefer = hasDeferPermission();
        canDeferUnavailable = hasDeferUnavailablePermission();
        
// TODO ********************** commented out during IRB backfit ************************        
//        canModifyProtocolSubmission = hasCanModifySubmissionPermission();
//        canModifyProtocolSubmissionUnavailable = hasCanModifySubmissionUnavailablePermission();
        
        canReviewNotRequired = hasReviewNotRequiredPermission();
        canReviewNotRequiredUnavailable = hasReviewNotRequiredUnavailablePermission();
        
// TODO ********************** commented out during IRB backfit ************************        
//        canManageReviewComments = hasManageReviewCommentsPermission();
//        canManageReviewCommentsUnavailable = hasManageReviewCommentsUnavailablePermission();
        
        canApproveOther = hasApproveOtherPermission();
        canManageNotes = hasManageNotesPermission();
        canManageNotesUnavailable = hasManageNotesUnavailablePermission();
     
// TODO ********************** commented out during IRB backfit ************************        
//        canAbandon = hasAbandonProtocolPermission();
        
        canReturnToPI = hasPermission(TaskName.RETURN_TO_PI_PROTOCOL);
        canReturnToPIUnavailable = hasPermission(TaskName.RETURN_TO_PI_PROTOCOL_UNAVAILABLE);
        
// TODO ********************** commented out during IRB backfit ************************        
//        followupActionActions = getFollowupActionService().getFollowupsForProtocol(form.getProtocolDocument().getProtocol());
//        
//        
//        canViewOnlineReviewers = hasCanViewOnlineReviewersPermission();
//        canViewOnlineReviewerComments = hasCanViewOnlineReviewerCommentsPermission();
        
        canAddCloseReviewerComments = hasCloseRequestLastAction();
        canAddCloseEnrollmentReviewerComments = hasCloseEnrollmentRequestLastAction();
        canAddDataAnalysisReviewerComments = hasDataAnalysisRequestLastAction();
        canAddReopenEnrollmentReviewerComments = hasReopenEnrollmentRequestLastAction();
        
// TODO ********************** commented out during IRB backfit ************************        
//        canAddSuspendReviewerComments = hasSuspendRequestLastAction();
//        canAddTerminateReviewerComments = hasTerminateRequestLastAction();
        
        hideReviewerName = checkToHideReviewName();
        undoLastActionBean = createUndoLastActionBean((Protocol) getProtocol());
       
        initSummaryDetails();

// TODO ********************** commented out during IRB backfit ************************        
//        initSubmissionDetails();
        
        setAmendmentDetails();
        initFilterDatesView();
        
// TODO ********************** commented out during IRB backfit ************************        
//        initAmendmentBeans(false);
//        initPrintQuestionnaire();
    }
    
    
    
    /**
     * Refreshes the comments for all the beans from the database.  Use sparingly since this will erase non-persisted comments.
     */
    public void prepareCommentsView() {
        
        super.prepareCommentsView();
 
// TODO ********************** commented out during IRB backfit ************************        
//        assignToAgendaBean.getReviewCommentsBean().setReviewComments(getCopiedReviewComments());
        
        protocolGrantExemptionBean.getReviewCommentsBean().setReviewComments(getCopiedReviewComments());
        protocolIrbAcknowledgementBean.getReviewCommentsBean().setReviewComments(getCopiedReviewComments());
        
// TODO ********************** commented out during IRB backfit ************************        
//        protocolFullApprovalBean.getReviewCommentsBean().setReviewComments(getCopiedReviewComments());
        
        
        protocolExpeditedApprovalBean.getReviewCommentsBean().setReviewComments(getCopiedReviewComments());
        protocolResponseApprovalBean.getReviewCommentsBean().setReviewComments(getCopiedReviewComments());
        
// TODO ********************** commented out during IRB backfit ************************        
//        protocolDisapproveBean.getReviewCommentsBean().setReviewComments(getCopiedReviewComments());
//        protocolSMRBean.getReviewCommentsBean().setReviewComments(getCopiedReviewComments());
//        protocolSRRBean.getReviewCommentsBean().setReviewComments(getCopiedReviewComments());
        
        protocolReopenEnrollmentBean.getReviewCommentsBean().setReviewComments(getCopiedReviewComments());
        protocolCloseEnrollmentBean.getReviewCommentsBean().setReviewComments(getCopiedReviewComments());
        
// TODO ********************** commented out during IRB backfit ************************        
//        protocolSuspendBean.getReviewCommentsBean().setReviewComments(getCopiedReviewComments());
        
        protocolSuspendByDsmbBean.getReviewCommentsBean().setReviewComments(getCopiedReviewComments());
        protocolCloseBean.getReviewCommentsBean().setReviewComments(getCopiedReviewComments());
        
// TODO ********************** commented out during IRB backfit ************************        
//        protocolExpireBean.getReviewCommentsBean().setReviewComments(getCopiedReviewComments());
//        protocolTerminateBean.getReviewCommentsBean().setReviewComments(getCopiedReviewComments());
        
        protocolPermitDataAnalysisBean.getReviewCommentsBean().setReviewComments(getCopiedReviewComments());
        
// TODO ********************** commented out during IRB backfit ************************        
//        committeeDecision.getReviewCommentsBean().setReviewComments(getCopiedReviewComments());
        
//        committeeDecision.getReviewCommentsBean().setHideReviewerName(getReviewCommentsService().setHideReviewerName(committeeDecision.getReviewCommentsBean().getReviewComments()));
        
// TODO ********************** commented out during IRB backfit ************************        
//        protocolDeferBean.getReviewCommentsBean().setReviewComments(getCopiedReviewComments());
//        protocolManageReviewCommentsBean.getReviewCommentsBean().setReviewComments(getCopiedReviewComments());
//        getProtocol().getProtocolSubmission().refreshReferenceObject("reviewAttachments");
//        protocolManageReviewCommentsBean.getReviewAttachmentsBean().setReviewAttachments(getProtocol().getProtocolSubmission().getReviewAttachments());
//        if (CollectionUtils.isNotEmpty(protocolManageReviewCommentsBean.getReviewAttachmentsBean().getReviewAttachments())) {
//            protocolManageReviewCommentsBean.getReviewAttachmentsBean().setHideReviewerName(getReviewerCommentsService().setHideReviewerName(protocolManageReviewCommentsBean.getReviewAttachmentsBean().getReviewAttachments()));
//        }
        
    }
    
// TODO ********************** commented out during IRB backfit ************************    
//    private List<CommitteeScheduleMinute> getCopiedReviewComments() {
//        List<CommitteeScheduleMinute> clonedMinutes = new ArrayList<CommitteeScheduleMinute>();
//        Long scheduleIdFk = getProtocol().getProtocolSubmission().getScheduleIdFk();
//        List<CommitteeScheduleMinute> minutes = getCommitteeScheduleService().getMinutesBySchedule(scheduleIdFk);
//        if (CollectionUtils.isNotEmpty(minutes)) {
//            for (CommitteeScheduleMinute minute : minutes) {
//                clonedMinutes.add(minute.getCopy());
//            }
//        }
//        
//        return clonedMinutes;
//    }
//        
//    private CommitteeScheduleService getCommitteeScheduleService() {
//        if (committeeScheduleService == null) {
//            committeeScheduleService = KraServiceLocator.getService(CommitteeScheduleService.class);        
//        }
//        return committeeScheduleService;
//    }
    
    
    protected ProtocolVersionService getProtocolVersionService() {
        if (this.protocolVersionService == null) {
            this.protocolVersionService = KraServiceLocator.getService(ProtocolVersionService.class);        
        }
        return (ProtocolVersionService) this.protocolVersionService;
    }
    
    private ProtocolSubmitActionService getProtocolSubmitActionService() {
        if (protocolSubmitActionService == null) {
            protocolSubmitActionService = KraServiceLocator.getService(ProtocolSubmitActionService.class);
        }
        return protocolSubmitActionService;
    }
    
    private ProtocolActionService getProtocolActionService() {
        if (protocolActionService == null) {
            protocolActionService = KraServiceLocator.getService(ProtocolActionService.class);
        }
        return protocolActionService;
    }

// TODO ********************** commented out during IRB backfit ************************    
//    private String getParameterValue(String parameterName) {
//        return this.getParameterService().getParameterValueAsString(ProtocolDocument.class, parameterName);      
//    }
//    
//    private boolean hasSubmitProtocolPermission() {
//        ProtocolTask task = new ProtocolTask(TaskName.SUBMIT_PROTOCOL, getProtocol());
//        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
//    }
//    
//    private boolean hasSubmitProtocolUnavailablePermission() {
//        ProtocolTask task = new ProtocolTask(TaskName.SUBMIT_PROTOCOL_UNAVAILABLE, getProtocol());
//        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
//    }
//    
//    private boolean hasCreateAmendmentPermission() {
//        ProtocolTask task = new ProtocolTask(TaskName.CREATE_PROTOCOL_AMMENDMENT, getProtocol());
//        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
//    }
//    
//    private boolean hasCreateAmendmentUnavailablePermission() {
//        ProtocolTask task = new ProtocolTask(TaskName.CREATE_PROTOCOL_AMMENDMENT_UNAVAILABLE, getProtocol());
//        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
//    }
//    
//    private boolean hasModifyAmendmentSectionsPermission() {
//        ProtocolTask task = new ProtocolTask(TaskName.MODIFY_PROTOCOL_AMMENDMENT_SECTIONS, getProtocol());
//        return ((!getProtocol().isRenewalWithoutAmendment())&&(getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task)));
//    }
//    
//    private boolean hasModifyAmendmentSectionsUnavailablePermission() {
//        ProtocolTask task = new ProtocolTask(TaskName.MODIFY_PROTOCOL_AMMENDMENT_SECTIONS_UNAVAILABLE, getProtocol());
//        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
//    }
//    
//    private boolean hasCreateRenewalPermission() {
//        ProtocolTask task = new ProtocolTask(TaskName.CREATE_PROTOCOL_RENEWAL, getProtocol());
//        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
//    }
//    
//    private boolean hasCreateRenewalUnavailablePermission() {
//        ProtocolTask task = new ProtocolTask(TaskName.CREATE_PROTOCOL_RENEWAL_UNAVAILABLE, getProtocol());
//        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
//    }
    
    private boolean hasNotifyIrbPermission() {
        ProtocolTask task = new ProtocolTask(TaskName.NOTIFY_IRB, (Protocol) getProtocol());
        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
    }
    
    private boolean hasNotifyIrbUnavailablePermission() {
        ProtocolTask task = new ProtocolTask(TaskName.NOTIFY_IRB_UNAVAILABLE, (Protocol) getProtocol());
        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
    }
    
    private boolean hasNotifyCommitteePermission() {
        ProtocolTask task = new ProtocolTask(TaskName.NOTIFY_COMMITTEE, (Protocol) getProtocol());
        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
    }
    
    private boolean hasNotifyCommitteeUnavailablePermission() {
        ProtocolTask task = new ProtocolTask(TaskName.NOTIFY_COMMITTEE_UNAVAILABLE, (Protocol) getProtocol());
        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
    }
    
// TODO ********************** commented out during IRB backfit ************************    
//    private boolean hasWithdrawPermission() {
//        ProtocolTask task = new ProtocolTask(TaskName.PROTOCOL_WITHDRAW, getProtocol());
//        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
//    }
//    
//    private boolean hasWithdrawUnavailablePermission() {
//        ProtocolTask task = new ProtocolTask(TaskName.PROTOCOL_WITHDRAW_UNAVAILABLE, getProtocol());
//        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
//    }
    
    private boolean hasRequestClosePermission() {
        ProtocolTask task = new ProtocolTask(TaskName.PROTOCOL_REQUEST_CLOSE, (Protocol) getProtocol());
        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
    }
    
    private boolean hasRequestCloseUnavailablePermission() {
        ProtocolTask task = new ProtocolTask(TaskName.PROTOCOL_REQUEST_CLOSE_UNAVAILABLE, (Protocol) getProtocol());
        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
    }
    
    private boolean hasRequestSuspensionPermission() {
        ProtocolTask task = new ProtocolTask(TaskName.PROTOCOL_REQUEST_SUSPENSION, (Protocol) getProtocol());
        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
    }
    
    private boolean hasRequestSuspensionUnavailablePermission() {
        ProtocolTask task = new ProtocolTask(TaskName.PROTOCOL_REQUEST_SUSPENSION_UNAVAILABLE, (Protocol) getProtocol());
        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
    }
    
    private boolean hasRequestCloseEnrollmentPermission() {
        ProtocolTask task = new ProtocolTask(TaskName.PROTOCOL_REQUEST_CLOSE_ENROLLMENT, (Protocol) getProtocol());
        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
    }
    
    private boolean hasRequestCloseEnrollmentUnavailablePermission() {
        ProtocolTask task = new ProtocolTask(TaskName.PROTOCOL_REQUEST_CLOSE_ENROLLMENT_UNAVAILABLE, (Protocol) getProtocol());
        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
    }
    
    private boolean hasRequestReOpenEnrollmentPermission() {
        ProtocolTask task = new ProtocolTask(TaskName.PROTOCOL_REQUEST_REOPEN_ENROLLMENT, (Protocol) getProtocol());
        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
    }
    
    private boolean hasRequestReOpenEnrollmentUnavailablePermission() {
        ProtocolTask task = new ProtocolTask(TaskName.PROTOCOL_REQUEST_REOPEN_ENROLLMENT_UNAVAILABLE, (Protocol) getProtocol());
        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
    }
    
    private boolean hasRequestDataAnalysisPermission() {
        ProtocolTask task = new ProtocolTask(TaskName.PROTOCOL_REQUEST_DATA_ANALYSIS, (Protocol) getProtocol());
        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
    }
    
    private boolean hasRequestDataAnalysisUnavailablePermission() {
        ProtocolTask task = new ProtocolTask(TaskName.PROTOCOL_REQUEST_DATA_ANALYSIS_UNAVAILABLE, (Protocol) getProtocol());
        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
    }
    
    private boolean hasRequestTerminatePermission() {
        ProtocolTask task = new ProtocolTask(TaskName.PROTOCOL_REQUEST_TERMINATE, (Protocol) getProtocol());
        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
    }
    
    private boolean hasRequestTerminateUnavailablePermission() {
        ProtocolTask task = new ProtocolTask(TaskName.PROTOCOL_REQUEST_TERMINATE_UNAVAILABLE, (Protocol) getProtocol());
        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
    }
    
// TODO ********************** commented out during IRB backfit ************************    
//    private boolean hasDeleteProtocolAmendRenewPermission() {
//        ProtocolTask task = new ProtocolTask(TaskName.PROTOCOL_AMEND_RENEW_DELETE, getProtocol());
//        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
//    }
//    
//    private boolean hasDeleteProtocolAmendRenewUnavailablePermission() {
//        ProtocolTask task = new ProtocolTask(TaskName.PROTOCOL_AMEND_RENEW_DELETE_UNAVAILABLE, getProtocol());
//        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
//    }
//    
//    private boolean hasAssignToAgendaPermission() {
//        ProtocolTask task = new ProtocolTask(TaskName.ASSIGN_TO_AGENDA, getProtocol());
//        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
//    }
//    
//    private boolean hasAssignToAgendaUnavailablePermission() {
//        ProtocolTask task = new ProtocolTask(TaskName.ASSIGN_TO_AGENDA_UNAVAILABLE, getProtocol());
//        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
//    }
    
    private boolean hasAssignCmtSchedPermission() {
        ProtocolTask task = new ProtocolTask(TaskName.ASSIGN_TO_COMMITTEE_SCHEDULE, (Protocol) getProtocol());
        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
    }
    
    public static boolean hasAssignCmtSchedPermission(String userId, String protocolNumber) {
        Map<String, String> fieldValues = new HashMap<String, String>();
        fieldValues.put("protocolNumber", protocolNumber);
        BusinessObjectService bos = KraServiceLocator.getService(BusinessObjectService.class);
        Protocol protocol = ((List<Protocol>) bos.findMatching(Protocol.class, fieldValues)).get(0);
        ProtocolTask task = new ProtocolTask(TaskName.MODIFY_PROPOSAL, protocol);
        TaskAuthorizationService tas = KraServiceLocator.getService(TaskAuthorizationService.class);        
        return tas.isAuthorized(userId, task);
    }
    
    private boolean hasAssignCmtSchedUnavailablePermission() {
        ProtocolTask task = new ProtocolTask(TaskName.ASSIGN_TO_COMMITTEE_SCHEDULE_UNAVAILABLE, (Protocol) getProtocol());
        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
    }
    
    private boolean hasAssignReviewersPermission() {
        ProtocolTask task = new ProtocolTask(TaskName.ASSIGN_REVIEWERS, (Protocol) getProtocol());
        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
    }
    
    private boolean hasAssignReviewersUnavailablePermission() {
        ProtocolTask task = new ProtocolTask(TaskName.ASSIGN_REVIEWERS_UNAVAILABLE, (Protocol) getProtocol());
        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
    }
    
    private boolean hasAssignReviewersCmtSel() {
        ProtocolTask task = new ProtocolTask(TaskName.ASSIGN_REVIEWERS_CMT_SEL, (Protocol) getProtocol());
        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
    }
    
    private boolean hasGrantExemptionPermission() {
        return hasPermission(TaskName.GRANT_EXEMPTION);
    }
    
    private boolean hasGrantExemptionUnavailablePermission() {
        return hasPermission(TaskName.GRANT_EXEMPTION_UNAVAILABLE);
    }

// TODO ********************** commented out during IRB backfit ************************    
//    private boolean hasFullApprovePermission() {
//        return hasPermission(TaskName.APPROVE_PROTOCOL);
//    }
//    
//    private boolean hasFullApproveUnavailablePermission() {
//        return hasPermission(TaskName.APPROVE_PROTOCOL_UNAVAILABLE);
//    }
    
    private boolean hasExpeditedApprovalPermission() {
        return hasPermission(TaskName.EXPEDITE_APPROVAL);
    }
    
    private boolean hasExpeditedApprovalUnavailablePermission() {
        return hasPermission(TaskName.EXPEDITE_APPROVAL_UNAVAILABLE);
    }
    
    private boolean hasResponseApprovalPermission() {
        return hasPermission(TaskName.RESPONSE_APPROVAL);
    }
    
    private boolean hasResponseApprovalUnavailablePermission() {
        return hasPermission(TaskName.RESPONSE_APPROVAL_UNAVAILABLE);
    }
    
// TODO ********************** commented out during IRB backfit ************************    
//    private boolean hasDisapprovePermission() {
//        return hasPermission(TaskName.DISAPPROVE_PROTOCOL);
//    }
//    
//    private boolean hasDisapproveUnavailablePermission() {
//        return hasPermission(TaskName.DISAPPROVE_PROTOCOL_UNAVAILABLE);
//    }
//    
//    private boolean hasReturnForSMRPermission() {
//        return hasPermission(TaskName.RETURN_FOR_SMR);
//    }
//    
//    private boolean hasReturnForSMRUnavailablePermission() {
//        return hasPermission(TaskName.RETURN_FOR_SMR_UNAVAILABLE);
//    }
//    
//    private boolean hasReturnForSRRPermission() {
//        return hasPermission(TaskName.RETURN_FOR_SRR);
//    }
//    
//    private boolean hasReturnForSRRUnavailablePermission() {
//        return hasPermission(TaskName.RETURN_FOR_SRR_UNAVAILABLE);
//    }
    
    private boolean hasReopenEnrollmentPermission() {
        return hasGenericPermission(GenericProtocolAuthorizer.REOPEN_PROTOCOL);
    }
    
    private boolean hasReopenEnrollmentUnavailablePermission() {
        return hasGenericUnavailablePermission(GenericProtocolAuthorizer.REOPEN_PROTOCOL);
    }
    
    private boolean hasCloseEnrollmentPermission() {
        return hasGenericPermission(GenericProtocolAuthorizer.CLOSE_ENROLLMENT_PROTOCOL);
    }
    
    private boolean hasCloseEnrollmentUnavailablePermission() {
        return hasGenericUnavailablePermission(GenericProtocolAuthorizer.CLOSE_ENROLLMENT_PROTOCOL);
    }
    
// TODO ********************** overridden during IRB backfit ************************    
    protected boolean hasSuspendPermission() {
        return hasGenericPermission(GenericProtocolAuthorizer.SUSPEND_PROTOCOL);
    }
    
    protected boolean hasSuspendUnavailablePermission() {
        return hasGenericUnavailablePermission(GenericProtocolAuthorizer.SUSPEND_PROTOCOL);
    }
    
// hooks from overridden methods given null impls
    @Override
    protected ProtocolTaskBase getSuspendTaskInstanceHook(ProtocolBase protocol) {
        return null;
    }

    @Override
    protected ProtocolTaskBase getSuspendUnavailableTaskInstanceHook(ProtocolBase protocol) {
        return null;
    }
    
    
    
    private boolean hasSuspendByDsmbPermission() {
        return hasGenericPermission(GenericProtocolAuthorizer.SUSPEND_PROTOCOL_BY_DSMB);
    }
    
    private boolean hasSuspendByDsmbUnavailablePermission() {
        return hasGenericUnavailablePermission(GenericProtocolAuthorizer.SUSPEND_PROTOCOL_BY_DSMB);
    }
    
    private boolean hasClosePermission() {
        return hasGenericPermission(GenericProtocolAuthorizer.CLOSE_PROTOCOL);
    }
    
    private boolean hasCloseUnavailablePermission() {
        return hasGenericUnavailablePermission(GenericProtocolAuthorizer.CLOSE_PROTOCOL);
    }
    
    
    
// TODO ********************** overridden during IRB backfit ************************    
    protected boolean hasExpirePermission() {
        return hasGenericPermission(GenericProtocolAuthorizer.EXPIRE_PROTOCOL);
    }
    
    protected boolean hasExpireUnavailablePermission() {
        return hasGenericUnavailablePermission(GenericProtocolAuthorizer.EXPIRE_PROTOCOL);
    }
    
    protected boolean hasTerminatePermission() {
        return hasGenericPermission(GenericProtocolAuthorizer.TERMINATE_PROTOCOL);
    }
    
    protected boolean hasTerminateUnavailablePermission() {
        return hasGenericUnavailablePermission(GenericProtocolAuthorizer.TERMINATE_PROTOCOL);
    }
    
    
    
    
// hooks from overridden methods given null impls
    
    @Override
    protected ProtocolTaskBase getExpireTaskInstanceHook(ProtocolBase protocol) {
        return null;
    }

    @Override
    protected ProtocolTaskBase getExpireUnavailableTaskInstanceHook(ProtocolBase protocol) {
        return null;
    }
    
    @Override
    protected ProtocolTaskBase getTerminateTaskInstanceHook(ProtocolBase protocol) {
        return null;
    }

    @Override
    protected ProtocolTaskBase getTerminateUnavailableTaskInstanceHook(ProtocolBase protocol) {
        return null;
    }
    

    
    // IRB specific
    private boolean hasPermitDataAnalysisPermission() {
        return hasGenericPermission(GenericProtocolAuthorizer.PERMIT_DATA_ANALYSIS);
    }
    
    // IRB specific
    private boolean hasPermitDataAnalysisUnavailablePermission() {
        return hasGenericUnavailablePermission(GenericProtocolAuthorizer.PERMIT_DATA_ANALYSIS);
    }
   
    
 // TODO ********************** overridden during IRB backfit ************************        
    protected boolean hasAdminCorrectionPermission() {
        return hasPermission(TaskName.PROTOCOL_ADMIN_CORRECTION);
    }
    
    protected boolean hasAdminCorrectionUnavailablePermission() {
        return hasPermission(TaskName.PROTOCOL_ADMIN_CORRECTION_UNAVAILABLE);
    }
    
    protected boolean hasUndoLastActionPermission() {
        return hasPermission(TaskName.PROTOCOL_UNDO_LAST_ACTION) && undoLastActionBean.canUndoLastAction();
    }
    
    protected boolean hasUndoLastActionUnavailablePermission() {
        return hasPermission(TaskName.PROTOCOL_UNDO_LAST_ACTION) && !undoLastActionBean.canUndoLastAction();
    }
    
 // hooks from overridden methods given null impls
    
    @Override
    protected ProtocolTaskBase getAdminCorrectionProtocolTaskInstanceHook(ProtocolBase protocol) {
        return null;
    }

    @Override
    protected ProtocolTaskBase getAdminCorrectionUnavailableProtocolTaskInstanceHook(ProtocolBase protocol) {
        return null;
    }

// TODO ********************** commented out during IRB backfit ************************    
//    private boolean hasRecordCommitteeDecisionPermission() {
//        return hasPermission(TaskName.RECORD_COMMITTEE_DECISION);
//    }
//    
//    private boolean hasRecordCommitteeDecisionUnavailablePermission() {
//        return hasPermission(TaskName.RECORD_COMMITTEE_DECISION_UNAVAILABLE);
//    }
    
    private boolean hasEnterRiskLevelPermission() {
        return hasPermission(TaskName.ENTER_RISK_LEVEL);
    }
    
    private boolean hasDeferPermission() {
        return hasPermission(TaskName.DEFER_PROTOCOL);
    }
    
    private boolean hasDeferUnavailablePermission() {
        return hasPermission(TaskName.DEFER_PROTOCOL_UNAVAILABLE);
    }
    
// TODO ********************** commented out during IRB backfit ************************    
//    private boolean hasManageReviewCommentsPermission() {
//        return hasPermission(TaskName.PROTOCOL_MANAGE_REVIEW_COMMENTS); 
//    }
//    
//    private boolean hasManageReviewCommentsUnavailablePermission() {
//        return hasPermission(TaskName.PROTOCOL_MANAGE_REVIEW_COMMENTS_UNAVAILABLE); 
//    }
    
    private boolean hasApproveOtherPermission() {
        ProtocolTask task = new ProtocolTask(TaskName.PROTOCOL_APPROVE_OTHER, (Protocol) getProtocol());
        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
    }
    
    private boolean hasManageNotesPermission() {
        ProtocolTask task = new ProtocolTask(TaskName.PROTOCOL_MANAGE_NOTES, (Protocol) getProtocol());
        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
    }
    
    private boolean hasManageNotesUnavailablePermission() {
        ProtocolTask task = new ProtocolTask(TaskName.PROTOCOL_MANAGE_NOTES_UNAVAILABLE, (Protocol) getProtocol());
        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
    }

// TODO ********************** commented out during IRB backfit ************************    
//    private boolean hasAbandonProtocolPermission() {
////        return hasGenericPermission(GenericProtocolAuthorizer.ABANDON_PROTOCOL);
//        ProtocolTask task = new ProtocolTask(TaskName.ABANDON_PROTOCOL, getProtocol());
//        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
//    }
    
    protected boolean hasPermission(String taskName) {
        ProtocolTask task = new ProtocolTask(taskName, (Protocol) getProtocol());
        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
    }
    
    private boolean hasGenericPermission(String genericActionName) {
        ProtocolTask task = new ProtocolTask(TaskName.GENERIC_PROTOCOL_ACTION, (Protocol) getProtocol(), genericActionName);
        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
    }
    
    private boolean hasGenericUnavailablePermission(String genericActionName) {
        ProtocolTask task = new ProtocolTask(TaskName.GENERIC_PROTOCOL_ACTION_UNAVAILABLE, (Protocol) getProtocol(), genericActionName);
        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
    }
    
    private boolean hasIrbAcknowledgementPermission() {
        ProtocolTask task = new ProtocolTask(TaskName.IRB_ACKNOWLEDGEMENT, (Protocol) getProtocol());
        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
    }
    
    private boolean hasIrbAcknowledgementUnavailablePermission() {
        ProtocolTask task = new ProtocolTask(TaskName.IRB_ACKNOWLEDGEMENT_UNAVAILABLE, (Protocol) getProtocol());
        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
    }

// TODO ********************** commented out during IRB backfit ************************    
//    private boolean hasCanModifySubmissionPermission() {
//        ProtocolTask task = new ProtocolTask(TaskName.MODIFY_PROTOCOL_SUBMISSION, getProtocol());
//        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
//    }
//    
//    private boolean hasCanModifySubmissionUnavailablePermission() {
//        ProtocolTask task = new ProtocolTask(TaskName.MODIFY_PROTOCOL_SUBMISSION_UNAVAILABLE, getProtocol());
//        return getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
//    }
    
    private boolean hasReviewNotRequiredPermission() {
        ProtocolTask task = new ProtocolTask(TaskName.PROTOCOL_REVIEW_NOT_REQUIRED, (Protocol) getProtocol());
        boolean retVal = getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
        return retVal;
    }
    
    private boolean hasReviewNotRequiredUnavailablePermission() {
        ProtocolTask task = new ProtocolTask(TaskName.PROTOCOL_REVIEW_NOT_REQUIRED_UNAVAILABLE, (Protocol) getProtocol());
        boolean retVal = getTaskAuthorizationService().isAuthorized(getUserIdentifier(), task);
        return retVal;
    }
    
// TODO ********************** commented out during IRB backfit ************************    
//    private boolean hasFollowupAction(String actionCode) {
//        for (ValidProtocolActionAction action : followupActionActions) {
//            if (StringUtils.equals(action.getFollowupActionCode(),actionCode)) {
//                return true;
//            }
//        }
//        return false;
//    }
//    
//	private boolean hasCanViewOnlineReviewersPermission() {
//        return getReviewerCommentsService().canViewOnlineReviewers(getUserIdentifier(), getSelectedSubmission());
//    }
//    
//    private boolean hasCanViewOnlineReviewerCommentsPermission() {
//        return getReviewerCommentsService().canViewOnlineReviewerComments(getUserIdentifier(), getSelectedSubmission());
//    }
    
    private boolean hasCloseRequestLastAction() {
        return ProtocolActionType.REQUEST_TO_CLOSE.equals(getLastPerformedAction().getProtocolActionTypeCode());
    }
    
    private boolean hasCloseEnrollmentRequestLastAction() {
        return ProtocolActionType.REQUEST_TO_CLOSE_ENROLLMENT.equals(getLastPerformedAction().getProtocolActionTypeCode());
    }
    
    private boolean hasDataAnalysisRequestLastAction() {
        return ProtocolActionType.REQUEST_FOR_DATA_ANALYSIS_ONLY.equals(getLastPerformedAction().getProtocolActionTypeCode());
    }
    
    private boolean hasReopenEnrollmentRequestLastAction() {
        return ProtocolActionType.REQUEST_TO_REOPEN_ENROLLMENT.equals(getLastPerformedAction().getProtocolActionTypeCode());
    }
    
    private boolean hasSuspendRequestLastAction() {
        return ProtocolActionType.REQUEST_FOR_SUSPENSION.equals(getLastPerformedAction().getProtocolActionTypeCode());
    }
    
    private boolean hasTerminateRequestLastAction() {
        return ProtocolActionType.REQUEST_FOR_TERMINATION.equals(getLastPerformedAction().getProtocolActionTypeCode());
    }

// TODO ********************** commented out during IRB backfit ************************    
//    private TaskAuthorizationService getTaskAuthorizationService() {
//        if (this.taskAuthorizationService == null) {
//            this.taskAuthorizationService = KraServiceLocator.getService(TaskAuthorizationService.class);        
//        }
//        return this.taskAuthorizationService;
//    }
//    
//    public ProtocolSubmitAction getProtocolSubmitAction() {
//        return protocolSubmitAction;
//    }
//
//    public ProtocolForm getProtocolForm() {
//        return form;
//    }

    
    public Protocol getProtocol() {
        return (Protocol) super.getProtocol();
    }

    
// TODO ********************** commented out during IRB backfit ************************    
//    public boolean getCanSubmitProtocol() {
//        return canSubmitProtocol;
//    }
//    
//    public boolean getCanSubmitProtocolUnavailable() {
//        return canSubmitProtocolUnavailable;
//    }
//    /**
//     * Get the userName of the user for the current session.
//     * @return the current session's userName
//     */
//    private String getUserIdentifier() {
//         return GlobalVariables.getUserSession().getPrincipalId();
//    }
//
//    public String getSubmissionConstraint() {
//        return submissionConstraint;
//    }
//
//    public ProtocolWithdrawBean getProtocolWithdrawBean() {
//        return protocolWithdrawBean;
//    }
//    
//    public ProtocolRequestBean getProtocolCloseRequestBean() {
//        return protocolCloseRequestBean;
//    }
//
//    public ProtocolRequestBean getProtocolSuspendRequestBean() {
//        return protocolSuspendRequestBean;
//    }
    
    public ProtocolRequestBean getProtocolCloseEnrollmentRequestBean() {
        return protocolCloseEnrollmentRequestBean;
    }

    public ProtocolRequestBean getProtocolReOpenEnrollmentRequestBean() {
        return protocolReOpenEnrollmentRequestBean;
    }
    
    public ProtocolRequestBean getProtocolDataAnalysisRequestBean() {
        return protocolDataAnalysisRequestBean;
    }

// TODO ********************** commented out during IRB backfit ************************    
//    public ProtocolRequestBean getProtocolTerminateRequestBean(){
//        return this.protocolTerminateRequestBean;
//    }
    
    public ProtocolNotifyIrbBean getProtocolNotifyIrbBean() {
        return protocolNotifyIrbBean;
    }
    
// TODO ********************** commented out during IRB backfit ************************    
//    public ProtocolNotifyCommitteeBean getProtocolNotifyCommitteeBean() {
//        return protocolNotifyCommitteeBean;
//    }
//    
//    public ProtocolAmendmentBean getProtocolAmendmentBean() {
//        return protocolAmendmentBean;
//    }
//    
//    public void setProtocolAmendmentBean(ProtocolAmendmentBean protocolAmendmentBean) {
//        this.protocolAmendmentBean = protocolAmendmentBean;
//    }
//    
//    public ProtocolAmendmentBean getProtocolRenewAmendmentBean() {
//        return protocolRenewAmendmentBean;
//    }
//    
//    public ProtocolDeleteBean getProtocolDeleteBean() {
//        return protocolDeleteBean;
//    }
//    
//    public ProtocolAssignToAgendaBean getAssignToAgendaBean(){
//        return this.assignToAgendaBean;
//    }
    
    public ProtocolAssignCmtSchedBean getAssignCmtSchedBean() {
        return assignCmtSchedBean;
    }
    
    public ProtocolAssignReviewersBean getProtocolAssignReviewersBean() {
        return protocolAssignReviewersBean;
    }
                           
    public ProtocolGrantExemptionBean getProtocolGrantExemptionBean() {
        return protocolGrantExemptionBean;
    }

// TODO ********************** commented out during IRB backfit ************************    
//    public ProtocolApproveBean getProtocolFullApprovalBean() {
//        return protocolFullApprovalBean;
//    }

    public ProtocolApproveBean getProtocolExpeditedApprovalBean() {
        return protocolExpeditedApprovalBean;
    }
    
    public ProtocolApproveBean getProtocolResponseApprovalBean() {
        return protocolResponseApprovalBean;
    }
    
// TODO ********************** commented out during IRB backfit ************************    
//    public ProtocolGenericActionBean getProtocolDisapproveBean() {
//        return protocolDisapproveBean;
//    }
//    
//    public ProtocolGenericActionBean getProtocolSMRBean() {
//        return protocolSMRBean;
//    }
//    
//    public ProtocolGenericActionBean getProtocolSRRBean() {
//        return protocolSRRBean;
//    }
    
    public ProtocolGenericActionBean getProtocolReopenEnrollmentBean() {
        return protocolReopenEnrollmentBean;
    }
    
    public ProtocolGenericActionBean getProtocolCloseEnrollmentBean() {
        return protocolCloseEnrollmentBean;
    }
    
// TODO ********************** commented out during IRB backfit ************************    
//    public ProtocolGenericActionBean getProtocolSuspendBean() {
//        return protocolSuspendBean;
//    }
    
    public ProtocolGenericActionBean getProtocolSuspendByDsmbBean() {
        return protocolSuspendByDsmbBean;
    }
    
// TODO ********************** commented out during IRB backfit ************************    
//    public ProtocolGenericActionBean getProtocolCloseBean() {
//        return protocolCloseBean;
//    }
//    
//    public ProtocolGenericActionBean getProtocolExpireBean() {
//        return protocolExpireBean;
//    }
//    
//    public ProtocolGenericActionBean getProtocolTerminateBean() {
//        return protocolTerminateBean;
//    }
    
    public ProtocolGenericActionBean getProtocolPermitDataAnalysisBean() {
        return protocolPermitDataAnalysisBean;
    }
    
    public ProtocolGenericActionBean getProtocolIrbAcknowledgementBean() {
        return protocolIrbAcknowledgementBean;
    }

// TODO ********************** commented out during IRB backfit ************************    
//    public AdminCorrectionBean getProtocolAdminCorrectionBean() {
//        return protocolAdminCorrectionBean;
//    }
    
    public UndoLastActionBean getUndoLastActionBean() {
        if(null != undoLastActionBean) {
            undoLastActionBean.refreshActionsPerformed();
        }
        return undoLastActionBean;
    }

// TODO ********************** commented out during IRB backfit ************************    
//    public CommitteeDecision getCommitteeDecision() {
//        return committeeDecision;
//    }
    
    public ProtocolModifySubmissionBean getProtocolModifySubmissionBean() {
        return this.protocolModifySubmissionBean;
    }
    
// TODO ********************** commented out during IRB backfit ************************    
//    public ProtocolGenericActionBean getProtocolManageReviewCommentsBean() {
//        return protocolManageReviewCommentsBean;
//    }
//
//    public boolean getCanCreateAmendment() {
//        return canCreateAmendment;
//    }
//    
//    public boolean getCanCreateAmendmentUnavailable() {
//        return canCreateAmendmentUnavailable;
//    }
//    
//    public boolean getCanModifyAmendmentSections() {
//        return canModifyAmendmentSections;
//    }
//
//    public boolean getCanModifyAmendmentSectionsUnavailable() {
//        return canModifyAmendmentSectionsUnavailable;
//    }
//
//    public boolean getCanCreateRenewal() {
//        return canCreateRenewal;
//    }
//
//    public boolean getCanCreateRenewalUnavailable() {
//        return canCreateRenewalUnavailable;
//    }
    
    public boolean getCanNotifyIrb() {
        return canNotifyIrb;
    }
    
    public boolean getCanNotifyIrbUnavailable() {
        return canNotifyIrbUnavailable;
    }
    
    
// TODO ********************** commented out during IRB backfit ************************    
//    public boolean getCanNotifyCommittee() {
//        return canNotifyCommittee;
//    }
//    
//    public boolean getCanNotifyCommitteeUnavailable() {
//        return canNotifyCommitteeUnavailable;
//    }
//    
//    public boolean getCanWithdraw() {
//        return canWithdraw;
//    }
//    
//    public boolean getCanWithdrawUnavailable() {
//        return canWithdrawUnavailable;
//    }
    
    
    public boolean getCanRequestClose() {
        return canRequestClose;
    }
    
    public boolean getCanRequestCloseUnavailable() {
        return canRequestCloseUnavailable;
    }
    
// TODO ********************** commented out during IRB backfit ************************    
//    public boolean getCanRequestSuspension() {
//        return canRequestSuspension;
//    }
//    
//    public boolean getCanRequestSuspensionUnavailable() {
//        return canRequestSuspensionUnavailable;
//    }
    
    public boolean getCanRequestCloseEnrollment() {
        return canRequestCloseEnrollment;
    }
    
    public boolean getCanRequestCloseEnrollmentUnavailable() {
        return canRequestCloseEnrollmentUnavailable;
    }
    
    public boolean getCanRequestReOpenEnrollment() {
        return canRequestReOpenEnrollment;
    }
    
    public boolean getCanRequestReOpenEnrollmentUnavailable() {
        return canRequestReOpenEnrollmentUnavailable;
    }
    
    public boolean getCanRequestDataAnalysis() {
        return canRequestDataAnalysis;
    }
    
    public boolean getCanRequestDataAnalysisUnavailable() {
        return canRequestDataAnalysisUnavailable;
    }
    
// TODO ********************** commented out during IRB backfit ************************    
//    public boolean getcanRequestTerminate(){
//        return this.canRequestTerminate;
//    }
//    
//    public boolean getcanRequestTerminateUnavailable(){
//        return this.canRequestTerminateUnavailable;
//    }
//    
//    public boolean getCanDeleteProtocolAmendRenew() {
//        return canDeleteProtocolAmendRenew;
//    }
//    
//    public boolean getCanDeleteProtocolAmendRenewUnavailable() {
//        return canDeleteProtocolAmendRenewUnavailable;
//    }
//    
//    public boolean getCanAssignToAgenda() {
//        return canAssignToAgenda;
//    }
//    
//    public boolean getCanAssignToAgendaUnavailable() {
//        return canAssignToAgendaUnavailable;
//    }
//    
//    public boolean getCanAssignCmtSched() {
//        return canAssignCmtSched;
//    }
//    
//    public boolean getCanAssignCmtSchedUnavailable() {
//        return canAssignCmtSchedUnavailable;
//    }
//    
//    public boolean getCanAssignReviewers() {
//        return canAssignReviewers;
//    }
//    
//    public boolean getCanAssignReviewersUnavailable() {
//        return canAssignReviewersUnavailable;
//    }
//    
//    public boolean getCanAssignReviewersCmtSel() {
//        return canAssignReviewersCmtSel;
//    }
    
    public boolean getCanGrantExemption() {
        return canGrantExemption;
    }
    
    public boolean getCanGrantExemptionUnavailable() {
        return canGrantExemptionUnavailable;
    }
    
// TODO ********************** commented out during IRB backfit ************************    
//    public boolean getCanApproveFull() {
//        return canApproveFull;
//    }
//    
//    public boolean getCanApproveFullUnavailable() {
//        return canApproveFullUnavailable;
//    }
    
    public boolean getCanApproveExpedited() {
        return canApproveExpedited;
    }
    
    public boolean getCanApproveExpeditedUnavailable() {
        return canApproveExpeditedUnavailable;
    }
    
    
// TODO ********************** commented out during IRB backfit ************************    
//    public boolean getCanApproveResponse() {
//        return canApproveResponse;
//    }
//    
//    public boolean getCanApproveResponseUnavailable() {
//        return canApproveResponseUnavailable;
//    }
//    
//    public boolean getCanDisapprove() {
//        return canDisapprove;
//    }
//    
//    public boolean getCanDisapproveUnavailable() {
//        return canDisapproveUnavailable;
//    }
//    
//    public boolean getCanReturnForSMR() {
//        return canReturnForSMR;
//    }
//    
//    public boolean getCanReturnForSMRUnavailable() {
//        return canReturnForSMRUnavailable;
//    }
//    
//    public boolean getCanReturnForSRR() {
//        return canReturnForSRR;
//    }
//    
//    public boolean getCanReturnForSRRUnavailable() {
//        return canReturnForSRRUnavailable;
//    }
    
    public boolean getCanReopenEnrollment() {
        return canReopenEnrollment;
    }
    
    public boolean getCanReopenEnrollmentUnavailable() {
        return canReopenEnrollmentUnavailable;
    }
    
    public boolean getCanCloseEnrollment() {
        return canCloseEnrollment;
    }
    
    public boolean getCanCloseEnrollmentUnavailable() {
        return canCloseEnrollmentUnavailable;
    }
    
// TODO ********************** commented out during IRB backfit ************************    
//    public boolean getCanSuspend() {
//        return canSuspend;
//    }
//    
//    public boolean getCanSuspendUnavailable() {
//        return canSuspendUnavailable;
//    }
    
    public boolean getCanSuspendByDsmb() {
        return canSuspendByDsmb;
    }
    
    public boolean getCanSuspendByDsmbUnavailable() {
        return canSuspendByDsmbUnavailable;
    }
    
// TODO ********************** commented out during IRB backfit ************************    
//    public boolean getCanClose() {
//        return canClose;
//    }
//    
//    public boolean getCanCloseUnavailable() {
//        return canCloseUnavailable;
//    }
//    
//    public boolean getCanExpire() {
//        return canExpire;
//    }
//    
//    public boolean getCanExpireUnavailable() {
//        return canExpireUnavailable;
//    }
//    
//    public boolean getCanTerminate() {
//        return canTerminate;
//    }
//    
//    public boolean getCanTerminateUnavailable() {
//        return canTerminateUnavailable;
//    }
    
    public boolean getCanPermitDataAnalysis() {
        return canPermitDataAnalysis;
    }
    
    public boolean getCanPermitDataAnalysisUnavailable() {
        return canPermitDataAnalysisUnavailable;
    }
    
    public boolean getCanEnterRiskLevel() {
        return canEnterRiskLevel;
    }
    
// TODO ********************** commented out during IRB backfit ************************    
//    public boolean getCanMakeAdminCorrection() {
//        return canMakeAdminCorrection;
//    }
//    
//    public boolean getCanMakeAdminCorrectionUnavailable() {
//        return canMakeAdminCorrectionUnavailable;
//    }
//    
//    public boolean getCanUndoLastAction() {
//        return canUndoLastAction;
//    }
//    
//    public boolean getCanUndoLastActionUnavailable() {
//        return canUndoLastActionUnavailable;
//    }
//    
//    public boolean getCanRecordCommitteeDecision() {
//        return canRecordCommitteeDecision;
//    }
//    
//    public boolean getCanRecordCommitteeDecisionUnavailable() {
//        return canRecordCommitteeDecisionUnavailable;
//    }
//    
//    public boolean getCanModifyProtocolSubmission() {
//        return this.canModifyProtocolSubmission;
//    }
//    
//    public boolean getCanModifyProtocolSubmissionUnavailable() {
//        return this.canModifyProtocolSubmissionUnavailable;
//    }
    
    public boolean getCanIrbAcknowledgement() {
        return canIrbAcknowledgement;
    }
    
    public boolean getCanIrbAcknowledgementUnavailable() {
        return canIrbAcknowledgementUnavailable;
    }
    
    public boolean getCanDefer() {
        return canDefer;
    }
    
    public boolean getCanDeferUnavailable() {
        return canDeferUnavailable;
    }
    
    public boolean getCanReviewNotRequired() {
        return this.canReviewNotRequired;
    }

    public boolean getCanReviewNotRequiredUnavailable() {
        return this.canReviewNotRequiredUnavailable;
    }

// TODO ********************** commented out during IRB backfit ************************    
//    public boolean getCanManageReviewComments() {  
//        return canManageReviewComments;
//    }
//    
//    public boolean getCanManageReviewCommentsUnavailable() {  
//        return canManageReviewCommentsUnavailable;
//    }
//    
//    public boolean getCanApproveOther() {
//        return canApproveOther;
//    }
//    
//    public boolean getCanManageNotes() {
//        return canManageNotes;
//    }
//
//    public boolean getCanManageNotesUnavailable() {
//        return canManageNotesUnavailable;
//    }
//
//    public boolean getIsApproveOpenForFollowup() {
//        return hasFollowupAction(ProtocolActionType.APPROVED);
//    }
//    
//    public boolean getIsDisapproveOpenForFollowup() {
//        return hasFollowupAction(ProtocolActionType.DISAPPROVED);
//    }
//    
//    public boolean getIsReturnForSMROpenForFollowup() {
//        return hasFollowupAction(ProtocolActionType.SPECIFIC_MINOR_REVISIONS_REQUIRED);
//    }
//    
//    public boolean getIsReturnForSRROpenForFollowup() {
//        return hasFollowupAction(ProtocolActionType.SUBSTANTIVE_REVISIONS_REQUIRED);
//    }
//    
//    /**
//     * 
//     * This method is one of the criteria to decide if there is a followup action for requested action panel to open
//     * @return
//     */
//    public boolean isOpenForFollowup() {
//        return getIsApproveOpenForFollowup() || getIsDisapproveOpenForFollowup() || getIsReturnForSMROpenForFollowup() || getIsReturnForSRROpenForFollowup();
//    }
//    
//    public Map<String,Boolean> getFollowupActionMap() {
//        return followupActionMap;
//    }
//    
//    public boolean getCanViewOnlineReviewers() {
//        return canViewOnlineReviewers;
//    }
//    
//    public boolean getCanViewOnlineReviewerComments() {
//        return canViewOnlineReviewerComments;
//    }

    public boolean getCanAddCloseReviewerComments() {
        return canAddCloseReviewerComments;
    }

    public boolean getCanAddCloseEnrollmentReviewerComments() {
        return canAddCloseEnrollmentReviewerComments;
    }

    public boolean getCanAddDataAnalysisReviewerComments() {
        return canAddDataAnalysisReviewerComments;
    }

    public boolean getCanAddReopenEnrollmentReviewerComments() {
        return canAddReopenEnrollmentReviewerComments;
    }

// TODO ********************** commented out during IRB backfit ************************    
//    public boolean getCanAddSuspendReviewerComments() {
//        return canAddSuspendReviewerComments;
//    }
//
//    public boolean getCanAddTerminateReviewerComments() {
//        return canAddTerminateReviewerComments;
//    }

    public void setPrintTag(String printTag) {
        this.printTag = printTag;
    }
    
    public String getPrintTag() {
        return printTag;
    }
 
    
// TODO ********************** commented out during IRB backfit ************************    
//    public ProtocolSummary getProtocolSummary() {
//        return protocolSummary;
//    }
//    
//    public ProtocolSummary getPrevProtocolSummary() {
//        return prevProtocolSummary;
//    }
//    
//    public void setSelectedHistoryItem(String selectedHistoryItem) {
//        this.selectedHistoryItem = selectedHistoryItem;
//    }
//    
//    public String getSelectedHistoryItem() {
//        return selectedHistoryItem;
//    }
//    
//    public Date getFilteredHistoryStartDate() {
//        return filteredHistoryStartDate;
//    }
//    
//    public void setFilteredHistoryStartDate(Date filteredHistoryStartDate) {
//        this.filteredHistoryStartDate = filteredHistoryStartDate;
//    }
//    
//    public Date getFilteredHistoryEndDate() {
//        return filteredHistoryEndDate;
//    }
//    
//    public void setFilteredHistoryEndDate(Date filteredHistoryEndDate) {
//        this.filteredHistoryEndDate = filteredHistoryEndDate;
//    }
//    
//    public ProtocolAction getLastPerformedAction() {
//        List<ProtocolAction> protocolActions = form.getProtocolDocument().getProtocol().getProtocolActions();
//        Collections.sort(protocolActions, new Comparator<ProtocolAction>() {
//            public int compare(ProtocolAction action1, ProtocolAction action2) {
//                return action2.getActualActionDate().compareTo(action1.getActualActionDate());
//            }
//        });
//     
//        return protocolActions.size() > 0 ? protocolActions.get(0) : null;
//    }
    
    
    
 // TODO ********************** OVERRIDDEN during IRB backfit ************************    
    /**
     * Prepares all protocol actions for being filtered by setting their isInFilterView attribute.
     */
    public void initFilterDatesView() {
        java.util.Date dayBeforeStartDate = null;
        java.util.Date dayAfterEndDate = null;

        if (filteredHistoryStartDate != null && filteredHistoryEndDate != null) {
            dayBeforeStartDate = DateUtils.addDays(filteredHistoryStartDate, -1);
            dayAfterEndDate = DateUtils.addDays(filteredHistoryEndDate, 1);
        }

        for (ProtocolActionBase protocolAction : getSortedProtocolActions()) {
            Timestamp actionDate = protocolAction.getActionDate();
            if (dayBeforeStartDate != null && dayAfterEndDate != null) {
                protocolAction.setIsInFilterView(actionDate.after(dayBeforeStartDate) && actionDate.before(dayAfterEndDate));
            }
            else {
                protocolAction.setIsInFilterView(true);
            }
            if (protocolAction.getIsInFilterView()) {
                ((ProtocolAction) protocolAction).setQuestionnairePrintOptionFromHelper(this);
            }
        }
    }
    
    // All the following methods were refactored into the ProtocolAction, where they belong
    
    /*
     * check if this particular ProtocolAction has any questionnaire associated with it.
     */
/*    private void checkQuestionnaire(ProtocolAction protocolAction) {
        if (protocolAction.getSubmissionNumber() != null
                && !ProtocolActionType.SUBMIT_TO_IRB.equals(protocolAction.getProtocolActionTypeCode())) {
            protocolAction.setAnswerHeadersCount(getAnswerHeaderCount(CoeusSubModule.PROTOCOL_SUBMISSION, Integer
                    .toString(protocolAction.getSubmissionNumber())));
             if (protocolAction.getAnswerHeadersCount() > 0) {
                protocolAction.setQuestionnairePrintOption(getQnPrintOptionForAction(protocolAction.getProtocolNumber(),
                        protocolAction.getSubmissionNumber().toString(), CoeusSubModule.PROTOCOL_SUBMISSION, protocolAction));
            }
        } else if (ProtocolActionType.SUBMIT_TO_IRB.equals(protocolAction.getProtocolActionTypeCode())
                && ("Submitted to IRB").equals(protocolAction.getComments())) {
            if (protocolAction.getProtocol().isAmendment() || protocolAction.getProtocol().isRenewal()) {
                protocolAction.setQuestionnairePrintOption(getQnPrintOptionForAction(protocolAction.getProtocolNumber(),
                        protocolAction.getSequenceNumber().toString(), CoeusSubModule.AMENDMENT_RENEWAL, protocolAction));

            } else {
                protocolAction.setQuestionnairePrintOption(getQnPrintOptionForAction(protocolAction.getProtocolNumber(),
                        getInitialSequence(protocolAction, ""), CoeusSubModule.ZERO_SUBMODULE, protocolAction));
            }
        } else if (ProtocolActionType.SUBMIT_TO_IRB.equals(protocolAction.getProtocolActionTypeCode())
                && StringUtils.isNotBlank(protocolAction.getComments())
                && (protocolAction.getComments().startsWith("Amendment-") || protocolAction.getComments().startsWith("Renewal-"))) {
            String amendmentRenewalNumber = getAmendmentRenewalNumber(protocolAction.getComments());
            protocolAction.setQuestionnairePrintOption(getQnPrintOptionForAction(protocolAction.getProtocolNumber()
                    + amendmentRenewalNumber, getInitialSequence(protocolAction, amendmentRenewalNumber),
                    CoeusSubModule.AMENDMENT_RENEWAL, protocolAction));
         }
    }
*/

    /*
     * set up questionnaire option for UI view button
     */
/*    QuestionnairePrintOption getQnPrintOptionForAction(String itemKey, String subItemKey, String subItemCode, ProtocolAction protocolAction) {

        protocolAction.setAnswerHeadersCount(getAnswerHeaderCount(subItemCode, itemKey, subItemKey));
        if (protocolAction.getAnswerHeadersCount() > 0) {
            QuestionnairePrintOption qnPrintOption = new QuestionnairePrintOption();
            qnPrintOption.setItemKey(itemKey);
            qnPrintOption.setSubItemCode(subItemCode);
            qnPrintOption.setSubItemKey(subItemKey);
            return qnPrintOption;
        } else {
            return null;
        }
    }
    
    public int getAnswerHeaderCount(String moduleSubItemCode, String moduleItemKey, String moduleSubItemKey) {
        Map<String, String> fieldValues = new HashMap<String, String>();
        fieldValues.put("moduleItemCode", CoeusModule.IRB_MODULE_CODE);
        fieldValues.put("moduleItemKey", moduleItemKey);
        if (!moduleItemKey.contains("A") && !moduleItemKey.contains("R") && !getProtocol().isAmendment() && !getProtocol().isRenewal()) {
            fieldValues.put("moduleSubItemCode", moduleSubItemCode);
        }
        fieldValues.put("moduleSubItemKey", moduleSubItemKey);
        return getBusinessObjectService().countMatching(AnswerHeader.class, fieldValues);
        
    }
*/
    
    /*
     * utility method to get amend or renewal number
     */
/*  private String getAmendmentRenewalNumber(String comment) {
        String retVal="";
        if (comment.startsWith("Amendment-")) {
            retVal = "A" + comment.substring(10, 13);
        } else {
            retVal = "R" + comment.substring(8, 11);
                     
        }
        return retVal;
    }
*/    

    /*
     * get the sequence number of the protocol that the action initially created
     */
/*  private String getInitialSequence(ProtocolAction protocolAction, String amendmentRenewalNumber) {
        Map<String, String> fieldValues = new HashMap<String, String>();
        fieldValues.put("protocolNumber", protocolAction.getProtocolNumber() + amendmentRenewalNumber);
        if (StringUtils.isBlank(amendmentRenewalNumber)) {
            fieldValues.put("actionId", protocolAction.getActionId().toString());
        }
        else {
            fieldValues.put("submissionNumber", protocolAction.getSubmissionNumber().toString());
        }
        fieldValues.put("protocolActionTypeCode", ProtocolActionType.SUBMIT_TO_IRB);
        return ((List<ProtocolAction>) getBusinessObjectService().findMatchingOrderBy(ProtocolAction.class, fieldValues,
                "protocolActionId", true)).get(0).getProtocol().getSequenceNumber().toString();
    }
*/

// TODO ********************** OVERRIDDEN during IRB backfit ************************    
    /**
     * Prepares, sorts, and returns a list of protocol actions.
     * @return
     */
    public List<ProtocolActionBase> getSortedProtocolActions() {
        List<ProtocolAction> protocolActions = new ArrayList<ProtocolAction>();
        for (ProtocolActionBase protocolAction : form.getProtocolDocument().getProtocol().getProtocolActions()) {
            if (protocolAction.getSubmissionNumber() != null && ACTION_TYPE_SUBMISSION_DOC.contains(protocolAction.getProtocolActionTypeCode())) {
                protocolAction.setProtocolSubmissionDocs(new ArrayList<ProtocolSubmissionDocBase>(getSubmissionDocs(protocolAction)));
            }
            protocolActions.add((ProtocolAction) protocolAction);
        }
        
        Collections.sort(protocolActions, new Comparator<ProtocolAction>() {
            public int compare(ProtocolAction action1, ProtocolAction action2) {
                return action2.getActualActionDate().compareTo(action1.getActualActionDate());
            }
        });
     
        return (List)protocolActions;
    }
    
// TODO ********************** commented out during IRB backfit ************************    
//    private Collection<ProtocolSubmissionDoc> getSubmissionDocs(ProtocolAction protocolAction) {
//        Map<String, Object> fieldValues = new HashMap<String, Object>();
//        fieldValues.put("protocolNumber", protocolAction.getProtocolNumber());
//        fieldValues.put("submissionNumber", protocolAction.getSubmissionNumber());
//        return getBusinessObjectService().findMatchingOrderBy(ProtocolSubmissionDoc.class, fieldValues, "documentId", true);
//    }
//    
//    public ProtocolAction getSelectedProtocolAction() {
//        for (ProtocolAction action : getProtocol().getProtocolActions()) {
//            if (StringUtils.equals(action.getProtocolActionId().toString(), selectedHistoryItem)) {
//                return action;
//            }
//        }
//        return null;
//    }
//    
//    public void setCurrentSequenceNumber(int currentSequenceNumber) {
//        this.currentSequenceNumber = currentSequenceNumber;
//    }
//    
//    public int getCurrentSequenceNumber() {
//        return currentSequenceNumber;
//    }
//    
//    public int getSequenceCount() {
//        return getProtocol().getSequenceNumber()  + 1;
//    }
//    
//    /**
//     * Looks up and returns the ParameterService.
//     * @return the parameter service. 
//     */
//    protected ParameterService getParameterService() {
//        if (this.parameterService == null) {
//            this.parameterService = KraServiceLocator.getService(ParameterService.class);        
//        }
//        return this.parameterService;
//    }
//
//    /**
//     * Finds and returns the current selection based on the currentSubmissionNumber.
//     * 
//     * If the currentSubmissionNumber is invalid, it will return the current protocol's latest submission (which is always non-null); otherwise, it will get
//     * the submission from the protocol based on the currentSubmissionNumber.
//     * @return the currently selected submission
//     */
//    public ProtocolSubmission getSelectedSubmission() {
//        ProtocolSubmission protocolSubmission = null;
//        
//        if (currentSubmissionNumber <= 0) {
//            protocolSubmission = getProtocol().getProtocolSubmission();
//        } else if (currentSubmissionNumber > 0) {
//            // For amendment/renewal, the submission number are not starting at 1
//            //protocolSubmission = getProtocol().getProtocolSubmissions().get(currentSubmissionNumber - 1);
//            for (ProtocolSubmission submission : getProtocol().getProtocolSubmissions()) {
//                if (submission.getSubmissionNumber().intValue() == currentSubmissionNumber) {
//                    protocolSubmission = submission;
//                    break;
//                }
//            }
//            if (protocolSubmission == null) {
//                // undo last action may remove the last submission; so it can't be found
//                protocolSubmission = getProtocol().getProtocolSubmission();
//                currentSubmissionNumber = protocolSubmission.getSubmissionNumber();
//            }
//        }
//        
//        return protocolSubmission;
//    }
//  
//    private CommitteeService getCommitteeService() {
//        return KraServiceLocator.getService(CommitteeService.class);
//    }
//
//
//    public List<CommitteeScheduleMinute> getReviewComments() {
//        return reviewComments;
//    }
//
//    private void setReviewComments(List<CommitteeScheduleMinute> reviewComments) {
//        this.reviewComments = reviewComments;
//    }
//
//
//    public List<ProtocolVoteAbstainee> getAbstainees() {
//        return abstainees;
//    }
//
//
//    public void setAbstainees(List<ProtocolVoteAbstainee> abstainees) {
//        this.abstainees = abstainees;
//    }
//    
//    public BusinessObjectService getBusinessObjectService() {
//        if (businessObjectService == null) {
//            businessObjectService = KraServiceLocator.getService(BusinessObjectService.class);
//        }
//        return businessObjectService;
//    }
//    
//    public FollowupActionService getFollowupActionService() {
//        if (followupActionService == null) {
//            followupActionService = KraServiceLocator.getService(FollowupActionService.class);
//        }
//        return followupActionService;
//    }
//    
//    private ReviewCommentsService getReviewerCommentsService() {
//        return KraServiceLocator.getService(ReviewCommentsService.class);
//    }
    
    protected CommitteeDecisionService getCommitteeDecisionService() {
        return KraServiceLocator.getService("protocolCommitteeDecisionService");
    }
    
// TODO ********************** commented out during IRB backfit ************************    
//    protected KcPersonService getKcPersonService() {
//        if (this.kcPersonService == null) {
//            this.kcPersonService = KraServiceLocator.getService(KcPersonService.class);
//        }
//        
//        return this.kcPersonService;
//    }
//    
//    public int getCurrentSubmissionNumber() {
//        return currentSubmissionNumber;
//    }
//
//    public void setCurrentSubmissionNumber(int currentSubmissionNumber) {
//        this.currentSubmissionNumber = currentSubmissionNumber;
//    }
    
    public int getTotalSubmissions() {
        return getProtocolSubmitActionService().getTotalSubmissions((Protocol) getProtocol());
    }
    
// TODO ********************** commented out during IRB backfit ************************    
//    /**
//     * Sets up the summary details subpanel.
//     * @throws Exception 
//     */
//    public void initSummaryDetails() throws Exception {
//        if (currentSequenceNumber == -1) {
//            currentSequenceNumber = getProtocol().getSequenceNumber();
//        } else if (currentSequenceNumber > getProtocol().getSequenceNumber()) {
//            currentSequenceNumber = getProtocol().getSequenceNumber();
//        }
//        
//        protocolSummary =  null;
//        String protocolNumber = getProtocol().getProtocolNumber();
//        Protocol protocol = getProtocolVersionService().getProtocolVersion(protocolNumber, currentSequenceNumber);
//        if (protocol != null) {
//            protocolSummary = protocol.getProtocolSummary();
//        }
//        
//        prevProtocolSummary = null;
//        if (currentSequenceNumber > 0) {
//            protocol = getProtocolVersionService().getProtocolVersion(protocolNumber, currentSequenceNumber - 1);
//            if (protocol != null) {
//                prevProtocolSummary = protocol.getProtocolSummary();
//            }
//        }
//        
//        if (protocolSummary != null && prevProtocolSummary != null) {
//            protocolSummary.compare(prevProtocolSummary);
//            prevProtocolSummary.compare(protocolSummary);
//        }
//
//        setSummaryQuestionnaireExist(hasAnsweredQuestionnaire((protocol.isAmendment() || protocol.isRenewal()) ? CoeusSubModule.AMENDMENT_RENEWAL : CoeusSubModule.ZERO_SUBMODULE, protocol.getSequenceNumber().toString()));
//    }

    
// TODO ********************** OVERRIDDEN during IRB backfit ************************    
    /**
     * Sets up dates for the submission details subpanel.
     */
    public void initSubmissionDetails() {
        if (currentSubmissionNumber <= 0) {
            currentSubmissionNumber = getTotalSubmissions();
        }

        if (CollectionUtils.isNotEmpty(getProtocol().getProtocolSubmissions()) && getProtocol().getProtocolSubmissions().size() > 1) {
            setPrevNextFlag();
        } else {
            setPrevDisabled(true);
            setNextDisabled(true);
        }

        setReviewComments(getReviewerCommentsService().getReviewerComments(getProtocol().getProtocolNumber(), currentSubmissionNumber));
        if (CollectionUtils.isNotEmpty(getReviewComments())) {
            // check if our comments bean has empty list of review comments, this can happen if the submission has no schedule assigned
            // also check that the list of deleted comments is empty, because deletion of comments can also lead to an empty list of review comments.
            if( (protocolManageReviewCommentsBean.getReviewCommentsBean().getReviewComments().size() == 0)
                && 
                (protocolManageReviewCommentsBean.getReviewCommentsBean().getDeletedReviewComments().size() == 0) ) {
                List<CommitteeScheduleMinute> reviewComments = getReviewerCommentsService().getReviewerComments(getProtocol().getProtocolNumber(), currentSubmissionNumber);
                Collections.sort(reviewComments, new Comparator<CommitteeScheduleMinute>() {

                    @Override
                    public int compare(CommitteeScheduleMinute csm1, CommitteeScheduleMinute csm2) {
                        int retVal = 0;
                        if( (csm1 != null) && (csm2 != null) && (csm1.getEntryNumber() != null) && (csm2.getEntryNumber() != null) ) {
                            retVal = csm1.getEntryNumber().compareTo(csm2.getEntryNumber());
                        }
                        return retVal;
                    }
                    
                });
                protocolManageReviewCommentsBean.getReviewCommentsBean().setReviewComments((List)reviewComments);
                getReviewerCommentsService().setHideReviewerName(reviewComments);
            }
            getReviewerCommentsService().setHideReviewerName(getReviewComments());
        }
        setReviewAttachments(getReviewerCommentsService().getReviewerAttachments(getProtocol().getProtocolNumber(), currentSubmissionNumber));
        if (CollectionUtils.isNotEmpty(getReviewAttachments())) {
            hideReviewerNameForAttachment = getReviewerCommentsService().setHideReviewerName(getReviewAttachments());
            getReviewerCommentsService().setHideViewButton(getReviewAttachments());
        }
        getProtocol().getProtocolSubmission().refreshReferenceObject("reviewAttachments");
//        setReviewAttachments(getProtocol().getProtocolSubmission().getReviewAttachments());
        hideSubmissionReviewerName = checkToHideSubmissionReviewerName();

        setProtocolReviewers(getReviewerCommentsService().getProtocolReviewers(getProtocol().getProtocolNumber(),
                currentSubmissionNumber));
        setAbstainees((List)getCommitteeDecisionService().getAbstainers(getProtocol().getProtocolNumber(), currentSubmissionNumber));
        setRecusers((List)getCommitteeDecisionService().getRecusers(getProtocol().getProtocolNumber(), currentSubmissionNumber));
        setSubmissionQuestionnaireExist(hasAnsweredQuestionnaire(CoeusSubModule.PROTOCOL_SUBMISSION, Integer.toString(currentSubmissionNumber)));
    }
   
    
// TODO ********************** commented out during IRB backfit ************************    
//    public void setCurrentTask(String currentTaskName) {
//        this.currentTaskName = currentTaskName;
//    }
//    
//    public String getCurrentTask() {
//        return currentTaskName;
//    }
    
    
    
    
    
 // TODO ********************** OVERRIDDEN during IRB backfit ************************    
    /**
     * This method populates the protocolAmendmentBean with the amendment details from the 
     * current submission.
     * @throws Exception
     */
    protected void setAmendmentDetails() throws Exception {
        /*
         * Check if the user is trying to modify amendment sections, if so, do not setAmendmentDetials.
         * If you set it, the user's data gets refreshed and the amendment details from the currentSubmission
         * will be populated in the protocolAmendmentBean.
         */
        if (!currentTaskName.equalsIgnoreCase(TaskName.MODIFY_PROTOCOL_AMMENDMENT_SECTIONS)) {
            ProtocolAmendmentBean amendmentBean = (ProtocolAmendmentBean) getProtocolAmendmentBean();
            String originalProtocolNumber;
            // Use the submission number to get the correct amendment details
            if (getProtocol().isAmendment()) {
                originalProtocolNumber = getProtocol().getProtocolAmendRenewal().getProtocolNumber();           
            } else {
                // We want to display amendment details even if the document is not an amendment.
                // Amendment details needs to be displayed even after the amendment has been merged with the protocol.
                originalProtocolNumber = getProtocol().getProtocolNumber();
            }
            List<ProtocolBase> protocols = getProtocolAmendRenewServiceHook().getAmendmentAndRenewals(originalProtocolNumber);

            ProtocolAmendRenewal correctAmendment = (ProtocolAmendRenewal) getCorrectAmendment(protocols);
            if (ObjectUtils.isNotNull(correctAmendment)) {
                setSubmissionHasNoAmendmentDetails(false);
                amendmentBean.setSummary(correctAmendment.getSummary());
                amendmentBean.setGeneralInfo((correctAmendment.hasModule(ProtocolModule.GENERAL_INFO)) ? true : false);
                amendmentBean.setProtocolPersonnel((correctAmendment.hasModule(ProtocolModule.PROTOCOL_PERSONNEL)) ? true : false);
                amendmentBean.setAreasOfResearch((correctAmendment.hasModule(ProtocolModule.AREAS_OF_RESEARCH)) ? true : false);
                amendmentBean.setAddModifyAttachments((correctAmendment.hasModule(ProtocolModule.ADD_MODIFY_ATTACHMENTS)) ? true : false);
                amendmentBean.setFundingSource((correctAmendment.hasModule(ProtocolModule.FUNDING_SOURCE)) ? true : false);
                amendmentBean.setOthers((correctAmendment.hasModule(ProtocolModule.OTHERS)) ? true : false);
                amendmentBean.setProtocolOrganizations((correctAmendment.hasModule(ProtocolModule.PROTOCOL_ORGANIZATIONS)) ? true : false);
                amendmentBean.setProtocolPermissions((correctAmendment.hasModule(ProtocolModule.PROTOCOL_PERMISSIONS)) ? true : false);
                amendmentBean.setProtocolReferencesAndOtherIdentifiers((correctAmendment.hasModule(ProtocolModule.PROTOCOL_REFERENCES)) ? true : false);
                amendmentBean.setQuestionnaire((correctAmendment.hasModule(ProtocolModule.QUESTIONNAIRE)) ? true : false);
                amendmentBean.setSpecialReview((correctAmendment.hasModule(ProtocolModule.SPECIAL_REVIEW)) ? true : false);
                amendmentBean.setSubjects((correctAmendment.hasModule(ProtocolModule.SUBJECTS)) ? true : false);
            } else {
                setSubmissionHasNoAmendmentDetails(true);
            }
        }
    }
 
// TODO ********************** commented out during IRB backfit ************************    
//    public void setSubmissionHasNoAmendmentDetails(boolean submissionHasNoAmendmentDetails) {
//        this.submissionHasNoAmendmentDetails = submissionHasNoAmendmentDetails;
//    }
//    
//    public boolean getSubmissionHasNoAmendmentDetails() {
//        return submissionHasNoAmendmentDetails;
//    }
//    
//    /**
//     * This method returns the amendRenewal bean with the current submission number. 
//     * @param protocols
//     * @return
//     */
//    protected ProtocolAmendRenewal getCorrectAmendment(List<Protocol> protocols) {
//        for (Protocol protocol : protocols) {
//            // There should always be an amendment with the current submission number.
//            if (protocol.isAmendment() && ObjectUtils.isNotNull(protocol.getProtocolSubmission().getSubmissionNumber()) 
//                && protocol.getProtocolSubmission().getSubmissionNumber() == currentSubmissionNumber) {
//                return protocol.getProtocolAmendRenewal();
//            }
//        }
//        return null;
//    }
//
//    
//    
//    private boolean hasAnsweredQuestionnaire(String moduleSubItemCode, String moduleSubItemKey) {
//        return getAnswerHeaderCount(moduleSubItemCode, moduleSubItemKey) > 0;
//    }
//
//    int getAnswerHeaderCount(String moduleSubItemCode, String moduleSubItemKey) {
//        Map<String, String> fieldValues = new HashMap<String, String>();
//        fieldValues.put("moduleItemCode", CoeusModule.IRB_MODULE_CODE);
//        fieldValues.put("moduleItemKey", getProtocol().getProtocolNumber());
//        fieldValues.put("moduleSubItemCode", moduleSubItemCode);
//        fieldValues.put("moduleSubItemKey", moduleSubItemKey);
//        return getBusinessObjectService().countMatching(AnswerHeader.class, fieldValues);
//        
//    }
    
    
    
    protected String getCoeusModule() {
        return CoeusModule.IRB_MODULE_CODE;
    }
    
    
    
    /*
     * This will check whetehr there is submission questionnaire.
     * When business rule is implemented, this will become more complicated because
     * each request action may have different set of questionnaire, so this has to be changed.
     */
    private boolean hasSubmissionQuestionnaire() {
        ModuleQuestionnaireBean moduleQuestionnaireBean = new ProtocolModuleQuestionnaireBean(CoeusModule.IRB_MODULE_CODE, this.getProtocolForm().getProtocolDocument().getProtocol().getProtocolNumber() + "T", CoeusSubModule.PROTOCOL_SUBMISSION, "999", false);
        return CollectionUtils.isNotEmpty(getQuestionnaireAnswerService().getQuestionnaireAnswer(moduleQuestionnaireBean));
    }


    private QuestionnaireAnswerService getQuestionnaireAnswerService() {
        return KraServiceLocator.getService(QuestionnaireAnswerService.class);
    }

    
 // TODO ********************** commented out during IRB backfit ************************        
//    
//    /**
//     * 
//     * This method is to get previous submission number.  Current implementation is based on submission number in sequence.
//     * If multiple amendment/renewal are submitted, but approved not according to submission order.  Then we may have gaping submission number.
//     * @return
//     */
//    public int getPrevSubmissionNumber() {
//        List<Integer> submissionNumbers = getAvailableSubmissionNumbers();
//        Integer submissionNumber = currentSubmissionNumber - 1;
//        if (!submissionNumbers.contains(submissionNumber)) {
//            for (int i = currentSubmissionNumber - 1; i > 0; i--) {
//                if (submissionNumbers.contains(i)) {
//                    submissionNumber = i;
//                    break;
//                }
//            }
//        }
//        return submissionNumber;
//
//    }
//    
//    /**
//     * 
//     * This method is to get next submissionnumber
//     * @return
//     */
//    public int getNextSubmissionNumber() {
//        List<Integer> submissionNumbers = getAvailableSubmissionNumbers();
//        int maxSubmissionNumber = getMaxSubmissionNumber();
//
//        Integer submissionNumber = currentSubmissionNumber + 1;
//        if (!submissionNumbers.contains(submissionNumber)) {
//            for (int i = currentSubmissionNumber + 1; i <= maxSubmissionNumber; i++) {
//                if (submissionNumbers.contains(i)) {
//                    submissionNumber = i;
//                    break;
//                }
//            }
//        }
//        return submissionNumber;
//
//    }
//
//    
//    /*
//     * this returns a list of submission numbers for a protocol.
//     */
//    private List<Integer> getAvailableSubmissionNumbers() {
//        List<Integer> submissionNumbers = new ArrayList<Integer>();
//        for (ProtocolSubmission submission : getProtocol().getProtocolSubmissions()) {
//            submissionNumbers.add(submission.getSubmissionNumber());
//        }
//        return submissionNumbers;
//    }
//
//  
//
//    /*
//     * utility method to set whether to display next or previous button on submission panel.
//     */
//    private void setPrevNextFlag() {
//        int maxSubmissionNumber = 0;
//        int minSubmissionNumber = 0;
//        setPrevDisabled(false);
//        setNextDisabled(false);
//
//        for (ProtocolSubmission submission : getProtocol().getProtocolSubmissions()) {
//            if (submission.getSubmissionNumber() > maxSubmissionNumber) {
//                maxSubmissionNumber = submission.getSubmissionNumber();
//            }
//            if (submission.getSubmissionNumber() < minSubmissionNumber || minSubmissionNumber == 0) {
//                minSubmissionNumber = submission.getSubmissionNumber();
//            }
//        }
//        if (currentSubmissionNumber == minSubmissionNumber) {
//            setPrevDisabled(true);
//        }
//        if (currentSubmissionNumber == maxSubmissionNumber) {
//            setNextDisabled(true);
//        }
//    }
//    
//    
//
//    
//    /**
//     * This method returns true if a protocol has amendments
//     * @return
//     * @throws Exception
//     */
//    public boolean getHasAmendments() throws Exception {
//        if (getProtocol().isAmendment()) {
//            hasAmendments = true;
//        } else {
//            List<Protocol> protocols = (List<Protocol>) getProtocolAmendRenewService().getAmendments(getProtocol().getProtocolNumber());
//            hasAmendments = protocols.isEmpty() ? false : true;
//        } 
//        return hasAmendments;
//    }
//    
//    
//    /**
//     * This method returns true if a protocol has renewals.
//     * @return
//     * @throws Exception
//     */
//    public boolean getHasRenewals() throws Exception {
//        if (getProtocol().isRenewal()) {
//            hasRenewals = true;
//        } else {
//            List<Protocol> protocols = (List<Protocol>) getProtocolAmendRenewService().getRenewals(getProtocol().getProtocolNumber());
//            hasRenewals = protocols.isEmpty() ? false : true;
//        }
//        return hasRenewals;
//    }
    
    public void addNotifyIrbAttachment() {
        getProtocolNotifyIrbBean().getActionAttachments().add(
                getProtocolNotifyIrbBean().getNewActionAttachment());
        getProtocolNotifyIrbBean().setNewActionAttachment(new ProtocolActionAttachment());
    }

    public boolean validFile(final ProtocolActionAttachment attachment, String propertyName) {
        
        boolean valid = true;
        
        //this got much more complex using anon keys
        if (attachment.getFile() == null || StringUtils.isBlank(attachment.getFile().getFileName())) {
            valid = false;
            new ErrorReporter().reportError("actionHelper." + propertyName + ".newActionAttachment.file",
                KeyConstants.ERROR_ATTACHMENT_REQUIRED);
        }
        
        return valid;
    }

// TODO ********************** commented out during IRB backfit ************************    
//    public List<ProtocolVoteRecused> getRecusers() {
//        return recusers;
//    }
//
//
//    public void setRecusers(List<ProtocolVoteRecused> recusers) {
//        this.recusers = recusers;
//    }
//    
//    /**
//     * 
//     * This method determines whether the committee select list should be displayed or not.
//     * @return
//     */
//    public boolean isShowCommittee() {
//        return "O".equals(this.getSubmissionConstraint()) || "M".equals(this.getSubmissionConstraint());
//    }

    public ProtocolGenericActionBean getProtocolDeferBean() {
        return protocolDeferBean;
    }

    
    public ProtocolReviewNotRequiredBean getProtocolReviewNotRequiredBean() {
        return this.protocolReviewNotRequiredBean;
    }
    
    
// TODO ********************** commented out during IRB backfit ************************
//    public boolean isPrevDisabled() {
//        return prevDisabled;
//    }
//
//
//    public void setPrevDisabled(boolean prevDisabled) {
//        this.prevDisabled = prevDisabled;
//    }
//
//
//    public boolean isNextDisabled() {
//        return nextDisabled;
//    }
//
//
//    public void setNextDisabled(boolean nextDisabled) {
//        this.nextDisabled = nextDisabled;
//    }
//
//
//    public List<ProtocolReviewer> getProtocolReviewers() {
//        return protocolReviewers;
//    }
//
//
//    public void setProtocolReviewers(List<ProtocolReviewer> protocolReviewers) {
//        this.protocolReviewers = protocolReviewers;
//    }
//
//
//    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
//        this.businessObjectService = businessObjectService;
//    }
//
//
//    public String getRenewalSummary() {
//        return renewalSummary;
//    }
//
//
//    public void setRenewalSummary(String renewalSummary) {
//        this.renewalSummary = renewalSummary;
//    }
//
//
//    /**
//     * Sets the protocolSummaryPrintOptions attribute value.
//     * @param protocolSummaryPrintOptions The protocolSummaryPrintOptions to set.
//     */
//    public void setProtocolSummaryPrintOptions(ProtocolSummaryPrintOptions protocolSumamryPrintOptions) {
//        this.protocolSummaryPrintOptions = protocolSumamryPrintOptions;
//    }
//
//
//    /**
//     * Gets the protocolSummaryPrintOptions attribute. 
//     * @return Returns the protocolSummaryPrintOptions.
//     */
//    public ProtocolSummaryPrintOptions getProtocolSummaryPrintOptions() {
//        return protocolSummaryPrintOptions;
//    }
//    
//    public ProtocolActionBean getActionBean(String taskName) {
//        return actionBeanTaskMap.get(taskName);
//    }

    public ProtocolRequestBean getRequestBean(String actionTypeCode) {
        ProtocolRequestBean protocolRequestBean = null;
        
        ProtocolRequestAction action = ProtocolRequestAction.valueOfActionTypeCode(actionTypeCode);
        if (action != null) {
            ProtocolActionBean bean = (ProtocolActionBean) actionBeanTaskMap.get(action.getTaskName());
            if (bean instanceof ProtocolRequestBean) {
                protocolRequestBean = (ProtocolRequestBean) bean;
            }
        }
        
        return protocolRequestBean;
    }
    
// TODO ********************** commented out during IRB backfit ************************
//    public Boolean getSummaryReport() {
//        return summaryReport;
//    }
//
//    public void setSummaryReport(Boolean summaryReport) {
//        this.summaryReport = summaryReport;
//    }
//
//    public Boolean getFullReport() {
//        return fullReport;
//    }
//
//    public void setFullReport(Boolean fullReport) {
//        this.fullReport = fullReport;
//    }
//
//    public Boolean getHistoryReport() {
//        return historyReport;
//    }
//
//    public void setHistoryReport(Boolean historyReport) {
//        this.historyReport = historyReport;
//    }
//
//    public Boolean getReviewCommentsReport() {
//        return reviewCommentsReport;
//    }
//
//    public void setReviewCommentsReport(Boolean reviewCommentsReport) {
//        this.reviewCommentsReport = reviewCommentsReport;
//    }

    public boolean isSubmissionQuestionnaireExist() {
        return submissionQuestionnaireExist;
    }

    public void setSubmissionQuestionnaireExist(boolean submissionQuestionnaireExist) {
        this.submissionQuestionnaireExist = submissionQuestionnaireExist;
    }

    public boolean isToAnswerSubmissionQuestionnaire() {
        return toAnswerSubmissionQuestionnaire;
    }

    public void setToAnswerSubmissionQuestionnaire(boolean toAnswerSubmissionQuestionnaire) {
        this.toAnswerSubmissionQuestionnaire = toAnswerSubmissionQuestionnaire;
    }

// TODO ********************** commented out during IRB backfit ************************    
//    public ProtocolSummaryPrintOptions getProtocolPrintOption() {
//        return protocolPrintOption;
//    }
//
//    public void setProtocolPrintOption(ProtocolSummaryPrintOptions protocolPrintOption) {
//        this.protocolPrintOption = protocolPrintOption;
//    }
//
//    public List<QuestionnairePrintOption> getQuestionnairesToPrints() {
//        return questionnairesToPrints;
//    }
//
//    public void setQuestionnairesToPrints(List<QuestionnairePrintOption> questionnairesToPrints) {
//        this.questionnairesToPrints = questionnairesToPrints;
//    }
//
//    private void initPrintQuestionnaire() {
//        setQuestionnairesToPrints(new ArrayList<QuestionnairePrintOption>());
//        ModuleQuestionnaireBean moduleQuestionnaireBean = new ProtocolModuleQuestionnaireBean(getProtocol());
//        //answerHeaders = getQuestionnaireAnswerService().getQuestionnaireAnswer(moduleQuestionnaireBean);
//        List<AnswerHeader> answerHeaders  = getQuestionnaireAnswerService().getAnswerHeadersForProtocol(getProtocol().getProtocolNumber());
//        setupQnPrintOption(answerHeaders);
//    }

// TODO ********************** commented out during IRB backfit ************************ CODE MOVED TO QNNR PRINT SERVICE    
//    /*
//     * This method is to set up the questionnaire print list.  Then sorted it.
//     */
//    private void setupQnPrintOption(List<AnswerHeader> answerHeaders) {
//        int maxSubmissionNumber = getMaxSubmissionNumber();
//        for (AnswerHeader answerHeader : answerHeaders) {
//            // only submission questionnaire and current protocol questionnaire will be printed
//            if ( (CoeusSubModule.PROTOCOL_SUBMISSION.equals(answerHeader.getModuleSubItemCode()) && Integer.parseInt(answerHeader.getModuleSubItemKey()) <= maxSubmissionNumber)
//                    || (isCurrentAmendRenewalQn(answerHeader)) ) {
//                QuestionnairePrintOption printOption = new QuestionnairePrintOption();
//                printOption.setQuestionnaireRefId(answerHeader.getQuestionnaire().getQuestionnaireRefIdAsLong());
//                printOption.setQuestionnaireId(answerHeader.getQuestionnaire().getQuestionnaireIdAsInteger());
//                printOption.setSelected(true);
//                printOption.setQuestionnaireName(answerHeader.getQuestionnaire().getName());
//                printOption.setLabel(getQuestionnaireLabel(answerHeader));
//                printOption.setItemKey(answerHeader.getModuleItemKey());
//                printOption.setSubItemKey(answerHeader.getModuleSubItemKey());
//                printOption.setSubItemCode(answerHeader.getModuleSubItemCode());
//                // finally check if the answerheader's questionnaire is active, and set it accordingly in the Qnnr print option bean
//                printOption.setQuestionnaireActive(isQuestionnaireActive(answerHeader));
//                getQuestionnairesToPrints().add(printOption);
//            }
//        }
//        Collections.sort(getQuestionnairesToPrints(), new QuestionnairePrintOptionComparator());
//    }
//    
//    private boolean isQuestionnaireActive(AnswerHeader answerHeader) {        
//        Integer questionnaireId = answerHeader.getQuestionnaire().getQuestionnaireIdAsInteger();
//        String coeusModuleCode = answerHeader.getModuleItemCode();
//        String coeusSubModuleCode = answerHeader.getModuleSubItemCode(); 
//        return getQuestionnaireAnswerService().checkIfQuestionnaireIsActiveForModule(questionnaireId, coeusModuleCode, coeusSubModuleCode);
//    }

    
    
// TODO ********************** commented out during IRB backfit ************************    
//    private int getMaxSubmissionNumber() {
//        int maxSubmissionNumber = 0;
//
//        for (Integer submissionNumber : getAvailableSubmissionNumbers()) {
//            if (submissionNumber > maxSubmissionNumber) {
//                maxSubmissionNumber = submissionNumber;
//            }
//        }
//        return maxSubmissionNumber;
//    }
//    
    
    
    
    

 // TODO ********************** commented out during IRB backfit ************************ CODE MOVED TO QNNR PRINT SERVICE    
//    /*
//     * check if this is the current version of the amend/renewal Qn
//     */
//    private boolean isCurrentAmendRenewalQn(AnswerHeader answerHeader) {
//        boolean isCurrentQn = CoeusSubModule.AMENDMENT_RENEWAL.equals(answerHeader.getModuleSubItemCode())
//                || CoeusSubModule.ZERO_SUBMODULE.equals(answerHeader.getModuleSubItemCode());
//        if (isCurrentQn) {
//            if (CoeusSubModule.ZERO_SUBMODULE.equals(answerHeader.getModuleSubItemCode())) {
//                isCurrentQn = isCurrentRegularQn(answerHeader);
//            } else if (CoeusSubModule.AMENDMENT_RENEWAL.equals(answerHeader.getModuleSubItemCode())) {
//                isCurrentQn = isCurrentAorRQn(answerHeader);
//            }
//        }
//        return isCurrentQn;
//    }
//
//    /*
//     * This is Questionnaire answer is with submodulecode of "0".
//     * Then if this protocol is A/R, then it has to check whether this is the first version
//     * A/R merged into. 
//     */
//    private boolean isCurrentRegularQn(AnswerHeader answerHeader) {
//        boolean isCurrentQn = false;
//        if ((getProtocol().isAmendment() || getProtocol().isRenewal()) && !answerHeader.getModuleItemKey().equals(getProtocol().getProtocolNumber())) {
//            Map keyValues = new HashMap();
//            keyValues.put("protocolNumber", answerHeader.getModuleItemKey());
//          //  keyValues.put("protocolNumber", getProtocol().getProtocolNumber());
//            Protocol prevProtocol = null;
//            // if this is an A/R protocol, then need to find the original protocol that the A/R first merged into.
//            for (Protocol protocol : ((List<Protocol>) businessObjectService.findMatchingOrderBy(Protocol.class, keyValues,
//                    "sequenceNumber", true))) {
//                isCurrentQn = answerHeader.getModuleSubItemKey().equals(protocol.getSequenceNumber().toString())
//                        && !CollectionUtils.isEmpty(getProtocol().getProtocolSubmissions())
//                        && isMergedToProtocol(protocol, getProtocol());
//                if (isCurrentQn) {
//                    if (prevProtocol == null
//                            || !isMergedToProtocol(prevProtocol, getProtocol())) {
//                        // this is the protocol this A/R merged into. so, use this questionnaire.
//                        break;
//                    } else {
//                        // prevProtocol is the initial Protocol that this A/R merged into.
//                        isCurrentQn = false;
//                    }
//
//                }
//                prevProtocol = protocol;
//            }
//        } else {
//            // if this is a regular protocol, then check if sequencenumber & modulesubitemkey match
//            isCurrentQn = answerHeader.getModuleSubItemKey().equals(getProtocol().getSequenceNumber().toString());
//        }
//        return isCurrentQn;
//    }
//    
//    /*
//     * if questionnaire is associated with Amendment/renewal submodulecode.
//     * if this protocol is normal protocol, then it has to check whether the A/R of this
//     * questionnaire has merged to this protocol yet.
//     */
//    private boolean isCurrentAorRQn(AnswerHeader answerHeader) {
//        boolean isCurrentQn = false;
//        if (getProtocol().isAmendment() || getProtocol().isRenewal()) {
//            // if this is A/R, then just match sequencenumber and modulesubitemkey
//            isCurrentQn = answerHeader.getModuleSubItemKey().equals(getProtocol().getSequenceNumber().toString());
//        } else {
//            // if this is a regular protocol, then get this A/R associated this this Qn and see if
//            // A/R has been merged to this version of protocol
//            Map keyValues = new HashMap();
//            keyValues.put("protocolNumber", answerHeader.getModuleItemKey());
//            Protocol protocol = ((List<Protocol>) businessObjectService.findMatchingOrderBy(Protocol.class, keyValues,
//                    "sequenceNumber", false)).get(0);
//            isCurrentQn = answerHeader.getModuleSubItemKey().equals(protocol.getSequenceNumber().toString())
//                    && !CollectionUtils.isEmpty(protocol.getProtocolSubmissions())
//                    && isMergedToProtocol((Protocol) getProtocol(), protocol);
//        }
//        return isCurrentQn;
//    }       
//    
//    private boolean isMergedToProtocol(Protocol protocol, Protocol amendment) {
//        boolean merged = false;
//        int submissionNumber = amendment.getProtocolSubmissions().get(amendment.getProtocolSubmissions().size() - 1).getSubmissionNumber();
//        for (ProtocolSubmissionBase submission : protocol.getProtocolSubmissions()) {
//            if (submissionNumber == submission.getSubmissionNumber().intValue()) {
//                merged = true;
//                break;
//            }
//        }
//        return merged;
//    }
//    
//    private String getQuestionnaireLabel(AnswerHeader answerHeader) {
//        String label = null;
//        List<QuestionnaireUsage> usages = answerHeader.getQuestionnaire().getQuestionnaireUsages();
//        if (CollectionUtils.isNotEmpty(usages) && usages.size() > 1) {
//            Collections.sort((List<QuestionnaireUsage>) usages);
//           // Collections.reverse((List<QuestionnaireUsage>) usages);
//        }
//        for (QuestionnaireUsage usage : usages) {
//            if (CoeusModule.IRB_MODULE_CODE.equals(usage.getModuleItemCode()) && answerHeader.getModuleSubItemCode().equals(usage.getModuleSubItemCode())) {
//                if ("0".equals(answerHeader.getModuleSubItemCode())) {
//                    label = usage.getQuestionnaireLabel();
//                } else if (CoeusSubModule.PROTOCOL_SUBMISSION.equals(answerHeader.getModuleSubItemCode())) {
//                    Map keyValues = new HashMap();
//                    keyValues.put("protocolNumber", answerHeader.getModuleItemKey());
//                    keyValues.put("submissionNumber", answerHeader.getModuleSubItemKey());
//                    List<ProtocolSubmission> submissions = ((List<ProtocolSubmission>) businessObjectService.findMatchingOrderBy(ProtocolSubmission.class, keyValues,
//                            "submissionId", false));
//                    if (submissions.isEmpty()) {
//                        // this may happen if it is undo last action
//                        label = usage.getQuestionnaireLabel();
//                    } else {
//                        keyValues.clear();
//                        keyValues.put("protocolId", submissions.get(0).getProtocolId());
//                        keyValues.put("submissionNumber", answerHeader.getModuleSubItemKey());
//                        // keyValues.put("submissionIdFk", submission.getSubmissionId());
//                        ProtocolAction protocolAction = ((List<ProtocolAction>) businessObjectService.findMatching(
//                                ProtocolAction.class, keyValues)).get(0);
//                        label = usage.getQuestionnaireLabel() + " - " + protocolAction.getProtocolActionType().getDescription()
//                                + " - " + protocolAction.getActionDateString();
//                    }
//                } else if (CoeusSubModule.AMENDMENT_RENEWAL.equals(answerHeader.getModuleSubItemCode())) {
//                    if (answerHeader.getModuleItemKey().contains("A")) {
//                        label = usage.getQuestionnaireLabel() + " - Amendment " + answerHeader.getModuleItemKey().substring(10);
//                    } else {
//                        label = usage.getQuestionnaireLabel() + " - Renewal " + answerHeader.getModuleItemKey().substring(10);
//                    }
//                }
//            }
//        }
//        return label;
//    }

    
    
    
// TODO ********************** commented out during IRB backfit ************************    
//    public boolean isSummaryQuestionnaireExist() {
//        return summaryQuestionnaireExist;
//    }
//
//    public void setSummaryQuestionnaireExist(boolean summaryQuestionnaireExist) {
//        this.summaryQuestionnaireExist = summaryQuestionnaireExist;
//    }
//
//    public boolean isCanAbandon() {
//        return canAbandon;
//    }
//
//    public void setCanAbandon(boolean canAbandon) {
//        this.canAbandon = canAbandon;
//    }
//
//    public ProtocolGenericActionBean getProtocolAbandonBean() {
//        return protocolAbandonBean;
//    }
//
//    public void setProtocolAbandonBean(ProtocolGenericActionBean protocolAbandonBean) {
//        this.protocolAbandonBean = protocolAbandonBean;
//    }
//
//    public boolean isHideReviewerName() {
//        return hideReviewerName;
//    }
//
//    public void setHideReviewerName(boolean hideReviewerName) {
//        this.hideReviewerName = hideReviewerName;
//    }
//    
//    /*
//     * check if to display reviewer name for any of the review comments of the submission selected in submission details..
//     */
//    private boolean checkToHideSubmissionReviewerName() {
//        boolean isHide = true;
//        for (CommitteeScheduleMinute reviewComment : getReviewComments()) {
//            if (reviewComment.isDisplayReviewerName()) {
//                isHide = false;
//                break;
//            }
//        }
//        return isHide;
//    }
//    
//    /*
//     * check if to display reviewer name for any of the review comments of current submission.
//     */
//    private boolean checkToHideReviewName() {
//        boolean isHide = true;
//        if (getProtocol().getProtocolSubmission().getSubmissionId() != null) {
//            isHide = getReviewerCommentsService().setHideReviewerName(getProtocol(), getProtocol().getProtocolSubmission().getSubmissionNumber());
//        }
//        return isHide;
//    }
//
//    public boolean isHideSubmissionReviewerName() {
//        return hideSubmissionReviewerName;
//    }
//
//    public void setHideSubmissionReviewerName(boolean hideSubmissionReviewerName) {
//        this.hideSubmissionReviewerName = hideSubmissionReviewerName;
//    }
//
//    public List<ProtocolReviewAttachment> getReviewAttachments() {
//        return reviewAttachments;
//    }
//
//    public void setReviewAttachments(List<ProtocolReviewAttachment> reviewAttachments) {
//        this.reviewAttachments = reviewAttachments;
//    }
//
//    public boolean isHideReviewerNameForAttachment() {
//        return hideReviewerNameForAttachment;
//    }
//
//    public void setHideReviewerNameForAttachment(boolean hideReviewerNameForAttachment) {
//        this.hideReviewerNameForAttachment = hideReviewerNameForAttachment;
//    }
//
//    public ProtocolCorrespondence getProtocolCorrespondence() {
//        return protocolCorrespondence;
//    }
//
//    public void setProtocolCorrespondence(ProtocolCorrespondence protocolCorrespondence) {
//        this.protocolCorrespondence = protocolCorrespondence;
//    }

    public boolean isIrbAdmin() {
        return getKraAuthorizationService().hasRole(GlobalVariables.getUserSession().getPrincipalId(), NAMESPACE, RoleConstants.IRB_ADMINISTRATOR);
    }

    @Override
    protected String getSRRProtocolActionTypeHook() {
        return ProtocolActionType.SUBSTANTIVE_REVISIONS_REQUIRED;
    }

    @Override
    protected String getSMRProtocolActionTypeHook() {
        return ProtocolActionType.SPECIFIC_MINOR_REVISIONS_REQUIRED;
    }

    @Override
    protected String getReturnToPIActionTypeHook() {
        return ProtocolActionType.RETURNED_TO_PI;
    }

    @Override
    protected String getDisapprovedProtocolActionTypeHook() {
        return ProtocolActionType.DISAPPROVED;
    }

    @Override
    protected String getProtocolActionTypeCodeForManageReviewCommentsHook() {
        return ProtocolActionType.MANAGE_REVIEW_COMMENTS;
    }

    @Override
    protected org.kuali.kra.protocol.actions.decision.CommitteeDecision<?> getNewCommitteeDecisionInstanceHook(ActionHelperBase actionHelper) {
        return new CommitteeDecision((ActionHelper) actionHelper);
    }

    @Override
    protected org.kuali.kra.protocol.actions.assignagenda.ProtocolAssignToAgendaBean getNewProtocolAssignToAgendaBeanInstanceHook(ActionHelperBase actionHelper) {
        return new ProtocolAssignToAgendaBean((ActionHelper) actionHelper);
    }

    @Override
    protected ProtocolAdministrativelyWithdrawBean getNewProtocolAdminWithdrawBeanInstanceHook(ActionHelperBase actionHelper) {
        return new ProtocolAdministrativelyWithdrawBean((ActionHelper) actionHelper);
    }

    @Override
    protected ProtocolAdministrativelyIncompleteBean getNewProtocolAdminIncompleteBeanInstanceHook(ActionHelperBase actionHelper) {
        return new ProtocolAdministrativelyIncompleteBean((ActionHelper) actionHelper);
    }

    @Override
    protected String getAdminApprovalProtocolActionTypeHook() {
        return ProtocolActionType.APPROVED;
    }

    @Override
    protected String getFullApprovalProtocolActionTypeHook() {
        return ProtocolActionType.APPROVED;
    }

    @Override
    protected ProtocolWithdrawBean getNewProtocolWithdrawBeanInstanceHook(ActionHelperBase actionHelper) {
        return new ProtocolWithdrawBean((ActionHelper) actionHelper);
    }

    @Override
    protected ProtocolAmendmentBean getNewProtocolAmendmentBeanInstanceHook(ActionHelperBase actionHelper) {
        return new ProtocolAmendmentBean((ActionHelper) actionHelper);
    }

    @Override
    protected ProtocolNotifyCommitteeBean getNewProtocolNotifyCommitteeBeanInstanceHook(ActionHelperBase actionHelper) {
        return new ProtocolNotifyCommitteeBean((ActionHelper) actionHelper);
    }

    @Override
    protected ProtocolSubmitAction getNewProtocolSubmitActionInstanceHook(ActionHelperBase actionHelper) {
        return new ProtocolSubmitAction((ActionHelper) actionHelper);
    }

    @Override
    protected ProtocolDeleteBean getNewProtocolDeleteBeanInstanceHook(ActionHelperBase actionHelper) {
        return new ProtocolDeleteBean((ActionHelper) actionHelper);
    }

    @Override
    protected AdminCorrectionBean getNewAdminCorrectionBeanInstanceHook(ActionHelperBase actionHelper) {
        return new AdminCorrectionBean((ActionHelper) actionHelper);
    }

    @Override
    protected UndoLastActionBean getNewUndoLastActionBeanInstanceHook() {
        return new UndoLastActionBean(this);
    }

    @Override
    protected String getAbandonActionTypeHook() {
        return ProtocolActionType.ABANDON_PROTOCOL;
    }

    @Override
    protected String getAbandonPropertyKeyHook() {
        return Constants.PROTOCOL_ABANDON_ACTION_PROPERTY_KEY;
    }

    @Override
    protected String getExpireKeyHook() {
        return ProtocolActionType.EXPIRED;
    }

    @Override
    protected String getTerminateKeyHook() {
        return ProtocolActionType.TERMINATED;
    }

    @Override
    protected String getSuspendKeyHook() {
        return ProtocolActionType.SUSPENDED;
    }

    @Override
    protected Class<ReviewCommentsService> getReviewCommentsServiceClassHook() {
        return ReviewCommentsService.class;
    }

    @Override
    protected ProtocolApproveBean getNewProtocolApproveBeanInstanceHook(ActionHelperBase actionHelper, String errorPropertyKey) {
        return new ProtocolApproveBean((ActionHelper) actionHelper, errorPropertyKey);
    }

    @Override
    protected ProtocolTaskBase getNewAdminApproveProtocolTaskInstanceHook(ProtocolBase protocol) {
        return new ProtocolTask(TaskName.ADMIN_APPROVE_PROTOCOL, getProtocol());
    }

    @Override
    protected ProtocolTaskBase getNewAdminApproveUnavailableProtocolTaskInstanceHook(ProtocolBase protocol) {
        return new ProtocolTask(TaskName.ADMIN_APPROVE_PROTOCOL_UNAVAILABLE, getProtocol());
    }

    @Override
    protected ProtocolTaskBase getNewAdminWithdrawProtocolTaskInstanceHook(ProtocolBase protocol) {
        return new ProtocolTask(TaskName.ADMIN_WITHDRAW_PROTOCOL, getProtocol());
    }

    @Override
    protected ProtocolTaskBase getNewAdminWithdrawUnavailableProtocolTaskInstanceHook(ProtocolBase protocol) {
        return new ProtocolTask(TaskName.ADMIN_WITHDRAW_PROTOCOL_UNAVAILABLE, getProtocol());
    }

    @Override
    protected ProtocolTaskBase getNewAdminMarkIncompleteProtocolTaskInstanceHook(ProtocolBase protocol) {
        return new ProtocolTask(TaskName.ADMIN_INCOMPLETE_PROTOCOL, getProtocol());
    }

    @Override
    protected ProtocolTaskBase getNewAdminMarkIncompleteUnavailableProtocolTaskInstanceHook(ProtocolBase protocol) {
        return new ProtocolTask(TaskName.ADMIN_INCOMPLETE_PROTOCOL_UNAVAILABLE, getProtocol());
    }

    @Override
    protected Class<? extends CommitteeScheduleServiceBase> getCommitteeScheduleServiceClassHook() {
        return CommitteeScheduleService.class;
    }

    @Override
    protected Class<? extends ProtocolDocumentBase> getProtocolDocumentBOClassHook() {
        return ProtocolDocument.class;
    }

    @Override
    protected ProtocolTaskBase getNewSubmitProtocolTaskInstanceHook(ProtocolBase protocol) {
        return new ProtocolTask(TaskName.SUBMIT_PROTOCOL, getProtocol());
    }

    @Override
    protected ProtocolTaskBase getNewSubmitProtocolUnavailableTaskInstanceHook(ProtocolBase protocol) {
        return new ProtocolTask(TaskName.SUBMIT_PROTOCOL_UNAVAILABLE, getProtocol());
    }

    @Override
    protected ProtocolTaskBase getNewAmendmentProtocolTaskInstanceHook(ProtocolBase protocol) {
        return new ProtocolTask(TaskName.CREATE_PROTOCOL_AMMENDMENT, getProtocol());
    }

    @Override
    protected ProtocolTaskBase getNewAmendmentProtocolUnavailableTaskInstanceHook(ProtocolBase protocol) {
        return new ProtocolTask(TaskName.CREATE_PROTOCOL_AMMENDMENT_UNAVAILABLE, getProtocol());
    }

    @Override
    protected ProtocolTaskBase getModifyAmendmentSectionsProtocolTaskInstanceHook(ProtocolBase protocol) {
        return new ProtocolTask(TaskName.MODIFY_PROTOCOL_AMMENDMENT_SECTIONS, getProtocol());
    }

    @Override
    protected ProtocolTaskBase getModifyAmendmentSectionsUnavailableProtocolUnavailableTaskInstanceHook(ProtocolBase protocol) {
        return new ProtocolTask(TaskName.MODIFY_PROTOCOL_AMMENDMENT_SECTIONS_UNAVAILABLE, getProtocol());
    }

    @Override
    protected ProtocolTaskBase getNewRenewalProtocolTaskInstanceHook(ProtocolBase protocol) {
        return new ProtocolTask(TaskName.CREATE_PROTOCOL_RENEWAL, getProtocol());
    }

    @Override
    protected ProtocolTaskBase getNewRenewalProtocolUnavailableTaskInstanceHook(ProtocolBase protocol) {
        return new ProtocolTask(TaskName.CREATE_PROTOCOL_RENEWAL_UNAVAILABLE, getProtocol());
    }

    @Override
    protected ProtocolTaskBase getNewWithdrawProtocolTaskInstanceHook(ProtocolBase protocol) {
        return new ProtocolTask(TaskName.PROTOCOL_WITHDRAW, getProtocol());
    }

    @Override
    protected ProtocolTaskBase getNewWithdrawProtocolUnavailableTaskInstanceHook(ProtocolBase protocol) {
        return new ProtocolTask(TaskName.PROTOCOL_WITHDRAW_UNAVAILABLE, getProtocol());
    }

    @Override
    protected ProtocolTaskBase createNewAmendRenewDeleteTaskInstanceHook(ProtocolBase protocol) {
        return new ProtocolTask(TaskName.PROTOCOL_AMEND_RENEW_DELETE, getProtocol());
    }

    @Override
    protected ProtocolTaskBase createNewAmendRenewDeleteUnavailableTaskInstanceHook(ProtocolBase protocol) {
        return new ProtocolTask(TaskName.PROTOCOL_AMEND_RENEW_DELETE_UNAVAILABLE, getProtocol());
    }

    @Override
    protected ProtocolTaskBase createNewAssignToAgendaTaskInstanceHook(ProtocolBase protocol) {
        return new ProtocolTask(TaskName.ASSIGN_TO_AGENDA, getProtocol());
    }

    @Override
    protected ProtocolTaskBase createNewAssignToAgendaUnavailableTaskInstanceHook(ProtocolBase protocol) {
        return new ProtocolTask(TaskName.ASSIGN_TO_AGENDA_UNAVAILABLE, getProtocol());
    }
    
    @Override
    protected ProtocolTaskBase createNewAbandonTaskInstanceHook(ProtocolBase protocol) {
        return new ProtocolTask(TaskName.ABANDON_PROTOCOL, getProtocol());
    }

    @Override
    protected ProtocolTaskBase getModifySubmissionAvailableTaskHook() {
        return new ProtocolTask(TaskName.MODIFY_PROTOCOL_SUBMISSION, getProtocol());
    }

    @Override
    protected ProtocolTaskBase getModifySubmissionUnavailableTaskHook() {
        return new ProtocolTask(TaskName.MODIFY_PROTOCOL_SUBMISSION_UNAVAILABLE, getProtocol());
    }

    @Override
    protected Class<? extends ProtocolSubmissionDocBase> getProtocolSubmissionDocClassHook() {
        return ProtocolSubmissionDoc.class;
    }

    @Override
    protected Class<? extends CommitteeServiceBase> getCommitteeServiceClassHook() {
        return CommitteeService.class;
    }

    @Override
    protected Class<? extends org.kuali.kra.protocol.actions.followup.FollowupActionService<?>> getFollowupActionServiceClassHook() {
        return FollowupActionService.class;
    }

    @Override
    protected ProtocolQuestionnairePrintingService getProtocolQuestionnairePrintingServiceHook() {
        return KraServiceLocator.getService(ProtocolQuestionnairePrintingService.class);
    }

    @Override
    protected ProtocolModuleQuestionnaireBeanBase getNewProtocolModuleQuestionnaireBeanInstanceHook(ProtocolBase protocol) {
        return new ProtocolModuleQuestionnaireBean((Protocol) protocol);
    }

    @Override
    protected void enableModuleOption(org.kuali.kra.protocol.actions.amendrenew.ProtocolAmendmentBean amendmentBean, ProtocolAmendRenewalBase correctAmendment) {
        // TODO deliberately empty implementation since the calling method for this hook has been overridden making it a redundant
        // hook. See backfit notes.
        // do nothing
    }

    
// TODO ********************** commented out during IRB backfit ************************    
//    protected KraAuthorizationService getKraAuthorizationService() {
//        if (this.kraAuthorizationService == null) {
//            this.kraAuthorizationService = KraServiceLocator.getService(KraAuthorizationService.class);
//        }
//        
//        return this.kraAuthorizationService;
//    }
//
//    public ProtocolGenericActionBean getProtocolReturnToPIBean() {
//        return protocolReturnToPIBean;
//    }
//
//    public boolean isCanReturnToPI() {
//        return canReturnToPI;
//    }
//
//    public boolean isCanReturnToPIUnavailable() {
//        return canReturnToPIUnavailable;
//    }
    

}
