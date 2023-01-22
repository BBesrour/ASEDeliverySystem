export default class Box {
  readonly id: string | null;
  name: string;
  address: string;
  numberOfItems: string;
  readonly assignedTo: string;
  readonly assignedCustomers: string[];
  readonly key: string;

  constructor(
    id: string | null,
    name: string,
    address: string,
    numberOfItems: string,
    assignedTo: string,
    assignedCustomers: string[],
    key: string
  ) {
    this.id = id;
    this.name = name;
    this.address = address;
    this.numberOfItems = numberOfItems;
    this.assignedTo = assignedTo;
    this.assignedCustomers = assignedCustomers;
    this.key = key;
  }

  static fromJson(json: any): Box {
    return new Box(
      json.id,
      json.name,
      json.address,
      json.numberOfItems,
      json.assignedTo,
      json.assignedCustomers,
      json.key
    );
  }
}
