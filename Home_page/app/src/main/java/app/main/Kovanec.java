package app.main;

public class Kovanec {
    private String market_cap_rank;
    private String name;
    private String symbol;
    private String current_price;
    private String price_change_percentage_24h_in_currency;
    private String market_cap;
    private String image;



    public Kovanec(String[] podatki){
        this.market_cap_rank=podatki[0];
        this.name=podatki[1];
        this.symbol=podatki[2];
        this.current_price=podatki[3];
        this.price_change_percentage_24h_in_currency=podatki[4];
        this.market_cap=podatki[5];
        this.image=podatki[6];
    }

    public String rank(){
        return market_cap_rank;
    }
    public String named(){
        return name;
    }
    public String sym(){
        return symbol;
    }
    public String price(){
        return current_price;
    }
    public String daily(){
        return price_change_percentage_24h_in_currency;
    }
    public String market_cap(){
        return market_cap;
    }




}
