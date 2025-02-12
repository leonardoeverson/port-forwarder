package com.local.portforwader.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TableModel {
    private StringProperty endereco;
    private StringProperty origem;
    private StringProperty destino;

    public TableModel(String enderecoRemoto, String portaOrigem, String portaDestino) {
        this.endereco = new SimpleStringProperty(enderecoRemoto);
        this.origem = new SimpleStringProperty(portaOrigem);
        this.destino = new SimpleStringProperty(portaDestino);
    }

    public String getEndereco() {
        return endereco.get();
    }

    public StringProperty enderecoProperty() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco.set(endereco);
    }

    public String getOrigem() {
        return origem.get();
    }

    public StringProperty origemProperty() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem.set(origem);
    }

    public String getDestino() {
        return destino.get();
    }

    public StringProperty destinoProperty() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino.set(destino);
    }
}
