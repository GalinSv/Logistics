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
  officeAddress?: string | null;
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

        const res = await fetch("/api/packages");
        if (!res.ok) throw new Error("Failed to load packages");

        const data: PackageDto[] = await res.json();

        const mine = data
          .filter(
            (p) =>
              p.idSenderClient === clientId ||
              p.idReceiverClient === clientId
          )
          .sort((a, b) => b.idPackage - a.idPackage);

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

  const status = (p: PackageDto) =>
    p.dateOfDelivery ? "DELIVERED" : "CREATED";

  return (
    <div style={{ padding: 32 }}>
      {/* HEADER */}
      <div className="page-header">
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
        <div style={{ maxWidth: 1200, margin: "0 auto" }}>
          <div className="grid">
            {items.map((p) => {
              const type =
                p.idSenderClient === clientId ? "Sent" : "Received";

              return (
                <div key={p.idPackage} className="card">
                  <div className="card-header">
                    <strong>Package #{p.idPackage}</strong>

                    <span className={`status ${status(p)}`}>
                      {status(p)}
                    </span>
                  </div>

                  {/* 📍 ADDRESS / OFFICE */}
                  <div className="card-address">
                    {p.toOffice
                      ? `To office: ${p.officeAddress ?? "-"}`
                      : `Address: ${p.addressToGo ?? "-"}`}
                  </div>

                  <div className="card-grid">
                    <div><b>Type</b></div>
                    <div
                      style={{
                        color:
                          type === "Sent" ? "#5b6cff" : "#0f9d58",
                        fontWeight: 600,
                      }}
                    >
                      {type}
                    </div>

                    <div><b>Weight</b></div>
                    <div>{p.weight} kg</div>

                    <div><b>Price</b></div>
                    <div>{p.price ?? "-"} лв</div>

                    <div><b>Delivered</b></div>
                    <div>{p.dateOfDelivery ?? "-"}</div>
                  </div>
                </div>
              );
            })}

            {items.length === 0 && (
              <div className="card" style={{ opacity: 0.7 }}>
                No packages found.
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
}
