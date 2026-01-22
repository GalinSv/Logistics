import { useEffect, useMemo, useState } from "react";
import { api } from "../lib/api";
import type { PackageDto } from "../types";

type PackageStatus = "CREATED" | "IN_TRANSIT" | "AT_OFFICE" | "DELIVERED" | "CANCELLED";

export default function PackagesPage() {
  const [items, setItems] = useState<PackageDto[]>([]);
  const [error, setError] = useState("");
  const [query, setQuery] = useState("");
  const [busyId, setBusyId] = useState<number | null>(null);

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

  const setStatus = async (id: number, status: PackageStatus) => {
    setError("");
    setBusyId(id);
    try {
      await api.patch(`/packages/${id}/status`, null, {
        params: { status },
      });
      await loadPackages();
    } catch (e: any) {
      console.error(e);
      const msg =
        e?.response?.data?.message ||
        (typeof e?.response?.data === "string" ? e.response.data : null) ||
        e?.message ||
        "Status update failed";
      setError(msg);
    } finally {
      setBusyId(null);
    }
  };

  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase();
    if (!q) return items;

    return items.filter((p) => {
      const hay = [
        p.id,
        p.senderFullName ?? "",
        p.receiverFullName ?? "",
        p.employeeFullName ?? "",
        p.senderUsername ?? "",
        p.receiverUsername ?? "",
        p.employeeUsername ?? "",
        p.addressToGo ?? "",
        p.officeAddress ?? "",
        p.status ?? "",
      ]
        .join(" ")
        .toLowerCase();

      return hay.includes(q);
    });
  }, [items, query]);

  const statusBadge = (s?: string) => {
    const txt = s ?? "CREATED";
    return (
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
  };

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
          placeholder="Search by id / sender / receiver / employee / status…"
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
              <th>Actions</th>
            </tr>
          </thead>

          <tbody>
            {filtered.map((p) => {
              const price =
                p.weight * p.pricePerKilogram + (p.toOffice ? 0 : 1);
              const s = (p.status ?? (p.dateOfDelivery ? "DELIVERED" : "CREATED")) as PackageStatus;
              const locked = s === "DELIVERED" || s === "CANCELLED";
              const busy = busyId === p.id;

              const address = p.toOffice ? (p.officeAddress ?? "-") : (p.addressToGo ?? "-");

              return (
                <tr key={p.id} style={{ borderTop: "1px solid #2a2a2a" }}>
                  <td>{p.id}</td>
                  <td>{p.weight} kg</td>
                  <td>{price.toFixed(2)}</td>

                  <td>
                    {p.senderFullName ?? p.senderUsername ?? "-"}{" "}
                    <span style={{ opacity: 0.6 }}>#{p.senderId}</span>
                  </td>

                  <td>
                    {p.receiverFullName ?? p.receiverUsername ?? "-"}{" "}
                    <span style={{ opacity: 0.6 }}>#{p.receiverId}</span>
                  </td>

                  <td>
                    {p.employeeFullName ?? p.employeeUsername ?? "-"}{" "}
                    <span style={{ opacity: 0.6 }}>#{p.employeeId}</span>
                  </td>

                  <td>{p.toOffice ? "Yes" : "No"}</td>
                  <td>{address}</td>
                  <td>{p.dateOfDelivery ?? "-"}</td>
                  <td>{statusBadge(s)}</td>

                  <td style={{ whiteSpace: "nowrap" }}>
                    {!locked ? (
                      <>
                        <button
                          disabled={busy}
                          onClick={() => setStatus(p.id, "IN_TRANSIT")}
                          style={{
                            marginRight: 8,
                            padding: "6px 10px",
                            borderRadius: 10,
                            border: "1px solid #444",
                            background: "#1a1a1a",
                            color: "white",
                            cursor: busy ? "not-allowed" : "pointer",
                            opacity: busy ? 0.6 : 1,
                          }}
                        >
                          In transit
                        </button>
                        <button
                          disabled={busy}
                          onClick={() => setStatus(p.id, "DELIVERED")}
                          style={{
                            marginRight: 8,
                            padding: "6px 10px",
                            borderRadius: 10,
                            border: "1px solid #444",
                            background: "#1a1a1a",
                            color: "white",
                            cursor: busy ? "not-allowed" : "pointer",
                            opacity: busy ? 0.6 : 1,
                          }}
                        >
                          Delivered
                        </button>
                        <button
                          disabled={busy}
                          onClick={() => setStatus(p.id, "CANCELLED")}
                          style={{
                            padding: "6px 10px",
                            borderRadius: 10,
                            border: "1px solid #444",
                            background: "#1a1a1a",
                            color: "white",
                            cursor: busy ? "not-allowed" : "pointer",
                            opacity: busy ? 0.6 : 1,
                          }}
                        >
                          Cancel
                        </button>
                      </>
                    ) : (
                      <span style={{ opacity: 0.6 }}>—</span>
                    )}
                  </td>
                </tr>
              );
            })}

            {filtered.length === 0 && (
              <tr>
                <td colSpan={11} style={{ padding: 16, opacity: 0.7 }}>
                  No packages found.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      <p style={{ marginTop: 10, opacity: 0.7 }}>
        *Price is calculated as: weight × pricePerKilogram + (toOffice ? 0 : 1)
      </p>
    </div>
  );
}
