package com.local.portforwader.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TableModel {
    private final StringProperty endereco;
    private final StringProperty origem;
    private final StringProperty destino;

    public TableModel(String enderecoRemoto, String portaOrigem, String portaDestino) {
        this.endereco = new SimpleStringProperty(enderecoRemoto);
        this.origem = new SimpleStringProperty(portaOrigem);
        this.destino = new SimpleStringProperty(portaDestino);
    }

    public String getEndereco() {
        return endereco.get();
    }

    public String getOrigem() {
        return origem.get();
    }

    public String getDestino() {
        return destino.get();
    }
}
