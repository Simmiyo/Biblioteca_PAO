package services;

import com.opencsv.CSVWriter;

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
        if (!ids.isEmpty()) {
            log.setId(-1);
            for (Integer i = 0, j = 0; i < ids.size() ; i += 1, j+= 1) {
                if (!j.equals(ids.get(i))) {
                    log.setId(j);
                    break;
                }
            }
            if (log.getId().equals(-1)) log.setId(ids.get(ids.size() - 1) + 1);
        } else {
            log.setId(0);
        }
        try {
            FileWriter filewriter = new FileWriter("data/operations_log.csv", true);
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[]{log.getId().toString(), log.getOperation(),
                    log.getTimeStamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))});
            writer.close();

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
            writer.close();
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
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
