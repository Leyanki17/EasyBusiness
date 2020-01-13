package com.projet.easybusiness;

public class Annonce {
    private String id;
    private String titre;
    private String description;
    private float prix;
    private String pseudo;
    private String emailContact;
    private String telContact;
    private String ville;
    private String cp;
    private String[] images;
    private Long date;

    public Annonce(String id, String titre, String description, float prix, String pseudo, String emailContact, String telContact, String ville, String cp, String[] image, Long date) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.prix = prix;
        this.pseudo = pseudo;
        this.emailContact = emailContact;
        this.telContact = telContact;
        this.ville = ville;
        this.cp = cp;
        this.images = image;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getEmailContact() {
        return emailContact;
    }

    public void setEmailContact(String emailContact) {
        this.emailContact = emailContact;
    }

    public String getTelContact() {
        return telContact;
    }

    public void setTelContact(String telContact) {
        this.telContact = telContact;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String[] getImages() {
        return images;
    }

    public void setImage(String[] images) {
        this.images = images;
    }

    public Long getDate(){
        return date;
    }

    public String getAdresse(){
        return this.cp+", "+ this.ville;
    }

    public void setDate(Long date) {
        this.date = date;
    }


}
