package rs.fonis;

public class Valuta {

	private String naziv;
	private double kurs;
	
	public String getNaziv() {
		return naziv;
	}
	
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	
	public double getKurs() {
		return kurs;
	}
	
	public void setKurs(double kurs) {
		this.kurs = kurs;
	}

	public String toString() {
		return "Valuta [naziv=" + naziv + ", kurs=" + kurs + "]";
	}
	
	public boolean equals(Object o) {
		if (o instanceof Valuta) {
			Valuta v = (Valuta) (o);
			if (naziv.equals(v.getNaziv()) && kurs == v.getKurs())
				return true;
		}
		return false;
	}
	
}
