import Client from "../Client";
import {deliveryServiceUrl} from "../config";
import Delivery from "../../model/Delivery";

const client = new Client(`${deliveryServiceUrl}/deliveries`);

function deserializeDeliveryList(json: any): Delivery[] {
    return json.map(Delivery.fromJson);
}

export async function getDeliveries(): Promise<Delivery[]> {
    return deserializeDeliveryList(await client.getRequest('/'));
}

export async function getDelivery(id: string): Promise<Delivery> {
    return Delivery.fromJson(await client.getRequest(`/${id}`));
}

export async function getActiveDeliveries(customerId: string): Promise<Delivery[]> {
    return deserializeDeliveryList(await client.getRequest('/active', {customer: customerId}));
}

export async function getInactiveDeliveries(customerId: string): Promise<Delivery[]> {
    return deserializeDeliveryList(await client.getRequest('/inactive', {customer: customerId}));
}

export async function createDelivery(delivery: Delivery): Promise<Delivery> {
    return Delivery.fromJson(await client.postRequest('/', {}, {...delivery, active: true}));
}

export async function updateDelivery(delivery: Delivery): Promise<Delivery> {
    return Delivery.fromJson(await client.putRequest(`/${delivery.id}`, {}, delivery));
}

export async function deleteDelivery(deliveryId: string): Promise<void> {
    await client.deleteRequest('/deliveries/' + deliveryId);
}