package gui;

import buttonHandlers.FilterHelpers;
import buttonHandlers.Helpers;
import client.Client;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.HPos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import main.Leihaus;
import product.Category;
import product.ProductDetails;
import product.Rent;
import product.Status;

public class OverView {

	MainContainer mainContainer;
	Label title;
	TextField searchClientFilter;
	TextField searchProductFilter;
	
	ChoiceBox<Category> categoryFilter;
	ChoiceBox<Status> statusFilter;
	public GridPane filterContainer ;
	public GridPane rentContainer ;
	Button showDetails;
	HBox footer;

	public TableView<Rent> rentTableView = new TableView<Rent>();
	public ObservableList<String> multiFilter = FXCollections.observableArrayList();
	public ObservableList<Rent> rentList = FXCollections.observableArrayList();
	private FilteredList<Rent> filteredRentList = new FilteredList<>(this.rentList, rent -> true);
	
	public OverView(MainContainer mainContainer)
	{
		try {
			this.rentList.addAll(Leihaus.db.getRentList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.mainContainer = mainContainer;
		this.buildTitle();
		this.buildFilter();
		this.buildTabView();
		this.buildFooter();
	}
	
	public void buildTitle()
	{
		this.title = new Label("Übersicht");
		this.title.getStyleClass().add("head-title");
	}
	
	public void buildFilter()
	{
		this.searchClientFilter = new TextField();
		this.searchClientFilter.setPromptText("Kunde suchen ...");
		this.searchClientFilter.getStyleClass().addAll("filter");
		
		this.searchProductFilter = new TextField();
		this.searchProductFilter.setPromptText("Produkt suchen ...");
		this.searchProductFilter.getStyleClass().addAll("filter");
		
		this.categoryFilter = new ChoiceBox<Category>();
		this.categoryFilter.getItems().add(new Category(-1, "Filter Ausblenden"));
		this.categoryFilter.getItems().addAll(Leihaus.categoriesList);
		this.categoryFilter.getSelectionModel().select(0);
		this.categoryFilter.getStyleClass().addAll("filter", "spacing-5");
		
		this.statusFilter = new ChoiceBox<Status>();
		this.statusFilter.getItems().addAll(Leihaus.statusItems);
		this.statusFilter.getSelectionModel().select(0);
		this.statusFilter.getStyleClass().addAll("filter", "spacing-5");
		
		this.buildFilterHandler();
		this.buildFiterView();
	}
	
	public void buildFilterHandler() {
		this.searchClientFilter.textProperty().addListener((observable, oldVal, newVal) -> {
			this.filteredRentList.setPredicate(rent -> {
				
				if(newVal == null || newVal.isEmpty()) {
					return true;
				}
				
				if(rent.getClientname().toLowerCase().contains(newVal.toLowerCase())) {
					return true;
				}
				
				return false;
			});
		});
		
		this.searchProductFilter.textProperty().addListener((observable, oldVal, newVal) -> {
			this.multiFilter.remove(oldVal);
			this.multiFilter.add(newVal);
			//this.filteredRentList.setPredicate(rent -> {
				//return FilterHelpers.productNameFilter(rent.getProductname(), newVal);
			//});
		});
		
		this.searchClientFilter.textProperty().addListener((observable, oldVal, newVal) -> {
			this.multiFilter.remove(oldVal);
			this.multiFilter.add(newVal);
		});
		
		//this.multiFilter
		
	}
	
	public void buildFiterView()
	{
		this.filterContainer = new GridPane();
		
		Label separatorLabel1= new Label(" : ");
		separatorLabel1.getStyleClass().addAll("filter-label");
		
		Label separatorLabel2= new Label(" : ");
		separatorLabel2.getStyleClass().addAll("filter-label");
		
		
		Label categoryFilterLabel= new Label("Produktkategorie");
		categoryFilterLabel.getStyleClass().addAll("filter-label");
		
		Label statusFilterLabel = new Label("Verfübarkeit");
		statusFilterLabel.getStyleClass().addAll("filter-label");

		this.filterContainer = new GridPane( );
		
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(25);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(5);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setPercentWidth(30);
		ColumnConstraints col4 = new ColumnConstraints();
		col4.setPercentWidth(40);
		
		filterContainer.getColumnConstraints().addAll(col1, col2, col3, col4);
		filterContainer.setHgap(5);
		filterContainer.setVgap(5);
		
		filterContainer.add(categoryFilterLabel, 0, 0, 1, 1);
		filterContainer.add(separatorLabel1, 1, 0, 1, 1);
		filterContainer.add(this.categoryFilter, 2, 0, 1, 1);
		
		filterContainer.add(statusFilterLabel, 0, 1, 1, 1);
		filterContainer.add(separatorLabel2, 1, 1, 1, 1);
		filterContainer.add(this.statusFilter, 2, 1, 1, 1);

		GridPane.setHalignment(this.searchClientFilter, HPos.RIGHT);
		filterContainer.add(this.searchClientFilter, 3, 2, 1, 1);

		GridPane.setHalignment(this.searchProductFilter, HPos.RIGHT);
		filterContainer.add(this.searchProductFilter, 3, 3, 1, 1);
	}
	
	public void buildTabView()
	{
		TableColumn clientCol = new TableColumn("Kunde");
		TableColumn productCol = new TableColumn("Produkt");
	    TableColumn preisCol = new TableColumn("€/Tag");
	    TableColumn daysCol = new TableColumn("Tage");
	    TableColumn dateFromCol = new TableColumn("Leihdatum");
	    TableColumn dateToCol = new TableColumn("Rückgabedatum");
	    
	    clientCol.setCellValueFactory(new PropertyValueFactory<Rent, String>("clientname"));
	    productCol.setCellValueFactory(new PropertyValueFactory<Rent, String>("productname"));
	    preisCol.setCellValueFactory(new PropertyValueFactory<Rent, Float>("preis"));
	    daysCol.setCellValueFactory(new PropertyValueFactory<Rent, String>("periode"));
	    dateFromCol.setCellValueFactory(new PropertyValueFactory<Rent, String>("datefrom"));
	    dateToCol.setCellValueFactory(new PropertyValueFactory<Rent, String>("dateto"));
	    
	    this.rentTableView.setItems(this.filteredRentList);
		this.rentTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		this.rentTableView.setMaxHeight(250);
		this.rentTableView.getColumns().addAll(clientCol,productCol, preisCol, daysCol, dateFromCol, dateToCol);
	}
	
	public void buildFooter()
	{
		this.showDetails = new Button("Details");
		this.showDetails.getStyleClass().addAll("btn", "spacing-15");
		this.showDetails.disableProperty().bind(Bindings.isEmpty(this.rentTableView.selectionModelProperty().get().getSelectedCells()));
		//Helpers.createInvoice(this.showDetails, this);
		
		this.footer = new HBox(this.showDetails);
		this.footer.getStyleClass().addAll("table-view-footer", "align-right");
	}
	
	public Group getView()
	{
		GridPane gPane = new GridPane();
		RowConstraints row1 = new RowConstraints();
		row1.setPercentHeight(5);
		RowConstraints row2 = new RowConstraints();
		RowConstraints row3 = new RowConstraints();
		RowConstraints row4 = new RowConstraints();
		RowConstraints row5 = new RowConstraints();
		row5.setPercentHeight(5);
		gPane.getRowConstraints().addAll(row1, row2, row3, row4, row5);
		gPane.setHgap(5);
		gPane.setVgap(5);
		
		GridPane.setHalignment(this.title, HPos.CENTER);
		gPane.add(this.title, 0, 0);
		gPane.add(this.filterContainer, 0, 1);
		gPane.add(this.rentTableView, 0, 2);
		gPane.add(this.footer, 0, 4);

		return new Group(gPane);
	}
	
	
}
