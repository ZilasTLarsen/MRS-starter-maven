package dk.easv.mrs.DAL.db;

import dk.easv.mrs.BE.Movie;
import dk.easv.mrs.DAL.IMovieDataAccess;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO_DB implements IMovieDataAccess {

    private MyDatabaseConnector databaseConnector;

    public MovieDAO_DB() throws IOException {
        databaseConnector = new MyDatabaseConnector();
    }


    public List<Movie> getAllMovies() throws SQLException {
        ArrayList<Movie> allMovies = new ArrayList<>();
        try(Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM Movie;";

            Statement statement = connection.createStatement();

            if(statement.execute(sql))
            {
               ResultSet resultSet = statement.getResultSet();
               while(resultSet.next()){
                  int id = resultSet.getInt("id");
                  String title = resultSet.getString("title");
                  int year = resultSet.getInt("year");

                  Movie movie = new Movie(id, year, title);
                  allMovies.add(movie);
            }

            }
        }
        return allMovies;
    }

    @Override
    public Movie createMovie(Movie newMovie) throws Exception {
        try(Connection connection = databaseConnector.getConnection()) {
            String sql = "INSERT INTO Movie (title, year) VALUES ('" + newMovie.getTitle() + "', " + newMovie.getYear() + ");";

            Statement statement = connection.createStatement();

            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);

            ResultSet resultSet = statement.getGeneratedKeys();

            if(resultSet.next()){
                int id = resultSet.getInt(1);
                return new Movie(id, newMovie.getYear(), newMovie.getTitle());
            } else {
                throw new SQLException("Creating movie failed, no ID obtained.");
            }
        }

    }

    @Override
    public void updateMovie(Movie movie) throws Exception {
        try(Connection connection = databaseConnector.getConnection()) {
            String sql = "UPDATE Movie SET title = '" + movie.getTitle() + "', year = " + movie.getYear() + " WHERE id = " + movie.getId() + ";";

            Statement statement = connection.createStatement();

            statement.executeUpdate(sql);
        }

    }

    @Override
    public void deleteMovie(Movie movie) throws Exception {
        try(Connection connection = databaseConnector.getConnection()) {
            String sql = "DELETE FROM Movie WHERE id = " + movie.getId() + ";";

            Statement statement = connection.createStatement();

            statement.executeUpdate(sql);
        }

    }

    public static void main(String[] args) throws SQLException, IOException {
        MovieDAO_DB movieDAO_db = new MovieDAO_DB();

        List<Movie> allMovies = movieDAO_db.getAllMovies();

        System.out.println(allMovies);
    }

}
