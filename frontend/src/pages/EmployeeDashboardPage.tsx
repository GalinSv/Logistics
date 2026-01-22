import { useNavigate } from "react-router-dom";
import { clearAuth, getAuth } from "../lib/auth";
import PackagesPage from "./PackagesPage";
import CreatePackagePage from "./CreatePackagePage";

export default function EmployeeDashboardPage() {
  const nav = useNavigate();
  const auth = getAuth();

  if (!auth || (auth.role !== "EMPLOYEE" && auth.role !== "ADMIN")) {
    nav("/");
    return null;
  }

  return (
    <div style={{ padding: 20 }}>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
        <h2>Employee Dashboard ({auth.fullName})</h2>
        <button
          onClick={() => {
            clearAuth();
            nav("/");
          }}
        >
          Logout
        </button>
      </div>

      <CreatePackagePage />
      <hr style={{ margin: "24px 0", opacity: 0.3 }} />
      <PackagesPage />
    </div>
  );
}
