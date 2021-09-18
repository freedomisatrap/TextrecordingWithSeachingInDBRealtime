package com.scanne;

public class DataBase {
    public String eadd, leveldanger, name, category, origin, description;
    public DataBase()
    {}

    public DataBase( String eadd, String name, String category, String description, String origin, String leveldanger) {
        this.name = name;
        this.category = category;
        this.origin = origin;
        this.description = description;
        this.eadd = eadd;
        this.leveldanger = leveldanger;
    }


    public String getLeveldanger() {
        return leveldanger;
    }

    public void setLeveldanger(String leveldanger) {
        this.leveldanger = leveldanger;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEadd() {
        return eadd;
    }

    public void setEadd(String eadd) {
        this.eadd = eadd;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
