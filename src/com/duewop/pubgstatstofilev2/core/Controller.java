package com.duewop.pubgstatstofilev2.core;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.swing.text.DefaultCaret;
import java.io.*;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class Controller extends Application {

	//FXML Variables
	@FXML
	private TextField apikey;
	@FXML
	private TextField username;
//	@FXML
//	private TextField previous_deaths;
//	@FXML
//	private TextField previous_kills;
//	@FXML
//	private TextField previous_wins;
//	@FXML
//	private TextField current_season;
	@FXML
	private Label lbl_output;
	@FXML
	private TextArea txtWorkLog;


	//Selection Checkboxes
	@FXML
	private CheckBox chk_wins;
	@FXML
	private CheckBox chk_kills;
	@FXML
	private CheckBox chk_deaths;
	@FXML
	private CheckBox chk_headshots;
	@FXML
	private CheckBox chk_assists;
	@FXML
	private CheckBox chk_knockouts;
	@FXML
	private CheckBox chk_kdratio;
	@FXML
	private CheckBox chk_revives;
	@FXML
	private CheckBox chk_longestkill;
	@FXML
	private CheckBox chk_rndplayed;
//	@FXML
//	private CheckBox chk_rank;
	@FXML
	private CheckBox chk_accessonly;

	//Selection Text Fields
	@FXML
	private TextField txt_wins;
	@FXML
	private TextField txt_kills;
	@FXML
	private TextField txt_deaths;
	@FXML
	private TextField txt_headshots;
	@FXML
	private TextField txt_assists;
	@FXML
	private TextField txt_knockouts;
	@FXML
	private TextField txt_kdratio;
	@FXML
	private TextField txt_revives;
	@FXML
	private TextField txt_longestkill;
	@FXML
	private TextField txt_rndplayed;
//	@FXML
//	private TextField txt_rank;
//	@FXML
//	private ChoiceBox sel_rank_type;


	//Properties File Selection booleans
	private boolean var_wins;
	private boolean var_kills;
	private boolean var_deaths;
	private boolean var_headshots;
	private boolean var_assists;
	private boolean var_knockouts;
	private boolean var_kdratio;
	private boolean var_revives;
	private boolean var_longestkill;
	private boolean var_rndplayed;
	private boolean var_rank;
	private boolean var_accessonly;

	//Properties File Text Fields
	private String var_txt_wins;
	private String var_txt_kills;
	private String var_txt_deaths;
	private String var_txt_headshots;
	private String var_txt_assists;
	private String var_txt_knockouts;
	private String var_txt_kdratio;
	private String var_txt_revives;
	private String var_txt_longestkill;
	private String var_txt_rndplayed;
	private String var_txt_rank;
	private String var_sel_rank;

	//Config variables
	private String strApiKey = null;
	private String strUserName = null;
	private String strPreviousDeaths = null;
	private String strPreviousKills = null;
	private String strPreviousWins = null;
	private String strCurrentSeason = null;
	private String strOutputDir = null;
	private String data;


	private String workLog = null;
	//Error check
	private boolean bad_selection = false;

	/**
	 * Initializes the controller class.
	 */
	@FXML
	private void initialize() {
		Properties prop = new Properties();
		InputStream input;

		try {
			File f = new File("config.properties");
			if (f.exists() && !f.isDirectory()) {

				input = new FileInputStream("config.properties");

				// load a properties file
				prop.load(input);

				this.strApiKey = prop.getProperty("apikey");
				this.strUserName = prop.getProperty("username");
//				this.strPreviousWins = prop.getProperty("previous_wins");
//				this.strPreviousKills = prop.getProperty("previous_kills");
//				this.strPreviousDeaths = prop.getProperty("previous_deaths");
//				this.strCurrentSeason = prop.getProperty("current_season");
				this.strOutputDir = prop.getProperty("output_dir");

				apikey.setText(strApiKey);
				username.setText(strUserName);
//				previous_wins.setText(strPreviousWins);
//				previous_kills.setText(strPreviousKills);
//				previous_deaths.setText(strPreviousDeaths);
//				current_season.setText(strCurrentSeason);

				this.var_wins = Boolean.parseBoolean(prop.getProperty("chk_wins"));
				this.var_txt_wins = prop.getProperty("txt_wins");
				chk_wins.setSelected(var_wins);
				txt_wins.setText(var_txt_wins);

				this.var_kills = Boolean.parseBoolean(prop.getProperty("chk_kills"));
				this.var_txt_kills = prop.getProperty("txt_kills");
				chk_kills.setSelected(var_kills);
				txt_kills.setText(var_txt_kills);

				this.var_deaths = Boolean.parseBoolean(prop.getProperty("chk_deaths"));
				this.var_txt_deaths = prop.getProperty("txt_deaths");
				chk_deaths.setSelected(var_deaths);
				txt_deaths.setText(var_txt_deaths);

				this.var_headshots = Boolean.parseBoolean(prop.getProperty("chk_headshots"));
				this.var_txt_headshots = prop.getProperty("txt_headshots");
				chk_headshots.setSelected(var_headshots);
				txt_headshots.setText(var_txt_headshots);

				this.var_assists = Boolean.parseBoolean(prop.getProperty("chk_assists"));
				this.var_txt_assists = prop.getProperty("txt_assists");
				chk_assists.setSelected(var_assists);
				txt_assists.setText(var_txt_assists);

				this.var_knockouts = Boolean.parseBoolean(prop.getProperty("chk_knockouts"));
				this.var_txt_knockouts = prop.getProperty("txt_knockouts");
				chk_knockouts.setSelected(var_knockouts);
				txt_knockouts.setText(var_txt_knockouts);

				this.var_kdratio = Boolean.parseBoolean(prop.getProperty("chk_kdratio"));
				this.var_txt_kdratio = prop.getProperty("txt_kdratio");
				chk_kdratio.setSelected(var_kdratio);
				txt_kdratio.setText(var_txt_kdratio);

				this.var_revives = Boolean.parseBoolean(prop.getProperty("chk_revives"));
				this.var_txt_revives = prop.getProperty("txt_revives");
				chk_revives.setSelected(var_revives);
				txt_revives.setText(var_txt_revives);

				this.var_longestkill = Boolean.parseBoolean(prop.getProperty("chk_longestkill"));
				this.var_txt_longestkill = prop.getProperty("txt_longestkill");
				chk_longestkill.setSelected(var_longestkill);
				txt_longestkill.setText(var_txt_longestkill);

				this.var_rndplayed = Boolean.parseBoolean(prop.getProperty("chk_rndplayed"));
				this.var_txt_rndplayed = prop.getProperty("txt_rndplayed");
				chk_rndplayed.setSelected(var_rndplayed);
				txt_rndplayed.setText(var_txt_rndplayed);

//				this.var_rank = Boolean.parseBoolean(prop.getProperty("chk_rank"));
//				this.var_txt_rank = prop.getProperty("txt_rank");
//				chk_rank.setSelected(var_rank);
//				txt_rank.setText(var_txt_rank);

//				this.var_sel_rank = prop.getProperty("sel_rank");
//
//				if (sel_rank_type.getItems().size() == 0) {
//
//					sel_rank_type.getItems().add("BEST");
//					sel_rank_type.getItems().add("SOLO");
//					sel_rank_type.getItems().add("DUO");
//					sel_rank_type.getItems().add("SQUAD");
//				}
//
//				if (this.var_sel_rank != null) {
//					sel_rank_type.setValue(this.var_sel_rank);
//				}

				this.var_accessonly = Boolean.parseBoolean(prop.getProperty("chk_accessonly"));
				chk_accessonly.setSelected(this.var_accessonly);

				if (strOutputDir != null) {
					lbl_output.setText(strOutputDir);
				}

				input.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (checkValues()) {
			runProcesses();
		}
	}

	/**
	 * Creates PUBG Stats object and passes the configuration data.
	 */
	private void runProcesses() {

		Timer time = new Timer();
		time.schedule( new TimerTask() {
			public void run() {
				Date thisDate = new Date();

				PUBGStats currentStats = new PUBGStats();
				try {
					currentStats.RunProcess();
				} catch (IOException e) {
					e.printStackTrace();
				}
				txtWorkLog.setText(thisDate + "\r\n" + username.getText() + "\r\n" + currentStats.getWorkLog());
			}
		}, 0, 60*1000);
	}

	/**
	 * Open webpage from URI.
	 */
	public void openWebpage() {
		getHostServices().showDocument("https://mixer.com/Rem-AG");
	}

	/**
	 * Check if the required fields are completed.
	 */
	private boolean checkValues() {
		workLog = "";
		bad_selection = false;

		if (strApiKey == null || apikey.getText().isEmpty()) {
			bad_selection = true;
			workLog = "Please enter the API Key.\n";
		}
		if (strUserName == null || username.getText().isEmpty()) {
			bad_selection = true;
			workLog = workLog + "Please enter the PUBG Username.\n";
		}
//		if (strPreviousWins == null || previous_wins.getText().isEmpty()) {
//			bad_selection = true;
//			workLog = workLog + "Please enter the previous wins (or zero).\n";
//		}
//		if (strPreviousKills == null || strPreviousKills == "") {
//			bad_selection = true;
//			workLog = workLog + "Please enter the previous kills (or zero).\n";
//		}
//		if (strPreviousDeaths == null || strPreviousDeaths == "") {
//			bad_selection = true;
//			workLog = workLog + "Please enter the previous deaths (or zero).\n";
//		}
//		if (strCurrentSeason == null || strCurrentSeason == "") {
//			bad_selection = true;
//			workLog = workLog + "Please enter the current season. (2017-pre5, 2017-pre6, 2018-01, etc)\n";
//		}
		if (strOutputDir == null) {
			bad_selection = true;
			workLog = workLog + "Please select a valid output directory.\n";
		}

		if (chk_wins.isSelected() && txt_wins.getText().isEmpty()) {
			bad_selection = true;
			workLog = workLog + "Please enter a filename for Wins or uncheck the box.\n";
		}

		if (chk_kills.isSelected() && txt_kills.getText().isEmpty()) {
			bad_selection = true;
			workLog = workLog + "Please enter a filename for Kills or uncheck the box.\n";
		}

		if (chk_deaths.isSelected() && txt_deaths.getText().isEmpty()) {
			bad_selection = true;
			workLog = workLog + "Please enter a filename for Deaths or uncheck the box.\n";
		}

		if (chk_headshots.isSelected() && txt_headshots.getText().isEmpty()) {
			bad_selection = true;
			workLog = workLog + "Please enter a filename for Headshot Kills or uncheck the box.\n";
		}

		if (chk_assists.isSelected() && txt_assists.getText().isEmpty()) {
			bad_selection = true;
			workLog = workLog + "Please enter a filename for Assists or uncheck the box.\n";
		}

		if (chk_knockouts.isSelected() && txt_knockouts.getText().isEmpty()) {
			bad_selection = true;
			workLog = workLog + "Please enter a filename for Knockouts or uncheck the box.\n";
		}

		if (chk_kdratio.isSelected() && txt_kdratio.getText().isEmpty()) {
			bad_selection = true;
			workLog = workLog + "Please enter a filename for K/D Ratio or uncheck the box.\n";
		}

		if (chk_revives.isSelected() && txt_revives.getText().isEmpty()) {
			bad_selection = true;
			workLog = workLog + "Please enter a filename for Revives or uncheck the box.\n";
		}

		if (chk_longestkill.isSelected() && txt_longestkill.getText().isEmpty()) {
			bad_selection = true;
			workLog = workLog + "Please enter a filename for Longest Kill or uncheck the box.\n";
		}

		if (chk_rndplayed.isSelected() && txt_rndplayed.getText().isEmpty()) {
			bad_selection = true;
			workLog = workLog + "Please enter a filename for Rounds Played or uncheck the box.\n";
		}
//		if (chk_rank.isSelected() && sel_rank_type.getValue() == null && txt_rank.getText().isEmpty()) {
//			bad_selection = true;
//			workLog = workLog + "Please select a Type for Rating and enter a filename or uncheck the box.\n";
//		}

		if (!chk_wins.isSelected() && !chk_kills.isSelected() && !chk_deaths.isSelected() && !chk_headshots.isSelected() && !chk_assists.isSelected() && !chk_knockouts.isSelected() &&
				!chk_kdratio.isSelected() && !chk_revives.isSelected() && !chk_longestkill.isSelected() && !chk_rndplayed.isSelected()) {
			bad_selection = true;
			workLog = workLog + "You must select at least one option, otherwise, why bother running me?\n";
		}

		if (chk_accessonly.isSelected() && !chk_wins.isSelected() && !chk_kills.isSelected() && !chk_deaths.isSelected()) {
			bad_selection = true;
			workLog = workLog + "You need to have a Win, Kill or Death selection to use Early Access 5 stats.\n";
		}

		if (bad_selection) {

			String errors = "This form contains errors. Please do the following and Save to retry connection.\n";

			txtWorkLog.setText(errors + workLog);

			return false;
		} else {
			return true;
		}
	}

	/**
	 * Called when the user clicks save.
	 */
	@FXML
	private void handleSave() {
		Properties prop = new Properties();
		OutputStream output = null;

		try {
			output = new FileOutputStream("config.properties");

			prop.setProperty("apikey", apikey.getText());

			if (!username.getText().equals("")) {
				prop.setProperty("username", username.getText());
			} else {
				prop.remove("username");
			}

//			if (!previous_wins.getText().equals("")) {
//				prop.setProperty("previous_wins", previous_wins.getText());
//			} else {
//				prop.remove("previous_wins");
//			}
//
//			prop.setProperty("previous_kills", previous_kills.getText());
//			prop.setProperty("previous_deaths", previous_deaths.getText());
//
//			if (!current_season.getText().equals("")) {
//				prop.setProperty("current_season", current_season.getText());
//			} else {
//				prop.remove("current_season");
//			}

			if (strOutputDir != null) {
				prop.setProperty("output_dir", strOutputDir);
			}

			//Set Fields
			prop.setProperty("chk_wins", chk_wins.isSelected() ? "true" : "false");
			if (!txt_wins.getText().isEmpty()) {
				prop.setProperty("txt_wins", txt_wins.getText());
			} else {
				prop.remove("txt_wins");
			}
			prop.setProperty("chk_kills", chk_kills.isSelected() ? "true" : "false");
			if (!txt_kills.getText().isEmpty()) {
				prop.setProperty("txt_kills", txt_kills.getText());
			} else {
				prop.remove("txt_kills");
			}
			prop.setProperty("chk_deaths", chk_deaths.isSelected() ? "true" : "false");
			if (!txt_deaths.getText().isEmpty()) {
				prop.setProperty("txt_deaths", txt_deaths.getText());
			} else {
				prop.remove("txt_deaths");
			}
			prop.setProperty("chk_headshots", chk_headshots.isSelected() ? "true" : "false");
			if (!txt_headshots.getText().isEmpty()) {
				prop.setProperty("txt_headshots", txt_headshots.getText());
			} else {
				prop.remove("txt_headshots");
			}
			prop.setProperty("chk_assists", chk_assists.isSelected() ? "true" : "false");
			if (!txt_assists.getText().isEmpty()) {
				prop.setProperty("txt_assists", txt_assists.getText());
			} else {
				prop.remove("txt_assists");
			}
			prop.setProperty("chk_knockouts", chk_knockouts.isSelected() ? "true" : "false");
			if (!txt_knockouts.getText().isEmpty()) {
				prop.setProperty("txt_knockouts", txt_knockouts.getText());
			} else {
				prop.remove("txt_knockouts");
			}
			prop.setProperty("chk_kdratio", chk_kdratio.isSelected() ? "true" : "false");
			if (!txt_kdratio.getText().isEmpty()) {
				prop.setProperty("txt_kdratio", txt_kdratio.getText());
			} else {
				prop.remove("txt_kdratio");
			}
			prop.setProperty("chk_revives", chk_revives.isSelected() ? "true" : "false");
			if (!txt_revives.getText().isEmpty()) {
				prop.setProperty("txt_revives", txt_revives.getText());
			} else {
				prop.remove("txt_revives");
			}
			prop.setProperty("chk_longestkill", chk_longestkill.isSelected() ? "true" : "false");
			if (!txt_longestkill.getText().isEmpty()) {
				prop.setProperty("txt_longestkill", txt_longestkill.getText());
			} else {
				prop.remove("txt_longestkill");
			}
			prop.setProperty("chk_rndplayed", chk_rndplayed.isSelected() ? "true" : "false");
			if (!txt_rndplayed.getText().isEmpty()) {
				prop.setProperty("txt_rndplayed", txt_rndplayed.getText());
			} else {
				prop.remove("txt_rndplayed");
			}
//			prop.setProperty("chk_rank", chk_rank.isSelected() ? "true" : "false");
//			if (!txt_rank.getText().isEmpty()) {
//				prop.setProperty("txt_rank", txt_rank.getText());
//			} else {
//				prop.remove("txt_rank");
//			}

//			prop.setProperty("sel_rank", sel_rank_type.getValue().toString());
			prop.setProperty("chk_accessonly", chk_accessonly.isSelected() ? "true" : "false");

			prop.store(output, null);

			output.close();

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		initialize();
	}

	/**
	 * Called when the user clicks close.
	 */
	@FXML
	private void handleClose() {
		System.exit(0);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

	}
}
