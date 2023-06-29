package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {

	private List<Esame> allEsami;
	private Set<Esame> migliore;
	private double mediaMigliore;
	
	
	public Model() {
		EsameDAO dao= new EsameDAO();
		this.allEsami= dao.getTuttiEsami();
	}
	
	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		migliore= new HashSet<>();
		mediaMigliore=0.0;
		
		Set<Esame> parziale= new HashSet<>();
		cercaMeglio(parziale, 0, numeroCrediti);
		
		return migliore;
		
	}

	
	private void cerca(Set<Esame> parziale, int livello, int numeroCrediti) {
		int sommaCrediti= sommaCrediti(parziale);
		
		if(sommaCrediti > numeroCrediti) 
			return;
		
		if(sommaCrediti==numeroCrediti) { // potrei avere una soluzione qui
			double mediaVoti= calcolaMedia(parziale);
			if(mediaVoti> mediaMigliore) {
				mediaMigliore= mediaVoti;
				migliore= new HashSet<>(parziale);
			}
			return;
		}
		
		if(livello== allEsami.size()) // ho aggiunto tutti gli esami a disposizione
			return;
		
		// se arrivo qui numeroCrediti > sommaCrediti
		
		for(Esame e: allEsami) {
			if(!parziale.contains(e)) {
				parziale.add(e);
				cerca(parziale, livello+1, numeroCrediti);
				parziale.remove(e);
			}
		}
		
		
	}
	
	
	private void cercaMeglio(Set<Esame> parziale, int livello, int numeroCrediti) {
		int sommaCrediti= sommaCrediti(parziale);
		
		if(sommaCrediti > numeroCrediti) 
			return;
		
		if(sommaCrediti==numeroCrediti) { // potrei avere una soluzione qui
			double mediaVoti= calcolaMedia(parziale);
			if(mediaVoti> mediaMigliore) {
				mediaMigliore= mediaVoti;
				migliore= new HashSet<>(parziale);
			}
			return;
		}
		
		if(livello== allEsami.size())
			return;
		
		
		// provo ad aggiungere il prossimo elemento
				// L=0 {e1}	 / {}	
				// L=1 {e1,e2}-{e1} / {e2}-{} 
				
		parziale.add(allEsami.get(livello));
		cercaMeglio(parziale, livello+1, numeroCrediti);
		parziale.remove(allEsami.get(livello));
		
		// provo ad non aggiungere il prossimo elemento
		
		/* manda avanti la ricorsione mettendo in prima posizione un elemento sempre diverso (da 0 a allEsami.size())
		funge anche da ciclo for
		*/
		cercaMeglio(parziale, livello+1, numeroCrediti); 
		return;
	}
	

	public double calcolaMedia(Set<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(Set<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}
