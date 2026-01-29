import { useState } from "react";
import { useNavigate } from "react-router-dom";

type Field =
  | "username"
  | "firstName"
  | "lastName"
  | "email"
  | "phone"
  | "password"
  | "confirm";

type Errors = Partial<Record<Field, string>>;

export default function RegisterPage() {
  const nav = useNavigate();

  const [form, setForm] = useState({
    username: "",
    firstName: "",
    lastName: "",
    email: "",
    phone: "",
    password: "",
    confirm: "",
  });

  const [errors, setErrors] = useState<Errors>({});
  const [touched, setTouched] = useState<Partial<Record<Field, boolean>>>({});
  const [serverError, setServerError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

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
        if (!value) return "Please confirm your password";
        if (value !== form.password) return "Passwords do not match";
        break;
    }
    return null;
  };

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
      // backend -> { allowed: boolean }
      if (!data.allowed) {
        setErrors(e => ({
          ...e,
          phone: "Phone already registered",
        }));
      }
    } else {
      // username / email -> boolean
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

  const submit = async (e: React.FormEvent) => {
    e.preventDefault();

    const allTouched: any = {};
    (Object.keys(form) as Field[]).forEach(f => (allTouched[f] = true));
    setTouched(allTouched);

    const newErrors: Errors = {};
    (Object.keys(form) as Field[]).forEach(f => {
      const err = validateField(f);
      if (err) newErrors[f] = err;
    });

    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }

    setLoading(true);
    try {
      const res = await fetch("/api/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          username: form.username,
          firstName: form.firstName,
          lastName: form.lastName,
          email: form.email,
          phone: form.phone,
          password: form.password,
        }),
      });

      if (!res.ok) {
        const data = await res.json();
        throw new Error(data.message || data.error);
      }

      nav("/login?type=CLIENT");
    } catch (e: any) {
      setServerError(e.message);
    } finally {
      setLoading(false);
    }
  };

  const hasErrors = Object.values(errors).some(Boolean);

  return (
    <div className="auth-center">
      <div className="card form-card register-card">
        <button
          onClick={() => nav("/")}
          style={{
            padding: 0,
            marginBottom: 8,
            border: "none",
            background: "transparent",
            color: "#5b6cff",
            fontWeight: 600,
          }}
        >
          ← Back
        </button>

        <h2>Register as Client</h2>

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

          <button disabled={loading || hasErrors}>
            {loading ? "Registering..." : "Register"}
          </button>

          {serverError && <p className="error">{serverError}</p>}
        </form>
      </div>
    </div>
  );
}
