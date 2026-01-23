import { useState } from "react";
import { api } from "../lib/api";
import { getAuth } from "../lib/auth";
import type { PackageDto } from "../types";

type CreateForm = {
  senderClientId: number;
  receiverClientId: number;
  weight: number;
  toOffice: boolean;
  addressToGo: string | null;
};

export default function CreatePackagePage() {
  const [form, setForm] = useState<CreateForm>({
    senderClientId: 1,
    receiverClientId: 2,
    weight: 1,
    toOffice: false,
    addressToGo: "",
  });

  const [error, setError] = useState("");
  const [created, setCreated] = useState<PackageDto | null>(null);

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
      const payload = {
        idSenderClient: form.senderClientId,
        idReceiverClient: form.receiverClientId,
        idEmployee: auth.employeeId,
        weight: form.weight,
        toOffice: form.toOffice,
        addressToGo: form.toOffice ? null : form.addressToGo,
      };

      console.log("CREATE PACKAGE PAYLOAD:", payload);

      const res = await api.post<PackageDto>("/packages", payload);
      setCreated(res.data);
    } catch (err: any) {
      setError(err?.response?.data?.message || err?.message || "Create failed");
    }
  };

  return (
    <div style={{ marginTop: 20 }}>
      <h2>Create Package</h2>

      {error && <p style={{ color: "crimson" }}>{error}</p>}

      <form onSubmit={submit} style={{ display: "grid", gap: 12, maxWidth: 420 }}>
        <label>
          Sender client ID
          <input
            type="number"
            value={form.senderClientId}
            onChange={(e) =>
              setForm((f) => ({ ...f, senderClientId: Number(e.target.value) }))
            }
          />
        </label>

        <label>
          Receiver client ID
          <input
            type="number"
            value={form.receiverClientId}
            onChange={(e) =>
              setForm((f) => ({ ...f, receiverClientId: Number(e.target.value) }))
            }
          />
        </label>

        <label>
          Weight (kg)
          <input
            type="number"
            step="0.1"
            min="0"
            value={form.weight}
            onChange={(e) =>
              setForm((f) => ({ ...f, weight: Number(e.target.value) }))
            }
          />
        </label>

        <label>
          <input
            type="checkbox"
            checked={form.toOffice}
            onChange={(e) =>
              setForm((f) => ({ ...f, toOffice: e.target.checked }))
            }
          />{" "}
          Deliver to office
        </label>

        {!form.toOffice && (
          <label>
            Address to go
            <input
              value={form.addressToGo ?? ""}
              onChange={(e) =>
                setForm((f) => ({ ...f, addressToGo: e.target.value }))
              }
            />
          </label>
        )}

        <button type="submit">Create</button>
      </form>

      {created && (
        <p style={{ color: "lightgreen", marginTop: 12 }}>
          Created package #{created.idPackage}
        </p>
      )}
    </div>
  );
}
