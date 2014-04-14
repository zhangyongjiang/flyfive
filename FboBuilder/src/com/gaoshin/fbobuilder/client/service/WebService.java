package com.gaoshin.fbobuilder.client.service;

import com.gaoshin.fbobuilder.client.data.FileProperty;
import com.gaoshin.fbobuilder.client.data.FlyerFolderProperty;
import com.gaoshin.fbobuilder.client.data.FlyerProperty;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;

public class WebService {
	public static final native String getFileManagerUrl() /*-{
		return $wnd.getFileManagerUrl();
	}-*/;
	
	public static final native String getUserId() /*-{
		return $wnd.getUserId();
	}-*/;

	public static final native String getProxyUrl() /*-{
		return $wnd.getProxyUrl();
	}-*/;

	public static void list(final String dir, final ListCallback callback)
	        throws Exception {
		String url = getFileManagerUrl() + "?action=list&dir=" + URL.encodeQueryString(dir);
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
		builder.setHeader("Accept", "application/json");

		Request response = builder.sendRequest("", new RequestCallback() {
			@Override
			public void onResponseReceived(Request request, Response response) {
				int statusCode = response.getStatusCode();

				if (statusCode == Response.SC_OK) {
					String json = response.getText();
					JSONObject jobj = (JSONObject) JSONParser.parse(json);
					String success = jobj.get("success").toString();
					if("false".equals(success)) {
						Window.alert("Error\n" + jobj.get("data").toString());
					}
					else {
						callback.list(dir, parse(dir, jobj));
					}
				} else {
					Window.alert("Error\n" + response.getText());
				}
			}

			@Override
			public void onError(Request request, Throwable exception) {
				Window.alert("Error\n" + exception.getMessage());
			}
		});
	}
	
	private static FlyerFolderProperty parse(String dir, JSONObject jobj) {
		FlyerFolderProperty ffp = new FlyerFolderProperty();
		
		String folderName = null;
		int pos = dir.lastIndexOf("/");
		folderName = dir.substring(pos + 1);
		ffp.setName(folderName);
		
		ffp.setLoaded(true);
		
		JSONValue jsonValue = jobj.get("data");
		ffp.setPermission(((JSONString)jobj.get("permission")).stringValue());
		if(jsonValue != null) {
			jsonValue = ((JSONObject)jsonValue).get("directories");
		}
		if(jsonValue != null) {
			if(jsonValue instanceof JSONArray) {
			}
			else {
				JSONObject subfolder = (JSONObject) jsonValue;
				for(String name : subfolder.keySet()) {
					FlyerFolderProperty child = new FlyerFolderProperty();
					child.setLoaded(false);
					child.setName(name);
					child.setPermission(((JSONString)subfolder.get(name)).stringValue());
					ffp.getChildren().add(child);
				}
			}
		}
		
		jsonValue = jobj.get("data");
		if(jsonValue != null) {
			jsonValue = ((JSONObject)jsonValue).get("files");
		}
		if(jsonValue != null) {
			if(jsonValue instanceof JSONArray) {
			}
			else {
				JSONObject subfolder = (JSONObject) jsonValue;
				for(String name : subfolder.keySet()) {
					if(name.endsWith(".fly")) {
						FlyerProperty fp = new FlyerProperty();
						fp.setLoaded(false);
						fp.setName(name);
						String permission = ((JSONString)subfolder.get(name)).stringValue();
						fp.setPermission(permission );
						ffp.getChildren().add(fp);
					}
					else {
						FileProperty fp = new FileProperty();
						fp.setName(name);
						String permission = ((JSONString)subfolder.get(name)).stringValue();
						fp.setPermission(permission );
						ffp.getChildren().add(fp);
					}
				}
			}
		}
		
		return ffp;
	}

	public static void mkdir(final String dir, final String subfolder, final FileManagerCallback callback) throws Exception {
		String url = getFileManagerUrl() + "?action=mkdir&dir=" + URL.encodeQueryString(dir) + "&newName=" + URL.encodeQueryString(subfolder);
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
		builder.setHeader("Accept", "application/json");

		Request response = builder.sendRequest("", new RequestCallback() {
			@Override
			public void onResponseReceived(Request request, Response response) {
				int statusCode = response.getStatusCode();
				if (statusCode == Response.SC_OK) {
					String respTxt = response.getText();
					JSONObject result = (JSONObject)JSONParser.parse(respTxt);
					if("true".equals(result.get("success").toString())) {
						callback.onSuccess(result);
					}
					else {
						callback.onError(-1, result.get("data").toString());
					}
				} else {
					callback.onError(response.getStatusCode(), response.getText());
				}
			}

			@Override
			public void onError(Request request, Throwable exception) {
				callback.onError(-1, exception.getMessage());
			}
		});
	}

	public static void remove(final String path, final FileManagerCallback callback) throws Exception {
		String url = getFileManagerUrl();
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
		builder.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
		builder.setHeader("Accept", "application/json");

		Request response = builder.sendRequest("action=remove&dir=" + URL.encodeQueryString(path), new RequestCallback() {
			@Override
			public void onResponseReceived(Request request, Response response) {
				int statusCode = response.getStatusCode();
				if (statusCode == Response.SC_OK) {
					JSONObject result = (JSONObject)JSONParser.parse(response.getText());
					if("true".equals(result.get("success").toString())) {
						if(callback != null)
							callback.onSuccess(result);
					}
					else {
						if(callback != null)
							callback.onError(-1, response.getText());
					}
				} else {
					callback.onError(response.getStatusCode(), response.getText());
				}
			}

			@Override
			public void onError(Request request, Throwable exception) {
				callback.onError(-1, exception.getMessage());
			}
		});
	}

	public static void upload(final String dir, final String name, final FileManagerCallback callback) throws Exception {
		String url = getFileManagerUrl() + "?action=upload&dir=" + URL.encodeQueryString(dir) + "&newName=" + URL.encodeQueryString(name);
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
		builder.setHeader("Accept", "application/json");

		Request response = builder.sendRequest("", new RequestCallback() {
			@Override
			public void onResponseReceived(Request request, Response response) {
				int statusCode = response.getStatusCode();
				if (statusCode == Response.SC_OK) {
					JSONObject result = (JSONObject)JSONParser.parse(response.getText());
					callback.onSuccess(result);
				} else {
					callback.onError(response.getStatusCode(), response.getText());
				}
			}

			@Override
			public void onError(Request request, Throwable exception) {
				callback.onError(-1, exception.getMessage());
			}
		});
	}

	public static void save(final String dir, final String name, final String content, final FileManagerCallback callback) throws Exception {
		saveFile(dir, name, content, callback, "700", false);
	}

	public static void saveFile(final String dir, final String name, final String content, final FileManagerCallback callback, String permission, boolean base64) throws Exception {
		String url = getFileManagerUrl();
		String formData = "action=save&dir=" + URL.encodeQueryString(dir) 
				+ "&newName=" + URL.encodeQueryString(name) 
				+ "&content=" + URL.encodeQueryString(content)
				+ "&permission=" + permission
				+ (base64 ? "&base64=1" : "")
				;
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
		builder.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
		builder.setHeader("Accept", "application/json");

		Request response = builder.sendRequest(formData, new RequestCallback() {
			@Override
			public void onResponseReceived(Request request, Response response) {
				String respTxt = response.getText();
				JSONObject result = (JSONObject)JSONParser.parse(respTxt);
				if("true".equals(result.get("success").toString())) {
					if(callback != null)
						callback.onSuccess(result);
				}
				else {
					if(callback != null)
						callback.onError(-1, respTxt);
				}
			}

			@Override
			public void onError(Request request, Throwable exception) {
				if(callback != null)
					callback.onError(-1, exception.getMessage());
			}
		});
	}

	public static void share(final String dir, boolean share, final FileManagerCallback callback) throws Exception {
		String url = getFileManagerUrl();
		String formData = "action=access-control&dir=" + URL.encodeQueryString(dir) + "&permission=" + (share ? "public" : "private");
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
		builder.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
		builder.setHeader("Accept", "application/json");

		Request response = builder.sendRequest(formData, new RequestCallback() {
			@Override
			public void onResponseReceived(Request request, Response response) {
				String respTxt = response.getText();
				JSONObject result = (JSONObject)JSONParser.parse(respTxt);
				if("true".equals(result.get("success").toString())) {
					if(callback != null)
						callback.onSuccess(result);
				}
				else {
					if(callback != null)
						callback.onError(-1, respTxt);
				}
			}

			@Override
			public void onError(Request request, Throwable exception) {
				if(callback != null)
					callback.onError(-1, exception.getMessage());
			}
		});
	}
	
	public static void getFile(final String path, final FileManagerCallback callback) throws Exception {
		getFileForUser(null, path, callback);
	}
	
	public static void getFileForUser(final String user, final String path, final FileManagerCallback callback) throws Exception {
		String url = getFileManagerUrl() + "?action=get-file-content&dir=" + URL.encodeQueryString(path);
		if(user != null)
			url = url + "&user=" + URL.encodeQueryString(user);
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
		builder.setHeader("Accept", "application/json");

		Request response = builder.sendRequest("", new RequestCallback() {
			@Override
			public void onResponseReceived(Request request, Response response) {
				String respTxt = response.getText();
				JSONObject result = (JSONObject)JSONParser.parse(respTxt);
				if("true".equals(result.get("success").toString())) {
					callback.onSuccess(result);
				}
				else {
					callback.onError(-1, result.get("data").toString());
				}
			}

			@Override
			public void onError(Request request, Throwable exception) {
				callback.onError(-1, exception.getMessage());
			}
		});
	}

	public static void postToFacebook(String message, String link, String imageUrl, final FileManagerCallback callback) throws Exception {
		String url = getFileManagerUrl();
		String data = "action=post-to-facebook&message=" + URL.encode(message)
				+ "&link=" + URL.encode(link)
				+ "&imageUrl=" + URL.encode(imageUrl);
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
		builder.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
		builder.setHeader("Accept", "application/json");

		Request response = builder.sendRequest(data, new RequestCallback() {
			@Override
			public void onResponseReceived(Request request, Response response) {
				String respTxt = response.getText();
				JSONObject result = (JSONObject)JSONParser.parse(respTxt);
				if("true".equals(result.get("success").toString())) {
					callback.onSuccess(result);
				}
				else {
					callback.onError(-1, result.get("data").toString());
				}
			}

			@Override
			public void onError(Request request, Throwable exception) {
				callback.onError(-1, exception.getMessage());
			}
		});
    }
}

