DROP TYPE PLAYER;

CREATE TYPE PLAYER AS OBJECT
(
    nombre VARCHAR2(50),
    vida   NUMBER,

    CONSTRUCTOR FUNCTION PLAYER(nombre VARCHAR2, vida NUMBER) RETURN SELF AS
        RESULT,
    CONSTRUCTOR FUNCTION PLAYER(nombre VARCHAR2) RETURN SELF AS
        RESULT,

    MEMBER FUNCTION GET_NOMBRE RETURN VARCHAR2,
    FINAL MEMBER PROCEDURE SET_VIDA(vida NUMBER),
-- Procedimiento estatico que muestre los datos de un jugador pasado por parametro
    STATIC PROCEDURE DATOS(player PLAYER)
) NOT FINAL;/

CREATE OR REPLACE TYPE BODY PLAYER AS
    CONSTRUCTOR FUNCTION PLAYER(nombre VARCHAR2, vida NUMBER)
        RETURN SELF AS RESULT
    AS
    BEGIN
        SELF.nombre := nombre;
        SELF.vida := vida;
        RETURN;
    END;

    CONSTRUCTOR FUNCTION PLAYER(nombre VARCHAR2)
        RETURN SELF AS RESULT
    AS
    BEGIN
        SELF.nombre := nombre;
        SELF.vida := 10;
        RETURN;
    END;

    MEMBER FUNCTION GET_NOMBRE RETURN VARCHAR2
    AS
    BEGIN
        RETURN SELF.nombre;
    END;

    FINAL MEMBER PROCEDURE SET_VIDA(vida NUMBER)
    AS
    BEGIN
        SELF.vida := vida;
    END;

    STATIC PROCEDURE DATOS(player PLAYER)
    AS
    BEGIN
        DBMS_OUTPUT.PUT_LINE('su puto nombre aqui ' || player.nombre);
        DBMS_OUTPUT.PUT_LINE(player.vida);
    END;
END;

SET SERVEROUTPUT ON;
DECLARE
    player1 PLAYER;
    player2 PLAYER;
BEGIN
    player1 := PLAYER('venancio',5);
    player2 := PLAYER('estroncio');
    DBMS_OUTPUT.PUT_LINE(player1.GET_NOMBRE());
    player1.SET_VIDA(10);
    DBMS_OUTPUT.PUT_LINE(player1.vida);
    PLAYER.DATOS(player1);

EXCEPTION
    WHEN OTHERS THEN DBMS_OUTPUT.PUT_LINE('Error: ' || SQLCODE || ' - ' || SQLERRM);
END;

CREATE TABLE PLAYERS OF PLAYER(
    nombre PRIMARY KEY
);
INSERT INTO PLAYERS VALUES ('asd', 10);

CREATE TABLE PLAYERS_2 (player PLAYER);
INSERT INTO PLAYERS_2 VALUES (PLAYER('asd'));

-- CREATE OR REPLACE TYPE DIRECCION_NUM UNDER DIRECCION
-- (
--     num NUMBER(3),
--
--     MEMBER PROCEDURE SET_NUM(n NUMBER),
--     MEMBER FUNCTION GET_NUM RETURN NUMBER,
--     OVERRIDING MEMBER PROCEDURE VER_DATOS
-- ) NOT FINAL;
--
-- CREATE OR REPLACE TYPE BODY DIRECCION_NUM AS
--     MEMBER PROCEDURE SET_NUM(n NUMBER) IS
--     BEGIN
--         num := n;
--     END;
--
--     MEMBER FUNCTION GET_NUM RETURN NUMBER IS
--     BEGIN
--         RETURN num;
--     END;
--
--     OVERRIDING MEMBER PROCEDURE VER_DATOS IS
--     BEGIN
--         DBMS_OUTPUT.PUT_LINE('Calle: ' || calle);
--         DBMS_OUTPUT.PUT_LINE('Ciudad: ' || ciudad);
--         DBMS_OUTPUT.PUT_LINE('CP: ' || cp);
--         DBMS_OUTPUT.PUT_LINE('Número: ' || num);
--     END;
-- END;