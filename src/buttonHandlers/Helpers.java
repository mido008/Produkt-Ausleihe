package buttonHandlers;

import java.util.Optional;

import client.Client;
import gui.ClientOverView;
import gui.InvoiceView;
import gui.MainContainer;
import gui.ProductOverView;
import gui.RentOverview;
import gui.ReturnOverView;
import gui.UpdateClientView;
import gui.UpdateProductView;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.StageStyle;
import main.Leihaus;
import product.ProductDetails;

public class Helpers {

	private static  Alert warningAlert = new Alert(AlertType.WARNING);
	private static Alert confirmtionAlert;
	
	public static void addClient(Button bt, MainContainer mainContainer)
	{
		bt.setOnAction(action -> {
			mainContainer.setContent(new UpdateClientView(mainContainer).getView());
		});
	}
	
	public static void updateClient(Button bt, ClientOverView clView, MainContainer mainContainer)
	{
		bt.setOnAction(action -> {
			Client client = (Client) clView.tableView.getSelectionModel().getSelectedItem();
			
			if(client == null) {
				warningAlert = new Alert(AlertType.WARNING);
				warningAlert.setHeaderText("Wählen Sie bitte einen Kunden aus!");
				Optional<ButtonType> option = warningAlert.showAndWait();
			} else {
				mainContainer.setContent(new UpdateClientView(client, mainContainer).getView());
			}
		});
	}
	
	public static void removeClient(Button bt, ClientOverView clView)
	{
		bt.setOnAction(action -> {
			Client client = (Client) clView.tableView.getSelectionModel().getSelectedItem();
			
			if(client == null) {
				warningAlert = new Alert(AlertType.WARNING);
				warningAlert.setHeaderText("Wählen Sie bitte einen Kunden aus!");
				Optional<ButtonType> option = warningAlert.showAndWait();
				
			} else {
				String fullname = client.getFirstname() + " " + client.getLastname();
				confirmtionAlert = new Alert(AlertType.CONFIRMATION);
				confirmtionAlert.setTitle("Kunde löschen");
				confirmtionAlert.setHeaderText("Möchten Sie den Kunden " + fullname + " wirklich löschen ?");
				Optional<ButtonType> option = confirmtionAlert.showAndWait();
				
				if(option.get() == ButtonType.OK) {
					try {
						Leihaus.db.removeClient(client);
						clView.initClientList();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	public static void addProductt(Button bt, MainContainer mainContainer)
	{
		bt.setOnAction(action -> {
			mainContainer.setContent(new UpdateProductView(mainContainer).getView());
		});
	}
	
	public static void updateProductt(Button bt, ProductOverView productView, MainContainer mainContainer)
	{
		bt.setOnAction(action -> {
			ProductDetails product = (ProductDetails) productView.tableView.getSelectionModel().getSelectedItem();
			if(product == null) {
				warningAlert = new Alert(AlertType.WARNING);
				warningAlert.setHeaderText("Wählen Sie bitte ein Produkt aus!");
				Optional<ButtonType> option = warningAlert.showAndWait();
			} else {
				mainContainer.setContent(new UpdateProductView(product, mainContainer).getView());
			}
		});
	}
	
	
	public static void removeProductt(Button bt, ProductOverView productView)
	{
		bt.setOnAction(action -> {
			ProductDetails product = (ProductDetails) productView.tableView.getSelectionModel().getSelectedItem();
			
			if(product == null) {
				warningAlert = new Alert(AlertType.WARNING);
				warningAlert.setHeaderText("Wählen Sie bitte ein Produkt aus!");
				Optional<ButtonType> option = warningAlert.showAndWait();
			} else {
				String productname = product.getProductname();
				confirmtionAlert = new Alert(AlertType.CONFIRMATION);
				confirmtionAlert.setTitle("Kunde löschen");
				confirmtionAlert.setHeaderText("Möchten Sie das Produkt " + productname + " wirklich löschen ?");
				Optional<ButtonType> option = confirmtionAlert.showAndWait();
				
				if(option.get() == ButtonType.OK) {
					try {
						Leihaus.db.removeProduct(product);
						productView.productList.clear();
						productView.productList.addAll(Leihaus.db.getProductOverView());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	public static void selectClientForRent(Button bt, RentOverview rentOverview)
	{
		bt.setOnAction(action-> {
			try {
				ClientOverView clientList = new ClientOverView();
				confirmtionAlert = new Alert(AlertType.CONFIRMATION);
				confirmtionAlert.setHeaderText("");
				confirmtionAlert.getDialogPane().setContent(clientList.getClientViewForSelectTableView());
				
				Optional<ButtonType> option = confirmtionAlert.showAndWait();
				
				if(option.get() == ButtonType.OK) {
					try {
						Client selectedClient = clientList.tableView.getSelectionModel().getSelectedItem();
						rentOverview.clientList.clear();
						rentOverview.clientList.add(selectedClient);
						rentOverview.buildClientLabel();
						rentOverview.buildClientContainer();
						rentOverview.rebuildView();
						System.out.println(selectedClient.getFirstname());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		});
	}
	
	public static void selecProductForRent(Button bt, RentOverview rentOverview)
	{
		bt.setOnAction(action-> {
			try {
				confirmtionAlert = new Alert(AlertType.CONFIRMATION);
				confirmtionAlert.setHeaderText("");
				confirmtionAlert.getDialogPane().setContent(rentOverview.productListView.getProductViewForSelectTableView(rentOverview.filteredProductList));
				
				Optional<ButtonType> option = confirmtionAlert.showAndWait();
				
				if(option.get() == ButtonType.OK) {
						ProductDetails selectedProduct = rentOverview.productListView.tableView.getSelectionModel().getSelectedItem();
						if(selectedProduct != null) {
							rentOverview.productListView.productList.remove(selectedProduct);
							confirmtionAlert = new Alert(AlertType.CONFIRMATION);
							confirmtionAlert.setHeaderText("");
							confirmtionAlert.getDialogPane().setContent(rentOverview.productListView.getProductSetDatumView());
							Optional<ButtonType> option2 = confirmtionAlert.showAndWait();
							
							if(option2.get() == ButtonType.OK) {
								selectedProduct.setDatefrom(rentOverview.productListView.dateFrom.getValue().toString());
								selectedProduct.setDateto(rentOverview.productListView.dateTo.getValue().toString());
								selectedProduct.computePeriode();
								rentOverview.productList.add(selectedProduct);
								rentOverview.computeTotalPreis();
								rentOverview.rebuildView();
							}
							//rentOverview.buildProductsContainer();
						}
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		});
	}
	
	public static void createInvoice(Button bt, RentOverview rentOverview)
	{
		bt.setOnAction(action-> {
			try {
				if(rentOverview.saveBt.isVisible()) {
					Leihaus.db.saveRent(rentOverview.clientList.get(0), rentOverview.productList);
				}
				rentOverview.renderInvoiceView();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	public static void saveRent(Button bt, RentOverview rentOverview)
	{
		bt.setOnAction(action-> {
			try {
				Leihaus.db.saveRent(rentOverview.clientList.get(0), rentOverview.productList);
				rentOverview.saveBt.visibleProperty().set(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	
	public static void selectClientForReturn(Button bt, ReturnOverView returnOverView)
	{
		bt.setOnAction(action-> {
			try {
				ClientOverView clientList = new ClientOverView();
				confirmtionAlert = new Alert(AlertType.CONFIRMATION);
				confirmtionAlert.setHeaderText("");
				confirmtionAlert.getDialogPane().setContent(clientList.getClientViewForSelectTableView());
				
				Optional<ButtonType> option = confirmtionAlert.showAndWait();
				
				if(option.get() == ButtonType.OK) {
					try {
						Client selectedClient = clientList.tableView.getSelectionModel().getSelectedItem();
						returnOverView.clientList.clear();
						returnOverView.clientList.add(selectedClient);
						returnOverView.productList.clear();
						returnOverView.productList.addAll(Leihaus.db.getRentedProductsByClient(selectedClient));
						returnOverView.buildClientLabel();
						returnOverView.buildClientContainer();
						returnOverView.rebuildView();
						System.out.println(selectedClient.getFirstname());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		});
	}
	
	
}
