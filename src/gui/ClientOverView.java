package gui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import buttonHandlers.Helpers;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.Group;
import javafx.scene.layout.VBox;
import main.Leihaus;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import client.Client;
import db.DB;

public class ClientOverView {
	
	Label title;
	TextField filter;
	HBox filterContainer;
	HBox footer;
	public TableView<Client> tableView = new TableView<Client>();
	
	
	public ObservableList<Client> clientsList = FXCollections.observableArrayList();
	private FilteredList<Client> filteredClientList = new FilteredList<>(this.clientsList, cl -> true);
	
	public ClientOverView() throws SQLException, Exception
	{
		this.initClientList();
		this.buildTitle();
		this.buildFiter();
		this.buildTabview();
	}
	
	public ClientOverView(MainContainer mainContainer) throws SQLException, Exception
	{
		this.initClientView(mainContainer);
	}
	
	
	public void initClientView(MainContainer mainContainer) throws SQLException, Exception
	{
		this.initClientList();
		this.buildTitle();
		this.buildFiter();
		this.buildTabview();
		this.buildFooter(mainContainer);
	}

	public void initClientList() throws SQLException, Exception
	{
		clientsList.clear();
		clientsList.addAll(Leihaus.db.getClients());
	}
	
	public void buildTitle()
	{
		this.title = new Label("Kunden");
		this.title.getStyleClass().add("head-title");
	}
	
	public void buildFiter()
	{
		this.filter = new TextField();
		this.filter.setPromptText("Search...");
		this.filter.getStyleClass().addAll("filter");
		
		this.filter.textProperty().addListener((observable, oldVal, newVal) -> {
			this.filteredClientList.setPredicate(client -> {
				
				if(newVal == null || newVal.isEmpty()) {
					return true;
				}
				
				if(client.getFirstname().toLowerCase().contains(newVal.toLowerCase()) || 
					client.getLastname().toLowerCase().contains(newVal.toLowerCase())) {
					return true;
				}
				
				return false;
			});
		});
		
		this.filterContainer = new HBox(this.filter);
		this.filterContainer.getStyleClass().addAll("filter-container", "spacing-15");
	}
	
	public void buildTabview()
	{
		TableColumn firstNameCol = new TableColumn("Vormane");
	    TableColumn lastnameCol = new TableColumn("Name");
	    TableColumn addressCol = new TableColumn("Anschrift");
	    TableColumn plzCol = new TableColumn("PLZ");
	    TableColumn cityCol = new TableColumn("Ort");
	    TableColumn telCol = new TableColumn("Tel");
	    
		firstNameCol.setCellValueFactory(new PropertyValueFactory<Client, String>("firstname"));
		lastnameCol.setCellValueFactory(new PropertyValueFactory<Client, String>("lastname"));
		addressCol.setCellValueFactory(new PropertyValueFactory<Client, String>("address"));
		plzCol.setCellValueFactory(new PropertyValueFactory<Client, String>("plz"));
		cityCol.setCellValueFactory(new PropertyValueFactory<Client, String>("city"));
		telCol.setCellValueFactory(new PropertyValueFactory<Client, String>("tel"));
		
		this.tableView.setItems(this.filteredClientList);
		this.tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		this.tableView.getColumns().addAll(firstNameCol, lastnameCol, addressCol, plzCol, cityCol, telCol);
		this.tableView.getStyleClass().add("client-over-view");
	}
	
	public void buildFooter(MainContainer mainContainer)
	{
		Button newClientBt = new Button("Neuer Kunde");
		Button editClientBt = new Button("Bearbeiten");
		Button removeClientBt = new Button("Löschen");
		
		editClientBt.disableProperty().bind(Bindings.isEmpty(this.tableView.selectionModelProperty().get().getSelectedCells()));
		removeClientBt.disableProperty().bind(Bindings.isEmpty(this.tableView.selectionModelProperty().get().getSelectedCells()));
		
		
		newClientBt.getStyleClass().addAll("btn", "spacing-15");
		editClientBt.getStyleClass().addAll("btn", "spacing-15");
		removeClientBt.getStyleClass().addAll("btn", "spacing-15");
		
		Helpers.addClient(newClientBt, mainContainer);
		Helpers.updateClient(editClientBt, this, mainContainer);
		Helpers.removeClient(removeClientBt, this);
		
		this.footer = new HBox(newClientBt, editClientBt, removeClientBt);
		this.footer.getStyleClass().addAll("table-view-footer", "align-center");
	}
	
	public Group getView()
	{
		GridPane gPane = new GridPane();
		RowConstraints row1 = new RowConstraints();
		row1.setPercentHeight(10);
		RowConstraints row2 = new RowConstraints();
		RowConstraints row3 = new RowConstraints();
		RowConstraints row4 = new RowConstraints();
		row4.setPercentHeight(10);
		gPane.getRowConstraints().addAll(row1, row2, row3, row4);
		gPane.setHgap(5);
		gPane.setVgap(5);
		
		GridPane.setHalignment(this.title, HPos.CENTER);
		gPane.add(this.title, 0, 0);
		gPane.add(this.filterContainer, 0, 1);
		gPane.add(this.tableView, 0, 2);
		gPane.add(this.footer, 0, 3);
		
		return new Group(gPane);
	}
	
	public Group getClientViewForSelectTableView()
	{
		GridPane gPane = new GridPane();
		RowConstraints row1 = new RowConstraints();
		row1.setPercentHeight(10);
		RowConstraints row2 = new RowConstraints();
		RowConstraints row3 = new RowConstraints();
		gPane.getRowConstraints().addAll(row1, row2, row3);
		gPane.setHgap(5);
		gPane.setVgap(5);
		
		GridPane.setHalignment(this.title, HPos.CENTER);
		gPane.add(this.title, 0, 0);
		gPane.add(this.filterContainer, 0, 1);
		gPane.add(this.tableView, 0, 2);
		
		return new Group(gPane);
	}
}
