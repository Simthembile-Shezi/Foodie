package za.simshezi.shop.model;

import java.io.Serializable;

public class UserModel implements Serializable {
    private String id;
    private String name;
    private String email;
    private String cellphone;
    private Double credit;
    private boolean eft;
    private boolean card;

    public UserModel() {
    }

    public UserModel(String name, String email, String cellphone) {
        this.name = name;
        this.email = email;
        this.cellphone = cellphone;
        this.credit = 0.0;
        this.eft = false;
        this.card = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public boolean isEft() {
        return eft;
    }

    public void setEft(boolean eft) {
        this.eft = eft;
    }

    public boolean isCard() {
        return card;
    }

    public void setCard(boolean card) {
        this.card = card;
    }
}
