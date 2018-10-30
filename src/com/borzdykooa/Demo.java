package com.borzdykooa;

import com.borzdykooa.dao.TraineeDao;
import com.borzdykooa.dao.TrainerDao;
import com.borzdykooa.entity.Trainee;
import com.borzdykooa.entity.Trainer;
import org.apache.log4j.Logger;

/*
Класс для демонстрации работы приложения
 */
public class Demo {

    private static final Logger log = Logger.getLogger(Demo.class);

    public static void main(String[] args) {
        Trainer andreiReut = new Trainer("Andrei Reut", "Java", 6);
        Trainer ivanIshchenko = new Trainer("Ivan Ishchenko", "Java", 6);
        TrainerDao.getInstance().save(andreiReut);
        TrainerDao.getInstance().save(ivanIshchenko);

        Trainer savedAndreiReut = TrainerDao.getInstance().getById(1L);
        Trainer savedIvanIshchenko = TrainerDao.getInstance().getById(2L);
        Trainee olgaBorzdyko = new Trainee("Olga Borzdyko", savedIvanIshchenko);
        TraineeDao.getInstance().saveAutocommitTrue(olgaBorzdyko);
        Trainee denisByhovsky = new Trainee("Denis Byhovsky", savedAndreiReut);
        TraineeDao.getInstance().saveAutocommitFalse(denisByhovsky);

        TraineeDao.getInstance().getAllTrainees();
    }
}
