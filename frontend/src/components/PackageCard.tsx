import type { PackageDto } from "../types";

export function PackageCard({ p }: { p: PackageDto }) {
  const status = p.dateOfDelivery
    ? "DELIVERED"
    : p.toOffice
    ? "IN TRANSIT"
    : "CREATED";

  const badge =
    status === "DELIVERED"
      ? "success"
      : status === "IN TRANSIT"
      ? "warn"
      : "danger";

  return (
    <div className="card card-hover" style={{ display: "grid", gap: 10 }}>
      <div style={{ display: "flex", justifyContent: "space-between" }}>
        <strong>Package #{p.idPackage}</strong>
        <span className={`badge ${badge}`}>{status}</span>
      </div>

      <div style={{ fontSize: 14, opacity: 0.85 }}>
        {p.toOffice ? (
          <>
            <div><b>To office:</b> {p.officeAddress}</div>
          </>
        ) : (
          <>
            <div><b>Address:</b> {p.addressToGo}</div>
          </>
        )}
      </div>

      <div
        style={{
          display: "grid",
          gridTemplateColumns: "1fr 1fr",
          gap: 6,
          fontSize: 14,
        }}
      >
        <div><b>Weight:</b> {p.weight} kg</div>
        <div><b>Price:</b> {p.price?.toFixed(2)} лв</div>
        <div><b>Sender:</b> Client #{p.idSenderClient}</div>
        <div><b>Receiver:</b> Client #{p.idReceiverClient}</div>
        <div><b>Employee:</b> #{p.idEmployee}</div>
        <div><b>Delivered:</b> {p.dateOfDelivery ?? "-"}</div>
      </div>
    </div>
  );
}
