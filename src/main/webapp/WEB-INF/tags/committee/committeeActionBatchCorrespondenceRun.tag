<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>

<%@ attribute name="committeeBatchCorrespondence" description="The committee batch correspondence." required="true" %>
<%@ attribute name="index" description="The index." required="true" %>

<c:set var="committeeBatchCorrespondenceDetailAttributes" value="${DataDictionary.CommitteeBatchCorrespondenceDetail.attributes}" />
<c:set var="protocolAttributes" value="${DataDictionary.Protocol.attributes}" />

<c:set var="batchRunTitle" value="${KualiForm.committeeHelper.committeeActionsHelper.batchCorrespondenceHistory[index].timeWindowStart} through ${KualiForm.committeeHelper.committeeActionsHelper.batchCorrespondenceHistory[index].timeWindowStart}" />

<kra:innerTab tabTitle="${batchRunTitle}" 
              parentTab="$Batch Correspondence" 
              defaultOpen="false"
              useCurrentTabIndexAsKey="true" 
              tabErrorKey="">
    <div align="left" style="padding-left: 56px;">
        <table class=tab cellpadding=0 cellspacing="0"> 
            <tr>
                <th>
                    <div align="right">
                        Run Date:
                    </div>
                </th>
                <td>
                    <div align="left">
                        <kul:htmlControlAttribute property="committeeHelper.committeeActionsHelper.batchCorrespondenceHistory[${index}].batchRunDate" 
                                                  attributeEntry="${kraAttributeReferenceDummyAttributes.genericDate}" 
                                                  readOnly="true" />
                    </div>
                </td>
                <th>
                    <div align="right">
                        Run Time:
                    </div>
                </th>
                <td> 
                    <div align="left">
                        <kul:htmlControlAttribute property="committeeHelper.committeeActionsHelper.batchCorrespondenceHistory[${index}].batchRunDate" 
                                                  attributeEntry="${kraAttributeReferenceDummyAttributes.genericDate}" 
                                                  readOnly="true" />
                    </div>
                </td>
            </tr>
            <tr>
                <th>
                    <div align="right">
                        User ID:
                    </div>
                </th>
                <td>
                    <div align="left">
                        <kul:htmlControlAttribute property="committeeHelper.committeeActionsHelper.batchCorrespondenceHistory[${index}].updateUser" 
                                                  attributeEntry="${committeeAttributes.committeeName}" 
                                                  readOnly="true" />
                    </div>
                </td>
                <th>
                    <div align="right">
                        Batch ID:
                    </div>
                </th>
                <td>
                    <div align="left">
                        <kul:htmlControlAttribute property="committeeHelper.committeeActionsHelper.batchCorrespondenceHistory[${index}].committeeBatchCorrespondenceId" 
                                                  attributeEntry="${committeeAttributes.committeeName}" 
                                                  readOnly="true" />
                    </div>
                </td>
            </tr>
        </table>
        
        <table class=tab cellpadding=0 cellspacing="0"> 
            <tr>
                <th width ="25">
                    <div align="center">
                    </div>
                </th>
                <th width="75">
                    <div align="center">
                        Protocol Number
                    </div>
                </th>
                <th>
                    <div align="center">
                        Title
                    </div>
                </th>
                <th width="75">
                    <div align="center">
                        Approval Date
                    </div>
                </th>
                <th width="75">
                    <div align="center">
                        Expiration Date
                    </div>
                </th>
                <th width="40%">
                    <div align="center">
                        Description
                    </div>
                </th>
                <th width="50">
                    <div align="center">
                        Actions
                    </div>
                </th>
            </tr>
            
        <c:forEach items="${KualiForm.committeeHelper.committeeActionsHelper.batchCorrespondenceHistory[index].committeeBatchCorrespondenceDetails}" var="batchCorrespondenceDetails" varStatus="status">
            <tr>
                <th>
                    <div align="right">
                        ${status.index + 100}
                    </div>
                </th>
                <td>
                    <div align="center">
                        <kul:htmlControlAttribute property="committeeHelper.committeeActionsHelper.batchCorrespondenceHistory[${index}].committeeBatchCorrespondenceDetails[${status.index}].protocolCorrespondence.protocol.protocolNumber" 
                                                  attributeEntry="${protocolAttributes.protocolNumber}" 
                                                  readOnly="true" />
                    </div>
                </td>
                <td>
                    <div align="left">
                        <kul:htmlControlAttribute property="committeeHelper.committeeActionsHelper.batchCorrespondenceHistory[${index}].committeeBatchCorrespondenceDetails[${status.index}].protocolCorrespondence.protocol.title" 
                                                  attributeEntry="${protocolAttributes.title}" 
                                                  readOnly="true" />
                    </div>
                </td>
                <td>
                    <div align="center">
                        <kul:htmlControlAttribute property="committeeHelper.committeeActionsHelper.batchCorrespondenceHistory[${index}].committeeBatchCorrespondenceDetails[${status.index}].protocolCorrespondence.protocol.approvalDate" 
                                                  attributeEntry="${protocolAttributes.approvalDate}" 
                                                  readOnly="true" />
                    </div>
                </td>
                <td>
                    <div align="center">
                        <kul:htmlControlAttribute property="committeeHelper.committeeActionsHelper.batchCorrespondenceHistory[${index}].committeeBatchCorrespondenceDetails[${status.index}].protocolCorrespondence.protocol.expirationDate" 
                                                  attributeEntry="${protocolAttributes.expirationDate}" 
                                                  readOnly="true" />
                    </div>
                </td>
                <td>
                    <div align="left">
                        <kul:htmlControlAttribute property="committeeHelper.committeeActionsHelper.batchCorrespondenceHistory[${index}].committeeBatchCorrespondenceDetails[${status.index}].protocolCorrespondence.protocolCorrespondenceType.description" 
                                                  attributeEntry="${protocolAttributes.expirationDate}" 
                                                  readOnly="true" />
                    </div>
                </td>
                <td>
                    <div align="center">
                        <kul:htmlControlAttribute property="committeeHelper.committeeActionsHelper.batchCorrespondenceHistory[${index}].committeeBatchCorrespondenceDetails[${status.index}].selected" 
                                                  attributeEntry="${committeeBatchCorrespondenceDetailAttributes.selected}" 
                                                  readOnly="false" />
                    </div>                         
                </td>
            </tr>
        </c:forEach>

        </table>
    </div>
</kra:innerTab>