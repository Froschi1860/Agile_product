package se.hkr.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javafx.event.ActionEvent;

public class AuthenticationTest {
    Authentication auth = new Authentication();

    @Mock
    private Connection con;

    @Mock
    private PreparedStatement stmt;

    @Mock
    private ResultSet rs;

    @Mock
    ResultSetMetaData rsmd;


    @BeforeEach
    public void init() {
        User.resetUserInstance();
    }

    // Login methods tests

    @Test
    @DisplayName("Test the checkLoginCredential method. With incorrect input")
    public void testIncorrectCheckLoginCredentials() throws SQLException {
        String email = "incorectemail@gmail.com";
        String password = "incorrectPassword";
        Boolean res = auth.checkLoginCredentials(email, password);
        assertTrue(!res);
        assertNotNull(DatabaseConnection.getInstance());
    }

    @Test
    @DisplayName("Test the checkLoginCredential method. With correct input")
    public void testCheckLoginCredentials() throws SQLException {
        String email = "enzotiberghien28@gmail.com";
        String password = "alabama";
        Boolean res = auth.checkLoginCredentials(email, password);
        assertTrue(res);
        assertNotNull(User.getInstance());
        assertNotNull(DatabaseConnection.getInstance());
    }

    @Test
    @DisplayName("Test the LogError Alert")
    public void testLogError() {
        try (MockedStatic<Authentication> msAlert = Mockito.mockStatic(Authentication.class)) {
            msAlert.when(() -> Authentication.logError()).thenCallRealMethod();
        }

    }

    //Registration methods tests

    @Test
    @DisplayName("Test the checkAvailability method, not available")
    public void testCheckAvailabilityNo() throws SQLException {
        Boolean[] req = auth.checkAvailability("280201-4999", "enzotiberghien28@gmail.com");
        Boolean res = Arrays.asList(req).contains(false);
        assertTrue(res);
        assertNotNull(DatabaseConnection.getInstance());
    }

    @Test
    @DisplayName("Test the checkAvailability method, available")
    public void testCheckAvailabilityYes() throws SQLException {
        Boolean[] req = auth.checkAvailability("280201-5000", "enzotiberghien972@gmail.com");
        Boolean res = Arrays.asList(req).contains(false);
        assertTrue(!res);
        assertNotNull(DatabaseConnection.getInstance());
    }

    @Test
    @DisplayName("Test the registerUser method")
    public void testRegisterUser() {
        short returnValue = 1;
        String personnummer = "010101-0001";
        String name = "test";
        String email = "testuser@gmail.com";
        String password = "alabama";
        auth.registerUser(personnummer, name, email, password);
        assertNotNull(DatabaseConnection.getInstance());
        try (MockedStatic<DatabaseApiInsert> ms = Mockito.mockStatic(DatabaseApiInsert.class)) {
            ms.when(() -> DatabaseApiInsert.createUserEntry((con), personnummer, name, email, password))
                    .thenReturn(returnValue);
        }
    }

    @Test
    @DisplayName("Test the successRegistration Alert")
    public void testSuccesRegistration() {
        try (MockedStatic<Authentication> msAlert = Mockito.mockStatic(Authentication.class)) {
            msAlert.when(() -> Authentication.successRegistration()).thenCallRealMethod();
        }
    }

    @Test
    @DisplayName("Test the registerError Alert")
    public void testRegisterError() {
        try (MockedStatic<Authentication> msAlert = Mockito.mockStatic(Authentication.class)) {
            msAlert.when(() -> Authentication.registerError()).thenCallRealMethod();
        }
    }


    // Email Regex

    @Test
    @DisplayName("Test the validEmail method with correct input")
    public void testValidEmailTrue() {
        Boolean res = auth.validEmail("enzotiberghien28@gmail.com");
        assertTrue(res);
    }

    @Test
    @DisplayName("Test the validEmail method with incorrect input")
    public void testValidEmailFalse() {
        Boolean res = auth.validEmail("enzotiberghienATgmail.com");
        assertFalse(res);
    }

    // Personnummer Regex

    @Test
    @DisplayName("Test the validPersonnummer method with correct input")
    public void testValidPersonnummerTrue() {
        Boolean res = auth.validPersonnummer("110203-4890");
        assertTrue(res);
    }

    @Test
    @DisplayName("Test the validPersonnummer method with incorrect input")
    public void testValidPersonnumerFalse() {
        Boolean res = auth.validPersonnummer("010203-77777");
        assertFalse(res);
    }

    // Name Regex

    @Test
    @DisplayName("Test the validName method with correct input")
    public void testValidNameTrue() {
        Boolean res = auth.validName("test");
        assertTrue(res);
    }

    @Test
    @DisplayName("Test the validName method with incorrect input")
    public void testValidNameFalse() {
        Boolean res = auth.validName("test1223");
        assertFalse(res);
    }

    // Password Regex

    @Test
    @DisplayName("Test the validPassword method with correct input")
    public void testValidPasswordTrue() {
        Boolean res = auth.validPassword("FxBhU98K25D4?");
        assertTrue(res);
    }

    @Test
    @DisplayName("Test the validPassword method with incorrect input")
    public void testValidPasswordFalse() {
        Boolean res = auth.validPassword("1234");
        assertFalse(res);
    }

    @Test
    @DisplayName("Test the equalPassword method when two passwords are equals")
    public void testEqualPasswordTrue() {
        Boolean res = auth.equalPassword("test123?", "test123?");
        assertTrue(res);
    }

    @Test
    @DisplayName("Test the equalPassword method when two passwords are not equals")
    public void testEqualPasswordFalse() {
        Boolean res = auth.equalPassword("test123?", "test456?");
        assertFalse(res);
    }

    @Test
    @DisplayName("Test the checkFormatError with no error")
    public void testcheckFormatErrorFalse() {
        ArrayList<Boolean> fields = new ArrayList<>(Arrays.asList(
                auth.validPersonnummer("110101-0001"),
                auth.validName("test"),
                auth.validEmail("enzotiberghien28@gmail.com"),
                auth.validPassword("FxBhU98k26D4?"),
                auth.equalPassword("FxBhU98k26D4?", "FxBhU98k26D4?")));

        Boolean res = fields.contains(false);
        assertFalse(res);
    }

    @Test
    @DisplayName("Test the checkFormatError with errors")
    public void testcheckFormatErrorTrue() {
        ArrayList<Boolean> fields = new ArrayList<>(Arrays.asList(
                auth.validPersonnummer("110101-0"), // Error
                auth.validName("test"),
                auth.validEmail("enzotiberghien28gmail.com"), // Error
                auth.validPassword("FxBhU98k26D4?"),
                auth.equalPassword("FxBhU98k26D4", "FxBhU98k26D4?"))); // Error

        Boolean res = fields.contains(false);
        assertTrue(res);
    }

    @Test
    @DisplayName("Test the showFormatError method Alert")
    public void testShowFormatError() {
        String personnummer = "010101-0001";
        String name = "test";
        String email = "testuser@gmail.com";
        String password = "alabama";
        String repPassword = "alabama";
        try (MockedStatic<Authentication> msAlert = Mockito.mockStatic(Authentication.class)) {
            msAlert.when(() -> Authentication.showFormatError(personnummer, name, email, password, repPassword)).thenCallRealMethod();
        }
    }

    @Test
    @DisplayName("Test the showFormatError method Alert with invalid input")
    public void testShowFormatErrorFalse() {
        String personnummer = "010101-01"; // Error
        String name = "test123"; // Errir
        String email = "testuser@gmail.com";
        String password = "alabama";
        String repPassword = "alabama";
        try (MockedStatic<Authentication> msAlert = Mockito.mockStatic(Authentication.class)) {
            msAlert.when(() -> Authentication.showFormatError(personnummer, name, email, password, repPassword)).thenCallRealMethod();
        }
    }
}
