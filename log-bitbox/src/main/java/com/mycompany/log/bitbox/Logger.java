/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.log.bitbox;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author mathe
 */
public class Logger {

    private static final int MAX_FILE_SIZE = 1000000; // Tamanho máximo do arquivo em bytes
    private static final String LOG_FILE_PATH = "C:/Users/mathe/OneDrive/Documentos/Arquivos/Logs"; // Diretório de logs
    private static final DateTimeFormatter LOG_FILE_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private static BufferedWriter writer;
    private static File currentLogFile;

    public static void log(String message, LogLevel level) {
        if (writer == null || isLogFileFull()) {
            createNewLogFile();
        }

        LocalDateTime timestamp = LocalDateTime.now();
        String logEntry = "[" + level.name() + "] " + timestamp + ": " + message;

        try {
            writer.write(logEntry);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createNewLogFile() {
        closeLogFile();

        LocalDateTime timestamp = LocalDateTime.now();
        String fileName = LOG_FILE_PATH + "log_" + timestamp.format(LOG_FILE_DATE_FORMATTER) + ".txt";
        currentLogFile = new File(fileName);

        try {
            writer = new BufferedWriter(new FileWriter(currentLogFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isLogFileFull() {
        return currentLogFile != null && currentLogFile.length() >= MAX_FILE_SIZE;
    }

    private static void closeLogFile() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    enum LogLevel {
        INFO, WARNING, ERROR
    }

    public class Main {
        public static void main(String[] args) {
            Logger.log("Mensagem de informação", LogLevel.INFO);
            Logger.log("Mensagem de aviso", LogLevel.WARNING);
            Logger.log("Mensagem de erro", LogLevel.ERROR);
        }
    }
}
