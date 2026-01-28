import { useEffect, useMemo, useState } from "react";
import { api } from "../lib/api";
import type { PackageDto } from "../types";
import CreatePackagePage from "./CreatePackagePage";

export default function PackagesPage() {
  const [items, setItems] = useState<PackageDto[]>([]);
  const [query, setQuery] = useState("");
  const [showCreate, setShowCreate] = useState(false);
  const [loading, setLoading] = useState(false);

  const loadPackages = async () => {
    setLoading(true);
    try {
      const res = await api.get<PackageDto[]>("/packages");
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

  const status = (p: PackageDto) =>
    p.dateOfDelivery ? "DELIVERED" : "CREATED";

  return (
    <div>
      {/* HEADER */}
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

          {/* REFRESH BUTTON */}
          <button onClick={loadPackages} disabled={loading}>
            {loading ? "Refreshing…" : "Refresh"}
          </button>

          {/* LOGOUT */}
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

      {/* MAIN LAYOUT */}
      <div
        style={{
          display: "grid",
          gridTemplateColumns: showCreate ? "360px 1fr" : "1fr",
          gap: 24,
          alignItems: "start",
        }}
      >
        {/* LEFT – CREATE FORM */}
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

        {/* RIGHT – PACKAGES */}
        <div>
          <div
            style={{
              display: "grid",
              gridTemplateColumns: "repeat(auto-fill, minmax(280px, 1fr))",
              gap: 20,
            }}
          >
            {/* NEW PACKAGE CARD */}
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
              .sort((a, b) => b.idPackage - a.idPackage)
              .map((p) => (
                <div
                  key={p.idPackage}
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
                    <strong>Package #{p.idPackage}</strong>
                    <span
                      style={{
                        padding: "4px 10px",
                        borderRadius: 999,
                        fontSize: 12,
                        background:
                          status(p) === "DELIVERED"
                            ? "#e8f7ee"
                            : "#fff3e0",
                        color:
                          status(p) === "DELIVERED"
                            ? "#1b8f4c"
                            : "#c77700",
                      }}
                    >
                      {status(p)}
                    </span>
                  </div>

                  <div style={{ opacity: 0.7, marginBottom: 8 }}>
                    {p.toOffice
                      ? `To office: ${p.officeAddress}`
                      : `Address: ${p.addressToGo}`}
                  </div>

                  <div style={{ fontSize: 14, display: "grid", gap: 4 }}>
                    <div>Weight: {p.weight} kg</div>
                    <div>Price: {p.price?.toFixed(2)} лв</div>
                    <div>Sender: Client #{p.idSenderClient}</div>
                    <div>Receiver: Client #{p.idReceiverClient}</div>
                    <div>Employee: #{p.idEmployee}</div>
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
