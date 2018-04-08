package Parser;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import com.maxmind.geoip2.*;
import com.maxmind.geoip2.exception.*;
import com.maxmind.geoip2.model.*;

public class Geo {
	CityResponse location;
	InetAddress ip;
	String ville;
	String pays;
	double lat;
	double lon;
	DatabaseReader dbPays;
	DatabaseReader dbASN;
	
	public Geo(String ip) throws IOException, GeoIp2Exception{
		dbPays = buildDBPays();
		dbASN = buildDBASN();
		this.ip = InetAddress.getByName(ip);
		location = dbPays.city(this.ip);
		pays = location.getCountry().getName();
		ville = location.getCity().getName();
		lat = location.getLocation().getLatitude();
		lon = location.getLocation().getLongitude();
	}

	public DatabaseReader buildDBPays() throws IOException{
		File database = new File("/Users/simon/Documents/Projet_Synthèse/City.mmdb");
		return new DatabaseReader.Builder(database).build();
	}
	
	public DatabaseReader buildDBASN() throws IOException{
		File database = new File("/Users/simon/Documents/Projet_Synthèse/ASN.mmdb");
		return new DatabaseReader.Builder(database).build();
	}
}
