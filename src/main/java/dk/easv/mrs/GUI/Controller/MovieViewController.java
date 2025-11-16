package dk.easv.mrs.GUI.Controller;
//Project imports
import dk.easv.mrs.BE.Movie;
import dk.easv.mrs.GUI.Model.MovieModel;
//JavaFX imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
//Java imports
import java.net.URL;
import java.util.ResourceBundle;

public class MovieViewController implements Initializable {


    public TextField txtMovieSearch;
    public ListView<Movie> lstMovies;
    private MovieModel movieModel;

    @FXML
    private TextField txtTitle, txtYear;



    public MovieViewController() {
        try {
            movieModel = new MovieModel();
        } catch (Exception e) {
            //displayError(e);
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lstMovies.setItems(movieModel.getObservableMovies());
        lstMovies.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, selectedMovie) ->
        {
            if (selectedMovie != null) {
                txtTitle.setText(selectedMovie.getTitle());
                txtYear.setText(String.valueOf(selectedMovie.getYear()));
            }

        });

        txtMovieSearch.textProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                movieModel.searchMovie(newValue);
            } catch (Exception e) {
                displayError(e);
                e.printStackTrace();
            }
        });
    }

    private void displayError(Throwable t) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Something went wrong");
        alert.setHeaderText(t.getMessage());
        alert.showAndWait();
    }
    @FXML
    private void btnHandleClick(ActionEvent actionEvent) throws Exception {
        // Get user movie data from ui
        String title = txtTitle.getText();
        int year = Integer.parseInt(txtYear.getText());

        // new movie object
        Movie newMovie = new Movie(-1, year, title);

        // call model to create movie
        movieModel.createMovie(newMovie);

    }
    @FXML
    private void onUpdate(ActionEvent actionEvent) {
       Movie selectedMovie = lstMovies.getSelectionModel().getSelectedItem();

       if (selectedMovie != null) {

           selectedMovie.setTitle(txtTitle.getText());
           selectedMovie.setYear(Integer.parseInt(txtYear.getText()));

           try {
               movieModel.updateMovie(selectedMovie);
           } catch (Exception err) {
               displayError(err);
           }
           lstMovies.refresh();
       }
    }

    @FXML
    private void onDelete(ActionEvent actionEvent) {
        Movie selectedMovie = lstMovies.getSelectionModel().getSelectedItem();

        if (selectedMovie != null) {
            try {
                movieModel.deleteMovie(selectedMovie);
            } catch (Exception err) {
                displayError(err);
            }
        }
    }
}
