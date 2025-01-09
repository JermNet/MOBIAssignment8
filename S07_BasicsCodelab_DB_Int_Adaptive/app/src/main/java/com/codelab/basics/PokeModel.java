package com.codelab.basics;

public class PokeModel {

    private long id_col;
    // Despite being either 0 or 1, legendary_col is an Integer since Google was dumb and doesn't have a getBoolean method in their cursor class.
    private Integer dex_col, total_col, hp_col, attack_col, defense_col, specialAttack_col, specialDefense_col, speed_col, access_col, generation_col, legendary_col;
    private String name_col, type1_col, type2_col;
    // Holds the actual image data, technically the assignment said BitMap, will change if that's super ultra important
    private byte[] image_col;
    // New string for the link to each image
    private String link_col;

    public PokeModel() {
        // Fun note: I made this the default since Bulbasaur is the default Pokemon used in the actual games!
        id_col = 0;
        dex_col = 1;
        name_col = "Bulbasaur";
        type1_col = "Grass";
        type2_col = "Poison";
        total_col = 318;
        hp_col = 45;
        attack_col = 49;
        defense_col = 49;
        specialAttack_col = 65;
        specialDefense_col = 65;
        speed_col = 45;
        generation_col = 1;
        legendary_col = 0;
        access_col = 0;
        image_col = null;
        link_col = "https://www.serebii.net/xy/pokemon/001.png";
    }

    public PokeModel(long id, Integer dexNumber, String pokemonName, String type1, String type2, Integer total, Integer hp, Integer attack, Integer defense, Integer specialAttack, Integer specialDefense, Integer speed, Integer generation, Integer legendary, Integer access, byte[] image, String link) {
        id_col = id;
        dex_col = dexNumber;
        name_col = pokemonName;
        type1_col = type1;
        type2_col = type2;
        total_col = total;
        hp_col = hp;
        attack_col = attack;
        defense_col = defense;
        specialAttack_col = specialAttack;
        specialDefense_col = specialDefense;
        speed_col = speed;
        generation_col = generation;
        legendary_col = legendary;
        access_col = access;
        image_col = image;
        link_col = link;
    }

    @Override
    public String toString() {
        // Once again, this is because Google is dumb
        boolean legendary;
        legendary = (legendary_col == 1);
        // Inline if statement to see if there's an image or not
        String isImage = (image_col != null) ? "yes" : "no";

        return "PokeModel{" +
                "id=" + id_col +
                ", dexNumber=" + dex_col +
                ", pokemonName='" + name_col + '\'' +
                ", type1='" + type1_col + '\'' +
                ", type2='" + type2_col + '\'' +
                ", total=" + total_col +
                ", hp=" + hp_col +
                ", attack=" + attack_col +
                ", defense=" + defense_col +
                ", specialAttack=" + specialAttack_col +
                ", specialDefense=" + specialDefense_col +
                ", speed=" + speed_col +
                ", generation=" + generation_col +
                ", legendary=" + legendary +
                ", access=" + access_col +
                ", image='" + isImage + '\'' +
                ", link='" + link_col + '\'';
    }

    // Fancier toString() method that returns values in a more human readable format, without some of the data. Two different strings depending on whether the Pokemon has 2 types or not
    public String toFancyString() {
        if (!getType2().equalsIgnoreCase("NONE")) {
            return name_col + " is Pokemon No. " + dex_col + ". It is a " + type1_col + " and " + type2_col + " type Pokemon with a base stat total of " + total_col + ". It has been accessed " + access_col + " time(s).";
        }
        return name_col + " is Pokemon No. " + dex_col + ". It is a " + type1_col + " type Pokemon with a base stat total of " + total_col + ". It has been accessed " + access_col + " time(s).";
    }

    // Numerous setters and getters, you know the drill
    public long getId() {
        return id_col;
    }

    public void setId(long id) {
        id_col = id;
    }

    public Integer getDexNumber() {
        return dex_col;
    }

    public void setDexNumber(Integer dexNumber) {
        dex_col = dexNumber;
    }

    public String getPokemonName() {
        return name_col;
    }

    public void setPokemonName(String pokemonName) {
        name_col = pokemonName;
    }

    public String getType1() {
        return type1_col;
    }

    public void setType1(String type1) {
        type1_col = type1;
    }

    public String getType2() {
        return type2_col;
    }

    public void setType2(String type2) {
        type2_col = type2;
    }

    public Integer getTotal() {
        return total_col;
    }

    public void setTotal(Integer total) {
        total_col = total;
    }

    public Integer getHP() {
        return hp_col;
    }

    public void setHP(Integer hp) {
        hp_col = hp;
    }

    public Integer getAttack() {
        return attack_col;
    }

    public void setAttack(Integer attack) {
        attack_col = attack;
    }

    public Integer getDefense() {
        return defense_col;
    }

    public void setDefense(Integer defense) {
        defense_col = defense;
    }

    public Integer getSpecialAttack() {
        return specialAttack_col;
    }

    public void setSpecialAttack(Integer specialAttack) {
        specialAttack_col = specialAttack;
    }

    public Integer getSpecialDefense() {
        return specialDefense_col;
    }

    public void setSpecialDefense(Integer specialDefense) {
        specialDefense_col = specialDefense;
    }

    public Integer getSpeed() {
        return speed_col;
    }

    public void setSpeed(Integer speed) {
        speed_col = speed;
    }

    public Integer getGeneration() {
        return generation_col;
    }

    public void setGeneration(Integer generation) {
        generation_col = generation;
    }

    public Integer getLegendary() {
        return legendary_col;
    }

    public void setLegendary(Integer legendary) {
        legendary_col = legendary;
    }

    public Integer getAccess() {
        return access_col;
    }

    public void setAccess(Integer access) {
        access_col = access;
    }

    public byte[] getImage() {
        return image_col;
    }

    public void setImage(byte[] image) {
        image_col = image;
    }

    public String getLink() {
        return link_col;
    }

    public void setLink(String link) {
        link_col = link;
    }
}
