package gui;

import java.sql.SQLException;
import java.util.ArrayList;

import client.Client;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.Leihaus;
import product.Category;
import product.ProductDetails;

public class UpdateProductView {

	Label title;
	VBox form;
	HBox footer;
	ProductDetails product;
	
	TextField productLabel;
	TextField preis;
	ChoiceBox<Category> category;
	
	public UpdateProductView(MainContainer mainContainer){
		this.product = new ProductDetails();
		this.initView(mainContainer);
	}
	
	public UpdateProductView(ProductDetails product ,MainContainer mainContainer) {
		this.product = product;
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
		this.title = new Label("Neues Produkt");
		this.title.getStyleClass().add("head-title");
	}
	
	public void buildForm()
	{
		this.productLabel = new TextField();
		this.productLabel.setPromptText("Vorname");
		this.productLabel.setText(this.product.getProductname());
		this.productLabel.getStyleClass().addAll("input", "spacing-5");
		
		this.preis = new TextField();
		this.preis.setPromptText("Name");
		this.preis.setText(this.product.getPreis().toString());
		this.preis.getStyleClass().addAll("input", "spacing-5");
		
		this.category = new ChoiceBox<Category>();
		this.category.getItems().addAll(Leihaus.categoriesList);
		this.category.getSelectionModel().select(0);
		this.category.getStyleClass().addAll("select-option", "spacing-5");
		
		this.form = new VBox(this.productLabel, this.preis, this.category);
		this.form.getStyleClass().addAll("input-form");
	}
	
	public void buildFooter(MainContainer mainContainer)
	{
		Button save = new Button("Speichern");
		Button cancel = new Button("Abbrechen");
		
		save.getStyleClass().addAll("btn", "spacing-15");
		cancel.getStyleClass().addAll("btn", "spacing-15");
		
		save.setOnAction(action -> {
			try {
				if(this.product.getProductId() == 0) {
					Leihaus.db.addProduct(this.saveProduct());
				} else {
					Leihaus.db.updateProduct(this.saveProduct());
				}
				mainContainer.setContent(new ProductOverView(mainContainer).getView());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		cancel.setOnAction(action -> {
			try {
				mainContainer.setContent(new ProductOverView(mainContainer).getView());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		this.footer = new HBox(save, cancel);
		this.footer.getStyleClass().add("table-view-footer");
	}	
	
	public ProductDetails saveProduct()
	{
		this.product.setProductname(this.productLabel.getText());
		this.product.setPreis(Float.parseFloat(this.preis.getText()));
		this.product.setCategory(this.category.getSelectionModel().getSelectedItem());
		return this.product;
	}
	
	public Group getView()
	{
		VBox vbox = new VBox(this.title, this.form, this.footer);
		vbox.getStyleClass().add("head-title-container");
		return new Group(vbox);
	}
	
	
}
