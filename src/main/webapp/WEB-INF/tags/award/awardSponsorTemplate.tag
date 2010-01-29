<%--
 Copyright 2006-2009 The Kuali Foundation
 
 Licensed under the Educational Community License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.osedu.org/licenses/ECL-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
--%>
<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>
<c:set var="awardAttributes" value="${DataDictionary.AwardDocument.attributes}" />
<c:set var="awardApprovedSubawardAttributes" value="${DataDictionary.AwardTemplate.attributes}" />
<c:set var="action" value="awardTemplateSync" />

<kul:tab tabTitle="Sponsor Template" defaultOpen="false" tabErrorKey="document.award.awardTemplate*">
	<div class="tab-container" align="center">
    	<h3>
    		<span class="subhead-left">Sponsor Template</span>
        </h3>
        <table id="sponsor-template-table" cellpadding="0" cellspacing="0" summary="Sponsor Template">
			<tr>
                <th width="50" align="center" scope="row"><div align="center">Select:</div></th>
            	<td class="infoline">
            		<div align="left">
            		<kul:htmlControlAttribute property="document.award.templateCode" attributeEntry="${awardAttributes.templateCode}" />
                    <c:out value="${KualiForm.document.award.awardTemplate.description}"/>
                    <c:if test="${!readOnly}">
                        <kul:lookup boClassName="org.kuali.kra.award.home.AwardTemplate" 
                        fieldConversions="templateCode:document.award.templateCode,description:document.award.awardTemplate.description" anchor="${currentTabIndex}"/>
                    </c:if> 
					<span class="fineprint">Note: Award data may have changed since Sponsor Template was applied </span>
					</div>
            	</td>
            </tr>
            <tr>
            	<th colspan="2" align="center" scope="row">
            		<div align="center">
            		  <c:if test="${(!readOnly)}">
	         			<kra-a:awardSyncButton propertyName = "" scopeNames="" tabKey="${tabKey}"/>
	        		   </c:if>
					</div>
	         	</th>
			</tr>
		</table>
	</div>
</kul:tab>
