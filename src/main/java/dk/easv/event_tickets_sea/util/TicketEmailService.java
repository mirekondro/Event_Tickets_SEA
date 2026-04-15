package dk.easv.event_tickets_sea.util;

import dk.easv.event_tickets_sea.model.Category;
import dk.easv.event_tickets_sea.model.Event;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class TicketEmailService {
    private static TicketEmailService instance;
    private final EmailSettings settings;

    private TicketEmailService() {
        this.settings = EmailSettings.load();
    }

    public static TicketEmailService getInstance() {
        if (instance == null) {
            instance = new TicketEmailService();
        }
        return instance;
    }

    public boolean isConfigured() {
        return settings.isConfigured();
    }

    public void sendTicketEmail(String recipientEmail,
                                String customerName,
                                Event event,
                                Category category,
                                int quantity,
                                String ticketCode,
                                String issuedBy) {
        if (!settings.isConfigured()) {
            throw new IllegalStateException("Email is not configured. Create config/email.settings or set SMTP_* environment variables.");
        }

        System.out.println("DEBUG - Email Settings:");
        System.out.println("  Host: " + settings.getHost());
        System.out.println("  Port: " + settings.getPort());
        System.out.println("  Username: " + settings.getUsername());
        System.out.println("  Password: " + (settings.getPassword() != null ? "***" : "null"));
        System.out.println("  From: " + settings.getFromAddress());

        Properties mailProps = new Properties();
        mailProps.put("mail.smtp.host", settings.getHost());
        mailProps.put("mail.smtp.port", String.valueOf(settings.getPort()));
        mailProps.put("mail.smtp.auth", String.valueOf(settings.isAuth()));
        mailProps.put("mail.smtp.starttls.enable", String.valueOf(settings.isStartTls()));
        mailProps.put("mail.smtp.ssl.trust", settings.getHost());

        Session session;
        if (settings.isAuth()) {
            session = Session.getInstance(mailProps, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(settings.getUsername(), settings.getPassword());
                }
            });
        } else {
            session = Session.getInstance(mailProps);
        }

        session.setDebug(true);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(settings.getFromAddress()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail, false));
            message.setSubject("Your ticket for " + event.getEventName());
            message.setText(buildTicketEmailBody(customerName, event, category, quantity, ticketCode, issuedBy));

            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send ticket email: " + e.getMessage(), e);
        }
    }

    private String buildTicketEmailBody(String customerName,
                                        Event event,
                                        Category category,
                                        int quantity,
                                        String ticketCode,
                                        String issuedBy) {
        return "Hello " + customerName + ",\n\n" +
                "your ticket has been issued successfully.\n\n" +
                "Event: " + event.getEventName() + "\n" +
                "Start: " + event.getStartDateTimeFormatted() + "\n" +
                "End: " + event.getEndDateTimeFormatted() + "\n" +
                "Location: " + event.getLocation() + "\n" +
                "Category: " + category.getCategoryName() + "\n" +
                "Price: " + category.getPriceFormatted() + "\n" +
                "Quantity: " + quantity + "\n" +
                "Ticket Code: " + ticketCode + "\n" +
                "Issued by: " + issuedBy + "\n\n" +
                "Please keep this email as your proof of purchase.\n" +
                "Best regards,\nEvent Tickets SEA";
    }
}

