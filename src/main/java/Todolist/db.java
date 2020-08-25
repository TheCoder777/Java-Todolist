package Todolist;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class db {
    static final String DRIVER = "org.sqlite.JDBC";
    static final String PATH = "jdbc:sqlite:./todos.db";
    static Connection conn = null;

    db() {
        // init db
        // Class.forName(DRIVER);
        try {
            conn = DriverManager.getConnection(PATH);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE if not exists notes (id INTEGER PRIMARY KEY, title TEXT, content TEXT)");
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Failed to initialize Database!");
            e.printStackTrace();
        }
    }

    public void add(Note note) {
        try {
            Connection conn = DriverManager.getConnection(PATH);
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO notes(title, content) VALUES(?,?)");
            pstmt.setString(1, note.title);
            pstmt.setString(2, note.content);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void list() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM notes");
            System.out.println("ID\t||\tTITLE\t|\tNOTE");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t||\t" + rs.getString("title") + "\t|\t" + rs.getString("content"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void del(int id) {
        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE from notes WHERE id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void edit(int id, BufferedReader br) throws IOException {
        String content,title;
        try {
            // get note from db
            PreparedStatement stmtGet = conn.prepareStatement("SELECT title,content FROM notes WHERE id = ?");
            stmtGet.setInt(1, id);
            ResultSet rs = stmtGet.executeQuery();
            System.out.println("Title: " + rs.getString("title") + "\nCurrent Content: " + rs.getString("content"));
            System.out.println("[Enter nothing to leave untouched]");
            // Change title
            System.out.print("Enter new Title: ");
            title = br.readLine();
            if (title.isEmpty()  || title.isBlank()) {
                System.out.println("Skipping Title!");
            } else {
                PreparedStatement stmtInsertTitle = conn.prepareStatement("UPDATE notes set title = ? WHERE id = ?");
                stmtInsertTitle.setString(1, title);
                stmtInsertTitle.setInt(2, id);
                stmtInsertTitle.executeUpdate();
                ResultSet re = stmtGet.executeQuery();
                System.out.println("New Title: " + re.getString("title") + "\n");
                stmtInsertTitle.close();
            }
            // Change Content
            System.out.print("Enter new Content: ");
            content = br.readLine();
            if (content.isEmpty() || content.isBlank()) {
                System.out.println("Skipping Content");
            } else {
                PreparedStatement stmtInsertContent = conn.prepareStatement("UPDATE notes set content = ? WHERE id = ?");
                stmtInsertContent.setString(1, content);
                stmtInsertContent.setInt(2, id);
                stmtInsertContent.executeUpdate();
                ResultSet re = stmtGet.executeQuery();
                System.out.println("New Content: " + re.getString("content") + "\n");
                stmtInsertContent.close();
            }
            stmtGet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
