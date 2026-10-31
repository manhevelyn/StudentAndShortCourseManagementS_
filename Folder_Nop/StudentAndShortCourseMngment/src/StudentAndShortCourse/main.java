package StudentAndShortCourse;

import core.CourseList;
import core.StudentList;
import tool.ConsoleInputter;

/* Chương trình chính quản lý sinh viên và khóa học ngắn hạn */
public class main {
    public static void main(String[] args) {
        String stuFile = "Students.txt";
        String couFile = "Courses.txt";

        StudentList studentList = new StudentList();
        CourseList courseList = new CourseList(studentList);

        // Nạp sẵn dữ liệu ban đầu từ file text nếu file đã tồn tại
        studentList.readFile(stuFile);
        courseList.readFile(couFile);

        Object[] mnuOptions = {
            "List all students",
            "Add new student",
            "Search student by ID",
            "Update student by ID",
            "List students by major",
            "Add new course",
            "List courses by student",
            "Calculate total duration",
            "Remove student by ID",
            "Sort students by GPA",
            "Save data",
            "Quit"
        };

        boolean studentChanged = false;
        boolean courseChanged = false;
        int choice;

        do {
            System.out.println("\nSTUDENT AND SHORT COURSE MANAGEMENT SYSTEM");
            System.out.println("------------------------------------------");
            choice = ConsoleInputter.intMenu(mnuOptions);

            switch (choice) {
                case 1:
                    studentList.listAllStudents();
                    break;
                case 2:
                    if (studentList.addStudent()) {
                        studentChanged = true;
                    }
                    break;
                case 3:
                    studentList.searchStudent();
                    break;
                case 4:
                    if (studentList.updateStudent()) {
                        studentChanged = true;
                    }
                    break;
                case 5:
                    studentList.listStudentsByMajor();
                    break;
                case 6:
                    if (courseList.addCourse()) {
                        courseChanged = true;
                    }
                    break;
                case 7:
                    courseList.listCoursesByStudent(studentList);
                    break;
                case 8:
                    courseList.calculateTotalDuration();
                    break;
                case 9:
                    if (studentList.removeStudent(courseList)) {
                        studentChanged = true;
                    }
                    break;
                case 10:
                    studentList.sortStudentsByGpa();
                    break;
                case 11:
                    studentList.writeFile(stuFile);
                    courseList.writeFile(couFile);
                    studentChanged = false;
                    courseChanged = false;
                    break;
                case 12:
                    boolean confirm = ConsoleInputter.getBoolean("Do you want to exit the program?");
                    if (confirm) {
                        if (studentChanged || courseChanged) {
                            boolean saveResp = ConsoleInputter.getBoolean("Data has been changed. Do you want to save before exiting?");
                            if (saveResp) {
                                studentList.writeFile(stuFile);
                                courseList.writeFile(couFile);
                                System.out.println("Saved. Good bye!");
                            } else {
                                System.out.println("Good bye!");
                            }
                        } else {
                            System.out.println("Good bye!");
                        }
                    } else {
                        choice = 0; // Giữ vòng lặp tiếp tục chạy nếu người dùng chọn No
                    }
                    break;
            }
        } while (choice != 12);
    }
}
