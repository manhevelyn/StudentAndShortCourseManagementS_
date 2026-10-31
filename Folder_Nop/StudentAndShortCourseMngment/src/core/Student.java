package core;

import java.io.Serializable;
import java.util.Objects;

/* Lớp mô tả cho 1 sinh viên (Student) */
public class Student implements Serializable, Comparable<Student> {
    private String stID;      // Mã sinh viên (STU0000, duy nhất)
    private String stName;    // Họ tên sinh viên (ít nhất 2 từ)
    private String major;     // Chuyên ngành (không rỗng)
    private double gpa;       // Điểm trung bình tích lũy (0.0 .. 4.0)

    public Student() {
    }

    // Constructor dùng cho thao tác tìm kiếm và so sánh dựa trên stID
    public Student(String stID) {
        this.stID = stID;
    }

    // Constructor đầy đủ tham số
    public Student(String stID, String stName, String major, double gpa) {
        this.stID = stID;
        this.stName = stName;
        this.major = major;
        this.gpa = gpa;
    }

    // Hành vi kiểm tra bằng nhau phục vụ quá trình tìm kiếm trong ArrayList theo stID
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student other = (Student) obj;
        return Objects.equals(this.stID != null ? this.stID.toUpperCase() : null,
                              other.stID != null ? other.stID.toUpperCase() : null);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.stID != null ? this.stID.toUpperCase() : null);
    }

    // So sánh sắp xếp theo GPA tăng dần (Ascending)
    @Override
    public int compareTo(Student o) {
        return Double.compare(this.gpa, o.gpa);
    }

    // Getters và Setters
    public String getStID() {
        return stID;
    }

    public void setStID(String stID) {
        this.stID = stID;
    }

    public String getStName() {
        return stName;
    }

    public void setStName(String stName) {
        this.stName = stName;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    // Xuất thông tin chi tiết sinh viên ra màn hình console theo dạng khối dọc
    public String toStringScreen() {
        String s = "------------------------------------------\n"
                 + "Student ID   : " + stID + "\n"
                 + "Student Name : " + stName + "\n"
                 + "Major        : " + major + "\n"
                 + "GPA          : " + String.format(java.util.Locale.US, "%.2f", gpa) + "\n"
                 + "------------------------------------------";
        return s;
    }

    // Xuất thông tin một dòng dữ liệu cho bảng danh sách sinh viên
    @Override
    public String toString() {
        return String.format(java.util.Locale.US, "| %-10s | %-24s | %-24s | %-6.2f |",
                stID, stName, major, gpa);
    }

    // Xuất dữ liệu định dạng CSV để ghi ra file Students.txt
    public String toFileString() {
        String gpaStr = (gpa == (long) gpa)
                ? String.format(java.util.Locale.US, "%.1f", gpa)
                : String.format(java.util.Locale.US, "%.2f", gpa).replaceAll("0+$", "");
        return String.format("%s, %s, %s, %s", stID, stName, major, gpaStr);
    }
}
