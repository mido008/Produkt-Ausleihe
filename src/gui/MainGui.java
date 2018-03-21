package gui;

import java.sql.SQLException;

import db.DB;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainGui{
	
	public void initMainGui(Stage stage) throws SQLException, Exception {

		final MainContainer mainContainer = new MainContainer();
		final LeftMenu leftMenu = new LeftMenu(mainContainer);
		OverView overView = new OverView(mainContainer);
		ClientOverView clientOverView = new ClientOverView(mainContainer);
		ProductOverView productOverview= new ProductOverView(mainContainer);
		
		mainContainer.setContent(overView.getView());
		
		final HBox parentContainer = new HBox(leftMenu.getLeftMenu(), mainContainer.getMainContainer());
		
		parentContainer.getStyleClass().add("parent-container");
		
		Scene scene = new Scene(parentContainer, 900, 600);
		scene.getStylesheets().add("/assets/styles/style.css");
		
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}
}
