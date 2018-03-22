package buttonHandlers;

import java.time.LocalDate;

import client.Client;
import gui.MainContainer;
import gui.UpdateClientView;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.util.Pair;
import product.Rent;

public class FilterHelpers {
	
	public static boolean filterVal = false;
	
	public static boolean clientNameFilter(String clientName, String newVal)
	{
		if(newVal == null || newVal.isEmpty()) {
			return true;
		}
		
		if(clientName.toLowerCase().contains(newVal.toLowerCase())) {
				return true;
			}
		
		return false;
	}
	
	public static boolean productNameFilter(String productName, String newVal)
	{
		if(newVal == null || newVal.isEmpty()) {
			return true;
		}
		
		if(productName.toLowerCase().contains(newVal.toLowerCase())) {
				return true;
			}
		return false;
	}
	
	public static boolean DateFromFilter(String dateFrom, String newVal)
	{
		if(newVal == null || newVal.isEmpty()) {
			return true;
		}
		
		LocalDate date = LocalDate.parse(dateFrom);
		LocalDate newDate = LocalDate.parse(newVal);
		return newDate.compareTo(date) <= 0;
	}
	
	public static boolean DateToFilter(String dateTo, String newVal)
	{
		System.out.println(dateTo + "  ---  " + newVal);
		if(newVal == null || newVal.isEmpty()) {
			return true;
		}
		
		LocalDate date = LocalDate.parse(dateTo);
		LocalDate newDate = LocalDate.parse(newVal);
		return newDate.compareTo(date) >= 0;
	}
	
	public static boolean multiFilter(Rent rent, ObservableList<? extends Pair<String,String>> observableList)
	{
		FilterHelpers.filterVal = true;
		
		observableList.forEach(item->{
			switch(item.getKey()) {
				case "client":
					FilterHelpers.filterVal  &= FilterHelpers.clientNameFilter(rent.getClientname(), item.getValue());
					break;
				case "product":
					FilterHelpers.filterVal  &= FilterHelpers.productNameFilter(rent.getProductname(), item.getValue());
					break;
				case "dateFrom":
					System.out.println(rent.getDatefrom() + "  ---  " + item.getValue());
					FilterHelpers.filterVal  &= FilterHelpers.DateFromFilter(rent.getDatefrom(), item.getValue());
					break;
				case "dateTo":
					FilterHelpers.filterVal  &= FilterHelpers.DateToFilter(rent.getDateto(), item.getValue());
					break;
			}
		});
		//System.out.println(FilterHelpers.filterVal);
		return FilterHelpers.filterVal;
	}
}
