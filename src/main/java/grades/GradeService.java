package grades;

import com.student.dao.Student;
import jdbc.JDBCManger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GradeService {
    private final JDBCManger jdbcManger=new JDBCManger();
   public List<Student> studentListWithGrades(int courseID) throws SQLException {
        String query="SELECT u.userID, u.name AS student_name, g.grade\n" +
                "FROM user u\n" +
                "INNER JOIN grades g ON u.userID = g.stID\n" +
                "INNER JOIN course c ON g.courseID = c.courseID\n" +
                "WHERE c.courseID = ?";
        PreparedStatement statement=jdbcManger.connect().prepareStatement(query);
        statement.setInt(1,courseID);
        ResultSet resultSet=statement.executeQuery();
        List<Student> studentList=new ArrayList<>();
        while (resultSet.next()){
            int userID= resultSet.getInt("userID");
            String name= resultSet.getString("student_name");
            Student student=new Student(userID,name);
            student.setGrade(resultSet.getInt("grade"));
            studentList.add(student);
        }
        return studentList;

    }
    public List<Student>studentListWithoutGrades(int courseID) throws SQLException {
        String query="SELECT u.userID, u.name AS student_name\n" +
                "FROM user u\n" +
                "INNER JOIN student_course sc ON u.userID = sc.stID\n" +
                "INNER JOIN course c ON sc.courseID = c.courseID\n" +
                "WHERE c.courseID = ?\n" +
                "AND u.userID NOT IN (\n" +
                "    SELECT g.stID\n" +
                "    FROM grades g\n" +
                "    WHERE g.courseID = ?)" ;
        PreparedStatement statement=jdbcManger.connect().prepareStatement(query);
        statement.setInt(1,courseID);
        statement.setInt(2,courseID);
        ResultSet resultSet=statement.executeQuery();
        List<Student> studentList=new ArrayList<>();
        while (resultSet.next()){
            int userID= resultSet.getInt("userID");
            String name= resultSet.getString("student_name");
            Student student=new Student(userID,name);
            studentList.add(student);
        }
        return studentList;
    }

    public List<ShowGrades> showGrades(int userID) throws SQLException {
        String query = "select  courseName,grade from user,course,grades where user.userID=grades.stID and course.courseID=grades.courseID and grades.stID=?";
        PreparedStatement statement = jdbcManger.connect().prepareStatement(query);
        statement.setInt(1, userID);
        ResultSet resultSet = statement.executeQuery();
        List<ShowGrades> grades=new ArrayList<>();
        while (resultSet.next()) {
            String courseName=resultSet.getString("courseName");
            int grade = resultSet.getInt("grade");
            ShowGrades grade1=new ShowGrades(courseName,grade);
            grades.add(grade1);
        }
        return grades;
    }
}
