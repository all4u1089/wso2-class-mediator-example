package com.quangnam.example;

import org.apache.synapse.MessageContext; 
import org.apache.synapse.mediators.AbstractMediator;
import org.apache.synapse.commons.json.JsonUtil;
import org.apache.synapse.core.axis2.Axis2MessageContext;

import org.json.JSONObject;

import com.quangnam.example.utils.KnobstickServiceExtImp;
import com.vpcp.services.model.GetChangeStatusResult;

public class UpdateStatusMediator extends AbstractMediator { 
	
	public boolean mediate(MessageContext context) { 
		updateStatus(context);
		return true;
	}
	
	@SuppressWarnings("deprecation")
	private void updateStatus(MessageContext context) {
		String docId = (String) context.getProperty("docId");
		String systemId = (String) context.getProperty("systemId");
		String secret = (String) context.getProperty("secret");
		String status = (String) context.getProperty("status");
		KnobstickServiceExtImp knobstickService = new KnobstickServiceExtImp(systemId, secret);
		
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("{"); 
		stringBuffer.append("\"status\":\""+ status +"\"");
		stringBuffer.append(",\"docid\":"+ docId );
		stringBuffer.append("}"); 
		String jsonHeader = stringBuffer.toString();
		
		GetChangeStatusResult getChangeStatusResult = knobstickService.updateStatus(jsonHeader);
		
		//Tạo json payload
	    String jsonPayloadToString = JsonUtil.jsonPayloadToString(((Axis2MessageContext) context).getAxis2MessageContext());
	    JSONObject jsonBody = new JSONObject(jsonPayloadToString);

	    //Nhap du lieu vao json payload
	    jsonBody.put("status", getChangeStatusResult.getStatus());
	    jsonBody.put("errorCode", getChangeStatusResult.getErrorCode());
	    jsonBody.put("errorDesc", getChangeStatusResult.getErrorDesc());

	    String transformedJson = jsonBody.toString();

	    //Day du lieu
	    JsonUtil.newJsonPayload(((Axis2MessageContext) context).getAxis2MessageContext(),transformedJson, true, true);
	}
}

