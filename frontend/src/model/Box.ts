export default class Box {
  readonly id: string | null;
  name: string;
  address: string;
  readonly assignedCustomer: string;

  constructor(
    id: string | null,
    name: string,
    address: string,
    assignedCustomer: string
  ) {
    this.id = id;
    this.name = name;
    this.address = address;
    this.assignedCustomer = assignedCustomer;
  }
  static fromJson(json: any): Box {
    return new Box(json.id, json.name, json.address, json.assignedCustomer);
  }
}
