package com.intesoft.syncworks.domain.entities;


import jakarta.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "userName")
    private String userName;
    @Column(name = "password")
    private String password;
    @Column(name = "name")
    private String name;
    @Column(name = "lastName")
    private String lastName;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rolId", referencedColumnName = "id")
    private Rol rol;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idArea")
    private Area area;

    public User() {
    }

    public User(Long id, String userName, String password, String name, String lastName, Rol rol, Area area) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.lastName = lastName;
        this.rol = rol;
        this.area = area;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }
}
