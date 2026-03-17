package dev.thalysom.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ConsoleUtils {

    public static LocalDate lerData(Scanner scanner) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        do {
            String text = scanner.nextLine();

            try {
                LocalDate data = LocalDate.parse(text, formatter);

                if (data.isAfter(LocalDate.now())) {
                    System.out.println("Data não pode estar no futuro.");
                }

                return data;
            }catch (DateTimeParseException e) {
                System.out.println("Data inválida. Use dd/MM/yyyy:");
            }
        }while (true);
    }
}
