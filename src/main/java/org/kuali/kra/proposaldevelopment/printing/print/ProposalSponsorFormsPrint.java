/*
 * Copyright 2006-2008 The Kuali Foundation
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
package org.kuali.kra.proposaldevelopment.printing.print;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.xmlbeans.XmlObject;
import org.kuali.kra.bo.SponsorFormTemplate;
import org.kuali.kra.document.ResearchDocumentBase;
import org.kuali.kra.printing.PrintingException;
import org.kuali.kra.printing.print.AbstractPrint;
import org.kuali.kra.printing.util.PrintingUtils;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;
import org.kuali.kra.proposaldevelopment.printing.service.impl.ProposalDevelopmentPrintingServiceImpl;
import org.kuali.kra.proposaldevelopment.printing.xmlstream.NIHResearchAndRelatedXmlStream;
import org.kuali.kra.proposaldevelopment.printing.xmlstream.ProposalDevelopmentXmlStream;
import org.kuali.kra.proposaldevelopment.printing.xmlstream.ResearchAndRelatedXmlStream;
import org.kuali.kra.s2s.S2SException;
import org.kuali.rice.kns.service.BusinessObjectService;

/**
 * This class provides the implementation for printing Print Certification
 * Report. It generates XML that conforms with Certification Report XSD, fetches
 * XSL style-sheets applicable to this XML, returns XML and XSL for any consumer
 * that would use this XML and XSls for any purpose like report generation, PDF
 * streaming etc.
 * 
 */
public class ProposalSponsorFormsPrint extends AbstractPrint {
	private static final String LOCAL_PRINT_FORM_SPONSOR_CODE ="LOCAL_PRINT_FORM_SPONSOR_CODE";
    private BusinessObjectService businessObjectService;
    private NIHResearchAndRelatedXmlStream nihResearchAndRelatedXmlStream;
	private ProposalDevelopmentXmlStream proposalDevelopmentXmlStream;
    /**
	 * Fetches the {@link ResearchDocumentBase}
	 * 
	 * @return {@link ResearchDocumentBase} document
	 */
	public ResearchDocumentBase getDocument() {
		return document;
	}

	/**
	 * This method fetches the XSL style-sheets required for transforming the
	 * generated XML into PDF.
	 * 
	 * @return {@link ArrayList}} of {@link Source} XSLs
	 */
	public Map<String,Source> getXSLTemplateWithBookmarks() {
		Map<String,Source> sourceMap = new LinkedHashMap<String,Source>(); 
		List<SponsorFormTemplate> printFormTemplates = (List<SponsorFormTemplate>)getReportParameters().get(ProposalDevelopmentPrintingServiceImpl.SELECTED_TEMPLATES);
		for (SponsorFormTemplate sponsorFormTemplate : printFormTemplates) {
		    Map<String,Object> htData = new HashMap<String,Object>();
		    htData.put("sponsorCode", sponsorFormTemplate.getSponsorCode());
		    htData.put("packageNumber", sponsorFormTemplate.getPackageNumber());
		    htData.put("pageNumber", sponsorFormTemplate.getPageNumber());
		    SponsorFormTemplate sponsorTemplate = (SponsorFormTemplate)getBusinessObjectService().findByPrimaryKey(SponsorFormTemplate.class, htData);
		    sourceMap.put(sponsorFormTemplate.getPageDescription(),new StreamSource(new ByteArrayInputStream(sponsorTemplate.getAttachmentContent())));
        }
		return sourceMap;
	}

    
    /**
     * Gets the businessObjectService attribute. 
     * @return Returns the businessObjectService.
     */
    public BusinessObjectService getBusinessObjectService() {
        return businessObjectService;
    }

    /**
     * Sets the businessObjectService attribute value.
     * @param businessObjectService The businessObjectService to set.
     */
    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

	public ProposalDevelopmentXmlStream getProposalDevelopmentXmlStream() {
		return proposalDevelopmentXmlStream;
	}

	public void setProposalDevelopmentXmlStream(
			ProposalDevelopmentXmlStream proposalDevelopmentXmlStream) {
		this.proposalDevelopmentXmlStream = proposalDevelopmentXmlStream;
	}
	
	@Override
	public Map<String, byte[]> renderXML() throws PrintingException {
		ProposalDevelopmentDocument pdDoc=(ProposalDevelopmentDocument) document;
		if (pdDoc.getDevelopmentProposal().getSponsorCode().equals(getProposalParameterValue(LOCAL_PRINT_FORM_SPONSOR_CODE))){
			setXmlStream(proposalDevelopmentXmlStream);
		} else {
			setXmlStream(nihResearchAndRelatedXmlStream);
		}
			
		return super.renderXML();
	}
	
	private String getProposalParameterValue(String param) {
		String value = null;
		try {
			value = PrintingUtils.getParameterValue(param);
		} catch (Exception e) {
			//TODO Log Exception
		}
		return value;
	}

	public NIHResearchAndRelatedXmlStream getNihResearchAndRelatedXmlStream() {
		return nihResearchAndRelatedXmlStream;
	}

	public void setNihResearchAndRelatedXmlStream(
			NIHResearchAndRelatedXmlStream nihResearchAndRelatedXmlStream) {
		this.nihResearchAndRelatedXmlStream = nihResearchAndRelatedXmlStream;
	}
}
