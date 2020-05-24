package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

public class Simulatore {
	//Modello --> stato del sistema ad ogni passo
	private Graph<Country, DefaultEdge>grafo; //da cui prendere i vicini
	
	//tipi di evento? --> coda prioritaria (evento che ci interessa--> migranti che arrivano in uno stato e poi si dividono)
	private PriorityQueue<Evento> queue;
	
	//parametri della simulazione
	private int N_MIGRANTI=1000;
	private Country partenza;
	//valori che il simulatore fornisce al modello
	private int T=-1; //passi simulati
	private Map<Country, Integer> stanziali;
	
	
	//il modello ci passa i parametri della simulazione e il modello (stato del sistema)
	public void init(Country partenza, Graph<Country, DefaultEdge> grafo) {
		this.partenza=partenza;
		this.grafo=grafo;
		
		//impostazione dello stato iniziale
		this.T=1;
		stanziali= new HashMap<>();
			//recupero i country modellati dal grafo
		for(Country c:this.grafo.vertexSet()) {
			stanziali.put(c, 0);
		}
		
		//creo la coda
		this.queue= new PriorityQueue<Evento>();
		//inserisco il primo evento nella coda
		this.queue.add(new Evento(T, partenza, N_MIGRANTI));
	}
	
	//metodo per eseguire la simulazione
	public void run() {
		//finchè c'è un evento nella coda lo estraggo e lo eseguo (uno per volta) e tengo traccia dell'output
		Evento e;
		while((e = this.queue.poll())!=null) { //evento in testa nella coda
			this.T=e.getT(); //tengo traccia dei passi fatti
			//ESEGUO L'EVENTO e (a volte con lo switch per simulazioni più complicate)
			//decido chi si sposta e dove
			int nPersone=e.getN();
			Country stato=e.getStato();
			//la metà si sposta in parti uguali tra tutti i vicini di stato --> cerco i vicini di stato che sono nel grafo
			List<Country> vicini= Graphs.neighborListOf(grafo, stato);
			//la metà si sposta in parti uguali nei vicini
			int migranti =(nPersone/2)/vicini.size(); //n° di persone che si sposta in ogni stato vicino
													  //se ci sono pochi migranti e molti stati non sono un numero sufficiente per spostarsi e diventano stanziali
			if(migranti>0) {
				//le persone si possono muovere (in parti uguali tra i vari vicini)
				for(Country confinante: vicini) {
					queue.add(new Evento(e.getT()+1, confinante, migranti));
				}
			}
			
			//tengo traccia delle persone divenute stanziali (persone arrivate-persone partite)
			int stanziali=nPersone-migranti*vicini.size();
			this.stanziali.put(stato, this.stanziali.get(stato)+stanziali); //perchè così possono tornare indietro
			
		}
		
	}
	
	public Map<Country, Integer> getStanziali(){
		return this.stanziali;
	}

	//numero di passi simulati --> ultimo T visto nella coda
	public Integer getT() {
		return this.T;
	}
}
