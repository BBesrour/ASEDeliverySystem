class CardContent:
    def __init__(self, token: str):
        self.token = token

    def __str__(self):
        return f"Token: {self.token}"


class InvalidCardContent(CardContent):
    def __init__(self):
        super().__init__(token="INVALID")
