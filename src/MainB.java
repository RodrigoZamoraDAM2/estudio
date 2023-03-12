import java.sql.*;

public class MainB {
    // jdbc:mysql://@localhost:3306/ejemplo?serverTimezone=UTC&useServerPrepStmts=true
    private static final String DB_CONNECTION_URI = "jdbc:mysql://@workerh1.local.lcx06.me:3307/ejemplo?serverTimezone=UTC&useServerPrepStmts=true";
    private static final String DB_CONNECTION_USER = "root";
    private static final String DB_CONNECTION_PWD = "afuera";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection(DB_CONNECTION_URI, DB_CONNECTION_USER, DB_CONNECTION_PWD);
        System.out.println("Connected to database");

        /*CallableStatement cs = conn.prepareCall("{ CALL CREATE_EMPLOYEE(?,?,?,?) }");
        cs.setString(1,"marcos");
        cs.setString(2,"dir");
        cs.setFloat(3,2.7f);
        cs.setInt(4,100);

        cs.executeUpdate();
        cs.close();*/

        /*CallableStatement cs = conn.prepareCall("{ CALL GET_MOST_USED_DEPARTMENT_PROC(?)}");
        cs.registerOutParameter(1, Types.TINYINT);
        cs.executeUpdate();
        System.out.println(cs.getInt(1));
        cs.close();*/

        CallableStatement cs = conn.prepareCall("{ CALL GET_MOST_USED_DEPARTMENT_DETAILS_PROC()}");
        ResultSet rs = cs.executeQuery();

        while (rs.next()) {
            System.out.printf("%d - %s - %s - %.2f %n", rs.getInt("idD"), rs.getString(2),
                    rs.getString(3), rs.getFloat(4));
        }
        rs.close();
        cs.close();
        conn.close();
    }

}

/*
DROP PROCEDURE IF EXISTS CREATE_EMPLOYEE;
DROP FUNCTION IF EXISTS GET_MOST_USED_DEPARTMENT;
DROP PROCEDURE IF EXISTS GET_MOST_USED_DEPARTMENT_PROC;
DROP PROCEDURE IF EXISTS GET_MOST_USED_DEPARTMENT_DETAILS_PROC;

DELIMITER //

-- Ejercicio I

CREATE PROCEDURE CREATE_EMPLOYEE(v_apellido VARCHAR(15), v_dir VARCHAR(30),
                                 v_salario FLOAT(6, 2), v_idD TINYINT)
BEGIN
    INSERT INTO empleados (apellido, dir, fecha_alta, salario, idD)
    VALUES (v_apellido, v_dir, CURRENT_DATE, v_salario, v_idD);
END //

-- Ejercicio III

CREATE PROCEDURE GET_MOST_USED_DEPARTMENT_PROC(OUT v_idD TINYINT)
BEGIN
    SELECT idD
    INTO v_idD
    FROM empleados
    GROUP BY idD
    ORDER BY COUNT(*) DESC, idD DESC
    LIMIT 1;
END //

-- Ejercicio IV

CREATE PROCEDURE GET_MOST_USED_DEPARTMENT_DETAILS_PROC()
BEGIN
    DECLARE v_count INT;
    SELECT COUNT(*) AS count INTO v_count FROM empleados GROUP BY idD ORDER BY count DESC LIMIT 1;

    SELECT nom, bloque, pto
    FROM empleados
             JOIN dptos d ON d.idD = empleados.idD
    GROUP BY empleados.idD
    HAVING COUNT(*) = v_count;
END //

DELIMITER ;

CALL CREATE_EMPLOYEE('Test', 'DIR07', 1100, 110);
SELECT GET_MOST_USED_DEPARTMENT();

CALL GET_MOST_USED_DEPARTMENT_PROC(@idD);
SELECT @idD;

CALL GET_MOST_USED_DEPARTMENT_DETAILS_PROC();
 */