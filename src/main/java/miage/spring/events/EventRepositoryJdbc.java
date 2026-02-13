package miage.spring.events;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;

@Repository
public class EventRepositoryJdbc implements EventRepository {

    private final DataSource db;

    public EventRepositoryJdbc(DataSource db) {
        this.db = db;
    }

    @PostConstruct
    public void init() {
        this.createEventsTableIfNotExists();
    }

    @Override
    public void save(Event event) {
        String sql = "INSERT INTO events (name) VALUES (?)";
        try (var connection = db.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, event.getName());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Event event) {
        String sql = "UPDATE events SET name = ? WHERE id = ?";
        try (var connection = db.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, event.getName());
            preparedStatement.setLong(2, event.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM events WHERE id = ?";
        try (var connection = db.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Event findById(Long id) {
        String sql = "SELECT * FROM events WHERE id = ?";
        try (var connection = db.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToEvent(resultSet);
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Event> findAll() {
        List<Event> events = new ArrayList<>();
        try (final var connection = db.getConnection()) {
            String sql = "SELECT * FROM events";
            var statement = connection.prepareStatement(sql);
            final var res = statement.executeQuery();
            while (res.next()) {
                Event event = mapResultSetToEvent(res);
                events.add(event);
            }
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return events;
    }

    private Event mapResultSetToEvent(ResultSet resultSet) throws SQLException {
        Event event = new Event();
        event.setId(resultSet.getLong("id"));
        event.setName(resultSet.getString("name"));
        return event;
    }

    private void createEventsTableIfNotExists() {
        try (var connection = db.getConnection()) {
            String sql = "CREATE TABLE IF NOT EXISTS events ("
                    + "id SERIAL PRIMARY KEY,"
                    + "name VARCHAR(255) NOT NULL)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
