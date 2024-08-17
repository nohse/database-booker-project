package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Arrays;

public class DBResetDAO {

    private DBConnector dbConnector;
    private ResultSet res;
    private static final List<String> RESET_QUERIES = Arrays.asList(
            "DROP TABLE IF EXISTS Tickets",
            "DROP TABLE IF EXISTS Screenings",
            "DROP TABLE IF EXISTS Bookings",
            "DROP TABLE IF EXISTS Seats",
            "DROP TABLE IF EXISTS Movies",
            "DROP TABLE IF EXISTS Theaters",
            "DROP TABLE IF EXISTS Customers",
            "CREATE SCHEMA IF NOT EXISTS `db2` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci",
            "USE `db2`",
            "CREATE TABLE IF NOT EXISTS Movies (" +
            "  `movie_id` INT NOT NULL AUTO_INCREMENT," +
            "  `title` VARCHAR(45) NULL," +
            "  `duration` INT NULL," +
            "  `rating` INT NULL," +
            "  `director` VARCHAR(45) NULL," +
            "  `actors` VARCHAR(45) NULL," +
            "  `genre` VARCHAR(45) NULL," +
            "  `description` VARCHAR(45) NULL," +
            "  `release_date` DATE NULL," +
            "  `score` FLOAT NULL," +
            "  PRIMARY KEY (`movie_id`))" +
            "ENGINE = InnoDB",
            "CREATE TABLE IF NOT EXISTS `db2`.`Theaters` (" +
            "  `theater_id` INT NOT NULL AUTO_INCREMENT," +
            "  `total_seats` INT NULL," +
            "  `is_active` TINYINT NULL," +
            "  `width` INT NULL," +
            "  `length` INT NULL," +
            "`standard_price` DECIMAL NULL,"+
            "  PRIMARY KEY (`theater_id`))" +
            "ENGINE = InnoDB",
            "CREATE TABLE IF NOT EXISTS `db2`.`Screenings` (" +
            "  `screening_id` INT NOT NULL AUTO_INCREMENT," +
            "  `movie_id` INT NULL," +
            "  `theater_id` INT NULL," +
            "  `start_date` DATE NULL," +
            "  `day_of_week` VARCHAR(45) NULL," +
            "  `session` INT NULL," +
            "  `start_time` TIME NULL," +
            "  PRIMARY KEY (`screening_id`)," +
            "  INDEX `fk_Screenings_Movies_idx` (`movie_id` ASC) VISIBLE," +
            "  INDEX `fk_Screenings_Theaters1_idx` (`theater_id` ASC) VISIBLE," +
            "  CONSTRAINT `fk_Screenings_Movies`" +
            "    FOREIGN KEY (`movie_id`)" +
            "    REFERENCES `db2`.`Movies` (`movie_id`)" +
            "    ON DELETE NO ACTION" +
            "    ON UPDATE NO ACTION," +
            "  CONSTRAINT `fk_Screenings_Theaters1`" +
            "    FOREIGN KEY (`theater_id`)" +
            "    REFERENCES `db2`.`Theaters` (`theater_id`)" +
            "    ON DELETE NO ACTION" +
            "    ON UPDATE NO ACTION)" +
            "ENGINE = InnoDB",
            "CREATE TABLE IF NOT EXISTS `db2`.`Seats` (" +
            "  `seat_id` INT NOT NULL AUTO_INCREMENT," +
            "  `theater_id` INT NULL," +
            "  `is_active` TINYINT NULL," +
            "  PRIMARY KEY (`seat_id`)," +
            "  INDEX `fk_Seats_Theaters1_idx` (`theater_id` ASC) VISIBLE," +
            "  CONSTRAINT `fk_Seats_Theaters1`" +
            "    FOREIGN KEY (`theater_id`)" +
            "    REFERENCES `db2`.`Theaters` (`theater_id`)" +
            "    ON DELETE NO ACTION" +
            "    ON UPDATE NO ACTION)" +
            "ENGINE = InnoDB",
            "CREATE TABLE IF NOT EXISTS `db2`.`Customers` (" +
            "  `customer_id` INT NOT NULL AUTO_INCREMENT," +
            "  `name` VARCHAR(45) NULL," +
            "  `phone_number` VARCHAR(45) NULL," +
            "  `email` VARCHAR(45) NULL," +
            "  PRIMARY KEY (`customer_id`))" +
            "ENGINE = InnoDB",
            "CREATE TABLE IF NOT EXISTS `db2`.`Bookings` (" +
            "  `booking_id` INT NOT NULL AUTO_INCREMENT," +
            "  `payment_method` VARCHAR(45) NULL," +
            "  `payment_status` VARCHAR(45) NULL," +
            "  `amount` DECIMAL NULL," +
            "  `customer_id` INT NULL," +
            "  `payment_date` DATE NULL," +
            "  PRIMARY KEY (`booking_id`)," +
            "  INDEX `fk_Bookings_Customers1_idx` (`customer_id` ASC) VISIBLE," +
            "  CONSTRAINT `fk_Bookings_Customers1`" +
            "    FOREIGN KEY (`customer_id`)" +
            "    REFERENCES `db2`.`Customers` (`customer_id`)" +
            "    ON DELETE NO ACTION" +
            "    ON UPDATE NO ACTION)" +
            "ENGINE = InnoDB",
            //"CREATE INDEX idx_standard_price ON `dbtest`.`Theaters` (`standard_price`);",
            "CREATE TABLE IF NOT EXISTS `db2`.`Tickets` (" +
            "  `ticket_id` INT NOT NULL AUTO_INCREMENT," +
            "  `screening_id` INT NULL," +
            "  `seat_id` INT NULL," +
            "  `theater_id` INT NULL," +
            "  `booking_id` INT NULL," +
            "  `is_issued` TINYINT NULL," +
            "  `standard_price` DECIMAL NULL," +
            "  `sale_price` DECIMAL NULL," +
            "  PRIMARY KEY (`ticket_id`)," +
            "  INDEX `fk_Tickets_Screenings1_idx` (`screening_id` ASC) VISIBLE," +
            "  INDEX `fk_Tickets_Theaters1_idx` (`theater_id` ASC) VISIBLE," +
            //"INDEX `fk_Tickets_Theaters2_idx` (`standard_price` ASC) VISIBLE,"+
            "  INDEX `fk_Tickets_Seats1_idx` (`seat_id` ASC) VISIBLE," +
            "  INDEX `fk_Tickets_Bookings1_idx` (`booking_id` ASC) VISIBLE," +
            "  CONSTRAINT `fk_Tickets_Screenings1`" +
            "    FOREIGN KEY (`screening_id`)" +
            "    REFERENCES `db2`.`Screenings` (`screening_id`)" +
            "    ON DELETE NO ACTION" +
            "    ON UPDATE NO ACTION," +
            "  CONSTRAINT `fk_Tickets_Theaters1`" +
            "    FOREIGN KEY (`theater_id`)" +
            "    REFERENCES `db2`.`Theaters` (`theater_id`)" +
            "    ON DELETE NO ACTION" +
            "    ON UPDATE NO ACTION," +
//            " CONSTRAINT `fk_Tickets_Theaters2`"
//            + "    FOREIGN KEY (`standard_price`)"
//            + "    REFERENCES `dbtest`.`Theaters` (`standard_price`)"
//            + "    ON DELETE NO ACTION"
//            + "    ON UPDATE NO ACTION,"+
            "  CONSTRAINT `fk_Tickets_Seats1`" +
            "    FOREIGN KEY (`seat_id`)" +
            "    REFERENCES `db2`.`Seats` (`seat_id`)" +
            "    ON DELETE NO ACTION" +
            "    ON UPDATE NO ACTION," +
            "  CONSTRAINT `fk_Tickets_Bookings1`" +
            "    FOREIGN KEY (`booking_id`)" +
            "    REFERENCES `db2`.`Bookings` (`booking_id`)" +
            "    ON DELETE NO ACTION" +
            "    ON UPDATE NO ACTION)" +
            "ENGINE = InnoDB",
            "INSERT INTO Movies (movie_id, title, duration, rating, director, actors, genre, description, release_date, score) VALUES " +
            "(1, 'The Great Adventure', 120, 12, 'John Smith', 'Actor A, Actor B', 'Action', 'An epic action movie.', '2023-05-01', 8.5), " +
            "(2, 'Romantic Getaway', 95, 12, 'Jane Doe', 'Actor C, Actor D', 'Romance', 'A romantic movie.', '2023-02-14', 7.8), " +
            "(3, 'Space Journey', 130, 12, 'Alice Johnson', 'Actor E, Actor F', 'Sci-Fi', 'A journey through space.', '2023-07-20', 9.0), " +
            "(4, 'Haunted House', 105, 19, 'Bob Brown', 'Actor G, Actor H', 'Horror', 'A terrifying horror movie.', '2023-10-31', 6.9), " +
            "(5, 'Comedy Night', 110, 15, 'Charlie Green', 'Actor I, Actor J', 'Comedy', 'A hilarious comedy movie.', '2023-04-01', 8.2), " +
            "(6, 'Mystery Case', 115, 8, 'Diana White', 'Actor K, Actor L', 'Mystery', 'A mysterious thriller.', '2023-03-15', 8.0), " +
            "(7, 'Historical Drama', 140, 12, 'Edward Black', 'Actor M, Actor N', 'Drama', 'A captivating drama.', '2023-06-10', 7.5), " +
            "(8, 'Animated Fun', 85, 15, 'Fiona Blue', 'Actor O, Actor P', 'Animation', 'A fun animated movie.', '2023-08-25', 8.9), " +
            "(9, 'Superheroes Unite', 125, 0, 'George Red', 'Actor Q, Actor R', 'Action', 'Superheroes saving the world.', '2023-11-05', 8.3), " +
            "(10, 'Fantasy Realm', 135, 0, 'Hannah Purple', 'Actor S, Actor T', 'Fantasy', 'A magical fantasy movie.', '2023-12-20', 7.7), " +
            "(11, 'Thrilling Heist', 100, 15, 'Ian Grey', 'Actor U, Actor V', 'Thriller', 'A thrilling heist story.', '2023-01-22', 7.9), " +
            "(12, 'Documentary Insights', 90, 12, 'Julia Yellow', 'Actor W, Actor X', 'Documentary', 'An insightful documentary.', '2023-09-15', 8.4);",
            "INSERT INTO Theaters (theater_id, total_seats, is_active, width, length, standard_price) VALUES " +
            "(1, 100, true, 10, 10, 10.00), " +
            "(2, 150, true, 15, 10, 12.00), " +
            "(3, 200, true, 20, 10, 15.00), " +
            "(4, 120, true, 12, 10, 14.00), " +
            "(5, 180, true, 18, 10, 12.00), " +
            "(6, 160, true, 16, 10, 18.00), " +
            "(7, 140, true, 14, 10, 12.00), " +
            "(8, 130, true, 13, 10, 14.00), " +
            "(9, 110, true, 11, 10, 16.00), " +
            "(10, 170, true, 17, 10, 18.00), " +
            "(11, 190, true, 19, 10, 12.00), " +
            "(12, 210, true, 21, 10, 14.00);",
            "INSERT INTO Screenings (screening_id, movie_id, theater_id, start_date, day_of_week, session, start_time) VALUES\r\n"
            + "(1, 1, 1, '2024-05-01', 'Monday', 1, '10:00:00'),\r\n"
            + "(2, 1, 2, '2024-05-01', 'Monday', 2, '14:00:00'),\r\n"
            + "(3, 1, 3, '2024-05-02', 'Tuesday', 1, '18:00:00'),\r\n"
            + "\r\n"
            + "(4, 2, 4, '2024-05-03', 'Wednesday', 1, '10:00:00'),\r\n"
            + "(5, 2, 5, '2024-05-03', 'Wednesday', 2, '14:00:00'),\r\n"
            + "\r\n"
            + "(6, 3, 6, '2024-05-04', 'Thursday', 1, '12:00:00'),\r\n"
            + "(7, 3, 7, '2024-05-04', 'Thursday', 2, '16:00:00'),\r\n"
            + "(8, 3, 8, '2024-05-05', 'Friday', 1, '20:00:00'),\r\n"
            + "\r\n"
            + "(9, 4, 9, '2024-05-06', 'Saturday', 1, '11:00:00'),\r\n"
            + "(10, 4, 10, '2024-05-06', 'Saturday', 2, '15:00:00'),\r\n"
            + "\r\n"
            + "(11, 5, 11, '2024-05-07', 'Sunday', 1, '13:00:00'),\r\n"
            + "(12, 5, 12, '2024-05-07', 'Sunday', 2, '17:00:00'),\r\n"
            + "\r\n"
            + "(13, 6, 1, '2024-05-08', 'Monday', 1, '09:00:00'),\r\n"
            + "(14, 6, 2, '2024-05-08', 'Monday', 2, '19:00:00'),\r\n"
            + "(15, 6, 3, '2024-05-09', 'Tuesday', 1, '15:00:00'),\r\n"
            + "\r\n"
            + "(16, 7, 4, '2024-05-10', 'Wednesday', 1, '12:00:00'),\r\n"
            + "(17, 7, 5, '2024-05-10', 'Wednesday', 2, '15:00:00'),\r\n"
            + "\r\n"
            + "(18, 8, 6, '2024-05-11', 'Thursday', 1, '11:00:00'),\r\n"
            + "(19, 8, 7, '2024-05-11', 'Thursday', 2, '14:00:00'),\r\n"
            + "\r\n"
            + "(20, 9, 8, '2024-05-12', 'Friday', 1, '10:00:00'),\r\n"
            + "(21, 9, 9, '2024-05-12', 'Friday', 2, '13:00:00'),\r\n"
            + "(22, 9, 10, '2024-05-13', 'Saturday', 1, '17:00:00'),\r\n"
            + "\r\n"
            + "(23, 10, 11, '2024-05-14', 'Sunday', 1, '12:00:00'),\r\n"
            + "(24, 10, 12, '2024-05-14', 'Sunday', 2, '15:00:00'),\r\n"
            + "\r\n"
            + "(25, 11, 1, '2024-05-15', 'Monday', 1, '11:00:00'),\r\n"
            + "(26, 11, 2, '2024-05-15', 'Monday', 2, '14:00:00'),\r\n"
            + "(27, 11, 3, '2024-05-16', 'Tuesday', 1, '13:00:00'),\r\n"
            + "\r\n"
            + "(28, 12, 4, '2024-05-17', 'Wednesday', 1, '10:00:00'),\r\n"
            + "(29, 12, 5, '2024-05-17', 'Wednesday', 2, '13:00:00'),\r\n"
            + "(30, 12, 6, '2024-05-18', 'Thursday', 1, '18:00:00');\r\n"
            + "",
            "INSERT INTO Seats (seat_id, theater_id, is_active) VALUES " +
                    "(1, 12, true), (2, 12, true), (3, 12, true), (4, 12, true), (5, 12, true), (6, 12, true), (7, 12, true), (8, 12, true), (9, 12, true), " +
                    "(10, 12, true), (11, 12, true), (12, 12, true), (13, 12, true), (14, 12, true), (15, 12, true), (16, 12, true), (17, 12, true), " +
                    "(18, 12, true), (19, 12, true), (20, 12, true), (21, 12, true), (22, 12, true), (23, 12, true), (24, 12, true), (25, 12, true), " +
                    "(26, 12, true), (27, 12, true), (28, 12, true), (29, 12, true), (30, 12, true), (31, 12, true), (32, 12, true), (33, 12, true), " +
                    "(34, 12, true), (35, 12, true), (36, 12, true), (37, 12, true), (38, 12, true), (39, 12, true), (40, 12, true), (41, 12, true), " +
                    "(42, 12, true), (43, 12, true), (44, 12, true), (45, 12, true), (46, 12, true), (47, 12, true), (48, 12, true), (49, 12, true), " +
                    "(50, 12, true), (51, 12, true), (52, 12, true), (53, 12, true), (54, 12, true), (55, 12, true), (56, 12, true), (57, 12, true), " +
                    "(58, 12, true), (59, 12, true), (60, 12, true), (61, 12, true), (62, 12, true), (63, 12, true), (64, 12, true), (65, 12, true), " +
                    "(66, 12, true), (67, 12, true), (68, 12, true), (69, 12, true), (70, 12, true), (71, 12, true), (72, 12, true), (73, 12, true), " +
                    "(74, 12, true), (75, 12, true), (76, 12, true), (77, 12, true), (78, 12, true), (79, 12, true), (80, 12, true), (81, 12, true), " +
                    "(82, 12, true), (83, 12, true), (84, 12, true), (85, 12, true), (86, 12, true), (87, 12, true), (88, 12, true), (89, 12, true), " +
                    "(90, 12, true), (91, 12, true), (92, 12, true), (93, 12, true), (94, 12, true), (95, 12, true), (96, 12, true), (97, 12, true), " +
                    "(98, 12, true), (99, 12, true), (100, 12, true), (101, 12, true), (102, 12, true), (103, 12, true), (104, 12, true), (105, 12, true), " +
                    "(106, 12, true), (107, 12, true), (108, 12, true), (109, 12, true), (110, 12, true), (111, 12, true), (112, 12, true), (113, 12, true), " +
                    "(114, 12, true), (115, 12, true), (116, 12, true), (117, 12, true), (118, 12, true), (119, 12, true), (120, 12, true), (121, 12, true), " +
                    "(122, 12, true), (123, 12, true), (124, 12, true), (125, 12, true), (126, 12, true), (127, 12, true), (128, 12, true), (129, 12, true), " +
                    "(130, 12, true), (131, 12, true), (132, 12, true), (133, 12, true), (134, 12, true), (135, 12, true), (136, 12, true), (137, 12, true), " +
                    "(138, 12, true), (139, 12, true), (140, 12, true), (141, 12, true), (142, 12, true), (143, 12, true), (144, 12, true), (145, 12, true), " +
                    "(146, 12, true), (147, 12, true), (148, 12, true), (149, 12, true), (150, 12, true), (151, 12, true), (152, 12, true), (153, 12, true), " +
                    "(154, 12, true), (155, 12, true), (156, 12, true), (157, 12, true), (158, 12, true), (159, 12, true), (160, 12, true), (161, 12, true), " +
                    "(162, 12, true), (163, 12, true), (164, 12, true), (165, 12, true), (166, 12, true), (167, 12, true), (168, 12, true), (169, 12, true), " +
                    "(170, 12, true), (171, 12, true), (172, 12, true), (173, 12, true), (174, 12, true), (175, 12, true), (176, 12, true), (177, 12, true), " +
                    "(178, 12, true), (179, 12, true), (180, 12, true), (181, 12, true), (182, 12, true), (183, 12, true), (184, 12, true), (185, 12, true), " +
                    "(186, 12, true), (187, 12, true), (188, 12, true), (189, 12, true), (190, 12, true), (191, 12, true), (192, 12, true), (193, 12, true), " +
                    "(194, 12, true), (195, 12, true), (196, 12, true), (197, 12, true), (198, 12, true), (199, 12, true), (200, 12, true), (201, 12, true), " +
                    "(202, 12, true), (203, 12, true), (204, 12, true), (205, 12, true), (206, 12, true), (207, 12, true), (208, 12, true), (209, 12, true), (210, 12, true);",
            "INSERT INTO Customers (customer_id, name, phone_number, email) VALUES " +
            "(1, 'John Doe', '010-1111-2222', 'johndoe@example.com'), " +
            "(2, 'Jane Smith', '010-3333-4444', 'janesmith@example.com'), " +
            "(3, 'Alice Brown', '010-5555-6666', 'alicebrown@example.com'), " +
            "(4, 'Bob Johnson', '010-7777-8888', 'bobjohnson@example.com'), " +
            "(5, 'Charlie Davis', '010-9999-0000', 'charliedavis@example.com'), " +
            "(6, 'Diana Evans', '010-1122-3344', 'dianaevans@example.com'), " +
            "(7, 'Edward Green', '010-5566-7788', 'edwardgreen@example.com'), " +
            "(8, 'Fiona Hall', '010-9900-1122', 'fionahall@example.com'), " +
            "(9, 'George King', '010-2233-4455', 'georgeking@example.com'), " +
            "(10, 'Hannah Lee', '010-6677-8899', 'hannahlee@example.com'), " +
            "(11, 'Ian Martinez', '010-3344-5566', 'ianmartinez@example.com'), " +
            "(12, 'Julia Nelson', '010-7788-9900', 'julianelson@example.com');",
            "INSERT INTO Bookings (Booking_id, payment_method, payment_status, amount, customer_id, payment_date) VALUES " +
            "(1, 'Credit Card', 'Paid', 16.00, 1, '2024-05-01'), " +
            "(2, 'Credit Card', 'Paid', 16.00, 2, '2024-05-01'), " +
            "(3, 'PayPal', 'Paid', 22.00, 3, '2024-05-02'), " +
            "(4, 'PayPal', 'Paid', 22.00, 4, '2024-05-02'), " +
            "(5, 'Credit Card', 'Paid', 28.00, 5, '2024-05-03'), " +
            "(6, 'Credit Card', 'Paid', 28.00, 6, '2024-05-03'), " +
            "(7, 'PayPal', 'Paid', 16.00, 7, '2024-05-04'), " +
            "(8, 'PayPal', 'Paid', 16.00, 8, '2024-05-04'), " +
            "(9, 'Credit Card', 'Paid', 20.00, 9, '2024-05-05'), " +
            "(10, 'Credit Card', 'Paid', 20.00, 10, '2024-05-05'), " +
            "(11, 'PayPal', 'Paid', 26.00, 11, '2024-05-06'), " +
            "(12, 'PayPal', 'Paid', 26.00, 12, '2024-05-06');",
            "INSERT INTO Tickets (ticket_id, screening_id, theater_id, seat_id, booking_id, is_issued, standard_price, sale_price) VALUES " +
            "(1, 1, 1, 1, 1, true, 10.00, 8.00), " +
            "(2, 1, 1, 2, 1, true, 10.00, 8.00), " +
            "(3, 2, 1, 3, 2, true, 10.00, 8.00), " +
            "(4, 2, 1, 4, 2, true, 10.00, 8.00), " +
            "(5, 3, 2, 5, 3, true, 12.00, 10.00), " +
            "(6, 3, 2, 6, 3, true, 12.00, 10.00), " +
            "(7, 4, 2, 7, 4, true, 12.00, 10.00), " +
            "(8, 4, 2, 8, 4, true, 12.00, 10.00), " +
            "(9, 5, 3, 9, 5, true, 15.00, 13.00), " +
            "(10, 5, 3, 10, 5, true, 15.00, 13.00), " +
            "(11, 6, 3, 11, 6, true, 15.00, 13.00), " +
            "(12, 6, 3, 12, 6, true, 15.00, 13.00);"
        );

    public DBResetDAO() {
        dbConnector = DBConnector.getInstance();
    }

    public void resetDatabase() throws SQLException {

        dbConnector.connectDB("root", "1234");

        try {
            for (String query : RESET_QUERIES) {
                dbConnector.update(query);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            dbConnector.disconnectDB();
        }
    }
    public void updateDatabase(String query) throws SQLException {

        dbConnector.connectDB("root", "1234");

        try {
                dbConnector.update(query);
        } catch (SQLException e) {
            throw e;
        } finally {
            dbConnector.disconnectDB();
        }
    }
    public ResultSet selectDatabase(String query) throws SQLException {

        dbConnector.connectDB("root", "1234");

        try {
                res= dbConnector.select(query);
                return res;
        } catch (SQLException e) {
            throw e;
        }
    }
    public void disconnect() {
    	dbConnector.disconnectDB();
    }
}
