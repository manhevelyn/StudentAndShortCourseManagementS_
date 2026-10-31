package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import tool.ConsoleInputter;

/* Lớp quản lý danh sách sinh viên */
public class StudentList extends ArrayList<Student> {
    public static final String ID_PAT = "^STU\\d{4}$";
    public static final String NAME_PAT = "^[a-zA-Z\\s]{2,50}$";
    // Nếu mà muốn đổi regex mã sinh viên thì cứ thay vô => vd: public static final
    // String ID_PAT = "^(SE|HE)\\d{6}$";

    public StudentList() {
        super();
    }

    /*
     * Chức năng 1: Hiển thị toàn bộ sinh viên dưới dạng bảng có tiêu đề
     * Input: Không
     * Output: Xuất bảng danh sách sinh viên ra màn hình
     */
    public void listAllStudents() {
        if (this.isEmpty()) {
            System.out.println(">> The student list is empty!");
            return;
        }

        System.out.println("\nLIST OF ALL STUDENTS");
        String header = "+------------+--------------------------+--------------------------+--------+";
        System.out.println(header);
        System.out.println("| ID         | Name                     | Major                    | GPA    |");
        System.out.println(header);
        for (Student s : this) {
            System.out.println(s.toString());
        }
        System.out.println(header);
        System.out.printf("Total: %d student(s).\n\n", this.size());
    }

    /*
     * Chức năng 2: Thêm sinh viên mới từ bàn phím
     * Input: Nhập từ bàn phím (stID, stName, major, gpa)
     * Output: Thêm 1 sinh viên vào studentList
     */
    public boolean addStudent() {
        boolean cont = true;
        boolean added = false;
        do {
            // Nhập và kiểm tra mã sinh viên: đúng format STU0000 và không trùng lặp
            String stID;
            int pos;
            do {
                stID = ConsoleInputter.getStr("Enter student ID (STU0000)", ID_PAT,
                        "Student ID must follow format STU0000 (e.g. STU0001)!");
                pos = this.indexOf(new Student(stID));
                if (pos >= 0) {
                    System.out.println(">> The student ID is duplicated!");
                }
            } while (pos >= 0);

            // Nhập và kiểm tra tên: chỉ chứa chữ cái và khoảng trắng, ít nhất 2 từ
            String stName;
            do {
                stName = ConsoleInputter.getStr("Enter student name", NAME_PAT,
                        "Student name must contain only alphabetic characters (2-50 chars)!");
                if (stName.trim().split("\\s+").length < 2) {
                    System.out.println(">> Student name must contain at least two words!");
                }
            } while (stName.trim().split("\\s+").length < 2);

            // Nhập chuyên ngành: không được để trống
            String major;
            do {
                major = ConsoleInputter.getStr("Enter major");
                if (major.isEmpty()) {
                    System.out.println(">> Major cannot be empty!");
                }
            } while (major.isEmpty());

            // Nhập điểm GPA: giá trị trong đoạn [0.0, 4.0]
            double gpa = ConsoleInputter.getDouble("Enter GPA (0.0 - 4.0)", 0.0, 4.0);

            Student newStudent = new Student(stID.toUpperCase(), stName, major, gpa);
            this.add(newStudent);
            added = true;
            System.out.println(">> Student added successfully.");

            cont = ConsoleInputter.getBoolean("Do you want to continue adding a new student?");
        } while (cont);
        return added;
    }

    /*
     * Chức năng 3: Tìm kiếm sinh viên theo ID
     * Input: Mã sinh viên
     * Output: Xuất thông tin sinh viên hoặc thông báo không tồn tại
     */
    public void searchStudent() {
        if (this.isEmpty()) {
            System.out.println(">> The student list is empty!");
            return;
        }
        String id = ConsoleInputter.getStr("Enter student ID to search");
        int pos = this.indexOf(new Student(id));
        if (pos < 0) {
            System.out.println("Student ID does not exist!");
        } else {
            System.out.println(this.get(pos).toStringScreen());
        }
    }

    /*
     * Chức năng 4: Cập nhật GPA sinh viên theo ID
     * Input: Mã sinh viên và GPA mới
     * Output: Cập nhật GPA trong bộ nhớ
     */
    public boolean updateStudent() {
        if (this.isEmpty()) {
            System.out.println(">> The student list is empty!");
            return false;
        }
        String id = ConsoleInputter.getStr("Enter student ID to update");
        int pos = this.indexOf(new Student(id));
        if (pos < 0) {
            System.out.println("Student ID does not exist!");
            System.out.println(">> Update failed!");
            return false;
        }

        Student student = this.get(pos);
        System.out.println(">> Student found:");
        System.out.println(student.toStringScreen());
        double newGpa = ConsoleInputter.getDouble("Enter new GPA (0.0 - 4.0)", 0.0, 4.0);
        student.setGpa(newGpa);
        System.out.println(">> Update successfully.");
        return true;
    }

    /*
     * Chức năng 5: Liệt kê sinh viên theo chuyên ngành (Major)
     * Input: Tên chuyên ngành
     * Output: Bảng danh sách sinh viên có Major phù hợp
     */
    public void listStudentsByMajor() {
        if (this.isEmpty()) {
            System.out.println(">> The student list is empty!");
            return;
        }
        String major;
        do {
            major = ConsoleInputter.getStr("Enter major to filter");
            if (major.isEmpty()) {
                System.out.println(">> Major cannot be empty!");
            }
        } while (major.isEmpty());

        ArrayList<Student> matched = new ArrayList<>();
        for (Student s : this) {
            if (s.getMajor().equalsIgnoreCase(major)) {
                matched.add(s);
            }
        }

        if (matched.isEmpty()) {
            System.out.println(">> No student found with major: " + major);
            return;
        }

        System.out.println("\nSTUDENTS IN MAJOR: " + major.toUpperCase());
        String header = "+------------+--------------------------+--------------------------+--------+";
        System.out.println(header);
        System.out.println("| ID         | Name                     | Major                    | GPA    |");
        System.out.println(header);
        for (Student s : matched) {
            System.out.println(s.toString());
        }
        System.out.println(header);
        System.out.printf("Total: %d student(s) found.\n\n", matched.size());
    }

    /*
     * Chức năng 9: Xóa sinh viên theo ID (từ chối xóa nếu sinh viên đã được gán vào
     * khóa học)
     * Input: Mã sinh viên
     * Output: Xóa sinh viên khỏi danh sách hoặc thông báo lỗi
     */
    public boolean removeStudent(CourseList courseList) {
        if (this.isEmpty()) {
            System.out.println(">> The student list is empty!");
            return false;
        }
        String id = ConsoleInputter.getStr("Enter student ID to remove");
        int pos = this.indexOf(new Student(id));
        if (pos < 0) {
            System.out.println("Student ID does not exist!");
            System.out.println(">> Delete failed!");
            return false;
        }

        // Kiểm tra ràng buộc khóa ngoại với danh sách khóa học
        if (courseList != null && courseList.isStudentAssigned(id)) {
            System.out.println("Cannot delete: Student is assigned to Courses.");
            System.out.println(">> Delete failed!");
            return false;
        }

        boolean confirm = ConsoleInputter.getBoolean("Are you sure you want to delete this student?");
        if (confirm) {
            this.remove(pos);
            System.out.println(">> Delete successfully.");
            return true;
        } else {
            System.out.println(">> Deletion canceled.");
            return false;
        }
    }

    /*
     * Chức năng 10: Sắp xếp danh sách sinh viên theo GPA tăng dần
     * Input: Không
     * Output: Bảng danh sách sinh viên đã sắp xếp
     */
    public void sortStudentsByGpa() {
        if (this.isEmpty()) {
            System.out.println(">> The student list is empty!");
            return;
        }
        ArrayList<Student> sortedList = new ArrayList<>(this);
        Collections.sort(sortedList);

        System.out.println("\nSTUDENTS SORTED BY GPA (ASCENDING)");
        String header = "+------------+--------------------------+--------------------------+--------+";
        System.out.println(header);
        System.out.println("| ID         | Name                     | Major                    | GPA    |");
        System.out.println(header);
        for (Student s : sortedList) {
            System.out.println(s.toString());
        }
        System.out.println(header);
        System.out.printf("Total: %d student(s).\n\n", sortedList.size());
    }

    /*
     * Đọc danh sách sinh viên từ file text CSV
     * Mỗi dòng: stID, stName, major, gpa
     * Bỏ qua các dòng sai định dạng hoặc không đủ trường
     */
    public void readFile(String fName) {
        File f = new File(fName);
        if (!f.exists()) {
            System.out.println("The file " + fName + " does not exist. Ignored.");
            return;
        }
        this.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty())
                    continue;
                try {
                    String[] parts = line.split(",");
                    if (parts.length < 4)
                        continue; // Bỏ qua dòng thiếu trường
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    String major = parts[2].trim();
                    double gpa = Double.parseDouble(parts[3].trim());

                    // Bỏ qua nếu ID đã tồn tại (trùng lặp)
                    if (this.indexOf(new Student(id)) >= 0)
                        continue;

                    this.add(new Student(id, name, major, gpa));
                } catch (Exception e) {
                    // Bỏ qua dòng lỗi parse
                }
            }
            System.out.println("All data in the file were read.");
        } catch (Exception e) {
            System.err.println(">> Error reading file: " + e.getMessage());
        }
    }

    /* Ghi danh sách sinh viên vào file text CSV */
    public void writeFile(String fName) {
        if (this.isEmpty()) {
            System.out.println("Empty list.");
            return;
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(fName))) {
            for (Student s : this) {
                pw.println(s.toFileString());
            }
            System.out.println("Data are saved to file.");
        } catch (IOException e) {
            System.err.println(">> Error writing file: " + e.getMessage());
        }
    }
}
