import { useEffect, useMemo, useState } from "react";
import { api } from "../lib/api";
import type { PackageDto } from "../types";

export default function PackagesPage() {
  const [items, setItems] = useState<PackageDto[]>([]);
  const [error, setError] = useState("");
  const [query, setQuery] = useState("");

  const loadPackages = async () => {
    setError("");
    try {
      const res = await api.get<PackageDto[]>("/packages");
      setItems(res.data);
    } catch (e) {
      console.error(e);
      setError("Cannot load packages");
    }
  };

  useEffect(() => {
    loadPackages();
  }, []);

  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase();
    if (!q) return items;

    return items.filter((p) => {
      const hay = [
        p.idPackage,
        p.idSenderClient,
        p.idReceiverClient,
        p.idEmployee,
        p.weight,
        p.addressToGo ?? "",
        p.toOffice ? "yes" : "no",
        p.dateOfDelivery ?? "",
        p.price ?? "",
      ]
        .join(" ")
        .toLowerCase();

      return hay.includes(q);
    });
  }, [items, query]);

  const statusFromDate = (dateOfDelivery: string | null) =>
    dateOfDelivery ? "DELIVERED" : "CREATED";

  const badge = (txt: string) => (
    <span
      style={{
        padding: "4px 8px",
        borderRadius: 999,
        border: "1px solid #444",
        background: "#111",
        fontSize: 12,
        opacity: 0.9,
      }}
    >
      {txt}
    </span>
  );

  return (
    <div>
      <div style={{ display: "flex", alignItems: "baseline", gap: 12 }}>
        <h2 style={{ margin: 0 }}>Packages</h2>
        <span style={{ opacity: 0.7 }}>({filtered.length})</span>
      </div>

      <div style={{ marginTop: 12, display: "flex", gap: 12, alignItems: "center" }}>
        <input
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          placeholder="Search by id / client / employee / address…"
          style={{
            width: 520,
            padding: "10px 12px",
            borderRadius: 10,
            border: "1px solid #444",
            background: "#111",
            color: "white",
            outline: "none",
          }}
        />
        <button
          onClick={loadPackages}
          style={{
            padding: "10px 12px",
            borderRadius: 10,
            border: "1px solid #444",
            background: "#1a1a1a",
            color: "white",
            cursor: "pointer",
          }}
        >
          Refresh
        </button>
      </div>

      {error && (
        <p style={{ color: "crimson", marginTop: 12 }}>
          {error} (check backend is running)
        </p>
      )}

      <div
        style={{
          marginTop: 12,
          border: "1px solid #333",
          borderRadius: 14,
          overflow: "hidden",
        }}
      >
        <table cellPadding={10} style={{ width: "100%", borderCollapse: "collapse" }}>
          <thead style={{ background: "#151515" }}>
            <tr style={{ textAlign: "left" }}>
              <th>ID</th>
              <th>Weight</th>
              <th>Price</th>
              <th>Sender</th>
              <th>Receiver</th>
              <th>Employee</th>
              <th>To Office</th>
              <th>Address</th>
              <th>Delivery Date</th>
              <th>Status</th>
            </tr>
          </thead>

          <tbody>
            {filtered.map((p) => {
              const status = statusFromDate(p.dateOfDelivery);
              return (
                <tr key={p.idPackage} style={{ borderTop: "1px solid #2a2a2a" }}>
                  <td>{p.idPackage}</td>
                  <td>{p.weight} kg</td>
                  <td>{p.price == null ? "-" : p.price.toFixed(2)}</td>
                  <td>Client #{p.idSenderClient}</td>
                  <td>Client #{p.idReceiverClient}</td>
                  <td>Employee #{p.idEmployee}</td>
                  <td>{p.toOffice ? "Yes" : "No"}</td>
                  <td>{p.addressToGo ?? "-"}</td>
                  <td>{p.dateOfDelivery ?? "-"}</td>
                  <td>{badge(status)}</td>
                </tr>
              );
            })}

            {filtered.length === 0 && (
              <tr>
                <td colSpan={10} style={{ padding: 16, opacity: 0.7 }}>
                  No packages found.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
