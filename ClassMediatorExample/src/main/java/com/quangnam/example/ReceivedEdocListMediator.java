package com.quangnam.example;

import org.apache.synapse.MessageContext; 
import org.apache.synapse.mediators.AbstractMediator;
import org.apache.synapse.commons.json.JsonUtil;
import org.apache.synapse.core.axis2.Axis2MessageContext;

import org.json.JSONObject;

import com.quangnam.example.utils.KnobstickServiceExtImp;
import com.vpcp.services.model.GetReceivedEdocResult;
import com.vpcp.services.model.MessageType;

public class ReceivedEdocListMediator extends AbstractMediator { 
	
	public boolean mediate(MessageContext context) { 
		getReceivedEdocList(context);
		return true;
	}
	
	@SuppressWarnings("deprecation")
	private void getReceivedEdocList(MessageContext context) {
		String mesageType = (String) context.getProperty("messageType");
		String systemId = (String) context.getProperty("systemId");
		String secret = (String) context.getProperty("secret");
		if (MessageType.valueOf(mesageType) == null) {
			mesageType = MessageType.edoc.toString();
		}
		KnobstickServiceExtImp knobstickService = new KnobstickServiceExtImp(systemId, secret);
		
		String jsonHeader = "{}";
		StringBuffer stringBuffer = new StringBuffer();
		String servicetype = "eDoc";
		stringBuffer.append("{"); 
		stringBuffer.append("\"servicetype\":\""+servicetype+"\"");
		stringBuffer.append(",\"messagetype\":\""+ mesageType +"\"");
		stringBuffer.append("}"); 
		jsonHeader = stringBuffer.toString();
		//Gọi hàm getAgenciesList với tham số là "{}"
		GetReceivedEdocResult getReceivedEdocResult = knobstickService.getReceivedEdocList(jsonHeader);
		
		//Tạo json payload
	    String jsonPayloadToString = JsonUtil.jsonPayloadToString(((Axis2MessageContext) context).getAxis2MessageContext());
	    JSONObject jsonBody = new JSONObject(jsonPayloadToString);

	    //Nhap du lieu vao json payload
	    jsonBody.put("data", getReceivedEdocResult.getKnobsticks());
	    jsonBody.put("status", getReceivedEdocResult.getStatus());
	    jsonBody.put("errorCode", getReceivedEdocResult.getErrorCode());
	    jsonBody.put("errorDesc", getReceivedEdocResult.getErrorDesc());
	    
	    String transformedJson = jsonBody.toString();

	    //Day du lieu
	    JsonUtil.newJsonPayload(((Axis2MessageContext) context).getAxis2MessageContext(),transformedJson, true, true);
	}
}

