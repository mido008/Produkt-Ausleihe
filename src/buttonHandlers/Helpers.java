package buttonHandlers;

import java.util.Optional;

import client.Client;
import gui.ClientOverView;
import gui.MainContainer;
import gui.ProductOverView;
import gui.RentOverview;
import gui.UpdateClientView;
import gui.UpdateProductView;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import main.Leihaus;
import product.ProductDetails;

public class Helpers {

	private static  Alert warningAlert = new Alert(AlertType.WARNING);
	private static Alert confirmtionAlert = new Alert(AlertType.CONFIRMATION);
	
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
				warningAlert.setHeaderText("Wählen Sie bitte einen Kunden aus!");
				Optional<ButtonType> option = warningAlert.showAndWait();
				
			} else {
				String fullname = client.getFirstname() + " " + client.getLastname();
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
				warningAlert.setHeaderText("Wählen Sie bitte ein Produkt aus!");
				Optional<ButtonType> option = warningAlert.showAndWait();
			} else {
				String productname = product.getProductname();
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

	public static void selecClientForRent(Button bt, RentOverview rentOverview)
	{
		bt.setOnAction(action-> {
			try {
				ClientOverView clientList = new ClientOverView();
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
				ProductOverView productList = new ProductOverView();
				confirmtionAlert.setHeaderText("");
				confirmtionAlert.getDialogPane().setContent(productList.getProductViewForSelectTableView());
				
				Optional<ButtonType> option = confirmtionAlert.showAndWait();
				
				if(option.get() == ButtonType.OK) {
						ProductDetails selectedProduct = productList.tableView.getSelectionModel().getSelectedItem();
						confirmtionAlert.setHeaderText("");
						confirmtionAlert.getDialogPane().setContent(productList.getProductSetDatumView());
						Optional<ButtonType> option2 = confirmtionAlert.showAndWait();
						if(option2.get() == ButtonType.OK) {
							System.out.println("yyyyyyyy");
						}
						rentOverview.productList.add(selectedProduct);
						//rentOverview.buildProductsContainer();
						//rentOverview.rebuildView();
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		});
	}
	
}