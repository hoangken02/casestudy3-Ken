package controller;

import dao.classroomService.ClassroomDAO;
import dao.classroomService.IClassroomDAO;
import dao.studentServive.IStudentDAO;
import dao.studentServive.StudentDAO;
import model.Classroom;
import model.Student;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ClassroomServlet",urlPatterns = "/classroom")
public class ClassroomServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "insertClassroom":
                try {
                    insertClassroom(request, response);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            default:
                listClassRoom(request,response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "deleteClassroom":
                try {
                    deleteClassroom(request, response);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "showAllStudent":
                showAllStudent(request, response);
                break;
            case "showStudentInClass":
                showStudentInClass(request, response);
            default:
                showAllStudent(request,response);
                break;
        }
    }

    private void insertClassroom(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        String name = request.getParameter("name");
        IClassroomDAO classroomDAO = new ClassroomDAO();
        Classroom classroom = new Classroom(name);
        classroomDAO.insertClassroom(classroom);
        RequestDispatcher dispatcher = request.getRequestDispatcher("");
        dispatcher.forward(request, response);
    }


    private void deleteClassroom(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        ClassroomDAO classroomDAO = new ClassroomDAO();
        int Id = Integer.parseInt(request.getParameter("id"));
        boolean rs = classroomDAO.deleteClassroom(Id);
        if (rs) {
            request.setAttribute("message", "delete success!");
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("");
        try {
            dispatcher.forward(request, response);
        } catch (IOException | ServletException exception) {
            exception.printStackTrace();
        }
    }

    private void showAllStudent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IStudentDAO studentDAO = new StudentDAO();
        List<Student> students = studentDAO.selectAllStudents();
        request.setAttribute("students", students);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("view/user/insertStudent.jsp");
        requestDispatcher.forward(request, response);
    }

    private void showStudentInClass(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IStudentDAO studentDAO = new StudentDAO();
        int classroomId = Integer.parseInt(request.getParameter("id"));
        List<Student> students = studentDAO.selectStudentInClass(classroomId);
        request.setAttribute("students", students);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("view/user/insertStudent.jsp");
        requestDispatcher.forward(request, response);
    }

    private void listClassRoom(HttpServletRequest request, HttpServletResponse response) {
        IClassroomDAO classroomDAO = new ClassroomDAO();
        List<Classroom> classrooms = classroomDAO.selectAllClassrooms();
        request.setAttribute("classrooms", classrooms);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("view/user/insertStudent.jsp");
        try {
            requestDispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
}
