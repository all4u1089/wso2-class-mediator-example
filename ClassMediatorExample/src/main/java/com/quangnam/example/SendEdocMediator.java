package com.quangnam.example;

import org.apache.synapse.MessageContext; 
import org.apache.synapse.mediators.AbstractMediator;
import org.apache.synapse.commons.json.JsonUtil;
import org.apache.synapse.core.axis2.Axis2MessageContext;

import org.json.JSONObject;

import com.quangnam.example.utils.KnobstickServiceExtImp;
import com.vpcp.services.model.MessageType;
import com.vpcp.services.model.SendEdocResult;

public class SendEdocMediator extends AbstractMediator { 
	
	public boolean mediate(MessageContext context) { 
		getSendEdoc(context);
		return true;
	}
	
	@SuppressWarnings("deprecation")
	private void getSendEdoc(MessageContext context) {
		System.out.println("getSendEdoc");
		String systemId = (String) context.getProperty("systemId");
		String secret = (String) context.getProperty("secret");
		String from = (String) context.getProperty("from");
		String mesageType = (String) context.getProperty("messageType");
		String base64Data = (String) context.getProperty("uri.var.base64data");
		if (MessageType.valueOf(mesageType) == null) {
			mesageType = MessageType.edoc.toString();
		}
		
		KnobstickServiceExtImp knobstickService = new KnobstickServiceExtImp(systemId, secret);
		
		StringBuffer stringBuffer = new StringBuffer();
		String servicetype = "eDoc";
		from = "000.00.00.K19";
		stringBuffer.append("{"); 
		stringBuffer.append("\"from\":\""+from+"\"");
		stringBuffer.append(",\"servicetype\":\""+servicetype+"\"");
		stringBuffer.append(",\"messagetype\":\""+ mesageType +"\"");
		stringBuffer.append("}"); 
		String jsonHeader = stringBuffer.toString();
		
		//String edXMLFileLocation = "./xml/952f8a6a-22fc-4458-af83-18bbbdaf722a.edxml";
		
		SendEdocResult sendEdocResult = knobstickService.sendEdocExt(jsonHeader, base64Data);
		
		//Tạo json payload
	    String jsonPayloadToString = JsonUtil.jsonPayloadToString(((Axis2MessageContext) context).getAxis2MessageContext());
	    JSONObject jsonBody = new JSONObject(jsonPayloadToString);

	    //Nhap du lieu vao json payload
		if(sendEdocResult!=null) {			
			jsonBody.put("docID", sendEdocResult.getDocID());
		}
	    jsonBody.put("status", sendEdocResult.getStatus());
	    jsonBody.put("errorCode", sendEdocResult.getErrorCode());
	    jsonBody.put("errorDesc", sendEdocResult.getErrorDesc());

	    String transformedJson = jsonBody.toString();

	    //Day du lieu
	    JsonUtil.newJsonPayload(((Axis2MessageContext) context).getAxis2MessageContext(),transformedJson, true, true);
	}
}

