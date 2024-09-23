package com.example.testaudioenglish.Model;



public class AccountCustomer {
    private Long id;

    public AccountCustomer(String name, Long age, String numberphone) {
        this.name = name;
        this.age = age;
        this.numberphone = numberphone;
    }

    private Long idcustomer;
    private String username;
    private String password;
    private String membership;
    private String email;
    private String name;
    private Long age;
    private String numberphone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCustomer() {
        return idcustomer;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public String getNumberphone() {
        return numberphone;
    }

    public void setNumberphone(String numberphone) {
        this.numberphone = numberphone;
    }

    public AccountCustomer(Long id, Long idCustomer, String username, String password, String membership, String email, String name, Long age, String numberphone) {
        this.id = id;
        this.idcustomer = idCustomer;
        this.username = username;
        this.password = password;
        this.membership = membership;
        this.email = email;
        this.name = name;
        this.age = age;
        this.numberphone = numberphone;
    }
    public AccountCustomer(Long id, String username, String password, String membership, String email, String name, Long age, String numberphone) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.membership = membership;
        this.email = email;
        this.name = name;
        this.age = age;
        this.numberphone = numberphone;
    }

    public AccountCustomer(String username, String password){
        this.username = username;
        this.password = password;
    }


}
