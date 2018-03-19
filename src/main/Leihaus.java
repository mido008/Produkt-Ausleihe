package main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import client.Client;
import db.DB;
import gui.MainGui;
import javafx.application.Application;
import javafx.stage.Stage;
import product.Category;
import product.Status;

public class Leihaus extends Application{

	@Override public void start(Stage stage) throws SQLException, Exception {
		MainGui mainGui = new MainGui();
		mainGui.initMainGui(stage);
	}
	
	public static DB db = new DB();
	public static ArrayList<Category> categoriesList;
	public static ArrayList<Status> statusItems = new ArrayList<Status>();

	public static void main(String[] args) throws SQLException, Exception {
		db.initDB();
		statusItems.add(new Status(-1, "Filter Ausblenden"));
		statusItems.add(new Status(0, "Verfügbar"));
		statusItems.add(new Status(1, "ausgeliehen"));
		categoriesList = Leihaus.db.getCategories();
		
		launch(args);
	}

}
