package model.zakupljivanje;

import model.prevozno_sredstvo.Avion;
import model.prevozno_sredstvo.PrevoznoSredstvo;
import model.smestaj.Hotel;
import model.smestaj.Smestaj;

import java.io.Serializable;
import java.util.Date;

public class Aranzman implements Serializable {

    private static final long serialVersionUID = 1L;
    private Boolean saOsiguranjem;
    private Boolean grupni;
    private Date datumPolaska;
    private Date datumPovratka;
    private PrevoznoSredstvo prevoznoSredstvo;
    private Smestaj smestaj;

    public Aranzman() {
    }

    @Override
    public String toString() {
    	String s1 = "Voz";
    	if (prevoznoSredstvo instanceof Avion) {
    		s1 = "Avion";
    	}
    	String s2 = "Apartman";
    	if (smestaj instanceof Hotel) {
    		s2 = "Hotel";
    	}
    	
        return "--------Aranzman---------" + "\n" +
                "Putno osiguranje: " + saOsiguranjem + "\n" +
                "Grupno putovanje: " + grupni + "\n" +
                "Datum putovanja: " + datumPolaska + "\n" +
                "Datum povratka: " + datumPovratka + "\n" +
                "Prevozno sredstvo: " + s1 + "\n" +
                prevoznoSredstvo + "\n" +
                "--------------------" + s2 + "------------------" + "\n" +
                  smestaj;
    }

    public Boolean getSaOsiguranjem() {
        return saOsiguranjem;
    }

    public void setSaOsiguranjem(Boolean saOsiguranjem) {
        this.saOsiguranjem = saOsiguranjem;
    }

    public Boolean getGrupni() {
        return grupni;
    }

    public void setGrupni(Boolean grupni) {
        this.grupni = grupni;
    }

    public Date getDatumPolaska() {
        return datumPolaska;
    }

    public void setDatumPolaska(Date datumPolaska) {
        this.datumPolaska = datumPolaska;
    }

    public Date getDatumPovratka() {
        return datumPovratka;
    }

    public void setDatumPovratka(Date datumPovratka) {
        this.datumPovratka = datumPovratka;
    }

    public PrevoznoSredstvo getPrevoznoSredstvo() {
        return prevoznoSredstvo;
    }

    public void setPrevoznoSredstvo(PrevoznoSredstvo prevoznoSredstvo) {
        this.prevoznoSredstvo = prevoznoSredstvo;
    }

    public Smestaj getSmestaj() {
        return smestaj;
    }

    public void setSmestaj(Smestaj smestaj) {
        this.smestaj = smestaj;
    }
}
