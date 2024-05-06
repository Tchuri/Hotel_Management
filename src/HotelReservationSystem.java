
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class HotelReservationSystem{

    private static final   String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final   String user = "root";
    private static final   String password = "Ab021vf@098";

    public static void main(String[] args) throws ClassNotFoundException, SQLException{
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try{
            Connection connection = DriverManager.getConnection(url, user, password);
            while(true) {
                System.out.println();
                System.out.println("Hotel Reservation System");
                Scanner scanner = new Scanner(System.in);
                System.out.println("1 Reserve a room");
                System.out.println("2 view reservation");
                System.out.println("3 Get Room Number");
                System.out.println("4 Update Reservation");
                System.out.println("5 Delete Reservation");
                System.out.println("0 Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        reserveRoom(connection, scanner);
                        break;
                    case 2:
                        viewReservations(connection);
                        break;
                    case 3:
                        getRoomNumber(connection, scanner);
                        break;
                    case 4:
                        updateReservation(connection, scanner);
                        break;
                    case 5:
                        deleteReservaton(connection, scanner);
                        break;
                    case 0:
                        exit();
                        scanner.close();
                        return;
                    default:
                        System.out.println("invalid choice try again");

                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }


    }

    private static void deleteReservaton(Connection connection, Scanner scanner) {
        try{
            System.out.println("plesae enter reservation id to Delete");
            int reservationId = scanner.nextInt();
            if(!reservationExists(connection, reservationId)){
                System.out.println("Reservation id does'nt exist");
                return;
            }

                String sql2 = "DELETE  from reservations WHERE reservation_id = " + reservationId;
                try (Statement statement = connection.createStatement()) {
                    Integer affectedRows = statement.executeUpdate(sql2);
                    if (affectedRows > 0) {
                        System.out.println("Reservation deleted successfully!");
                    } else {
                        System.out.println("Reservation deletion failed.");
                    }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }

    private static void updateReservation(Connection connection, Scanner scanner) {
        int newRoomNumber;
        String newContactNumber;
        String newGuestName;
        int reservationId;
        try {
            System.out.println("enter reservation id to update: ");
            reservationId = scanner.nextInt();
            scanner.nextLine();

            if (!reservationExists(connection, reservationId)) {
                System.out.println("Id invalid");
                return;

            }
            System.out.print("Enter new guest name: ");
            newGuestName = scanner.nextLine();
            System.out.print("Enter new room number: ");
            newRoomNumber = scanner.nextInt();
            System.out.print("Enter new contact number: ");
            newContactNumber = scanner.next();

            String sql = "UPDATE reservations SET guest_name = '" + newGuestName + "', " +
                    "room_number = " + newRoomNumber + ", " +
                    "contact_number = '" + newContactNumber + "' " +
                    "WHERE reservation_id = " + reservationId;
            try (Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("updated successfully");
                } else {
                    System.out.println("update failed");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }finally {

        }
    }

    private static boolean reservationExists(Connection connection, int reservationId) {
       try {
           String sql2 = "SELECT reservation_id from reservations WHERE reservation_id = " + reservationId;
           try (Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql2)) {
               return resultSet.next();
           } catch (SQLException e) {
               e.printStackTrace();
               return false; // Handle database errors as needed
           }
       }
       finally {

       }
    }

    private static void exit() throws InterruptedException{
        System.out.println("Exiting System");
        int i =5 ;
        while(i!=0){
            System.out.print(".");
            Thread.sleep(500);
            i--;
        }
        System.out.println();
        System.out.println("Thank you for visiting");
    }

    private static void getRoomNumber(Connection connection, Scanner scanner) {

    }

    private static void viewReservations(Connection connection) throws SQLException{
        String sql1 = "SELECT reservation_id, guest_name, room_number, contact_number, reservation_date FROM reservations";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql1)) {

            System.out.println("Current Reservations:");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
            System.out.println("| Reservation ID | Guest           | Room Number   | Contact Number      | Reservation Date         |");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");

            while (resultSet.next()) {
                int reservationId = resultSet.getInt("reservation_id");
                String guestName1 = resultSet.getString("guest_name");
                int roomNumber1 = resultSet.getInt("room_number");
                String contactNumber1 = resultSet.getString("contact_number");
                String reservationDate = resultSet.getTimestamp("reservation_date").toString();

                // Format and display the reservation data in a table-like format
                System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s   |\n",
                        reservationId, guestName1, roomNumber1, contactNumber1, reservationDate);
            }

            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    private static void reserveRoom(Connection connection,Scanner scanner) {
        try {
            System.out.println("Enter guest name: ");
            String guestName = scanner.next();
            scanner.nextLine();
            System.out.println("Enter the room number: ");
            int roomNumber = scanner.nextInt();
            System.out.println("Enter contact number: ");
            int contactNumber = scanner.nextInt();

            String sql = "INSERT INTO reservations (guest_name, room_number, contact_number)" +
                    "VALUES ('" + guestName + "', " + roomNumber + ",'" + contactNumber + "' )";

            try (Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("reservation successfully");
                } else {
                    System.out.println("reservation failed");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        finally {

        }

    }
}