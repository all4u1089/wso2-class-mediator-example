package com.quangnam.example.utils;

public class EdocUtils {
	public static String createHeaderJsonGetDoc(String docId){
		StringBuffer stringBuffer = new StringBuffer();
		String filePath = "xml";
		stringBuffer.append("{"); 
		stringBuffer.append("\"filePath\":\""+filePath+"\"");
		stringBuffer.append(",\"docId\":\""+ docId +"\"");
		stringBuffer.append("}"); 
		String json = stringBuffer.toString();
		return json;
	}
	
	public static String createHeaderJsonUpdate(String docId, String status){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("{"); 
		stringBuffer.append("\"status\":\""+ status +"\"");
		stringBuffer.append(",\"docId\":"+ docId );
		stringBuffer.append("}"); 
		String	json = stringBuffer.toString();
		return json;
	}
}
