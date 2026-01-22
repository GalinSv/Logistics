import { useEffect, useMemo, useState } from "react";
import { api } from "../lib/api";
import { getAuth } from "../lib/auth";
import type { ClientDto, PricingDto, PackageDto } from "../types";

type OfficeDto = {
  id: number;
  address: string | null;
  companyId: number | null;
  companyName: string | null;
};

type PackageCreateForm = {
  weight: number;
  toOffice: boolean;
  addressToGo: string | null;
  officeId: number | null;
  pricingId: number;
};

type PartyState = {
  phone: string;
  firstName: string;
  lastName: string;
  clientId: number | null;
  loading: boolean;
  notFound: boolean;
};

const emptyParty: PartyState = {
  phone: "",
  firstName: "",
  lastName: "",
  clientId: null,
  loading: false,
  notFound: false,
};

function normalizePhone(raw: string) {
  return (raw ?? "").replace(/'/g, "").trim();
}

export default function CreatePackagePage() {
  const [pricing, setPricing] = useState<PricingDto[]>([]);
  const [offices, setOffices] = useState<OfficeDto[]>([]);

  const [sender, setSender] = useState<PartyState>(emptyParty);
  const [receiver, setReceiver] = useState<PartyState>(emptyParty);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [created, setCreated] = useState<PackageDto | null>(null);

  const [form, setForm] = useState<PackageCreateForm>({
    weight: 1,
    toOffice: true,
    addressToGo: null,
    officeId: null,
    pricingId: 0,
  });

  useEffect(() => {
    Promise.all([api.get("/pricing"), api.get("/offices")]).then(([p, o]) => {
      setPricing(p.data);
      setOffices(o.data);

      setForm((f) => ({
        ...f,
        pricingId: p.data[0]?.id ?? 0,
        officeId: o.data[0]?.id ?? null,
      }));

      setLoading(false);
    });
  }, []);

  const selectedPricing = useMemo(
    () => pricing.find((x) => x.id === form.pricingId) ?? null,
    [pricing, form.pricingId]
  );

  const computedPrice = useMemo(() => {
    if (!selectedPricing) return null;
    const base = form.weight * selectedPricing.pricePerKilogram;
    const extra = form.toOffice ? 0 : 1;
    return base + extra;
  }, [form.weight, form.toOffice, selectedPricing]);

  async function lookupClient(
    phoneRaw: string,
    setParty: React.Dispatch<React.SetStateAction<PartyState>>
  ) {
    const phone = normalizePhone(phoneRaw);
    if (!phone) return;

    setParty((p) => ({ ...p, loading: true, notFound: false, phone }));

    try {
      const res = await api.get<ClientDto>("/clients/lookup", { params: { phone } });

      setParty({
        phone,
        firstName: res.data.firstName ?? "",
        lastName: res.data.lastName ?? "",
        clientId: res.data.id,
        loading: false,
        notFound: false,
      });
    } catch {
      setParty((p) => ({
        ...p,
        phone,
        loading: false,
        notFound: true,
        clientId: null,
        firstName: "",
        lastName: "",
      }));
    }
  }

  async function ensureClient(party: PartyState): Promise<number> {
    if (party.clientId) return party.clientId;

    const phone = normalizePhone(party.phone);
    if (!phone) throw new Error("Phone is required");

    if (!party.firstName.trim() || !party.lastName.trim()) {
      throw new Error("First name and last name are required for new client");
    }

    const res = await api.post<ClientDto>("/clients", {
      phone,
      firstName: party.firstName.trim(),
      lastName: party.lastName.trim(),
    });

    return res.data.id;
  }

  const submit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    setCreated(null);

    const auth = getAuth();
    if (!auth?.employeeId) {
      setError("Not logged in as employee");
      return;
    }

    try {
      if (!form.pricingId) {
        setError("Pricing is required.");
        return;
      }

      if (form.toOffice) {
        if (!form.officeId) {
          setError("Office is required when To Office is selected.");
          return;
        }
      } else {
        if (!form.addressToGo?.trim()) {
          setError("Address is required when To Address is selected.");
          return;
        }
      }

      const senderId = await ensureClient(sender);
      const receiverId = await ensureClient(receiver);

      const payload = {
        weight: form.weight,
        toOffice: form.toOffice,
        deliveryAddress: form.toOffice ? null : form.addressToGo,
        officeId: form.toOffice ? form.officeId : null,
        senderClientId: senderId,
        receiverClientId: receiverId,
        pricingId: form.pricingId,
        employeeId: auth.employeeId,
      };

      const res = await api.post<PackageDto>("/packages", payload);
      setCreated(res.data);
    } catch (err: any) {
      setError(err?.response?.data?.message || err?.message || "Create failed");
    }
  };

  return (
    <div style={{ marginTop: 18 }}>
      <h2 style={{ marginBottom: 8 }}>Create Package</h2>

      {loading && <p>Loading…</p>}
      {error && <p style={{ color: "crimson" }}>{error}</p>}

      <form onSubmit={submit} style={{ display: "grid", gap: 12, maxWidth: 560 }}>
        {/* ✅ 1) Sender */}
        <div style={{ border: "1px solid #333", padding: 12, borderRadius: 8 }}>
          <strong>Sender</strong>

          <label style={{ display: "grid", gap: 6, marginTop: 10 }}>
            Phone
            <input
              value={sender.phone}
              onChange={(e) => setSender((p) => ({ ...p, phone: e.target.value }))}
              onBlur={() => lookupClient(sender.phone, setSender)}
              placeholder="089..."
            />
          </label>

          {sender.loading && <small>Looking up…</small>}

          {(sender.clientId || sender.notFound) && (
            <div style={{ display: "grid", gap: 10, marginTop: 10 }}>
              <label>
                First name
                <input
                  value={sender.firstName}
                  disabled={!!sender.clientId}
                  style={{ opacity: sender.clientId ? 0.6 : 1 }}
                  onChange={(e) => setSender((p) => ({ ...p, firstName: e.target.value }))}
                />
              </label>

              <label>
                Last name
                <input
                  value={sender.lastName}
                  disabled={!!sender.clientId}
                  style={{ opacity: sender.clientId ? 0.6 : 1 }}
                  onChange={(e) => setSender((p) => ({ ...p, lastName: e.target.value }))}
                />
              </label>
            </div>
          )}
        </div>

        {/* ✅ 2) Receiver */}
        <div style={{ border: "1px solid #333", padding: 12, borderRadius: 8 }}>
          <strong>Receiver</strong>

          <label style={{ display: "grid", gap: 6, marginTop: 10 }}>
            Phone
            <input
              value={receiver.phone}
              onChange={(e) => setReceiver((p) => ({ ...p, phone: e.target.value }))}
              onBlur={() => lookupClient(receiver.phone, setReceiver)}
              placeholder="089..."
            />
          </label>

          {receiver.loading && <small>Looking up…</small>}

          {(receiver.clientId || receiver.notFound) && (
            <div style={{ display: "grid", gap: 10, marginTop: 10 }}>
              <label>
                First name
                <input
                  value={receiver.firstName}
                  disabled={!!receiver.clientId}
                  style={{ opacity: receiver.clientId ? 0.6 : 1 }}
                  onChange={(e) => setReceiver((p) => ({ ...p, firstName: e.target.value }))}
                />
              </label>

              <label>
                Last name
                <input
                  value={receiver.lastName}
                  disabled={!!receiver.clientId}
                  style={{ opacity: receiver.clientId ? 0.6 : 1 }}
                  onChange={(e) => setReceiver((p) => ({ ...p, lastName: e.target.value }))}
                />
              </label>
            </div>
          )}
        </div>

        {/* ✅ 3) Delivery */}
        <div style={{ border: "1px solid #333", padding: 12, borderRadius: 8 }}>
          <strong>Delivery</strong>

          <label style={{ display: "flex", gap: 10, alignItems: "center", marginTop: 10 }}>
            <input
              type="radio"
              checked={form.toOffice}
              onChange={() =>
                setForm((f) => ({
                  ...f,
                  toOffice: true,
                  addressToGo: null,
                }))
              }
            />
            To Office
          </label>

          <label style={{ display: "flex", gap: 10, alignItems: "center" }}>
            <input
              type="radio"
              checked={!form.toOffice}
              onChange={() =>
                setForm((f) => ({
                  ...f,
                  toOffice: false,
                  officeId: null,
                }))
              }
            />
            To Address
          </label>

          {form.toOffice ? (
            <label style={{ display: "grid", gap: 6, marginTop: 10 }}>
              Office
              <select
                value={form.officeId ?? ""}
                onChange={(e) => setForm((f) => ({ ...f, officeId: Number(e.target.value) }))}
              >
                {offices.map((o) => (
                  <option key={o.id} value={o.id}>
                    #{o.id} — {o.address ?? "-"} {o.companyName ? `(${o.companyName})` : ""}
                  </option>
                ))}
              </select>
            </label>
          ) : (
            <label style={{ display: "grid", gap: 6, marginTop: 10 }}>
              Address to go
              <input
                value={form.addressToGo ?? ""}
                onChange={(e) => setForm((f) => ({ ...f, addressToGo: e.target.value }))}
                placeholder="Sofia, blvd..."
              />
            </label>
          )}
        </div>

        {/* ✅ Group: Weight + Pricing */}
                <div style={{ border: "1px solid #333", padding: 12, borderRadius: 8 }}>
                  <strong>Details</strong>

                  <div style={{ display: "grid", gap: 10, marginTop: 10 }}>
                    <label>
                      Weight (kg)
                      <input
                        type="number"
                        step="0.1"
                        min="0"
                        value={form.weight}
                        onChange={(e) => setForm((f) => ({ ...f, weight: Number(e.target.value) }))}
                      />
                    </label>

                    <label>
                      Pricing
                      <select
                        value={form.pricingId}
                        onChange={(e) => setForm((f) => ({ ...f, pricingId: Number(e.target.value) }))}
                      >
                        {pricing.map((p) => (
                          <option key={p.id} value={p.id}>
                            #{p.id} — {p.pricePerKilogram}/kg {p.toOffice ? "" : " + to address"}
                          </option>
                        ))}
                      </select>
                    </label>

                    <div style={{ opacity: 0.85 }}>
                      Estimated price:{" "}
                      <strong>{computedPrice === null ? "-" : computedPrice.toFixed(2)}</strong>
                    </div>
                  </div>
                </div>

        <button type="submit" style={{ padding: "10px 12px", cursor: "pointer" }}>
          Create
        </button>
      </form>

      {created && (
        <p style={{ color: "lightgreen", marginTop: 12 }}>
          Created package #{created.id}
        </p>
      )}
    </div>
  );


}
