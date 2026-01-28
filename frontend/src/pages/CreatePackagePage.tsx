import { useEffect, useState } from "react";
import { api } from "../lib/api";
import { getAuth } from "../lib/auth";
import type { PackageDto } from "../types";

type ClientByPhoneDto = {
  exists: boolean;
  clientId: number | null;
  firstName: string | null;
  lastName: string | null;
};

type OfficeDto = {
  idOffice: number;
  address: string;
};

type Props = {
  onCreated: () => void;
  onClose: () => void;
};

export default function CreatePackagePage({ onCreated, onClose }: Props) {
  const [weight, setWeight] = useState(1);
  const [toOffice, setToOffice] = useState(false);
  const [addressToGo, setAddressToGo] = useState("");
  const [officeId, setOfficeId] = useState<number | null>(null);

  const [senderPhone, setSenderPhone] = useState("");
  const [senderFirst, setSenderFirst] = useState("");
  const [senderLast, setSenderLast] = useState("");
  const [senderExists, setSenderExists] = useState(false);
  const [senderResolved, setSenderResolved] = useState(false);

  const [receiverPhone, setReceiverPhone] = useState("");
  const [receiverFirst, setReceiverFirst] = useState("");
  const [receiverLast, setReceiverLast] = useState("");
  const [receiverExists, setReceiverExists] = useState(false);
  const [receiverResolved, setReceiverResolved] = useState(false);

  const [offices, setOffices] = useState<OfficeDto[]>([]);
  const [error, setError] = useState("");
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    if (toOffice) {
      api
        .get<OfficeDto[]>("/offices/by-company?companyId=6")
        .then((r) => setOffices(r.data));
    }
  }, [toOffice]);

  const lookup = async (
    phone: string,
    setFirst: (v: string) => void,
    setLast: (v: string) => void,
    setExists: (v: boolean) => void,
    setResolved: (v: boolean) => void
  ) => {
    if (!phone) return;

    const r = await api.get<ClientByPhoneDto>(
      `/clients/by-phone?phone=${phone}`
    );

    setFirst(r.data.firstName ?? "");
    setLast(r.data.lastName ?? "");
    setExists(r.data.exists);
    setResolved(true);
  };


  useEffect(() => {
    if (!form.toOffice) {
      setOffices([]);
      setForm((f) => ({ ...f, officeId: undefined }));
      return;
    }

    const loadOffices = async () => {
      try {
        setLoadingOffices(true);
        const res = await api.get<OfficeDto[]>("/offices");
        setOffices(res.data);
      } catch {
        setError("Failed to load offices");
      } finally {
        setLoadingOffices(false);
      }
    };

    loadOffices();
  }, [form.toOffice]);

  const submit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (submitting) return;

    setSubmitting(true);
    setError("");

    if (form.toOffice && !form.officeId) {
      setError("Please select an office");
      return;
    }

    try {
      const auth = getAuth();
      if (!auth?.employeeId) {
        setError("Not logged in");
        return;
      }

      await api.post<PackageDto>("/packages", {
        senderPhone,
        senderFirstName: senderExists ? null : senderFirst,
        senderLastName: senderExists ? null : senderLast,
        receiverPhone,
        receiverFirstName: receiverExists ? null : receiverFirst,
        receiverLastName: receiverExists ? null : receiverLast,
        idEmployee: auth.employeeId,
        weight,
        toOffice,
        addressToGo: toOffice ? null : addressToGo,
        officeId: toOffice ? officeId : null,
      });

      onCreated();
      onClose();
    } catch (e: any) {
      setError(e?.response?.data?.message || "Create failed");
    } finally {
      setSubmitting(false);
    }
  };


  return (
    <form onSubmit={submit} style={{ display: "grid", gap: 12 }}>
      <div style={{ display: "flex", justifyContent: "space-between" }}>
        <h3 style={{ margin: 0 }}>New Package</h3>
        <button type="button" onClick={onClose}>✕</button>
      </div>

      <input
        placeholder="Sender phone"
        value={senderPhone}
        onChange={(e) => {
          setSenderPhone(e.target.value);
          setSenderResolved(false);
        }}
        onBlur={() =>
          lookup(
            senderPhone,
            setSenderFirst,
            setSenderLast,
            setSenderExists,
            setSenderResolved
          )
        }
      />

      <input
        placeholder="Sender first name"
        value={senderFirst}
        readOnly={senderResolved && senderExists}
        onChange={(e) => setSenderFirst(e.target.value)}
      />

      <input
        placeholder="Sender last name"
        value={senderLast}
        readOnly={senderResolved && senderExists}
        onChange={(e) => setSenderLast(e.target.value)}
      />

      <input
        placeholder="Receiver phone"
        value={receiverPhone}
        onChange={(e) => {
          setReceiverPhone(e.target.value);
          setReceiverResolved(false);
        }}
        onBlur={() =>
          lookup(
            receiverPhone,
            setReceiverFirst,
            setReceiverLast,
            setReceiverExists,
            setReceiverResolved
          )
        }
      />

      <input
        placeholder="Receiver first name"
        value={receiverFirst}
        readOnly={receiverResolved && receiverExists}
        onChange={(e) => setReceiverFirst(e.target.value)}
      />

      <input
        placeholder="Receiver last name"
        value={receiverLast}
        readOnly={receiverResolved && receiverExists}
        onChange={(e) => setReceiverLast(e.target.value)}
      />

      <input
        type="number"
        min="0"
        step="0.1"
        value={weight}
        onChange={(e) => setWeight(+e.target.value)}
      />

      <label>
        <input
          type="checkbox"
          checked={toOffice}
          onChange={(e) => setToOffice(e.target.checked)}
        />{" "}
        Deliver to office
      </label>

      {toOffice ? (
        <select
          value={officeId ?? ""}
          onChange={(e) => setOfficeId(+e.target.value)}
        >
          <option value="">Select office</option>
          {offices.map((o) => (
            <option key={o.idOffice} value={o.idOffice}>
              {o.address}
            </option>
          ))}
        </select>
      ) : (
        <input
          placeholder="Address to go"
          value={addressToGo}
          onChange={(e) => setAddressToGo(e.target.value)}
        />
      )}

      <button disabled={submitting}>
        {submitting ? "Creating…" : "Create"}
      </button>

      {error && <p style={{ color: "crimson" }}>{error}</p>}
    </form>
  );
}
