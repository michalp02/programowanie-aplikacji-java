package com.jsfcourse.calc;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

@Named
@RequestScoped
//@SessionScoped
public class CalcBB {
	private String kwota;
	private String okres;
        private String oprocentowanie;
        private String formaRat;
	private String result;

	@Inject
	FacesContext ctx;

    public String getKwota() {
        return kwota;
    }

    public void setKwota(String kwota) {
        this.kwota = kwota;
    }

    public String getOkres() {
        return okres;
    }

    public void setOkres(String okres) {
        this.okres = okres;
    }

    public String getOprocentowanie() {
        return oprocentowanie;
    }

    public void setOprocentowanie(String oprocentowanie) {
        this.oprocentowanie = oprocentowanie;
    }

    public String getFormaRat() {
        return formaRat;
    }

    public void setFormaRat(String formaRat) {
        this.formaRat = formaRat;
    }

	

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public boolean doTheMath() {
		try {
			double kwota = Double.parseDouble(this.kwota);
			double okres = Double.parseDouble(this.okres);
			double oprocentowanie = Double.parseDouble(this.oprocentowanie);
			
            double monthly_interest = oprocentowanie / 100 / 12;

                double rata = (kwota * Math.pow(1 + monthly_interest, okres) * monthly_interest) / (Math.pow(1 + monthly_interest, okres) - 1);
                double sumaOdsetek = 0;
                double sumaKapitalu = 0;
                for(int i = 0; i < okres; i++) {
                    double rataOdsetkowa = kwota * monthly_interest;
                    double rataKapitalowa = rata - rataOdsetkowa;
                    kwota -= rataKapitalowa;
                    sumaOdsetek += rataOdsetkowa;
                    sumaKapitalu += rataKapitalowa;

                    rataOdsetkowa = Math.round(rataOdsetkowa * 100.0) / 100.0;
                    rataKapitalowa = Math.round(rataKapitalowa * 100.0) / 100.0;
                    rata = Math.round(rata * 100.0) / 100.0;
                    kwota = Math.round(kwota * 100.0) / 100.0;
                    sumaOdsetek = Math.round(sumaOdsetek * 100.0) / 100.0;
                    sumaKapitalu = Math.round(sumaKapitalu * 100.0) / 100.0;
                }
                result = "Miesięczna rata: " + rata + " zł, suma odsetek: " + sumaOdsetek + " zł, koszt kredytu: " + (sumaOdsetek + sumaKapitalu) + " zł";

			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacja wykonana poprawnie", null));
			return true;
		} catch (Exception e) {
			ctx.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błąd podczas przetwarzania parametrów", null));
			return false;
		}
	}

	// Go to "showresult" if ok
	public String calc() {
		if (doTheMath()) {
			return "showresult";
		}
		return null;
	}

	// Put result in messages on AJAX call
	public String calc_AJAX() {
		if (doTheMath()) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Wynik: " + result, null));
		}
		return null;
	}

	public String info() {
		return "info";
	}
}
