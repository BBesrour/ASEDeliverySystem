import Client from "../Client";
import { deliveryServiceUrl } from "../config";
import Box from "../../model/Box";

const client = new Client(`${deliveryServiceUrl}/boxes`);

function deserializeBoxList(json: any): Box[] {
  return json.map(Box.fromJson);
}

export async function getBoxes(): Promise<Box[]> {
  return deserializeBoxList(await client.getRequest("/"));
}

export async function getDelivererBoxes(delivererId: string): Promise<Box[]> {
  return deserializeBoxList(
    await client.getRequest(`/deliverer/${delivererId}`)
  );
}

export async function createBox(box: Box): Promise<Box> {
  return Box.fromJson(await client.postRequest("/", {}, box));
}

export async function updateBox(box: Box): Promise<Box> {
  return Box.fromJson(await client.putRequest(`/${box.id}`, {}, box));
}

export async function deleteBox(id: string): Promise<null> {
  return await client.deleteRequest(`/${id}`, {});
}

export async function updateBoxStatus(
  boxId: string,
  status: string
): Promise<Box> {
  return Box.fromJson(
    await client.putRequest(`/${boxId}/update_status`, { status }, {})
  );
}
