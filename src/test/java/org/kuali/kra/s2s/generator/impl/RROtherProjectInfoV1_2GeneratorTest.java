/*
 * Copyright 2005-2010 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
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
package org.kuali.kra.s2s.generator.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.kuali.kra.bo.Organization;
import org.kuali.kra.proposaldevelopment.bo.DevelopmentProposal;
import org.kuali.kra.proposaldevelopment.bo.Narrative;
import org.kuali.kra.proposaldevelopment.bo.NarrativeAttachment;
import org.kuali.kra.proposaldevelopment.bo.NarrativeType;
import org.kuali.kra.proposaldevelopment.bo.ProposalSite;
import org.kuali.kra.proposaldevelopment.bo.ProposalYnq;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;
import org.kuali.kra.proposaldevelopment.specialreview.ProposalSpecialReview;
import org.kuali.kra.s2s.generator.S2STestBase;
import org.kuali.kra.s2s.generator.util.S2STestUtils;
import org.kuali.rice.kns.service.BusinessObjectService;

/**
 * 
 * This class is to test RROtherProjectInfoV1_2 form
 */

public class RROtherProjectInfoV1_2GeneratorTest extends
		S2STestBase<RROtherProjectInfoV1_0Generator> {

	@Override
	protected Class<RROtherProjectInfoV1_0Generator> getFormGeneratorClass() {
		return RROtherProjectInfoV1_0Generator.class;
	}

	@Override
	protected void prepareData(ProposalDevelopmentDocument document)
			throws Exception {
		DevelopmentProposal developmentProposal = document
				.getDevelopmentProposal();
		ProposalSpecialReview specialReview = new ProposalSpecialReview();
		specialReview.setSpecialReviewNumber(1);
		specialReview.setSpecialReviewTypeCode("1");
		specialReview.setComments("E1,E2,E3");
		specialReview.setApprovalTypeCode("4");
		specialReview.setApplicationDate(new Date(0));
		specialReview.getExemptionTypeCodes().add("1");
		specialReview.getExemptionTypeCodes().add("2");
		specialReview.getExemptionTypeCodes().add("3");
		Organization organization = new Organization();
		organization.setHumanSubAssurance("Human");
		ProposalSpecialReview specialReview1 = new ProposalSpecialReview();
		specialReview1.setSpecialReviewNumber(2);
		specialReview1.setSpecialReviewTypeCode("2");
		specialReview1.setApplicationDate(new Date(0));
		specialReview1.setApprovalTypeCode("4");
		Organization organization1 = new Organization();
		organization1.setHumanSubAssurance("Human");
		List<ProposalSpecialReview> speList = new ArrayList<ProposalSpecialReview>();
		speList.add(specialReview);
		speList.add(specialReview1);
		developmentProposal.setApplicantOrgFromOrganization(organization);
		developmentProposal.setApplicantOrgFromOrganization(organization1);
		developmentProposal.setPropSpecialReviews(speList);
		ProposalYnq proposalYnq = new ProposalYnq();
		proposalYnq.setQuestionId("P002");
		proposalYnq.setAnswer("Y");
		ProposalYnq proposalYnq2 = new ProposalYnq();
		proposalYnq2.setQuestionId("P003");
		proposalYnq2.setAnswer("Y");
		proposalYnq2.setExplanation("Environmental Impact");
		ProposalYnq proposalYnq3 = new ProposalYnq();
		proposalYnq3.setQuestionId("P007");
		proposalYnq3.setAnswer("Y");
		proposalYnq3.setExplanation("Environmental Exemption");
		ProposalYnq proposalYnq4 = new ProposalYnq();
		proposalYnq4.setQuestionId("P008");
		proposalYnq4.setAnswer("Y");
		proposalYnq4.setExplanation("International Activities");
		ProposalYnq proposalYnq5 = new ProposalYnq();
		proposalYnq5.setQuestionId("G6");
		proposalYnq5.setAnswer("Y");
		proposalYnq5.setExplanation("Historic Destination");
		List<ProposalYnq> proList = new ArrayList<ProposalYnq>();
		proList.add(proposalYnq);
		proList.add(proposalYnq2);
		proList.add(proposalYnq3);
		proList.add(proposalYnq5);
		developmentProposal.setProposalYnqs(proList);

		Narrative narrative = new Narrative();
		List<Narrative> naList = new ArrayList<Narrative>();
		NarrativeAttachment narrativeAttachment = new NarrativeAttachment();
		File file = new File(S2STestUtils.ATT_DIR_PATH + "exercise1.pdf");
		InputStream inStream = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(inStream);
		byte[] narrativePdf = new byte[bis.available()];
		narrativeAttachment.setNarrativeData(narrativePdf);
		List<NarrativeAttachment> narrativeList = new ArrayList<NarrativeAttachment>();
		narrativeList.add(narrativeAttachment);
		narrative.setProposalNumber(developmentProposal.getProposalNumber());
		narrative.setModuleNumber(1);
		narrative.setModuleSequenceNumber(1);
		narrative.setModuleStatusCode("C");
		narrative.setNarrativeTypeCode("3");
		narrative.setNarrativeAttachmentList(narrativeList);
		narrative.setObjectId("12345678890abcd");
		narrative.setFileName("exercise1");
		NarrativeType narrativeType = new NarrativeType();
		narrativeType.setDescription("Testing for Project Attachment");
		narrative.setNarrativeType(narrativeType);
		naList.add(narrative);
		getService(BusinessObjectService.class).save(narrative);
		narrative.getNarrativeAttachmentList().clear();
		developmentProposal.setNarratives(naList);
		document.setDevelopmentProposal(developmentProposal);
		// developmentProposal.setPerformingOrganization has side effect logic.
		// It creates proposalSite object for each organization without setting
		// the mandatory field - LOCATION_NAME. The code below sets location
		// name.
		int count = 0;
		for (ProposalSite site : developmentProposal.getProposalSites()) {
			site.setLocationName("NJ");
			site.setSiteNumber(++count);
		}
	}
}
