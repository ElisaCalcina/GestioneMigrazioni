package it.polito.tdp.borders.model;

//gli eventi devono essere comparabili perchè sono inseriti in una priorityQueue

public class Evento implements Comparable<Evento>{
	private int t; //tempo
	private Country stato; //lo stato in cui arrivano i migranti al tempo t
	private int n; //il numero di migranti che arrivano in "stato" al tempo t (la metà di essi si sposterà)
	
	public Evento(int t, Country stato, int n) {
		super();
		this.t = t;
		this.stato = stato;
		this.n = n;
	}

	public int getT() {
		return t;
	}

	public void setT(int t) {
		this.t = t;
	}

	public Country getStato() {
		return stato;
	}

	public void setStato(Country stato) {
		this.stato = stato;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	@Override
	public int compareTo(Evento o) {
		return (this.t-o.t);
	}
	
	

}
