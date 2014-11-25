package edu.udel.jatlas.gdxexample.google;

public interface GoogleAppsFacade {
	public void obtainAccessToken(OnObtainAccessToken callback);
	public void queryFusionTable(String SQL, OnQueryResult callback, AccessToken accessToken);
	public String getTableId(String tableName);

	public interface OnObtainAccessToken {
		public void displayToUser(String message);
		public void success(AccessToken token);
		public void failure(String error);
	}
	
	public interface OnQueryResult {
		public void success(String json);
		public void failure(String error);
	}
	
	public interface AccessToken {
	}
}
