import { useState } from "react";
import { useNavigate } from "react-router-dom";
import PackagesPage from "./PackagesPage";
import EmployeesPage from "./EmployeesPage";
import OfficesPage from "./OfficesPage";
import { getAuth } from "../lib/auth";

type View = "packages" | "employees" | "offices";

export default function EmployeeDashboardPage() {
  const nav = useNavigate();
  const auth = getAuth();

  if (!auth || (auth.role !== "EMPLOYEE" && auth.role !== "ADMIN")) {
    nav("/");
    return null;
  }

  const [view, setView] = useState<View>("packages");
  const isAdmin = auth.role === "ADMIN";

  return (
    <div className="employee-layout">
      {/* SIDEBAR */}
      <aside className="employee-sidebar">
        <button
          className={`sidebar-btn ${view === "packages" ? "active" : ""}`}
          onClick={() => setView("packages")}
        >
          📦
        </button>

        {isAdmin && (
          <button
            className={`sidebar-btn ${view === "employees" ? "active" : ""}`}
            onClick={() => setView("employees")}
          >
            👤
          </button>
        )}

        {isAdmin && (
          <button
            className={`sidebar-btn ${view === "offices" ? "active" : ""}`}
            onClick={() => setView("offices")}
          >
            🏢
          </button>
        )}
      </aside>

      {/* CONTENT */}
      <main className="employee-content">
        {view === "packages" && <PackagesPage />}

        {view === "employees" && isAdmin && (
          <div className="center-page">
            <EmployeesPage />
          </div>
        )}

        {view === "offices" && isAdmin && (
          <div className="center-page">
            <OfficesPage />
          </div>
        )}
      </main>
    </div>
  );
}
