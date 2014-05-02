/*
 * Copyright 2005-2014 The Kuali Foundation
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
package com.rsmart.kuali.kra.web.jstl;

import java.util.Arrays;
import java.util.List;

import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliation;
import org.kuali.rice.kim.impl.identity.PersonImpl;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;

public class PortalFunctions {

	//STAFF,FCLTY,AFLT, etc...
	public static boolean showByAffiliateType(String types){
		boolean success = false;
		PersonImpl currentUser = (PersonImpl) GlobalVariables.getUserSession().getPerson();
		
		if (ObjectUtils.isNull(currentUser)){
			return false;
		}
		List<String> typesList = Arrays.asList(types.split(","));
		for (int i=0;i< currentUser.getAffiliations().size();i++){
			if (currentUser.getAffiliations().get(i).isDefaultValue() && currentUser.getAffiliations().get(i).isActive()){
				if (typesList.contains(currentUser.getAffiliations().get(i).getAffiliationType().getCode())){
					success = true;
				}
				break;
			}
		}
		return success;
	}
}
