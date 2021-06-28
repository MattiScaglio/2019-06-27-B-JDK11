package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.Adiacenza;
import it.polito.tdp.crimes.db.EventsDao;


public class Model {
	
	EventsDao dao;
	private Graph<String, DefaultWeightedEdge> grafo;
	private Map<String, Event> idMap;
	
	public Model() {
		this.dao = new EventsDao();
	}
	
	
	public void CreaGrafo(int mese, String categoria) {
		double peso=0.0;
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		
		//aggiungo i vertici
		Graphs.addAllVertices(this.grafo, this.dao.getVertici(categoria, mese));
		
		System.out.println("VERTICI FATTI: "+this.grafo.vertexSet().size());
		
		
		//aggiungo archi
		for (Adiacenza arco : dao.getAdiacenze(categoria, mese)) {
			
			if(arco.getPeso()!=0)
			Graphs.addEdgeWithVertices(this.grafo,arco.getE1(),arco.getE2(), peso);

		}
		System.out.println("#archi"+grafo.edgeSet().size());

	
	}

	public int nVertici() {
		return this.grafo.vertexSet().size();
	}

	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	
	
	
	public List<String> getVettore() {
		return dao.getVettore();
	}





	public List<Integer> getVettoreMesi() {
		// TODO Auto-generated method stub
		return dao.getVettoreMesi();
	}


	public float getMediaPesoArchi(String categoria,int  mese) {
		int sum = 0;
		for(Adiacenza a : this.dao.getAdiacenze(categoria,mese) ) {
			sum += a.getPeso();
		}
		float avg = sum/this.dao.getAdiacenze(categoria,mese).size();
		
		return avg;
	}
	
	public List<Adiacenza> getArchiSopraMedia(String categoria,int  mese){
		
		ArrayList<Adiacenza> archiMediaSuperiore = new ArrayList<Adiacenza>();
		float avg= this.getMediaPesoArchi(categoria, mese);
		
		for(Adiacenza a: this.dao.getAdiacenze(categoria, mese)) {
			if(a.getPeso()>avg) {
				archiMediaSuperiore.add(a);
			}
		}
		return archiMediaSuperiore;
	}
	
}
