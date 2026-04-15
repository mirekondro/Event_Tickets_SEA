package dk.easv.event_tickets_sea.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class EmailSettings {
    private static final String PROP_FILE = "config/email.settings";

    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final String fromAddress;
    private final boolean auth;
    private final boolean startTls;

    private EmailSettings(String host, int port, String username, String password, String fromAddress, boolean auth, boolean startTls) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.fromAddress = fromAddress;
        this.auth = auth;
        this.startTls = startTls;
    }

    public static EmailSettings load() {
        Properties props = new Properties();

        File file = new File(PROP_FILE);
        System.out.println("DEBUG - Loading email settings from: " + file.getAbsolutePath());
        System.out.println("DEBUG - File exists: " + file.exists());

        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                props.load(fis);
                System.out.println("DEBUG - Loaded properties: " + props);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load email settings from " + PROP_FILE, e);
            }
        }

        String host = getValue(props, "SMTPHost", "SMTP_HOST");
        String portValue = getValue(props, "SMTPPort", "SMTP_PORT");
        String username = getValue(props, "SMTPUser", "SMTP_USER");
        String password = getValue(props, "SMTPPassword", "SMTP_PASSWORD");
        String fromAddress = getValue(props, "FromEmail", "FROM_EMAIL");
        boolean auth = Boolean.parseBoolean(getValue(props, "SMTPAuth", "SMTP_AUTH", "true"));
        boolean startTls = Boolean.parseBoolean(getValue(props, "SMTPStartTls", "SMTP_STARTTLS", "true"));

        System.out.println("DEBUG - Parsed settings:");
        System.out.println("  Host: " + host);
        System.out.println("  Port: " + portValue);
        System.out.println("  Username: " + username);
        System.out.println("  Password: " + (password != null ? "***" : "null"));
        System.out.println("  FromAddress: " + fromAddress);
        System.out.println("  Auth: " + auth);
        System.out.println("  StartTls: " + startTls);

        if (host == null || host.isBlank() || portValue == null || portValue.isBlank() || fromAddress == null || fromAddress.isBlank()) {
            return new EmailSettings(null, 0, username, password, fromAddress, auth, startTls);
        }

        int port = Integer.parseInt(portValue);
        return new EmailSettings(host, port, username, password, fromAddress, auth, startTls);
    }

    private static String getValue(Properties props, String key, String envKey) {
        return getValue(props, key, envKey, null);
    }

    private static String getValue(Properties props, String key, String envKey, String defaultValue) {
        String value = props.getProperty(key);
        if (value == null || value.isBlank()) {
            value = System.getenv(envKey);
        }
        if ((value == null || value.isBlank()) && defaultValue != null) {
            value = defaultValue;
        }
        return value;
    }

    public boolean isConfigured() {
        return host != null && !host.isBlank() && port > 0 && fromAddress != null && !fromAddress.isBlank();
    }

    public String getHost() { return host; }
    public int getPort() { return port; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFromAddress() { return fromAddress; }
    public boolean isAuth() { return auth; }
    public boolean isStartTls() { return startTls; }
}

