package rs.fonis.komunikacija;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import rs.fonis.Valuta;

public class JsonRatesAPIKomunikacija {

	// http://jsonrates.com/get/?from=EUR&to=RSD&apiKey=jr-ba8999934fc5a7ab64a4872fb4ed9af7

	static final String appKey = "jr-ba8999934fc5a7ab64a4872fb4ed9af7";
	static final String jsonRatesURL = "http://jsonrates.com/get/";

	public static LinkedList<Valuta> vratiIznosKurseva (String[] nazivi) {
		LinkedList<Valuta> valute = new LinkedList<Valuta>();
		String url;
		
		for (int i = 0; i < nazivi.length; i++) {
			for (int j = 0; j < nazivi.length; j++) {
				
				if (i == j) continue;
				
				url = ispisi(nazivi[i], nazivi[j]);
				
				try {
					String rezultat = sendGet(url);

					Gson gson = new GsonBuilder().create();
					JsonObject jsonResult = (JsonObject) gson.fromJson(rezultat, JsonObject.class);

					Valuta nova = new Valuta();
					nova.setNaziv(nazivi[i]);
					nova.setKurs(Double.parseDouble(jsonResult.get("rate").getAsString()));

					valute.add(nova);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return valute;

	}

	private static String ispisi(String from, String to) {
		String urlAdresa = jsonRatesURL + "?" +
				"from=" + from +
				"&to=" + to +
				"&apiKey=" + appKey;

		return urlAdresa;
	}

	private static String sendGet(String url) throws Exception {
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
