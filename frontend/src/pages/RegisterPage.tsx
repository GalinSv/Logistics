import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function RegisterPage() {
    const nav = useNavigate();

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [email, setEmail] = useState("");
    const [phone, setPhone] = useState("");

    const [address, setAddress] = useState("");
    const [err, setErr] = useState<string | null>(null);
    const [ok, setOk] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);

    async function onSubmit(e: React.FormEvent) {
        e.preventDefault();
        setErr(null);
        setOk(null);
        setLoading(true);

        try {
            const res = await fetch("http://localhost:8080/api/register/client", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    username,
                    password,
                    firstName,
                    lastName,
                    email,
                    phone,
                    address,
                }),
            });

            if (!res.ok) {
                const txt = await res.text();
                throw new Error(txt || "Register failed");
            }

            setOk("Registration successful. Please login.");
            setTimeout(() => nav("/login?type=client"), 800);
        } catch (e: any) {
            setErr(e?.message ?? "Register error");
        } finally {
            setLoading(false);
        }
    }

    return (
        <div style={{ padding: 20, maxWidth: 520 }}>
            <h2>Register (Client)</h2>

            <form onSubmit={onSubmit} style={{ display: "grid", gap: 10 }}>
                <input placeholder="username" value={username} onChange={(e) => setUsername(e.target.value)} />
                <input placeholder="password" type="password" value={password} onChange={(e) => setPassword(e.target.value)} />

                <input placeholder="first name" value={firstName} onChange={(e) => setFirstName(e.target.value)} />
                <input placeholder="last name" value={lastName} onChange={(e) => setLastName(e.target.value)} />

                <input placeholder="email" value={email} onChange={(e) => setEmail(e.target.value)} />
                <input placeholder="phone" value={phone} onChange={(e) => setPhone(e.target.value)} />
                <input placeholder="address" value={address} onChange={(e) => setAddress(e.target.value)} />

                <button disabled={loading}>{loading ? "Registering..." : "Register"}</button>

                {err && <div style={{ color: "crimson" }}>{err}</div>}
                {ok && <div style={{ color: "green" }}>{ok}</div>}
            </form>
        </div>
    );
}
