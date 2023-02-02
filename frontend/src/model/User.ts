export type UserRole = "ROLE_CUSTOMER" | "ROLE_DELIVERER" | "ROLE_DISPATCHER";

export default class User {
  readonly id: string | null;
  readonly email: string;
  readonly password: string | null;
  readonly role: UserRole;
  readonly token: string | null;

  constructor(
    id: string | null,
    email: string,
    password: string | null,
    role: UserRole,
    token: string | null
  ) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.role = role;
    this.token = token;
  }

  static fromJson(json: any): User {
    console.log(json.token);
    return new User(json.id, json.email, null, json.role, json.token);
  }
}
