package com.student;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/student")
public class Student extends HttpServlet {

    // Database details
    private String url = "jdbc:mysql://localhost:3306/college_db";
    private String username = "root";
    private String password = "Jeni23ferp@";

    private Connection con;

    // =========================
    // DATABASE CONNECTION
    // =========================
    @Override
    public void init() throws ServletException {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(
                    url,
                    username,
                    password
            );

            System.out.println("================================");
            System.out.println("DATABASE CONNECTED SUCCESSFULLY");
            System.out.println("================================");

        } catch (Exception e) {

            System.out.println("================================");
            System.out.println("DATABASE CONNECTION FAILED");
            System.out.println("================================");

            e.printStackTrace();

            throw new ServletException(
                    "Unable to connect to MySQL database",
                    e
            );
        }
    }


    // =========================
    // INSERT / UPDATE / DELETE
    // =========================
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        String action = request.getParameter("action");

        try {

            // Check connection
            if (con == null || con.isClosed()) {

                response.getWriter().println(
                        "<h3>Database connection is not available!</h3>"
                );

                return;
            }


            // =========================
            // INSERT
            // =========================
            if ("insert".equals(action)) {

                int id = Integer.parseInt(
                        request.getParameter("id")
                );

                String name = request.getParameter("name");

                String course = request.getParameter("course");

                int marks = Integer.parseInt(
                        request.getParameter("marks")
                );

                String sql =
                        "INSERT INTO student (id, name, course, marks) VALUES (?, ?, ?, ?)";

                PreparedStatement ps =
                        con.prepareStatement(sql);

                ps.setInt(1, id);
                ps.setString(2, name);
                ps.setString(3, course);
                ps.setInt(4, marks);

                int result = ps.executeUpdate();

                ps.close();

                if (result > 0) {

                    response.sendRedirect("index.html");

                } else {

                    response.getWriter().println(
                            "<h3>Student not inserted!</h3>"
                    );
                }
            }


            // =========================
            // UPDATE
            // =========================
            else if ("update".equals(action)) {

                int id = Integer.parseInt(
                        request.getParameter("id")
                );

                String name = request.getParameter("name");

                String course = request.getParameter("course");

                int marks = Integer.parseInt(
                        request.getParameter("marks")
                );

                String sql =
                        "UPDATE student SET name=?, course=?, marks=? WHERE id=?";

                PreparedStatement ps =
                        con.prepareStatement(sql);

                ps.setString(1, name);
                ps.setString(2, course);
                ps.setInt(3, marks);
                ps.setInt(4, id);

                int result = ps.executeUpdate();

                ps.close();

                if (result > 0) {

                    response.sendRedirect("index.html");

                } else {

                    response.getWriter().println(
                            "<h3>Student ID not found!</h3>"
                    );
                }
            }


            // =========================
            // DELETE
            // =========================
            else if ("delete".equals(action)) {

                int id = Integer.parseInt(
                        request.getParameter("id")
                );

                String sql =
                        "DELETE FROM student WHERE id=?";

                PreparedStatement ps =
                        con.prepareStatement(sql);

                ps.setInt(1, id);

                int result = ps.executeUpdate();

                ps.close();

                if (result > 0) {

                    response.sendRedirect("index.html");

                } else {

                    response.getWriter().println(
                            "<h3>Student ID not found!</h3>"
                    );
                }
            }


            // =========================
            // INVALID ACTION
            // =========================
            else {

                response.getWriter().println(
                        "<h3>Invalid action!</h3>"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.getWriter().println(
                    "<h3>Error: " + e.getMessage() + "</h3>"
            );
        }
    }


    // =========================
    // VIEW STUDENTS
    // =========================
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        try {

            // Check connection
            if (con == null || con.isClosed()) {

                out.println(
                        "<h3>Database connection is not available!</h3>"
                );

                return;
            }

            Statement st = con.createStatement();

            ResultSet rs =
                    st.executeQuery("SELECT * FROM student");


            // =========================
            // TABLE
            // =========================

            out.println("<html>");
            out.println("<head>");

            out.println("<title>Student Details</title>");

            out.println("<style>");

            out.println(
                    "body { " +
                    "font-family: Arial; " +
                    "background-color: #f4f4f4; " +
                    "padding: 30px; " +
                    "}"
            );

            out.println(
                    "table { " +
                    "width: 80%; " +
                    "margin: auto; " +
                    "border-collapse: collapse; " +
                    "background: white; " +
                    "}"
            );

            out.println(
                    "th, td { " +
                    "border: 1px solid #ddd; " +
                    "padding: 12px; " +
                    "text-align: center; " +
                    "}"
            );

            out.println(
                    "th { " +
                    "background-color: #333; " +
                    "color: white; " +
                    "}"
            );

            out.println(
                    "h2 { text-align: center; }"
            );

            out.println("</style>");

            out.println("</head>");

            out.println("<body>");

            out.println("<h2>Student Details</h2>");

            out.println("<table>");

            out.println("<tr>");

            out.println("<th>ID</th>");
            out.println("<th>Name</th>");
            out.println("<th>Course</th>");
            out.println("<th>Marks</th>");

            out.println("</tr>");


            // =========================
            // DISPLAY DATA
            // =========================

            while (rs.next()) {

                out.println("<tr>");

                out.println(
                        "<td>" +
                        rs.getInt("id") +
                        "</td>"
                );

                out.println(
                        "<td>" +
                        rs.getString("name") +
                        "</td>"
                );

                out.println(
                        "<td>" +
                        rs.getString("course") +
                        "</td>"
                );

                out.println(
                        "<td>" +
                        rs.getInt("marks") +
                        "</td>"
                );

                out.println("</tr>");
            }

            out.println("</table>");

            out.println("</body>");

            out.println("</html>");


            rs.close();

            st.close();

        } catch (Exception e) {

            e.printStackTrace();

            out.println(
                    "<h3>Error: " +
                    e.getMessage() +
                    "</h3>"
            );
        }
    }


    // =========================
    // CLOSE DATABASE
    // =========================
    @Override
    public void destroy() {

        try {

            if (con != null && !con.isClosed()) {

                con.close();

                System.out.println(
                        "Database Connection Closed!"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}