import { useState } from "react";
import { getAuth } from "../lib/auth";

export default function OfficesPage() {
  const auth = getAuth();
  if (!auth || auth.role !== "ADMIN") return null;

  const [address, setAddress] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const submit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!address.trim()) {
      setError("Address is required");
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const res = await fetch("/api/offices", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          address,
          companyId: 6, // DEFAULT
        }),
      });

      if (!res.ok) {
        const data = await res.json();
        throw new Error(data.message || "Failed to create office");
      }

      setAddress("");
      alert("Office created");
    } catch (e: any) {
      setError(e.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="card form-card wide-form">
      <h2>Create Office</h2>

      <form onSubmit={submit} style={{ display: "grid", gap: 10 }}>
        <input
          placeholder="Office address"
          value={address}
          onChange={e => setAddress(e.target.value)}
        />

        <button disabled={loading}>
          {loading ? "Creating..." : "Create Office"}
        </button>

        {error && <p className="error">{error}</p>}
      </form>
    </div>
  );
}
