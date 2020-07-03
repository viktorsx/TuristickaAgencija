import model.korisnici.Agent;
import model.korisnici.Korisnik;
import utils.Model;
import utils.PomocnaKlasa;

import java.util.ArrayList;

public class Autentifikacija {

    public Autentifikacija(){

    }

    public Boolean autentifikuj(String korisnicko_ime, String sifra){
        ArrayList<Korisnik> korisnici = Model.getInstance().getKorisnici();
        //Proverava ko pokusava da se uloguje
        for(Korisnik k : korisnici){
        	//Pitamo se da li je dobra sifra i korisnicko ime
            if(k.getIme().equals(korisnicko_ime) && k.getSifra().equals(sifra)){
            	//Proveravamo da li je ulogovan agent, ako nije onda je klijent
                if(k instanceof Agent){
                	//
                    CurrentSession.getInstance().setKlijent(false);
                    CurrentSession.getInstance().setKorisnik(k);
                }else{
                    CurrentSession.getInstance().setKlijent(true);
                    CurrentSession.getInstance().setKorisnik(k);
                }
                StateMachine.getInstance().setState(State.MENI_PRIJAVLJEN);
                return true;
            }
        }
        System.out.println("Pogresno ime ili lozinka!");
        return false;
    }
}
