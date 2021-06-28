package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.crimes.model.Event;


public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	
public List<String> getVettore() {
		
		String sql="SELECT distinct e.offense_category_id "
				+ "FROM EVENTS e "
				+ "ORDER BY e.offense_category_id";
		
		List<String> result = new ArrayList<String>();
		Connection conn = DBConnect.getConnection();
		
		try {
			
			System.out.println("INGRESSO TRY");
			PreparedStatement st = conn.prepareStatement(sql);
			
			ResultSet res = st.executeQuery();
			while (res.next()) {
				System.out.println("AGGIUNGO");
				result.add((res.getString("offense_category_id")));
			
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			System.out.println("ERRORE SQL");
			e.printStackTrace();
			return null;
		}

	}


public List<Integer> getVettoreMesi() {
	
	String sql="SELECT DISTINCT MONTH(e.reported_date) as mesi "
			+ "FROM events e "
			+ "ORDER BY Month(e.reported_date)";
	
	List<Integer> result = new ArrayList<Integer>();
	Connection conn = DBConnect.getConnection();
	
	try {
		
		System.out.println("INGRESSO TRY");
		PreparedStatement st = conn.prepareStatement(sql);
		
		ResultSet res = st.executeQuery();
		
		while (res.next()) {
			System.out.println("AGGIUNGO");
			result.add((res.getInt("mesi")));
		
		}
		conn.close();
		return result;

	} catch (SQLException e) {
		System.out.println("ERRORE SQL");
		e.printStackTrace();
		return null;
	}
}

public List<String> getVertici(String categoria, int mese) {
	
	String sql="SELECT distinct e.offense_type_id as tipoReato "
			+ "FROM events e "
			+ "WHERE e.offense_category_id = ? "
			+ "AND MONTH(e.reported_date)= ? ";
	
	List<String> result = new ArrayList<String>();
	Connection conn = DBConnect.getConnection();
	
	try {
		PreparedStatement st = conn.prepareStatement(sql);
		st.setString(1, categoria);
		st.setInt(2, mese);
		ResultSet res = st.executeQuery();
		while (res.next()) {

		result.add(res.getString("tipoReato"));				
	
		}
		conn.close();
		return result;

	} catch (SQLException e) {
		e.printStackTrace();
		return null;
	}

}


public List<Adiacenza> getAdiacenze(String categoria, int mese) {
	
	String sql="SELECT e1.offense_type_id AS id1, e2.offense_type_id AS id2, COUNT(distinct e1.neighborhood_id) AS peso\n"
			+ "FROM events e1, events e2 "
			+ "WHERE e1.offense_type_id> e2.offense_type_id "
			+ "AND e1.neighborhood_id = e2.neighborhood_id "
			+ "AND e1.offense_category_id = ? "
			+ "AND e2.offense_category_id = e1.offense_category_id "
			+ "AND MONTH(e1.reported_date)= ? "
			+ "and MONTH(e1.reported_date)= MONTH(e2.reported_date) "
			+ "GROUP BY e1.offense_type_id, e2.offense_type_id";
	
	List <Adiacenza> result= new ArrayList <Adiacenza>();
	Connection conn = DBConnect.getConnection();
	try {
		PreparedStatement st = conn.prepareStatement(sql);
		st.setString(1, categoria);
		st.setInt(2, mese);
		ResultSet res = st.executeQuery();
		while (res.next()) {

				result.add(new Adiacenza(res.getString("id1"),res.getString("id2"),res.getInt("peso")));
		
		}
		conn.close();
		return result;

	} catch (SQLException e) {
		e.printStackTrace();
		return null;
	}
	
}
}
