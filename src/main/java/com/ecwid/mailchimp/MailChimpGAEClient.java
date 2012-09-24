/*
 * Copyright 2012 Ecwid, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ecwid.mailchimp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MailChimp API wrapper.
 * 
 * @author Vasily Karyaev <v.karyaev@gmail.com>
 * @author James Broberg <jbroberg@gmail.com> (java.net.* client)
 */
public class MailChimpGAEClient extends MailChimpClient {
	
	private static final Logger log = Logger.getLogger(MailChimpGAEClient.class.getName());
	HttpURLConnection conn = null;
	
	public String execute(String url, String request) throws IOException {
		if(log.isLoggable(Level.FINE)) {
			log.fine("Post to "+url+" : "+request);
		}
		
		String response = "";
				
		try {
            URL mcUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) mcUrl.openConnection();
    		conn.setDoOutput(true);
    		conn.setRequestMethod("POST");
    		
    		conn.addRequestProperty("Content-Type", "application/" + "POST");
    		conn.setRequestProperty("Content-Length", Integer.toString(request.length()));
    		conn.getOutputStream().write(request.getBytes("UTF8"));

    		BufferedReader br = new BufferedReader(new InputStreamReader(
    				(conn.getInputStream())));
     
    		String output = "";
    		StringBuilder sbResponse = new StringBuilder();
    		while ((output = br.readLine()) != null) {
    			sbResponse.append(output + "\n");
    		}
     
    		response = sbResponse.toString();
    		// conn.disconnect();
    		
        } catch (MalformedURLException e) {
            log.warning(e.getMessage());
        } catch (IOException e) {
            log.warning(e.getMessage());
        }

		if(log.isLoggable(Level.FINE)) {
			log.fine("Response: "+response);
		}
		return response;
	}
	
	/**
	 * Release resources.
	 */
	public void close() {
		if (conn != null) {
			conn.disconnect();
		}
	}
}
