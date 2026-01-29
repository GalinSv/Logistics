import { useMemo, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { setAuth } from "../lib/auth";

type LoginResponse = {
  role: "CLIENT" | "EMPLOYEE" | "ADMIN";
  userId: number;
  clientId: number | null;
  employeeId: number | null;
  username: string;
  firstName: string;
  lastName: string;
};

export default function LoginPage() {
  const nav = useNavigate();
  const [params] = useSearchParams();

  const userType = useMemo(() => {
    const t = (params.get("type") || "").toUpperCase();
    return t === "CLIENT" ? "CLIENT" : "EMPLOYEE";
  }, [params]);

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [err, setErr] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  async function onSubmit(e: React.FormEvent) {
    e.preventDefault();
    setErr(null);
    setLoading(true);

    try {
      const res = await fetch("/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ userType, username, password }),
      });

      if (!res.ok) {
        const txt = await res.text();
        throw new Error(txt || "Login failed");
      }

      const data = (await res.json()) as LoginResponse;

      setAuth({
        username: data.username,
        role: data.role,
        userId: data.userId,
        clientId: data.clientId ?? undefined,
        employeeId: data.employeeId ?? undefined,
        fullName: `${data.firstName} ${data.lastName}`,
      });

      if (data.role === "CLIENT") nav("/client");
      else nav("/employee");
    } catch (e: any) {
      setErr(e?.message ?? "Login error");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div
      style={{
        minHeight: "100vh",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
      }}
    >
      <div className="card" style={{ width: 380 }}>
        {/* BACK */}
        <button
          onClick={() => nav("/")}
          style={{
            marginBottom: 12,
            padding: 0,
            border: "none",
            background: "transparent",
            color: "#5b6cff",
            cursor: "pointer",
            fontWeight: 600,
          }}
        >
          ← Back
        </button>

        <h2 style={{ marginTop: 0, marginBottom: 16 }}>
          Login as {userType === "CLIENT" ? "Client" : "Employee"}
        </h2>

        <form onSubmit={onSubmit} style={{ display: "grid", gap: 14 }}>
          <input
            placeholder="Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />

          <input
            placeholder="Password"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />

          <button disabled={loading}>
            {loading ? "Signing in…" : "Login"}
          </button>

          <button
            type="button"
            onClick={() => nav("/register")}
            style={{
              background: "transparent",
              border: "none",
              color: "#5b6cff",
              cursor: "pointer",
            }}
          >
            Register as client
          </button>

          {err && <div style={{ color: "crimson" }}>{err}</div>}
        </form>
      </div>
    </div>
  );
}
