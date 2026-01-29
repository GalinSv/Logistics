import { useState } from "react";
import { getAuth } from "../lib/auth";

type Field =
  | "username"
  | "firstName"
  | "lastName"
  | "email"
  | "phone"
  | "password"
  | "confirm"
  | "dob"
  | "typeOfEmpl";

type Errors = Partial<Record<Field, string>>;

export default function EmployeesPage() {
  const auth = getAuth();
  if (!auth || auth.role !== "ADMIN") return null;

  const [form, setForm] = useState({
    username: "",
    firstName: "",
    lastName: "",
    email: "",
    phone: "",
    password: "",
    confirm: "",
    dob: "",
    typeOfEmpl: "1", // 0 = ADMIN, 1 = EMPLOYEE
  });

  const [errors, setErrors] = useState<Errors>({});
  const [touched, setTouched] = useState<Partial<Record<Field, boolean>>>({});
  const [loading, setLoading] = useState(false);
  const [serverError, setServerError] = useState<string | null>(null);

  /* ---------- validation ---------- */
  const validateField = (field: Field, value = form[field]) => {
    switch (field) {
      case "username":
        if (!value.trim()) return "Username is required";
        break;
      case "firstName":
        if (!value.trim()) return "First name is required";
        break;
      case "lastName":
        if (!value.trim()) return "Last name is required";
        break;
      case "email":
        if (!value.trim()) return "Email is required";
        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value))
          return "Invalid email format";
        break;
      case "phone":
        if (!value.trim()) return "Phone is required";
        break;
      case "password":
        if (!value) return "Password is required";
        if (value.length < 6) return "Password must be at least 6 characters";
        break;
      case "confirm":
        if (!value) return "Please confirm password";
        if (value !== form.password) return "Passwords do not match";
        break;
      case "dob":
        if (!value) return "Date of birth is required";
        break;
    }
    return null;
  };

  /* ---------- availability checks ---------- */
  const checkAvailability = async (
    field: "username" | "email" | "phone"
  ) => {
    const value = form[field];
    if (!value) return;

    const res = await fetch(
      `/api/auth/check-${field}?${field}=${encodeURIComponent(value)}`
    );
    const data = await res.json();

    if (field === "phone") {
      if (!data.allowed) {
        setErrors(e => ({ ...e, phone: "Phone already registered" }));
      }
    } else {
      if (!data) {
        setErrors(e => ({
          ...e,
          [field]:
            field === "username"
              ? "Username already exists"
              : "Email already exists",
        }));
      }
    }
  };

  const update = (field: Field, value: string) => {
    setForm(f => ({ ...f, [field]: value }));
    if (!touched[field]) return;

    setErrors(e => ({ ...e, [field]: validateField(field, value) || "" }));
    setServerError(null);
  };

  const onBlur = async (field: Field) => {
    setTouched(t => ({ ...t, [field]: true }));

    const err = validateField(field);
    setErrors(e => ({ ...e, [field]: err || "" }));

    if (!err && (field === "username" || field === "email" || field === "phone")) {
      await checkAvailability(field);
    }
  };

  /* ---------- submit ---------- */
  const submit = async (e: React.FormEvent) => {
    e.preventDefault();

    const newErrors: Errors = {};
    (Object.keys(form) as Field[]).forEach(f => {
      const err = validateField(f);
      if (err) newErrors[f] = err;
    });

    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      setTouched(Object.fromEntries(Object.keys(form).map(f => [f, true])));
      return;
    }

    setLoading(true);
    try {
      const res = await fetch("/api/auth/register-employee", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          username: form.username,
          firstName: form.firstName,
          lastName: form.lastName,
          email: form.email,
          phone: form.phone,
          password: form.password,
          idOffice: 1,
          typeOfEmpl: Number(form.typeOfEmpl),
          dateOfBirth: form.dob,
        }),
      });

      if (!res.ok) {
        const text = await res.text();
        throw new Error(text || "Failed to create employee");
      }

      alert("Employee created successfully");
      setForm({
        username: "",
        firstName: "",
        lastName: "",
        email: "",
        phone: "",
        password: "",
        confirm: "",
        dob: "",
        typeOfEmpl: "1",
      });
      setTouched({});
    } catch (e: any) {
      setServerError(e.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="center-page">
      <div className="card form-card wide-form">
        <h2>Create Employee</h2>

        <form onSubmit={submit} style={{ display: "grid", gap: 10 }}>
          {(
            [
              ["username", "Username"],
              ["firstName", "First name"],
              ["lastName", "Last name"],
              ["email", "Email"],
              ["phone", "Phone"],
              ["password", "Password"],
              ["confirm", "Confirm password"],
            ] as [Field, string][]
          ).map(([f, label]) => (
            <div key={f}>
              <input
                type={f === "password" || f === "confirm" ? "password" : "text"}
                placeholder={label}
                value={form[f]}
                onChange={e => update(f, e.target.value)}
                onBlur={() => onBlur(f)}
              />
              {touched[f] && errors[f] && (
                <small className="error">{errors[f]}</small>
              )}
            </div>
          ))}

          {/* DATE OF BIRTH */}
          <div>
            <input
              type="date"
              value={form.dob}
              onChange={e => update("dob", e.target.value)}
              onBlur={() => onBlur("dob")}
            />
            {touched.dob && errors.dob && (
              <small className="error">{errors.dob}</small>
            )}
          </div>

          {/* TYPE OF EMPLOYEE */}
          <div>
            <select
              value={form.typeOfEmpl}
              onChange={e => update("typeOfEmpl", e.target.value)}
              style={{
                width: "100%",
                padding: "10px 14px",
                borderRadius: 12,
                border: "1px solid #ddd",
                background: "white",
              }}
            >
              <option value="1">Employee</option>
              <option value="0">Admin</option>
            </select>
          </div>

          <button disabled={loading}>
            {loading ? "Creating..." : "Create Employee"}
          </button>

          {serverError && <p className="error">{serverError}</p>}
        </form>
      </div>
    </div>
  );
}
