package com.acn.sap_citest;

// Create ./src/com/test/TestScript.groovy

import com.sap.gateway.ip.core.customdev.util.Message
import com.sap.it.api.ITApiFactory
import com.sap.it.api.mapping.ValueMappingApi

/**
 * Get whether Message mapping is required or not for message to respective ERP system
 *
 * @param message
 * @return
 */
def Message processData(Message message) {
	//Source value will be <SAP-SID>_<MessageType>
	String sourceValue = message.getProperty("SAPSID") + "_" + message.getHeader("SAP_MessageType", String.class)
	//Get message mapping name need to execute if required(VM present in same package)
	String mappingName = getValueMapping("MDG", "MessageType", sourceValue, "ERP", "MappingName", "")
	
	if(!mappingName.isEmpty()) {
		//Set isMappingNeeded based on VM configuration
		message.setProperty("isMappingNeeded", "true")
		//Set message mapping to property
		message.setProperty("mappingName", mappingName)
	} else
		message.setProperty("isMappingNeeded", "false")
		
	return message
}

/**
 * Get value mapping using ValueMappingApi.
 *
 * @param sourceAgency      The source agency for value mapping.
 * @param sourceIdentifier  The source identifier for value mapping.
 * @param sourceValue       The source value for value mapping.
 * @param targetAgency      The target agency for value mapping.
 * @param targetIdentifier  The target identifier for value mapping.
 * @param defaultValue      The default value for value mapping.
 * @return The mapped value or the default value.
 * @throws Exception If there are issues with the ValueMappingApi or if $_THROW_ERROR_$ is specified as the defaultValue.
 */
private String getValueMapping(String sourceAgency, String sourceIdentifier, String sourceValue, String targetAgency, String targetIdentifier, String defaultValue) {
	ValueMappingApi service = ITApiFactory.getService(ValueMappingApi.class, sourceValue)
	if(service!=null) {
		String returnValue = service.getMappedValue(sourceAgency, sourceIdentifier, sourceValue, targetAgency, targetIdentifier)
		if(returnValue!=null && !returnValue.isEmpty())
			return returnValue
		else if(defaultValue.equals("\$_THROW_ERROR_\$"))
			throw new Exception("Maintain value mapping for source (agency: ${sourceAgency}, identifier: ${sourceIdentifier} & value: ${sourceValue}) to target (agency: ${targetAgency} & identifier: ${targetIdentifier})")
		else
			return defaultValue
	}
	else
		throw new Exception("API Factory class is not retrieving ValueMappingApi.")
}