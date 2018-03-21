package gui;

import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.Group;
import javafx.scene.layout.VBox;
import main.Leihaus;
import javafx.scene.layout.HBox;

import java.sql.SQLException;

import client.Client;

public class UpdateClientView {
	Label title;
	VBox form;
	HBox footer;
	Client client;
	TextField firstname;
	TextField lastname;
	TextField address;
	TextField plz;
	TextField city;
	TextField tel;
	
	public UpdateClientView(MainContainer mainContainer) {
		this.client = new Client();
		this.initView(mainContainer);
	}
	
	public UpdateClientView(Client client ,MainContainer mainContainer) {
		this.client = client;
		this.initView(mainContainer);
	}
	
	public void initView(MainContainer mainContainer)
	{
		this.buildTitle();
		this.buildForm();
		this.buildFooter(mainContainer);
	}
	
	public void buildTitle()
	{
		this.title = new Label("Neuer Kunde");
		this.title.getStyleClass().add("head-title");
	}
	
	public void buildForm()
	{
		this.firstname = new TextField();
		this.firstname.setPromptText("Vorname");
		this.firstname.setText(this.client.getFirstname());
		this.firstname.getStyleClass().addAll("input", "spacing-5");
		
		this.lastname = new TextField();
		this.lastname.setPromptText("Name");
		this.lastname.setText(this.client.getLastname());
		this.lastname.getStyleClass().addAll("input", "spacing-5");
		
		this.address = new TextField();
		this.address.setPromptText("Anschrift");
		this.address.setText(this.client.getAddress());
		this.address.getStyleClass().addAll("input", "spacing-5");
		
		this.plz = new TextField();
		this.plz.setPromptText("plz");
		this.plz.setText(this.client.getPlz());
		this.plz.getStyleClass().addAll("input", "spacing-5");

		this.city = new TextField();
		this.city.setPromptText("Ort");
		this.city.setText(this.client.getCity());
		this.city.getStyleClass().addAll("input", "spacing-5");

		this.tel = new TextField();
		this.tel.setPromptText("Tel");
		this.tel.setText(this.client.getTel());
		this.tel.getStyleClass().addAll("input", "spacing-5");
		
		this.form = new VBox(this.firstname, this.lastname, this.address, this.plz, this.city, this.tel);
		this.form.getStyleClass().addAll("input-form");
	}
	
	public void buildFooter(MainContainer mainContainer)
	{
		Button save = new Button("Speichern");
		Button cancel = new Button("Abbrechen");
		
		save.getStyleClass().addAll("btn", "spacing-15");
		cancel.getStyleClass().addAll("btn", "spacing-15");
		
		save.setOnAction(action -> {
			if(this.client.getId() == 0) {
				try {
					Leihaus.db.addClient(this.saveClient());
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					Leihaus.db.updateClient(this.saveClient());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			try {
				mainContainer.setContent(new ClientOverView(mainContainer).getView());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		cancel.setOnAction(action -> {
			try {
				mainContainer.setContent(new ClientOverView(mainContainer).getView());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		this.footer = new HBox(save, cancel);
		this.footer.getStyleClass().add("table-view-footer");
	}
	
	public Client saveClient()
	{
		this.client.setFirstname(this.firstname.getText());
		this.client.setLastname(this.lastname.getText());
		this.client.setAddress(this.address.getText());
		this.client.setPlz(this.plz.getText());
		this.client.setCity(this.city.getText());
		this.client.setTel(this.tel.getText());
		
		return this.client;
	}
	
	public Group getView()
	{
		VBox vbox = new VBox(this.title, this.form, this.footer);
		vbox.getStyleClass().add("head-title-container");
		return new Group(vbox);
	}
}
