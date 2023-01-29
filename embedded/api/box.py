class Box:
    id: str
    name: str
    assigned_to: str
    assigned_customer: str
    assigned_by: str
    address: str

    def __init__(self, id: str, name: str, assigned_to: str, assigned_customer: str, assigned_by: str, address: str):
        self.id = id
        self.name = name
        self.assigned_to = assigned_to
        self.assigned_customer = assigned_customer
        self.assigned_by = assigned_by
        self.address = address

    def __str__(self):
        return f"Box(id={self.id}, name={self.name}, assigned_to={self.assigned_to}, assigned_customer={self.assigned_customer}, assigned_by={self.assigned_by}, address={self.address})"

    @staticmethod
    def from_json(json: dict):
        return Box(json["id"], json["name"], json["assignedTo"], json["assignedCustomer"], json["assignedBy"], json["address"])