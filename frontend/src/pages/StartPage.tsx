import { useNavigate } from "react-router-dom";

export default function StartPage() {
  const nav = useNavigate();

  return (
    <div
      style={{
        minHeight: "100vh",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
      }}
    >
      <div className="card" style={{ width: 420, textAlign: "center" }}>
        <h1 style={{ marginTop: 0 }}>Logistics</h1>
        <p style={{ opacity: 0.75, marginBottom: 24 }}>
          Please choose how you want to sign in
        </p>

        <div style={{ display: "grid", gap: 14 }}>
          <button
            style={{ fontSize: 16 }}
            onClick={() => nav("/login?type=CLIENT")}
          >
            I am a Client
          </button>

          <button
            style={{ fontSize: 16 }}
            onClick={() => nav("/login?type=EMPLOYEE")}
          >
            I am an Employee
          </button>
        </div>
      </div>
    </div>
  );
}
