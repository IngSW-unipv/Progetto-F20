package main.services.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;


/**
 * Defines every interaction with db, using sql queries.
 * 
 * @author Alessandro Capici
 * @author Cristian Garau
 * @author Andrea
 *
 */

public class RdbOperation {
	private Connection c;
	private Statement stmt;

	public RdbOperation() {
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:Util/projectDatabase.sqlite");
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());

		}
	}

	public Connection getC() {
		return c;
	}

	public Statement getStmt() {
		return stmt;
	}

	/*
	 * QUERY COMPONENT RELATION
	 */
	public ResultSet getAllComponents() {
		ResultSet rs = null;
		Statement s;
		try {
			s = c.createStatement();
			rs = s.executeQuery("SELECT * FROM Component");
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return rs;

	}
	
	public ResultSet checkIfUserExist(String mail) {
		ResultSet rs = null;
		Statement s;
		try {
			s = c.createStatement();
			rs = s.executeQuery("SELECT email\n" + 
					"FROM User\n" + 
					"WHERE email='"+mail+"'");
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return rs;

	}
	
	public boolean addAdmin(String mail,boolean decision) {
		String sql = ("UPDATE User SET isAdmin= ? WHERE email= ?");
		int admin=0;
		if (decision) {
			admin=1;
		}
		try (PreparedStatement pstmt = c.prepareStatement(sql)) {
			// set the corresponding param
			pstmt.setInt(1, admin);
			pstmt.setString(2, mail);
			// execute the delete statement
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;

	}

	public ResultSet getNeededComponents() {
		ResultSet rs = null;
		Statement s;
		try {
			s = c.createStatement();
			rs = s.executeQuery("SELECT type\r\nFROM TypeComponent\r\nwhere isNeeded=1");
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return rs;
	}
	
	public ResultSet getAdmin() {
		ResultSet rs = null;
		Statement s;
		try {
			s = c.createStatement();
			rs = s.executeQuery("SELECT email FROM User where isAdmin=1");
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return rs;
	}

	public ResultSet getOwnerMailByConfigurationId(int id) {
		ResultSet rs = null;
		String idAsString = Integer.toString(id);
		Statement s;
		try {
			s = c.createStatement();
			rs = s.executeQuery("SELECT EmailU from Configuration WHERE id=" + idAsString);
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return rs;
	}

	public ResultSet getTypeComponents() {
		ResultSet rs = null;
		Statement s;
		try {
			s = c.createStatement();
			rs = s.executeQuery("SELECT type\r\nFROM TypeComponent");
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return rs;
	}

	public boolean removeComponent(String model, String type) {
		String sql = "DELETE FROM Component WHERE TypeofC = ? AND Model= ?";

		try (PreparedStatement pstmt = c.prepareStatement(sql)) {

			// set the corresponding param
			pstmt.setString(1, type);
			pstmt.setString(2, model);
			// execute the delete statement
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		
		sql = "DELETE FROM Attribute WHERE ModelofC = ? AND TypeofC = ?";
		try (PreparedStatement pstmt = c.prepareStatement(sql)) {

			pstmt.setString(1, model);
			pstmt.setString(2, type);
			// execute the delete statement
			pstmt.executeUpdate();
			return true;
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return false;

	}
	
	public boolean addComponent(String model, String type, double price) {
		String sql = "INSERT INTO Component(TypeofC,Model,Price) VALUES(?,?,?)";
		PreparedStatement ps;
		try {
			ps = c.prepareStatement(sql);
			ps.setString(1, type);
			ps.setString(2, model);
			ps.setDouble(3, price);
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;

		}
	}

	/*
	 * QUERY ATTRIBUTE RELATION
	 */

	public ResultSet getAttributesByComponent(String model, String typeOfComponent) {
		Statement s;
		String sql = "select attribute.TypeofC, Attribute.ModelofC, Attribute.NameStdAtt, Attribute.AttValue, StandardAttribute.ConstraintName, StandardAttribute.Category, StandardAttribute.IsPresentable\r\n"
				+ "from Attribute, StandardAttribute\r\n" + "where attribute.TypeofC = '" + typeOfComponent
				+ "' and attribute.ModelofC = '" + model + "'\r\n" + "and StandardAttribute.TypeOfComponent = '"
				+ typeOfComponent + "'\r\n" + "and StandardAttribute.Name = Attribute.NameStdAtt\r\n";
		ResultSet rs = null;
		try {
			s = c.createStatement();
			rs = s.executeQuery(sql);

		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());

		}

		return rs;
	}

	public ResultSet getConfiguration(int confId) {
		try {
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT*\n" + "FROM ElementConfiguration NATURAL join Configuration\n" + "where Id=" + confId);
			return rs;
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());

		}
		return null;
	}

	public ResultSet getConfigurationByEmail(String email) {
		try {
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT*\n" + "FROM ElementConfiguration NATURAL join Configuration\n"
					+ "where EmailU='" + email + "'");
			return rs;
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		return null;
	}

	
	public boolean addConfiguration(int id, String name, String email, List<String> Type, List<String> Model,
			Map<String, Integer> counter) {
		String sql = "INSERT INTO Configuration(Id,Name,EmailU) VALUES(?,?,?)";
		PreparedStatement ps;
		try {
			ps = c.prepareStatement(sql);
			ps.setInt(1, id);
			ps.setString(2, name);
			ps.setString(3, email);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;

		}
		sql = "INSERT INTO ElementConfiguration(TypeofC, ModelofC, Id,Counter) VALUES(?,?,?,?)";
		for (int i = 0; i < Type.size(); i++) {
			try {
				ps = c.prepareStatement(sql);
				ps.setString(1, Type.get(i));
				ps.setString(2, Model.get(i));
				ps.setInt(3, id);
				ps.setInt(4, counter.get(Model.get(i)));
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;

	}

	public ResultSet getUser(String email) {
		try {
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT*\n" + "FROM User where email='" + email + "'");
			return rs;
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		return null;
	}

	public ResultSet login(String email, String password) {
		String sql = "SELECT * FROM User WHERE email = ? AND password = ?";
		PreparedStatement ps;

		try {
			ps = c.prepareStatement(sql);
			ps.setString(1, email);
			ps.setString(2, password);
			return ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public boolean addUser(String name, String cognome, String email, String password, boolean isAdmin) {
		String sql = "INSERT INTO User(firstName,lastName,email,password,isAdmin) VALUES(?,?,?,?,?)";
		PreparedStatement ps;
		try {
			ps = c.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, cognome);
			ps.setString(3, email);
			ps.setString(4, password);
			if (isAdmin) {
				ps.setInt(5, 1);
			} else {
				ps.setInt(5, 0);
			}
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}
	
	public boolean removeUser(String email) {
		String query = "DELETE FROM User WHERE email= '"+ email+"'";
		boolean isDone = false;
		try {
			PreparedStatement pstm = c.prepareStatement(query);
			pstm.executeUpdate();
			isDone = true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return isDone;
	}

	public boolean removeConfiguration(int id) {
		String sql = "DELETE FROM ElementConfiguration WHERE Id = ?";
		try (PreparedStatement pstmt = c.prepareStatement(sql)) {

			// set the corresponding param
			pstmt.setInt(1, id);
			// execute the delete statement
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}

		sql = "DELETE FROM Configuration WHERE Id = ?";
		try (PreparedStatement pstmt = c.prepareStatement(sql)) {

			// set the corresponding param
			pstmt.setInt(1, id);
			// execute the delete statement
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return false;

	}

	public ResultSet getAllConstraints() {
		try {
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Bound");
			return rs;
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}

		return null;

	}

	public boolean addNewConstraint(String name, String type) {
		String sql = "INSERT INTO Bound(Name,Type) VALUES(?,?)";
		PreparedStatement ps;
		try {
			ps = c.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, type);
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean removeConstraint(String name) {
		String sql = "DELETE FROM Bound WHERE Name = ?";

		try (PreparedStatement pstmt = c.prepareStatement(sql)) {

			// set the corresponding param
			pstmt.setString(1, name);
			// execute the delete statement
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	public ResultSet getLastUsedId() {
		String sql = "SELECT max(Id) as maxId\r\nFROM Configuration";
		try {
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			return rs;
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		return null;

	}

	public boolean changeEmail(String oldEmail, String newEmail) {
		String sql = ("UPDATE User SET email= ? WHERE email= ?");
		try (PreparedStatement pstmt = c.prepareStatement(sql)) {
			// set the corresponding param
			pstmt.setString(1, newEmail);
			pstmt.setString(2, oldEmail);
			// execute the delete statement
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;

	}

	public boolean changeConfName(int confId, String newName) {
		String sql = ("UPDATE Configuration SET Name= ? WHERE Id= ?");
		try (PreparedStatement pstmt = c.prepareStatement(sql)) {
			// set the corresponding param
			pstmt.setString(1, newName);
			pstmt.setInt(2,confId);
			// execute the delete statement
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;

	}
	
	public boolean changePassword(String email, String newPassword) {
		String sql = ("UPDATE User SET password= ? WHERE email= ?");
		try (PreparedStatement pstmt = c.prepareStatement(sql)) {
			// set the corresponding param
			pstmt.setString(1, newPassword);
			pstmt.setString(2, email);
			// execute the delete statement
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;

	}
	
	public ResultSet getPassword(String email) {
		String sql = "SELECT password FROM User WHERE email = ?";
		try {
			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setString(1, email);
			
			return pstmt.executeQuery();
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
			
		return null;
	}
	
	public ResultSet getStandardAttributes(String typeComponent) {
		String sql = "SELECT Name FROM StandardAttribute WHERE TypeOfComponent = ?";
		PreparedStatement ps;

		try {
			ps = c.prepareStatement(sql);
			ps.setString(1, typeComponent);
			return ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public boolean addAttribute(String typeOfC, String modelOfC, String nameAtt, String attValue) {
		String sql = "INSERT INTO Attribute(TypeofC, NameStdAtt, ModelofC, AttValue, TypeofStdAtt) VALUES(?,?,?,?,?)";
		PreparedStatement ps;
		try {
			ps = c.prepareStatement(sql);
			ps.setString(1, typeOfC);
			ps.setString(2, nameAtt);
			ps.setString(3, modelOfC);
			ps.setString(4, attValue);
			ps.setString(5, typeOfC);
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean addStandardAttribute(String name, String typeOfC, String constraintName, String category, int isPresentable) {
		String sql = "INSERT INTO StandardAttribute(Name, TypeOfComponent, ConstraintName, Category, IsPresentable) "
				+ "VALUES(?,?,?,?,?)";
		PreparedStatement ps;
		try {
			ps = c.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, typeOfC);
			ps.setString(3, constraintName);
			ps.setString(4, category);
			ps.setInt(5, isPresentable);
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean addTypeComponent(String type, int isNeeded) {
		String sql = "INSERT INTO TypeComponent(type, isNeeded) VALUES(?,?)";
		PreparedStatement ps;
		try {
			ps = c.prepareStatement(sql);
			ps.setString(1, type);
			ps.setInt(2, isNeeded);
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
