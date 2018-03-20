package gui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import buttonHandlers.Helpers;
import client.Client;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.HPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Group;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import main.Leihaus;
import product.Category;
import product.Product;
import product.ProductDetails;
import product.Rent;
import product.Status;
import javafx.scene.layout.HBox;

public class ProductOverView {

	Label title;
	TextField searchFilter;
	ChoiceBox<Category> categoryFilter;
	ChoiceBox<Status> statusFilter;
	GridPane filterContainer;
	HBox footer;
	
	public DatePicker dateFrom = new DatePicker();
	public DatePicker dateTo = new DatePicker();
	
	
	public TableView<ProductDetails> tableView = new TableView<ProductDetails>();
	
	public ObservableList<ProductDetails> productList = FXCollections.observableArrayList();
	private FilteredList<ProductDetails> filteredProductList = new FilteredList<>(this.productList, productdetails -> true);
	
	public ProductOverView() throws SQLException, Exception {
		this.initProductList();
		this.buildTitle();
		this.buildFilter();
		this.buildTabView();
	}
	
	public ProductOverView(MainContainer mainContainer) throws SQLException, Exception {
		this.initOverview(mainContainer);
	}
	
	public void initOverview(MainContainer mainContainer) throws SQLException, Exception
	{
		this.initProductList();
		this.buildTitle();
		this.buildFilter();
		this.buildTabView();
		this.buildFooter(mainContainer);
	}
	
	public void initProductList() throws SQLException, Exception
	{
		productList.clear();
		productList.addAll(Leihaus.db.getProductOverView());
	}
	
	public void buildTitle()
	{
		this.title = new Label("Produkte");
		this.title.getStyleClass().add("head-title");
	}
	
	public void buildFilter()
	{
		this.searchFilter = new TextField();
		this.searchFilter.setPromptText("Search...");
		this.searchFilter.getStyleClass().addAll("filter");
		
		this.categoryFilter = new ChoiceBox<Category>();
		this.categoryFilter.getItems().add(new Category(-1, "Filter Ausblenden"));
		this.categoryFilter.getItems().addAll(Leihaus.categoriesList);
		this.categoryFilter.getSelectionModel().select(0);
		this.categoryFilter.getStyleClass().addAll("filter", "spacing-5");
		
		this.statusFilter = new ChoiceBox<Status>();
		this.statusFilter.getItems().addAll(Leihaus.statusItems);
		this.statusFilter.getSelectionModel().select(0);
		this.statusFilter.getStyleClass().addAll("filter", "spacing-5");
		
		
		this.searchFilter.textProperty().addListener((observable, oldVal, newVal) -> {
			this.filteredProductList.setPredicate(product -> {
				
				if(newVal == null || newVal.isEmpty()) {
					return true;
				}
				
				if(product.getProductname().toLowerCase().contains(newVal.toLowerCase())) {
					return true;
				}
				
				return false;
			});
		});
		
		this.categoryFilter.getSelectionModel().selectedItemProperty().addListener((observable, oldVal, newVal) -> {
			System.out.println(newVal.toString());
			this.filteredProductList.setPredicate(product -> {
				
				if(newVal.getId() == -1 || newVal.getId() == product.getCategory().getId()) {
					return true;
				}
				return false;
			});
		});
		
		this.statusFilter.getSelectionModel().selectedItemProperty().addListener((observable, oldVal, newVal) -> {
			this.filteredProductList.setPredicate(product -> {
				if(newVal.getId() == -1) {
					return true;
				}
				
				if(newVal.getLabel().toLowerCase().equals(product.getStatus().toLowerCase())) {
					return true;
				}
				
				return false;
			});
		});
		
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
		filterContainer.add(categoryFilter, 2, 0, 1, 1);
		
		filterContainer.add(statusFilterLabel, 0, 1, 1, 1);
		filterContainer.add(separatorLabel2, 1, 1, 1, 1);
		filterContainer.add(this.statusFilter, 2, 1, 1, 1);
		
		GridPane.setHalignment(this.searchFilter, HPos.RIGHT);
		filterContainer.add(this.searchFilter, 3, 2, 1, 1);
		
		this.filterContainer.getStyleClass().addAll("spacing-5");
	}
	
	public void buildTabView()
	{
		TableColumn productCol = new TableColumn("Produkt");
	    TableColumn categoryCol = new TableColumn("Produktgrouppe");
	    TableColumn preisCol = new TableColumn("€/Tag");
	    TableColumn statusCol = new TableColumn("Status");
	    TableColumn dateFromCol = new TableColumn("Leihdatum");
	    TableColumn dateToCol = new TableColumn("Rückgabedatum");
	    
	    productCol.setCellValueFactory(new PropertyValueFactory<ProductDetails, String>("productname"));
	    categoryCol.setCellValueFactory(new PropertyValueFactory<ProductDetails, String>("categorylabel"));
	    preisCol.setCellValueFactory(new PropertyValueFactory<ProductDetails, Float>("preis"));
	    statusCol.setCellValueFactory(new PropertyValueFactory<ProductDetails, String>("status"));
	    dateFromCol.setCellValueFactory(new PropertyValueFactory<ProductDetails, String>("datefrom"));
	    dateToCol.setCellValueFactory(new PropertyValueFactory<ProductDetails, String>("dateto"));
	    
	    this.tableView.setItems(this.filteredProductList);
	    this.tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		this.tableView.getColumns().addAll(productCol, categoryCol, preisCol, statusCol, dateFromCol, dateToCol);
		this.tableView.getStyleClass().add("client-over-view");
	}
	
	public void buildFooter(MainContainer mainContainer)
	{
		Button newProductBt = new Button("Neues Produkt");
		Button editProductBt = new Button("Bearbeiten");
		Button removeProductBt = new Button("Löschen");
		
		editProductBt.disableProperty().bind(Bindings.isEmpty(this.tableView.selectionModelProperty().get().getSelectedCells()));
		removeProductBt.disableProperty().bind(Bindings.isEmpty(this.tableView.selectionModelProperty().get().getSelectedCells()));
		
		newProductBt.getStyleClass().addAll("btn", "spacing-15");
		editProductBt.getStyleClass().addAll("btn", "spacing-15");
		removeProductBt.getStyleClass().addAll("btn", "spacing-15");
		
		Helpers.addProductt(newProductBt, mainContainer);
		Helpers.updateProductt(editProductBt, this, mainContainer);
		Helpers.removeProductt(removeProductBt, this);
		
		this.footer = new HBox(newProductBt, editProductBt, removeProductBt);
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
	
	public Group getProductViewForSelectTableView(FilteredList<ProductDetails> itemsToexlude)
	{
		this.filteredProductList.setPredicate(product -> {
			if(product.getStatus().toLowerCase().equals("verfügbar")){
				return true;
			}
			return false;
		});
		
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
		
		this.filterContainer.getChildren().removeIf(node -> this.filterContainer.getRowIndex(node) == 1);
		this.tableView.getColumns().removeIf(item -> {
			return item.getText().contains("Leihdatum") ||  item.getText().contains("Rückgabedatum") ;
		});
		
		GridPane.setHalignment(this.title, HPos.CENTER);
		gPane.add(this.title, 0, 0);
		gPane.add(this.filterContainer, 0, 1);
		gPane.add(this.tableView, 0, 2);

		return new Group(gPane);
	}
	
	public Group getProductSetDatumView()
	{
		Label title = new Label("Datum");
		Label fromLabel = new Label("Leihdatum");
		Label toLabel = new Label("Rückgabedatum");
		Label separator1 = new Label(":");
		Label separator2 = new Label(":");
		GridPane gPane = new GridPane();
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(30);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(5);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setPercentWidth(30);
		
		gPane.getColumnConstraints().addAll(col1, col2, col3);
		gPane.setHgap(5);
		gPane.setVgap(5);
		
		GridPane.setHalignment(title, HPos.CENTER);
		gPane.add(title, 0, 0, 3, 1);
		
		gPane.add(fromLabel, 0, 1, 1, 1);
		gPane.add(separator1, 1, 1, 1, 1);
		gPane.add(this.dateFrom, 2, 1, 1, 1);
		
		gPane.add(toLabel, 0, 2, 1, 1);
		gPane.add(separator2, 1, 2, 1, 1);
		gPane.add(this.dateTo, 2, 2, 1, 1);
		return new Group(gPane);
	}
	
}
