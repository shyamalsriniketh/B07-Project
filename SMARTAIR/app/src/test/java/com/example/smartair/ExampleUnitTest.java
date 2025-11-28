package com.example.smartair;

import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {
    @Mock
    LoginView view;

    @Mock
    LoginModel model;

    @Test
    public void testDetermineScreen1() {
        LoginPresenter presenter = new LoginPresenter(model, view);
        when(model.checkLoggedIn()).thenReturn(true);
        presenter.determineScreen();
        verify(view).goToActivity(OnboardingActivity.class);
    }

    @Test
    public void testDetermineScreen2() {
        LoginPresenter presenter = new LoginPresenter(model, view);
        when(model.checkLoggedIn()).thenReturn(false);
        presenter.determineScreen();
        verify(view, never()).goToActivity(OnboardingActivity.class);
    }

    @Test
    public void testHandleRoleSelectionClick() {
        LoginPresenter presenter = new LoginPresenter(model, view);
        presenter.handleRoleSelectionClick();
        verify(view).goToActivity(RoleSelectionActivity.class);
    }

    @Test
    public void testHandleLoginClick1() {
        LoginPresenter presenter = new LoginPresenter(model, view);
        presenter.handleLoginClick("", "");
        verify(view).showMessage("Enter email or username");
    }

    @Test
    public void testHandleLoginClick2() {
        LoginPresenter presenter = new LoginPresenter(model, view);
        presenter.handleLoginClick("email", "");
        verify(view).showMessage("Enter password");
    }

    @Test
    public void testHandleLoginClick3() {
        LoginPresenter presenter = new LoginPresenter(model, view);
        presenter.handleLoginClick("email", "password");
        verify(model).attemptSignIn("email@smartairchildaccount.com", "password", presenter);
    }

    @Test
    public void testHandleLoginClick4() {
        LoginPresenter presenter = new LoginPresenter(model, view);
        presenter.handleLoginClick("email@gmail.com", "password");
        verify(model).attemptSignIn("email@gmail.com", "password", presenter);
    }

    @Test
    public void testHandleLoginResult1() {
        LoginPresenter presenter = new LoginPresenter(model, view);
        presenter.handleLoginResult(true);
        verify(view).showMessage("Login successful!");
        verify(view).goToActivity(OnboardingActivity.class);
    }

    @Test
    public void testHandleLoginResult2() {
        LoginPresenter presenter = new LoginPresenter(model, view);
        presenter.handleLoginResult(false);
        verify(view).showMessage("Authentication failed.");
    }

    @Test
    public void testHandleForgotPasswordClick() {
        LoginPresenter presenter = new LoginPresenter(model, view);
        presenter.handleForgotPasswordClick();
        verify(view).goToActivity(ForgotPassword.class);
    }
}