package com.duewop.pubgstatstofilev2.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PUBGStats {

    //Config Values
    String apiKey = null;
    String userName = null;
    URL url;
    HttpURLConnection connection;
    int win = 0;
    int kill = 0;
    int death = 0;
    String current_season = null;
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

    public PUBGStats() {

        Properties prop = new Properties();
        InputStream input;

        try {
            File f = new File("config.properties");
            if (f.exists() && !f.isDirectory()) {

                input = new FileInputStream("config.properties");

                // load a properties file
                prop.load(input);

                this.apiKey = prop.getProperty("apikey");
                this.userName = prop.getProperty("username");
//                this.win = Integer.parseInt(prop.getProperty("previous_wins"));
//                this.kill = Integer.parseInt(prop.getProperty("previous_kills"));
//                this.death = Integer.parseInt(prop.getProperty("previous_deaths"));
//                this.current_season = prop.getProperty("current_season");
                this.strOutputDir = prop.getProperty("output_dir");

                this.var_wins = Boolean.parseBoolean(prop.getProperty("chk_wins"));
                this.var_txt_wins = prop.getProperty("txt_wins");

                this.var_kills = Boolean.parseBoolean(prop.getProperty("chk_kills"));
                this.var_txt_kills = prop.getProperty("txt_kills");

                this.var_deaths = Boolean.parseBoolean(prop.getProperty("chk_deaths"));
                this.var_txt_deaths = prop.getProperty("txt_deaths");

                this.var_headshots = Boolean.parseBoolean(prop.getProperty("chk_headshots"));
                this.var_txt_headshots = prop.getProperty("txt_headshots");

                this.var_assists = Boolean.parseBoolean(prop.getProperty("chk_assists"));
                this.var_txt_assists = prop.getProperty("txt_assists");

                this.var_knockouts = Boolean.parseBoolean(prop.getProperty("chk_knockouts"));
                this.var_txt_knockouts = prop.getProperty("txt_knockouts");

                this.var_kdratio = Boolean.parseBoolean(prop.getProperty("chk_kdratio"));
                this.var_txt_kdratio = prop.getProperty("txt_kdratio");

                this.var_revives = Boolean.parseBoolean(prop.getProperty("chk_revives"));
                this.var_txt_revives = prop.getProperty("txt_revives");

                this.var_longestkill = Boolean.parseBoolean(prop.getProperty("chk_longestkill"));
                this.var_txt_longestkill = prop.getProperty("txt_longestkill");

                this.var_rndplayed = Boolean.parseBoolean(prop.getProperty("chk_rndplayed"));
                this.var_txt_rndplayed = prop.getProperty("txt_rndplayed");

                this.var_rank = Boolean.parseBoolean(prop.getProperty("chk_rank"));
                this.var_txt_rank = prop.getProperty("txt_rank");

                this.sel_rank_type = prop.getProperty("sel_rank");

                this.var_accessonly = Boolean.parseBoolean(prop.getProperty("chk_accessonly"));

                input.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void RunProcess() throws IOException {

        String pastSeason = null;
        Map<String, String> pastStats;

        String currentSeason = GetJSONData("2018-01");
        Map<String, String> currentStats = ProcessData(currentSeason);

        //Work the variables to get totals
        currentStats = CleanStats(currentStats);
        currentStats = CompileStats(currentStats, currentStats);

        //Write Current Files If Wanted
        if (var_accessonly)
            WriteCurrentFiles(currentStats, "2018-01");

        //Get Season 1 Stats
        pastSeason = GetJSONData("2017-pre1");
        pastStats = ProcessData(pastSeason);

        pastStats = CleanStats(pastStats);
        currentStats = CompileStats(currentStats, pastStats);

        pastStats.clear();

        //Get Season 2 Stats
        pastSeason = GetJSONData("2017-pre2");
        pastStats = ProcessData(pastSeason);

        pastStats = CleanStats(pastStats);
        currentStats = CompileStats(currentStats, pastStats);

        pastStats.clear();

        //Get Season 3 Stats
        pastSeason = GetJSONData("2017-pre3");
        pastStats = ProcessData(pastSeason);

        pastStats = CleanStats(pastStats);
        currentStats = CompileStats(currentStats, pastStats);

        pastStats.clear();

        //Get Season 4 Stats
        pastSeason = GetJSONData("2017-pre4");
        pastStats = ProcessData(pastSeason);

        pastStats = CleanStats(pastStats);
        currentStats = CompileStats(currentStats, pastStats);

        pastStats.clear();

        //Get Season 5 Stats
        pastSeason = GetJSONData("2017-pre5");
        pastStats = ProcessData(pastSeason);

        pastStats = CleanStats(pastStats);
        currentStats = CompileStats(currentStats, pastStats);

        pastStats.clear();

        //Get Season 6 Stats
        pastSeason = GetJSONData("2017-pre6");
        pastStats = ProcessData(pastSeason);

        pastStats = CleanStats(pastStats);
        currentStats = CompileStats(currentStats, pastStats);

        //Write Current Files If Wanted
        WriteCurrentFiles(currentStats, "totals");


















    }

    public String GetJSONData(String selected_season) throws IOException {

        String urlString = "https://api.pubgtracker.com/v2/profile/pc/" + this.userName + "?season=" + selected_season;

        url = new URL(urlString);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setRequestProperty("TRN-Api-Key", this.apiKey);
        int responseCode = connection.getResponseCode();

        StringBuilder response = new StringBuilder();

        if (responseCode == HttpURLConnection.HTTP_OK) {
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

    public Map<String, String> ProcessData(String data) {

        //Output for test purposes
//		System.out.println(data);

        Map<String, String> statsMap = new HashMap<String, String>();

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
                    if (json_win_object.get("label").getAsString().equals("Wins")) {
                        if (jobject.get("mode").toString().equals("\"solo\"")) {
                            statsMap.put("agg_solo_win", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"solo-fpp\"")) {
                            statsMap.put("agg_solo_fpp_win", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"duo\"")) {
                            statsMap.put("agg_duo_win", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"duo-fpp\"")) {
                            statsMap.put("agg_duo_fpp_win", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"squad\"")) {
                            statsMap.put("agg_squad_win", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"squad-fpp\"")) {
                            statsMap.put("agg_squad_fpp_win", json_win_object.get("valueInt").toString());
                        }
                    }

                    //Get Kill Stats
                    if (json_win_object.get("label").getAsString().equals("Kills")) {
                        if (jobject.get("mode").toString().equals("\"solo\"")) {
                            statsMap.put("agg_solo_kill", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"solo-fpp\"")) {
                            statsMap.put("agg_solo_fpp_kill", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"duo\"")) {
                            statsMap.put("agg_duo_kill", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"duo-fpp\"")) {
                            statsMap.put("agg_duo_fpp_kill", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"squad\"")) {
                            statsMap.put("agg_squad_kill", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"squad-fpp\"")) {
                            statsMap.put("agg_squad_fpp_kill", json_win_object.get("valueInt").toString());
                        }
                    }

                    //Get Death Stats
                    if (json_win_object.get("label").getAsString().equals("Losses")) {
                        if (jobject.get("mode").toString().equals("\"solo\"")) {
                            statsMap.put("agg_solo_death", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"solo-fpp\"")) {
                            statsMap.put("agg_solo_fpp_death", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"duo\"")) {
                            statsMap.put("agg_duo_death", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"duo-fpp\"")) {
                            statsMap.put("agg_duo_fpp_death", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"squad\"")) {
                            statsMap.put("agg_squad_death", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"squad-fpp\"")) {
                            statsMap.put("agg_squad_fpp_death", json_win_object.get("valueInt").toString());
                        }
                    }

                    //Get Headshot Kill Stats
                    if (json_win_object.get("label").getAsString().equals("Headshot Kills")) {
                        if (jobject.get("mode").toString().equals("\"solo\"")) {
                            statsMap.put("agg_solo_headshots", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"solo-fpp\"")) {
                            statsMap.put("agg_solo_fpp_headshots", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"duo\"")) {
                            statsMap.put("agg_duo_headshots", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"duo-fpp\"")) {
                            statsMap.put("agg_duo_fpp_headshots", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"squad\"")) {
                            statsMap.put("agg_squad_headshots", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"squad-fpp\"")) {
                            statsMap.put("agg_squad_fpp_headshots", json_win_object.get("valueInt").toString());
                        }
                    }

                    //Get Assist Stats
                    if (json_win_object.get("label").getAsString().equals("Assists")) {
                        if (jobject.get("mode").toString().equals("\"solo\"")) {
                            statsMap.put("agg_solo_assists", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"solo-fpp\"")) {
                            statsMap.put("agg_solo_fpp_assists", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"duo\"")) {
                            statsMap.put("agg_duo_assists", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"duo-fpp\"")) {
                            statsMap.put("agg_duo_fpp_assists", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"squad\"")) {
                            statsMap.put("agg_squad_assists", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"squad-fpp\"")) {
                            statsMap.put("agg_squad_fpp_assists", json_win_object.get("valueInt").toString());
                        }
                    }

                    //Get Knock Out Stats
                    if (json_win_object.get("label").getAsString().equals("Knock Outs")) {
                        if (jobject.get("mode").toString().equals("\"solo\"")) {
                            statsMap.put("agg_solo_knockouts", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"solo-fpp\"")) {
                            statsMap.put("agg_solo_fpp_knockouts", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"duo\"")) {
                            statsMap.put("agg_duo_knockouts", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"duo-fpp\"")) {
                            statsMap.put("agg_duo_fpp_knockouts", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"squad\"")) {
                            statsMap.put("agg_squad_knockouts", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"squad-fpp\"")) {
                            statsMap.put("agg_squad_fpp_knockouts", json_win_object.get("valueInt").toString());
                        }
                    }

                    //Get K/D Ration Stats
                    if (json_win_object.get("label").getAsString().equals("K/D Ratio")) {
                        if (jobject.get("mode").toString().equals("\"solo\"")) {
                            statsMap.put("agg_solo_kdratio", json_win_object.get("valueDec").toString());
                        } else if (jobject.get("mode").toString().equals("\"solo-fpp\"")) {
                            statsMap.put("agg_solo_fpp_kdratio", json_win_object.get("valueDec").toString());
                        } else if (jobject.get("mode").toString().equals("\"duo\"")) {
                            statsMap.put("agg_duo_kdratio", json_win_object.get("valueDec").toString());
                        } else if (jobject.get("mode").toString().equals("\"duo-fpp\"")) {
                            statsMap.put("agg_duo_fpp_kdratio", json_win_object.get("valueDec").toString());
                        } else if (jobject.get("mode").toString().equals("\"squad\"")) {
                            statsMap.put("agg_squad_kdratio", json_win_object.get("valueDec").toString());
                        } else if (jobject.get("mode").toString().equals("\"squad-fpp\"")) {
                            statsMap.put("agg_squad_fpp_kdratio", json_win_object.get("valueDec").toString());
                        }
                    }

                    //Get Revive Stats
                    if (json_win_object.get("label").getAsString().equals("Revives")) {
                        if (jobject.get("mode").toString().equals("\"solo\"")) {
                            statsMap.put("agg_solo_revives", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"solo-fpp\"")) {
                            statsMap.put("agg_solo_fpp_revives", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"duo\"")) {
                            statsMap.put("agg_duo_revives", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"duo-fpp\"")) {
                            statsMap.put("agg_duo_fpp_revives", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"squad\"")) {
                            statsMap.put("agg_squad_revives", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"squad-fpp\"")) {
                            statsMap.put("agg_squad_fpp_revives", json_win_object.get("valueInt").toString());
                        }
                    }

                    //Get Longest Kill Stats
                    if (json_win_object.get("label").getAsString().equals("Longest Kill")) {
                        if (jobject.get("mode").toString().equals("\"solo\"")) {
                            statsMap.put("agg_solo_longestkill", json_win_object.get("valueDec").toString());
                        } else if (jobject.get("mode").toString().equals("\"solo-fpp\"")) {
                            statsMap.put("agg_solo_fpp_longestkill", json_win_object.get("valueDec").toString());
                        } else if (jobject.get("mode").toString().equals("\"duo\"")) {
                            statsMap.put("agg_duo_longestkill", json_win_object.get("valueDec").toString());
                        } else if (jobject.get("mode").toString().equals("\"duo-fpp\"")) {
                            statsMap.put("agg_duo_fpp_longestkill", json_win_object.get("valueDec").toString());
                        } else if (jobject.get("mode").toString().equals("\"squad\"")) {
                            statsMap.put("agg_squad_longestkill", json_win_object.get("valueDec").toString());
                        } else if (jobject.get("mode").toString().equals("\"squad-fpp\"")) {
                            statsMap.put("agg_squad_fpp_longestkill", json_win_object.get("valueDec").toString());
                        }
                    }

                    //Get Rounds Played Stats
                    if (json_win_object.get("label").getAsString().equals("Rounds Played")) {
                        if (jobject.get("mode").toString().equals("\"solo\"")) {
                            statsMap.put("agg_solo_rndplayed", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"solo-fpp\"")) {
                            statsMap.put("agg_solo_fpp_rndplayed", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"duo\"")) {
                            statsMap.put("agg_duo_rndplayed", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"duo-fpp\"")) {
                            statsMap.put("agg_duo_fpp_rndplayed", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"squad\"")) {
                            statsMap.put("agg_squad_rndplayed", json_win_object.get("valueInt").toString());
                        } else if (jobject.get("mode").toString().equals("\"squad-fpp\"")) {
                            statsMap.put("agg_squad_fpp_rndplayed", json_win_object.get("valueInt").toString());
                        }
                    }

                    //Have to work on this since it's rank only for top 100
//				    //Get Rank Stats
//					if (json_win_object.get("label").getAsString().equals("Rating")) {
//						if (jobject.get("mode").toString().equals("\"solo\"")) {
//							statsMap.put("agg_solo_rank", json_win_object.get("rank").toString());
//						} else if (jobject.get("mode").toString().equals("\"solo-fpp\"")) {
////						statsMap.put("agg_solo_fpp_rank", json_win_object.get("rank").toString());
////					} else if (jobject.get("mode").toString().equals("\"duo\"")) {
////						statsMap.put("agg_duo_rank", json_win_object.get("rank").toString());
////					} else if (jobject.get("mode").toString().equals("\"duo-fpp\"")) {
////						statsMap.put("agg_duo_fpp_rank", json_win_object.get("rank").toString());
////					} else if (jobject.get("mode").toString().equals("\"squad\"")) {
//							statsMap.put("agg_squad_rank", json_win_object.get("rank").toString());
//						} else if (jobject.get("mode").toString().equals("\"squad-fpp\"")) {
////						statsMap.put("agg_squad_fpp_rank", json_win_object.get("rank").toString());
////					}
//					}
                }
            }
        }

        return statsMap;
    }

    private void WriteCurrentFiles(Map<String, String> currentStats, String season) {

        checkLog = "\r\n";
        finalLog = "\r\n";

        //Configured to process Wins
        if (var_wins && var_txt_wins != null) {
            checkLog = checkLog +
                    "Current Season Solo Wins: " + currentStats.get("agg_solo_win") + "\r\n" +
                    "Current Season Solo FPP Wins: " + currentStats.get("agg_solo_fpp_win") + "\r\n" +
                    "Current Season Duo Wins: " + currentStats.get("agg_duo_win") + "\r\n" +
                    "Current Season Duo FPP Wins: " + currentStats.get("agg_duo_fpp_win") + "\r\n" +
                    "Current Season Squad Wins: " + currentStats.get("agg_squad_win") + "\r\n" +
                    "Current Season Squad FPP Wins: " + currentStats.get("agg_squad_fpp_win") + "\r\n";
            finalLog = finalLog + "Total Wins: " + currentStats.get("total_win") + "\r\n";

            //Write the chicken dinners
            if (var_accessonly && season == "2018-01")
                writeToFile("seasononly_" + var_txt_wins, currentStats.get("total_win"));

            if (season == "totals")
                writeToFile(var_txt_wins, currentStats.get("total_win"));

        }

        //Configured to process Kills
        if (var_kills && var_txt_kills != null) {
            checkLog = checkLog +
                    "Current Season Solo Kills: " + currentStats.get("agg_solo_kill") + "\r\n" +
                    "Current Season Solo FPP Kills: " + currentStats.get("agg_solo_fpp_kill") + "\r\n" +
                    "Current Season Duo Kills: " + currentStats.get("agg_duo_kill") + "\r\n" +
                    "Current Season Duo FPP Kills: " + currentStats.get("agg_duo_fpp_kill") + "\r\n" +
                    "Current Season Squad Kills: " + currentStats.get("agg_squad_kill") + "\r\n" +
                    "Current Season Squad FPP Kills: " + currentStats.get("agg_squad_fpp_kill") + "\r\n";
            finalLog = finalLog + "Total Kills: " + currentStats.get("total_kill") + "\r\n";

            //Write the kills
            if (var_accessonly && season == "2018-01")
                writeToFile("seasononly_" + var_txt_kills, currentStats.get("total_kill"));

            if (season == "totals")
                writeToFile(var_txt_kills, currentStats.get("total_kill"));
        }

        //Configured to process Deaths
        if (var_deaths && var_txt_deaths != null) {
            checkLog = checkLog +
                    "Current Season Solo Deaths: " + currentStats.get("agg_solo_death") + "\r\n" +
                    "Current Season Solo FPP Deaths: " + currentStats.get("agg_solo_fpp_death") + "\r\n" +
                    "Current Season Duo Deaths: " + currentStats.get("agg_duo_death") + "\r\n" +
                    "Current Season Duo FPP Deaths: " + currentStats.get("agg_duo_fpp_death") + "\r\n" +
                    "Current Season Squad Deaths: " + currentStats.get("agg_squad_death") + "\r\n" +
                    "Current Season Squad FPP Deaths: " + currentStats.get("agg_squad_fpp_death") + "\r\n";
            finalLog = finalLog + "Total Deaths: " + currentStats.get("total_death") + "\r\n";

            //Write the deaths
            if (var_accessonly && season == "2018-01")
                writeToFile("seasononly_" + var_txt_deaths, currentStats.get("total_death"));

            if (season == "totals")
                writeToFile(var_txt_deaths, currentStats.get("total_death"));
        }

        //Configured to process Headshot Kills
        if (var_headshots && var_txt_headshots != null) {
            checkLog = checkLog +
                    "Current Season Solo Headshot Kills: " + currentStats.get("agg_solo_headshots") + "\r\n" +
                    "Current Season Solo FPP Headshot Kills: " + currentStats.get("agg_solo_fpp_headshots") + "\r\n" +
                    "Current Season Duo Headshot Kills: " + currentStats.get("agg_duo_headshots") + "\r\n" +
                    "Current Season Duo FPP Headshot Kills: " + currentStats.get("agg_duo_fpp_headshots") + "\r\n" +
                    "Current Season Squad Headshot Kills: " + currentStats.get("agg_squad_headshots") + "\r\n" +
                    "Current Season Squad FPP Headshot Kills: " + currentStats.get("agg_squad_fpp_headshots") + "\r\n";
            finalLog = finalLog + "Total Headshot Kills: " + currentStats.get("total_headshots") + "\r\n";

            //Write the headshots
            if (var_accessonly && season == "2018-01")
                writeToFile("seasononly_" + var_txt_headshots, currentStats.get("total_headshots"));

            if (season == "totals")
                writeToFile(var_txt_headshots, currentStats.get("total_headshots"));
        }

        //Configured to process Assists
        if (var_assists && var_txt_assists != null) {
            checkLog = checkLog +
                    "Current Season Solo Assists: " + currentStats.get("agg_solo_assists") + "\r\n" +
                    "Current Season Solo FPP Assists: " + currentStats.get("agg_solo_fpp_assists") + "\r\n" +
                    "Current Season Duo Assists: " + currentStats.get("agg_duo_assists") + "\r\n" +
                    "Current Season Duo FPP Assists: " + currentStats.get("agg_duo_fpp_assists") + "\r\n" +
                    "Current Season Squad Assists: " + currentStats.get("agg_squad_assists") + "\r\n" +
                    "Current Season Squad FPP Assists: " + currentStats.get("agg_squad_fpp_assists") + "\r\n";
            finalLog = finalLog + "Total Assists: " + currentStats.get("total_assists") + "\r\n";

            //Write the assists
            if (var_accessonly && season == "2018-01")
                writeToFile("seasononly_" + var_txt_assists, currentStats.get("total_assists"));

            if (season == "totals")
                writeToFile(var_txt_assists, currentStats.get("total_assists"));
        }

        //Configured to process Knock Outs
        if (var_knockouts && var_txt_knockouts != null) {
            checkLog = checkLog +
                    "Current Season Solo Knock Outs: " + currentStats.get("agg_solo_knockouts") + "\r\n" +
                    "Current Season Solo FPP Knock Outs: " + currentStats.get("agg_solo_fpp_knockouts") + "\r\n" +
                    "Current Season Duo Knock Outs: " + currentStats.get("agg_duo_knockouts") + "\r\n" +
                    "Current Season Duo FPP Knock Outs: " + currentStats.get("agg_duo_fpp_knockouts") + "\r\n" +
                    "Current Season Squad Knock Outs: " + currentStats.get("agg_squad_knockouts") + "\r\n" +
                    "Current Season Squad FPP Knock Outs: " + currentStats.get("agg_squad_fpp_knockouts") + "\r\n";
            finalLog = finalLog + "Total Knock Outs: " + currentStats.get("total_knockouts") + "\r\n";

            //Write the knockouts
            if (var_accessonly && season == "2018-01")
                writeToFile("seasononly_" + var_txt_knockouts, currentStats.get("total_knockouts"));

            if (season == "totals")
                writeToFile(var_txt_knockouts, currentStats.get("total_knockouts"));
        }

        //Configured to process K/D Ratio
        if (var_kdratio && var_txt_kdratio != null) {
            checkLog = checkLog +
                    "Current Season Solo K/D Ratio: " + currentStats.get("agg_solo_kdratio") + "\r\n" +
                    "Current Season Solo FPP K/D Ratio: " + currentStats.get("agg_solo_fpp_kdratio") + "\r\n" +
                    "Current Season Duo K/D Ratio: " + currentStats.get("agg_duo_kdratio") + "\r\n" +
                    "Current Season Duo FPP K/D Ratio: " + currentStats.get("agg_duo_fpp_kdratio") + "\r\n" +
                    "Current Season Squad K/D Ratio: " + currentStats.get("agg_squad_kdratio") + "\r\n" +
                    "Current Season Squad FPP K/D Ratio: " + currentStats.get("agg_squad_fpp_kdratio") + "\r\n";
            finalLog = finalLog + "Total K/D Ratio: " + String.format("%.2f", Double.parseDouble(currentStats.get("total_kdratio"))) + "\r\n";

            //Write the kdratio
            if (var_accessonly && season == "2018-01")
            writeToFile("seasononly_" + var_txt_kdratio, String.format("%.2f", Double.parseDouble(currentStats.get("total_kdratio"))));

            if (season == "totals")
                writeToFile(var_txt_kdratio, String.format("%.2f", Double.parseDouble(currentStats.get("total_kdratio"))));
        }

        //Configured to process Revives
        if (var_revives && var_txt_revives != null) {
            checkLog = checkLog +
                    "Current Season Solo Revives: " + currentStats.get("agg_solo_revives") + "\r\n" +
                    "Current Season Solo FPP Revives: " + currentStats.get("agg_solo_fpp_revives") + "\r\n" +
                    "Current Season Duo Revives: " + currentStats.get("agg_duo_revives") + "\r\n" +
                    "Current Season Duo FPP Revives: " + currentStats.get("agg_duo_fpp_revives") + "\r\n" +
                    "Current Season Squad Revives: " + currentStats.get("agg_squad_revives") + "\r\n" +
                    "Current Season Squad FPP Revives: " + currentStats.get("agg_squad_fpp_revives") + "\r\n";
            finalLog = finalLog + "Total Revives: " + currentStats.get("total_revives") + "\r\n";

            //Write the revives
            if (var_accessonly && season == "2018-01")
                writeToFile("seasononly_" + var_txt_revives, currentStats.get("total_revives"));

            if (season == "totals")
                writeToFile(var_txt_revives, currentStats.get("total_revives"));
        }

        //Configured to process Rounds Played
        if (var_rndplayed && var_txt_rndplayed != null) {
            checkLog = checkLog +
                    "Current Season Solo Rounds Played: " + currentStats.get("agg_solo_rndplayed") + "\r\n" +
                    "Current Season Solo FPP Rounds Played: " + currentStats.get("agg_solo_fpp_rndplayed") + "\r\n" +
                    "Current Season Duo Rounds Played: " + currentStats.get("agg_duo_rndplayed") + "\r\n" +
                    "Current Season Duo FPP Rounds Played: " + currentStats.get("agg_duo_fpp_rndplayed") + "\r\n" +
                    "Current Season Squad Rounds Played: " + currentStats.get("agg_squad_rndplayed") + "\r\n" +
                    "Current Season Squad FPP Rounds Played: " + currentStats.get("agg_squad_fpp_rndplayed") + "\r\n";
            finalLog = finalLog + "Total Rounds Played: " + currentStats.get("total_rndplayed") + "\r\n";

            //Write the rndplayed
            if (var_accessonly && season == "2018-01")
                writeToFile("seasononly_" + var_txt_rndplayed, currentStats.get("total_rndplayed"));

            if (season == "totals")
                writeToFile(var_txt_rndplayed, currentStats.get("total_rndplayed"));
        }

        //Configured to process Longest Kill
        if (var_longestkill && var_txt_longestkill != null) {
            checkLog = checkLog +
                    "Current Season Solo Longest Kill: " + currentStats.get("agg_solo_longestkill") + "\r\n" +
                    "Current Season Solo FPP Longest Kill: " + currentStats.get("agg_solo_fpp_longestkill") + "\r\n" +
                    "Current Season Duo Longest Kill: " + currentStats.get("agg_duo_longestkill") + "\r\n" +
                    "Current Season Duo FPP Longest Kill: " + currentStats.get("agg_duo_fpp_longestkill") + "\r\n" +
                    "Current Season Squad Longest Kill: " + currentStats.get("agg_squad_longestkill") + "\r\n" +
                    "Current Season Squad FPP Longest Kill: " + currentStats.get("agg_squad_fpp_longestkill") + "\r\n";
            finalLog = finalLog + "Longest Kill: " + currentStats.get("total_longestkill") + "\r\n";

            //Write the kdratio
            if (var_accessonly && season == "2018-01")
                writeToFile("seasononly_" + var_txt_longestkill, String.format("%.2f", Double.parseDouble(currentStats.get("total_longestkill"))));

            if (season == "totals")
                writeToFile(var_txt_longestkill, String.format("%.2f", Double.parseDouble(currentStats.get("total_longestkill"))));
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

    //Go through all stats rows and if one is null, set to zero
    public Map<String, String> CleanStats (Map<String, String> fixStats) {

        if (fixStats.get("agg_solo_win") == null) { fixStats.put("agg_solo_win", "0"); }
        if (fixStats.get("agg_solo_fpp_win") == null) { fixStats.put("agg_solo_fpp_win", "0"); }
        if (fixStats.get("agg_duo_win") == null) { fixStats.put("agg_duo_win", "0"); }
        if (fixStats.get("agg_duo_fpp_win") == null) { fixStats.put("agg_duo_fpp_win", "0"); }
        if (fixStats.get("agg_squad_win") == null) { fixStats.put("agg_squad_win", "0"); }
        if (fixStats.get("agg_squad_fpp_win") == null) { fixStats.put("agg_squad_fpp_win", "0"); }
        if (fixStats.get("total_win") == null) { fixStats.put("total_win", "0"); }

        if (fixStats.get("agg_solo_kill") == null) { fixStats.put("agg_solo_kill", "0"); }
        if (fixStats.get("agg_solo_fpp_kill") == null) { fixStats.put("agg_solo_fpp_kill", "0"); }
        if (fixStats.get("agg_duo_kill") == null) { fixStats.put("agg_duo_kill", "0"); }
        if (fixStats.get("agg_duo_fpp_kill") == null) { fixStats.put("agg_duo_fpp_kill", "0"); }
        if (fixStats.get("agg_squad_kill") == null) { fixStats.put("agg_squad_kill", "0"); }
        if (fixStats.get("agg_squad_fpp_kill") == null) { fixStats.put("agg_squad_fpp_kill", "0"); }
        if (fixStats.get("total_kill") == null) { fixStats.put("total_kill", "0"); }

        if (fixStats.get("agg_solo_death") == null) { fixStats.put("agg_solo_death", "0"); }
        if (fixStats.get("agg_solo_fpp_death") == null) { fixStats.put("agg_solo_fpp_death", "0"); }
        if (fixStats.get("agg_duo_death") == null) { fixStats.put("agg_duo_death", "0"); }
        if (fixStats.get("agg_duo_fpp_death") == null) { fixStats.put("agg_duo_fpp_death", "0"); }
        if (fixStats.get("agg_squad_death") == null) { fixStats.put("agg_squad_death", "0"); }
        if (fixStats.get("agg_squad_fpp_death") == null) { fixStats.put("agg_squad_fpp_death", "0"); }
        if (fixStats.get("total_death") == null) { fixStats.put("total_death", "0"); }

        if (fixStats.get("agg_solo_headshots") == null) { fixStats.put("agg_solo_headshots", "0"); }
        if (fixStats.get("agg_solo_fpp_headshots") == null) { fixStats.put("agg_solo_fpp_headshots", "0"); }
        if (fixStats.get("agg_duo_headshots") == null) { fixStats.put("agg_duo_headshots", "0"); }
        if (fixStats.get("agg_duo_fpp_headshots") == null) { fixStats.put("agg_duo_fpp_headshots", "0"); }
        if (fixStats.get("agg_squad_headshots") == null) { fixStats.put("agg_squad_headshots", "0"); }
        if (fixStats.get("agg_squad_fpp_headshots") == null) { fixStats.put("agg_squad_fpp_headshots", "0"); }
        if (fixStats.get("total_headshots") == null) { fixStats.put("total_headshots", "0"); }

        if (fixStats.get("agg_solo_assists") == null) { fixStats.put("agg_solo_assists", "0"); }
        if (fixStats.get("agg_solo_fpp_assists") == null) { fixStats.put("agg_solo_fpp_assists", "0"); }
        if (fixStats.get("agg_duo_assists") == null) { fixStats.put("agg_duo_assists", "0"); }
        if (fixStats.get("agg_duo_fpp_assists") == null) { fixStats.put("agg_duo_fpp_assists", "0"); }
        if (fixStats.get("agg_squad_assists") == null) { fixStats.put("agg_squad_assists", "0"); }
        if (fixStats.get("agg_squad_fpp_assists") == null) { fixStats.put("agg_squad_fpp_assists", "0"); }
        if (fixStats.get("total_assists") == null) { fixStats.put("total_assists", "0"); }

        if (fixStats.get("agg_solo_knockouts") == null) { fixStats.put("agg_solo_knockouts", "0"); }
        if (fixStats.get("agg_solo_fpp_knockouts") == null) { fixStats.put("agg_solo_fpp_knockouts", "0"); }
        if (fixStats.get("agg_duo_knockouts") == null) { fixStats.put("agg_duo_knockouts", "0"); }
        if (fixStats.get("agg_duo_fpp_knockouts") == null) { fixStats.put("agg_duo_fpp_knockouts", "0"); }
        if (fixStats.get("agg_squad_knockouts") == null) { fixStats.put("agg_squad_knockouts", "0"); }
        if (fixStats.get("agg_squad_fpp_knockouts") == null) { fixStats.put("agg_squad_fpp_knockouts", "0"); }
        if (fixStats.get("total_knockouts") == null) { fixStats.put("total_knockouts", "0"); }

        if (fixStats.get("agg_solo_kdratio") == null) { fixStats.put("agg_solo_kdratio", "0"); }
        if (fixStats.get("agg_solo_fpp_kdratio") == null) { fixStats.put("agg_solo_fpp_kdratio", "0"); }
        if (fixStats.get("agg_duo_kdratio") == null) { fixStats.put("agg_duo_kdratio", "0"); }
        if (fixStats.get("agg_duo_fpp_kdratio") == null) { fixStats.put("agg_duo_fpp_kdratio", "0"); }
        if (fixStats.get("agg_squad_kdratio") == null) { fixStats.put("agg_squad_kdratio", "0"); }
        if (fixStats.get("agg_squad_fpp_kdratio") == null) { fixStats.put("agg_squad_fpp_kdratio", "0"); }
        if (fixStats.get("total_kdratio") == null) { fixStats.put("total_kdratio", "0"); }

        if (fixStats.get("agg_solo_revives") == null) { fixStats.put("agg_solo_revives", "0"); }
        if (fixStats.get("agg_solo_fpp_revives") == null) { fixStats.put("agg_solo_fpp_revives", "0"); }
        if (fixStats.get("agg_duo_revives") == null) { fixStats.put("agg_duo_revives", "0"); }
        if (fixStats.get("agg_duo_fpp_revives") == null) { fixStats.put("agg_duo_fpp_revives", "0"); }
        if (fixStats.get("agg_squad_revives") == null) { fixStats.put("agg_squad_revives", "0"); }
        if (fixStats.get("agg_squad_fpp_revives") == null) { fixStats.put("agg_squad_fpp_revives", "0"); }
        if (fixStats.get("total_revives") == null) { fixStats.put("total_revives", "0"); }

        if (fixStats.get("agg_solo_rndplayed") == null) { fixStats.put("agg_solo_rndplayed", "0"); }
        if (fixStats.get("agg_solo_fpp_rndplayed") == null) { fixStats.put("agg_solo_fpp_rndplayed", "0"); }
        if (fixStats.get("agg_duo_rndplayed") == null) { fixStats.put("agg_duo_rndplayed", "0"); }
        if (fixStats.get("agg_duo_fpp_rndplayed") == null) { fixStats.put("agg_duo_fpp_rndplayed", "0"); }
        if (fixStats.get("agg_squad_rndplayed") == null) { fixStats.put("agg_squad_rndplayed", "0"); }
        if (fixStats.get("agg_squad_fpp_rndplayed") == null) { fixStats.put("agg_squad_fpp_rndplayed", "0"); }
        if (fixStats.get("total_rndplayed") == null) { fixStats.put("total_rndplayed", "0"); }

        if (fixStats.get("agg_solo_longestkill") == null) { fixStats.put("agg_solo_longestkill", "0"); }
        if (fixStats.get("agg_solo_fpp_longestkill") == null) { fixStats.put("agg_solo_fpp_longestkill", "0"); }
        if (fixStats.get("agg_duo_longestkill") == null) { fixStats.put("agg_duo_longestkill", "0"); }
        if (fixStats.get("agg_duo_fpp_longestkill") == null) { fixStats.put("agg_duo_fpp_longestkill", "0"); }
        if (fixStats.get("agg_squad_longestkill") == null) { fixStats.put("agg_squad_longestkill", "0"); }
        if (fixStats.get("agg_squad_fpp_longestkill") == null) { fixStats.put("agg_squad_fpp_longestkill", "0"); }
        if (fixStats.get("total_longestkill") == null) { fixStats.put("total_longestkill", "0"); }

        return fixStats;
    }


    //Add new stats to the old stats totals
    public Map<String, String> CompileStats (Map<String, String> currentStats, Map<String, String> newStats) {

        currentStats.put("total_win", Integer.toString(Integer.parseInt(newStats.get("agg_solo_win")) + Integer.parseInt(newStats.get("agg_solo_fpp_win")) + Integer.parseInt(newStats.get("agg_duo_win")) + Integer.parseInt(newStats.get("agg_duo_fpp_win")) + Integer.parseInt(newStats.get("agg_squad_win")) + Integer.parseInt(newStats.get("agg_squad_fpp_win")) + Integer.parseInt(currentStats.get("total_win"))));
        currentStats.put("total_kill", Integer.toString(Integer.parseInt(newStats.get("agg_solo_kill")) + Integer.parseInt(newStats.get("agg_solo_fpp_kill")) + Integer.parseInt(newStats.get("agg_duo_kill")) + Integer.parseInt(newStats.get("agg_duo_fpp_kill")) + Integer.parseInt(newStats.get("agg_squad_kill")) + Integer.parseInt(newStats.get("agg_squad_fpp_kill")) + Integer.parseInt(currentStats.get("total_kill"))));
        currentStats.put("total_death", Integer.toString(Integer.parseInt(newStats.get("agg_solo_death")) + Integer.parseInt(newStats.get("agg_solo_fpp_death")) + Integer.parseInt(newStats.get("agg_duo_death")) + Integer.parseInt(newStats.get("agg_duo_fpp_death")) + Integer.parseInt(newStats.get("agg_squad_death")) + Integer.parseInt(newStats.get("agg_squad_fpp_death")) + Integer.parseInt(currentStats.get("total_death"))));
        currentStats.put("total_headshots", Integer.toString(Integer.parseInt(newStats.get("agg_solo_headshots")) + Integer.parseInt(newStats.get("agg_solo_fpp_headshots")) + Integer.parseInt(newStats.get("agg_duo_headshots")) + Integer.parseInt(newStats.get("agg_duo_fpp_headshots")) + Integer.parseInt(newStats.get("agg_squad_headshots")) + Integer.parseInt(newStats.get("agg_squad_fpp_headshots")) + Integer.parseInt(currentStats.get("total_headshots"))));
        currentStats.put("total_assists", Integer.toString(Integer.parseInt(newStats.get("agg_solo_assists")) + Integer.parseInt(newStats.get("agg_solo_fpp_assists")) + Integer.parseInt(newStats.get("agg_duo_assists")) + Integer.parseInt(newStats.get("agg_duo_fpp_assists")) + Integer.parseInt(newStats.get("agg_squad_assists")) + Integer.parseInt(newStats.get("agg_squad_fpp_assists")) + Integer.parseInt(currentStats.get("total_assists"))));
        currentStats.put("total_knockouts", Integer.toString(Integer.parseInt(newStats.get("agg_solo_knockouts")) + Integer.parseInt(newStats.get("agg_solo_fpp_knockouts")) + Integer.parseInt(newStats.get("agg_duo_knockouts")) + Integer.parseInt(newStats.get("agg_duo_fpp_knockouts")) + Integer.parseInt(newStats.get("agg_squad_knockouts")) + Integer.parseInt(newStats.get("agg_squad_fpp_knockouts")) + Integer.parseInt(currentStats.get("total_knockouts"))));

        //Only calculate valid KDR values
        int kdr_count = 0;
        double kdr_total = 0.00;

        if (Double.parseDouble(currentStats.get("total_kdratio")) > 0) {
            kdr_total += Double.parseDouble(currentStats.get("total_kdratio"));
            ++kdr_count;
        }
        if (Double.parseDouble(newStats.get("agg_solo_kdratio")) > 0) {
            kdr_total += Double.parseDouble(newStats.get("agg_solo_kdratio"));
            ++kdr_count;
        }
        if (Double.parseDouble(newStats.get("agg_solo_fpp_kdratio")) > 0) {
            kdr_total += Double.parseDouble(newStats.get("agg_solo_fpp_kdratio"));
            ++kdr_count;
        }
        if (Double.parseDouble(newStats.get("agg_duo_kdratio")) > 0) {
            kdr_total += Double.parseDouble(newStats.get("agg_duo_kdratio"));
            ++kdr_count;
        }
        if (Double.parseDouble(newStats.get("agg_duo_fpp_kdratio")) > 0) {
            kdr_total += Double.parseDouble(newStats.get("agg_duo_fpp_kdratio"));
            ++kdr_count;
        }
        if (Double.parseDouble(newStats.get("agg_squad_kdratio")) > 0) {
            kdr_total += Double.parseDouble(newStats.get("agg_squad_kdratio"));
            ++kdr_count;
        }
        if (Double.parseDouble(newStats.get("agg_squad_fpp_kdratio")) > 0) {
            kdr_total += Double.parseDouble(newStats.get("agg_squad_fpp_kdratio"));
            ++kdr_count;
        }
        currentStats.put("total_kdratio", Double.toString(kdr_total/kdr_count));

        currentStats.put("total_revives", Integer.toString(Integer.parseInt(newStats.get("agg_solo_revives")) + Integer.parseInt(newStats.get("agg_solo_fpp_revives")) + Integer.parseInt(newStats.get("agg_duo_revives")) + Integer.parseInt(newStats.get("agg_duo_fpp_revives")) + Integer.parseInt(newStats.get("agg_squad_revives")) + Integer.parseInt(newStats.get("agg_squad_fpp_revives")) + Integer.parseInt(currentStats.get("total_revives"))));
        currentStats.put("total_rndplayed", Integer.toString(Integer.parseInt(newStats.get("agg_solo_rndplayed")) + Integer.parseInt(newStats.get("agg_solo_fpp_rndplayed")) + Integer.parseInt(newStats.get("agg_duo_rndplayed")) + Integer.parseInt(newStats.get("agg_duo_fpp_rndplayed")) + Integer.parseInt(newStats.get("agg_squad_rndplayed")) + Integer.parseInt(newStats.get("agg_squad_fpp_rndplayed")) + Integer.parseInt(currentStats.get("total_rndplayed"))));
        //Find Longest Kill
        currentStats.put("total_longestkill", (Double.parseDouble(newStats.get("agg_solo_longestkill")) > Double.parseDouble(currentStats.get("total_longestkill"))) ? newStats.get("agg_solo_longestkill") : currentStats.get("total_longestkill"));
        currentStats.put("total_longestkill", (Double.parseDouble(newStats.get("agg_solo_fpp_longestkill")) > Double.parseDouble(currentStats.get("total_longestkill"))) ? newStats.get("agg_solo_fpp_longestkill") : currentStats.get("total_longestkill"));
        currentStats.put("total_longestkill", (Double.parseDouble(newStats.get("agg_duo_longestkill")) > Double.parseDouble(currentStats.get("total_longestkill"))) ? newStats.get("agg_duo_longestkill") : currentStats.get("total_longestkill"));
        currentStats.put("total_longestkill", (Double.parseDouble(newStats.get("agg_duo_fpp_longestkill")) > Double.parseDouble(currentStats.get("total_longestkill"))) ? newStats.get("agg_duo_fpp_longestkill") : currentStats.get("total_longestkill"));
        currentStats.put("total_longestkill", (Double.parseDouble(newStats.get("agg_squad_longestkill")) > Double.parseDouble(currentStats.get("total_longestkill"))) ? newStats.get("agg_squad_longestkill") : currentStats.get("total_longestkill"));
        currentStats.put("total_longestkill", (Double.parseDouble(newStats.get("agg_squad_fpp_longestkill")) > Double.parseDouble(currentStats.get("total_longestkill"))) ? newStats.get("agg_squad_fpp_longestkill") : currentStats.get("total_longestkill"));

//
//		//Find Rank
//		if(sel_rank_type.equals("BEST")) {
//			total_rank = (agg_solo_rank > agg_duo_rank) ? agg_solo_rank:agg_duo_rank;
//			total_rank = (agg_squad_rank > total_rank) ? agg_squad_rank:total_rank;
//		} else if (sel_rank_type.equals("SOLO")) {
//			total_rank = agg_solo_rank;
//		} else if (sel_rank_type.equals("DUO")) {
//			total_rank = agg_duo_rank;
//		} else if (sel_rank_type.equals("SQUAD")) {
//			total_rank = agg_squad_rank;
//		}

        return currentStats;
    }
}