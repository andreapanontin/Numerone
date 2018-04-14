/**
* TODO:
* * Implementare somma con numeri negativi;
* * Implementare toString con numeri negativi
* * Implementare ciclo su un numerone
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
		
		if (!segno) {
			tS = tS + "-";
		}
		
		for (int j=0; j < numerini.length; j++) {
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
					for (int h = 1; h < 9-logaritmo(numerini[j], 10); h++) {
						tS = tS + "0";
					}
					//concatena gli elementi per stampare un numero singolo
					tS = tS + numerini[j];
				} else {
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
		
		if (Numerone.xor(fattore1.getSegno(), fattore2.getSegno())) {
			if (fattore1.getSegno()) {
				Numerone fattore3 = new Numerone(fatt2, true);
				risultato = Numerone.differenza(fattore1, fattore3);
			} else {
				Numerone fattore3 = new Numerone(fatt1, true);
				risultato = Numerone.differenza(fattore2, fattore3);
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
			
			if (temp[0] == 0) {
				long[] temp2 = new long[lunghezza-1];
				
				for (int i = 0; i < temp2.length; i++) {
					temp2[i] = temp[i+1];
				}
				
				temp = temp2;
			}
			
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
		
		if (Numerone.xor(fattore1.getSegno(), fattore2.getSegno())) {
			Numerone fattore3 = new Numerone(fatt2, !fattore2.getSegno());
			risultato = Numerone.somma(fattore1, fattore3);
		} else {
			segnoRisultato = fattore1.getSegno();
			Numerone fattore1temp = new Numerone(fatt1, true);
			Numerone fattore2temp = new Numerone(fatt2, true);
			if (fattore2temp.isGreater(fattore1temp)) {
				segnoRisultato = !fattore1.getSegno();
				long[] fatt3 = fatt2;
				fatt2 = fatt1;
				fatt1 = fatt3;
				fatt3 = null;
			}
			
			int lunghezza = fatt1.length;
			int delta = fatt1.length - fatt2.length;
			long[] temp = new long[lunghezza];
			
			for (int j = 0; j < delta; j++) {
				temp[j] = fatt1[j];
			}
			
			for (int j = 0; j < fatt2.length; j++) {
				temp[j + delta] = fatt1[j + delta] - fatt2[j];
				
				if (temp[j + delta] < 0) {
					temp[j + delta - 1] -= 1;
					temp[j + delta] = CONTROLLO + temp[j + delta];
				}
			}
			
			risultato = new Numerone(temp, segnoRisultato);
		}
		
		return risultato;
	}
	
	public Numerone differenza(Numerone fattore) {
		return this.differenza(this, fattore);
	}
	
	public static boolean equals(Numerone fattore1, Numerone fattore2) {
		boolean uguali = true;
		long[] fatt1, fatt2;
		fatt1 = fattore1.getNumerini();
		fatt2 = fattore2.getNumerini();
		
		if (Numerone.xor(fattore1.getSegno(), fattore2.getSegno())) {
			uguali = false;
		} else if (fatt1.length != fatt2.length) {
			uguali = false;
		} else {
			for (int i = 0; i < fatt1.length && uguali; i++) {
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
		
		if (Numerone.equals(fattore1, fattore2)) {
			maggiore = false;
		} else {
			if (segno1) {
				if (segno2) {
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
				if (segno2) {
					maggiore = false;
				} else {
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
		
		if (Numerone.equals(fattore1, fattore2)) {
			minore = false;
		} else {
			minore = !Numerone.isGreater(fattore1, fattore2);
		}
		
		return minore;
	}
	
	public boolean isLower(Numerone fattore) {
		return this.isLower(this, fattore);
	}
	
	private long logaritmo (long argomento, long base) {
		long risultato = (long) (Math.log(argomento) / Math.log(base));
		return risultato;
	}
	
	private static boolean xor(boolean a1, boolean a2) {
		return (!(a1 & a2) && (a1 | a2));
	}
	
	public static void main(String[] args) {
		Numerone numero0 = new Numerone(-100000000);
		Numerone numero1 = new Numerone(100000000);
		Numerone numero2 = new Numerone(100000000);
		Numerone numero3 = new Numerone(1000000);
		
		Numerone numero = Numerone.somma(numero0,numero1);
		numero = numero.somma(numero0);
		System.out.println(numero0);
		
		System.out.println(numero0.equals(numero1));
		System.out.println(numero1.equals(numero2));
		System.out.println(numero0.equals(numero0));
		System.out.println(Numerone.isGreater(numero1, numero0));
		System.out.println(Numerone.isGreater(numero0, numero1));
		System.out.println(Numerone.isGreater(numero0, numero0));
		System.out.println(Numerone.isGreater(numero3, numero2));
		System.out.println(numero);
		
		for (int i = 0; i < 12; i++) {
			numero = Numerone.somma(numero, numero1);
			System.out.println(numero);
		}
		
		
		System.out.println(Numerone.isGreater(numero, numero0));
		System.out.println(Numerone.isGreater(numero0, numero));
		System.out.println(Numerone.isGreater(numero0, numero0));
		System.out.println(Numerone.isGreater(numero3, numero));
	}
}
