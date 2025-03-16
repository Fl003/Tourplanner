package org.example.tourplanner.view;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import org.example.tourplanner.viewmodel.SearchBarViewModel;

public class SearchBarController {
    private final SearchBarViewModel searchBarViewModel;

    public TextField SearchBar;

    public SearchBarController(SearchBarViewModel searchBarViewModel) {
        this.searchBarViewModel = searchBarViewModel;
    }

    public void onSearchButton(ActionEvent actionEvent) {

    }
}
