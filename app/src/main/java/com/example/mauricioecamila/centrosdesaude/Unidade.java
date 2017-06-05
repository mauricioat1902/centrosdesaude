package com.example.mauricioecamila.centrosdesaude;

public class Unidade {
    private long id;
    private String nome;
    private String logradouro;
    private String numero;
    private String bairro;
    private String municipio;
    private long cep;
    private String estado;
    private String latitude, longitude;
    private Double distancia;
    private int posicaoRank;
    private Double mdAtendimento, mdEstrutura, mdEquipamentos, mdLocalizacao, mdTempoAtendimento, mdGeral;
    private String vinculoSus;
    private String tipoUnidade;

    public Unidade(long id, String nome, String logradouro, String numero, String latitude, String longitude) {
        this.id = id;
        this.nome = nome;
        this.logradouro = logradouro;
        this.numero = numero;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Unidade (){
    }

    public Unidade(long id, String nome, String logradouro, String numero, String bairro, String municipio, long cep, String estado, String latitude, String longitude, Double distancia, Double mdAtendimento, Double mdEstrutura, Double mdEquipamentos, Double mdLocalizacao, Double mdTempoAtendimento, Double mdGeral, String tipoUnidade) {
        this.id = id;
        this.nome = nome;
        this.logradouro = logradouro;
        this.numero = numero;
        this.bairro = bairro;
        this.municipio = municipio;
        this.cep = cep;
        this.estado = estado;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distancia = distancia;
        this.mdAtendimento = mdAtendimento;
        this.mdEstrutura = mdEstrutura;
        this.mdEquipamentos = mdEquipamentos;
        this.mdLocalizacao = mdLocalizacao;
        this.mdTempoAtendimento = mdTempoAtendimento;
        this.mdGeral = mdGeral;
        this.tipoUnidade = tipoUnidade;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public long getCep() {
        return cep;
    }

    public void setCep(long cep) {
        this.cep = cep;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public Double getDistancia() {
        return distancia;
    }

    public void setDistancia(Double distancia) {
        this.distancia = distancia;
    }

    public int getPosicaoRank() {
        return posicaoRank;
    }

    public void setPosicaoRank(int posicaoRank) {
        this.posicaoRank = posicaoRank;
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

    public Double getMdGeral() {
        return mdGeral;
    }

    public void setMdGeral(Double mdGeral) {
        this.mdGeral = mdGeral;
    }

    public String getVinculoSus() {
        return vinculoSus;
    }

    public void setVinculoSus(String vinculoSus) {
        this.vinculoSus = vinculoSus;
    }

    public String getTipoUnidade() {
        return tipoUnidade;
    }

    public void setTipoUnidade(String tipoUnidade) {
        this.tipoUnidade = tipoUnidade;
    }
}
