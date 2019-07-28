package com.example.app;

public class Beers {

    private String abv;
    private String ibu;
    private String id;
    private String name;
    private String style;
    private String ounces;



    public String getAbv() {
        return abv;
    }

    public String getIbu() {
        return ibu;

    }

    public String getId() {

        return id;
    }

    public String getName() {

        return name;
    }

    public String getStyle() {
        return  style;
    }

    public String getOunces() {
        return ounces;
    }


    //------------ set -------------//

    public void setAbv(String abv) {
        this.abv = abv;
    }

    public void setIbu(String ibu) {
        this.ibu = ibu;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setOunces(String ounces) {
        this.ounces = ounces;
    }


}
