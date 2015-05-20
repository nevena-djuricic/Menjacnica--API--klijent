package rs.fonis.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import rs.fonis.Valuta;

public class JsonRatesAPIKomunikacija {

	// http://jsonrates.com/get/?from=EUR&to=RSD&apiKey=jr-ba8999934fc5a7ab64a4872fb4ed9af7

	String appKey = "jr-ba8999934fc5a7ab64a4872fb4ed9af7";
	String jsonRatesURL = "http://jsonrates.com/get/?from=EUR&to=RSD&apiKey=jr-ba8999934fc5a7ab64a4872fb4ed9af7";

	private Valuta[] vratiIznosKurseva (String[] nazivi, String from, String to) {
		Valuta[] valute = new Valuta[nazivi.length];

		for (int i = 0; i < nazivi.length; i++) {
			
			String url = jsonRatesURL + "?" +
					"from=" + from +
					"&to=" + to +
					"&apiKey=" + appKey;

			try {
				String rezultat = sendGet(url);

				Gson gson = new GsonBuilder().create();
				JsonObject jsonResult = (JsonObject) gson.fromJson(rezultat, JsonObject.class);

				Valuta nova = new Valuta();
				nova.setNaziv(nazivi[i]);
				nova.setKurs(Double.parseDouble(jsonResult.get("rate").getAsString()));
				
				valute[i] = nova;
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return valute;
		
	}

	private String sendGet(String url) throws Exception {
		URL objekat = new URL(url);
		HttpURLConnection konekcija = (HttpURLConnection) objekat.openConnection();

		konekcija.setRequestMethod("GET");

		BufferedReader in = new BufferedReader(
				new InputStreamReader(konekcija.getInputStream()));

		boolean kraj = false;
		String odgovor = "";

		while (!kraj) {
			String s = in.readLine();

			if (s != null) {
				odgovor += s;
			} else {
				kraj = true;
			}
		}
		in.close();

		return odgovor.toString();
	}

}
