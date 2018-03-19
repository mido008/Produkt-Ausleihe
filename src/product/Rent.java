package product;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleFloatProperty;


public class Rent extends ProductDetails{

	protected SimpleIntegerProperty id;
	protected SimpleIntegerProperty cId;
	protected SimpleIntegerProperty pId;
	protected SimpleStringProperty status;
	protected SimpleStringProperty dateFrom;
	protected SimpleStringProperty dateTo;
	
	public Rent()
	{
		super();
		this.cId = new SimpleIntegerProperty();
		this.pId = new SimpleIntegerProperty();
		this.status = new SimpleStringProperty();
		this.dateFrom = new SimpleStringProperty();
		this.dateTo = new SimpleStringProperty();
	}

	public Rent(int id, int cId, int pId, String status, String dateFrom, String dateTo)
	{
		super();
		this.id = new SimpleIntegerProperty(id);
		this.cId = new SimpleIntegerProperty(cId);
		this.pId = new SimpleIntegerProperty(pId);
		this.status = new SimpleStringProperty(status);
		this.dateFrom = new SimpleStringProperty(dateFrom);
		this.dateTo = new SimpleStringProperty(dateTo);
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
	public int getCid()
	{
		return this.cId.get();
	}
	
	public void setCid(int c_id)
	{
		this.cId.setValue(c_id);
	}
	
	public int getPid()
	{
		return this.pId.get();
	}
	
	public void setPid(int p_id)
	{
		this.pId.setValue(p_id);
	}
	
	public String getStatus() {
		return this.status.get();
	}
	
	public void setStatus(String status) {
		this.status.setValue(status);
	}
	
	public String getDatefrom() {
		return this.dateFrom.get();
	}
	
	public void setDatefrom(String dateFrom) {
		this.dateFrom.setValue(dateFrom);
	}
	
	public String getDateto() {
		return this.dateTo.get();
	}
	
	public void setDateto(String dateTo) {
		this.dateTo.setValue(dateTo);
	}

	public String parseRentToDb()
	{
		return 	this.cId.get() + " ,"+
				this.pId.get() + " ,"+
				"'"+ this.status.get() + "'"+
				"'"+ this.dateFrom.get() + "'"+
				"'"+ this.dateTo.get() + "'";
	}
	*/
	
}
