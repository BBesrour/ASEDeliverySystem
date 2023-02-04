import Client from "../Client";
import {authenticationServiceUrl} from "../config";

const client = new Client(authenticationServiceUrl, '');

export async function login(email: string, password: string): Promise<{ id: string, role: string, accessToken: string }> {
    return await client.postRequest('/login', {}, {email, password});
}