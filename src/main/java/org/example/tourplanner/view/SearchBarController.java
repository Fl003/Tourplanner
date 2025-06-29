package org.example.tourplanner.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import org.example.tourplanner.viewmodel.SearchBarViewModel;

public class SearchBarController {
    private final SearchBarViewModel searchBarViewModel;

    @FXML
    public TextField searchBar;

    public SearchBarController(SearchBarViewModel searchBarViewModel) {
        this.searchBarViewModel = searchBarViewModel;
    }

    public void initialize() {
        searchBar.textProperty().bindBidirectional(searchBarViewModel.searchStringProperty());
        // search on enter
        searchBar.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                searchBarViewModel.searchAllToursForString();
            }
        });
    }

    public void onSearchButton(ActionEvent actionEvent) {
        searchBarViewModel.searchAllToursForString();
    }
}
