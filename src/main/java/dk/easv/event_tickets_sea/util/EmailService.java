package dk.easv.event_tickets_sea.util;

import dk.easv.event_tickets_sea.model.Category;
import dk.easv.event_tickets_sea.model.Event;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class EmailService {

    private static EmailService instance;

    private String smtpHost;
    private String smtpPort;
    private String smtpUser;
    private String smtpPassword;
    private boolean smtpAuth;
    private boolean smtpStartTls;

    private static final String PROP_FILE = "config/email.settings";

    private EmailService() {
        loadConfig();
    }

    public static EmailService getInstance() {
        if (instance == null) {
            instance = new EmailService();
        }
        return instance;
    }

    private void loadConfig() {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream(PROP_FILE));
            smtpHost     = props.getProperty("SMTPHost",     "smtp.gmail.com");
            smtpPort     = props.getProperty("SMTPPort",     "587");
            smtpUser     = props.getProperty("SMTPUser",     "");
            smtpPassword = props.getProperty("SMTPPassword", "");
            smtpAuth     = Boolean.parseBoolean(props.getProperty("SMTPAuth",     "true"));


            // DEBUG - remove after fixing
            System.out.println("SMTP loaded: host=" + smtpHost + " user=" + smtpUser + " passEmpty=" + smtpPassword.isEmpty());
        } catch (IOException e) {
            System.err.println("EmailService: Could not load SMTP config: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sends a ticket confirmation email to the customer.
     * Returns true if sent successfully, false otherwise.
     */
    public boolean sendTicketEmail(Event event, Category category,
                                   String customerName, String customerEmail,
                                   int quantity, String issuedBy, String ticketId) {
        if (smtpUser.isEmpty() || smtpPassword.isEmpty()) {
            System.err.println("EmailService: SMTP credentials not configured in config.settings");
            return false;
        }

        Properties mailProps = new Properties();
        mailProps.put("mail.smtp.host",            smtpHost);
        mailProps.put("mail.smtp.port",            smtpPort);
        mailProps.put("mail.smtp.auth",            String.valueOf(smtpAuth));
        mailProps.put("mail.smtp.socketFactory.port",     smtpPort);
        mailProps.put("mail.smtp.socketFactory.class",    "javax.net.ssl.SSLSocketFactory");
        mailProps.put("mail.smtp.socketFactory.fallback", "false");

        Session session = Session.getInstance(mailProps, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUser, smtpPassword);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(smtpUser, "Event Tickets SEA"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(customerEmail));
            message.setSubject("Your ticket for " + event.getEventName());
            message.setContent(buildHtmlBody(event, category, customerName, quantity, issuedBy, ticketId), "text/html; charset=UTF-8");

            Transport.send(message);
            System.out.println("EmailService: Ticket email sent to " + customerEmail);
            return true;
        } catch (Exception e) {
            System.err.println("EmailService: Failed to send email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }


    }

    private String buildHtmlBody(Event event, Category category,
                                 String customerName, int quantity,
                                 String issuedBy, String ticketId) {
        String categoryName = category != null ? category.getCategoryName() : "Regular";
        String priceStr     = category != null ? category.getPriceFormatted() : "-";
        String startStr     = event.getStartDateTimeFormatted();
        String endStr       = event.getEndDateTimeFormatted();
        String location     = event.getLocation() != null ? event.getLocation() : "-";
        String notes        = event.getNotes() != null ? event.getNotes() : "-";

        return "<!DOCTYPE html>" +
                "<html><head><meta charset='UTF-8'></head><body style='font-family:Segoe UI,Arial,sans-serif;background:#F3F4F6;padding:32px;'>" +
                "<div style='max-width:600px;margin:0 auto;background:white;border-radius:16px;overflow:hidden;box-shadow:0 4px 24px rgba(0,0,0,0.10);'>" +

                // Header
                "<div style='background:linear-gradient(to right,#4F46E5,#7C3AED);padding:28px 32px;'>" +
                "<h1 style='color:white;margin:0;font-size:22px;'>Event Tickets SEA</h1>" +
                "<p style='color:#E0E7FF;margin:4px 0 0;font-size:14px;'>Your ticket confirmation</p>" +
                "</div>" +

                // Event name banner
                "<div style='background:#EEF2FF;padding:20px 32px;border-bottom:1px solid #E0E7FF;'>" +
                "<h2 style='margin:0;color:#1E1B4B;font-size:20px;'>" + event.getEventName() + "</h2>" +
                "<span style='display:inline-block;margin-top:8px;padding:4px 14px;background:#4F46E5;color:white;border-radius:20px;font-size:12px;font-weight:bold;'>" +
                categoryName.toUpperCase() + "</span>" +
                "</div>" +

                // Ticket details
                "<div style='padding:28px 32px;'>" +
                "<table style='width:100%;border-collapse:collapse;'>" +
                row("Customer",  customerName) +
                row("Quantity",  String.valueOf(quantity)) +
                row("Price",     priceStr) +
                row("Start",     startStr) +
                row("End",       endStr.equals("—") ? "Not specified" : endStr) +
                row("Location",  location) +
                row("Notes",     notes) +
                row("Issued by", issuedBy) +
                "</table>" +
                "</div>" +

                // Ticket ID footer
                "<div style='background:#F9FAFB;border-top:1px solid #E5E7EB;padding:16px 32px;'>" +
                "<p style='margin:0;font-family:Courier New,monospace;font-size:11px;color:#6B7280;'>Ticket ID: " + ticketId + "</p>" +
                "<p style='margin:6px 0 0;font-size:12px;color:#9CA3AF;'>Please show this email at the entrance. Payment is handled on-site.</p>" +
                "</div>" +

                "</div></body></html>";
    }

    private String row(String label, String value) {
        return "<tr>" +
                "<td style='padding:8px 0;font-size:13px;font-weight:bold;color:#374151;width:120px;'>" + label + "</td>" +
                "<td style='padding:8px 0;font-size:13px;color:#1F2937;'>" + value + "</td>" +
                "</tr>";
    }

}

