package services;

import com.opencsv.CSVWriter;
import entities.Author;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Logger {
    public static ArrayList<LoggedOperation> LoggedOperations = new ArrayList<LoggedOperation>();

    public Logger() {

    }

    public static Integer logOperation(String operation) {
        List<Integer> ids = LoggedOperations.stream().map(LoggedOperation::getId).sorted(Comparator.comparing(Integer::valueOf)).
                collect(Collectors.toList());
        LoggedOperation log = new LoggedOperation(operation, LocalDateTime.now());
        for (Integer i = 0; i < ids.get(ids.size() - 1) + 1; i += 1) {
            if (!i.equals(ids.get(i))) {
                log.setId(i);
                break;
            }
        }
        try {
            FileWriter filewriter = new FileWriter("data/operations_log.csv", true);
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[]{log.getId().toString(), log.getOperation(),
                    log.getTimeStamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))});

        } catch (IOException e) {
            e.printStackTrace();
        }
        LoggedOperations.add(log);
        return log.getId();
    }

    public static LoggedOperation getLoggedOperation(Integer id) {
        for (LoggedOperation loggedOperation: LoggedOperations) {
            if (loggedOperation.getId().equals(id))
                return loggedOperation;
        }
        return null;
    }

    public static void deleteLog(Integer id) {
        LoggedOperations.removeIf(loggedOperation -> loggedOperation.getId().equals(id));
        try {
            FileWriter filewriter = new FileWriter("data/operations_log.csv");
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[]{"Id", "Operation", "TimeStamp"});
            for (LoggedOperation log: LoggedOperations) {
                writer.writeNext(new String[]{log.getId().toString(), log.getOperation(),
                        log.getTimeStamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearLogs() {
        LoggedOperations.clear();
        try {
            FileWriter filewriter = new FileWriter("data/operations_log.csv");
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[]{"Id", "Operation", "TimeStamp"});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
