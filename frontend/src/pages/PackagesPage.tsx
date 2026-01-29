import { useEffect, useMemo, useState } from "react";
import { api } from "../lib/api";
import type { PackageReportDto } from "../types";
import CreatePackagePage from "./CreatePackagePage";

export default function PackagesPage() {
  const [items, setItems] = useState<PackageReportDto[]>([]);
  const [query, setQuery] = useState("");
  const [showCreate, setShowCreate] = useState(false);
  const [loading, setLoading] = useState(false);

  const loadPackages = async () => {
    setLoading(true);
    try {
      const res = await api.get<PackageReportDto[]>("/packages/report");
      setItems(res.data);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadPackages();
  }, []);

  const filtered = useMemo(() => {
    const q = query.toLowerCase().trim();
    if (!q) return items;
    return items.filter((p) =>
      JSON.stringify(p).toLowerCase().includes(q)
    );
  }, [items, query]);

  return (
    <div>
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          marginBottom: 24,
        }}
      >
        <h2>Packages</h2>

        <div style={{ display: "flex", gap: 12 }}>
          <input
            placeholder="Search…"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            style={{
              padding: "10px 14px",
              borderRadius: 12,
              border: "1px solid #ddd",
            }}
          />

          <button onClick={() => (window.location.href = "/employee/reports")}>
            Reports
          </button>

          <button onClick={loadPackages} disabled={loading}>
            {loading ? "Refreshing…" : "Refresh"}
          </button>

          <button
            onClick={() => {
              localStorage.clear();
              window.location.href = "/login";
            }}
          >
            Logout
          </button>
        </div>
      </div>
      <div
        style={{
          display: "grid",
          gridTemplateColumns: showCreate ? "360px 1fr" : "1fr",
          gap: 24,
          alignItems: "start",
        }}
      >

        {showCreate && (
          <div
            style={{
              background: "white",
              borderRadius: 16,
              padding: 16,
              boxShadow: "0 10px 30px rgba(0,0,0,0.08)",
            }}
          >
            <CreatePackagePage
              onCreated={loadPackages}
              onClose={() => setShowCreate(false)}
            />
          </div>
        )}

        <div>
          <div
            style={{
              display: "grid",
              gridTemplateColumns: "repeat(auto-fill, minmax(280px, 1fr))",
              gap: 20,
            }}
          >

            <div
              onClick={() => setShowCreate(true)}
              style={{
                cursor: "pointer",
                background: "white",
                borderRadius: 16,
                padding: 24,
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "center",
                gap: 12,
                color: "#5b6cff",
                fontWeight: 600,
                boxShadow: "0 10px 30px rgba(0,0,0,0.08)",
              }}
            >
              <div style={{ fontSize: 32 }}>＋</div>
              <div>New Package</div>
            </div>

            {[...filtered]
              .sort((a, b) => b.packageId - a.packageId)
              .map((p) => (
                <div
                  key={p.packageId}
                  style={{
                    background: "white",
                    borderRadius: 16,
                    padding: 16,
                    boxShadow: "0 10px 30px rgba(0,0,0,0.08)",
                  }}
                >
                  <div
                    style={{
                      display: "flex",
                      justifyContent: "space-between",
                      marginBottom: 8,
                    }}
                  >
                    <strong>Package #{p.packageId}</strong>
                    <span
                      style={{
                        padding: "4px 10px",
                        borderRadius: 999,
                        fontSize: 12,
                        background:
                          p.status === "DELIVERED"
                            ? "#e8f7ee"
                            : "#fff3e0",
                        color:
                          p.status === "DELIVERED"
                            ? "#1b8f4c"
                            : "#c77700",
                      }}
                    >
                      {p.status}
                    </span>
                  </div>

                  <div style={{ opacity: 0.7, marginBottom: 8 }}>
                    {p.toOffice
                      ? `To office: ${p.officeAddress}`
                      : `Address: ${p.addressToGo}`}
                  </div>

                  <div style={{ fontSize: 14, display: "grid", gap: 4 }}>
                    <div>Weight: {p.weight} kg</div>
                    <div>Price: {p.price.toFixed(2)} €</div>

                    <div>Sender: {p.senderPhone}</div>
                    <div>Receiver: {p.receiverPhone}</div>

                    <div>Employee phone: {p.employeePhone}</div>
                    <div>Delivered: {p.dateOfDelivery ?? "-"}</div>
                  </div>
                </div>
              ))}
          </div>
        </div>
      </div>
    </div>
  );
}