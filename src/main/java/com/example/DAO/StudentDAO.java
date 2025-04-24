package com.example.DAO;

import com.example.model.Student;
import com.example.model.Graduation;
import com.example.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    private final DatabaseConnection databaseConnection = new DatabaseConnection();


    public void addStudent(Student student, Graduation graduation) throws SQLException {
        String studentSQL = "INSERT INTO SINHVIEN (SoCMND, HoTen, Email, SoDT, DiaChi) VALUES (?, ?, ?, ?, ?)";
        String graduationSQL = "INSERT INTO TOT_NGHIEP (SoCMND, MaTruong, MaNganh, HeTN, NgayTN, LoaiTN) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = databaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Thêm sinh viên
                PreparedStatement studentStmt = conn.prepareStatement(studentSQL);
                studentStmt.setLong(1, student.getSoCMND());
                studentStmt.setString(2, student.getHoTen());
                studentStmt.setString(3, student.getEmail());
                studentStmt.setLong(4, student.getSoDT());
                studentStmt.setString(5, student.getDiaChi());
                studentStmt.executeUpdate();

                // Thêm thông tin tốt nghiệp
                PreparedStatement gradStmt = conn.prepareStatement(graduationSQL);
                gradStmt.setLong(1, graduation.getSoCMND());
                gradStmt.setInt(2, graduation.getMaTruong());
                gradStmt.setInt(3, graduation.getMaNganh());
                gradStmt.setString(4, graduation.getHeTN());
                gradStmt.setString(5, graduation.getNgayTN());
                gradStmt.setString(6, graduation.getLoaiTN());
                gradStmt.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public List<Object[]> getAllSchools() throws SQLException {
        List<Object[]> schools = new ArrayList<>();
        String sql = "SELECT MaTruong, TenTruong FROM truong";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                schools.add(new Object[]{rs.getInt("MaTruong"), rs.getString("TenTruong")});
            }
        }
        // Thêm log để kiểm tra dữ liệu
        System.out.println("Số lượng trường lấy được: " + schools.size());
        for (Object[] school : schools) {
            System.out.println("Trường: MaTruong=" + school[0] + ", TenTruong=" + school[1]);
        }
        return schools;
    }

    public List<Object[]> getAllMajors() throws SQLException {
        List<Object[]> majors = new ArrayList<>();
        String sql = "SELECT MaNganh, TenNganh FROM nganh";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                majors.add(new Object[]{rs.getInt("MaNganh"), rs.getString("TenNganh")});
            }
        }
        // Thêm log để kiểm tra dữ liệu
        System.out.println("Số lượng ngành lấy được: " + majors.size());
        for (Object[] major : majors) {
            System.out.println("Ngành: MaNganh=" + major[0] + ", TenNganh=" + major[1]);
        }
        return majors;
    }

    public List<Object[]> getAllStudents() throws SQLException {
        List<Object[]> students = new ArrayList<>();
        String sql = """
        SELECT s.SoCMND, s.HoTen, s.Email, s.SoDT, s.DiaChi, 
               t.MaTruong, t.TenTruong, n.MaNganh, n.TenNganh, 
               g.HeTN, g.NgayTN, g.LoaiTN
        FROM SINHVIEN s
        JOIN TOT_NGHIEP g ON s.SoCMND = g.SoCMND
        JOIN TRUONG t ON g.MaTruong = t.MaTruong
        JOIN NGANH n ON g.MaNganh = n.MaNganh
    """;
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                students.add(new Object[]{
                        rs.getLong("SoCMND"),
                        rs.getString("HoTen"),
                        rs.getString("Email"),
                        rs.getLong("SoDT"),
                        rs.getString("DiaChi"),
                        rs.getInt("MaTruong"),
                        rs.getString("TenTruong"),
                        rs.getInt("MaNganh"),
                        rs.getString("TenNganh"),
                        rs.getString("HeTN"),
                        rs.getString("NgayTN"),
                        rs.getString("LoaiTN")
                });
            }
        }
        System.out.println("Số lượng sinh viên lấy được: " + students.size());
        return students;
    }

    public List<Object[]> searchStudentsByName(String hoTen) throws SQLException {
        List<Object[]> students = new ArrayList<>();
        String sql = "SELECT SoCMND, HoTen, Email, SoDT, DiaChi FROM SINHVIEN WHERE HoTen LIKE ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + hoTen + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    students.add(new Object[]{
                            rs.getLong("SoCMND"),
                            rs.getString("HoTen"),
                            rs.getString("Email"),
                            rs.getLong("SoDT"),
                            rs.getString("DiaChi")
                    });
                }
            }
        }
        System.out.println("Số lượng sinh viên tìm được: " + students.size());
        return students;
    }

    public List<Object[]> searchGraduationAndJobById(long soCMND) throws SQLException {
        List<Object[]> results = new ArrayList<>();
        String sql = """
                SELECT s.SoCMND, s.HoTen, 
                       g.MaNganh AS MaNganhTotNghiep, g.MaTruong, 
                       c.MaNganh AS MaNganhCongTy, c.TenCongTy, c.ThoiGianLamViec
                FROM SINHVIEN s
                JOIN TOT_NGHIEP g ON s.SoCMND = g.SoCMND
                LEFT JOIN CONG_VIEC c ON s.SoCMND = c.SoCMND
                WHERE s.SoCMND = ?
        """;
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, soCMND);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(new Object[]{
                            rs.getLong("SoCMND"),
                            rs.getString("HoTen"),
                            rs.getInt("MaNganhTotNghiep"),
                            rs.getInt("MaTruong"),
                            rs.getInt("MaNganhCongTy"),
                            rs.getString("TenCongTy"),
                            rs.getString("ThoiGianLamViec")
                    });
                }
            }
        }
        System.out.println("Số lượng kết quả tốt nghiệp và việc làm tìm được: " + results.size());
        return results;
    }

    // Phương thức xóa sinh viên
    public void deleteStudent(long soCMND) throws SQLException {
        String deleteGraduationSQL = "DELETE FROM TOT_NGHIEP WHERE SoCMND = ?";
        String deleteStudentSQL = "DELETE FROM SINHVIEN WHERE SoCMND = ?";

        try (Connection conn = databaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Xóa thông tin tốt nghiệp trước (do khóa ngoại)
                PreparedStatement gradStmt = conn.prepareStatement(deleteGraduationSQL);
                gradStmt.setLong(1, soCMND);
                gradStmt.executeUpdate();

                // Xóa thông tin sinh viên
                PreparedStatement studentStmt = conn.prepareStatement(deleteStudentSQL);
                studentStmt.setLong(1, soCMND);
                studentStmt.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    // Phương thức sửa sinh viên
    public void updateStudent(Student student, Graduation graduation) throws SQLException {
        String updateStudentSQL = "UPDATE SINHVIEN SET HoTen = ?, Email = ?, SoDT = ?, DiaChi = ? WHERE SoCMND = ?";
        String updateGraduationSQL = "UPDATE TOT_NGHIEP SET MaTruong = ?, MaNganh = ?, HeTN = ?, NgayTN = ?, LoaiTN = ? WHERE SoCMND = ?";

        try (Connection conn = databaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Cập nhật thông tin sinh viên
                PreparedStatement studentStmt = conn.prepareStatement(updateStudentSQL);
                studentStmt.setString(1, student.getHoTen());
                studentStmt.setString(2, student.getEmail());
                studentStmt.setLong(3, student.getSoDT());
                studentStmt.setString(4, student.getDiaChi());
                studentStmt.setLong(5, student.getSoCMND());
                studentStmt.executeUpdate();

                // Cập nhật thông tin tốt nghiệp
                PreparedStatement gradStmt = conn.prepareStatement(updateGraduationSQL);
                gradStmt.setInt(1, graduation.getMaTruong());
                gradStmt.setInt(2, graduation.getMaNganh());
                gradStmt.setString(3, graduation.getHeTN());
                gradStmt.setString(4, graduation.getNgayTN());
                gradStmt.setString(5, graduation.getLoaiTN());
                gradStmt.setLong(6, graduation.getSoCMND());
                gradStmt.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    // Phương thức lấy thông tin sinh viên theo SoCMND (dùng cho chức năng sửa)
    public Student getStudentById(long soCMND) throws SQLException {
        String sql = "SELECT * FROM SINHVIEN WHERE SoCMND = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, soCMND);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Student(
                            rs.getLong("SoCMND"),
                            rs.getString("HoTen"),
                            rs.getString("Email"),
                            rs.getLong("SoDT"),
                            rs.getString("DiaChi")
                    );
                }
            }
        }
        return null;
    }

    // Phương thức lấy thông tin tốt nghiệp theo SoCMND (dùng cho chức năng sửa)
    public Graduation getGraduationById(long soCMND) throws SQLException {
        String sql = "SELECT * FROM TOT_NGHIEP WHERE SoCMND = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, soCMND);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Graduation(
                            rs.getLong("SoCMND"),
                            rs.getInt("MaTruong"),
                            rs.getInt("MaNganh"),
                            rs.getString("HeTN"),
                            rs.getString("NgayTN"),
                            rs.getString("LoaiTN")
                    );
                }
            }
        }
        return null;
    }
}
