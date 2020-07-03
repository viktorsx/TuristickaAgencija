import model.adresa.Adresa;
import model.adresa.Drzava;
import model.adresa.Mesto;
import model.korisnici.Klijent;
import model.prevozno_sredstvo.Avion;
import model.prevozno_sredstvo.KlasaAvionskogMesta;
import model.prevozno_sredstvo.KlasaVoznogMesta;
import model.prevozno_sredstvo.Voz;
import model.smestaj.Apartman;
import model.smestaj.Hotel;
import model.smestaj.TipPansiona;
import model.zakupljivanje.Aranzman;
import model.zakupljivanje.Paket;
import utils.Model;
import utils.PomocnaKlasa;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Meni {

    private Scanner in;
    private Autentifikacija autentifikacija;
    private CurrentSession currentSession;
    private static Aranzman aranzman = new Aranzman();

    private static Meni instance = null;

    public static Meni getInstance(){
        if(instance ==null){
            instance = new Meni();
            return instance;
        }

        return instance;
    }

    private Meni(){
        in = new Scanner(System.in);
        autentifikacija = new Autentifikacija();
    }

    public Boolean ispisiMeni(){
        System.out.print("Unesite korisnicko ime: ");
        String korisnicko_ime = in.next();
        System.out.print("Unesite sifru: ");
        String sifra = in.next();
        return autentifikacija.autentifikuj(korisnicko_ime,sifra);
    }

    void ispisiMeniUlogovan(){
        if(CurrentSession.getInstance().getKlijent()){
            System.out.println("------------------------");
            System.out.println("1 - Uplata novca");
            System.out.println("2 - Kreiranje paketa");
            System.out.println("3 - Zakupi paket");
            System.out.println("4 - Otkazi paket");
            System.out.println("0 - Odjavi se");
            System.out.println("------------------------");
            Integer number = in.nextInt();
            obradiKlijentUnos(number);
        }else{
            System.out.println("------------------------");
            System.out.println("1 - Kreiranje aranzmana");
            System.out.println("2 - Kreiranje paketa");
            System.out.println("3 - Brisanje aranzmana");
            System.out.println("0 - Odjavi se");
            System.out.println("------------------------");
            Integer number = in.nextInt();
            obradiAgentUnos(number);
        }
    }

    void ispisiUplataNovca(){
        System.out.println("Unesite svotu novca: ");
        String money = in.next();
        Klijent k = (Klijent) CurrentSession.getInstance().getKorisnik();
        try{
            double value = Double.parseDouble(money);
            if(value >= 0) {
                k.setRaspolozivoStanje(k.getRaspolozivoStanje() + value);
                StateMachine.getInstance().setState(State.MENI_PRIJAVLJEN);
                System.out.println("Uspesno uplaceno!");
                System.out.println("Raspolozivo stanje: " + k.getRaspolozivoStanje());
            }else {
                System.out.println("Morate uneti pozitivan broj!");
            }
        }catch (Exception e){
            System.out.println("Uneli ste svotu u pogresnom formatu (primer 1500.10)");
        }
    }

    void smestaj(){
        System.out.println("------------------------");
        System.out.println("1 - Apartman");
        System.out.println("2 - Hotel");
        System.out.println("0 - Vrati se nazad");
        System.out.println("------------------------");
        Integer tipSmestaja = in.nextInt();
        if(tipSmestaja == 1) {
            System.out.println("Rezervacija za apartman je u toku...");
            Apartman apartman = rezervacijaApartmana();
            Model.getInstance().getApartmani().add(apartman);
            aranzman.setSmestaj(apartman);
        }else if(tipSmestaja == 2){
            System.out.println("Rezervacija za hotel je u toku...");
            Hotel hotel = rezervacijaHotela();
            Model.getInstance().getHoteli().add(hotel);
            aranzman.setSmestaj(hotel);
        } else{
            StateMachine.getInstance().setState(State.KREIRANJE_ARANZMANA);
        }
    }

    void prevoz(){
        System.out.println("------------------------");
        System.out.println("Prevozno sredstvo");
        System.out.println("1 - Avion");
        System.out.println("2 - Voz");
        System.out.println("0 - Vrati se nazad");
        System.out.println("------------------------");
        Integer tipPrevoza = in.nextInt();
        if(tipPrevoza == 1){
            System.out.println("Rezervacija za avion je u toku...");
            Avion avion = rezervacijaAviona();
            aranzman.setPrevoznoSredstvo(avion);
        }else if(tipPrevoza == 2){
            System.out.println("Rezervacija za voz je u toku...");
            Voz voz = rezervacijaVoza();
            aranzman.setPrevoznoSredstvo(voz);
        }else
            StateMachine.getInstance().setState(State.KREIRANJE_ARANZMANA);
    }

    void datumiPolaskaIPovratka(){
        System.out.println("------------------------");
        System.out.println("Datumi polaska i povratka");
        System.out.println("1 - Polazak");
        System.out.println("2 - Povratak");
        System.out.println("0 - vrati se nazad");
        System.out.println("------------------------");
        Integer broj = in.nextInt();
        if(broj == 1){
            System.out.println("-----------DATUM POLASKA----------");
            aranzman.setDatumPolaska(biranjeDatuma());
            datumiPolaskaIPovratka();
        }else if(broj == 2){
            System.out.println("---------DATUM POVRATKA----------");
            aranzman.setDatumPovratka(biranjeDatuma());
            datumiPolaskaIPovratka();
        }else
            StateMachine.getInstance().setState(State.KREIRANJE_ARANZMANA);
    }

    void osiguranje(){
        System.out.println("------------------------");
        System.out.println("Da li zelite putno osiguranje?");
        System.out.println("1 - Da, zelim");
        System.out.println("2 - Ne, ne zelim");
        System.out.println("0 - Vrati se nazad");
        System.out.println("------------------------");
        
        Integer broj = in.nextInt();
        while(broj != 1 && broj != 0 && broj != 2 ){
            System.out.println("1 za DA ili 2 za NE 0 za nazad");
            broj = in.nextInt();
        }
        if(broj == 1){
            aranzman.setSaOsiguranjem(true);
        }else
            aranzman.setSaOsiguranjem(false);

    }

    void grupni(){
        System.out.println("------------------------");
        System.out.println("Da li je u pitanju grupno putovanje?");
        System.out.println("1 - Da");
        System.out.println("2 - Ne");
        System.out.println("0 - vrati se nazad");
        System.out.println("------------------------");
        Integer broj = in.nextInt();
        while(broj != 1 && broj != 0 && broj != 2){
            System.out.println("1 za DA ili 2 za NE ili 0 za nazad");
            broj = in.nextInt();
        }
        if(broj == 1){
            aranzman.setGrupni(true);
        }else
            aranzman.setGrupni(false);

    }

    Date biranjeDatuma(){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        System.out.println("Unesite zeljeni datum po formatu:  dd-MM-yyyy ");
        String dateString = in.next();
        Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            System.out.println("Niste uneli dobar format datuma!");
        }
        return date;
    }

    Voz rezervacijaVoza(){
        Voz voz = new Voz();
        voz.setKlasaVoznogMesta(ispisiMeniTipVoznogMesta());
        System.out.println("Da li zelite obrok na putu? 1 za DA ili 0 za NE ");
        Integer obrok = in.nextInt();
        while(obrok != 1 && obrok != 0){
            System.out.println("1 za DA ili 0 za NE");
            obrok = in.nextInt();
        }
        if(obrok == 1){
            voz.setUkljucenObrok(true);
        }else
            voz.setUkljucenObrok(false);

        System.out.println("Unesite polaznu adresu");
        System.out.println("-------------------------");
        Adresa polazna = novaAdresa();
        voz.setPolaznaAdresa(polazna);

        System.out.println("Unesite odredisnu adresu");
        System.out.println("-------------------------");
        Adresa odredisna = novaAdresa();
        voz.setOdredisnaAdresa(odredisna);

        return voz;
    }

    KlasaVoznogMesta ispisiMeniTipVoznogMesta(){
        System.out.println("------------------------");
        System.out.println("Izaberite tip voznog mesta");
        System.out.println("1 - PRVI RAZRED");
        System.out.println("2 - DRUGI RAZRED");
        System.out.println("3 - SPAVACA KOLA");
        System.out.println("0 - vrati se nazad");
        System.out.println("------------------------");
        Integer broj = in.nextInt();
        if(broj == 1)
            return KlasaVoznogMesta.PRVI_RAZRED;
        else if(broj == 2)
            return KlasaVoznogMesta.DRUGI_RAZRED;
        else if(broj == 3)
            return KlasaVoznogMesta.SPAVACA_KOLA;
        else if(broj == 0)
            odradiKreiranjeAranzmana(2);
        return KlasaVoznogMesta.PRVI_RAZRED;
    }

    Avion rezervacijaAviona(){
        Avion avion = new Avion();
        avion.setKlasaAvionskogMesta(ispisiMeniTipAvionskogMesta());
        System.out.println("Da li zelite obrok na putu? 1 za DA ili 0 za NE ");
        Integer obrok = in.nextInt();
        while(obrok != 1 && obrok != 0){
            System.out.println("1 za DA ili 0 za NE");
            obrok = in.nextInt();
        }
        if(obrok == 1){
            avion.setUkljucenObrok(true);
        }else
            avion.setUkljucenObrok(false);

        System.out.println("Unesite polaznu adresu");
        System.out.println("-------------------------");
        Adresa polazna = novaAdresa();
        avion.setPolaznaAdresa(polazna);

        System.out.println("Unesite odredisnu adresu");
        System.out.println("-------------------------");
        Adresa odredisna = novaAdresa();
        avion.setOdredisnaAdresa(odredisna);

        return avion;
    }

    KlasaAvionskogMesta ispisiMeniTipAvionskogMesta(){
        System.out.println("------------------------");
        System.out.println("Izaberite tip avionskog mesta");
        System.out.println("1 - BIZNIS");
        System.out.println("2 - EKONOMSKA");
        System.out.println("0 - vrati se nazad");
        System.out.println("------------------------");
        Integer broj = in.nextInt();
        if(broj == 1)
            return KlasaAvionskogMesta.BIZNIS;
        else if(broj == 2)
            return KlasaAvionskogMesta.EKONOMSKA;
        else if(broj == 0)
            odradiKreiranjeAranzmana(2);
        return KlasaAvionskogMesta.EKONOMSKA;
    }

    Hotel rezervacijaHotela(){
        Hotel hotel = new Hotel();

        hotel.setTipPansiona(ispisiMeniTipPansiona());

        //unos broj kreveta
        System.out.println("Unesite zeljeni broj kreveta za vas smestaj");
        hotel.setBrojKreveta(in.nextInt());

        //unos povrsine smestaja
        System.out.println("Unesite povrsinu smestaja");
        hotel.setPovrsina(in.nextFloat());

        //unos adrese drzave i mesta
        hotel.setAdresa(ispisiMeniZaLokaciju());

        return hotel;
    }

    Apartman rezervacijaApartmana(){
        Apartman apartman = new Apartman();

        apartman.setTipPansiona(ispisiMeniTipPansiona());

        //unos broj kreveta
        System.out.println("Unesite zeljeni broj kreveta za vas smestaj");
        apartman.setBrojKreveta(in.nextInt());

        //unos povrsine smestaja
        System.out.println("Unesite povrsinu smestaja");
        apartman.setPovrsina(in.nextFloat());

        //unos adrese drzave i mesta
        apartman.setAdresa(ispisiMeniZaLokaciju());

        return apartman;
    }

    void ispisiMeniZakupljivanjePaketa(){
        ArrayList<Paket> paketi = new ArrayList<>();
        Klijent klijent = (Klijent) CurrentSession.getInstance().getKorisnik();
        System.out.println("------------------------");
        System.out.println("Slobodni paketi");
        int i = 0;
        for(Paket p: Model.getInstance().getPaketi()){
            if(p.getZakupio() == null){
                i++;
                paketi.add(p);
                System.out.println(i + " - " + p);
            }
        }
        System.out.println("0 - vrati se nazad");
        System.out.println("------------------------");
        Integer broj = in.nextInt();
        for(int j = 0; j < i+1 ; j++){
            if(broj == 0){
                StateMachine.getInstance().setState(State.MENI_PRIJAVLJEN);
            }else if(broj == j){
                System.out.println(paketi.get(j-1).zakupi());
                System.out.println("Da li zelite da zakupite paket? 1 za DA ili 0 za NE");
                Integer nekiInt = in.nextInt();
                while(nekiInt != 1 && nekiInt != 0) {
                    System.out.println("1 za DA ili 0 za NE");
                    nekiInt = in.nextInt();
                }
                if(nekiInt == 0){
                    StateMachine.getInstance().setState(State.MENI_PRIJAVLJEN);
                }else if (nekiInt == 1 && klijent.getRaspolozivoStanje() >= paketi.get(j-1).zakupi()) {
                    System.out.println("Uspesno ste zakupili paket, cestitam!");
                    paketi.get(j - 1).setZakupio(klijent);
                    klijent.setRaspolozivoStanje(klijent.getRaspolozivoStanje()-paketi.get(j-1).zakupi());
                    StateMachine.getInstance().setState(State.MENI_PRIJAVLJEN);
                }else if(nekiInt == 1 && klijent.getRaspolozivoStanje() < paketi.get(j-1).zakupi()){
                    System.out.println("Klijent nema dovoljno sredstava.");
                    StateMachine.getInstance().setState(State.MENI_PRIJAVLJEN);
                    break;
                }
            }
        }
    }

    void ispisimeniZaOtkazivanjePaketa(){
        ArrayList<Paket> paketi = new ArrayList<>();
        
        Klijent klijent = (Klijent) CurrentSession.getInstance().getKorisnik();
        System.out.println("------------------------");
        System.out.println("Koji paket zelite da obrisete?");
        int i = 0;
        for(Paket p: Model.getInstance().getPaketi()){
            if(p.getZakupio() == klijent){
                i++;
                paketi.add(p);
                System.out.println(i + " - " + p);
            }
        }
        System.out.println("0 - vrati se nazad");
        System.out.println("------------------------");
        Integer broj = in.nextInt();
        for(int j = 0; j < i+1 ; j++) {
            if (broj == 0) {
                StateMachine.getInstance().setState(State.MENI_PRIJAVLJEN);
            } else if (broj == j) {
                Double pare = paketi.get(j - 1).zakupi();
                klijent.setRaspolozivoStanje(klijent.getRaspolozivoStanje() + (pare / 100) * paketi.get(j - 1).getProcentualnaVrednostPovratkaNovca());
                paketi.get(j - 1).setZakupio(null);
                System.out.println("Uspesno ste otkazali paket, novac vam je vracen na racun!");
            }
        }
    }

    Adresa ispisiMeniZaLokaciju(){
        ArrayList<Adresa> adrese = Model.getInstance().getAdrese();
        System.out.println("------------------------");
        System.out.println("Adrese");
        int i = 0;
        for(Adresa a : adrese){
            i++;
            System.out.println(i + " - " + a);
        }
        System.out.println("0 - Smestaj na novoj adresi");
        System.out.println("------------------------");
        Integer broj = in.nextInt();
        for(int j = 0; j < i+1 ;j++){
            if(broj == 0){
            	return novaAdresa();
                
            }else if(broj == j){
            	return adrese.get(j-1);
            }
        }

        return new Adresa();
    }

    Adresa novaAdresa(){
        Adresa adresa = new Adresa();
        Mesto mesto = new Mesto();
        Drzava drzava = new Drzava();
        System.out.println("Unesite novu ulicu");
        String ulica = PomocnaKlasa.ocitajTekst();
        adresa.setUlica(ulica);
        System.out.println("Unesite broj");
        String broj  = PomocnaKlasa.ocitajTekst();
        adresa.setBroj(broj);
        System.out.println("Unesite naziv grada");
        String imeMesta = PomocnaKlasa.ocitajTekst();
        mesto.setNaziv(imeMesta);
        System.out.println("Unesite naziv drzave");
        String imeDrzave = PomocnaKlasa.ocitajTekst();
        drzava.setNaziv(imeDrzave);
        mesto.setDrzava(drzava);
        adresa.setMesto(mesto);
        return adresa;
    }

    TipPansiona ispisiMeniTipPansiona(){
        System.out.println("------------------------");
        System.out.println("Izaberite tip pansiona za smestaj koji ste izabrali");
        System.out.println("1 - NOCENJE");
        System.out.println("2 - NOCENJE SA DORUCKOM");
        System.out.println("3 - POLUPANSION");
        System.out.println("4 - PUN PANSION");
        System.out.println("5 - ALL INCLUSIVE");
        System.out.println("0 - vrati se nazad");
        System.out.println("------------------------");
        Integer broj = in.nextInt();
        if(broj == 1)
            return TipPansiona.NOCENJE;
        else if(broj == 2)
            return TipPansiona.NOCENJE_SA_DORUCKOM;
        else if(broj == 3)
            return TipPansiona.POLUPANSION;
        else if(broj == 4)
            return TipPansiona.PUN_PANSION;
        else if(broj == 5)
            return TipPansiona.ALL_INCLUSIVE;
        else if(broj == 0)
            odradiKreiranjeAranzmana(1);
        return TipPansiona.NOCENJE;
    }

    void ispisiMeniKreiranjePaketa(){
        System.out.println("------------------------");
        System.out.println("Unesite procentualnu vrednost povratka novca: ");
        Double procenat = in.nextDouble();
        if(procenat < 0 || procenat > 100)
            System.out.println("Vrednost mora biti izmedju 0 i 100 !");
        else{
            CurrentSession.getInstance().setPaket(new Paket());
            CurrentSession.getInstance().getPaket().setProcentualnaVrednostPovratkaNovca(procenat);
            StateMachine.getInstance().setState(State.KREIRANJE_PAKETA_STEP2);
        }
    }

    void ispisiMeniKreiranjePaketaStep2(){
        System.out.println("------------------------");
        for(Aranzman a : Model.getInstance().getAranzmani()){
            System.out.println(Model.getInstance().getAranzmani().indexOf(a) + " - " + a.toString());
        }
        System.out.println("------------------------");
        System.out.println("Izaberite jedan ili vise aranzmana unosenjem rednog broja(ukoliko ih je vise, razdvojite razmakom na kraju unesite 'exit' za izlaz): ");
        List<Integer> list = new ArrayList<Integer>();
        try{

            while (in.hasNextInt())
                list.add(in.nextInt());
            in.next();
        }catch (Exception e){
            System.out.println("Niste uneli u ispravnom formatu(primer: 1 2 3 ...)");
        }
        Aranzman a = null;
        ArrayList<Aranzman> aranzmani = new ArrayList<>();
        for(Integer i : list){
            try {
                a = Model.getInstance().getAranzmani().get(i);
                aranzmani.add(a);
            }catch (Exception e){
                System.out.println("Aranzman sa " + i + " ne postoji!");
                return;
            }

        }
        CurrentSession.getInstance().getPaket().setAranzmani(aranzmani);
        CurrentSession.getInstance().getPaket().setKreirao(CurrentSession.getInstance().getKorisnik());
        CurrentSession.getInstance().getPaket().setZakupio(null);
        Model.getInstance().getPaketi().add(CurrentSession.getInstance().getPaket());
        StateMachine.getInstance().setState(State.MENI_PRIJAVLJEN);
    }

    void ispisiMeniKreiranjeAranzmana(){
        System.out.println("------------------------");
        System.out.println("1 - Smestaj");
        System.out.println("2 - Prevoz");
        System.out.println("3 - Datum polaska i datum povratka");
        System.out.println("4 - Osiguranje");
        System.out.println("5 - Za grupu?");
        System.out.println("6 - Ispisi trenutni aranzman");
        System.out.println("7 - Sacuvaj ovaj aranzman");
        System.out.println("0 - Vrati se Nazad");
        System.out.println("------------------------");
        Integer number = in.nextInt();
        odradiKreiranjeAranzmana(number);
    }

    void ispisiMeniBrisanjeAranzmana(){
        ArrayList<Aranzman> aranzmani = Model.getInstance().getAranzmani();
        if(aranzmani.isEmpty()){
            System.out.println("Trenutno nema kreiranih aranzmana!");
            StateMachine.getInstance().setState(State.MENI_PRIJAVLJEN);
        }else{
            System.out.println("------------------------");
            System.out.println("Obrisite aranzman:");
            int i = 0;
            for(Aranzman a: aranzmani){
                i++;
                System.out.println("------------------------" +
                        "--------------------------------------------------");
                System.out.println(i + " - " + a);
                System.out.println("------------------------" +
                        "--------------------------------------------------");
            }
            System.out.println("0 - vrati se nazad");
            Integer broj = in.nextInt();
            for(int j = 0; j < i+1 ;j++){
                if(broj == 0){
                    StateMachine.getInstance().setState(State.MENI_PRIJAVLJEN);
                }else if(broj == j){
                    Model.getInstance().getAranzmani().remove(aranzmani.get(j-1));
                    StateMachine.getInstance().setState(State.MENI_PRIJAVLJEN);
                }
            }
        }

    }

    void odradiKreiranjeAranzmana(Integer broj){
        if(broj == 1)
            smestaj();
        else if(broj == 2)
            prevoz();
        else if(broj == 3)
            datumiPolaskaIPovratka();
        else if(broj == 4)
            osiguranje();
        else if(broj == 5)
            grupni();
        else if(broj == 6)
            System.out.println(aranzman);
        else if(broj == 7) {
            Model.getInstance().getAranzmani().add(aranzman);
            aranzman = null;
            aranzman = new Aranzman();
            StateMachine.getInstance().setState(State.MENI_PRIJAVLJEN);
        }
        else if(broj == 0) {
            StateMachine.getInstance().setState(State.MENI_PRIJAVLJEN);
        	aranzman = null;
        	aranzman = new Aranzman();
        }
    }

    void obradiKlijentUnos(Integer broj){
        if(broj == 1)
            StateMachine.getInstance().setState(State.UPLATA_NOVCA);
        else if(broj == 2)
            StateMachine.getInstance().setState(State.KREIRANJE_PAKETA);
        else if(broj == 3)
            StateMachine.getInstance().setState(State.ZAKUPLJIVANJE_PAKETA);
        else if(broj == 4)
            StateMachine.getInstance().setState(State.OTKAZIVANJE_PAKETA);
        else if(broj == 0)
            StateMachine.getInstance().setState(State.NIJE_PRIJAVLJEN);
    }

    void obradiAgentUnos(Integer broj) {
        if(broj == 1)
            StateMachine.getInstance().setState(State.KREIRANJE_ARANZMANA);
        else if(broj == 2)
            StateMachine.getInstance().setState(State.KREIRANJE_PAKETA);
        else if(broj == 3)
            StateMachine.getInstance().setState(State.BRISANJE_ARANZMANA);
        else if(broj == 0) {
            try {
                Model.getInstance().snimiModel();
            } catch (IOException e) {
                System.out.println("Snimanje sa fajlovima nije uspelo!");
            }
            StateMachine.getInstance().setState(State.NIJE_PRIJAVLJEN);
        }
    }
}
