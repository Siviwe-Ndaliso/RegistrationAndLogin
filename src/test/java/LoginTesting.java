/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Student
 */

 import com.mycompany.registrationandlogin.Login;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTesting {

    @Test
    public void testValidUsername() {
        Login user = new Login("Kyle", "Barnes", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(user.checkUserName());
    }

    @Test
    public void testInvalidUsername() {
        Login user = new Login("Kyle", "Barnes", "kyle!!!!!!", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(user.checkUserName());
    }

    @Test
    public void testValidPassword() {
        Login user = new Login("Kyle", "Barnes", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(user.checkPasswordComplexity());
    }

    @Test
    public void testInvalidPassword() {
        Login user = new Login("Kyle", "Barnes", "kyl_1", "password", "+27838968976");
        assertFalse(user.checkPasswordComplexity());
    }

    @Test
    public void testValidCellPhone() {
        Login user = new Login("Kyle", "Barnes", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(user.checkCellPhoneNumber());
    }

    @Test
    public void testInvalidCellPhone() {
        Login user = new Login("Kyle", "Barnes", "kyl_1", "Ch&&sec@ke99!", "08966553");
        assertFalse(user.checkCellPhoneNumber());
    }

    @Test
    public void testSuccessfulLogin() {
        Login user = new Login("Kyle", "Barnes", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(user.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void testFailedLogin() {
        Login user = new Login("Kyle", "Barnes", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(user.loginUser("kyl_1", "wrongpassword"));
    }

    @Test
    public void testValidUsernameEquals() {
        Login user = new Login("Kyle", "Barnes", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals(true, user.checkUserName());
    }

    @Test
    public void testInvalidUsernameEquals() {
        Login user = new Login("Kyle", "Barnes", "kyle!!!!!!", "Ch&&sec@ke99!", "+27838968976");
        assertEquals(false, user.checkUserName());
    }

    @Test
    public void testValidPasswordEquals() {
        Login user = new Login("Kyle", "Barnes", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals(true, user.checkPasswordComplexity());
    }

    @Test
    public void testInvalidPasswordEquals() {
        Login user = new Login("Kyle", "Barnes", "kyl_1", "password", "+27838968976");
        assertEquals(false, user.checkPasswordComplexity());
    }

    @Test
    public void testValidCellPhoneEquals() {
        Login user = new Login("Kyle", "Barnes", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals(true, user.checkCellPhoneNumber());
    }

    @Test
    public void testInvalidCellPhoneEquals() {
        Login user = new Login("Kyle", "Barnes", "kyl_1", "Ch&&sec@ke99!", "08966553");
        assertEquals(false, user.checkCellPhoneNumber());
    }

    @Test
    public void testSuccessfulLoginEquals() {
        Login user = new Login("Kyle", "Barnes", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals(true, user.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void testFailedLoginEquals() {
        Login user = new Login("Kyle", "Barnes", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals(false, user.loginUser("kyl_1", "wrongpassword"));
    }
}   
