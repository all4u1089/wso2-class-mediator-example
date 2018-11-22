package com.quangnam.example;

import org.apache.synapse.MessageContext; 
import org.apache.synapse.mediators.AbstractMediator;
import org.apache.synapse.commons.json.JsonUtil;
import org.apache.synapse.core.axis2.Axis2MessageContext;

import org.json.JSONObject;
import com.vpcp.services.AgencyServiceImp;
import com.vpcp.services.model.GetAgenciesResult;

public class AgenciesListMediator extends AbstractMediator { 
	
	public boolean mediate(MessageContext context) { 
		getAgenciesList(context);
		return true;
	}
	
	@SuppressWarnings("deprecation")
	private void getAgenciesList(MessageContext context) {
		AgencyServiceImp agencyService = new AgencyServiceImp();
		String jsonHeader = "{}";
		//Gọi hàm getAgenciesList với tham số là "{}"
		GetAgenciesResult getAgenciesResult = agencyService.getAgenciesList(jsonHeader);
		
		//Tạo json payload
	    String jsonPayloadToString = JsonUtil.jsonPayloadToString(((Axis2MessageContext) context).getAxis2MessageContext());
	    JSONObject jsonBody = new JSONObject(jsonPayloadToString);

	    //Nhap du lieu vao json payload
	    jsonBody.put("data", getAgenciesResult.getAgencies());
	    jsonBody.put("status", getAgenciesResult.getStatus());
	    jsonBody.put("errorCode", getAgenciesResult.getErrorCode());
	    jsonBody.put("errorDesc", getAgenciesResult.getErrorDesc());

	    String transformedJson = jsonBody.toString();

	    //Day du lieu
	    JsonUtil.newJsonPayload(((Axis2MessageContext) context).getAxis2MessageContext(),transformedJson, true, true);
	}
}

