/**
* TODO:
* * Commenta i pezzi di codice che non sono ancora commentati!
* * Modificare Costruttore di default a costrutore interattivo... Devi cercare
*   bene come si posssa Implementare: non credo sia una bnalita'
* * Implementare prodotto tra due numeroni
* * Implementare Fattoriale con un numerone in input (forse non e' la migliore
*   idea...)
* * Implementare potenza con due numeroni in input
*/ 

public class Numerone {
	private long[] numerini;
	// True: numerone positivo; false: numerone negativo; zero è positivo...
	private boolean segno;
	public static final long CONTROLLO = 1000000000;
	
	public Numerone(long valoreIniziale) {
		if (valoreIniziale < CONTROLLO) {
			numerini = new long[1];
			numerini[0] = Math.abs(valoreIniziale);
		} else {
			numerini = new long[2];
			numerini[0] = Math.abs(valoreIniziale) / CONTROLLO;
			numerini[1] = Math.abs(valoreIniziale) % CONTROLLO;
		}
		
		segno = (valoreIniziale + Math.abs(valoreIniziale) != 0 || valoreIniziale == 0);
	}
	
	public Numerone() {
		this(0);
	}
	
	/* Devi ASSOLUTAMENTE sistemare questo metodo */
	public Numerone(long[] valoreIniziale, boolean segno) {
		numerini = new long[valoreIniziale.length];
		
		for (int i = 0; i < numerini.length; i++) {
			numerini[i] = valoreIniziale[i];
		}
		
		this.segno = segno;
	}
	
	public long[] getNumerini() {
		long[] risultato = new long[numerini.length];
		for (int i = 0; i < risultato.length; i++) {
			risultato[i] = numerini[i];
		}
		
		return risultato;
	}
	
	public boolean getSegno() {
		return segno;
	}
	
	/* Decidi se stampare + se il numero è strettamente positivo */
	public String toString() {
		boolean inizio = false;
		String tS = "";
		
		// Se il numero è negativo stampo "-" davanti al numero
		if (!segno) {
			tS = tS + "-";
		}
		
		for (int j=0; j < numerini.length; j++) {
			// Finché gli elementi di numerone sono nulli => non è ancora
			// iniziato il numero
			if (!inizio) {
				if (numerini[j] == 0) {
					continue;
				} else {
					inizio = true;
					
					/* Questa riga stampa + se il numero e' positivo
					if (segno) {
						tS = tS + "+";
					} */
					
					tS = tS + numerini[j];
				}
			} else {
				if (numerini[j]!=0) {
					// Stampo un numero di 0 adeguato a riempire le nove cifre
					// reppresentate da ogni elemento dell'array
					for (int h = 1; h < 9-logaritmo(numerini[j], 10); h++) {
						tS = tS + "0";
					}
					tS = tS + numerini[j];
				} else {
					// Se l'elemento è 0 => stampo 0 (nota: il numero è già
					// iniziato!)
					tS = tS + "00000000" + numerini[j];
				}
			}
		}
		
		// Stampa "0" se il numerone rappresenta zero
		if (!inizio) {
			tS = "0";
		}
		
		return tS;
	}
	
	public static Numerone somma(Numerone fattore1, Numerone fattore2) {
		Numerone risultato;
		long[] fatt1 = fattore1.getNumerini(), fatt2 = fattore2.getNumerini();
		
		if (xor(fattore1.getSegno(), fattore2.getSegno())) {
			if (fattore1.getSegno()) {
				Numerone fattore3 = new Numerone(fatt2, true);
				risultato = differenza(fattore1, fattore3);
			} else {
				Numerone fattore3 = new Numerone(fatt1, true);
				risultato = differenza(fattore2, fattore3);
			}
		} else {
			if (fatt1.length < fatt2.length) {
				long[] fatt3 = fatt2;
				fatt2 = fatt1;
				fatt1 = fatt3;
				fatt3 = null;
			}
			int lunghezza = fatt1.length + 1;
			long[] temp = new long[lunghezza];
			long riporto = 0;
		
			for (int j = 1; j <= fatt2.length; j++) {
				temp[lunghezza - j] += fatt1[fatt1.length - j] + fatt2[fatt2.length - j];
				temp[lunghezza - j] += riporto;
				
				if (temp[lunghezza - j] < CONTROLLO) {
					riporto = 0;
				} else {
					riporto = temp[lunghezza - j] / CONTROLLO;
					temp[lunghezza - j] = temp[lunghezza - j] % CONTROLLO;
				}
			}
			
			for (int j = fatt2.length + 1; j <= fatt1.length; j++) {
				temp[lunghezza - j] += fatt1[fatt1.length - j];
				temp[lunghezza - j] += riporto;
				
				if (temp[lunghezza - j] < CONTROLLO) {
					riporto = 0;
				} else {
					riporto = temp[lunghezza - j] / CONTROLLO;
					temp[lunghezza - j] = temp[lunghezza - j] % CONTROLLO;
				}
			}
			
			temp[0] = riporto;
			
			// Elimino eventuali celle nulle del vettore, per evitare
			// di consumare troppa memoria
			temp = sanitize(temp);
			
			risultato = new Numerone(temp, fattore1.getSegno());
		}
		
		return risultato;
	}
	
	public Numerone somma(Numerone fattore) {
		return this.somma(this, fattore);
	}
	
	/* Appena sistemato... Non l'ho ancora testato però */
	public static Numerone differenza(Numerone fattore1, Numerone fattore2) {
		Numerone risultato;
		boolean segnoRisultato;
		long[] fatt1 = fattore1.getNumerini(), fatt2 = fattore2.getNumerini();
		
		// Se i due fattori hanno segni diversi => devo fare una somma, 
		// invertendo il segno del secondo fattore
		if (xor(fattore1.getSegno(), fattore2.getSegno())) {
			Numerone fattore3 = new Numerone(fatt2, !fattore2.getSegno());
			risultato = somma(fattore1, fattore3);
		} else {
			// Se il primo fattore è maggiore (in vaole assoluto) del secondo =>
			// il segno del risultato coincide con quello del primo fattore
			segnoRisultato = fattore1.getSegno();
			Numerone fattore1temp = new Numerone(fatt1, true);
			Numerone fattore2temp = new Numerone(fatt2, true);
			if (fattore2temp.isGreater(fattore1temp)) {
				// Altrimenti è il segno opposto
				segnoRisultato = !fattore1.getSegno();
				
				// Inoltre inverto i fattori in quanto il mio algoritmo
				// presuppone che il fattore più grande sia il primo
				long[] fatt3 = fatt2;
				fatt2 = fatt1;
				fatt1 = fatt3;
				fatt3 = null;
			}
			
			int lunghezza = fatt1.length;
			int delta = fatt1.length - fatt2.length;
			long[] temp = new long[lunghezza];
			
			// Popolo i primi elementi del risultato con i valori di fattore1
			for (int j = 0; j < delta; j++) {
				temp[j] = fatt1[j];
			}
			
			// Calcolo la differenza
			for (int j = 0; j < fatt2.length; j++) {
				temp[j + delta] = fatt1[j + delta] - fatt2[j];
				
				if (temp[j + delta] < 0) {
					temp[j + delta - 1] -= 1;
					temp[j + delta] = CONTROLLO + temp[j + delta];
				}
			}
			
			// Elimino eventuali celle nulle del vettore, per evitare
			// di consumare troppa memoria
			temp = sanitize(temp);
			risultato = new Numerone(temp, segnoRisultato);
		}
		
		return risultato;
	}
	
	public Numerone differenza(Numerone fattore) {
		return differenza(this, fattore);
	}
	
	// Just a stab. Not yet implemented!
	public static Numerone fattoriale(Numerone fattore) {
		Numerone zero, menomeno, risultato;
		long[] temp;
		zero = new Numerone();
		menomeno = new Numerone(-1);
		risultato = new Numerone();
		
		if (fattore.isGreater(zero)) {
			while (!fattore.equals(zero)) {
				for (int j=risultato.getNumerini().length-1; j>=0; j--) {
					// Devi riadattare il codice che avevi Implementato in 
					// fattoriale Steroidi sulla base del metodo prodotto
				}
				
				fattore = fattore.somma(menomeno);
			}
		}
		
		return risultato;
	}
	
	public static boolean equals(Numerone fattore1, Numerone fattore2) {
		boolean uguali = true;
		long[] fatt1, fatt2;
		fatt1 = fattore1.getNumerini();
		fatt2 = fattore2.getNumerini();
		
		// Se i due numeri hanno segno discorde => sono diversi
		if (xor(fattore1.getSegno(), fattore2.getSegno())) {
			uguali = false;
		} else if (fatt1.length != fatt2.length) {
			// Se non hanno la stessa lunghezza => sono diversi
			uguali = false;
		} else {
			for (int i = 0; i < fatt1.length && uguali; i++) {
				// Se esiste un indice in cui differiscono => sono diversi
				if (fatt1[i] != fatt2[i])
					uguali = false;
			}
		}
		
		return uguali;
	}
	
	public boolean equals(Numerone fattore) {
		return this.equals(fattore, this);
	}
	
	public static boolean isGreater(Numerone fattore1, Numerone fattore2) {
		boolean segno1, segno2, maggiore = true;
		long[] fatt1, fatt2;
		fatt1 = fattore1.getNumerini();
		fatt2 = fattore2.getNumerini();
		segno1 = fattore1.getSegno();
		segno2 = fattore2.getSegno();
		
		// Se sono uguali => il primo non è maggiore del secondo (MDS)
		if (equals(fattore1, fattore2)) {
			maggiore = false;
		} else {
			if (segno1) {
				if (segno2) {
					// Se sono entrambi positivi => il primo MDS <=> tutti i
					// suoi elementi sono MDS
					if (fatt1.length < fatt2.length) {
						maggiore = false;
					} else if (fatt1.length > fatt2.length) {
						maggiore = true;
					} else {
						for (int i = 0; maggiore && i < fatt1.length; i++) {
							if (fatt1[i] < fatt2[i]) {
								maggiore = false;
							}
						}
					}
				}
			} else {
				// Il primo ha segno negativo
				if (segno2) {
					// E il secondo positivo => primo non MDS
					maggiore = false;
				} else {
					// secondo negativo => inverso di entrambi positivi
					if (fatt2.length < fatt1.length) {
						maggiore = false;
					} else if (fatt2.length > fatt1.length) {
						maggiore = true;
					} else {
						for (int i = 0; maggiore && i < fatt1.length; i++) {
							if (fatt2[i] < fatt1[i]) {
								maggiore = false;
							}
						}
					}
				}
			}
		}
		
		return maggiore;
	}
	
	public boolean isGreater(Numerone fattore) {
		return this.isGreater(this, fattore);
	}
	
	public static boolean isLower(Numerone fattore1, Numerone fattore2) {
		boolean minore = true;
		
		// Si spiega da solo
		if (equals(fattore1, fattore2)) {
			minore = false;
		} else {
			minore = !isGreater(fattore1, fattore2);
		}
		
		return minore;
	}
	
	public boolean isLower(Numerone fattore) {
		return this.isLower(this, fattore);
	}
	
	// E ora i metodi ausiliari!, nulla di che, solo ogni tanto risultano utili!
	
	private long logaritmo (long argomento, long base) {
		long risultato = (long) (Math.log(argomento) / Math.log(base));
		return risultato;
	}
	
	private static boolean xor(boolean a1, boolean a2) {
		return (!(a1 & a2) && (a1 | a2));
	}
	
	private static long[] sanitize(long[] input) {
		int j = 0;
		while (j < (input.length - 1) && input[j] == 0) {
			j++;
		}
		
		long[] output = new long[input.length-j];
		
		for (int i = 0; i < output.length; i++) {
			output[i] = input[i+j];
		}
		
		return output;
	}
	
	public static void main(String[] args) {
		Numerone numero0 = new Numerone(-1000000000);
		Numerone numero1 = new Numerone(1000000000);
		
		System.out.println(numero1);
		System.out.println(numero1.getNumerini().length);
		
		for (int i = 0; i < 100; i++) {
			numero0 = numero0.somma(numero0);
			numero1 = numero1.somma(numero1);
		System.out.println(numero1);
		System.out.println(numero1.getNumerini().length);
		}
		
		Numerone numero = Numerone.somma(numero0, numero1);
		System.out.println(numero);
		long[] temp = numero.getNumerini();
		System.out.println(temp.length);
	}
}
