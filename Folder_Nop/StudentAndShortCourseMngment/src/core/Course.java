package core;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import tool.ConsoleInputter;

/* Lớp mô tả cho 1 khóa học ngắn hạn (Course) */
public class Course implements Serializable {
    public static final String DATE_PAT = "dd/MM/yyyy";

    private String courseID;      // Mã khóa học, duy nhất
    private String studID;        // Mã sinh viên học khóa này (phải tồn tại trong StudentList)
    private String courseName;    // Tên khóa học (không được để trống)
    private int duration;         // Thời lượng tính theo tuần (>= 1)
    private Date startDate;       // Ngày bắt đầu (Future date khi thêm mới)

    public Course() {
    }

    // Constructor dùng cho thao tác tìm kiếm và so sánh dựa trên courseID
    public Course(String courseID) {
        this.courseID = courseID;
    }

    // Constructor đầy đủ tham số
    public Course(String courseID, String studID, String courseName, int duration, Date startDate) {
        this.courseID = courseID;
        this.studID = studID;
        this.courseName = courseName;
        this.duration = duration;
        this.startDate = startDate;
    }

    // Hành vi kiểm tra bằng nhau theo mã khóa học phục vụ tìm kiếm trong ArrayList
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Course other = (Course) obj;
        return Objects.equals(this.courseID != null ? this.courseID.toUpperCase() : null,
                              other.courseID != null ? other.courseID.toUpperCase() : null);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.courseID != null ? this.courseID.toUpperCase() : null);
    }

    // Getters và Setters
    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getStudID() {
        return studID;
    }

    public void setStudID(String studID) {
        this.studID = studID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    // Xuất thông tin chi tiết khóa học ra màn hình console theo dạng khối dọc
    public String toStringScreen() {
        String s = "------------------------------------------\n"
                 + "Course ID    : " + courseID + "\n"
                 + "Student ID   : " + studID + "\n"
                 + "Course Name  : " + courseName + "\n"
                 + "Duration     : " + duration + " week(s)\n"
                 + "Start Date   : " + ConsoleInputter.dateStr(startDate, DATE_PAT) + "\n"
                 + "------------------------------------------";
        return s;
    }

    // Xuất thông tin một dòng dữ liệu cho bảng danh sách khóa học
    @Override
    public String toString() {
        return String.format("| %-10s | %-10s | %-26s | %-8d | %-12s |",
                courseID, studID, courseName, duration,
                ConsoleInputter.dateStr(startDate, DATE_PAT));
    }

    // Xuất dữ liệu định dạng CSV để ghi ra file Courses.txt
    public String toFileString() {
        return String.format("%s, %s, %s, %d, %s",
                courseID, studID, courseName, duration,
                ConsoleInputter.dateStr(startDate, DATE_PAT));
    }
}
