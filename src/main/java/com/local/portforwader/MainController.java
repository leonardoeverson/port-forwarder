package com.local.portforwader;

import com.local.portforwader.command.Terminal;
import com.local.portforwader.model.TableModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class MainController {
    @FXML
    private TextField portaOrigem;

    @FXML
    private TextField portaDestino;

    @FXML
    private TextField enderecoDestino;

    @FXML
    private TableView<TableModel> tabela;

    @FXML
    private TableColumn<TableModel, String> endereco;

    @FXML
    private TableColumn<TableModel, String> origem;

    @FXML
    private TableColumn<TableModel, String> destino;

    private final List<TableModel> tabelas = new ArrayList<>();

    @FXML
    public void initialize() {
        endereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        origem.setCellValueFactory(new PropertyValueFactory<>("origem"));
        destino.setCellValueFactory(new PropertyValueFactory<>("destino"));

        portaOrigem.addEventFilter(KeyEvent.KEY_TYPED, this::setPortFilter);
        portaDestino.addEventFilter(KeyEvent.KEY_TYPED, this::setPortFilter);
        setIpFilter(enderecoDestino);

        Runnable runnable = () -> {
            List<String[]> lista = null;
            try {
                lista = Terminal.read();
                for (String[] anotherList : lista) {
                    tabelas.add(new TableModel(anotherList[1], anotherList[2], anotherList[3]));
                }

                ObservableList<TableModel> local_models = FXCollections.observableArrayList(tabelas);
                tabela.setItems(local_models);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        new Thread(runnable).start();
    }

    @FXML
    public void onSaveButtonClicked(ActionEvent actionEvent) {
        if (portaOrigem.getText().length() < 3) {
            getAlert("Preencha o campo de porta de origem", AlertBuilder.WARNING);
            return;
        }

        if (portaDestino.getText().length() < 3) {
            getAlert("Preencha o campo de porta de destino", AlertBuilder.WARNING);
            return;
        }

       String[] dot = enderecoDestino.getText().split("\\.");
        if (dot.length < 4) {
            getAlert("Preencha o campo de endereço de destino", AlertBuilder.WARNING);
            return;
        }

        boolean result = Terminal.run(portaOrigem.getText(), portaDestino.getText(), enderecoDestino.getText());
        if (result) {
            getAlert("Comando executado com sucesso", AlertBuilder.CONFIRMATION);
            return;
        }

        getAlert("Erro ao executar o comando", AlertBuilder.ERROR);
    }

    private void getAlert(String content, int type) {
        new AlertBuilder().message(content).type(type).build();
    }

    private void setPortFilter(KeyEvent event) {
        String character = event.getCharacter();
        TextField field = (TextField) event.getSource();

        if (!character.matches("\\d") || field.getText().length() >= 5) {
            event.consume();
        }
    }

    private void setIpFilter(TextField field) {
        String regex = makePartialIPRegex();
        UnaryOperator<TextFormatter.Change> ipFilter = change -> {
            String text = change.getControlNewText();

            if (text.matches(regex)) {
                return change;
            }

            return null;
        };

        field.setTextFormatter(new TextFormatter<>(ipFilter));
    }

    /**
     * @see "https://stackoverflow.com/questions/32094296/javafx-textfield-ip-address"
     * @return string
     */
    private String makePartialIPRegex() {
        String partialBlock = "(([01]?[0-9]{0,2})|(2[0-4][0-9])|(25[0-5]))" ;
        String subsequentPartialBlock = "(\\."+partialBlock+")" ;
        String ipAddress = partialBlock+"?"+subsequentPartialBlock+"{0,3}";
        return "^"+ipAddress ;
    }
}