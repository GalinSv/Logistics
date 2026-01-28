import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { getAuth } from "../lib/auth";

export default function AdminRegisterEmployeePage() {
    const nav = useNavigate();
    const auth = getAuth();

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [email, setEmail] = useState("");

    // typeOfEmpl: 1 = EMPL, 0 = ADMIN
    const [typeOfEmpl, setTypeOfEmpl] = useState<number>(1);

    const [err, setErr] = useState<string | null>(null);
    const [ok, setOk] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);

    async function onSubmit(e: React.FormEvent) {
        e.preventDefault();
        setErr(null);
        setOk(null);

        if (!auth?.userId || auth.role !== "ADMIN") {
            setErr("Only ADMIN can register employees.");
            return;
        }

        setLoading(true);
        try {
            const res = await fetch("http://localhost:8080/api/register/employee", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    adminUserId: auth.userId,
                    username,
                    password,
                    firstName,
                    lastName,
                    email,
                    typeOfEmpl,
                }),
            });

            if (!res.ok) {
                const txt = await res.text();
                throw new Error(txt || "Register employee failed");
            }

            setOk("Employee registered successfully.");
            setTimeout(() => nav("/employee"), 800);
        } catch (e: any) {
            setErr(e?.message ?? "Error");
        } finally {
            setLoading(false);
        }
    }

    return (
        <div style={{ padding: 20, maxWidth: 520 }}>
            <h2>Register Employee (Admin)</h2>

            <form onSubmit={onSubmit} style={{ display: "grid", gap: 10 }}>
                <input placeholder="username" value={username} onChange={(e) => setUsername(e.target.value)} />
                <input placeholder="password" type="password" value={password} onChange={(e) => setPassword(e.target.value)} />

                <input placeholder="first name" value={firstName} onChange={(e) => setFirstName(e.target.value)} />
                <input placeholder="last name" value={lastName} onChange={(e) => setLastName(e.target.value)} />
                <input placeholder="email" value={email} onChange={(e) => setEmail(e.target.value)} />

                <select value={typeOfEmpl} onChange={(e) => setTypeOfEmpl(Number(e.target.value))}>
                    <option value={1}>Employee</option>
                    <option value={0}>Admin</option>
                </select>

                <button disabled={loading}>{loading ? "Creating..." : "Create Employee"}</button>

                {err && <div style={{ color: "crimson" }}>{err}</div>}
                {ok && <div style={{ color: "green" }}>{ok}</div>}
            </form>
        </div>
    );
}
