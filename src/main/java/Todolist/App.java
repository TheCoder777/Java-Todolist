
package Todolist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App {
    public static Note getNewNote(BufferedReader br) throws IOException {
        String name;
        String content = "";

        System.out.print("Name: ");
        name = br.readLine();
        System.out.print("Write it down: ");
        content = br.readLine();

        return new Note(name, content);
    }

    private static int getToDelte(BufferedReader br) throws NumberFormatException, IOException {
        System.out.print("Note to delete: ");
        int id = Integer.parseInt(br.readLine());

        return id;
    }

    private static int getToEdit(BufferedReader br) throws NumberFormatException, IOException {
        System.out.print("Note to edit: ");
        int id = Integer.parseInt(br.readLine());
        return id;
    }

    public static void main(String[] args) throws IOException {
        // Scanner sc = new Scanner(System.in);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        db todolist = new db();
        String action = "";
        Boolean exit = true;

        System.out.println("\nTODOLIST by THECODER777\n");

        do {
            System.out.print("> ");
            action = br.readLine();

            switch (action) {
                case "new":
                    todolist.add(getNewNote(br));
                    continue;

                case "list":
                    todolist.list();
                    continue;

                case "del":
                    todolist.del(getToDelte(br));
                    continue;

                case "edit":
                    todolist.edit(getToEdit(br), br);
                    continue;
    
                case "exit":
                    br.close();
                    exit = false;
                    break;

                default:
                    continue;
            }
        } while (exit);

    }

}
