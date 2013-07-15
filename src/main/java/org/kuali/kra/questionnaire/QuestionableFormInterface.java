/*
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.kra.questionnaire;

/**
 * 
 * This class determines what functions must be present on a form that uses questionnaire.
 * KraTransactionalFormBase.populateFalseCheckboxes will call these function to determine if a field is
 * associated with a questionnaire answer field.
 */
public interface QuestionableFormInterface {
    public static final String DEFAULT_MIDDLE = "].answers[";
    public static final String DEFAULT_END = "].answer";
    
    public String getQuestionnaireFieldStarter();
    public String getQuestionnaireFieldMiddle();
    public String getQuestionnaireFieldEnd();
    public String getQuestionnaireExpression();
}