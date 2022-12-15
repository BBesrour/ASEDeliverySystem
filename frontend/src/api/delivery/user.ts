import Client from "../Client";
import {deliveryServiceUrl} from "../config";
import User from "../model/User";

const client = new Client(`${deliveryServiceUrl}/user`);

function deserializeUserList(json: any): User[] {
    return json.map(User.fromJson);
}

export async function getAllUsers(): Promise<User[]> {
    return deserializeUserList(await client.getRequest("/", {}));
}

export async function deleteUser(id: string): Promise<void> {
    await client.deleteRequest(`/${id}`, {});
}

export async function updateUser(user: User): Promise<void> {
    await client.putRequest(`/${user.id}`, user);
}

export async function createUser(user: User): Promise<void> {
    await client.postRequest("/", user);
}