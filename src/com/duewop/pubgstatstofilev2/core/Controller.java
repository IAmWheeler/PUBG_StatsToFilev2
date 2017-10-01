package com.duewop.pubgstatstofilev2.core;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class Controller extends Application {

	@FXML
	private TextField apikey;
	@FXML
	private TextField username;
	@FXML
	private TextField previous_deaths;
	@FXML
	private TextField previous_kills;
	@FXML
	private TextField previous_wins;
	@FXML
	private Label lbl_output;
	@FXML
	private TextArea txtWorkLog;







	@FXML
	private CheckBox chk_wins;
	@FXML
	private TextField txt_wins;

	private boolean var_wins;
	private String 	var_txt_wins;














	private String strApiKey = null;
	private String strUserName = null;
	private String strPreviousDeaths = null;
	private String strPreviousKills = null;
	private String strPreviousWins = null;
	private String strOutputDir = null;
	private String data;


	private String workLog = null;
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

				strApiKey = prop.getProperty("apikey");
				strUserName = prop.getProperty("username");
				strPreviousDeaths = prop.getProperty("previous_deaths");
				strPreviousKills = prop.getProperty("previous_kills");
				strPreviousWins = prop.getProperty("previous_wins");
				strOutputDir = prop.getProperty("output_dir");

				apikey.setText(strApiKey);
				username.setText(strUserName);
				previous_deaths.setText(strPreviousDeaths);
				previous_kills.setText(strPreviousKills);
				previous_wins.setText(strPreviousWins);

				var_wins = Boolean.parseBoolean(prop.getProperty("chk_wins"));
				var_txt_wins = prop.getProperty("txt_wins");
				chk_wins.setSelected(var_wins);
				txt_wins.setText(var_txt_wins);












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
	 * Called when the user clicks directory button.
	 */
	private void runProcesses() {

		Timer time = new Timer();
		time.schedule( new TimerTask() {
			public void run() {
				Date thisDate = new Date();

				PUBGStats st = new PUBGStats(strApiKey,
						strUserName,
						Integer.parseInt(strPreviousWins),
						Integer.parseInt(strPreviousKills),
						Integer.parseInt(strPreviousDeaths),
						strOutputDir,
						var_wins,
						var_txt_wins);
				try {
					data = st.RunProcess();
				} catch (IOException e) {
					e.printStackTrace();
				}

				st.ProcessData(data);

				txtWorkLog.setText(thisDate + "\r\n" + st.getWorkLog());
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

		if (strApiKey == null) { workLog = "Please enter the API Key.\n"; }
		if (strUserName == null) { workLog = workLog + "Please enter the PUBG Username.\n"; }
		if (strPreviousWins == null) { workLog = workLog + "Please enter the previous wins (or zero).\n"; }
		if (strPreviousKills == null) { workLog = workLog + "Please enter the previous kills (or zero).\n"; }
		if (strPreviousDeaths == null) { workLog = workLog + "Please enter the previous deaths (or zero).\n"; }
		if (strOutputDir == null) { workLog = workLog + "Please select a valid output directory.\n"; }

		if (strApiKey == null || strUserName == null || strPreviousDeaths == null || strPreviousKills == null || strPreviousWins == null || strOutputDir == null) {

			String errors = "This form contains errors. Please do the following and Save to retry connection.\n";

			txtWorkLog.setText(errors + workLog);

			return false;
		} else {
			return true;
		}
	}

	/**
	 * Called when the user clicks ok.
	 */
	@FXML
	private void handleSave() {
		Properties prop = new Properties();
		OutputStream output = null;

		try {
			output = new FileOutputStream("config.properties");

			prop.setProperty("apikey", apikey.getText());
			prop.setProperty("username", username.getText());
			prop.setProperty("previous_deaths", previous_deaths.getText());
			prop.setProperty("previous_kills", previous_kills.getText());
			prop.setProperty("previous_wins", previous_wins.getText());
			if(strOutputDir != null) {
				prop.setProperty("output_dir", strOutputDir);
			}



			prop.setProperty("chk_wins", chk_wins.isSelected() ? "true":"false");












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
