package com.quangnam.example;

import org.apache.synapse.MessageContext; 
import org.apache.synapse.mediators.AbstractMediator;
import org.apache.synapse.commons.json.JsonUtil;
import org.apache.synapse.core.axis2.Axis2MessageContext;

import org.json.JSONObject;

import com.quangnam.example.utils.EdocUtils;
import com.quangnam.example.utils.KnobstickServiceExtImp;
import com.vpcp.services.model.GetEdocResult;

public class EdocMediator extends AbstractMediator { 
	
	public boolean mediate(MessageContext context) { 
		getEdoc(context);
		return true;
	}
	
	@SuppressWarnings("deprecation")
	private void getEdoc(MessageContext context) {
		String docId = (String) context.getProperty("docId");
		String systemId = (String) context.getProperty("systemId");
		String secret = (String) context.getProperty("secret");
		KnobstickServiceExtImp knobstickService = new KnobstickServiceExtImp(systemId, secret);
		String jsonHeader = EdocUtils.createHeaderJsonGetDoc(docId);
		
		GetEdocResult getEdocResult = knobstickService.getEdoc(jsonHeader);
		
		//Tạo json payload
	    String jsonPayloadToString = JsonUtil.jsonPayloadToString(((Axis2MessageContext) context).getAxis2MessageContext());
	    JSONObject jsonBody = new JSONObject(jsonPayloadToString);

	    //Nhap du lieu vao json payload
		if(getEdocResult!=null) {			
			jsonBody.put("data", getEdocResult.getData());
		    jsonBody.put("filePath", getEdocResult.getFilePath());	
		}
	    jsonBody.put("status", getEdocResult.getStatus());
	    jsonBody.put("errorCode", getEdocResult.getErrorCode());
	    jsonBody.put("errorDesc", getEdocResult.getErrorDesc());

	    String transformedJson = jsonBody.toString();

	    //Day du lieu
	    JsonUtil.newJsonPayload(((Axis2MessageContext) context).getAxis2MessageContext(),transformedJson, true, true);
	}
}

