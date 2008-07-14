/*
 * Copyright 2008 The Kuali Foundation.
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
package org.kuali.kra.s2s.service;

import java.util.List;

import org.kuali.kra.bo.SponsorFormTemplate;
import org.kuali.kra.bo.SponsorFormTemplateList;

/**
 * This class...
 */
public interface PrintService {
    public byte[] printProposalSponsorForms(String proposalNumber,List<SponsorFormTemplate> sponsorFormTemplates);
    public void populateSponsorForms(List<SponsorFormTemplateList> sponsorFormTemplates, String sponsorCode);
    public List<SponsorFormTemplate> getSponsorFormTemplates(List<SponsorFormTemplateList> sponsorFormTemplateLists);
}
