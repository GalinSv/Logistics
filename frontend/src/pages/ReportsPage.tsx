import { useEffect, useMemo, useState } from "react";
import { Navigate, useNavigate } from "react-router-dom";
import { api } from "../lib/api";
import { getAuth } from "../lib/auth";
import "../styles/reports.css";

import type {
  PackageReportDto,
  EmployeeReportDto,
  ClientReportDto,
} from "../types";

/* ===== lookup DTOs ===== */
type ClientByPhoneDto = {
  exists: boolean;
  clientId: number | null;
  phone: string;
  firstName: string;
  lastName: string;
};

type EmployeeByPhoneDto = {
  exists: boolean;
  employeeId: number | null;
  phone: string;
  firstName: string;
  lastName: string;
};

/* ===== collapsible section ===== */
function Section({
  title,
  children,
}: {
  title: string;
  children: React.ReactNode;
}) {
  const [open, setOpen] = useState(false);

  return (
    <div className="report-section">
      <div className="report-header" onClick={() => setOpen(!open)}>
        <span>{title}</span>
        <span>{open ? "▲" : "▼"}</span>
      </div>

      {open && <div className="report-body">{children}</div>}
    </div>
  );
}
/* ===== unified PACKAGE table ===== */
function PackageTable({ data }: { data: PackageReportDto[] }) {
  return (
    <table className="report-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Sender phone</th>
          <th>Receiver phone</th>
          <th>Employee phone</th>
          <th>Weight (kg)</th>
          <th>Price (€)</th>
          <th>Delivery type</th>
          <th>Address / Office</th>
          <th>Delivered</th>
          <th>Status</th>
        </tr>
      </thead>
      <tbody>
        {data.map((p) => (
          <tr key={p.packageId}>
            <td>{p.packageId}</td>
            <td>{p.senderPhone}</td>
            <td>{p.receiverPhone}</td>
            <td>{p.employeePhone}</td>
            <td>{p.weight}</td>
            <td>{p.price.toFixed(2)}</td> 
            <td>{p.toOffice ? "Office" : "Address"}</td>
            <td>{p.toOffice ? p.officeAddress : p.addressToGo}</td>
            <td>{p.dateOfDelivery ?? "-"}</td>
            <td>{p.status}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}

export default function ReportsPage() {
  const nav = useNavigate();
  const auth = getAuth();

  if (!auth || auth.role !== "EMPLOYEE") {
    return <Navigate to="/" replace />;
  }

  /* ===== data ===== */
  const [packages, setPackages] = useState<PackageReportDto[]>([]);
  const [employees, setEmployees] = useState<EmployeeReportDto[]>([]);
  const [clients, setClients] = useState<ClientReportDto[]>([]);

  const [phone, setPhone] = useState("");
  const [clientLookup, setClientLookup] = useState<ClientByPhoneDto | null>(null);
  const [employeeLookup, setEmployeeLookup] =
    useState<EmployeeByPhoneDto | null>(null);

  const [from, setFrom] = useState("");
  const [to, setTo] = useState("");
  const [revenue, setRevenue] = useState<number | null>(null);

  useEffect(() => {
    api.get("/packages/report").then((r) => setPackages(r.data));
    api.get("/employees/report").then((r) => setEmployees(r.data));
    api.get("/clients/report").then((r) => setClients(r.data));
  }, []);

  /* ===== filters ===== */
  const byEmployee = useMemo(() => {
    if (!employeeLookup?.exists) return [];
    return packages.filter(
      (p) => p.employeePhone === employeeLookup.phone
    );
  }, [packages, employeeLookup]);

  const undelivered = useMemo(
    () => packages.filter((p) => !p.dateOfDelivery),
    [packages]
  );

  const sentByClient = useMemo(() => {
    if (!clientLookup?.exists) return [];
    return packages.filter(
      (p) => p.senderPhone === clientLookup.phone
    );
  }, [packages, clientLookup]);

  const receivedByClient = useMemo(() => {
    if (!clientLookup?.exists) return [];
    return packages.filter(
      (p) =>
        p.receiverPhone === clientLookup.phone && p.dateOfDelivery
    );
  }, [packages, clientLookup]);

  /* ===== lookups ===== */
  const lookupClient = async () => {
    const r = await api.get<ClientByPhoneDto>(
      `/clients/by-phone?phone=${phone}`
    );
    setClientLookup(r.data);
    setEmployeeLookup(null);
  };

  const lookupEmployee = async () => {
    const r = await api.get<EmployeeByPhoneDto>(
      `/employees/by-phone?phone=${phone}`
    );
    setEmployeeLookup(r.data);
    setClientLookup(null);
  };

  const loadRevenue = async () => {
    const r = await api.get<{ totalRevenue: number }>(
      `/packages/revenue?from=${from}&to=${to}`
    );
    setRevenue(r.data.totalRevenue);
  };

  return (
    <div style={{ padding: 32 }}>
      <div style={{ display: "flex", justifyContent: "space-between" }}>
        <h2>Reports</h2>
        <button onClick={() => nav("/employee")}>← Back to Packages</button>
      </div>

      {/* a */}
      <Section title="All employees">
        <table className="report-table">
        <thead>
          <tr>
            <th>First name</th>
            <th>Last name</th>
            <th>Phone</th>
            <th>Email</th>
            <th>Office</th>
          </tr>
        </thead>
        <tbody>
          {employees.map((e) => (
            <tr key={e.employeeId}>
              <td>{e.firstName}</td>
              <td>{e.lastName}</td>
              <td>{e.phone}</td>
              <td>{e.email}</td>
              <td>{e.officeAddress}</td>
            </tr>
          ))}
        </tbody>
      </table>

      </Section>

      {/* b */}
      <Section title="All clients">
        <table className="report-table">
          <thead>
            <tr>
              <th>First name</th>
              <th>Last name</th>
              <th>Phone</th>
              <th>Email</th>
            </tr>
          </thead>
          <tbody>
            {clients.map((c) => (
              <tr key={c.clientId}>
                <td data-label="First name">{c.firstName}</td>
                <td data-label="Last name">{c.lastName}</td>
                <td data-label="Phone">{c.phone}</td>
                <td data-label="Email">{c.email}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </Section>

      {/* c */}
      <Section title="All packages">
        <PackageTable data={packages} />
      </Section>


      {/* e */}
      <Section title="Sent but not delivered packages">
        <PackageTable data={undelivered} />
      </Section>


      <Section title="Search by phone (d / f / g)">
          <div className="search-box">
            <input
              placeholder="Phone number"
              value={phone}
              onChange={(e) => setPhone(e.target.value)}
            />
            <button onClick={lookupEmployee}>Employee</button>
            <button className="secondary" onClick={lookupClient}>
              Client
            </button>
          </div>
        </Section>

        {/* d */}
      {employeeLookup?.exists && (
        <Section title="Packages registered by employee">
          <PackageTable data={byEmployee} />
        </Section>
      )}

      
      {/* f */}
      {clientLookup?.exists && (
        <Section title="Packages sent by client">
          <PackageTable data={sentByClient} />
        </Section>
      )}

      {/* g */}
      {clientLookup?.exists && (
        <Section title="Packages received by client">
          <PackageTable data={receivedByClient} />
        </Section>
      )}

      {/* h */}
      <Section title="Company revenue for period">
        <input type="date" value={from} onChange={(e) => setFrom(e.target.value)} />
        <input type="date" value={to} onChange={(e) => setTo(e.target.value)} />
        <button onClick={loadRevenue}>Calculate</button>
        {revenue != null && <p>Total: {revenue.toFixed(2)} €</p>}
      </Section>
    </div>
  );
}