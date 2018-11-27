package com.quangnam.example;

import org.apache.synapse.MessageContext; 
import org.apache.synapse.mediators.AbstractMediator;
import org.apache.synapse.commons.json.JsonUtil;
import org.apache.synapse.core.axis2.Axis2MessageContext;

import org.json.JSONObject;

import com.vpcp.services.AgencyServiceImp;
import com.vpcp.services.model.RegisterAgencyResult;

public class RegisterAgencyMediator extends AbstractMediator { 
	
	public boolean mediate(MessageContext context) { 
		registerAgency(context);
		return true;
	}
	
	@SuppressWarnings("deprecation")
	private void registerAgency(MessageContext context) {
		String id = (String) context.getProperty("id");
		String pid = (String) context.getProperty("pid");
		String code = (String) context.getProperty("code");
		String name = (String) context.getProperty("name");
		String mail = (String) context.getProperty("mail");
		String mobile = (String) context.getProperty("mobile");
		String fax = (String) context.getProperty("fax");
		
		
		AgencyServiceImp agencyService = new AgencyServiceImp();
		
		String jsonHeader = "{}";
		
		//String edXMLFileLocation = "./xml/952f8a6a-22fc-4458-af83-18bbbdaf722a.edxml";
		
		String data = "{\"id\":\"" + id + "\",\"pid\":\"" + pid + "\",\"code\":\"" + code + "\",\"name\":\"" + name + "\",\"mail\":\"" + mail + "\",\"mobile\":\"" + mobile + "\",\"fax\":\"" + fax +"\"}";
		RegisterAgencyResult registerAgencyResult = agencyService.registerAgency(jsonHeader, data);
		
		//Tạo json payload
	    String jsonPayloadToString = JsonUtil.jsonPayloadToString(((Axis2MessageContext) context).getAxis2MessageContext());
	    JSONObject jsonBody = new JSONObject(jsonPayloadToString);
	    jsonBody.remove("data");
	    jsonBody.put("status", registerAgencyResult.getStatus());
	    jsonBody.put("errorCode", registerAgencyResult.getErrorCode());
	    jsonBody.put("errorDesc", registerAgencyResult.getErrorDesc());

	    String transformedJson = jsonBody.toString();

	    //Day du lieu
	    JsonUtil.newJsonPayload(((Axis2MessageContext) context).getAxis2MessageContext(),transformedJson, true, true);
	}
}

