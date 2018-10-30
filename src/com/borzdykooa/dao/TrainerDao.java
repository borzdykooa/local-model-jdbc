package com.borzdykooa.dao;

import com.borzdykooa.connection.Connector;
import com.borzdykooa.entity.Trainer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
Класс, сожержащий методы save и findById для таблицы trainer
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TrainerDao {

    private static final Logger log = Logger.getLogger(TrainerDao.class);

    private static final TrainerDao INSTANCE = new TrainerDao();

    private static final String SAVE =
            "INSERT INTO test_database.trainer (name,language,experience) VALUES (?,?,?)";

    private static final String FIND_BY_ID = "SELECT " +
            "  t.id          AS trainer_id, " +
            "  t.name        AS trainer_name, " +
            "  t.language    AS trainer_language, " +
            "  t.experience  AS trainer_experience " +
            "FROM test_database.trainer t " +
            "WHERE id=?";

    public Trainer getById(Long id) {
        Trainer trainer = null;
        try (Connection connection = Connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                trainer = Trainer.builder()
                        .id(resultSet.getLong("trainer_id"))
                        .name(resultSet.getString("trainer_name"))
                        .language(resultSet.getString("trainer_language"))
                        .experience(resultSet.getInt("trainer_experience"))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trainer;
    }

    public void save(Trainer trainer) {
        try (Connection connection = Connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE)) {
            preparedStatement.setString(1, trainer.getName());
            preparedStatement.setString(2, trainer.getLanguage());
            preparedStatement.setInt(3, trainer.getExperience());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Error!", e);
        }
    }

    public static TrainerDao getInstance() {
        return INSTANCE;
    }
}
