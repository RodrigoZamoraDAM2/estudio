import java.sql.*;

public class Main {

    // jdbc:mysql://@localhost:3306/ejemplo?serverTimezone=UTC&useServerPrepStmts=true
    private static final String DB_CONNECTION_URI = "jdbc:mysql://@workerh1.local.lcx06.me:3307/ejemplo?serverTimezone=UTC&useServerPrepStmts=true";
    private static final String DB_CONNECTION_USER = "root";
    private static final String DB_CONNECTION_PWD = "afuera";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection(DB_CONNECTION_URI, DB_CONNECTION_USER, DB_CONNECTION_PWD);
        System.out.println("Connected to database");

        // Statement
        Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = st.executeQuery("SELECT * FROM dptos WHERE pto > 100;");

        /*rs.afterLast();
        rs.last();
        rs.previous();

        rs.first();
        rs.beforeFirst();
        rs.next();*/

        rs.afterLast();
        while (rs.previous()) {
            System.out.printf("%d - %s - %s - %.2f %n", rs.getInt("idD"), rs.getString(2),
                    rs.getString(3), rs.getFloat(4));

            /* Update
            rs.updateFloat(4, rs.getFloat(4) * 1.1f);
            rs.updateRow(); */

            // rs.deleteRow(); // Borrar

            /* Inserción
            rs.moveToInsertRow();
            rs.updateFloat();
            ...
            rs.insertRow();
            */
        }

        rs.close();
        st.close();

        // PreparedStatement
        // String sql = "";
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM dptos WHERE nom LIKE ? AND pto > ?;",
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        char c = 'T';
        ps.setString(1, c + "%");
        ps.setFloat(2, 100);
        ResultSet res = ps.executeQuery();
        while (res.next()) {
            float dataPto = res.getFloat("pto");
            float newPto = dataPto * 1.01f;
            System.out.printf("%.2f -> %.2f %n", dataPto, newPto);
            res.updateFloat("pto", newPto);
            res.updateRow();
        }
        res.close();
        ps.close();

        // CallableStatement
        CallableStatement cs = conn.prepareCall("{ ? = CALL TEMPLATE(?) }");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.setInt(2, 5);
        cs.executeUpdate();
        System.out.println(cs.getInt(1));
        cs.close();

        conn.close();
    }
}

    /*
DROP FUNCTION IF EXISTS TEMPLATE;
DROP FUNCTION IF EXISTS obtBloque;
DELIMITER //

CREATE FUNCTION TEMPLATE(test INT) RETURNS INT
BEGIN
    # code
    RETURN test;
END //

CREATE FUNCTION obtBloque(p_idD TINYINT) RETURNS CHAR
BEGIN
    DECLARE v_bloque CHAR DEFAULT ' ';

    SET v_bloque = (SELECT bloque FROM dptos WHERE idD = p_idD);
    SELECT bloque INTO v_bloque FROM dptos WHERE idD = p_idD;

    RETURN v_bloque;
END //

DELIMITER ;

SELECT obtBloque(90);

DROP PROCEDURE IF EXISTS TEMPLATE_PROC;

DELIMITER //

CREATE PROCEDURE TEMPLATE_PROC(IN v_test INT)
BEGIN
    # code
END //

DELIMITER ;

CALL TEMPLATE_PROC(5);

DROP PROCEDURE IF EXISTS obtBloqueProc;
DELIMITER //

CREATE PROCEDURE obtBloqueProc(IN p_idD TINYINT, OUT p_bloque CHAR)
BEGIN
    SELECT bloque INTO p_bloque FROM dptos WHERE idD = p_idD;
END //

DELIMITER ;
CALL obtBloqueProc(90, @bloque);
SELECT @bloque;

# Procedimiento que obtenga mediante un param de salida el departamento siguiente al introducido
DROP PROCEDURE IF EXISTS obtSiguienteProc;
DELIMITER //

CREATE PROCEDURE obtSiguienteProc(INOUT p_idD TINYINT)
BEGIN
    SELECT idD INTO p_idD FROM dptos WHERE idD > p_idD ORDER BY idD LIMIT 1;
END //

DELIMITER ;

SET @idD = 100;
CALL obtSiguienteProc(@idD);
SELECT @idD;

     */