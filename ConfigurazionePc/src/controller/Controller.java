package controller;

import model.component.Component;
import model.configurator.Configurator;
import view.TesterFrame;

public class Controller {

	private TesterFrame view;
	private Configurator model;

	public Controller(TesterFrame view, Configurator model) {
		this.view = view;
		this.model = model;
		String s=null;
		for (Component c : model.getCatalog().getComponentList()) {
			s=c.getAttributesMap().get("name");
			view.getListModel().addElement(s);
		}
		
	}

}
