export type Role = "CLIENT" | "EMPLOYEE" | "ADMIN";

export type AuthState = {
  role: Role;
  userId: number;
  clientId?: number | null;
  employeeId?: number | null;
  username: string;
  fullName?: string;
};

const KEY = "logistics_auth";

export function setAuth(state: AuthState) {
  localStorage.setItem(KEY, JSON.stringify(state));
}

export function getAuth(): AuthState | null {
  const raw = localStorage.getItem(KEY);
  return raw ? (JSON.parse(raw) as AuthState) : null;
}

export function clearAuth() {
  localStorage.removeItem(KEY);
}
