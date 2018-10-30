package com.borzdykooa.dao;

import com.borzdykooa.connection.Connector;
import com.borzdykooa.entity.Trainee;
import com.borzdykooa.entity.Trainer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
Класс, сожержащий метод save для таблицы trainee
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TraineeDao {

    private static final TraineeDao INSTANCE = new TraineeDao();

    private static final Logger log = Logger.getLogger(TraineeDao.class);

    private static final String SAVE =
            "INSERT INTO test_database.trainee (name,trainer_id) VALUES (?,(SELECT id FROM test_database.trainer WHERE id = ?))";

    private static final String GET_ALL_TRAINEES =
            "SELECT " +
                    "te.id          AS trainee_id, " +
                    "te.name        AS trainee_name, " +
                    "tr.id AS trainer_id, " +
                    "tr.name AS trainer_name, " +
                    "tr.language AS trainer_language, " +
                    "tr.experience AS trainer_experience " +
                    "FROM test_database.trainee te " +
                    "INNER JOIN test_database.trainer tr ON te.trainer_id=tr.id";

    public void saveAutocommitTrue(Trainee trainee) {
        log.info("Method saveAutocommitTrue is called in TraineeDao");
        try (Connection connection = Connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE)) {
            preparedStatement.setString(1, trainee.getName());
            preparedStatement.setLong(2, trainee.getTrainer().getId());
            preparedStatement.executeUpdate();
            log.info("Trainee " + trainee.getName() + " has been saved successfully!");
        } catch (SQLException e) {
            log.error("Error!", e);
        }
    }

    public void saveAutocommitFalse(Trainee trainee) {
        log.info("Method saveAutocommitFalse is called in TraineeDao");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = Connector.getConnection();
            preparedStatement = connection.prepareStatement(SAVE);
            connection.setAutoCommit(false);
            preparedStatement.setString(1, trainee.getName());
            preparedStatement.setLong(2, trainee.getTrainer().getId());
            preparedStatement.executeUpdate();
            connection.commit();
            log.info("Trainee " + trainee.getName() + " has been saved successfully!");
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException e1) {
                log.error("Error!", e);
            }
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                log.error("Error!", e);
            }
        }
    }

    public List<Trainee> getAllTrainees() {
        List<Trainee> trainees = new ArrayList<Trainee>();
        try (Connection connection = Connector.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(GET_ALL_TRAINEES);

            while (resultSet.next()) {
                Trainee trainee = Trainee.builder()
                        .id(resultSet.getLong("trainee_id"))
                        .name(resultSet.getString("trainee_name"))
                        .trainer(Trainer.builder()
                                .id(resultSet.getLong("trainer_id"))
                                .name(resultSet.getString("trainer_name"))
                                .language(resultSet.getString("trainer_language"))
                                .experience(resultSet.getInt("trainer_experience"))
                                .build())
                        .build();

                trainees.add(trainee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Error!", e);
        }
        if (trainees.size() != 0) {
            log.info("List of trainees: " + trainees.toString());
        }
        return trainees;
    }

    public static TraineeDao getInstance() {
        return INSTANCE;
    }
}
