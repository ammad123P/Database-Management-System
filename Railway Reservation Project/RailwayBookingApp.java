import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RailwayBookingApp extends JFrame implements ActionListener {
    private JComboBox<String> trainCombo, fromCombo, toCombo, berthCombo;
    private JTextField nameField, ageField, cnicField, seatsField, dateField;
    private JButton bookButton;
    private JLabel priceLabel;

    private String[] trainNames = {"Khyber Mail", "GreenLine", "Tezgam Express", "Allama Iqbal Express", "Hazara Express",
                                    "Awam Express", "Karachi Express", "Millat Express", "Bahauddin Zikria Express", "Shalimar Express",
                                    "Pak Buisness", "Fareed Express", "Jaffar Express", "Karakoram Express", "Pakistan Express",
                                    "Rehman Baba Express", "Sir Sayyed Express", "Subak Raftar", "Subak Khurram", "Rawal Express"};

    private String[] cities = {"Karachi Cantt", "Lahore Jn", "Sialkot Jn", "Rawalpindi", "Peshawar Cantt"};
    private String[] berths = {"Berth Upper", "Berth Middle", "Berth Lower", "Seat"};

    private int[][] fares = {
        {0, 1500, 1400, 2000, 1800},
        {1500, 0, 1300, 1800, 1600},
        {1400, 1300, 0, 1700, 1500},
        {2000, 1800, 1700, 0, 1900},
        {1800, 1600, 1500, 1900, 0}
    };

    private Connection connection;

    public RailwayBookingApp() {
        setTitle("Railway Booking App");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeDB();

        setLayout(new BorderLayout());

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon("vecteezy_ai-generated-a-blue-and-black-train-is-on-the-tracks-with-a_35782783.jpg");
                g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        backgroundPanel.setLayout(new FlowLayout());

        JPanel mainPanel = new JPanel(new GridLayout(10, 1));

        JPanel passengerPanel = new JPanel(new FlowLayout());
        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(15);
        JLabel ageLabel = new JLabel("Age:");
        ageField = new JTextField(5);
        JLabel cnicLabel = new JLabel("CNIC:");
        cnicField = new JTextField(15);
        passengerPanel.add(nameLabel);
        passengerPanel.add(nameField);
        passengerPanel.add(ageLabel);
        passengerPanel.add(ageField);
        passengerPanel.add(cnicLabel);
        passengerPanel.add(cnicField);

        JPanel trainPanel = new JPanel(new FlowLayout());
        JLabel trainLabel = new JLabel("Select Train:");
        trainCombo = new JComboBox<>(trainNames);
        trainPanel.add(trainLabel);
        trainPanel.add(trainCombo);

        JPanel stationPanel = new JPanel(new FlowLayout());
        JLabel fromLabel = new JLabel("From:");
        fromCombo = new JComboBox<>(cities);
        JLabel toLabel = new JLabel("To:");
        toCombo = new JComboBox<>(cities);
        stationPanel.add(fromLabel);
        stationPanel.add(fromCombo);
        stationPanel.add(toLabel);
        stationPanel.add(toCombo);

        JPanel datePanel = new JPanel(new FlowLayout());
        JLabel dateLabel = new JLabel("Date:");
        dateField = new JTextField(10);
        datePanel.add(dateLabel);
        datePanel.add(dateField);

        JPanel berthPanel = new JPanel(new FlowLayout());
        JLabel berthLabel = new JLabel("Berth:");
        berthCombo = new JComboBox<>(berths);
        berthPanel.add(berthLabel);
        berthPanel.add(berthCombo);

        JPanel seatsPanel = new JPanel(new FlowLayout());
        JLabel seatsLabel = new JLabel("Number of Seats:");
        seatsField = new JTextField(5);
        seatsPanel.add(seatsLabel);
        seatsPanel.add(seatsField);

        JPanel farePanel = new JPanel(new FlowLayout());
        JLabel fareLabel = new JLabel("Fare:");
        priceLabel = new JLabel();
        farePanel.add(fareLabel);
        farePanel.add(priceLabel);

        bookButton = new JButton("Book Now");
        bookButton.addActionListener(this);

        mainPanel.add(passengerPanel);
        mainPanel.add(trainPanel);
        mainPanel.add(stationPanel);
        mainPanel.add(datePanel);
        mainPanel.add(berthPanel);
        mainPanel.add(seatsPanel);
        mainPanel.add(farePanel);
        mainPanel.add(bookButton);

        backgroundPanel.add(mainPanel);

        mainPanel.setOpaque(false);

        add(backgroundPanel);
    }

    private void initializeDB() {
        String url = "jdbc:mysql://localhost:3306/railway_booking_db";
        String user = "root";
        String password = "1234";

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Book Now")) {
            try {
                String train = (String) trainCombo.getSelectedItem();
                String fromCity = (String) fromCombo.getSelectedItem();
                String toCity = (String) toCombo.getSelectedItem();
                String date = dateField.getText();
                String berth = (String) berthCombo.getSelectedItem();
                int seats = Integer.parseInt(seatsField.getText());

                int fromIndex = fromCombo.getSelectedIndex();
                int toIndex = toCombo.getSelectedIndex();
                int fare = fares[fromIndex][toIndex];
                int totalFare = fare * seats;

                String name = nameField.getText();
                int age = Integer.parseInt(ageField.getText());
                String cnic = cnicField.getText();

                String sql = "INSERT INTO tickets (train, from_city, to_city, date, berth, seats, fare, total_fare, passenger_name, passenger_age, cnic_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, train);
                statement.setString(2, fromCity);
                statement.setString(3, toCity);
                statement.setString(4, date);
                statement.setString(5, berth);
                statement.setInt(6, seats);
                statement.setInt(7, fare);
                statement.setInt(8, totalFare);
                statement.setString(9, name);
                statement.setInt(10, age);
                statement.setString(11, cnic);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, "Ticket booked successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to book ticket", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid age and number of seats", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new RailwayBookingApp().setVisible(true);
            }
        });
    }
}
