package yms.com;

import java.io.Serializable;

public class Person implements Serializable{
    private String phone;
    private String name;
    private String type;
    private String email;

    public Person(String phone, String name, String type, String email) {
        this.phone = phone;
        this.name = name;
        this.type = type;
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
