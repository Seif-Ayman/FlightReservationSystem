package GUI;

import Controllers.ThemeManager;
import Models.Ticket;
import Utils.FileManager;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class TicketsPage extends JFrame {
    JFrame frame = new JFrame ("Your Tickets");
    ImageIcon logo = new ImageIcon ("Assets/logo.png");
    JButton backButton = new JButton("<");
    JPanel panel = new JPanel (null);
    JLabel subHeading = new JLabel ("This is,");
    JLabel heading = new JLabel ("<html> Your Reserved <br> Tickets");


    public TicketsPage(){
        String[] ticketIDs = FileManager.GetEveryTicketIDGivenUsername(HomePage.currentUser.getUsername());
        Ticket[] tickets = FileManager.GetEveryTicketGivenUsername ( HomePage.currentUser.getUsername () );
        JButton[] buttons = new JButton[ticketIDs.length];
        int gap = 220;
        for (int i=0 ; i < ticketIDs.length; i++) {
            JButton deleteButton = new JButton("Delete Ticket " + (i + 1));
            deleteButton.setBounds ( 20,gap,340,60 );
            deleteButton.setFont(new Font("Arial", Font.BOLD, 18));
            deleteButton.setForeground ( Color.white );
            deleteButton.setBackground ( Color.decode ( "#DE3341" ) );
            deleteButton.setBorder ( BorderFactory.createEmptyBorder () );

            buttons[i] = new JButton("Ticket " + (i + 1));
            buttons[i].setBounds ( 20,gap,340,60 );
            buttons[i].setFont(new Font("Arial", Font.BOLD, 18));
            buttons[i].setForeground ( Color.white );
            buttons[i].setBackground ( Color.decode ( "#DE3341" ) );
            buttons[i].setBorder ( BorderFactory.createEmptyBorder () );

            panel.add(deleteButton);

            Ticket ticket = tickets[i];

            buttons[i].addActionListener(e -> {
                new TicketGUI (ticket);
            });

            deleteButton.addActionListener(e -> {
                deleteTicket(ticket.getTicketID());
            });

            gap+=80;

        }

        frame.setSize(400, 550);
        frame.setBackground( Color.decode("#FFFFFF"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(logo.getImage());
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        panel.add(subHeading);
        panel.add ( heading );
        panel.add(backButton);

        panel.setBackground ( Color.white );
        backButton.setBounds ( 0,0,50,20 );
        backButton.setBackground ( Color.white );
        backButton.setBorder ( BorderFactory.createEmptyBorder () );
        subHeading.setBounds (20,20,300,50  );
        subHeading.setFont ( new Font ( "SansSerif", Font.BOLD,25 ) );
        subHeading.setForeground ( Color.decode ( "#FD9426" ) );
        heading.setBounds (20,60,360,120  );
        heading.setFont ( new Font ( "SansSerif", Font.BOLD,50 ) );
        heading.setForeground ( Color.decode ( "#0B3E91" ) );

        for (int i=0 ; i < ticketIDs.length; i++){
            buttons[i] = new JButton("Ticket " + (i + 1));
            buttons[i].setBounds ( 20,gap,340,60 );
            buttons[i].setFont(new Font("Arial", Font.BOLD, 18));
            buttons[i].setForeground ( Color.white );
            buttons[i].setBackground ( Color.decode ( "#0B3E91" ) );
            buttons[i].setBorder ( BorderFactory.createEmptyBorder () );

            gap+=80;
            panel.add(buttons[i]);
            Ticket ticket = tickets[i];

            buttons[i].addActionListener(e -> {
                new TicketGUI (ticket);
            });

        }
        frame.add(panel);
        frame.setVisible ( true );

        backButton.addActionListener(e -> {
            new HomePage();
            frame.dispose();
        });

        if ( ThemeManager.isDarkMode ()) {
            setDarkMode();
        } else {
            setLightMode();
        }
    }

    private void setDarkMode() {
        panel.setBackground ( Color.decode ( "#111827" ) );
        backButton.setBackground ( Color.decode ( "#111827" ) );
        backButton.setForeground ( Color.white );
        subHeading.setForeground ( Color.decode ( "#FD9426" ) );
        heading.setForeground ( Color.decode ( "#ffffff" ) );

        ThemeManager.setDarkMode ( true );

    }
    private void setLightMode() {

        panel.setBackground ( Color.white );
        backButton.setBackground ( Color.white );
        backButton.setForeground ( Color.black );
        subHeading.setForeground ( Color.decode ( "#FD9426" ) );
        heading.setForeground ( Color.decode ( "#0B3E91" ) );
        ThemeManager.setDarkMode ( false );
    }

    private void deleteTicket(String ticketID) {
        File inputFile = new File("tickets/" + HomePage.currentUser.getUsername() + "Tickets.txt");
        File tempFile = new File("tickets/TempTickets.txt");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;

            while((currentLine = reader.readLine()) != null) {
                String trimmedLine = currentLine.trim();
                if(trimmedLine.startsWith(ticketID)) continue;
                writer.write(currentLine + System.getProperty("line.separator"));
            }
            writer.close();
            reader.close();

            // Delete the original file before renaming the temporary file
            if (!inputFile.delete()) {
                System.out.println("Failed to delete the original file.");
                return;
            }

            boolean successful = tempFile.renameTo(inputFile);
            System.out.println(successful ? "Ticket deleted successfully" : "Ticket deletion failed");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}

