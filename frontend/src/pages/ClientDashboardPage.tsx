import { useEffect, useMemo, useState } from "react";
import { getAuth, clearAuth } from "../lib/auth";
import { Navigate, useNavigate } from "react-router-dom";

type PackageDto = {
  idPackage: number;
  idSenderClient: number;
  idReceiverClient: number;
  idEmployee: number;
  weight: number;
  addressToGo: string | null;
  toOffice: boolean;
  dateOfDelivery: string | null;
  price: number | null;
};

export default function ClientDashboardPage() {
  const nav = useNavigate();

  const auth = useMemo(() => getAuth(), []);
  if (!auth || auth.role !== "CLIENT" || auth.clientId == null) {
    return <Navigate to="/" replace />;
  }

  // ✅ FIX: clientId винаги number
  const clientId = Number(auth.clientId);

  const [items, setItems] = useState<PackageDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState<string | null>(null);

  useEffect(() => {
    let cancelled = false;

    (async () => {
      try {
        setLoading(true);
        setErr(null);

        // ✅ ползваме Vite proxy → без CORS
        const res = await fetch("/api/packages");
        if (!res.ok) throw new Error("Failed to load packages");

        const data: PackageDto[] = await res.json();

        // ✅ само пратките на този client
        const mine = data.filter(
          (p) =>
            p.idSenderClient === clientId ||
            p.idReceiverClient === clientId
        );

        if (!cancelled) setItems(mine);
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
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
        }}
      >
        <h2>My Packages</h2>
        <button
          onClick={() => {
            clearAuth();
            nav("/");
          }}
        >
          Logout
        </button>
      </div>

      {loading && <p>Loading…</p>}
      {err && <p style={{ color: "crimson" }}>{err}</p>}

      {!loading && !err && (
        <table
          cellPadding={8}
          style={{
            width: "100%",
            borderCollapse: "collapse",
            marginTop: 12,
          }}
        >
          <thead>
            <tr style={{ textAlign: "left", borderBottom: "1px solid #ccc" }}>
              <th>ID</th>
              <th>Type</th>
              <th>Weight</th>
              <th>To Office</th>
              <th>Address</th>
              <th>Delivery Date</th>
              <th>Price</th>
            </tr>
          </thead>
          <tbody>
            {items.map((p) => {
              const type =
                p.idSenderClient === clientId ? "Sent" : "Received";

              return (
                <tr
                  key={p.idPackage}
                  style={{ borderBottom: "1px solid #eee" }}
                >
                  <td>{p.idPackage}</td>
                  <td>{type}</td>
                  <td>{p.weight}</td>
                  <td>{p.toOffice ? "Yes" : "No"}</td>
                  <td>{p.addressToGo ?? "-"}</td>
                  <td>{p.dateOfDelivery ?? "-"}</td>
                  <td>{p.price ?? "-"}</td>
                </tr>
              );
            })}

            {items.length === 0 && (
              <tr>
                <td colSpan={7} style={{ padding: 12, opacity: 0.7 }}>
                  No packages found.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      )}
    </div>
  );
}
