package com.quangnam.example.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.vpcp.services.KnobstickServiceImp;
import com.vpcp.services.model.SendEdocResult;


public class KnobstickServiceExtImp extends KnobstickServiceImp {

	public KnobstickServiceExtImp(String systemId, String secret) {
		super();
		this.headers.put("systemid", systemId);
		this.secret = secret;
	}

	public SendEdocResult sendEdocExt(String jsonHeaders, String base64data) {
		String urlRequest = this.endpoint + "/sendEdoc";
		SendEdocResult sendEdocResult = null;
		try {
			this.headers.put("Content-Type", "application/octet-stream");
			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(jsonHeaders);
			Set<Map.Entry<String, JsonElement>> set = element.getAsJsonObject().entrySet();
			Iterator<Map.Entry<String, JsonElement>> iteratorJE = set.iterator();
			while (iteratorJE.hasNext()) {
				Map.Entry<String, JsonElement> entry = (Map.Entry<String, JsonElement>) iteratorJE.next();
				if (((String) entry.getKey()).equals("to")) {
					String s = ((JsonElement) entry.getValue()).toString();
					this.headers.put(entry.getKey(), s);
				} else {
					this.headers.put(entry.getKey(), ((JsonElement) entry.getValue()).getAsString());
				}
			}
			createContentHA256(base64data);
			
			ByteArrayInputStream inputStream = new ByteArrayInputStream(base64data.getBytes());
			
			String json = executeSendFileDoc(urlRequest, "POST", null, getListHeader(), inputStream);

			parser = new JsonParser();
			element = parser.parse(json);
			System.out.println(json);
			if (element != null) {
				sendEdocResult = new SendEdocResult();
				String status = element.getAsJsonObject().get("status").getAsString();
				String errorCode = element.getAsJsonObject().get("ErrorCode").getAsString();
				String errorDesc = element.getAsJsonObject().get("ErrorDesc").getAsString();
				String docID = element.getAsJsonObject().get("DocId").getAsString();

				sendEdocResult.setErrorCode(errorCode);
				sendEdocResult.setErrorDesc(errorDesc);
				sendEdocResult.setStatus(status);
				sendEdocResult.setDocID(docID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendEdocResult;
	}

	private void createContentHA256(String base64data) {
		ByteArrayInputStream fis = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			fis = new ByteArrayInputStream(base64data.getBytes());

			byte[] dataBytes = new byte['È'];

			int nread = 0;
			while ((nread = fis.read(dataBytes)) != -1) {
				md.update(dataBytes, 0, nread);
			}
			byte[] mdbytes = md.digest();

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < mdbytes.length; i++) {
				sb.append(Integer.toString((mdbytes[i] & 0xFF) + 256, 16).substring(1));
			}
			System.out.println("Hex format : " + sb.toString());

			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < mdbytes.length; i++) {
				hexString.append(Integer.toHexString(0xFF & mdbytes[i]));
			}
			this.headers.put("hash-file", hexString.toString());
			System.out.println("Hex format : " + hexString.toString());
			return;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
