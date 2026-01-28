export type PackageDto = {
  idPackage: number;
  idSenderClient: number;
  idReceiverClient: number;
  idEmployee: number;
  weight: number;
  addressToGo: string | null;
  toOffice: boolean;
  dateOfDelivery: string | null; // идва като "2026-01-05"
  price: number | null;
};

export type ClientDto = {
  id: number;
  userId: number;
  username: string;
  firstName: string;
  lastName: string;
  email: string;
};

export type EmployeeDto = {
  id: number;
  typeOfEmpl: number;
  userId: number;
  username: string;
  companyId: number;
  companyName: string;
  officeId: number;
  officeAddress: string;
};

export type PricingDto = {
  id: number;
  pricePerKilogram: number;
  toAddress: number; // <--- важно: pricing endpoint връща toAddress
};

export type EmployeeReportDto = {
  employeeId: number;
  firstName: string;
  lastName: string;
  phone: string;
  email: string;
  officeAddress: string;
  typeOfEmpl: number;
};

export type ClientReportDto = {
  clientId: number;
  firstName: string;
  lastName: string;
  phone: string;
  email: string;
};

export type PackageReportDto = {
  packageId: number;

  senderPhone: string;
  receiverPhone: string;
  employeePhone: string;

  weight: number;
  toOffice: boolean;

  addressToGo: string | null;
  officeAddress: string | null;

  dateOfDelivery: string | null;
  status: string;
};

