import { useNavigate } from "react-router-dom";

type Props = {
  role: "ADMIN" | "EMPLOYEE";
};

export default function EmployeeSidebar({ role }: Props) {
  const nav = useNavigate();

  return (
    <div className="sidebar">
      <button
        className="sidebar-btn"
        onClick={() => nav("/employee")}
        title="Packages"
      >
        📦
      </button>

      {role === "ADMIN" && (
        <>
          <button
            className="sidebar-btn"
            onClick={() => nav("/employee/employees")}
            title="Employees"
          >
            👥
          </button>

          <button
            className="sidebar-btn"
            onClick={() => nav("/employee/offices")}
            title="Offices"
          >
            🏢
          </button>
        </>
      )}
    </div>
  );
}
