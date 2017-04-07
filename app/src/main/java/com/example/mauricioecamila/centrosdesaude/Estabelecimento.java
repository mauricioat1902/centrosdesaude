package com.example.mauricioecamila.centrosdesaude;

/**
 * Created by Mauricio e Camila on 25/01/2017.
 */

public class Estabelecimento {

    private long id;
    private String nome;
    private String tipoEstabelecimento;
    private String vinculoSus, temAtendimentoUrgencia, temAtendimentoAmbulatorial, temCentroCirurgico, temObstetra, temNeoNatal, temDialise;
    private String logradouro;
    private String numero;
    private String bairro, cidade, nuCep, estado;
    private String telefone;
    private String turnoAtendimento;
    private String latitude, longitude;
    private Double distancia;
    private Double mdAtendimento, mdEstrutura, mdEquipamentos, mdLocalizacao, mdTempoAtendimento, mdGeral;
    private int posicaoRank;



    public Estabelecimento(long id, String nome, String logradouro, String numero, String latitude, String longitude) {
        this.id = id;
        this.nome = nome;
        this.logradouro = logradouro;
        this.numero = numero;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Estabelecimento(long id, String nome, String tipoEstabelecimento, String vinculoSus, String temAtendimentoUrgencia, String temAtendimentoAmbulatorial, String temCentroCirurgico, String temObstetra, String temNeoNatal, String temDialise, String logradouro, String numero, String bairro, String cidade, String nuCep, String estado, String telefone, String turnoAtendimento, String latitude, String longitude) {
        this.id = id;
        this.nome = nome;
        this.tipoEstabelecimento = tipoEstabelecimento;
        this.vinculoSus = vinculoSus;
        this.temAtendimentoUrgencia = temAtendimentoUrgencia;
        this.temAtendimentoAmbulatorial = temAtendimentoAmbulatorial;
        this.temCentroCirurgico = temCentroCirurgico;
        this.temObstetra = temObstetra;
        this.temNeoNatal = temNeoNatal;
        this.temDialise = temDialise;
        this.logradouro = logradouro;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.nuCep = nuCep;
        this.estado = estado;
        this.telefone = telefone;
        this.turnoAtendimento = turnoAtendimento;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getTipoEstabelecimento() {
        return tipoEstabelecimento;
    }

    public void setTipoEstabelecimento(String tipoEstabelecimento) {
        this.tipoEstabelecimento = tipoEstabelecimento;
    }

    public String getVinculoSus() {
        return vinculoSus;
    }

    public void setVinculoSus(String vinculoSus) {
        this.vinculoSus = vinculoSus;
    }

    public String getTemAtendimentoUrgencia() {
        return temAtendimentoUrgencia;
    }

    public void setTemAtendimentoUrgencia(String temAtendimentoUrgencia) {
        this.temAtendimentoUrgencia = temAtendimentoUrgencia;
    }

    public String getTemAtendimentoAmbulatorial() {
        return temAtendimentoAmbulatorial;
    }

    public void setTemAtendimentoAmbulatorial(String temAtendimentoAmbulatorial) {
        this.temAtendimentoAmbulatorial = temAtendimentoAmbulatorial;
    }

    public String getTemCentroCirurgico() {
        return temCentroCirurgico;
    }

    public void setTemCentroCirurgico(String temCentroCirurgico) {
        this.temCentroCirurgico = temCentroCirurgico;
    }

    public String getTemObstetra() {
        return temObstetra;
    }

    public void setTemObstetra(String temObstetra) {
        this.temObstetra = temObstetra;
    }

    public String getTemNeoNatal() {
        return temNeoNatal;
    }

    public void setTemNeoNatal(String temNeoNatal) {
        this.temNeoNatal = temNeoNatal;
    }

    public String getTemDialise() {
        return temDialise;
    }

    public void setTemDialise(String temDialise) {
        this.temDialise = temDialise;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getNuCep() {
        return nuCep;
    }

    public void setNuCep(String nuCep) {
        this.nuCep = nuCep;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getTurnoAtendimento() {
        return turnoAtendimento;
    }

    public void setTurnoAtendimento(String turnoAtendimento) {
        this.turnoAtendimento = turnoAtendimento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getlogradouro() {
        return logradouro;
    }

    public void setlogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;

    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Double getDistancia() {
        return distancia;
    }

    public void setDistancia(Double distancia) {
        this.distancia = distancia;
    }

    public Double getMdGeral() {
        return mdGeral;
    }

    public void setMdGeral(Double mdGeral) {
        this.mdGeral = mdGeral;
    }

    public Double getMdAtendimento() {
        return mdAtendimento;
    }

    public void setMdAtendimento(Double mdAtendimento) {
        this.mdAtendimento = mdAtendimento;
    }

    public Double getMdEstrutura() {
        return mdEstrutura;
    }

    public void setMdEstrutura(Double mdEstrutura) {
        this.mdEstrutura = mdEstrutura;
    }

    public Double getMdEquipamentos() {
        return mdEquipamentos;
    }

    public void setMdEquipamentos(Double mdEquipamentos) {
        this.mdEquipamentos = mdEquipamentos;
    }

    public Double getMdLocalizacao() {
        return mdLocalizacao;
    }

    public void setMdLocalizacao(Double mdLocalizacao) {
        this.mdLocalizacao = mdLocalizacao;
    }

    public Double getMdTempoAtendimento() {
        return mdTempoAtendimento;
    }

    public void setMdTempoAtendimento(Double mdTempoAtendimento) {
        this.mdTempoAtendimento = mdTempoAtendimento;
    }

    public int getPosicaoRank() {
        return posicaoRank;
    }

    public void setPosicaoRank(int posicaoRank) {
        this.posicaoRank = posicaoRank;
    }
}
