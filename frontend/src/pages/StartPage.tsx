import { useNavigate } from "react-router-dom";

export default function StartPage() {
  const nav = useNavigate();

  return (
    <div style={{ padding: 20, maxWidth: 520 }}>
      <h1>Logistics</h1>
      <p>Please choose how you want to sign in:</p>

      <div style={{ display: "flex", gap: 12 }}>
        <button onClick={() => nav("/login?type=CLIENT")}>I am a Client</button>
        <button onClick={() => nav("/login?type=EMPLOYEE")}>I am an Employee</button>
      </div>
    </div>
  );
}
