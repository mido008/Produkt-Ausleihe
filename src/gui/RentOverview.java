package gui;

import java.sql.SQLException;

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
import product.Status;

public class RentOverview {

	MainContainer mainContainer;
	public ProductOverView productListView;
	
	Label title;
	TextField searchFilter;
	ChoiceBox<Category> categoryFilter;
	ChoiceBox<Status> statusFilter;
	public GridPane clientContainer ;
	public GridPane productsContainer ;
	public GridPane totalPreisContainer;
	Button selectClient;
	Button addProduct;
	HBox footer;
	Float totalPreis;
	Label totalPreisLabel = new Label();
	
	Label clientLabel = new Label();
	
	public TableView<Client> clientTableView = new TableView<Client>();
	public TableView<ProductDetails> productsTableView = new TableView<ProductDetails>();
	
	public ObservableList<Client> clientList = FXCollections.observableArrayList();
	private FilteredList<Client> filteredClientList = new FilteredList<>(this.clientList, client -> true);
	
	public ObservableList<ProductDetails> productList = FXCollections.observableArrayList();
	public FilteredList<ProductDetails> filteredProductList = new FilteredList<>(this.productList, productdetail -> true);
	
	
	public RentOverview(MainContainer mainContainer)
	{
		this.mainContainer = mainContainer;
		try {
			productListView = new ProductOverView();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.selectClient = new Button("Kunde auswählen");
		this.addProduct = new Button("Produkt hinzufügen");
		this.computeTotalPreis();
		this.initSelectButtonsAction();
		this.buildTitle();
		this.buildClientContainer();
		this.buildProductsContainer();
		this.buildTotalPreisContainer();
		this.buildFooter();
	}
	
	public void initSelectButtonsAction()
	{
		Helpers.selecClientForRent(this.selectClient, this);
		Helpers.selecProductForRent(this.addProduct, this);
	}
	
	public void buildTitle()
	{
		this.title = new Label("Produkt(e) ausleihen");
		this.title.getStyleClass().add("head-title");
	}
	
	public void buildClientContainer()
	{
		Label title = new Label("Kunde");
		title.getStyleClass().addAll("filter-label");

		Label separator = new Label(":");
		separator.getStyleClass().addAll("filter-label");
		
		this.clientContainer = new GridPane( );
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(25);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(5);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setPercentWidth(35);
		ColumnConstraints col4 = new ColumnConstraints();
		col4.setPercentWidth(35);
		
		clientContainer.getColumnConstraints().addAll(col1, col2, col3, col4);
		clientContainer.setHgap(5);
		clientContainer.setVgap(5);
		
		clientContainer.add(title, 0, 0, 1, 1);
		clientContainer.add(separator, 1, 0, 1, 1);
		if(this.clientList.isEmpty()) {
			clientContainer.add(this.selectClient, 2, 0, 2, 1);
		} else {
			this.buildClientTableView();
			clientContainer.add(this.clientLabel, 2, 0, 1, 1);
			clientContainer.add(this.selectClient, 3, 0, 1, 1);
		}
	}
	
	public void buildProductsContainer()
	{
		this.buildProductTableView();
		Label title = new Label("Produkte");
		title.getStyleClass().addAll("filter-label");

		Label separator = new Label(":");
		separator.getStyleClass().addAll("filter-label");
		
		this.productsContainer = new GridPane( );
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(25);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(5);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setPercentWidth(70);
		
		productsContainer.getColumnConstraints().addAll(col1, col2, col3);
		productsContainer.setHgap(5);
		productsContainer.setVgap(5);
		
		productsContainer.add(title, 0, 0, 1, 1);
		productsContainer.add(separator, 1, 0, 1, 1);
		productsContainer.add(this.productsTableView, 0, 1, 3, 1);
		productsContainer.add(this.addProduct, 2, 2, 1, 1);
			
	}
	
	public void buildClientLabel()
	{
		this.clientLabel.setText(this.clientList.get(0).getFirstname() + " " + this.clientList.get(0).getLastname());
		this.clientLabel.getStyleClass().addAll("size-18","text-white");
	}
	
	public void buildClientTableView()
	{
		TableColumn firstNameCol = new TableColumn("Vormane");
	    TableColumn lastnameCol = new TableColumn("Name");
	    
	    firstNameCol.setCellValueFactory(new PropertyValueFactory<Client, String>("firstname"));
		lastnameCol.setCellValueFactory(new PropertyValueFactory<Client, String>("lastname"));
		
	    this.clientTableView.setItems(this.filteredClientList);
		this.clientTableView.getColumns().addAll(firstNameCol, lastnameCol);
		this.clientTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		this.clientTableView.setMaxHeight(60);
		//this.clientTableView.getStyleClass().add("client-over-view");
		
	}
	
	
	public void buildProductTableView()
	{
		TableColumn productCol = new TableColumn("Produkt");
	    TableColumn preisCol = new TableColumn("€/Tag");
	    TableColumn daysCol = new TableColumn("Tage");
	    TableColumn dateFromCol = new TableColumn("Leihdatum");
	    TableColumn dateToCol = new TableColumn("Rückgabedatum");
	    
	    productCol.setCellValueFactory(new PropertyValueFactory<ProductDetails, String>("productname"));
	    preisCol.setCellValueFactory(new PropertyValueFactory<ProductDetails, Float>("preis"));
	    daysCol.setCellValueFactory(new PropertyValueFactory<ProductDetails, String>("periode"));
	    dateFromCol.setCellValueFactory(new PropertyValueFactory<ProductDetails, String>("datefrom"));
	    dateToCol.setCellValueFactory(new PropertyValueFactory<ProductDetails, String>("dateto"));
	    
	    this.productsTableView.setItems(this.filteredProductList);
		this.productsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		this.productsTableView.setMaxHeight(300);
		this.productsTableView.getColumns().addAll(productCol, preisCol, daysCol, dateFromCol, dateToCol);
		//this.productsTableView.getStyleClass().add("client-over-view");
		
	}
	
	public void buildTotalPreisContainer()
	{
		this.totalPreisContainer = new GridPane( );
		Label totalLabel = new Label("Total");
		totalLabel.getStyleClass().addAll("size-18","text-white");
		Label separator = new Label(":");
		separator.getStyleClass().addAll("size-18","text-white");
		Label prefix = new Label(" €");
		prefix.getStyleClass().addAll("size-18","text-white");
		this.totalPreisLabel.setText(this.totalPreis.toString());
		this.totalPreisLabel.getStyleClass().addAll("size-18","text-white");
		
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(80);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(5);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setPercentWidth(10);
		ColumnConstraints col4 = new ColumnConstraints();
		col4.setPercentWidth(5);
		
		this.totalPreisContainer.getColumnConstraints().addAll(col1, col2, col3);
		this.totalPreisContainer.setHgap(5);
		this.totalPreisContainer.setVgap(5);
		
		GridPane.setHalignment(totalLabel, HPos.RIGHT);
		this.totalPreisContainer.add(totalLabel, 0, 0, 1, 1);
		this.totalPreisContainer.add(separator, 1, 0, 1, 1);
		this.totalPreisContainer.add(this.totalPreisLabel, 2, 0, 1, 1);
		this.totalPreisContainer.add(prefix, 3, 0, 1, 1);

	}
	
	public void buildFooter()
	{
		Button invoiceBt = new Button("Quittung erstellen");
		invoiceBt.disableProperty().bind(Bindings.or(Bindings.size(this.productList).isEqualTo(0), Bindings.size(this.clientList).isEqualTo(0)));
		invoiceBt.getStyleClass().addAll("btn", "spacing-15");
		
		Helpers.createInvoice(invoiceBt, this);
		
		this.footer = new HBox(invoiceBt);
		this.footer.getStyleClass().addAll("table-view-footer", "align-right");
	}
	
	public void computeTotalPreis()
	{
		this.totalPreis = (float) 0;
		this.productList.forEach(item -> {
			this.totalPreis += item.calculateTotal();
		});
		this.buildTotalPreisContainer();
	}
	
	public void rebuildView()
	{
		this.mainContainer.setContent(this.getView());
	}
	
	public Group getView()
	{
		GridPane gPane = new GridPane();
		RowConstraints row1 = new RowConstraints();
		row1.setPercentHeight(15);
		RowConstraints row2 = new RowConstraints();
		RowConstraints row3 = new RowConstraints();
		RowConstraints row4 = new RowConstraints();
		RowConstraints row5 = new RowConstraints();
		row5.setPercentHeight(10);
		gPane.getRowConstraints().addAll(row1, row2, row3, row4, row5);
		gPane.setHgap(5);
		gPane.setVgap(5);
		
		GridPane.setHalignment(this.title, HPos.CENTER);
		gPane.add(this.title, 0, 0);
		gPane.add(this.clientContainer, 0, 1);
		gPane.add(this.productsContainer, 0, 2);
		GridPane.setHalignment(this.totalPreisContainer, HPos.RIGHT);
		gPane.add(this.totalPreisContainer, 0, 3);
		gPane.add(this.footer, 0, 4);

		return new Group(gPane);
	}
	
	
	
}
