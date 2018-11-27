package com.quangnam.example;

import org.apache.synapse.MessageContext; 
import org.apache.synapse.mediators.AbstractMediator;
import org.apache.synapse.commons.json.JsonUtil;
import org.apache.synapse.core.axis2.Axis2MessageContext;

import org.json.JSONObject;

import com.vpcp.services.AgencyServiceImp;
import com.vpcp.services.model.DeleteAgencyResult;

public class DeleteAgencyMediator extends AbstractMediator { 
	
	public boolean mediate(MessageContext context) { 
		registerAgency(context);
		return true;
	}
	
	@SuppressWarnings("deprecation")
	private void registerAgency(MessageContext context) {
		String agencyCode = (String) context.getProperty("agencyCode");
		
		
		AgencyServiceImp agencyService = new AgencyServiceImp();
		
		String jsonHeader = "{\"AgencyCode\":\""+agencyCode+"\"}";
		
		DeleteAgencyResult deleteAgencyResult = agencyService.deleteAgency(jsonHeader);
		
		//Tạo json payload
	    String jsonPayloadToString = JsonUtil.jsonPayloadToString(((Axis2MessageContext) context).getAxis2MessageContext());
	    JSONObject jsonBody = new JSONObject(jsonPayloadToString);
	    
	    jsonBody.put("status", deleteAgencyResult.getStatus());
	    jsonBody.put("errorCode", deleteAgencyResult.getErrorCode());
	    jsonBody.put("errorDesc", deleteAgencyResult.getErrorDesc());

	    String transformedJson = jsonBody.toString();

	    //Day du lieu
	    JsonUtil.newJsonPayload(((Axis2MessageContext) context).getAxis2MessageContext(),transformedJson, true, true);
	}
}

