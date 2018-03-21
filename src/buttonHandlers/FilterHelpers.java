package buttonHandlers;

import client.Client;
import gui.MainContainer;
import gui.UpdateClientView;
import javafx.scene.control.Button;

public class FilterHelpers {
	
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
}
