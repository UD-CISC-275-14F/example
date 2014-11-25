package edu.udel.jatlas.google;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import edu.udel.jatlas.gdx.util.GdxPollingDispatcher;

public class GoogleAppsHttp implements GoogleAppsFacade {
	Map<String, String> properties;

	private String readProperties() {
		if (this.properties == null) {
			try {
				Map<String, String> properties = new HashMap<String, String>();

				FileHandle h = Gdx.files.internal("googleapps.properties");
				BufferedReader r = h.reader(4096);
				String line;
				while ((line = r.readLine()) != null) {
					String[] values = line.split("=");
					properties.put(values[0].trim(), values[1].trim());
				}
				if (!properties.containsKey("client_id")) {
					return "client_id not found in googleapps.properties";
				}
				if (!properties.containsKey("client_secret")) {
					return "client_secret not found in googleapps.properties";
				}
				this.properties = properties;
			} catch (IOException e) {
				this.properties = null;
				String message = "Could not read googleapps.properties";
				Gdx.app.error("GoogleApps", message, e);
				return message + " " + e.getMessage();
			}
		}
		return null;
	}

	public void obtainAccessToken(OnObtainAccessToken callback) {
		String error = readProperties();
		if (error == null) {
			String clientId = properties.get("client_id");
			HttpRequest request = new HttpRequest("POST");
			request.setUrl("https://accounts.google.com/o/oauth2/device/code");
			request.setHeader("Host", "accounts.google.com");
			request.setHeader("Content-Type",
					"application/x-www-form-urlencoded");
			request.setContent("client_id="
					+ clientId
					+ "&scope=email https://www.googleapis.com/auth/fusiontables");

			Gdx.net.sendHttpRequest(request,
					new CodeVerificationHttpResponseListener(callback));
		} else {
			callback.failure(error);
		}
	}

	public void queryFusionTable(String SQL, OnQueryResult callback,
			AccessToken accessToken) {

		JavaAccessToken token = (JavaAccessToken) accessToken;
		HttpRequest request = new HttpRequest("POST");
		request.setUrl("https://www.googleapis.com/fusiontables/v1/query");
		request.setHeader("Host", "accounts.google.com");
		request.setHeader("Content-Type", "application/x-www-form-urlencoded");
		request.setHeader("Authorization", "Bearer " + token.accessToken);
		request.setContent("sql=" + SQL);
		Gdx.net.sendHttpRequest(request, new FusionTableInsert(callback));

	}

	public String getTableId(String tableName) {
		readProperties();
		return properties.get(tableName);
	}

	class FusionTableInsert implements HttpResponseListener {
		OnQueryResult callback;

		public FusionTableInsert(OnQueryResult callback) {
			this.callback = callback;
		}

		public void handleHttpResponse(HttpResponse httpResponse) {
			HttpStatus status = httpResponse.getStatus();

			if (status.getStatusCode() == HttpStatus.SC_OK) {
				String response = httpResponse.getResultAsString();
				System.out.println(response);
				callback.success(response);
			} else {
				String response = httpResponse.getResultAsString();
				Gdx.app.error("HttpResponse", response);
				System.err.println(response);
				callback.failure("Failed to insert to table: " + response);
			}
		}

		public void cancelled() {
			callback.failure("cancelled");
		}

		public void failed(Throwable t) {
			callback.failure("Failed to insert to table: " + t.getMessage());
		}

	}

	class CodeVerificationHttpResponseListener implements HttpResponseListener {
		OnObtainAccessToken callback;

		public CodeVerificationHttpResponseListener(OnObtainAccessToken callback) {
			this.callback = callback;
		}

		public void handleHttpResponse(HttpResponse httpResponse) {
			HttpStatus status = httpResponse.getStatus();

			if (status.getStatusCode() == HttpStatus.SC_OK) {
				// force the GDX app to display the code somehow
				JsonReader jsonReader = new JsonReader();
				String response = httpResponse.getResultAsString();
				System.out.println(response);

				JsonValue root = jsonReader.parse(response);
				System.out.println(root.getString("user_code"));
				System.out.println(root.getString("verification_url"));
				
				callback.displayToUser("Please go to: " + root.getString("verification_url") + "\n and enter the code: " + 
						root.getString("user_code"));
				
				Gdx.net.openURI(root.getString("verification_url"));
				AccessTokenPoller poller = new AccessTokenPoller(callback,
						root.getString("device_code"), root.getLong("interval"));
				poller.queue();
			} else {
				String response = httpResponse.getResultAsString();
				Gdx.app.error("HttpResponse", response);
				System.err.println(response);
				callback.failure("Failed to get CodeVerification: " + response);
			}
		}

		public void cancelled() {
			callback.failure("cancelled");
		}

		public void failed(Throwable t) {
			callback.failure("Failed to get CodeVerification: "
					+ t.getMessage());
		}

	}

	class AccessTokenPoller implements Runnable, HttpResponseListener {
		OnObtainAccessToken callback;
		String deviceCode;
		long interval;

		public AccessTokenPoller(OnObtainAccessToken callback,
				String deviceCode, long interval) {
			this.callback = callback;
			this.deviceCode = deviceCode;
			this.interval = interval;
		}

		public void queue() {
			GdxPollingDispatcher.getInstance().runOnce(this, interval * 1000);
		}

		public void run() {
			String clientId = properties.get("client_id");
			String clientSecret = properties.get("client_secret");

			HttpRequest request = new HttpRequest("POST");
			request.setUrl("https://accounts.google.com/o/oauth2/token");
			request.setHeader("Host", "accounts.google.com");
			request.setHeader("Content-Type",
					"application/x-www-form-urlencoded");
			request.setContent("client_id=" + clientId + "&client_secret="
					+ clientSecret + "&code=" + deviceCode
					+ "&grant_type=http://oauth.net/grant_type/device/1.0");

			Gdx.net.sendHttpRequest(request, this);
		}

		public void cancelled() {
			callback.failure("cancelled");
		}

		public void failed(Throwable t) {
			callback.failure("Failed to get AccessToken: " + t.getMessage());
		}

		public void handleHttpResponse(HttpResponse httpResponse) {
			HttpStatus status = httpResponse.getStatus();

			if (status.getStatusCode() == HttpStatus.SC_OK) {
				// force the GDX app to display the code somehow
				JsonReader jsonReader = new JsonReader();
				String response = httpResponse.getResultAsString();
				System.out.println(response);

				JsonValue root = jsonReader.parse(response);
				if (root.has("error")) {
					String error = root.getString("error");
					if ("authorization_pending".equals(error)
							|| "slow_down".equals(error)) {
						queue();
					} else {
						callback.failure("Error obtaining Access Token from Google: "
								+ error);
					}
				} else {
					JavaAccessToken accessToken = new JavaAccessToken(
							root.getString("access_token"),
							root.getString("refresh_token"),
							root.getLong("expires_in"),
							root.getString("token_type"));
					callback.success(accessToken);
				}
			} else {
				String response = httpResponse.getResultAsString();
				Gdx.app.error("HttpResponse", response);
				System.err.println(response);
			}
		}
	}

	static class JavaAccessToken implements AccessToken {
		String accessToken;
		String refreshToken;
		long expiresIn;
		String tokenType;

		public JavaAccessToken(String accessToken, String refreshToken,
				long expiresIn, String tokenType) {
			this.accessToken = accessToken;
			this.refreshToken = refreshToken;
			this.expiresIn = expiresIn;
			this.tokenType = tokenType;
		}

	}
}
