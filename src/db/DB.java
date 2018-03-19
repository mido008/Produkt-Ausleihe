package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import product.Category;
import product.Product;
import product.ProductDetails;
import product.Rent;


interface Callback{
	public void queryCallback() throws SQLException;
}


public class DB {
	
	Connection connection;
	Statement statement;
	
	public DB()
	{
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void prepareStatement(Callback callback) throws SQLException, Exception
	{
		this.connection = null;
		try{
			this.connection = DriverManager.getConnection("jdbc:sqlite:Laiheus.db");
			this.statement = connection.createStatement();
			this.statement.setQueryTimeout(30);
			callback.queryCallback();
			
		} catch(SQLException e){
			System.err.println(e.getMessage());
		} finally {
			try
		      {
		        if(this.connection != null)
		        	this.connection.close();
		      }
		      catch(SQLException e)
		      {
		        System.err.println(e);
		      }
		}
	}
	
	
	public void initDB() throws Exception
	{
		ArrayList<Category> productCategories = new ArrayList<Category>();
		 productCategories.add(new Category("Technick"));
		 productCategories.add(new Category("Technick2"));
		 productCategories.add(new Category("Technick3"));
		 
		 String categories = this.provideCategoriesList(productCategories);
		 
		this.prepareStatement(new Callback() {
			@Override
			public void queryCallback() throws SQLException {
				//statement.executeUpdate("DROP TABLE IF EXISTS clients");
				//statement.executeUpdate("DROP TABLE IF EXISTS categories");
				//statement.executeUpdate("DROP TABLE IF EXISTS products");
				
				statement.executeUpdate("CREATE TABLE IF NOT EXISTS clients (id integer PRIMARY KEY, firstname string, lastname string, address string, plz string, city string, tel string)");
				statement.executeUpdate("CREATE TABLE IF NOT EXISTS products (id integer PRIMARY KEY, label string, preis numeric, categorie_id integer, FOREIGN KEY (categorie_id) REFERENCES cotegories (id) ON DELETE CASCADE ON UPDATE NO ACTION)");
				statement.executeUpdate("CREATE TABLE IF NOT EXISTS categories (id integer PRIMARY KEY, label string UNIQUE)");
				statement.executeUpdate("CREATE TABLE IF NOT EXISTS rents (id integer PRIMARY KEY, c_id integer, p_id integer, status string, date_from date, date_to date, FOREIGN KEY (c_id) REFERENCES clients (id) ON DELETE CASCADE ON UPDATE NO ACTION, FOREIGN KEY (p_id) REFERENCES products (id) ON DELETE CASCADE ON UPDATE NO ACTION)");
				statement.executeUpdate("INSERT OR IGNORE INTO categories (label)  VALUES "+ categories);
			};
		});
	}

	public ArrayList<Client> getClients() throws SQLException, Exception
	{
		ArrayList<Client> clientsList = new ArrayList<Client>();
		this.prepareStatement(new Callback() {
			@Override
			public void queryCallback() throws SQLException {
				ResultSet clients =  statement.executeQuery("select * from clients");
				while(clients.next()) {
					Client client = new Client();
					client.setId(clients.getString("id"));
					client.setFirstname(clients.getString("firstname"));
					client.setLastname(clients.getString("lastname"));
					client.setAddress(clients.getString("address"));
					client.setPlz(clients.getString("plz"));
					client.setCity(clients.getString("city"));
					client.setTel(clients.getString("tel"));
					
					clientsList.add(client);
				}
			};
		});
		
		return clientsList;
	}
	
	public void addClient(Client client) throws SQLException, Exception {
		String values = client.parseClientToDB(); 
		this.prepareStatement(new Callback() {
			@Override
			public void queryCallback() throws SQLException {
				statement.executeUpdate("INSERT INTO clients (firstname, lastname, address, plz, city, tel) VALUES ("+ values +")");
			};
		});
	}

	public void updateClient(Client client) throws SQLException, Exception {
		this.prepareStatement(new Callback() {
			@Override
			public void queryCallback() throws SQLException {
				statement.executeUpdate("UPDATE clients SET firstname = '"+ client.getFirstname() +
						"', lastname = '"+ client.getLastname() +
						"', address = '"+ client.getAddress()+
						"', plz = '"+ client.getPlz() + 
						"', city = '"+ client.getCity() +
						"', tel = '"+ client.getTel() +"' WHERE id = "+ client.getId() +";");
			};
		});
	}
	
	public void removeClient(Client client) throws SQLException, Exception {
		this.prepareStatement(new Callback() {
			@Override
			public void queryCallback() throws SQLException {
				statement.executeUpdate("DELETE FROM clients WHERE id = "+ client.getId() +";");
			};
		});
	}
	
	/******************************************************************************/
	
	
	/****************** Product Queries *****************/
	
	public void initCategories(ArrayList<Category> categories) throws SQLException, Exception {
		String values = categories.toString(); 
		this.prepareStatement(new Callback() {
			@Override
			public void queryCallback() throws SQLException {
				statement.executeUpdate("INSERT IGNORE INTO categories (label) values ("+ values +")");
			};
		});
	}
	
	public String provideCategoriesList(ArrayList<Category> productCategories)
	{
		String categories = "";
		int i = 0;
		for(Category item : productCategories){
			i++;
			if(i < productCategories.size()) {
				categories += "('" + item.getLabel() + "'),"; 
			} else {
				categories += "('" + item.getLabel() + "')"; 
			}
		};
		return categories;
	}

	
	public ArrayList<Category> getCategories() throws SQLException, Exception
	{
		ArrayList<Category> categoriesList = new ArrayList<Category>();

		this.prepareStatement(new Callback() {
			@Override
			public void queryCallback() throws SQLException {
				ResultSet categories = statement.executeQuery("select * from categories");
				while(categories.next()) {
					Category category = new Category();
					category.setId(categories.getInt("id"));
					category.setLabel(categories.getString("label"));
					categoriesList.add(category);
				}
			};
		});
		return categoriesList;
	}
	
	/******************************************************************************/
	
	/****************** Product Queries *****************/
	public ArrayList<ProductDetails> getProductOverView() throws SQLException, Exception
	{
		ArrayList<ProductDetails> productsList = new ArrayList<ProductDetails>();

		this.prepareStatement(new Callback() {
			@Override
			public void queryCallback() throws SQLException {
				ResultSet productDetails = statement.executeQuery("SELECT p.id As pId,p.label AS pLabel,preis,categorie_id, c.label AS cLabel, r.id AS rId, c_id, p_id, CASE WHEN status IS NULL THEN 'verfügbar' ELSE status END AS status, date_from, date_to  FROM products p INNER JOIN categories c ON p.categorie_id = c.id LEFT JOIN rents r ON p.id = r.p_id");
				while(productDetails.next()) {
					
					ProductDetails product = new ProductDetails();
					product.setProductId(productDetails.getInt("pId"));
					product.setProductname(productDetails.getString("pLabel"));
					product.setPreis(productDetails.getFloat("preis"));
					product.setCategory(new Category(productDetails.getInt("categorie_id"), productDetails.getString("cLabel")));
					
					product.setRentId(productDetails.getInt("rId"));
					product.setCid(productDetails.getInt("c_id"));
					product.setStatus(productDetails.getString("status"));
					product.setDatefrom(productDetails.getString("date_from"));
					product.setDateto(productDetails.getString("date_to"));
					productsList.add(product);
				}
			};
		});
		return productsList;
	}
	
	public void addProduct(ProductDetails product) throws SQLException, Exception {
		String values = product.parseProduktToDb(); 
		this.prepareStatement(new Callback() {
			@Override
			public void queryCallback() throws SQLException {
				statement.executeUpdate("INSERT INTO products (label, categorie_id, preis) values ("+ values +")");
			};
		});
	}
	
	public void updateProduct(ProductDetails product) throws SQLException, Exception {
		this.prepareStatement(new Callback() {
			@Override
			public void queryCallback() throws SQLException {
				statement.executeUpdate("UPDATE products SET label = '"+ product.getProductname() +"', "+
						"preis = " + product.getPreis() + "," +
						"categorie_id = "+ product.getCategory().getId()+ 
						" WHERE id = "+ product.getProductId() +";");
			};
		});
	}

	public void removeProduct(ProductDetails product) throws SQLException, Exception {
		this.prepareStatement(new Callback() {
			@Override
			public void queryCallback() throws SQLException {
				statement.executeUpdate("DELETE FROM products WHERE id = "+ product.getProductId() +";");
			};
		});
	}
	
	/******************************************************************************/

}


