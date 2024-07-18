import java.sql.*;

public class JDBCRunner {

    private static final String PROTOCOL = "jdbc:postgresql://";
    private static final String DRIVER = "org.postgresql.Driver";
    private static final String URL_LOCALE_NAME = "localhost/";

    private static final String DATABASE_NAME = "schedule2.0";

    public static final String DATABASE_URL = PROTOCOL + URL_LOCALE_NAME + DATABASE_NAME;
    public static final String USER_NAME = "postgres";
    public static final String DATABASE_PASS = "postgres";

    public static void main(String[] args) {

        checkDriver();
        checkDB();
        System.out.println("Подключение к базе данных | " + DATABASE_URL + "\n");

        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USER_NAME, DATABASE_PASS)) {

            System.out.println("student_id |   ФИО  |  birthday | speciality | group_number");
            getStudents(connection); System.out.println();
            System.out.println(" group_id  | number");
            getGroups(connection); System.out.println();
            System.out.println("teacher_id |    ФИО    |  birthday  | speciality");
            getTeachers(connection); System.out.println();
            System.out.println(" class_id  | number");
            getClasses(connection); System.out.println();

            System.out.println("student_id | teacher_id | class_id" + "\n" + "------------------------------------"); getSchedule_id(connection); System.out.println();
            System.out.println(" ID | ФИО преп. | №" + "\n" + "----------------------"); getSchedule(connection); System.out.println();

            addStudent(connection, "SI-0000001", "А А.А.", new Date(2000,1,1), "special-1", 100); System.out.println();
            addGroup(connection, "GI-0000001", 100); System.out.println();
            addTeacher(connection, "TI-0000001", "АА АА.АА.", new Date(2000,1,1), "special-1"); System.out.println();
            addClass(connection, "CI-0000001", 1000); System.out.println();

            removeStudent(connection, "SI-0000001"); System.out.println();
            removeGroup(connection, "GI-0000001"); System.out.println();
            removeTeacher(connection, "TI-0000001"); System.out.println();
            removeClass(connection, "CI-0000001"); System.out.println();

            System.out.println("student_id | teacher_id | class_id" + "\n" + "------------------------------------"); getSchedule_id(connection); System.out.println();
            System.out.println(" ID | ФИО преп. | №" + "\n" + "----------------------"); getSchedule(connection); System.out.println();

            System.out.println("group_id | teacher_id");
            getScheduleByGroupAndTeacher(connection, "GI-0000003", "TI-0000002"); System.out.println();

        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23")){
                System.out.println("Произошло дублирование данных");
            } else throw new RuntimeException(e);
        }
    }


    public static void checkDriver () {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Нет JDBC-драйвера! Подключите JDBC-драйвер к проекту согласно инструкции.");
            throw new RuntimeException(e);
        }
    }

    public static void checkDB () {
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, USER_NAME, DATABASE_PASS);
        } catch (SQLException e) {
            System.out.println("Нет базы данных! Проверьте имя базы, путь к базе или разверните локально резервную копию согласно инструкции");
            throw new RuntimeException(e);
        }
    }

    private static void getStudents(Connection connection) throws SQLException{
     
        String columnName0 = "student_id", columnName1 = "ФИО", columnName2 = "birthday", columnName3 = "speciality", columnName4 = "group_number";
        String param0 = null;
        String param1 = null;
        Date param2 = null;
        String param3 = null;
        int param4 = -1;

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM students;");

        while (rs.next()) {
            param2 = rs.getDate(columnName2);
            param1 = rs.getString(columnName1);
            param3 = rs.getString(columnName3);
            param4 = rs.getInt(columnName4);
            param0 = rs.getString(columnName0);
            System.out.println(param0 + " | " + param1 + " | " + param2 + " | " + param3 + " | " + param4);
        }
    }

    static void getGroups (Connection connection) throws SQLException {
        String param0 = null;
        int param1 = -1;
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM groups;");

        while (rs.next()) {
            param0 = rs.getString(1);
            param1 = rs.getInt(2);
            System.out.println(param0 + " | " + param1);
        }
    }

    private static void getTeachers(Connection connection) throws SQLException{
        String columnName0 = "teacher_id", columnName1 = "ФИО_преподавателя", columnName2 = "birthday", columnName3 = "speciality";
        String param0 = null;
        String param1 = null;
        Date param2 = null;
        String param3 = null;

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM teachers;");

        while (rs.next()) {
            param2 = rs.getDate(columnName2);
            param1 = rs.getString(columnName1);
            param3 = rs.getString(columnName3);
            param0 = rs.getString(columnName0);
            System.out.println(param0 + " | " + param1 + " | " + param2 + " | " + param3);
        }
    }

    static void getClasses (Connection connection) throws SQLException {
        String param0 = null;
        int param1 = -1;

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM classes;");

        while (rs.next()) {
            param0 = rs.getString(1);
            param1 = rs.getInt(2);
            System.out.println(param0 + " | " + param1);
        }
    }

    private static void getSchedule_id(Connection connection) throws SQLException{
        String columnName0 = "group_id", columnName1 = "teacher_id", columnName2 = "class_id";
        String param0 = null;
        String param1 = null;
        String param2 = null;

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM schedule_id;");

        while (rs.next()) {
            param2 = rs.getString(columnName2);
            param1 = rs.getString(columnName1);
            param0 = rs.getString(columnName0);
            System.out.println(param0 + " | " + param1 + " | " + param2);
        }
    }

    private static void getSchedule(Connection connection) throws SQLException{
        String columnName0 = "group_number", columnName1 = "ФИО_преподавателя", columnName2 = "class_number";
        String param0 = null;
        String param1 = null;
        String param2 = null;

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM schedule;");

        while (rs.next()) {
            param2 = rs.getString(columnName2);
            param1 = rs.getString(columnName1);
            param0 = rs.getString(columnName0);
            System.out.println(param0 + " | " + param1 + " | " + param2);
        }
    }

    private static void getScheduleByGroupAndTeacher(Connection connection, String group_id, String teacher_id) throws SQLException {
        String columnName0 = "ФИО", columnName1 = "group_number", columnName2 = "ФИО_преподавателя", columnName3 = "class_number";
        String param0 = null;
        int param1 = -1;
        String param2 = null;
        int param3 = -1;

        PreparedStatement statement = connection.prepareStatement(
                "SELECT DISTINCT " +
                        "    s.ФИО, " +
                        "    g.group_number, " +
                        "    t.ФИО_преподавателя, " +
                        "    c.class_number " +
                        "FROM " +
                        "    students s " +
                        "JOIN " +
                        "    groups g ON s.group_number = g.group_number " +
                        "JOIN " +
                        "    schedule_id sch ON g.group_id = sch.group_id " +
                        "JOIN " +
                        "    teachers t ON sch.teacher_id = t.teacher_id " +
                        "JOIN " +
                        "    classes c ON sch.class_id = c.class_id " +
                        "WHERE " +
                        "    g.group_id = ? AND t.teacher_id = ?");
        statement.setString(1, group_id);
        statement.setString(2, teacher_id);

        ResultSet rs = statement.executeQuery();

        System.out.println("ФИО | Группа | ФИО_преподавателя | Аудитория");
        while (rs.next()) {
            param0 = rs.getString(columnName0);
            param1 = rs.getInt(columnName1);
            param2 = rs.getString(columnName2);
            param3 = rs.getInt(columnName3);
            System.out.println(param0 + " | " + param1 + " | " + param2 + " | " + param3);
        }
    }


    private static void addStudent (Connection connection, String student_id, String full_name, Date birthday, String speciality, int group_number)  throws SQLException {
        if (student_id == null || student_id.isBlank() || full_name == null || full_name.isBlank() || group_number <= 0) return;
        PreparedStatement checkStatement = connection.prepareStatement("SELECT 1 FROM students WHERE student_id = ?");
        checkStatement.setString(1, student_id);
        ResultSet checkResult = checkStatement.executeQuery();
        if (checkResult.next()) {
            System.out.println("Студент с идентификатором " + student_id + " уже существует. Не удалось добавить нового студента.");
            return;
        }

        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO students(student_id, ФИО, birthday, speciality, group_number) VALUES (?, ?, ?, ?, ?) returning student_id;", Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, student_id);
        statement.setString(2, full_name);
        statement.setDate(3, birthday);
        statement.setString(4, speciality);
        statement.setInt(5, group_number);

        int count = statement.executeUpdate();
        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()) {
            System.out.println("Идентификатор студента " + rs.getString(1));
        }

        System.out.println();
        System.out.println("INSERTed " + count + " student");
        System.out.println();
        System.out.println("student_id |   ФИО  |  birthday | speciality | group_number");
        getStudents(connection);
    }

    private static void addGroup (Connection connection, String group_id, int group_number)  throws SQLException {
        if (group_id == null || group_id.isBlank() || group_number <= 0) return;

        PreparedStatement checkStatement = connection.prepareStatement("SELECT 1 FROM groups WHERE group_id = ?");
        checkStatement.setString(1, group_id);
        ResultSet checkResult = checkStatement.executeQuery();
        if (checkResult.next()) {
            System.out.println("Группа с идентификатором " + group_id + " уже существует. Не удалось добавить новую группу.");
            return;
        }

        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO groups(group_id, group_number) VALUES (?, ?) returning group_id;", Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, group_id);
        statement.setInt(2, group_number);

        int count = statement.executeUpdate();
        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()) {
            System.out.println("Идентификатор группы " + rs.getString(1));
        }

        System.out.println();
        System.out.println("INSERTed " + count + " group");
        System.out.println();
        System.out.println(" group_id  | number");
        getGroups(connection);
    }

    private static void addTeacher (Connection connection, String teacher_id, String full_name, Date birthday, String speciality)  throws SQLException {
        if (teacher_id == null || teacher_id.isBlank() || full_name == null || full_name.isBlank()) return;

        PreparedStatement checkStatement = connection.prepareStatement("SELECT 1 FROM teachers WHERE teacher_id = ?");
        checkStatement.setString(1, teacher_id);
        ResultSet checkResult = checkStatement.executeQuery();
        if (checkResult.next()) {
            System.out.println("Преподаватель с идентификатором " + teacher_id + " уже существует. Не удалось добавить нового преподавателя.");
            return;
        }

        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO teachers(teacher_id, ФИО_преподавателя, birthday, speciality) VALUES (?, ?, ?, ?) returning teacher_id;", Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, teacher_id);
        statement.setString(2, full_name);
        statement.setDate(3, birthday);
        statement.setString(4, speciality);

        int count = statement.executeUpdate();
        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()) {
            System.out.println("Идентификатор преподавателя " + rs.getString(1));
        }

        System.out.println();
        System.out.println("INSERTed " + count + " teacher");
        System.out.println();
        System.out.println("teacher_id |    ФИО    |  birthday  | speciality");
        getTeachers(connection);
    }

    private static void addClass (Connection connection, String class_id, int class_number)  throws SQLException {
        if (class_id == null || class_id.isBlank() || class_number <= 0) return;

        PreparedStatement checkStatement = connection.prepareStatement("SELECT 1 FROM classes WHERE class_id = ?");
        checkStatement.setString(1, class_id);
        ResultSet checkResult = checkStatement.executeQuery();
        if (checkResult.next()) {
            System.out.println("Аудитория с идентификатором " + class_id + " уже существует. Не удалось добавить новую аудиторию.");
            return;
        }

        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO classes(class_id, class_number) VALUES (?, ?) returning class_id;", Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, class_id);
        statement.setInt(2, class_number);

        int count = statement.executeUpdate();
        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()) {
            System.out.println("Идентификатор аудитории " + rs.getString(1));
        }

        System.out.println();
        System.out.println("INSERTed " + count + " class");
        System.out.println();
        System.out.println(" class_id  | number");
        getClasses(connection);
    }

    private static void removeStudent(Connection connection, String student_id) throws SQLException {
        if (student_id == null || student_id.isBlank()) return;

        PreparedStatement statement = connection.prepareStatement("DELETE from students WHERE student_id=?;");
        statement.setString(1, student_id);

        int count = statement.executeUpdate();
        System.out.println("DELETEd " + count + " student");
        System.out.println();
        System.out.println("student_id |   ФИО  |  birthday | speciality | group_number");
        getStudents(connection);
    }

    private static void removeGroup(Connection connection, String group_id) throws SQLException {
        if (group_id == null || group_id.isBlank()) return;

        PreparedStatement statement = connection.prepareStatement("DELETE from groups WHERE group_id=?;");
        statement.setString(1, group_id);

        int count = statement.executeUpdate();
        System.out.println("DELETEd " + count + " group");
        System.out.println();
        System.out.println(" group_id  | number");
        getGroups(connection);
    }

    private static void removeTeacher(Connection connection, String teacher_id) throws SQLException {
        if (teacher_id == null || teacher_id.isBlank()) return;

        PreparedStatement statement = connection.prepareStatement("DELETE from teachers WHERE teacher_id=?;");
        statement.setString(1, teacher_id);

        int count = statement.executeUpdate();
        System.out.println("DELETEd " + count + " teacher");
        System.out.println();
        System.out.println("teacher_id |    ФИО    |  birthday  | speciality");
        getTeachers(connection);
    }

    private static void removeClass(Connection connection, String class_id) throws SQLException {
        if (class_id == null || class_id.isBlank()) return;

        PreparedStatement statement = connection.prepareStatement("DELETE from classes WHERE class_id=?;");
        statement.setString(1, class_id);

        int count = statement.executeUpdate();
        System.out.println("DELETEd " + count + " class");
        System.out.println();
        System.out.println(" class_id  | number");
        getClasses(connection);
    }
}
