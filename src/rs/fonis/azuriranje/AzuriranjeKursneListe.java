package rs.fonis.azuriranje;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.GregorianCalendar;
import java.util.LinkedList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import rs.fonis.Valuta;
import rs.fonis.komunikacija.JsonRatesAPIKomunikacija;

public class AzuriranjeKursneListe {

	private final String putanjaDoFajlaKursnaLista = "data/kursnaLista.json";

	@SuppressWarnings("resource")
	public LinkedList<Valuta> ucitajValute() {
		LinkedList<Valuta> kursnaLista = new LinkedList<Valuta>();
		try {
			FileReader in = new FileReader(putanjaDoFajlaKursnaLista);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();

			JsonReader reader = new JsonReader(new StringReader(""));
			reader.setLenient(true);
			
			JsonObject kursJson = gson.fromJson(in, JsonObject.class);
			JsonArray kurseviArray = kursJson.get("valute").getAsJsonArray();
			
			for (int i = 0; i < kurseviArray.size() ; i++) {
				Valuta v = new Valuta();
				v.setKurs(kurseviArray.get(i).getAsJsonObject().get("kurs").getAsDouble());
				v.setNaziv(kurseviArray.get(i).getAsJsonObject().get("naziv").getAsString());
				
				kursnaLista.add(v);	
			}

			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return kursnaLista;
	}

	@SuppressWarnings("static-access")
	public void upisiValute(LinkedList<Valuta> valute, GregorianCalendar calendar) {
		try {
			calendar = new GregorianCalendar();

			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(putanjaDoFajlaKursnaLista)));
			
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonObject kurseviJson = new JsonObject();
			
			kurseviJson.addProperty("datum", 
					calendar.get(calendar.DAY_OF_MONTH) + "." + (calendar.get(calendar.MONTH)+1) + "." + calendar.get(calendar.YEAR) + ".");

			JsonArray valuteJson = new JsonArray();
			for (int i = 0; i < valute.size(); i++) {
				JsonObject valutaJson = new JsonObject();
				valutaJson.addProperty("naziv", valute.get(i).getNaziv());
				valutaJson.addProperty("kurs", valute.get(i).getKurs());
				
				valuteJson.add(valutaJson);
			}
			
			kurseviJson.add("valute", valuteJson);
			
			out.println(gson.toJson(kurseviJson));

			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void azurirajValute() {
		LinkedList<Valuta> noviKursevi = ucitajValute();

		String[] naziviKurseva = new String[noviKursevi.size()];

		for (int i = 0; i < naziviKurseva.length; i++) {
			naziviKurseva[i] = noviKursevi.get(i).getNaziv();
		}

		LinkedList<Valuta> azuriraneValute = JsonRatesAPIKomunikacija.vratiIznosKurseva(naziviKurseva);

		for (int i = 0; i < noviKursevi.size(); i++) {
			if (!azuriraneValute.contains(noviKursevi.get(i))) {
				azuriraneValute.add(noviKursevi.get(i));
			}
		}

		upisiValute(azuriraneValute, new GregorianCalendar());
	}

}
