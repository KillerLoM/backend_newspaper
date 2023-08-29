package com.example.backend.ErrorHandle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ErrorLogger {
    public static void logError(Exception e) {
        // Get the current date and time
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH-mm-ss");
        String formattedDate = dateFormat.format(date);
        String formattedTime = timeFormat.format(date);

        // Create a folder for the error logs within a log folder
        String logFolderName = "log";
        String folderName = logFolderName + "/error_logs/" + formattedDate;
        File folder = new File(folderName);
        folder.mkdirs();

        // Create a file to write the error log to
        String fileName = folderName + "/error_log_" + formattedTime + ".txt";
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileName);
        } catch (IOException e1) {
            System.out.println("Error creating error log file: " + e1.getMessage());
        }

        // Write the error to the file
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.println("Error: " + e.getMessage());
        printWriter.println("Stack trace:");
        e.printStackTrace(printWriter);
        printWriter.close();
    }
}
