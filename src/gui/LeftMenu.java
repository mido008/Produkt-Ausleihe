package gui;

import java.sql.SQLException;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;


public class LeftMenu {
	
	final Button clientBt;
	final Button productBt;
	final Button rentBt;
	final Button backBt;

	public LeftMenu(MainContainer relatedContainer){
		this.clientBt = new Button("Kunden");
		this.productBt = new Button("Produkt");
		this.rentBt = new Button("Ausleihe");
		this.backBt = new Button("Rückgabe");
		
		this.initButtonHandles(relatedContainer);
		this.initMenu();
	};
	
	public void initMenu()
	{
		clientBt.getStyleClass().addAll("btn", "spacing-15");
		productBt.getStyleClass().addAll("btn", "spacing-15");
		rentBt.getStyleClass().addAll("btn", "spacing-15");
		backBt.getStyleClass().addAll("btn", "spacing-15");
	}
	
	public void initButtonHandles(MainContainer relatedContainer)
	{
		this.initClientBtHandle(relatedContainer);
		this.initProduktBtHandle(relatedContainer);
		this.initRentBtHandle(relatedContainer);
		this.initBackBtHandle(relatedContainer);
	}
	
	public void initClientBtHandle(MainContainer relatedContainer)
	{
		this.clientBt.setOnAction(action -> {
			ClientOverView clientOverView;
			try {
				clientOverView = new ClientOverView(relatedContainer);
				relatedContainer.setContent(clientOverView.getView());
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	public void initProduktBtHandle(MainContainer relatedContainer)
	{
		this.productBt.setOnAction(action -> {
			ProductOverView productOverview;
			try {
				productOverview = new ProductOverView(relatedContainer);
				relatedContainer.setContent(productOverview.getView());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
	}
	
	public void initRentBtHandle(MainContainer relatedContainer)
	{
		this.rentBt.setOnAction(action -> {
			RentOverview rentOverview;
			try {
				rentOverview = new RentOverview(relatedContainer);
				relatedContainer.setContent(rentOverview.getView());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
	}
	
	public void initBackBtHandle(MainContainer relatedContainer)
	{
		
	}
	
	public VBox getLeftMenu() {
		VBox vbox = new VBox(this.clientBt, this.productBt, this.rentBt, this.backBt);
		vbox.getStyleClass().add("left-menu");
		return vbox;
	}
	
}
