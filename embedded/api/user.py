class User:
    def __init__(self, uid: str, email: str, role: str):
        self.id = uid
        self.email = email
        self.role = role

    def __str__(self):
        return f"User(id={self.id}, email={self.email}, role={self.role})"
