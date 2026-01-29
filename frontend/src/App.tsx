import "./styles/ui.css";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import StartPage from "./pages/StartPage";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import ClientDashboardPage from "./pages/ClientDashboardPage";

import EmployeeDashboardPage from "./pages/EmployeeDashboardPage";
import EmployeesPage from "./pages/EmployeesPage";
import OfficesPage from "./pages/OfficesPage";
import ReportsPage from "./pages/ReportsPage";

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<StartPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />

        <Route path="/client" element={<ClientDashboardPage />} />

        {/* EMPLOYEE AREA */}
        <Route path="/employee" element={<EmployeeDashboardPage />} />
        <Route path="/employee/employees" element={<EmployeesPage />} />
        <Route path="/employee/offices" element={<OfficesPage />} />
        <Route path="/employee/reports" element={<ReportsPage />} />

        <Route path="*" element={<div>No route matched</div>} />
      </Routes>
    </BrowserRouter>
  );
}
