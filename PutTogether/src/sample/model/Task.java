package sample.model;


import java.time.LocalDate;

public class Task {
    //Updated: private long datecreated;
    private LocalDate datecreated;
    private String description;
    private String task;

    public Task() {
    }

    public Task(LocalDate datecreated, String description, String task) {
        this.datecreated = datecreated;
        this.description = description;
        this.task = task;
    }

    public LocalDate getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(LocalDate datecreated) {
        this.datecreated = datecreated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return
                "At " + this.getDatecreated() +
                ", task: " + this.getTask() + '\'' +
                ", " + this.getDescription() + '\'';
    }
}
