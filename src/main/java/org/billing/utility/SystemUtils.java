package org.billing.utility;

import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;
import org.billing.configuration.EnvironmentConfig;
import org.billing.pages.MessageScreen;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class SystemUtils {
    static public Integer screenWidth;
    static public Integer screenHeight;

    static {
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        screenHeight = (int) size.getHeight();
        screenWidth = (int) size.getWidth();
    }

    public static void convertDatePicker(DatePicker myDatePicker) {
        myDatePicker.setConverter(
                new StringConverter<LocalDate>() {
                    final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(EnvironmentConfig.systemDateFormat);

                    @Override
                    public String toString(LocalDate date) {
                        return (date != null) ? dateFormatter.format(date) : "";
                    }

                    @Override
                    public LocalDate fromString(String string) {
                        return (string != null && !string.isEmpty())
                                ? LocalDate.parse(string, dateFormatter)
                                : null;
                    }
                });
    }

    public static LocalDate getLocalDateFromString( String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(EnvironmentConfig.systemDateFormat);
        return LocalDate.parse(date, formatter);
    }

    public static String getDateInSystemFormat( LocalDate localDate) {
        return (localDate != null) ? DateTimeFormatter.ofPattern(EnvironmentConfig.systemDateFormat).format(localDate) : null;
    }


    public static String getDateWithCurrentTime( LocalDate date) {
        LocalTime local_Time = LocalTime.now();
        LocalDateTime localDateTime = LocalDateTime.of(date, local_Time);
        return localDateTime.format(DateTimeFormatter.ofPattern(EnvironmentConfig.systemDateFormat + " " + EnvironmentConfig.systemTimeFormat));
    }


    public static String[] getDateTime( String datetime) {

        if (CommonUtils.isValidString(datetime)) {
            try {
                String dt = EnvironmentConfig.systemDateFormat + " " + EnvironmentConfig.systemTimeFormat;
                new SimpleDateFormat(dt).parse(datetime);
                return datetime.split(" ");
            } catch (Exception exception) {
                MessageScreen.showException("Error Occurred while converting String value of date to Date Object");
            }
        } else {
            MessageScreen.showException("Not Valid String");
        }
        return null;
    }

    public static boolean executeCommand(String command) {

        String value = null;
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (System.getProperty("os.name").contains("Windows")) {
            if (command.contains(".bat"))
                processBuilder.command(command);
            else
                processBuilder.command("cmd.exe", "/c", command);
        } else if (command.contains(".sh"))
            processBuilder.command(command);
        else
            processBuilder.command("bash", "-c", command);

        try {
            Process process = processBuilder.start();
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                output.append(line);
            }
            int exitVal = process.waitFor();
            if (exitVal == 0) {
                System.out.println("Success!");
                value = output.toString();
                return true;
            } else {
                System.out.println("abnormal...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getStringForFileName(String value) {
        String str = value.replaceAll("[^a-zA-Z0-9:/]", " ");
        return str.replace("/", "-").replace(":", "-").replace(" ", "_");
    }


}
