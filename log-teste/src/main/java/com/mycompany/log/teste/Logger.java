package com.mycompany.log.teste;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 *
 * @author mathe
 */
public class Logger {

    private Double cpu = 0.65;
    private static final String LOG_DIRECTORY = "C:/Users/mathe/OneDrive/Documentos/Arquivos/";
    private static final String LOG_FILENAME_PREFIX = "log_";
    private static final String LOG_FILENAME_SUFFIX = ".txt";
    private static final Integer MAX_FILE_SIZE = 1024 * 1024; // 1MB

    private static File currentLogFile;
    private static PrintWriter printWriter;

    public static void log(String massage, LogLevel level) {
        // Verifica se o arquivo de LOG atual ainda pode ser usad
        if (currentLogFile != null && currentLogFile.length() >= MAX_FILE_SIZE) {
            close();
        } // Cria um novo arquivo de LOG, se necessário
        else if (currentLogFile == null) {
            createNewLogFile();
        }

        // Escreve a mensagem no arquivo de LOG se o nível for igual ou superior ao nível configurado
        LocalDateTime timestamp = LocalDateTime.now();
        printWriter.println("[" + level + "]" + timestamp + ": " + massage);
        printWriter.flush();
    }

    public Double getCpu() {
        return cpu;
    }

    public void setCpu(Double cpu) {
        this.cpu = cpu;
    }

    private enum LogLevel {
        INFO, WARNING, ERROR;
    }

    private static void createNewLogFile() {
        File directory = new File(LOG_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdir();
        }

        // Cria um nome único para o arquivo de LOG
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH-mm-ss");
        String date = dateFormat.format(new Date());
        String filename = LOG_DIRECTORY + LOG_FILENAME_PREFIX + date + LOG_FILENAME_SUFFIX;
        currentLogFile = new File(filename);
        try {
            printWriter = new PrintWriter(new BufferedWriter(new FileWriter(currentLogFile)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        if (printWriter != null) {
            printWriter.close();
        }

        currentLogFile = null;
        printWriter = null;
    }

    // Define o nível de LOG com base na variável de ambiente "LOG_LEVEL"
    private static LogLevel getLogLevel() {
        String loglevel = System.getenv("LOG_LEVEL");
        if (loglevel == null) {
            return LogLevel.INFO;
        }
        switch (loglevel.toUpperCase()) {
            case "BASIC":
                return LogLevel.WARNING;
            case "MEDIUM":
                return LogLevel.ERROR;
            case "HIGH":
                return LogLevel.INFO;
            default:
                return LogLevel.ERROR;
        }
    }
    
    
}
