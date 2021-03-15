package com.saurabhsingh.todolist.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

public class TodoData {
    private static TodoData instance= new TodoData();
    private static String filename = "TodoListItems.txt";

    private ObservableList<TodoItem> todoItems;
    private DateTimeFormatter formatter;

    public static TodoData getInstance() {
        return instance;
    }

    public ObservableList<TodoItem> getTodoItems() {
        return todoItems;
    }

    public void addTodoItem(TodoItem item) {
        todoItems.add(item);
    }
    private TodoData() {
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    public void loadTodoItems() throws IOException {
        todoItems = FXCollections.observableArrayList();
        Path path = Paths.get(filename);
        BufferedReader br = Files.newBufferedReader(path);

        String input;

        try {
            while((input = br.readLine()) != null) {
                String[] itemPieces = input.split("\t");

                String shortDescription = itemPieces[0];
                String details = itemPieces[1];
                String dateString = itemPieces[2];

                LocalDate date = LocalDate.parse(dateString, formatter);
                TodoItem todoItem = new TodoItem(shortDescription, details, date);
                todoItems.add(todoItem);
            }
        }
        finally {
            if(br!=null) {
                br.close();
            }
        }
    }

    public void storeTodoItems() throws IOException {
        Path path = Paths.get(filename);
        BufferedWriter bw = Files.newBufferedWriter(path);

        try {
            Iterator<TodoItem> itr = todoItems.iterator();

            while(itr.hasNext()) {
                TodoItem i = itr.next();
                bw.write(String.format("%s\t%s\t%s",
                        i.getShortDescription(),
                        i.getDetails(),
                        i.getDeadline().format(formatter)));
                bw.newLine();
            }
        }
        catch (IOException e) {
            e.getMessage();
            System.out.println("YESSS");
        }
        finally {
            if(bw!=null) {
                bw.close();
            }
        }
    }

    public void deleteTodoItem(TodoItem item) {
       todoItems.remove(item);
    }
}
