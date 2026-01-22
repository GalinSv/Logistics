export type PackageDto = {
  id: number;
  weight: number;
  addressToGo: string | null;
  dateOfDelivery: string | null; // ISO string от backend-а
  toOffice: boolean;

  senderId: number;
  senderUsername: string;
  receiverId: number;
  receiverUsername: string;

  employeeId: number;
  employeeUsername: string;

  pricingId: number;
  pricePerKilogram: number;
  pricingToAddress: number;

  status?: "CREATED" | "IN_TRANSIT" | "AT_OFFICE" | "DELIVERED" | "CANCELLED";

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


