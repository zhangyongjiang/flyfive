package com.flyerfive.client;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;

public class WebService {
    
    public static void listFlyers(RequestCallback callback)
            throws Exception {
        String url = "php/list-flyers.php?";
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
        Request response = builder.sendRequest("", callback);
    }
    
    public static void listMyFlyers(RequestCallback callback)
            throws Exception {
        String url = "php/list-my-flyers.php?";
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
        Request response = builder.sendRequest("", callback);
    }
    
    public static void getFlyer(String id, RequestCallback callback)
            throws Exception {
        String url = "php/get-flyer.php?id=" + id;
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
        Request response = builder.sendRequest("", callback);
    }
    
	public static void saveFlyer(final String id, final String content, String img, final RequestCallback callback) throws Exception {
        String url = "php/save-flyer.php";
		String formData = ((id == null) ? "" : "id=" + id) 
                + "&content=" + URL.encodeQueryString(content)
                + "&img=" + URL.encodeQueryString(img)
				;
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
		builder.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
        Request response = builder.sendRequest(formData, callback);
	}

    public static void removeFlyer(String id) {
        String url = "php/remove-flyer.php?id=" + id;
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
        try {
            Request response = builder.sendRequest("", new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                }
                
                @Override
                public void onError(Request request, Throwable exception) {
                }
            });
        }
        catch (RequestException e) {
            e.printStackTrace();
            Window.alert("Error\n" + e.getMessage());
        }
    }

}

