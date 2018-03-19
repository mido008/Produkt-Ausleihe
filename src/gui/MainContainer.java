package gui;

import javafx.scene.Group;
import javafx.scene.layout.StackPane;

public class MainContainer {

	final StackPane mainContainer;
	
	public MainContainer()
	{
		this.mainContainer = new StackPane();
		this.initMainContainer();
	}
	
	public void initMainContainer() 
	{
		this.mainContainer.getStyleClass().add("main-container");
	};
	
	public StackPane getMainContainer()
	{
		return this.mainContainer;
	}
	
	public void setContent(Group group)
	{
		this.mainContainer.getChildren().clear();
		this.mainContainer.getChildren().addAll(group);
	}
	
}
