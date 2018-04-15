/**
* TODO:
* * Commenta i pezzi di codice che non sono ancora commentati!
* * Modificare Costruttore di default a costrutore interattivo... Devi cercare
*   bene come si posssa Implementare: non credo sia una banalita'
* * Implementare potenza con due numeroni in input
*/

public class Numerone {
	// Questo e' l'array che contiene tutti i vari elementi di Numerone
	private long[] numerini;
	// True: numerone positivo; false: numerone negativo; zero è positivo...
	private boolean segno;
	// CONTROLLO e' il valore che ogni cella di numerini NON DEVE MAI superare
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
		
		segno = (valoreIniziale + Math.abs(valoreIniziale) != 0
		|| valoreIniziale == 0);
	}
	
	public Numerone() {
		this(0);
	}
	
	/* Devi ASSOLUTAMENTE sistemare questo metodo */
	public Numerone(long[] valoreIniziale, boolean segno) {
		numerini = new long[valoreIniziale.length];
		
		for (int i = numerini.length - 1; i >= 0
			&& valoreIniziale[i] < CONTROLLO; i--) {
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
					// Stampa un numero di 0 adeguato a riempire le nove cifre
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
		
		// Stampa "0" se il numerone rappresenta il numero zero
		if (!inizio) {
			tS = "0";
		}
		
		return tS;
	}
	
	public static Numerone somma(Numerone fattore1, Numerone fattore2) {
		Numerone risultato;
		long[] fatt1 = fattore1.getNumerini(), fatt2 = fattore2.getNumerini();
		
		// Se i numeri in input ha segni discordi => eseguo una differenza
		// passando i fattori con segni opportuni
		if (xor(fattore1.getSegno(), fattore2.getSegno())) {
			if (fattore1.getSegno()) {
				Numerone fattore3 = new Numerone(fatt2, true);
				risultato = differenza(fattore1, fattore3);
			} else {
				Numerone fattore3 = new Numerone(fatt1, true);
				risultato = differenza(fattore2, fattore3);
			}
		} else {
			// Voglio il numero piu' lungo per primo, per evitare di ciclare
			// su celle non esistenti di un array (out of bound)
			if (fatt1.length < fatt2.length) {
				long[] fatt3 = fatt2;
				fatt2 = fatt1;
				fatt1 = fatt3;
				fatt3 = null;
			}
			
			// Nella somma il risultato puo' essere una cella piu' lunga degli
			// addendi, non di piu'
			int lunghezza = fatt1.length + 1;
			long[] temp = new long[lunghezza];
			long riporto = 0;
			
			// Sommo cella per cella secondo l'algoritmo che si insegna alle
			// elementari, nel primo ciclo sugli elementi di fattore2
			// (il numero piu' corto dei due)
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
			
			// Nel secondo ciclo completo gli elementi di fattore1
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
			
			// Aggiungo l'ultimo riporto (se esiste)
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
	
	public Numerone somma(long fattore) {
		Numerone input = new Numerone(fattore);
		return this.somma(this, input);
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
	
	public Numerone differenza(long fattore) {
		Numerone input = new Numerone(fattore);
		return this.differenza(this, input);
	}
	
	public static Numerone prodotto(Numerone fattore1, Numerone fattore2) {
		Numerone fattore, risultato;
		boolean segnoRisultato;
		long riporto = 0;
		long[] fatt, fatt1 = fattore1.getNumerini(), fatt2 = fattore2.getNumerini();
		long[][] prodotto = new long[fatt1.length][fatt2.length + 1];
		
		segnoRisultato = !xor(fattore1.getSegno(), fattore2.getSegno());
		fatt = new long[1];
		fatt[0] = 0;
		risultato = new Numerone(fatt, segnoRisultato);
		
		// Eseguo l'algoritmo di moltiplicazione che si insegna alle elementari
		// per blocchi di 9 cifre
		for (int j = 0; j < fatt1.length; j++) {
			for (int i = fatt2.length - 1; i >= 0; i--) {
				prodotto[j][i + 1] = fatt1[j] * fatt2[i];
				prodotto[j][i + 1] += riporto;
				
				riporto = prodotto[j][i + 1] / CONTROLLO;
				prodotto[j][i + 1] = prodotto[j][i + 1] % CONTROLLO;
			}
			prodotto[j][0] = riporto;
			riporto = 0;
		}
		
		// Sommo le righe della tabella per ottenere il risultato
		for (int j = fatt1.length - 1; j >= 0; j--) {
			fatt = new long[fatt2.length + fatt1.length - j];
			
			for (int i = 0; i < fatt2.length + 1; i++) {
				fatt[i] = prodotto[j][i];
			}
			
			fattore = new Numerone(fatt, segnoRisultato);
			risultato = risultato.somma(fattore);
		}
		
		return risultato;
	}
	
	public Numerone prodotto(Numerone fattore) {
		return prodotto(this, fattore);
	}
	
	public Numerone prodotto(long fattore) {
		Numerone input = new Numerone(fattore);
		return this.prodotto(input, this);
	}
	
	public static Numerone fattoriale(Numerone fattore) {
		Numerone zero, menomeno, risultato;
		zero = new Numerone(0);
		menomeno = new Numerone(-1);
		risultato = new Numerone(1);
		
		// Per ciclare su un Numerone strettamente positivo lo sommo a "-1"
		// Tante volte quante ne servono per portarlo a 0. Nel ciclo poi
		// Implemento l'algoritmo per il calcolo del fattoriale
		if (fattore.isGreater(zero)) {
			while (!fattore.equals(zero)) {
				risultato = risultato.prodotto(fattore);
				
				fattore = fattore.somma(menomeno);
			}
		}
		
		return risultato;
	}
	
	public static Numerone fattoriale(long fattore) {
		Numerone risultato;
		risultato = new Numerone(1);
		
		// Posso ciclare su un long al posto che su un Numerone! Molto piu'
		// efficiente! Inoltre l'algoritmo prodotto(long) e' piu' efficiente
		// che il prodotto tra due numeroni
		if (fattore > 0) {
			for (int i = 1; i <= fattore; i++) {
				risultato = risultato.prodotto(i);
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
		return (!(a1 && a2) && (a1 | a2));
	}
	
	private static long[] sanitize(long[] input) {
		int j = 0;
		
		// Conto quante celle di input sono nulle
		while (j < (input.length - 1) && input[j] == 0) {
			j++;
		}
		
		// Creo un array lungo almeno 1, in cui non salvero' alcuna cella nulla
		// prima dell'inizio del numero
		long[] output = new long[input.length-j];
		
		// Popolo il nuovo array
		for (int i = 0; i < output.length; i++) {
			output[i] = input[i+j];
		}
		
		return output;
	}
	
	public static void main(String[] args) {
		/*Numerone numero0 = new Numerone(-111111111);
		Numerone numero1 = new Numerone(111111111);
		Numerone dieci = new Numerone(10);
		
		System.out.println(numero1);
		System.out.println(dieci.getNumerini().length);
		
		/*
		for (int i = 0; i < 100; i++) {
			numero0 = numero0.somma(numero0);
			numero1 = numero1.somma(numero1);
		System.out.println(numero1);
		System.out.println(numero1.getNumerini().length);
		}
		
		Numerone numero = Numerone.prodotto(numero0, numero1);
		for (int i = 0; i < 65; i++) {
			numero = Numerone.prodotto(numero, dieci);
		}
		System.out.println(numero);
		long[] temp = numero.getNumerini();
		System.out.println(temp.length);*/
		
		Numerone fattoriale;
		for (int i = 1; i < 100; i++) {
			System.out.println(i + "! = " + Numerone.fattoriale(i));
		}
		fattoriale = Numerone.fattoriale(123);
		System.out.println("123! = " + fattoriale);
		System.out.println(fattoriale.getNumerini().length);
		
		long x = 1999999999;
		x *= x;
		for (int i = 0; i < 10; i++) {
			x += x;
		}
		
		System.out.println(x);
		System.out.println(new Numerone(x));
	}
}
