package com.example.backend.CronjobService;

import com.example.backend.Newspaper.GetNewspaper;
import com.example.backend.SourceNewspapers.GetData;
import com.example.backend.DataBaseService.DbFunction;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

@Service

public class cronjob {
    @Scheduled(fixedDelay = 1800000)
    public void startBatch() throws SQLException, IOException, ParseException {
        GetData.data();
        GetNewspaper.readData();
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        System.out.println("Cronjob has implemented the job in: " + sdf.format(currentTime));
    }
}
