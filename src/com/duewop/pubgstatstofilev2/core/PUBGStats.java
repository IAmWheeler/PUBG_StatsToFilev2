package com.duewop.pubgstatstofilev2.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class PUBGStats {

	//Config Values
	String apiKey = null;
	String userName = null;
	URL url;
	HttpURLConnection connection;
	int win = 0;
	int kill = 0;
	int death = 0;
	String strOutputDir = null;

	//Selections
	boolean var_wins;
	boolean var_kills;
	boolean var_deaths;
	boolean var_headshots;
	boolean var_assists;
	boolean var_knockouts;
	boolean var_kdratio;
	boolean var_revives;
	boolean var_longestkill;
	boolean var_rndplayed;
	boolean var_rank;
	boolean var_accessonly;
	String var_txt_wins = null;
	String var_txt_kills = null;
	String var_txt_deaths = null;
	String var_txt_headshots = null;
	String var_txt_assists = null;
	String var_txt_knockouts = null;
	String var_txt_kdratio = null;
	String var_txt_revives = null;
	String var_txt_longestkill = null;
	String var_txt_rndplayed = null;
	String sel_rank_type = null;
	String var_txt_rank = null;

	String checkLog = null;

	String finalLog = null;

	public PUBGStats(String apiKey,
					 String userName,
					 int win,
					 int kill,
					 int death,
					 String strOutputDir,
					 boolean var_wins,
					 String var_txt_wins,
					 boolean var_kills,
					 String var_txt_kills,
					 boolean var_deaths,
					 String var_txt_deaths,
					 boolean var_headshots,
					 String var_txt_headshots,
					 boolean var_assists,
					 String var_txt_assists,
					 boolean var_knockouts,
					 String var_txt_knockouts,
					 boolean var_kdratio,
					 String var_txt_kdratio,
					 boolean var_revives,
					 String var_txt_revives,
					 boolean var_longestkill,
					 String var_txt_longestkill,
					 boolean var_rndplayed,
					 String var_txt_rndplayed,
					 boolean var_rank,
					 String sel_rank_type,
					 String var_txt_rank,
					 boolean var_accessonly) {
		this.apiKey = apiKey;
		this.userName = userName;
		this.win = win;
		this.kill = kill;
		this.death = death;
		this.strOutputDir = strOutputDir;

		this.var_wins = var_wins;
		this.var_kills = var_kills;
		this.var_deaths = var_deaths;
		this.var_headshots = var_headshots;
		this.var_assists = var_assists;
		this.var_knockouts = var_knockouts;
		this.var_kdratio = var_kdratio;
		this.var_revives = var_revives;
		this.var_longestkill = var_longestkill;
		this.var_rndplayed = var_rndplayed;
		this.var_rank = var_rank;
		this.var_accessonly = var_accessonly;
		this.var_txt_wins = var_txt_wins;
		this.var_txt_kills = var_txt_kills;
		this.var_txt_deaths = var_txt_deaths;
		this.var_txt_headshots = var_txt_headshots;
		this.var_txt_assists = var_txt_assists;
		this.var_txt_knockouts = var_txt_knockouts;
		this.var_txt_kdratio = var_txt_kdratio;
		this.var_txt_revives = var_txt_revives;
		this.var_txt_longestkill = var_txt_longestkill;
		this.var_txt_rndplayed = var_txt_rndplayed;
		this.sel_rank_type = sel_rank_type;
		this.var_txt_rank = var_txt_rank;
	}

	public String RunProcess() throws IOException {

//		String urlString = "https://pubgtracker.com/api/profile/pc/" + this.userName + "?season=2017-pre5";
		String urlString = "https://api.pubgtracker.com/v2/profile/pc/" + this.userName + "?season=2017-pre5";
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

		//Win Variables
		int agg_solo_win = 0;
		int agg_duo_win = 0;
		int agg_squad_win = 0;
		int total_win = 0;

		//Kill Variables
		int agg_solo_kill = 0;
		int agg_duo_kill = 0;
		int agg_squad_kill = 0;
		int total_kill = 0;

		//Death Variables
		int agg_solo_death = 0;
		int agg_duo_death = 0;
		int agg_squad_death = 0;
		int total_death = 0;

		//Headshot Variables
		int agg_solo_headshots = 0;
		int agg_duo_headshots = 0;
		int agg_squad_headshots = 0;
		int total_headshots = 0;

		//Assists Variables
		int agg_solo_assists = 0;
		int agg_duo_assists = 0;
		int agg_squad_assists = 0;
		int total_assists = 0;

		//Knock Out Variables
		int agg_solo_knockouts = 0;
		int agg_duo_knockouts = 0;
		int agg_squad_knockouts = 0;
		int total_knockouts = 0;

		//KD Ratio Variables
		double agg_solo_kdratio = 0;
		double agg_duo_kdratio = 0;
		double agg_squad_kdratio = 0;
		double total_kdratio = 0;

		//Revives Variables
		int agg_solo_revives = 0;
		int agg_duo_revives = 0;
		int agg_squad_revives = 0;
		int total_revives = 0;

		//Revives Variables
		double agg_solo_longestkill = 0;
		double agg_duo_longestkill = 0;
		double agg_squad_longestkill = 0;
		double total_longestkill = 0;

		//Rounds Played Variables
		int agg_solo_rndplayed = 0;
		int agg_duo_rndplayed = 0;
		int agg_squad_rndplayed = 0;
		int total_rndplayed = 0;

		//Rank Variables
		int agg_solo_rank = 0;
		int agg_duo_rank = 0;
		int agg_squad_rank = 0;
		int total_rank = 0;

		//Early Access Only Variables
		int total_win_accessonly = 0;
		int total_kill_accessonly = 0;
		int total_death_accessonly = 0;

		JsonElement json_full = new JsonParser().parse(data);
		JsonObject jobject = json_full.getAsJsonObject();
		JsonArray json_stats = jobject.getAsJsonArray("stats");

		for (int i = 0; i < json_stats.size(); i++) {

			jobject = json_stats.get(i).getAsJsonObject();

			if ((jobject.get("region").toString().equals("\"agg\""))) {

				JsonArray json_match_array = jobject.getAsJsonArray("stats");

				for (int j = 0; j < json_match_array.size(); j++) {
					JsonObject json_win_object = json_match_array.get(j).getAsJsonObject();
//					System.out.println(json_win_object.get("label").getAsString());

					//Get Win stats
					if (var_wins && var_txt_wins != null) {
						if (json_win_object.get("label").getAsString().equals("Wins")) {
							if (jobject.get("mode").toString().equals("\"solo\"")) {
								agg_solo_win = Integer.parseInt(json_win_object.get("valueInt").toString());
							} else if (jobject.get("mode").toString().equals("\"duo\"")) {
								agg_duo_win = Integer.parseInt(json_win_object.get("valueInt").toString());
							} else if (jobject.get("mode").toString().equals("\"squad\"")) {
								agg_squad_win = Integer.parseInt(json_win_object.get("valueInt").toString());
							}
						}
					}

					//Get Kill Stats
					if (var_kills && var_txt_kills != null) {
						if (json_win_object.get("label").getAsString().equals("Kills")) {
							if (jobject.get("mode").toString().equals("\"solo\"")) {
								agg_solo_kill = Integer.parseInt(json_win_object.get("valueInt").toString());
							} else if (jobject.get("mode").toString().equals("\"duo\"")) {
								agg_duo_kill = Integer.parseInt(json_win_object.get("valueInt").toString());
							} else if (jobject.get("mode").toString().equals("\"squad\"")) {
								agg_squad_kill = Integer.parseInt(json_win_object.get("valueInt").toString());
							}
						}
					}

					//Get Death Stats
					if (var_deaths && var_txt_deaths != null) {
						if (json_win_object.get("label").getAsString().equals("Losses")) {
							if (jobject.get("mode").toString().equals("\"solo\"")) {
								agg_solo_death = Integer.parseInt(json_win_object.get("valueInt").toString());
							} else if (jobject.get("mode").toString().equals("\"duo\"")) {
								agg_duo_death = Integer.parseInt(json_win_object.get("valueInt").toString());
							} else if (jobject.get("mode").toString().equals("\"squad\"")) {
								agg_squad_death = Integer.parseInt(json_win_object.get("valueInt").toString());
							}
						}
					}

					//Get Headshot Kill Stats
					if (var_headshots && var_txt_headshots != null) {
						if (json_win_object.get("label").getAsString().equals("Headshot Kills")) {
							if (jobject.get("mode").toString().equals("\"solo\"")) {
								agg_solo_headshots = Integer.parseInt(json_win_object.get("valueInt").toString());
							} else if (jobject.get("mode").toString().equals("\"duo\"")) {
								agg_duo_headshots = Integer.parseInt(json_win_object.get("valueInt").toString());
							} else if (jobject.get("mode").toString().equals("\"squad\"")) {
								agg_squad_headshots = Integer.parseInt(json_win_object.get("valueInt").toString());
							}
						}
					}

					//Get Assist Stats
					if (var_assists && var_txt_assists != null) {
						if (json_win_object.get("label").getAsString().equals("Assists")) {
							if (jobject.get("mode").toString().equals("\"solo\"")) {
								agg_solo_assists = Integer.parseInt(json_win_object.get("valueInt").toString());
							} else if (jobject.get("mode").toString().equals("\"duo\"")) {
								agg_duo_assists = Integer.parseInt(json_win_object.get("valueInt").toString());
							} else if (jobject.get("mode").toString().equals("\"squad\"")) {
								agg_squad_assists = Integer.parseInt(json_win_object.get("valueInt").toString());
							}
						}
					}

					//Get Knock Out Stats
					if (var_knockouts && var_txt_knockouts != null) {
						if (json_win_object.get("label").getAsString().equals("Knock Outs")) {
							if (jobject.get("mode").toString().equals("\"solo\"")) {
								agg_solo_knockouts = Integer.parseInt(json_win_object.get("valueInt").toString());
							} else if (jobject.get("mode").toString().equals("\"duo\"")) {
								agg_duo_knockouts = Integer.parseInt(json_win_object.get("valueInt").toString());
							} else if (jobject.get("mode").toString().equals("\"squad\"")) {
								agg_squad_knockouts = Integer.parseInt(json_win_object.get("valueInt").toString());
							}
						}
					}

					//Get K/D Ration Stats
					if (var_kdratio && var_txt_kdratio != null) {
						if (json_win_object.get("label").getAsString().equals("K/D Ratio")) {
							if (jobject.get("mode").toString().equals("\"solo\"")) {
								agg_solo_kdratio = Double.parseDouble(json_win_object.get("valueDec").toString());
							} else if (jobject.get("mode").toString().equals("\"duo\"")) {
								agg_duo_kdratio = Double.parseDouble(json_win_object.get("valueDec").toString());
							} else if (jobject.get("mode").toString().equals("\"squad\"")) {
								agg_squad_kdratio = Double.parseDouble(json_win_object.get("valueDec").toString());
							}
						}
					}

					//Get Revive Stats
					if (var_revives && var_txt_revives != null) {
						if (json_win_object.get("label").getAsString().equals("Revives")) {
							if (jobject.get("mode").toString().equals("\"solo\"")) {
								agg_solo_revives = Integer.parseInt(json_win_object.get("valueInt").toString());
							} else if (jobject.get("mode").toString().equals("\"duo\"")) {
								agg_duo_revives = Integer.parseInt(json_win_object.get("valueInt").toString());
							} else if (jobject.get("mode").toString().equals("\"squad\"")) {
								agg_squad_revives = Integer.parseInt(json_win_object.get("valueInt").toString());
							}
						}
					}

					//Get Longest Kill Stats
					if (var_longestkill && var_txt_longestkill != null) {
						if (json_win_object.get("label").getAsString().equals("Longest Kill")) {
							if (jobject.get("mode").toString().equals("\"solo\"")) {
								agg_solo_longestkill = Double.parseDouble(json_win_object.get("valueDec").toString());
							} else if (jobject.get("mode").toString().equals("\"duo\"")) {
								agg_duo_longestkill = Double.parseDouble(json_win_object.get("valueDec").toString());
							} else if (jobject.get("mode").toString().equals("\"squad\"")) {
								agg_squad_longestkill = Double.parseDouble(json_win_object.get("valueDec").toString());
							}
						}
					}

					//Get Rounds Played Stats
					if (var_rndplayed && var_txt_rndplayed != null) {
						if (json_win_object.get("label").getAsString().equals("Rounds Played")) {
							if (jobject.get("mode").toString().equals("\"solo\"")) {
								agg_solo_rndplayed = Integer.parseInt(json_win_object.get("valueInt").toString());
							} else if (jobject.get("mode").toString().equals("\"duo\"")) {
								agg_duo_rndplayed = Integer.parseInt(json_win_object.get("valueInt").toString());
							} else if (jobject.get("mode").toString().equals("\"squad\"")) {
								agg_squad_rndplayed = Integer.parseInt(json_win_object.get("valueInt").toString());
							}
						}
					}

					//Get Rank Stats
					if (var_rndplayed && var_txt_rndplayed != null) {
						if (json_win_object.get("label").getAsString().equals("Rating")) {
							if (jobject.get("mode").toString().equals("\"solo\"")) {
								agg_solo_rank = Integer.parseInt(json_win_object.get("rank").toString());
							} else if (jobject.get("mode").toString().equals("\"duo\"")) {
								agg_duo_rank = Integer.parseInt(json_win_object.get("rank").toString());
							} else if (jobject.get("mode").toString().equals("\"squad\"")) {
								agg_squad_rank = Integer.parseInt(json_win_object.get("rank").toString());
							}
						}
					}
				}
			}
		}

		//Work the variables to get totals
		total_win = agg_solo_win + agg_duo_win + agg_squad_win + win;
		total_kill = agg_solo_kill + agg_duo_kill + agg_squad_kill + kill;
		total_death = agg_solo_death + agg_duo_death + agg_squad_death + death;
		total_headshots = agg_solo_headshots + agg_duo_headshots + agg_squad_headshots;
		total_assists = agg_solo_assists + agg_duo_assists + agg_squad_assists;
		total_knockouts = agg_solo_knockouts + agg_duo_knockouts + agg_squad_knockouts;
		total_kdratio = (double) Math.round((agg_solo_kdratio + agg_duo_kdratio + agg_squad_kdratio) / 3);
		total_revives = agg_solo_revives + agg_duo_revives + agg_squad_revives;
		total_rndplayed = agg_solo_rndplayed + agg_duo_rndplayed + agg_squad_rndplayed;

		//Find Longest Kill
		total_longestkill = (agg_solo_longestkill > agg_duo_longestkill) ? agg_solo_longestkill:agg_duo_longestkill;
		total_longestkill = (agg_squad_longestkill > total_longestkill) ? agg_squad_longestkill:total_longestkill;

		total_win_accessonly = agg_solo_win + agg_duo_win + agg_squad_win;
		total_kill_accessonly = agg_solo_kill + agg_duo_kill + agg_squad_kill;
		total_death_accessonly = agg_solo_death + agg_duo_death + agg_squad_death;

		//Find Rank
		if(sel_rank_type.equals("BEST")) {
			total_rank = (agg_solo_rank > agg_duo_rank) ? agg_solo_rank:agg_duo_rank;
			total_rank = (agg_squad_rank > total_rank) ? agg_squad_rank:total_rank;
		} else if (sel_rank_type.equals("SOLO")) {
			total_rank = agg_solo_rank;
		} else if (sel_rank_type.equals("DUO")) {
			total_rank = agg_duo_rank;
		} else if (sel_rank_type.equals("SQUAD")) {
			total_rank = agg_squad_rank;
		}

		checkLog =
				"Previous Wins: " + win + "\r\n" +
						"Previous Kills: " + kill + "\r\n" +
						"Previous Deaths: " + death + "\r\n\r\n";

		finalLog = "\r\n";

		//Configured to process Wins
		if (var_wins && var_txt_wins != null) {
			checkLog = checkLog +
					"Current Season Solo Wins: " + agg_solo_win + "\r\n" +
					"Current Season Duo Wins: " + agg_duo_win + "\r\n" +
					"Current Season Squad Wins: " + agg_squad_win + "\r\n";
			finalLog = finalLog + "Total Wins: " + total_win + "\r\n";

			//Write the chicken dinners
			writeToFile(var_txt_wins, Integer.toString(total_win));

			if (var_accessonly) {
				writeToFile("accessonly_" + var_txt_wins, Integer.toString(total_win_accessonly));
			}
		}

		//Configured to process Kills
		if (var_kills && var_txt_kills != null) {
			checkLog = checkLog +
					"Current Season Solo Kills: " + agg_solo_kill + "\r\n" +
					"Current Season Duo Kills: " + agg_duo_kill + "\r\n" +
					"Current Season Squad Kills: " + agg_squad_kill + "\r\n";
			finalLog = finalLog + "Total Kills: " + total_kill + "\r\n";

			//Write the kills
			writeToFile(var_txt_kills, Integer.toString(total_kill));

			if (var_accessonly) {
				writeToFile("accessonly_" + var_txt_kills, Integer.toString(total_kill_accessonly));
			}
		}

		//Configured to process Deaths
		if (var_deaths && var_txt_deaths != null) {
			checkLog = checkLog +
					"Current Season Solo Deaths: " + agg_solo_death + "\r\n" +
					"Current Season Duo Deaths: " + agg_duo_death + "\r\n" +
					"Current Season Squad Deaths: " + agg_squad_death + "\r\n";
			finalLog = finalLog + "Total Deaths: " + total_death + "\r\n";

			//Write the deaths
			writeToFile(var_txt_deaths, Integer.toString(total_death));

			if (var_accessonly) {
				writeToFile("accessonly_" + var_txt_deaths, Integer.toString(total_death_accessonly));
			}
		}

		//Configured to process Headshot Kills
		if (var_headshots && var_txt_headshots != null) {
			checkLog = checkLog +
					"Current Season Solo Headshot Kills: " + agg_solo_headshots + "\r\n" +
					"Current Season Duo Headshot Kills: " + agg_duo_headshots + "\r\n" +
					"Current Season Squad Headshot Kills: " + agg_squad_headshots + "\r\n";
			finalLog = finalLog + "Total Headshot Kills: " + total_headshots + "\r\n";

			//Write the headshots
			writeToFile(var_txt_headshots, Integer.toString(total_headshots));
		}

		//Configured to process Assists
		if (var_assists && var_txt_assists != null) {
			checkLog = checkLog +
					"Current Season Solo Assists: " + agg_solo_assists + "\r\n" +
					"Current Season Duo Assists: " + agg_duo_assists + "\r\n" +
					"Current Season Squad Assists: " + agg_squad_assists + "\r\n";
			finalLog = finalLog + "Total Assists: " + total_assists + "\r\n";

			//Write the assists
			writeToFile(var_txt_assists, Integer.toString(total_assists));
		}

		//Configured to process Knock Outs
		if (var_knockouts && var_txt_knockouts != null) {
			checkLog = checkLog +
					"Current Season Solo Knock Outs: " + agg_solo_knockouts + "\r\n" +
					"Current Season Duo Knock Outs: " + agg_duo_knockouts + "\r\n" +
					"Current Season Squad Knock Outs: " + agg_squad_knockouts + "\r\n";
			finalLog = finalLog + "Total Knock Outs: " + total_knockouts + "\r\n";

			//Write the knockouts
			writeToFile(var_txt_knockouts, Integer.toString(total_knockouts));
		}

		//Configured to process K/D Ratio
		if (var_kdratio && var_txt_kdratio != null) {
			checkLog = checkLog +
					"Current Season Solo K/D Ratio: " + agg_solo_kdratio + "\r\n" +
					"Current Season Duo K/D Ratio: " + agg_duo_kdratio + "\r\n" +
					"Current Season Squad K/D Ratio: " + agg_squad_kdratio + "\r\n";
			finalLog = finalLog + "Total K/D Ratio: " + total_kdratio + "\r\n";

			//Write the kdratio
			writeToFile(var_txt_kdratio, Double.toString(total_kdratio));
		}

		//Configured to process Revives
		if (var_revives && var_txt_revives != null) {
			checkLog = checkLog +
					"Current Season Solo Revives: " + agg_solo_revives + "\r\n" +
					"Current Season Duo Revives: " + agg_duo_revives + "\r\n" +
					"Current Season Squad Revives: " + agg_squad_revives + "\r\n";
			finalLog = finalLog + "Total Revives: " + total_revives + "\r\n";

			//Write the revives
			writeToFile(var_txt_revives, Integer.toString(total_revives));
		}

		//Configured to process Longest Kill
		if (var_longestkill && var_txt_longestkill != null) {
			checkLog = checkLog +
					"Current Season Solo Longest Kill: " + agg_solo_longestkill + "\r\n" +
					"Current Season Duo Longest Kill: " + agg_duo_longestkill + "\r\n" +
					"Current Season Squad Longest Kill: " + agg_squad_longestkill + "\r\n";
			finalLog = finalLog + "Longest Kill: " + total_longestkill + "\r\n";

			//Write the kdratio
			writeToFile(var_txt_longestkill, Double.toString(total_longestkill));
		}

		//Configured to process Rounds Played
		if (var_rndplayed && var_txt_rndplayed != null) {
			checkLog = checkLog +
					"Current Season Solo Rounds Played: " + agg_solo_rndplayed + "\r\n" +
					"Current Season Duo Rounds Played: " + agg_duo_rndplayed + "\r\n" +
					"Current Season Squad Rounds Played: " + agg_squad_rndplayed + "\r\n";
			finalLog = finalLog + "Total Rounds Played: " + total_rndplayed + "\r\n";

			//Write the rndplayed
			writeToFile(var_txt_rndplayed, Integer.toString(total_rndplayed));
		}

		//Configured to process Rank
		if (var_rndplayed && var_txt_rndplayed != null) {
			checkLog = checkLog +
					"Current Season Solo Rank: " + agg_solo_rank + "\r\n" +
					"Current Season Duo Rank: " + agg_duo_rank + "\r\n" +
					"Current Season Squad Rank: " + agg_squad_rank + "\r\n";
			finalLog = finalLog + "Selected Rank: " + total_rank + "\r\n";

			//Write the rank
			writeToFile(var_txt_rank, Integer.toString(total_rank));
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
