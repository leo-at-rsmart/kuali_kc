<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>

<c:set var="budgetCostShareAttributes" value="${DataDictionary.BudgetCostShare.attributes}" />

<kul:tabTop tabTitle="Cost Sharing (${KualiForm.document.budgetCostShareCount})" defaultOpen="false" tabErrorKey="newCostShare*,costShare*">
	<div class="tab-container" align="center">
		<c:choose>
			<c:when test="${KualiForm.costSharingEditFormVisible}">
			<div id="costSharingAvailable">
				<div class="h2-container">
			    	<div class="h2-container">
			    		<span class="subhead-left"><h2>Cost Sharing Distribution List</h2></span>
			    		<span class="subhead-right"><kul:help businessObjectClassName="fillMeIn" altText="help"/></span>
			        </div>
			    </div>
				<div align="center">
					<table id="budget-cost-sharing-table" cellpadding="0" cellspacing="0" summary="Budget Cost Shares">
		
						<tr>
							<th width="5%"><div align="left">&nbsp</div></th>
							<th width="17%"><div align="center">Fiscal Year</div></th>
							<th width="17%"><div align="center">Percentage</div></th>
							<th width="29%"><div align="center">Source Account</div></th>
							<th width="17%"><div align="center">Amount</div></th>					
							<th width="15%"><div align="center">Actions</div></th>	
						</tr>
						<tr>
			            	<th width="50" align="right"><div align="right">Add:</div></th>
							<td class="infoline"><div align="center">
			        			<kul:htmlControlAttribute property="newBudgetCostShare.fiscalYear" attributeEntry="${budgetCostShareAttributes.fiscalYear}" />
			        		</div></td>
			        		<td class="infoline"><div align="center">
								<kul:htmlControlAttribute property="newBudgetCostShare.sharePercentage" attributeEntry="${budgetCostShareAttributes.sharePercentage}" />						
			    			</div></td>
			    			<td class="infoline"><div align="center">
			        			<kul:htmlControlAttribute property="newBudgetCostShare.sourceAccount" attributeEntry="${budgetCostShareAttributes.sourceAccount}" />
			        		</div></td>
			        		<td class="infoline"><div align="center">
			        			<kul:htmlControlAttribute property="newBudgetCostShare.shareAmount" attributeEntry="${budgetCostShareAttributes.shareAmount}" styleClass="amount" />
			        		</div></td>	        		
			                <td class="infoline">
			            		<div align=center>
			            			<html:image property="methodToCall.addCostShare" src='${ConfigProperties.kra.externalizable.images.url}tinybutton-add1.gif' />
								</div>
							</td>
			          	</tr>
					          	
			  			<c:forEach var="budgetCostShare" items="${KualiForm.document.budgetCostShares}" varStatus="status">
			          		<tr>
			          			<th><div align="right">${status.index + 1}</div></th>
			            		
			            		<td><div align="center">
									<kul:htmlControlAttribute property="document.budgetCostShares[${status.index}].fiscalYear" attributeEntry="${budgetCostShareAttributes.fiscalYear}" />            				
			        			</div></td>
			        			
			            		<td><div align="center">
			            			<kul:htmlControlAttribute property="document.budgetCostShares[${status.index}].sharePercentage" attributeEntry="${budgetCostShareAttributes.sharePercentage}" styleClass="amount" />
			    				</div></td>
			            		
			            		<td><div align="center">
			        				<kul:htmlControlAttribute property="document.budgetCostShares[${status.index}].sourceAccount" attributeEntry="${budgetCostShareAttributes.sourceAccount}" />
			        			</div></td>
			            		
			            		<td><div align="center">
			        				<kul:htmlControlAttribute property="document.budgetCostShares[${status.index}].shareAmount" attributeEntry="${budgetCostShareAttributes.shareAmount}" styleClass="amount" />
			        			</div></td>
			        				        			
			            		<td>
			            			<div align=center>
			            				<c:if test="${!viewOnly and fn:length(KualiForm.document.budgetCostShares) > 0}">
										  	<html:image property="methodToCall.deleteCostShare.line${status.index}" src='${ConfigProperties.kra.externalizable.images.url}tinybutton-delete1.gif' title="Delete a Cost Share" alt="Delete a Cost Share" styleClass="tinybutton" />
										</c:if>
									</div>
			            		</td>
			         		</tr>
			         		
			          	</c:forEach>
			          	<tr>
			          		<th colspan="4" class="infoline"><div align="right">Total Allocated:</div></th>
						    <td><div align="right"><span class="amount">${KualiForm.document.availableCostSharing}</span></div></td></td>
						    <th>&nbsp;</th>	          		
			          	</tr>
			          	<tr>
			          		<th colspan="4" class="infoline"><div align="right">Unallocated:</div></th>
						    <td><div align="right"><span class="amount">${KualiForm.document.unallocatedCostSharing}</span></div></td>
						    <th>&nbsp;</th>
			          	</tr>
			        </table>
				</div>			
			    
				<div class="h2-container">
					<span class="subhead-left"><h2>Cost Sharing Summary</h2></span>		    		
				</div>
			
				<div align="center">
					<table id="budget-cost-sharing-summary-table" cellpadding="0" cellspacing="0" summary="Cost Sharing Amounts to be Allocated">
			    		<c:forEach var="fiscalYearCostShare" items="${KualiForm.document.fiscalYearCostShareTotals}" varStatus="status">
							<tr>
						    	<th width="68.5%"><div align="right">Period ${status.index + 1}: ${fiscalYearCostShare.assignedBudgetPeriod.dateRangeLabel}:</div></th>
						    	<td width="17%"><div align="right"><span class="amount">${fiscalYearCostShare.costShare}</span></div></td>
						    	<th width="14.5%">&nbsp;</th>
						    </tr>
					    </c:forEach>
					</table>
					
					<div align="center" style="padding-top: 2em;">
						<html:image property="methodToCall.resetCostSharingToDefault" src='${ConfigProperties.kra.externalizable.images.url}tinybutton-resettodefault.gif' />
						<html:image property="methodToCall.refreshTotals" src='${ConfigProperties.kra.externalizable.images.url}tinybutton-recalculate.gif' />
					</div>
				</div>
			</div>
			</c:when>
			<c:otherwise>
				<div align="center">Cost Sharing doesn't apply or is not available</div>
			</c:otherwise>
		</c:choose>
	</div>	
</kul:tabTop>