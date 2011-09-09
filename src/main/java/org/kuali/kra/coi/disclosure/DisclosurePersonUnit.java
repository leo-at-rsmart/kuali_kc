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
package org.kuali.kra.coi.disclosure;

import java.util.LinkedHashMap;

import org.apache.commons.lang.StringUtils;
import org.kuali.kra.bo.Unit;
import org.kuali.kra.coi.DisclosureReporterUnit;

public class DisclosurePersonUnit  extends DisclosureReporterUnit {

    private Long disclosurePersonUnitsId; 
    private Long disclosurePersonId;
    private String unitNumber; 
    private boolean leadUnitFlag; 
    private String personId; 
    
//    @SkipVersioning
    private DisclosurePerson disclosurePerson;

@Override
protected LinkedHashMap toStringMapper() {
    // TODO Auto-generated method stub
    return null;
}

public Long getDisclosurePersonUnitsId() {
    return disclosurePersonUnitsId;
}

public void setDisclosurePersonUnitsId(Long disclosurePersonUnitsId) {
    this.disclosurePersonUnitsId = disclosurePersonUnitsId;
}

public Long getDisclosurePersonId() {
    return disclosurePersonId;
}

public void setDisclosurePersonId(Long disclosurePersonId) {
    this.disclosurePersonId = disclosurePersonId;
}

public String getUnitNumber() {
    return unitNumber;
}

public void setUnitNumber(String unitNumber) {
    this.unitNumber = unitNumber;
}

public boolean isLeadUnitFlag() {
    return leadUnitFlag;
}

public void setLeadUnitFlag(boolean leadUnitFlag) {
    this.leadUnitFlag = leadUnitFlag;
}

public String getPersonId() {
    return personId;
}

public void setPersonId(String personId) {
    this.personId = personId;
}

public DisclosurePerson getDisclosurePerson() {
    return disclosurePerson;
}

public void setDisclosurePerson(DisclosurePerson disclosurePerson) {
    this.disclosurePerson = disclosurePerson;
} 
       


}
