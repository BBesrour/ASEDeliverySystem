class CardContent:
    def __init__(self, card_id: str, token: str):
        self.card_id = card_id
        self.token = token

    def __str__(self):
        return f"Card ID: {self.card_id}, Token: {self.token}"


class InvalidCardContent(CardContent):
    def __init__(self):
        super().__init__(card_id="INVALID", token="INVALID")
