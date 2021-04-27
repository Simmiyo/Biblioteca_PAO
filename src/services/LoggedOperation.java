package services;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

public class LoggedOperation {
    private Integer Id;
    private String Operation;
    private LocalDateTime TimeStamp;

    public LoggedOperation(String operation, LocalDateTime timeStamp) {
        Operation = operation;
        TimeStamp = timeStamp;
    }

    public LoggedOperation() {

    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getOperation() {
        return Operation;
    }

    public void setOperation(String operation) {
        Operation = operation;
    }

    public LocalDateTime getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        TimeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "LoggedOperation{" +
                "Id=" + Id +
                ", Operation='" + Operation + '\'' +
                ", TimeStamp=" + TimeStamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoggedOperation)) return false;
        LoggedOperation that = (LoggedOperation) o;
        return getId().equals(that.getId()) && getOperation().equals(that.getOperation()) && getTimeStamp().equals(that.getTimeStamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOperation(), getTimeStamp());
    }
}
