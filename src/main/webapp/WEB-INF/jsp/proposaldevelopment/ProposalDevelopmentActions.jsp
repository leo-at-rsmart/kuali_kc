<%--
 Copyright 2005-2006 The Kuali Foundation.

 Licensed under the Educational Community License, Version 1.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.opensource.org/licenses/ecl1.php

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
--%>
<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>

<kul:documentPage
	showDocumentInfo="true"
	htmlFormAction="proposalDevelopmentActions"
	documentTypeName="ProposalDevelopmentDocument"
	renderMultipart="false"
	showTabButtons="true"
	auditCount="0"
  	headerDispatch="save"
  	headerTabActive="actions">
	
	<kra-pd:proposalDevelopmentDataValidation /> 
	<kra-pd:proposalDevelopmentHierarchy /> 
	<kra-pd:proposalDevelopmentPrintForms /> 
	<kra-pd:proposalDevelopmentCopy /> 
	<kul:routeLog /> 
	<kul:adHocRecipients /> 
	<kul:panelFooter />
	<kul:documentControls transactionalDocument="true" suppressRoutingControls="true" />
	<script language="javascript" src="scripts/kuali_application.js"></script>

</kul:documentPage>