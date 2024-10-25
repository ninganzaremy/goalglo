import {fireEvent, render, screen, waitFor} from "@testing-library/react";
import LoginPage from "./LoginPage";
import {Provider} from "react-redux";
import {BrowserRouter} from "react-router-dom";
import {configureStore} from "@reduxjs/toolkit";
import rootReducer from "../redux/reducers/rootReducer.js";
import {beforeEach, describe, expect, test, vi} from "vitest";
import {loginUser} from "../redux/actions/loginAction";

// Mock the `useNavigate` hook from `react-router-dom` to track navigation during tests
const mockNavigate = vi.fn();
vi.mock("react-router-dom", async () => {
   const actual = await vi.importActual("react-router-dom");
   return {
      ...actual,
      useNavigate: () => mockNavigate, // Replace `useNavigate` with a mock function
      // Mock `Link` component to simplify testing by rendering it as an `<a>` element
      Link: ({ children, to }) => <a href={to}>{children}</a>,
   };
});

// Mock the `useAuthContext` hook to control its return values in tests
const mockUseAuthContext = vi.fn();
vi.mock("../hooks/useAuthContext", () => ({
   useAuthContext: () => mockUseAuthContext(),
}));

// Mock the `loginUser` action to control its behavior in tests
vi.mock("../redux/actions/loginAction", async () => {
   const actual = await vi.importActual("../redux/actions/loginAction");
   return {
      ...actual,
      // Mocked `loginUser` function simulates dispatching a login request
      // and resolves with a test user object
      loginUser: vi.fn(() => (dispatch) => {
         dispatch({ type: "LOGIN_REQUEST" });
         return Promise.resolve({ username: "testuser" });
      }),
   };
});

/**
 * Test suite for the `LoginPage` component
 */
describe("LoginPage Component", () => {
   let store;

   // Initialize a new Redux store and mock return values before each test
   beforeEach(() => {
      store = configureStore({
         reducer: rootReducer,
      });
      mockUseAuthContext.mockReturnValue({
         isAuthenticated: false,
         loading: false,
         error: null,
      });
   });

   // Test that the login form renders correctly
   test("renders login form correctly", () => {
      render(
         <Provider store={store}>
            <BrowserRouter>
               <LoginPage />
            </BrowserRouter>
         </Provider>
      );
      expect(screen.getByText(/Login/i)).toBeInTheDocument();
      expect(screen.getByLabelText(/Username/i)).toBeInTheDocument();
      expect(screen.getByLabelText(/Password/i)).toBeInTheDocument();
      expect(screen.getByRole("button", { name: /Sign In/i })).toBeInTheDocument();
   });

   // Test that the `loginUser` action is dispatched correctly on form submission
   test("dispatches loginUser action on form submit", async () => {
      render(
         <Provider store={store}>
            <BrowserRouter>
               <LoginPage />
            </BrowserRouter>
         </Provider>
      );
      fireEvent.change(screen.getByLabelText(/Username/i), {
         target: { value: "tester" },
      });
      fireEvent.change(screen.getByLabelText(/Password/i), {
         target: { value: "password" },
      });
      fireEvent.click(screen.getByRole("button", { name: /Sign In/i }));
      await waitFor(() => {
         expect(loginUser).toHaveBeenCalledWith({
            username: "tester",
            password: "password",
         });
      });
      await waitFor(() => {
         expect(mockNavigate).toHaveBeenCalledWith("/dashboard");
      });
   });

   // Test that the user is navigated to the dashboard if they are already authenticated
   test("navigates to dashboard if already authenticated", async () => {
      mockUseAuthContext.mockReturnValue({
         isAuthenticated: true,
         loading: false,
         error: null,
      });
      render(
         <Provider store={store}>
            <BrowserRouter>
               <LoginPage />
            </BrowserRouter>
         </Provider>
      );
      await waitFor(() => {
         expect(mockNavigate).toHaveBeenCalledWith("/dashboard");
      });
   });

   // Test that an error message is displayed when authentication fails
   test("displays error message when authentication fails", async () => {
      mockUseAuthContext.mockReturnValue({
         isAuthenticated: false,
         loading: false,
         error: "Invalid credentials",
      });
      render(
         <Provider store={store}>
            <BrowserRouter>
               <LoginPage />
            </BrowserRouter>
         </Provider>
      );
      expect(screen.getByRole("alert")).toHaveTextContent("Invalid credentials");
   });
});