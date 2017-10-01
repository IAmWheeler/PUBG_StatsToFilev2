package com.duewop.pubgstatstofilev2.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class PUBGStats {

	String apiKey = null;
	String userName = null;
	URL url;
	HttpURLConnection connection;
	int win = 0;
	int kill = 0;
	int death = 0;
	String strOutputDir = null;





	boolean var_wins;
	String var_txt_wins = null;







	String checkLog = null;

	String finalLog = null;

	public PUBGStats(String apiKey,
					 String userName,
					 int win,
					 int kill,
					 int death,
					 String strOutputDir,
					 boolean var_wins,
					 String var_txt_wins) {
		this.apiKey = apiKey;
		this.userName = userName;
		this.win = win;
		this.kill = kill;
		this.death = death;
		this.strOutputDir = strOutputDir;

		this.var_wins = var_wins;
		this.var_txt_wins = var_txt_wins;
	}

	public String RunProcess() throws IOException {
		String urlString = "https://pubgtracker.com/api/profile/pc/" + this.userName;
		url = new URL(urlString);
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("User-Agent", "Mozilla/5.0");
		connection.setRequestProperty("TRN-Api-Key", apiKey);
		int responseCode = connection.getResponseCode();

		StringBuilder response = new StringBuilder();

		if(responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(connection.getInputStream()));
			String output;

			while ((output = bufferedReader.readLine()) != null) {
				response.append(output);
			}

			bufferedReader.close();
		}

		return response.toString();
	}

	public void ProcessData(String data) {

		JsonElement json_full = new JsonParser().parse(data);
		JsonObject jobject = json_full.getAsJsonObject();
		JsonArray json_stats = jobject.getAsJsonArray("Stats");

		for (int i = 0; i < json_stats.size(); i++) {

			jobject = json_stats.get(i).getAsJsonObject();

			if ((jobject.get("Region").toString().equals("\"agg\"")) && (jobject.get("Season").toString().equals("\"2017-pre4\""))) {

				JsonArray json_match_array = jobject.getAsJsonArray("Stats");

				System.out.println(jobject.getAsJsonArray("Stats").toString());

				for (int j = 0; j < json_match_array.size(); j++) {
//					if (json_match_array.get("\"field\"").equals("Wins")) {
//					System.out.println(json_match_array.toString());
//						JsonObject json_win_object = json_match_array.get(j).getAsJsonObject();
//						System.out.println(json_win_object.get("ValueInt").toString());
//					}
				}
			}
		}








	}

	private void writeToFile(String fileName, String total_value) {
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(strOutputDir + "/" + fileName), "utf-8"))) {
			writer.write(total_value);
			writer.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getWorkLog() {
		return checkLog + finalLog;
	}

}
