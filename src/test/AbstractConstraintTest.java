package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import main.model.configurator.component.Attribute;
import main.model.configurator.component.Component;
import main.model.configurator.constraint.DimensionConstraint;
import main.model.configurator.constraint.EqualsConstraint;

/**
 * 
 * @author Andrea
 * @author Irene R.
 *
 */

public class AbstractConstraintTest {
	
	
	public AbstractConstraintTest() {

	}
	
	/**
	 * Qui devo avere tutti gli attribute che poi verranno provati con assertTrue
	 * @see EqualsConstraint
	 */
	private static ArrayList<Object[]> initializeAttributesForEqualsTrue() {
		/*
		 * Costruisco cos� la lista, almeno creo un Component con un 
		 * attributo solo e riesco a provare il funzionamento dei singoli constraint.
		 * Qui posso sfruttare il fatto che non ho internal ed external per
		 * "interlacciare" gli attribti.
		 */
		
		ArrayList<Object[]> buff = new ArrayList<Object[]>();
		
		buff.add(new Object[] {"cpuSocket", "1200", "CpuSocket", true, true});
		buff.add(new Object[] {"cpuSocket", "1200", "CpuSocket", true, true});
		
		buff.add(new Object[] {"cpuSocket", "0", "CpuSocket", true, true});
		buff.add(new Object[] {"cpuSocket", "0", "CpuSocket", true, true});
		
		buff.add(new Object[] {"cpuSocket", "1000", "CpuSocket", true, true});
		buff.add(new Object[] {"cpuSocket", "1000", "CpuSocket", true, true});

		return buff;		
	} 
	
	/**
	 * Qui devo avere tutti gli attribute che poi verranno provati con assertFalse
	 * @see EqualsConstraint
	 */
	private static ArrayList<Object[]> initializeAttributesForEqualsFalse() {
		/*
		 * Costruisco cos� la lista, almeno creo un Component con un 
		 * attributo solo e riesco a provare il funzionamento dei singoli constraint.
		 * Qui posso sfruttare il fatto che non ho internal ed external per
		 * "interlacciare" gli attribti.
		 */
		
		ArrayList<Object[]> buff = new ArrayList<Object[]>();
		
		buff.add(new Object[] {"cpuSocket", "1200", "CpuSocket", true, true});
		buff.add(new Object[] {"cpuSocket", "1201", "CpuSocket", true, true});
		
		buff.add(new Object[] {"cpuSocket", "5", "CpuSocket", true, true});
		buff.add(new Object[] {"cpuSocket", "0", "CpuSocket", true, true});
		
		buff.add(new Object[] {"cpuSocket", "800", "CpuSocket", true, true});
		buff.add(new Object[] {"cpuSocket", "500", "CpuSocket", true, true});

		return buff;		
	} 
	
	/**
	 * Qui devo avere tutti gli attribute che poi verranno provati con assertTrue
	 * @see DimensionConstraint
	 */
	private static ArrayList<Object[]> initializeAttributesForDimensionTrue() {

		
		ArrayList<Object[]> buff = new ArrayList<Object[]>();
		
		buff.add(new Object[] {"ramSize", "16", "RamSize", true, true, "external"});
		buff.add(new Object[] {"ramSize", "8", "RamSize", true, true, "internal"});
		
		buff.add(new Object[] {"ramSize", "16", "RamSize", true, true, "external"});
		buff.add(new Object[] {"ramSize", "16", "RamSize", true, true, "internal"});
		
		buff.add(new Object[] {"ramSize", "8", "RamSize", true, true, "internal"});
		buff.add(new Object[] {"ramSize", "16", "RamSize", true, true, "external"});
		
		return buff;		
	}

	/**
	 * Qui devo avere tutti gli attribute che poi verranno provati con assertFalse
	 * @see DimensionConstraint
	 */
	private static ArrayList<Object[]> initializeAttributesForDimensionFalse() {
		
		ArrayList<Object[]> buff = new ArrayList<Object[]>();
		
		buff.add(new Object[] {"ramSize", "16", "RamSize", true, true, "internal"});
		buff.add(new Object[] {"ramSize", "8", "RamSize", true, true, "external"});
		
//		buff.add(new Object[] {"ramSize", "16", "RamSize", true, true, "internal"});
//		buff.add(new Object[] {"ramSize", "16", "RamSize", true, true, "external"});
		
//		buff.add(new Object[] {"ramSize", "8", "RamSize", true, true, "external"});
//		buff.add(new Object[] {"ramSize", "16", "RamSize", true, true, "internal"});
		
		return buff;		
	}


	/**
	 * @param attBuff
	 * @return attriuteList - elements goes 2 by 2
	 * 
	 */
	private static ArrayList<Attribute> createAttributesCouples(ArrayList<Object[]> attBuff) {
		
		ArrayList<Attribute> attributesList = new ArrayList<Attribute>(); 
	
		if(attBuff.size() % 2 == 0) {
			//ATTENZIONE, LA VARIABILE 'i' VIENE INCREMENTATA DI DUE AD OGNI CICLO
			for(int i = 0; i < attBuff.size() - 1; i += 2) {
				
				int j = i+1;
				String name1 = (String)attBuff.get(i)[0];
				String value1 = (String)attBuff.get(i)[1]; 
				String constraintName1 = (String)attBuff.get(i)[2];
				boolean isBinding1 = (boolean)attBuff.get(i)[3];
				boolean isPresentable1 = (boolean)attBuff.get(i)[4];
				String constraintCategory1 = (String)attBuff.get(i)[5];				
				Attribute newAtt = new Attribute(name1, value1, constraintName1, isBinding1, isPresentable1, constraintCategory1);
				
				String name2 = (String)attBuff.get(j)[0];
				String value2 = (String)attBuff.get(j)[1]; 
				String constraintName2 = (String)attBuff.get(j)[2];
				boolean isBinding2 = (boolean)attBuff.get(j)[3];
				boolean isPresentable2 = (boolean)attBuff.get(j)[4];
				String constraintCategory2 = (String)attBuff.get(j)[5];				
				Attribute oldAtt = new Attribute(name2, value2, constraintName2, isBinding2, isPresentable2, constraintCategory2);
				
				System.out.println(name1 +" "+value1+" "+ constraintCategory1);
				System.out.println(name2 +" "+value2+" "+ constraintCategory2);			
				System.out.println();
				
				attributesList.add(newAtt);
				attributesList.add(oldAtt);
			}
		} else {
			System.out.println("La lista di oggetti deve contenere un numero pari di oggetti");
		}
		return attributesList;
	}
	
	
	
	/**
	 * This test should consider ok
	 */
	@Test
	public void trueTestCheckListOnDimension() {

		ArrayList<Object[]> attBuff = AbstractConstraintTest.initializeAttributesForDimensionTrue();
		ArrayList<Attribute> attributeList = AbstractConstraintTest.createAttributesCouples(attBuff);
		
		DimensionConstraint constraint = new DimensionConstraint(attributeList.get(0).getConstraintName());
		
		for (int i = 0; i < attributeList.size() - 1; i++) {
						
			HashMap<String, Attribute> attributesMap1 = new HashMap<String, Attribute>();
			HashMap<String, Attribute> attributesMap2 = new HashMap<String, Attribute>();
			
			Attribute att1 = attributeList.get(i);
			Attribute att2 = attributeList.get(i+1);
			
			attributesMap1.put(att1.getName(), att1);
			attributesMap2.put(att2.getName(), att2);
			
			Component alreadyCheckedComp = new Component("old", "cpu", 16, attributesMap1);
			ArrayList<Component> oldCheckedComponents = new ArrayList<Component>();
			oldCheckedComponents.add(alreadyCheckedComp);
			
			Component componentToCheck = new Component("new", "mobo", 14, attributesMap2);
			
			assertTrue(constraint.checkList(oldCheckedComponents, componentToCheck));
		}
		
	}

	/**
	 * This test should consider ok
	 */
	@Test
	public void falseTestCheckListOnDimension() {

		ArrayList<Object[]> attBuff = AbstractConstraintTest.initializeAttributesForDimensionFalse();
		ArrayList<Attribute> attributeList = AbstractConstraintTest.createAttributesCouples(attBuff);
		
		DimensionConstraint constraint = new DimensionConstraint(attributeList.get(0).getConstraintName());
		
		for (int i = 0; i < attributeList.size() - 1; i++) {
						
			HashMap<String, Attribute> attributesMap1 = new HashMap<String, Attribute>();
			HashMap<String, Attribute> attributesMap2 = new HashMap<String, Attribute>();
			
			Attribute att1 = attributeList.get(i);
			Attribute att2 = attributeList.get(i+1);
			
			attributesMap1.put(att1.getName(), att1);
			attributesMap2.put(att2.getName(), att2);
			
			Component alreadyCheckedComp = new Component("old", "cpu", 16, attributesMap1);
			ArrayList<Component> oldCheckedComponents = new ArrayList<Component>();
			oldCheckedComponents.add(alreadyCheckedComp);
			
			Component componentToCheck = new Component("new", "mobo", 14, attributesMap2);
			
			assertFalse(constraint.checkList(oldCheckedComponents, componentToCheck));
		}
		
	}

	
	@Test
	public void testCheckListEqualsConstraint() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testCheckListMaxConstraint() {
		fail("Not yet implemented");
	}
	
/*
	@Test
	@Ignore
	public void testSelectAttributeSameNameListOfComponent() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testSelectAttributeSameNameComponent() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testFilterAttributesList() {
		fail("Not yet implemented");
	}
*/
}
