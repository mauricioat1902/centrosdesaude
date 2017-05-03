package com.example.mauricioecamila.centrosdesaude;

/**
 * Created by Mauricio e Camila on 03/11/2016.
 */

public class Usuario {
    private int id;
    private String nome;
    private String sobreNome;
    private String email;
    private String sexo;
    private int tipoUsuario;

    public Usuario(int id, String nome, String sobreNome, String email) {
        this.id = id;
        this.nome = nome;
        this.sobreNome = sobreNome;
        this.email = email;
    }
    public Usuario(String nome, String sobreNome, String email) {
        this.nome = nome;
        this.sobreNome = sobreNome;
        this.email = email;
    }
    public Usuario(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSobreNome() {
        return sobreNome;
    }

    public void setSobreNome(String sobreNome) {
        this.sobreNome = sobreNome;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void inserirDadosBD(String nome, String sobrenome, String email, String nomeUsuario){}

    public int getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(int tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
}
