import { useEffect, useMemo, useState } from "react";
import { getAuth, clearAuth } from "../lib/auth";
import { Navigate, useNavigate } from "react-router-dom";

export default function ClientDashboardPage() {
  const nav = useNavigate();

  const auth = useMemo(() => getAuth(), []); // ✅ взимаме го 1 път
  if (!auth || auth.role !== "CLIENT" || !auth.clientId) {
    return <Navigate to="/" replace />;
  }

  const clientId = auth.clientId;

  const [items, setItems] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState<string | null>(null);

  useEffect(() => {
    let cancelled = false;

    (async () => {
      try {
        setLoading(true);
        setErr(null);

        const res = await fetch(`http://localhost:8080/packages/client/${clientId}/all`);
        if (!res.ok) throw new Error(await res.text());
        const data = await res.json();

        if (!cancelled) setItems(data);
      } catch (e: any) {
        if (!cancelled) setErr(e?.message ?? "Error loading packages");
      } finally {
        if (!cancelled) setLoading(false);
      }
    })();

    return () => {
      cancelled = true;
    };
  }, [clientId]);

  return (
    <div style={{ padding: 20 }}>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
       <h2>My Packages (Client: {auth.fullName})</h2>
        <button
          onClick={() => {
            clearAuth();
            nav("/");
          }}
        >
          Logout
        </button>
      </div>

      {loading && <p>Loading...</p>}
      {err && <p style={{ color: "crimson" }}>{err}</p>}

      {!loading && !err && (
        <table cellPadding={8} style={{ width: "100%", borderCollapse: "collapse" }}>
          <thead>
            <tr style={{ textAlign: "left", borderBottom: "1px solid #ddd" }}>
              <th>ID</th>
              <th>Status</th>
              <th>Weight</th>
              <th>From</th>
              <th>To</th>
              <th>To Office</th>
              <th>Address</th>
              <th>Delivery Date</th>
            </tr>
          </thead>
          <tbody>
            {items.map((p) => (
              <tr key={p.id} style={{ borderBottom: "1px solid #eee" }}>
                <td>{p.id}</td>
                <td>{p.status}</td>
                <td>{p.weight}</td>
                <td>{p.senderFullName ?? "-"}</td>
                <td>{p.receiverFullName ?? "-"}</td>
                <td>{p.toOffice ? "Yes" : "No"}</td>
                <td>{p.toOffice ? (p.officeAddress ?? "-") : (p.addressToGo ?? "-")}</td>
                <td>{p.dateOfDelivery ?? "-"}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
