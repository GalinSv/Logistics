import "./styles/ui.css";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import StartPage from "./pages/StartPage";
import LoginPage from "./pages/LoginPage";
import ClientDashboardPage from "./pages/ClientDashboardPage";
import EmployeeDashboardPage from "./pages/EmployeeDashboardPage";
import ReportsPage from "./pages/ReportsPage";

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<StartPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/client" element={<ClientDashboardPage />} />
        <Route path="/employee" element={<EmployeeDashboardPage />} />
        <Route path="/employee/reports" element={<ReportsPage />} />

        {/* debug fallback */}
        <Route path="*" element={<div style={{ padding: 20 }}>No route matched</div>} />
      </Routes>
    </BrowserRouter>
  );
}
