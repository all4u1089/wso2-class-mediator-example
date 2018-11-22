package com.quangnam.example;

import org.apache.synapse.MessageContext; 
import org.apache.synapse.mediators.AbstractMediator;
import org.apache.synapse.commons.json.JsonUtil;
import org.apache.synapse.core.axis2.Axis2MessageContext;

import org.json.JSONObject;

import com.quangnam.example.utils.KnobstickServiceExtImp;
import com.vpcp.services.model.GetSendEdocResult;
import com.vpcp.services.model.MessageType;

public class SendEdocListMediator extends AbstractMediator { 
	
	public boolean mediate(MessageContext context) { 
		getSendEdocList(context);
		return true;
	}
	
	@SuppressWarnings("deprecation")
	private void getSendEdocList(MessageContext context) {
		String docId = (String) context.getProperty("docId");
		String mesageType = (String) context.getProperty("messageType");
		String systemId = (String) context.getProperty("systemId");
		String secret = (String) context.getProperty("secret");
		if (MessageType.valueOf(mesageType) == null) {
			mesageType = MessageType.edoc.toString();
		}
		KnobstickServiceExtImp knobstickService = new KnobstickServiceExtImp(systemId, secret);
		
		StringBuffer stringBuffer = new StringBuffer();
		String servicetype = "eDoc";
		stringBuffer.append("{"); 
		stringBuffer.append("\"servicetype\":\""+servicetype+"\"");
		stringBuffer.append(",\"messagetype\":\""+ mesageType +"\"");
		stringBuffer.append(",\"docId\":\""+ docId +"\"");
		stringBuffer.append("}"); 
		String jsonHeader = stringBuffer.toString();
		
		GetSendEdocResult getSendEdocResult = knobstickService.getSentEdocList(jsonHeader);
		
		//Tạo json payload
	    String jsonPayloadToString = JsonUtil.jsonPayloadToString(((Axis2MessageContext) context).getAxis2MessageContext());
	    JSONObject jsonBody = new JSONObject(jsonPayloadToString);

	    //Nhap du lieu vao json payload
	    jsonBody.put("data", getSendEdocResult.getKnobsticks());
	    jsonBody.put("status", getSendEdocResult.getStatus());
	    jsonBody.put("errorCode", getSendEdocResult.getErrorCode());
	    jsonBody.put("errorDesc", getSendEdocResult.getErrorDesc());
	    
	    String transformedJson = jsonBody.toString();

	    //Day du lieu
	    JsonUtil.newJsonPayload(((Axis2MessageContext) context).getAxis2MessageContext(),transformedJson, true, true);
	}
}

