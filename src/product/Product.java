package product;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleFloatProperty;

public class Product extends ProductDetails{

	protected SimpleIntegerProperty id;
	protected SimpleStringProperty productName;
	protected SimpleIntegerProperty productCategory;
	protected SimpleFloatProperty preis;

	public Product()
	{
		super();
		this.id = new SimpleIntegerProperty();
		this.productName = new SimpleStringProperty();
		this.productCategory = new SimpleIntegerProperty();
		this.preis = new SimpleFloatProperty();
	}
	
	public Product(int id, String productName, int productCategory, Float preis)
	{
		super();
		this.id = new SimpleIntegerProperty(id);
		this.productName = new SimpleStringProperty(productName);
		this.productCategory = new SimpleIntegerProperty(productCategory);
		this.preis = new SimpleFloatProperty(preis);
	}
	
	public int getId()
	{
		return this.id.get();
	}
	
	public void setId(int id)
	{
		this.id.setValue(id);
	}
	
	/*
	public String getProductname()
	{
		return this.productName.get();
	}
	
	public void setProductname(String name)
	{
		this.productName.setValue(name);
	}
	
	public int getCategory() {
		return this.productCategory.get();
	}
	
	public void setCategory(int categoryId) {
		this.productCategory.setValue(categoryId);
	}
	
	public Float getPreis() {
		return this.preis.get();
	}
	
	public void setPreis(Float preis) {
		this.preis.setValue(preis);
	}

	public String parseProduktToDb()
	{
		return 	"'" + this.productName.get() + "' ,"+
				+ this.productCategory.get() + " ,"+
				+ this.preis.get();
	}
	*/
}
