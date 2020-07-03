package utils;

import model.adresa.Adresa;
import model.adresa.Drzava;
import model.adresa.Mesto;
import model.korisnici.Korisnik;
import model.smestaj.Apartman;
import model.smestaj.Hotel;
import model.zakupljivanje.Aranzman;
import model.zakupljivanje.Paket;

import java.io.IOException;
import java.util.ArrayList;

public class Model {

    private ArrayList<Adresa> adrese;
    private ArrayList<Drzava> drzave;
    private ArrayList<Korisnik> korisnici;
    private ArrayList<Mesto> mesta;
    private ArrayList<Apartman> apartmani;
    private ArrayList<Hotel> hoteli;
    private ArrayList<Paket> paketi;

    private ArrayList<Aranzman> aranzmani;

    private static Model instance = null;

    public static Model getInstance(){
        if(instance ==null){
            instance = new Model();
            return instance;
        }

        return instance;
    }

    private Model(){
        this.adrese = PomocnaKlasa.citajIzFajlaAdresa("fajlovi/adrese");
        this.drzave = PomocnaKlasa.citajIzFajlaDrzava("fajlovi/drzave");
        this.korisnici = PomocnaKlasa.citajIzFajlaKorisnik("fajlovi/korisnici");
        this.mesta = PomocnaKlasa.citajIzFajlaMesto("fajlovi/mesta");
        this.apartmani = PomocnaKlasa.citajIzFajlaApartmani("fajlovi/apartmani");
        this.hoteli = PomocnaKlasa.citajIzFajlaHotel("fajlovi/hoteli");
        this.aranzmani = PomocnaKlasa.citajIzFajlaAranzmani("fajlovi/aranzmani");
        this.paketi = PomocnaKlasa.citajIzFajlaPaketi("fajlovi/paketi");
    }

    public void snimiModel() throws IOException {
        PomocnaKlasa.upisiUFajlAdresa(adrese);
        PomocnaKlasa.upisiUFajlDrzave(drzave);
        PomocnaKlasa.upisiUFajlKorisnik(korisnici);
        PomocnaKlasa.upisiUFajlMesta(mesta);
        PomocnaKlasa.upisiUFajlApartmani(apartmani);
        PomocnaKlasa.upisiUFajlHotel(hoteli);
        PomocnaKlasa.upisiUFajlAranzmani(aranzmani);
        PomocnaKlasa.upisiUFajlPaketi(paketi);
    }

    public ArrayList<Adresa> getAdrese() {
        return adrese;
    }

    public void setAdrese(ArrayList<Adresa> adrese) {
        this.adrese = adrese;
    }

    public ArrayList<Drzava> getDrzave() {
        return drzave;
    }

    public void setDrzave(ArrayList<Drzava> drzave) {
        this.drzave = drzave;
    }

    public ArrayList<Korisnik> getKorisnici() {
        return korisnici;
    }

    public void setKorisnici(ArrayList<Korisnik> korisnici) {
        this.korisnici = korisnici;
    }

    public ArrayList<Mesto> getMesta() {
        return mesta;
    }

    public void setMesta(ArrayList<Mesto> mesta) {
        this.mesta = mesta;
    }

    public static void setInstance(Model instance) {
        Model.instance = instance;
    }

    public ArrayList<Apartman> getApartmani() {
        return apartmani;
    }

    public void setApartmani(ArrayList<Apartman> apartmani) {
        this.apartmani = apartmani;
    }

    public ArrayList<Hotel> getHoteli() {
        return hoteli;
    }

    public void setHoteli(ArrayList<Hotel> hoteli) {
        this.hoteli = hoteli;
    }

    public ArrayList<Aranzman> getAranzmani() {
        return aranzmani;
    }

    public void setAranzmani(ArrayList<Aranzman> aranzmani) {
        this.aranzmani = aranzmani;
    }

    public ArrayList<Paket> getPaketi() {
        return paketi;
    }

    public void setPaketi(ArrayList<Paket> paketi) {
        this.paketi = paketi;
    }
}
