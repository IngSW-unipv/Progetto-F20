package model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.component.Attribute;
import model.component.Component;

public class RdbComponentDAO implements InterfaceComponentDAO {

	private RdbOperation dbop;

	public RdbComponentDAO(RdbOperation dbop) {
		this.dbop = dbop;
	}

	public List<Component> getAllComponent() {
		ResultSet allComp = dbop.getAllComponents();
		ResultSet comp;
		
		ArrayList<Component> c = new ArrayList<>();
		HashMap<String, Attribute> attributes;
		Attribute a;
		
		String model, typeOfComponent, stringPrice;
		String nameStdAtt, attValue, constraintName, category;
		boolean isPresentable, isBinding;
		double price = -1; // settato a -1 perch� � un valore non valido
		
		try {
			while(allComp.next()) {
				attributes = new HashMap<>();
				model = allComp.getString("Model");
				typeOfComponent = allComp.getString("TypeofC");
				stringPrice = allComp.getString("Price");
				comp = dbop.getAttributesByComponent(model, typeOfComponent);
				
				while(comp.next()) {
					nameStdAtt = comp.getString("NameStdAtt");
					attValue = comp.getString("AttValue");
					constraintName = comp.getString("ConstraintName");
					category = comp.getString("Category");
					isPresentable = Boolean.parseBoolean(comp.getString("IsPresentable"));
					if(constraintName == null) {
						isBinding = false;
					}else {
						isBinding = true;
					}
					
					a = new Attribute(nameStdAtt, attValue, isPresentable, isBinding, category);
					attributes.put(nameStdAtt, a);
				}
				
				try {
					price = Double.parseDouble(stringPrice);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				c.add(new Component(model, typeOfComponent, price, attributes));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return c;
	}
	
}
