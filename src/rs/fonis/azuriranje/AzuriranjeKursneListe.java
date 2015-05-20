package rs.fonis.azuriranje;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.GregorianCalendar;
import java.util.LinkedList;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import rs.fonis.Valuta;
import rs.fonis.util.JsonRatesAPIKomunikacija;

public class AzuriranjeKursneListe {

	private final String putanjaDoFajlaKursnaLista = "data/kursnaLista.json";
	private LinkedList<Valuta> kursevi;
	
	
	@SuppressWarnings("unchecked")
	public LinkedList<Valuta> ucitajValute() {
		try {
			ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(
					new FileInputStream(putanjaDoFajlaKursnaLista)));
			
			kursevi = (LinkedList<Valuta>)(in.readObject());
			
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return kursevi;
	}
	
	public void upisiValute(LinkedList<Valuta> kursevi, GregorianCalendar datum) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(
					new FileOutputStream(putanjaDoFajlaKursnaLista)));
			
			JsonObject datumJson = new JsonObject();
			datumJson.addProperty("datum", datum.toString());
			
			out.writeObject(datumJson);
			
			JsonArray kurseviArray = new JsonArray();
			
			for (int i = 0; i < kursevi.size(); i++) {
				JsonObject kursJson = new JsonObject();
				
				kursJson.addProperty("naziv", kursevi.get(i).getNaziv());
				kursJson.addProperty("kurs", kursevi.get(i).getKurs());
				
				kurseviArray.add(kursJson);
			}
			
			out.writeObject(kurseviArray);
			
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void azurirajValute() {
		LinkedList<Valuta> noviKursevi = ucitajValute();
		
		String[] naziviKurseva = new String[kursevi.size()];
		
		for (int i = 0; i < naziviKurseva.length; i++) {
			naziviKurseva[i] = kursevi.get(i).getNaziv();
		}
		
		Valuta[] azuriraneValute = JsonRatesAPIKomunikacija.vratiIznosKurseva(naziviKurseva);
		
		for (int i = 0; i < azuriraneValute.length; i++) {
			if (!noviKursevi.contains(azuriraneValute[i])) {
				noviKursevi.add(azuriraneValute[i]);
			}
		}
		
		upisiValute(noviKursevi, new GregorianCalendar());
	}
	
}
