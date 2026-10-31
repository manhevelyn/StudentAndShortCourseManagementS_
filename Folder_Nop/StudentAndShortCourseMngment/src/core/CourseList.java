package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import tool.ConsoleInputter;

/* Lớp quản lý danh sách các khóa học ngắn hạn */
public class CourseList extends ArrayList<Course> {
    public static final String DATE_PAT = "dd/MM/yyyy"; // public static final String DATE_PAT = "yyyy-MM-dd";
    private StudentList studentList; // Tham chiếu đến danh sách sinh viên để kiểm tra và đối soát

    public CourseList(StudentList studentList) {
        super();
        this.studentList = studentList;
    }

    public void setStudentList(StudentList studentList) {
        this.studentList = studentList;
    }

    // Kiểm tra sinh viên đã có ít nhất 1 khóa học trong danh sách hay chưa
    public boolean isStudentAssigned(String stID) {
        if (stID == null)
            return false;
        for (Course c : this) {
            if (c.getStudID() != null && c.getStudID().equalsIgnoreCase(stID)) {
                return true;
            }
        }
        return false;
    }

    /*
     * Chức năng 6: Thêm 1 khóa học mới
     * Input: Nhập từ bàn phím (courseID, studID, courseName, duration, startDate)
     * Output: Thêm khóa học vào courseList
     */
    public boolean addCourse() {
        if (studentList == null || studentList.isEmpty()) {
            System.out.println(">> The student list is empty! Student information must be registered first.");
            return false;
        }

        boolean cont = true;
        boolean added = false;
        do {
            // Nhập và kiểm tra mã khóa học: không rỗng và không trùng lặp
            String courseID;
            int pos;
            do {
                courseID = ConsoleInputter.getStr("Enter course ID");
                if (courseID.isEmpty()) {
                    System.out.println(">> Course ID cannot be empty!");
                    pos = 0;
                    continue;
                }
                pos = this.indexOf(new Course(courseID));
                if (pos >= 0) {
                    System.out.println(">> The course ID is duplicated!");
                }
            } while (pos >= 0 || courseID.isEmpty());

            // Nhập và kiểm tra mã sinh viên: phải tồn tại trong StudentList
            String studID;
            int stPos;
            do {
                studID = ConsoleInputter.getStr("Enter student ID");
                stPos = studentList.indexOf(new Student(studID));
                if (stPos < 0) {
                    System.out.println(">> Student ID does not exist! Please enter again.");
                }
            } while (stPos < 0);

            Student targetStudent = studentList.get(stPos);
            System.out.println(">> Student found: " + targetStudent.getStName());

            // Nhập tên khóa học: không được để trống
            String courseName;
            do {
                courseName = ConsoleInputter.getStr("Enter course name");
                if (courseName.isEmpty()) {
                    System.out.println(">> Course name cannot be empty!");
                }
            } while (courseName.isEmpty());

            // Nhập thời lượng: số nguyên >= 1
            int duration = ConsoleInputter.getInt("Enter duration in weeks (>= 1)", 1, Integer.MAX_VALUE);

            // Nhập ngày bắt đầu: phải là ngày trong tương lai
            Date startDate;
            Date today = new Date();
            do {
                startDate = ConsoleInputter.getDate("Enter start date", DATE_PAT);
                if (!startDate.after(today)) {

                    // Bây giờ khóa học này là khóa học bù cho đợt trước, hãy sửa lại để chỉ chấp
                    // nhận ngày trong quá khứ (trước ngày hôm nay)."
                    // * if (!startDate.before(today)) {
                    // System.out.println(">> Start date must be a past date (before today)!");
                    // }
                    System.out.println(">> Start date must be a future date (after today)!");
                }
            } while (!startDate.after(today));

            Course newCourse = new Course(courseID, studID, courseName, duration, startDate);
            System.out.println("\nCourse Information:");
            System.out.println(newCourse.toStringScreen());

            boolean confirm = ConsoleInputter.getBoolean("Do you want to save this course?");
            if (confirm) {
                this.add(newCourse);
                added = true;
                System.out.println(">> New course was added successfully.");
            } else {
                System.out.println(">> Saving canceled.");
            }

            cont = ConsoleInputter.getBoolean("Do you want to continue adding a new course?");
        } while (cont);
        return added;

    }

    /*
     * Chức năng 7: Liệt kê các khóa học theo từng sinh viên
     * Input: Tham chiếu đến StudentList
     * Output: Báo cáo COURSES BY STUDENT
     */
    public void listCoursesByStudent(StudentList sList) {
        if (sList == null || sList.isEmpty()) {
            System.out.println(">> The student list is empty!");
            return;
        }

        System.out.println("\nCOURSES BY STUDENT");
        System.out.println("================================================================================");
        for (Student st : sList) {
            System.out.printf("Student: %s - %s [Major: %s]\n", st.getStID(), st.getStName(), st.getMajor());

            // Lọc các khóa học của sinh viên này
            ArrayList<Course> stCourses = new ArrayList<>();
            for (Course c : this) {
                if (c.getStudID() != null && c.getStudID().equalsIgnoreCase(st.getStID())) {
                    stCourses.add(c);
                }
            }

            if (stCourses.isEmpty()) {
                System.out.println("  (No courses enrolled yet)");
            } else {
                for (Course c : stCourses) {
                    System.out.printf("  - [%s] %s | Duration: %d week(s) | Start: %s\n",
                            c.getCourseID(),
                            c.getCourseName(),
                            c.getDuration(),
                            ConsoleInputter.dateStr(c.getStartDate(), DATE_PAT));
                }
            }
            System.out.println("--------------------------------------------------------------------------------");
        }
        System.out.println("================================================================================\n");
    }

    /*
     * Chức năng 8: Tính tổng thời lượng các khóa học của 1 sinh viên
     * Input: Mã sinh viên
     * Output: Tổng số tuần
     */
    public void calculateTotalDuration() {
        if (studentList == null || studentList.isEmpty()) {
            System.out.println(">> The student list is empty!");
            return;
        }
        String stID = ConsoleInputter.getStr("Enter student ID to calculate total duration");
        int stPos = studentList.indexOf(new Student(stID));
        if (stPos < 0) {
            System.out.println("Student ID does not exist!");
            return;
        }

        Student st = studentList.get(stPos);
        int totalDuration = 0;
        int courseCount = 0;
        for (Course c : this) {
            if (c.getStudID() != null && c.getStudID().equalsIgnoreCase(stID)) {
                totalDuration += c.getDuration();
                courseCount++;
            }
        }

        System.out.println("------------------------------------------");
        System.out.println("Student      : " + st.getStID() + " - " + st.getStName());
        System.out.println("Courses      : " + courseCount + " course(s)");
        System.out.println("Total duration: " + totalDuration + " week(s)");
        System.out.println("------------------------------------------");
    }

    /*
     * Đọc danh sách khóa học từ file text CSV (Defensive Parsing)
     * Mỗi dòng: courseID, studID, courseName, duration, startDate
     * Bỏ qua các dòng sai format, trùng ID hoặc studID không tồn tại
     * KHÔNG kiểm tra Future date khi đọc file (dữ liệu lịch sử)
     */
    public void readFile(String fName) {
        File f = new File(fName);
        if (!f.exists()) {
            System.out.println("The file " + fName + " does not exist. Ignored.");
            return;
        }
        this.clear();
        DateFormat df = new SimpleDateFormat(DATE_PAT);
        df.setLenient(false);

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty())
                    continue;
                try {
                    String[] parts = line.split(",");
                    if (parts.length < 5)
                        continue; // Bỏ qua dòng thiếu trường

                    String courseID = parts[0].trim();
                    String studID = parts[1].trim();
                    String courseName = parts[2].trim();
                    int duration = Integer.parseInt(parts[3].trim());
                    Date startDate = df.parse(parts[4].trim());

                    // Bỏ qua nếu Course ID đã tồn tại (trùng lặp)
                    if (this.indexOf(new Course(courseID)) >= 0)
                        continue;

                    // Bỏ qua nếu Student ID không tồn tại trong danh sách sinh viên
                    if (studentList != null && studentList.indexOf(new Student(studID)) < 0)
                        continue;

                    this.add(new Course(courseID, studID, courseName, duration, startDate));
                } catch (Exception e) {
                    // Bỏ qua dòng lỗi parse (sai format, sai ngày, sai số,...)
                }
            }
            System.out.println("All data in the file were read.");
        } catch (Exception e) {
            System.err.println(">> Error reading file: " + e.getMessage());
        }
    }

    // Ghi danh sách khóa học vào file text CSV
    public void writeFile(String fName) {
        if (this.isEmpty()) {
            System.out.println("Empty list.");
            return;
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(fName))) {
            for (Course c : this) {
                pw.println(c.toFileString());
            }
            System.out.println("Data are saved to file.");
        } catch (IOException e) {
            System.err.println(">> Error writing file: " + e.getMessage());
        }
    }
}
