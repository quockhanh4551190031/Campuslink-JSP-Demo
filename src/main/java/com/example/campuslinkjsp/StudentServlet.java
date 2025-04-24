package com.example.campuslinkjsp;

import com.example.DAO.StudentDAO;
import com.example.model.Student;
import com.example.model.Graduation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date; import java.util.List;

@WebServlet(name = "student", value = "/student")
public class StudentServlet extends HttpServlet {
    private StudentDAO studentDAO;

    @Override
    public void init() throws ServletException {
        studentDAO = new StudentDAO();
        System.out.println("StudentServlet initialized successfully! URL pattern: /student");
    }
    // Xử lý GET để hiển thị form
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "form";
        }

        // Lấy và xóa thông báo từ session
        String error = (String) request.getSession().getAttribute("error");
        String message = (String) request.getSession().getAttribute("message");
        if (error != null) {
            request.setAttribute("error", error);
            request.getSession().removeAttribute("error");
        }
        if (message != null) {
            request.setAttribute("message", message);
            request.getSession().removeAttribute("message");
        }

        try {
            switch (action) {
                case "list":
                    listStudents(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteStudent(request, response);
                    break;
                case "searchBasic":
                    showSearchBasicForm(request, response);
                    break;
                case "searchGradJob":
                    showSearchGradJobForm(request, response);
                    break;
                default:
                    showStudentForm(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Lỗi truy xuất dữ liệu", e);
        }
    }

    // Xử lý POST để nhận dữ liệu từ form
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Đặt mã hóa cho request
        request.setCharacterEncoding("UTF-8");
        // Đặt mã hóa cho response
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String action = request.getParameter("action");
        if ("update".equals(action)) {
            updateStudent(request, response);
        } else {
            // Lấy dữ liệu từ form
            String soCMNDStr = request.getParameter("soCMND");
            String hoTen = request.getParameter("hoTen");
            String email = request.getParameter("email");
            String soDTStr = request.getParameter("soDT");
            String diaChi = request.getParameter("diaChi");
            String maTruongStr = request.getParameter("maTruong");
            String maNganhStr = request.getParameter("maNganh");
            String heTN = request.getParameter("heTN");
            String ngayTN = request.getParameter("ngayTN");
            String loaiTN = request.getParameter("loaiTN");

            // Kiểm tra dữ liệu đầu vào
            StringBuilder errorMsg = new StringBuilder();
            long soCMND = 0;
            long soDT = 0;
            int maTruong = 0;
            int maNganh = 0;

            // Kiểm tra SoCMND
            if (soCMNDStr == null || soCMNDStr.trim().isEmpty()) {
                errorMsg.append("Số CMND là bắt buộc.<br>");
            } else {
                try {
                    soCMND = Long.parseLong(soCMNDStr);
                    if (soCMND <= 0) {
                        errorMsg.append("Số CMND phải là số dương.<br>");
                    } else if (soCMNDStr.length() != 12) {
                        errorMsg.append("Số CMND phải có đúng 12 chữ số.<br>");
                    }
                } catch (NumberFormatException e) {
                    errorMsg.append("Số CMND chỉ được chứa chữ số.<br>");
                }
            }

            // Kiểm tra SoDT
            if (soDTStr != null && !soDTStr.trim().isEmpty()) {
                try {
                    soDT = Long.parseLong(soDTStr);
                    if (soDT <= 0) {
                        errorMsg.append("Số điện thoại phải là số dương.<br>");
                    } else if (soDTStr.length() != 10) {
                        errorMsg.append("Số điện thoại phải có đúng 10 chữ số.<br>");
                    }
                } catch (NumberFormatException e) {
                    errorMsg.append("Số điện thoại chỉ được chứa chữ số.<br>");
                }
            }

            // Kiểm tra MaTruong
            if (maTruongStr == null || maTruongStr.trim().isEmpty()) {
                errorMsg.append("Mã trường là bắt buộc.<br>");
            } else {
                try {
                    maTruong = Integer.parseInt(maTruongStr);
                    if (maTruong <= 0) {
                        errorMsg.append("Mã trường phải là số dương.<br>");
                    }
                } catch (NumberFormatException e) {
                    errorMsg.append("Mã trường chỉ được chứa chữ số.<br>");
                }
            }

            // Kiểm tra MaNganh
            if (maNganhStr == null || maNganhStr.trim().isEmpty()) {
                errorMsg.append("Mã ngành là bắt buộc.<br>");
            } else {
                try {
                    maNganh = Integer.parseInt(maNganhStr);
                    if (maNganh <= 0) {
                        errorMsg.append("Mã ngành phải là số dương.<br>");
                    }
                } catch (NumberFormatException e) {
                    errorMsg.append("Mã ngành chỉ được chứa chữ số.<br>");
                }
            }

            // Kiểm tra NgayTN
            if (ngayTN != null && !ngayTN.trim().isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    sdf.setLenient(false);
                    Date graduationDate = sdf.parse(ngayTN);
                    Date today = new Date();
                    if (graduationDate.after(today)) {
                        errorMsg.append("Ngày tốt nghiệp không được là ngày trong tương lai.<br>");
                    }
                } catch (ParseException e) {
                    errorMsg.append("Ngày tốt nghiệp phải có định dạng yyyy-MM-dd.<br>");
                }
            }

            // Nếu có lỗi, quay lại form
            if (errorMsg.length() > 0) {
                request.setAttribute("error", errorMsg.toString());
                doGet(request, response);
                return;
            }

            // Tạo đối tượng Student và Graduation
            Student student = new Student(soCMND, hoTen, email, soDT, diaChi);
            Graduation graduation = new Graduation(soCMND, maTruong, maNganh, heTN, ngayTN, loaiTN);

            // Lưu vào cơ sở dữ liệu
            try {
                studentDAO.addStudent(student, graduation);
                request.setAttribute("message", "Thêm sinh viên thành công!");
            } catch (SQLException e) {
                request.setAttribute("error", "Lỗi khi thêm sinh viên: " + e.getMessage());
            }
            doGet(request, response);
        }
    }

    private void showSearchBasicForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String hoTen = request.getParameter("hoTen");
        List<Object[]> students = null;
        if (hoTen != null && !hoTen.trim().isEmpty()) {
            try {
                students = studentDAO.searchStudentsByName(hoTen);
            } catch (SQLException e) {
                request.setAttribute("error", "Lỗi khi tìm kiếm sinh viên: " + e.getMessage());
            }
        }
        request.setAttribute("students", students);
        request.getRequestDispatcher("/searchStudent.jsp").forward(request, response);
    }

    private void showSearchGradJobForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String soCMNDStr = request.getParameter("soCMND");
        List<Object[]> results = null;
        if (soCMNDStr != null && !soCMNDStr.trim().isEmpty()) {
            try {
                long soCMND = Long.parseLong(soCMNDStr);
                results = studentDAO.searchGraduationAndJobById(soCMND);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Số CMND phải là số hợp lệ.");
            } catch (SQLException e) {
                request.setAttribute("error", "Lỗi khi tìm kiếm thông tin: " + e.getMessage());
            }
        }
        request.setAttribute("results", results);
        request.getRequestDispatcher("/searchGraduationJob.jsp").forward(request, response);
    }

    private void listStudents(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        List<Object[]> students = studentDAO.getAllStudents();
        request.setAttribute("students", students);
        request.getRequestDispatcher("/studentList.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        String soCMNDStr = request.getParameter("soCMND");
        if (soCMNDStr == null || soCMNDStr.trim().isEmpty()) {
            request.setAttribute("error", "Số CMND không hợp lệ.");
            listStudents(request, response);
            return;
        }

        try {
            long soCMND = Long.parseLong(soCMNDStr);
            Student student = studentDAO.getStudentById(soCMND);
            Graduation graduation = studentDAO.getGraduationById(soCMND);

            if (student == null || graduation == null) {
                request.setAttribute("error", "Không tìm thấy sinh viên với Số CMND: " + soCMND);
                listStudents(request, response);
                return;
            }

            List<Object[]> schools = studentDAO.getAllSchools();
            List<Object[]> majors = studentDAO.getAllMajors();

            request.setAttribute("student", student);
            request.setAttribute("graduation", graduation);
            request.setAttribute("schools", schools);
            request.setAttribute("majors", majors);
            request.getRequestDispatcher("/studentForm.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Số CMND không hợp lệ.");
            listStudents(request, response);
        }
    }
    private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String soCMNDStr = request.getParameter("soCMND");
        String hoTen = request.getParameter("hoTen");
        String email = request.getParameter("email");
        String soDTStr = request.getParameter("soDT");
        String diaChi = request.getParameter("diaChi");
        String maTruongStr = request.getParameter("maTruong");
        String maNganhStr = request.getParameter("maNganh");
        String heTN = request.getParameter("heTN");
        String ngayTN = request.getParameter("ngayTN");
        String loaiTN = request.getParameter("loaiTN");

        StringBuilder errorMsg = new StringBuilder();
        long soCMND = 0;
        long soDT = 0;
        int maTruong = 0;
        int maNganh = 0;

        try {
            soCMND = Long.parseLong(soCMNDStr);
            if (soCMND <= 0) {
                errorMsg.append("Số CMND phải là số dương.<br>");
            } else if (soCMNDStr.length() != 12) {
                errorMsg.append("Số CMND phải có đúng 12 chữ số.<br>");
            }
        } catch (NumberFormatException e) {
            errorMsg.append("Số CMND chỉ được chứa chữ số.<br>");
        }

        if (soDTStr != null && !soDTStr.trim().isEmpty()) {
            try {
                soDT = Long.parseLong(soDTStr);
                if (soDT <= 0) {
                    errorMsg.append("Số điện thoại phải là số dương.<br>");
                } else if (soDTStr.length() != 10) {
                    errorMsg.append("Số điện thoại phải có đúng 10 chữ số.<br>");
                }
            } catch (NumberFormatException e) {
                errorMsg.append("Số điện thoại chỉ được chứa chữ số.<br>");
            }
        }

        if (maTruongStr == null || maTruongStr.trim().isEmpty()) {
            errorMsg.append("Mã trường là bắt buộc.<br>");
        } else {
            try {
                maTruong = Integer.parseInt(maTruongStr);
                if (maTruong <= 0) {
                    errorMsg.append("Mã trường phải là số dương.<br>");
                }
            } catch (NumberFormatException e) {
                errorMsg.append("Mã trường chỉ được chứa chữ số.<br>");
            }
        }

        if (maNganhStr == null || maNganhStr.trim().isEmpty()) {
            errorMsg.append("Mã ngành là bắt buộc.<br>");
        } else {
            try {
                maNganh = Integer.parseInt(maNganhStr);
                if (maNganh <= 0) {
                    errorMsg.append("Mã ngành phải là số dương.<br>");
                }
            } catch (NumberFormatException e) {
                errorMsg.append("Mã ngành chỉ được chứa chữ số.<br>");
            }
        }

        if (ngayTN != null && !ngayTN.trim().isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false);
                Date graduationDate = sdf.parse(ngayTN);
                Date today = new Date();
                if (graduationDate.after(today)) {
                    errorMsg.append("Ngày tốt nghiệp không được là ngày trong tương lai.<br>");
                }
            } catch (ParseException e) {
                errorMsg.append("Ngày tốt nghiệp phải có định dạng yyyy-MM-dd.<br>");
            }
        }

        // Lưu thông báo lỗi vào session nếu có lỗi
        if (errorMsg.length() > 0) {
            request.getSession().setAttribute("error", errorMsg.toString());
            response.sendRedirect("student?action=edit&soCMND=" + soCMNDStr);
            return;
        }

        try {
            Student student = new Student(soCMND, hoTen, email, soDT, diaChi);
            Graduation graduation = new Graduation(soCMND, maTruong, maNganh, heTN, ngayTN, loaiTN);
            studentDAO.updateStudent(student, graduation);
            request.getSession().setAttribute("message", "Cập nhật sinh viên thành công!");
            response.sendRedirect("student?action=list");
        } catch (SQLException e) {
            request.getSession().setAttribute("error", "Lỗi khi cập nhật sinh viên: " + e.getMessage());
            response.sendRedirect("student?action=list");
        }
    }

    private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        String soCMNDStr = request.getParameter("soCMND");
        if (soCMNDStr == null || soCMNDStr.trim().isEmpty()) {
            request.setAttribute("error", "Số CMND không hợp lệ.");
            listStudents(request, response);
            return;
        }

        try {
            long soCMND = Long.parseLong(soCMNDStr);
            studentDAO.deleteStudent(soCMND);
            request.setAttribute("message", "Xóa sinh viên thành công!");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Số CMND không hợp lệ.");
        } catch (SQLException e) {
            request.setAttribute("error", "Lỗi khi xóa sinh viên: " + e.getMessage());
        }
        listStudents(request, response);
    }

    private void showStudentForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        List<Object[]> schools = studentDAO.getAllSchools();
        List<Object[]> majors = studentDAO.getAllMajors();

        System.out.println("Truyền dữ liệu vào JSP:");
        System.out.println("Số trường: " + schools.size());
        System.out.println("Số ngành: " + majors.size());

        request.setAttribute("schools", schools);
        request.setAttribute("majors", majors);
        request.getRequestDispatcher("/studentForm.jsp").forward(request, response);
    }
}
