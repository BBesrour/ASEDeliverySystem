export type UserRole = "ROLE_CUSTOMER" | "ROLE_DELIVERER" | "ROLE_DISPATCHER";

export default class User {
    readonly id: string | null;
    readonly email: string;
    readonly password: string | null;
    readonly role: UserRole;
    
    constructor(id: string | null, email: string, password: string | null, role: UserRole) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    static fromJson(json: any): User {
        return new User(json.id, json.email, null, json.role);
    }
}