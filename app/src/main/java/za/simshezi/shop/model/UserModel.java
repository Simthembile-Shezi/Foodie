package za.simshezi.shop.model;

import java.io.Serializable;

public class UserModel implements Serializable {
    private String id;
    private String name;
    private String email;
    private String cellphone;

    public UserModel() {
    }

    public UserModel(String name, String email, String cellphone) {
        this.name = name;
        this.email = email;
        this.cellphone = cellphone;
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
}
