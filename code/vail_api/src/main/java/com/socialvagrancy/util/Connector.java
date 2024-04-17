package com.socialvagrancy.vail.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.StringBuilder;
import java.net.URL;
import java.net.HttpURLConnection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

public class Connector
{
    private HttpClient http_client; // Used for PATCH requests

	public Connector()
	{
		// Turn off SSL certification since the SSL certificate on
		// these libraries is always flagged.
		TrustManager[] trustAllCerts = new TrustManager[] { 
			new X509TrustManager() {
				@Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() { return new X509Certificate[]{}; }
				@Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {}
				@Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {}
			}
		};
		
        // Install the all-trusting trust manager

		try
		{
            SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		
			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) { return true; }
			};

			// Install the all-trustng host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

		    http_client = HttpClients.custom()
                               .setSSLSocketFactory(new SSLConnectionSocketFactory(sc, NoopHostnameVerifier.INSTANCE))
                               .build();
        
        }
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	public String DELETE(String httpRequest, String token)
	{
		// Send DELETE command to the server.

		StringBuilder response = new StringBuilder();
		int response_code = 0;

		// Open connection		
		try
		{
			URL url = new URL(httpRequest);
			HttpURLConnection cxn = (HttpURLConnection) url.openConnection();
		
			// Configuration the connection
			cxn.setRequestMethod("DELETE");
			cxn.setDoOutput(true);
			cxn.setRequestProperty("Content-Type", "application/json; utf-8");
			cxn.setRequestProperty("Accept", "application/json");
			cxn.setRequestProperty("Authorization", token);

			// GET response code.
			response_code = cxn.getResponseCode();

			// Read response	
			BufferedReader br = new BufferedReader(new InputStreamReader(cxn.getInputStream(), "utf-8"));

			String responseLine = null;

			while((responseLine = br.readLine()) != null)
			{
				response.append(responseLine);
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}

		// Check to see if there is a response.
		// If not, return the response code instead.
		if(response.length()==0)
		{
			return Integer.toString(response_code);
		}
		else
		{
			return response.toString();
		}
	}

	public String GET(String httpRequest, String token)
	{
		// Query library with a header (authorization)
		// mostly just for logging in.

		StringBuilder response = new StringBuilder();
		int response_code = 0;

		// Open connection		
		try
		{
			URL url = new URL(httpRequest);
			HttpURLConnection cxn = (HttpURLConnection) url.openConnection();
		
			// Configuration the connection
			cxn.setRequestMethod("GET");
			cxn.setDoOutput(true);
			cxn.setRequestProperty("Content-Type", "application/json; utf-8");
			cxn.setRequestProperty("Accept", "application/json");
			cxn.setRequestProperty("Authorization", token);
	/*
			OutputStream output = cxn.getOutputStream();
			byte[] input = body.getBytes("utf-8");
			output.write(input, 0, input.length);
	*/		

			// GET response code.
			response_code = cxn.getResponseCode();

			// Read response	
			BufferedReader br = new BufferedReader(new InputStreamReader(cxn.getInputStream(), "utf-8"));

			String responseLine = null;

			while((responseLine = br.readLine()) != null)
			{
				response.append(responseLine);
			}
		}
		catch(Exception e)
		{
			System.err.println("ERROR: " + e.getMessage());
		}

		// Check to see if there is a response.
		// If not, return the response code instead.
		if(token.equals("code"))
		{
			return Integer.toString(response_code);
		}
		else
		{
			return response.toString();
		}
	}
	
	public String POST(String httpRequest, String body)
	{
		// Query library without a header (authorization)
		// mostly just for logging in.

		StringBuilder response = new StringBuilder();
		
		// Open connection		
		try
		{
			URL url = new URL(httpRequest);
			HttpURLConnection cxn = (HttpURLConnection) url.openConnection();
		
			// Configuration the connection
			cxn.setRequestMethod("POST");
			cxn.setDoOutput(true);
			cxn.setRequestProperty("Content-Type", "application/json; utf-8");
			cxn.setRequestProperty("Accept", "application/json");
	
			OutputStream output = cxn.getOutputStream();
			byte[] input = body.getBytes("utf-8");
			output.write(input, 0, input.length);
			
			
			// Read response	
			BufferedReader br = new BufferedReader(new InputStreamReader(cxn.getInputStream(), "utf-8"));

			String responseLine = null;

			while((responseLine = br.readLine()) != null)
			{
				response.append(responseLine);
			}
		}
		catch(Exception e)
		{	
			System.err.println(e.getMessage());
		}

		return response.toString();
	}
	
	public String POST(String httpRequest, String token, String body)
	{
		// Query library without a header (authorization)
		// mostly just for logging in.

		StringBuilder response = new StringBuilder();
		
		// Open connection		
		try
		{
			URL url = new URL(httpRequest);
			HttpURLConnection cxn = (HttpURLConnection) url.openConnection();
		
			// Configuration the connection
			cxn.setRequestMethod("POST");
			cxn.setDoOutput(true);
			cxn.setRequestProperty("Content-Type", "application/json; utf-8");
			cxn.setRequestProperty("Accept", "application/json");
			cxn.setRequestProperty("Authorization", token);

			if(body.length() > 0)
			{
				OutputStream output = cxn.getOutputStream();
				byte[] input = body.getBytes("utf-8");
				output.write(input, 0, input.length);
			}
			
			// Read response	
			BufferedReader br = new BufferedReader(new InputStreamReader(cxn.getInputStream(), "utf-8"));

			String responseLine = null;

			while((responseLine = br.readLine()) != null)
			{
				response.append(responseLine);
			}
		}
		catch(Exception e)
		{
			return e.getMessage();
		}

		return response.toString();
	}
	
	public String PATCH(String httpRequest, String token, String body)
	{
		// Open connection		
		HttpResponse response = null;
        try
		{
            HttpPatch request = new HttpPatch(httpRequest);
            request.setHeader("Content-Type", "application/merge-patch+json");
            request.setHeader("Accepts", "application/json");
            request.setHeader("Authorization", token);
            request.setEntity(new StringEntity(body));


            response = http_client.execute(request);
		    
            return EntityUtils.toString(response.getEntity());
        }
		catch(Exception e)
		{
			return e.getMessage();
		}

	}
	
	public String PUT(String httpRequest, String token, String body)
	{
		// Query library without a header (authorization)
		// mostly just for logging in.

		StringBuilder response = new StringBuilder();
		
		// Open connection		
		try
		{
			URL url = new URL(httpRequest);
			HttpURLConnection cxn = (HttpURLConnection) url.openConnection();
		
			// Configuration the connection
			cxn.setRequestMethod("PUT");
			cxn.setDoOutput(true);
			cxn.setRequestProperty("Content-Type", "application/json; utf-8");
			cxn.setRequestProperty("Accept", "application/json");
			cxn.setRequestProperty("Authorization", token);

			if(body.length() > 0)
			{
				OutputStream output = cxn.getOutputStream();
				byte[] input = body.getBytes("utf-8");
				output.write(input, 0, input.length);
			}
			
			// Read response	
			BufferedReader br = new BufferedReader(new InputStreamReader(cxn.getInputStream(), "utf-8"));

			String responseLine = null;

			while((responseLine = br.readLine()) != null)
			{
				response.append(responseLine);
			}
		}
		catch(Exception e)
		{
			return e.getMessage();
		}

		return response.toString();
	}
	
}
