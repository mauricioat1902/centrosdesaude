package com.example.mauricioecamila.centrosdesaude;

import java.util.Date;

/**
 * Created by Mauricio e Camila on 04/03/2017.
 */

public class Avaliacao {

    private int id;
    private long idUnidade;
    private String nomeUsuario;
    private String nomeEstabelecimento;
    private Double avAtendimento, avEstrutura, avEquipamentos, avLocalizacao, avTempoAtendimento;
    private String comentario;
    private Date dataComentario;

    public Avaliacao(int id, long idUnidade, String nomeUsuario , Double avAtendimento, Double avEstrutura, Double avEquipamentos, Double avLocalizacao, Double avTempoAtendimento, String comentario) {
        this.id = id;
        this.idUnidade = idUnidade;
        this.nomeUsuario = nomeUsuario;
        this.avAtendimento = avAtendimento;
        this.avEstrutura = avEstrutura;
        this.avEquipamentos = avEquipamentos;
        this.avLocalizacao = avLocalizacao;
        this.avTempoAtendimento = avTempoAtendimento;
        this.comentario = comentario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getNomeEstabelecimento() {
        return nomeEstabelecimento;
    }

    public void setNomeEstabelecimento(String nomeEstabelecimento) {
        this.nomeEstabelecimento = nomeEstabelecimento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getIdUnidade() {
        return idUnidade;
    }

    public void setIdUnidade(long idUnidade) {
        this.idUnidade = idUnidade;
    }

    public Double getAvAtendimento() {
        return avAtendimento;
    }

    public void setAvAtendimento(Double avAtendimento) {
        this.avAtendimento = avAtendimento;
    }

    public Double getAvEstrutura() {
        return avEstrutura;
    }

    public void setAvEstrutura(Double avEstrutura) {
        this.avEstrutura = avEstrutura;
    }

    public Double getAvEquipamentos() {
        return avEquipamentos;
    }

    public void setAvEquipamentos(Double avEquipamentos) {
        this.avEquipamentos = avEquipamentos;
    }

    public Double getAvLocalizacao() {
        return avLocalizacao;
    }

    public void setAvLocalizacao(Double avLocalizacao) {
        this.avLocalizacao = avLocalizacao;
    }

    public Double getAvTempoAtendimento() {
        return avTempoAtendimento;
    }

    public Date getDataComentario() {
        return dataComentario;
    }

    public void setDataComentario(Date dataComentario) {
        this.dataComentario = dataComentario;
    }

    public void setAvTempoAtendimento(Double avTempoAtendimento) {
        this.avTempoAtendimento = avTempoAtendimento;
    }

    public Double mediaAvaliacoes(){
        Double media;
        media = (this.getAvAtendimento()+this.getAvEquipamentos()+this.getAvEstrutura()+this.getAvLocalizacao()+this.getAvTempoAtendimento()) / 5;
        return media;
    }
}
