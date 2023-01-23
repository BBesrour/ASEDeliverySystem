export default class Box {
  readonly id: string | null;
  name: string;
  address: string;
  numberOfItems: number;
  readonly assignedTo: string;
  readonly assignedCustomer: string;

  constructor(
    id: string | null,
    name: string,
    address: string,
    numberOfItems: number,
    assignedTo: string,
    assignedCustomer: string
  ) {
    this.id = id;
    this.name = name;
    this.address = address;
    this.numberOfItems = numberOfItems;
    this.assignedTo = assignedTo;
    this.assignedCustomer = assignedCustomer;
  }

  static fromJson(json: any): Box {
    return new Box(
      json.id,
      json.name,
      json.address,
      json.numberOfItems,
      json.assignedTo,
      json.assignedCustomer
    );
  }
}
